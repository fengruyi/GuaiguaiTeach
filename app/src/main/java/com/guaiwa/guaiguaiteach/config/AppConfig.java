package com.guaiwa.guaiguaiteach.config;

import android.content.Context;
import android.util.DisplayMetrics;

import com.guaiwa.guaiguaiteach.common.utils.PhoneInfo;

/**
 * Created by 80151689 on 2017-10-18.
 */

public class AppConfig {
    public static long FAST_CLICK_INTERVAL = 500;//million
    public static long MIN_AVAILABLE_SPACE = 10 * 1024 * 1024;

    public static void initConfig(Context context) {
        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        PhoneInfo.screenWidth = metric.widthPixels < metric.heightPixels ? metric.widthPixels : metric.heightPixels;  // 屏幕宽度（像素）
        PhoneInfo.screenHeight = metric.heightPixels > metric.widthPixels ? metric.heightPixels : metric.widthPixels;  // 屏幕高度（像素）
    }
}
