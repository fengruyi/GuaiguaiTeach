package com.guaiwa.guaiguaiteach.picturview;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.facebook.common.file.FileUtils;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.request.ImageRequest;
import com.guaiwa.guaiguaiteach.R;
import com.guaiwa.guaiguaiteach.common.utils.PhoneInfo;
import com.guaiwa.guaiguaiteach.config.AppConfig;
import com.guaiwa.guaiguaiteach.config.AppThreadExecutor;
import com.guaiwa.guaiguaiteach.imageloader.FrescoImageLoader;
import com.guaiwa.guaiguaiteach.imageloader.ImageDownloadSubscriber;
import com.guaiwa.guaiguaiteach.imageloader.ImageLoader;
import com.guaiwa.guaiguaiteach.picturview.longimageview.ImageSource;
import com.guaiwa.guaiguaiteach.picturview.longimageview.ImageViewState;
import com.guaiwa.guaiguaiteach.picturview.longimageview.SubsamplingScaleImageView;

import java.io.File;


/**
 * Created by 80151689 on 2016-07-20.
 * 带有loading层的simpledraweee组合控件
 */
public class LoadingDraweeview extends FrameLayout {

    private final static String TAG = LoadingDraweeview.class.getSimpleName();
    private SubsamplingScaleImageView mScaleImageView;
    private ImageView imageView;
    private PhotoDraweeView mDraweeView;
    private ProgressBar loadingView;
    private TagImageInfo mTagImageInfo;
    private Context mContext;
    private Uri mUri;
    private FrescoImageLoader imageLoader = new FrescoImageLoader();
    private OnClickListener onClickListener;
    private String thumbPath;//缩略图，
    private String realImagePath;
    private Uri currentUri;//图片已经被加载显示完成的uri;

    public LoadingDraweeview(Context context, TagImageInfo tagImageInfo) {
        super(context);
        mTagImageInfo = tagImageInfo;
        mContext = context;
        if (mTagImageInfo.getPath().startsWith("http")) {
            thumbPath = tagImageInfo.getPath();
            if (tagImageInfo.isHasSrcCache()) {
                realImagePath = tagImageInfo.getSrcPath();
            } else if (thumbPath.contains(".thread.list")) {
                realImagePath = thumbPath.replace(".thread.list", ".thread.detail");
            }
            downloadImage(Uri.parse(thumbPath), false);
        } else {//显示本地图片
            File file = new File(mTagImageInfo.getPath());
            checkImageFile(file, Uri.fromFile(file));
        }
    }

    public LoadingDraweeview(Context context, String imagePath) {
        super(context);
        mContext = context;
        File file = new File(imagePath);
        checkImageFile(file, Uri.fromFile(file));
    }


    public Uri getUri() {
        return mUri;
    }


    //网络图片或者本地图片
    public void setImagePath(@NonNull String path) {
        downloadImage(Uri.parse(path), true);
    }

    private boolean isLongImage(int imgWidth, int imgHeight) {
        if ((imgWidth > PhoneInfo.screenWidth || imgHeight > PhoneInfo.screenHeight)) {
            if ((imgWidth / (float) imgHeight) > 2 || (imgHeight / (float) imgWidth) > 2) {
                return true;
            }
        }
        return false;
    }

    //根据图片信息决定使用哪个控件显示图片
    private void checkImageFile(File file, Uri uri) {
        if (file.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            // 确保图片不加载到内存
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            String type = options.outMimeType;
            if (isLongImage(options.outWidth, options.outHeight) && !"image/gif".equals(type)) {
                if (uri.toString().equals(thumbPath) && realImagePath != null) {
                    showImage(uri, true);
                    downloadImage(Uri.parse(realImagePath), true);
                } else {
                    showLongImage(file.getAbsolutePath());
                }
            } else {
                showImage(uri, false);
                if (uri.toString().equals(thumbPath) && realImagePath != null) {
                    downloadImage(Uri.parse(realImagePath), true);
                }
            }
        }
    }

    private void downloadImage(@NonNull final Uri uri, final boolean needShowLloadingView) {
        this.mUri = uri;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        if (mScaleImageView == null && mDraweeView == null) {
            imageView = new ImageView(mContext);
            imageView.setImageResource(R.mipmap.ic_launcher_round);
            addView(imageView, layoutParams);
        }
        if (needShowLloadingView) {
            loadingView = new ProgressBar(mContext);
            addView(loadingView, layoutParams);
        }

        imageLoader.prefetch(uri, new ImageLoader.Callback() {
            @Override
            public void onSuccess(final File image) {
                currentUri = uri;
                final String url = uri.toString();
                if (url.equals(mTagImageInfo.getSrcPath())) {
                    mTagImageInfo.setHasSrcCache(true);
                }
                post(new Runnable() {
                    @Override
                    public void run() {
                        removeView(imageView);
                        removeView(loadingView);
                        checkImageFile(image, uri);
                    }
                });

            }

            @Override
            public void onProgress(int progress) {

            }

            @Override
            public void onFail(final Throwable e) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        removeView(loadingView);
                    }
                });

            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        imageLoader.close();
    }

    private void showImage(final Uri uri, final boolean forceFillWidth) {
        if (mDraweeView == null) {
            mDraweeView = new PhotoDraweeView(mContext);
            addView(mDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            mDraweeView.setOnViewTapListener(new OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    if (onClickListener != null) {
                        onClickListener.onClick(view);
                    }
                }
            });
            final PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .setOldController(mDraweeView.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);
                            if (imageInfo == null) {
                                mDraweeView.setGestureEnable(true);
                                return;
                            }
                            if (forceFillWidth && imageInfo.getWidth() < imageInfo.getHeight()) {
                                mDraweeView.fillWidth(imageInfo.getWidth(), imageInfo.getHeight());
                            } else {
                                mDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
                                mDraweeView.setGestureEnable(true);
                            }
                        }

                        @Override
                        public void onFailure(String id, Throwable throwable) {
                            super.onFailure(id, throwable);
                        }
                    });
            mDraweeView.setController(controller.build());
        } else {
            //在同一个PhotoDraweeView更新图片时会闪烁，暂时只能是新增一个盖在上面代替旧的
            Matrix matrix = mDraweeView.getDrawMatrix();
            mDraweeView = new PhotoDraweeView(mContext);
            mDraweeView.setMatrix(matrix);
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) instanceof PhotoDraweeView) {
                    addView(mDraweeView, i + 1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    break;
                }
            }
            mDraweeView.setOnViewTapListener(new OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    if (onClickListener != null) {
                        onClickListener.onClick(view);
                    }
                }
            });
            final PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
            controller
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .setOldController(mDraweeView.getController())
                    .setControllerListener(new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);
                            if (imageInfo == null) {
                                mDraweeView.setGestureEnable(true);
                                return;
                            }
                            if (forceFillWidth && imageInfo.getWidth() < imageInfo.getHeight()) {

                            } else {
                                mDraweeView.setGestureEnable(true);
                            }
                            mDraweeView.update2(imageInfo.getWidth(), imageInfo.getHeight());
                            mDraweeView.invalidate();
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < getChildCount(); i++) {
                                        if (getChildAt(i) instanceof PhotoDraweeView) {
                                            removeViewAt(i);
                                            break;
                                        }
                                    }
                                }
                            }, 350);
                        }

                        @Override
                        public void onFailure(String id, Throwable throwable) {
                            super.onFailure(id, throwable);
                        }
                    });
            mDraweeView.setController(controller.build());
        }

    }

    private void showLongImage(String imageLocalPath) {
        if (mScaleImageView == null) {
            mScaleImageView = new SubsamplingScaleImageView(mContext);
            addView(mScaleImageView, new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mScaleImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(v);
                    }
                }
            });
        }
        mScaleImageView.setImage(ImageSource.uri(imageLocalPath), new ImageViewState(0.0F, new PointF(0, 0), 0));
        mScaleImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
    }

    public void animaPlay() {
        if (mDraweeView != null) {
            Animatable animatable = mDraweeView.getController().getAnimatable();
            if (animatable != null) {
                animatable.start();
            }
        }
    }

    public void saveBitmap(String saveFileName) {
        if (mUri == null || currentUri == null) {
            return;
        }
        String imageUrl = mUri.getPath();

        File file = new File(saveFileName);
        if (file.isFile()) {// 判断图片已经存在，则不再重复保存并提示

        } else {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            ImageRequest imageRequest = ImageRequest.fromUri(currentUri);
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            final DataSource<CloseableReference<PooledByteBuffer>> dataSource = imagePipeline.fetchEncodedImage(imageRequest, true);
            dataSource.subscribe(new ImageDownloadSubscriber(saveFileName) {
                @Override
                protected void onSuccess(File image) {

                }

                @Override
                protected void onFail(Throwable t) {
                    super.onFail(t);

                }
            }, AppThreadExecutor.getInstance().forBackgroundTasks());
        }
    }


    @Override
    public void setOnClickListener(final OnClickListener l) {
        onClickListener = l;
    }
}
