package com.guaiwa.guaiguaiteach.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.WorkerThread;

import com.facebook.common.file.FileUtils;
import com.facebook.common.internal.Closeables;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.memory.PooledByteBufferInputStream;
import com.guaiwa.guaiguaiteach.common.utils.FileUtil;
import com.guaiwa.guaiguaiteach.common.utils.IOUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by 80151689 on 2017-07-08.
 */
public class ImageDownloadSubscriber
        extends BaseDataSubscriber<CloseableReference<PooledByteBuffer>> {
    private final File mTempFile;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;

    private volatile boolean mFinished;

    public ImageDownloadSubscriber(String filePath) {
        mTempFile = new File(filePath);
    }


    @Override
    public void onProgressUpdate(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
        if (!mFinished) {
            onProgress((int) (dataSource.getProgress() * 100));
        }
    }

    @Override
    protected void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
        if (!dataSource.isFinished() || dataSource.getResult() == null) {
            return;
        }
        // if we try to retrieve image file by cache key, it will return null
        // so we need to create a temp file, little bit hack :(
        PooledByteBufferInputStream inputStream = null;
        FileOutputStream outputStream = null;
        Bitmap bitmap = null;
        try {
            inputStream = new PooledByteBufferInputStream(dataSource.getResult().get());
            String path = mTempFile.getAbsolutePath();
            if (path.endsWith("gif") || path.endsWith("GIF")) {
                FileUtil.save2File(inputStream, mTempFile.getAbsolutePath());
            } else {
                outputStream = new FileOutputStream(mTempFile);
                bitmap = BitmapFactory.decodeStream(inputStream);
                bitmap.compress(mCompressFormat, 100, outputStream);
            }
            mFinished = true;
            onSuccess(mTempFile);
        } catch (Exception e) {
            onFail(e);
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
            IOUtil.closeQuietly(inputStream, outputStream);
        }
    }

    @Override
    protected void onFailureImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
        mFinished = true;
        onFail(new RuntimeException("onFailureImpl"));
    }

    @WorkerThread
    protected void onProgress(int progress) {
    }

    @WorkerThread
    protected void onSuccess(File image) {
    }

    @WorkerThread
    protected void onFail(Throwable t) {
    }

}
