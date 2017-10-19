package com.guaiwa.guaiguaiteach.imageloader;

import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

/**
 * Created by 80151689 on 2017-10-19.
 */

public class FrescoUtil {

    public static void showFixSizeImage(final SimpleDraweeView draweeView, final Uri uri) {
        final int width = draweeView.getLayoutParams().width;
        final int height = draweeView.getLayoutParams().height;
        if (width <= 0 || height <= 0 || TextUtils.isEmpty(uri.getPath())) {
            return;
        }
        ImageDecodeOptions decodeOptions = ImageDecodeOptions.newBuilder()
                .setForceStaticImage(true)
                .build();
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .setImageDecodeOptions(decodeOptions)
                .build();
        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(draweeView.getController())
                .build();
        draweeView.setController(draweeController);
    }
}
