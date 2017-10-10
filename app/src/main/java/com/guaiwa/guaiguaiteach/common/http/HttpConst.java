package com.guaiwa.guaiguaiteach.common.http;

public class HttpConst {
    // 接口请求成功，根据返回数据的code标识确定业务响应状态（301用于兼容域名或服务迁移情况，亦表示请求成功）
    public static final int HTTP_CODE_200 = 200;
    public static final int HTTP_CODE_301 = 301;
    // 对于获取数据，表示服务端的数据无变化，使用本地缓存数据或本地不刷新；对于更新请求，表示服务器更新无变化，可能是请求的参数为空
    public static final int HTTP_CODE_204 = 204;
    // 服务器异常
    public static final int HTTP_CODE_500 = 500;
    public static final int HTTP_CODE_503 = 503;
    // 请求接口超时
    public static final int HTTP_CODE_504 = 504;
    // 错误的请求地址或请求地址非法
    public static final int HTTP_CODE_400 = 400;
    public static final int HTTP_CODE_404 = 404;
    // 请求登录
    public static final int HTTP_CODE_401 = 401;
    // POST请求正在处理，重复请求
    public static final int HTTP_CODE_202 = 202;


    // 操作系统平台，如android、ios，此处仅为了后续兼容其他平台，目前均为android
    public static final String PLATFORM = "platform";

    // 系统的user-agent标识
    public static final String UA = "ua";

    // 机型名
    public static final String MODEL = "modal";

    // 屏幕分辨率，使用x分隔，如800x600
    public static final String SCREEN_SIZE = "screen_size";

    // 操作系统版本
    public static final String OS = "os";

    // 客户端软件版本
    public static final String APK_VERSION = "s_version";

    // 手机IMEI号
    public static final String IMEI = "imei";

    // 网络类型，统一小写，如wifi
    public static final String NETWORK_TYPE = "networktype";

    // 图片清晰度
    public static final String IMAGE_TYPE = "imageTyep";

    // 登录信息
    public static final String UKEY = "ukey";

    // 登录信息
    public static final String TOKEN = "token";

}