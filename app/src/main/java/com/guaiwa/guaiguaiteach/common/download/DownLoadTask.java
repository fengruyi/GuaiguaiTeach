package com.guaiwa.guaiguaiteach.common.download;

import android.util.Log;


import com.guaiwa.guaiguaiteach.common.http.DomainApiService;
import com.guaiwa.guaiguaiteach.common.http.HttpResultSubscriber;
import com.guaiwa.guaiguaiteach.common.http.RetrofitClient;

import okhttp3.ResponseBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 80151689 on 2016-12-25.
 */
public class DownLoadTask implements Runnable {
    private final static String TAG = DownLoadTask.class.getSimpleName();
    private String fileUrl;//下载地址
    private String fileSavePath;//文件保存路径名
    private DownLoadListener downLoadListener;


    public interface DownLoadListener {

        void updateProgress(long total, long current);

        void onSuccess();

        void onFailue(Throwable e);
    }

    public DownLoadTask(String url, String savePath, DownLoadListener listener) {
        this.fileUrl = url;
        this.fileSavePath = savePath;
        downLoadListener = listener;
    }

    public String getFileUrl() {
        return fileUrl;
    }


    @Override
    public void run() {
        RetrofitClient.getInstance()
                .getApiService(DomainApiService.class)
                .downloadFile(fileUrl)
                .subscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, Boolean>() {
                    @Override
                    public Boolean call(ResponseBody responseBody) {
                        final long totalLenth = responseBody.contentLength();
                        CommonSaveFileTask saveFileTask = CommonSaveFileTask.newInstance();
                        boolean flag = saveFileTask.save(responseBody.byteStream(), fileSavePath, totalLenth, new CommonSaveFileTask.ProgressHandler() {
                            @Override
                            public void updateProgress(long total, final long current) {
                                if (downLoadListener != null) {
                                    downLoadListener.updateProgress(totalLenth, current);
                                }
                            }
                        });
                        responseBody.close();

                        return flag;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultSubscriber<Boolean>() {
                    @Override
                    protected void onSuccess(Boolean aBoolean) {
                        Log.d(TAG, "file save onSuccess:");
                        if (downLoadListener != null) {
                            if(aBoolean) {
                                downLoadListener.onSuccess();
                            }else{
                                downLoadListener.onFailue(null);
                            }
                        }

                    }

                    @Override
                    protected void onFailue(Throwable e) {
                        super.onFailue(e);
                        DownloadManager.getInstance().removeDownload(fileUrl);
                        if (downLoadListener != null) {
                            downLoadListener.onFailue( e);
                        }
                    }
                });
    }


}
