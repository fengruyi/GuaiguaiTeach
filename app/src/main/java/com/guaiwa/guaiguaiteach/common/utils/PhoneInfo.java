package com.guaiwa.guaiguaiteach.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by 80151689 on 2017-10-17.
 */

public class PhoneInfo {

    public static int screenWidth = 1080;
    public static int screenHeight = 1920;

    /**
     * 获取手机屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取手机分辨率高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getApkVersionCode(Context context) {
        int versionCode = -1;
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(context.getPackageName(), "can't get the versionCode.");
        }
        return versionCode;
    }

    /**
     * 获取客户端版本号
     *
     * @param context
     * @return
     */
    public static String getApkVersion(Context context) {
        String version = "";
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(context.getPackageName(), "can't get the versionCode and versionName.");
        }
        return version;
    }

}
