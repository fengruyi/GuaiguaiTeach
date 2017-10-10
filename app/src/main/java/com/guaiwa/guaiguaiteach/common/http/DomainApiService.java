package com.guaiwa.guaiguaiteach.common.http;


import com.guaiwa.guaiguaiteach.config.UrlConfig;

/**
 * Created by 80151689 on 2016-11-25.
 * 调用服务器api库
 */
public interface DomainApiService {
    //域名xxx下的所有接口
    String HOST_URL = UrlConfig.ENV.host;

    /*
    当POST请求时，@FormUrlEncoded和@Field简单的表单键值对。两个需要结合使用，否则会报错

    @Query请求参数。无论是GET或POST的参数都可以用它来实现
     */
}
