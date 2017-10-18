package com.guaiwa.guaiguaiteach.common.download;

import android.annotation.TargetApi;
import android.os.Build;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 80151689 on 2016-12-25.
 */
public class DownloadManager {

    private static volatile DownloadManager instance;
    private final List<String> downloadInfoList = new ArrayList<String>();
    private DownloadThreadPool sDownloadThreadPool;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;



    // 普通文件下载
    public synchronized void startFileDownload(String url, String savePath, DownLoadTask.DownLoadListener fileDownLoadListener) {
        downloadInfoList.add(url);
        DownLoadTask fileDownLoadTask = new DownLoadTask(url, savePath, fileDownLoadListener);
        getThreadExecutor().execute(fileDownLoadTask);
    }


    /*package*/
    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    public boolean hasDownloadindTask() {
        return downloadInfoList.size() > 0;
    }

    public boolean isDownloading(String url) {
        for (String downloadUrl : downloadInfoList) {
            if (downloadUrl.equals(url)) {
                return true;
            }
        }
        return false;
    }

    public void stopDownload(int index) {

    }

    public void stopDownload(String url) {

    }

    public void stopAllDownload() {

    }

    public void removeDownload(int index) {

    }

    public void removeDownload(String url) {
        downloadInfoList.remove(url);
    }

    public synchronized ThreadPoolExecutor getThreadExecutor() {
        if (sDownloadThreadPool == null || sDownloadThreadPool.isShutdown()) {
            sDownloadThreadPool = new DownloadThreadPool(CORE_POOL_SIZE);
        }
        return sDownloadThreadPool;
    }

    public static class DownloadThreadPool extends ThreadPoolExecutor {

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        public DownloadThreadPool(int poolSize) {
            super(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingDeque<Runnable>(),
                    Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());
        }
    }
}
