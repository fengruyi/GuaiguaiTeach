package com.guaiwa.guaiguaiteach.config;

import android.os.Environment;

/**
 * Created by 80151689 on 2017-10-17.
 */

public class FileConstant {

    public static final String APP_DIR = "/guaiguai/";
    public static final String STORAGE_PATH = Environment.getExternalStorageDirectory() + APP_DIR;
    public static final String TEMP_STORAGE_PATH = STORAGE_PATH+"TEMP/";
    public static final String APK_PATH = STORAGE_PATH+"myapp.apk";



}
