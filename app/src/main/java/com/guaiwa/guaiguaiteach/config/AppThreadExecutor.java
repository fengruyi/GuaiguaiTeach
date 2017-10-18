package com.guaiwa.guaiguaiteach.config;

import android.os.Process;

import com.facebook.imagepipeline.core.PriorityThreadFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by 80151689 on 2017-07-08.
 * 整个app线程池对象，项目里使用的的线程池引用引单例即可
 * Provides one thread pool for the CPU-bound operations and another thread pool for the
 */
public class AppThreadExecutor {
    private final Executor mIoBoundExecutor;//涉及io操作线程池
    private final Executor mDecodeExecutor;//图片解码线程池
    private final Executor mBackgroundExecutor;//后台工作线程池
    private final int THREAD_NUM = Runtime.getRuntime().availableProcessors();

    private static final AppThreadExecutor sInstance = new AppThreadExecutor();

    public static AppThreadExecutor getInstance() {
        return sInstance;
    }

    private AppThreadExecutor() {
        ThreadFactory backgroundPriorityThreadFactory =
                new PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);

        mIoBoundExecutor = Executors.newFixedThreadPool(THREAD_NUM);
        mDecodeExecutor = Executors.newFixedThreadPool(
                THREAD_NUM,
                backgroundPriorityThreadFactory);
        mBackgroundExecutor = Executors.newFixedThreadPool(
                THREAD_NUM,
                backgroundPriorityThreadFactory);
    }

    public Executor forLocalStorageWrite() {
        return mIoBoundExecutor;
    }

    public Executor forDecode() {
        return mDecodeExecutor;
    }

    public Executor forBackgroundTasks() {
        return mBackgroundExecutor;
    }

    /**
     * (普通无界线程池，可以进行自动回收无用线程)
     *
     * @param task
     */
    public void executeNormalTask(Runnable task) {
        mBackgroundExecutor.execute(task);
    }

    public void executeUploadTask(Runnable task) {
        mIoBoundExecutor.execute(task);
    }
}
