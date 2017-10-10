package com.guaiwa.guaiguaiteach.common.http;



import com.guaiwa.guaiguaiteach.common.utils.TimeSynCheck;
import com.guaiwa.guaiguaiteach.config.UrlConfig;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.HttpDate;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by 80151689 on 2016-11-25.
 * Retrofit的简单封装
 */
public class RetrofitClient {
    private static final String TAG = RetrofitClient.class.getSimpleName();
    private OkHttpClient okHttpClient;
    long minResponseTime = Long.MAX_VALUE;
    /**
     * 项目服务器域名多个，用域名区分
     */
    private Map<String, Retrofit> retrofitMap = new HashMap<String, Retrofit>();
    private Map<String, Object> apiServiceMap = new HashMap<String, Object>();

    {
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //拦截请求添加公共参数
                        Request request = chain.request();
                        String url = request.url().toString();
                        //对指定域名添加公共参数
                        if (isAddGlobalUrl(url)) {
                            HttpUrl httpUrl = GlobalParams.addGlobalParams(request.url().newBuilder());
                            request = request.newBuilder().url(httpUrl)/*.cacheControl(CacheControl.FORCE_NETWORK)*/.build();
                        }
//                        LogUtil.d(TAG, "after interceptor:" + request.url().toString());
                        long startTime = System.nanoTime();
                        Response response = chain.proceed(request);
                        long responseTime = System.nanoTime() - startTime;
                        if (response != null) {
                            //如果这一次的请求响应时间小于上一次，则更新本地维护的时间
                            if (responseTime < minResponseTime || response.code() == 400) {
                                try {
                                    String contentType = response.header("Content-type");
                                    if (contentType != null && (contentType.contains("application/json") || contentType.contains("text/html") || contentType.contains("application/x-protobuf"))) {
                                        String gmt = response.header("Date");
                                        Date date = HttpDate.parse(gmt);
                                        TimeSynCheck.getInstance().initServerTime(date.getTime());
                                        minResponseTime = responseTime;
//                                        LogUtil.d(TAG, "get server time:" + date.getTime()+",host:"+request.url().host()+",code:"+response.code());
                                    }
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return response;
                    }
                })
                .connectTimeout(7, TimeUnit.SECONDS)//设置超时时间 120s
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
    }

    private boolean isAddGlobalUrl(String url) {
        return true;
    }

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    private RetrofitClient() {
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public <T> T getApiService(final Class<T> service) {
        String className = service.getSimpleName();
        if (apiServiceMap.containsKey(className)) {
            return (T) apiServiceMap.get(className);
        }
        String host_url = null;
        try {
            host_url = service.getFields()[0].get("HOST_URL").toString();
        } catch (Exception e) {
            host_url = UrlConfig.ENV.host;
            e.printStackTrace();
        }
        T tService = getRetrofitByHostName(host_url).create(service);
        apiServiceMap.put(className, tService);
        return tService;
    }

    private Retrofit buildRetrofit(String hostName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(hostName)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//与rxjava结合使用
                .addConverterFactory(ScalarsConverterFactory.create())//直接返回字符串
//                .addConverterFactory(CustomWireConverterFactory.create())//pb解释
                .build();
        retrofitMap.put(hostName, retrofit);
        return retrofit;
    }

    private Retrofit getRetrofitByHostName(String hostName) {
        if (retrofitMap.containsKey(hostName)) {
            return retrofitMap.get(hostName);
        } else {
            return buildRetrofit(hostName);
        }
    }

}
