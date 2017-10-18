package com.guaiwa.guaiguaiteach;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.guaiwa.guaiguaiteach.config.AppConfig;
import com.guaiwa.guaiguaiteach.config.ImagePipelineConfigFactory;

/**
 * Created by 80151689 on 2017-10-17.
 */

public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        AppConfig.initConfig(context);
        Fresco.initialize(context, ImagePipelineConfigFactory.getImagePipelineConfig(context));
    }
}
