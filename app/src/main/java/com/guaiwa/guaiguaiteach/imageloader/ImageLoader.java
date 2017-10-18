package com.guaiwa.guaiguaiteach.imageloader;

import android.net.Uri;
import android.support.annotation.WorkerThread;

import java.io.File;

/**
 * Created by 80151689 on 2017-03-14.
 */
public interface ImageLoader {
    void loadImage(Uri uri, String filePath, Callback callback);


    void prefetch(Uri uri, Callback callback);

    interface Callback {

        void onSuccess(File image);

        @WorkerThread
        void onProgress(int progress);

        @WorkerThread
        void onFail(Throwable e);
    }
}