package com.guaiwa.guaiguaiteach.common.utils;

import android.util.Log;


/**
 * Created by 80151689 on 2017-10-17.
 */

public class Logger {
    public static boolean debug = true;
    public static void d(String tag,String msg){
        if(debug){
            Log.d(tag, "[app logger]"+msg);
        }
    }

}
