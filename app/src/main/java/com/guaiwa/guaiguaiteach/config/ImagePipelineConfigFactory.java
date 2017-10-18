package com.guaiwa.guaiguaiteach.config;

import android.content.Context;
import android.os.Environment;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.util.ByteConstants;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.guaiwa.guaiguaiteach.common.http.OkHttpNetworkFetcher;
import com.guaiwa.guaiguaiteach.common.http.RetrofitClient;


import java.io.File;

/**
 * Created by 80151689 on 2016-07-19.
 */
public class ImagePipelineConfigFactory {
    private static final String IMAGE_PIPELINE_CACHE_DIR = "cache/";

    private static ImagePipelineConfig sImagePipelineConfig;

    private static final int MAX_HEAP_SIZE = (int) Runtime.getRuntime().maxMemory();

    public static final int MAX_DISK_CACHE_SIZE = 300 * ByteConstants.MB;
    public static final int MAX_MEMORY_CACHE = 50 * ByteConstants.MB;
    public static final int MAX_MEMORY_CACHE_SIZE = (MAX_HEAP_SIZE / 3) > MAX_MEMORY_CACHE ? MAX_MEMORY_CACHE : MAX_HEAP_SIZE / 3;

    public static ImagePipelineConfig getImagePipelineConfig(Context context) {
        if (sImagePipelineConfig == null) {
            ImagePipelineConfig.Builder configBuilder = ImagePipelineConfig.newBuilder(context);
            configureCaches(configBuilder, context);
            sImagePipelineConfig = configBuilder
                    .setDownsampleEnabled(true)
                    .setNetworkFetcher(new OkHttpNetworkFetcher(RetrofitClient.getInstance().getOkHttpClient()))
                    .build();
        }
        return sImagePipelineConfig;
    }


    private static void configureCaches(ImagePipelineConfig.Builder configBuilder, Context context) {
        final MemoryCacheParams bitmapCacheParams = new MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE,                     // Max entries in the cache
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in eviction queue
                Integer.MAX_VALUE,                     // Max length of eviction queue
                Integer.MAX_VALUE);                    // Max cache entry size
        configBuilder
                .setBitmapMemoryCacheParamsSupplier(
                        new Supplier<MemoryCacheParams>() {
                            public MemoryCacheParams get() {
                                return bitmapCacheParams;
                            }
                        })
                .setMainDiskCacheConfig(DiskCacheConfig.newBuilder(context)
                        .setBaseDirectoryPath(getExternalCacheDir(context))
                        .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                        .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                        .setMaxCacheSizeOnLowDiskSpace(MAX_MEMORY_CACHE_SIZE / 3)
                        .setMaxCacheSizeOnVeryLowDiskSpace(MAX_MEMORY_CACHE_SIZE / 5)
                        .build());
    }

    public static File getExternalCacheDir(final Context context) {
        if (hasSDCard())
            return new File(FileConstant.TEMP_STORAGE_PATH);
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return createFile(Environment.getExternalStorageDirectory().getPath() + cacheDir, "");
    }

    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    public static File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName);
    }


}
