package com.guaiwa.guaiguaiteach.imagepicker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.Nullable;
import android.text.TextUtils;


import com.guaiwa.guaiguaiteach.R;
import com.guaiwa.guaiguaiteach.common.utils.PhoneInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


//查询相册任务
public class ImagesFetchTask extends ABasyncTask<Void, ArrayList<ImageFolder>> implements Cloneable {
    private int mPageSize = 100;
    private int mPageIndex = 1;//从1开始，不能从0开始
    private boolean mImageHasMore = true;
    private boolean mVideoHasMore = true;

    private static final String WHERE_CLAUSE = "(" + Media.MIME_TYPE + " in (?, ?, ?, ?,?,?))";
    //支持的图片格式，jpg,png,gif bmp
    //Media.BUCKET_DISPLAY_NAME(图片上级目录名称)，Media.DATA(图片全路径名)，Media.SIZE(图片大小，字节)，Media.MIME_TYPE(图片类型)
    public static final String[] SUPPORT_IMAGE_PROJECTION = new String[]{
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            Media.DATE_ADDED};
    public static final String[] SUPPORT_IMAGE_TYPES = new String[]{
            "image/jpeg", "image/png", "image/gif", "image/jpg", "image/x-ms-bmp", "image/webp"};

    public static final String[] SUPPORT_IMAGE_TYPE_NOGIF = new String[]{
            "image/jpeg", "image/png", "image/jpg", "image/x-ms-bmp", "image/webp"};

    //    private static final String SORT_ORDER = MediaStore.Images.Media.DATE_ADDED + " DESC";
    //视频
    public static final String[] SUPPORT_VIDEO_PROJECTION = new String[]{
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT,
            MediaStore.Images.Media.SIZE,
    };
    public static final String[] SUPPORT_VIDEO_TYPES = new String[]{
            "video/mp4", "video/ext-mp4", "video/3gpp", "video/avi", "video/mov"};
    //    private static final String VIDEO_SORT_ORDER = MediaStore.Video.Media.DATE_ADDED + " DESC";

    private Context mContext;
    private boolean mIsFilterGif = true;
    private boolean mIsFilterLongImg = true;//是否过滤长图
    private boolean mIsHasCamara = true;//是否有拍照功能
    private boolean mIsFilterImage = false;//是否过滤图片
    private boolean mIsFilterVideo = true;//是否过滤视频默认不过滤
    private ContentResolver mContentResolver;

    @Override
    protected ImagesFetchTask clone() throws CloneNotSupportedException {
        ImagesFetchTask imagesFetchTask = new ImagesFetchTask(mContext, getCallback());
        imagesFetchTask.setIsFilterGif(mIsFilterGif);
        imagesFetchTask.setmIsFilterLongImg(mIsFilterLongImg);
        imagesFetchTask.setmIsHasCamara(mIsHasCamara);
        imagesFetchTask.setIsFilterImage(mIsFilterImage);
        imagesFetchTask.setIsFilterVideo(mIsFilterVideo);
        imagesFetchTask.setVideoHasMore(mVideoHasMore);
        imagesFetchTask.setImageHasMore(mImageHasMore);
        imagesFetchTask.setPageIndex(mPageIndex);
        return imagesFetchTask;
    }

    public ImagesFetchTask(Context context, Callback<ArrayList<ImageFolder>> callback) {
        super(callback);
        mContext = context.getApplicationContext();
        mContentResolver = context.getContentResolver();
    }

    public ImagesFetchTask setIsFilterGif(boolean isFilterGif) {
        mIsFilterGif = isFilterGif;
        return this;
    }

    public ImagesFetchTask setmIsFilterLongImg(boolean mIsFilterLongImg) {
        this.mIsFilterLongImg = mIsFilterLongImg;
        return this;
    }

    public ImagesFetchTask setmIsHasCamara(boolean mIsHasCamara) {
        this.mIsHasCamara = mIsHasCamara;
        return this;
    }

    public ImagesFetchTask setIsFilterImage(boolean isFilterImage) {
        this.mIsFilterImage = isFilterImage;
        return this;
    }

    public ImagesFetchTask setIsFilterVideo(boolean isFilterVideo) {
        this.mIsFilterVideo = isFilterVideo;
        return this;
    }

    public boolean ismIsFilterVideo() {
        return mIsFilterVideo;
    }

    public boolean isImageHasMore() {
        return mImageHasMore;
    }

    public ImagesFetchTask setImageHasMore(boolean imageHasMore) {
        mImageHasMore = imageHasMore;
        return this;
    }

    public boolean isVideoHasMore() {
        return mVideoHasMore;
    }

    public ImagesFetchTask setVideoHasMore(boolean videoHasMore) {
        mVideoHasMore = videoHasMore;
        return this;
    }

    public int getPageIndex() {
        return mPageIndex;
    }

    public ImagesFetchTask setPageIndex(int pageIndex) {
        this.mPageIndex = pageIndex;
        return this;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public ImagesFetchTask setPageSize(int pageSize) {
        this.mPageSize = pageSize;
        return this;
    }

    private String getSortOrder() {
        return MediaStore.Images.Media.DATE_ADDED + " DESC limit " + mPageIndex * mPageSize;
    }

    public String getVidoeSortOrder() {
        return MediaStore.Video.Media.DATE_ADDED + " DESC limit " + mPageIndex * mPageSize;
    }

    @Override
    protected ArrayList<ImageFolder> doInBackground(Void... params) {
        //一次性取出所有的图库照片记录，用map分类文件夹
        ArrayList<ImageFolder> imageFolders = new ArrayList<>();
        ImageFolder camaraFolder = null;
        ImageFolder allVideoFolder = null;
        ImageFolder allImageFolder = new ImageFolder(mContext.getString(R.string.all_image));
        allImageFolder.setId(1.0);//这个手动创建的“相册”对象将id设为1.0，“视频”对象为2.0
        HashMap<Double, ImageFolder> imageFolderMap = new HashMap<>();//用于文件夹
        if (!mIsFilterImage) {
            camaraFolder = getImageFolder(imageFolderMap, allImageFolder);
        } else {
            mImageHasMore = false;
        }
        //当有视频时才去和图片排序
        if (!mIsFilterVideo) {
            allVideoFolder = new ImageFolder(mContext.getString(R.string.all_video));
            allImageFolder.setId(2.0);//这个手动创建的“所有视频”对象将id设为2.0，“相册”对象为2.0
            getVideoFile(imageFolderMap, allVideoFolder, allImageFolder);
            if (allVideoFolder.getAllImages().size() > 0) {
                imageFolders.add(0, allVideoFolder);
            }
        } else {
            mVideoHasMore = false;
        }
        imageFolders.addAll(imageFolderMap.values());

        if (camaraFolder != null) {//把相机相册提前
            imageFolders.remove(camaraFolder);
            imageFolders.add(0, camaraFolder);
        }
        imageFolders.add(0, allImageFolder);
        try {
            if (allVideoFolder != null && allVideoFolder.getAllImages().size() > 0) {
                for (ImageFolder imageFolder : imageFolders) {
                    Collections.sort(imageFolder.getAllImages());
                }
            }
        } catch (IllegalArgumentException e) {

        }
        if (mIsHasCamara) {
            allImageFolder.getAllImages().add(0, new PhoneImageInfo());
        }
        imageFolderMap.clear();
        return imageFolders;
    }

    @Nullable
    private ImageFolder getImageFolder(HashMap<Double, ImageFolder> imageFolderMap, ImageFolder allImageFolder) {
        PhoneImageInfo phoneImageInfo;
        ImageFolder imageFolder = null;
        Cursor imgaeCursor = null;
        ImageFolder camaraFolder = null;
        try {
            imgaeCursor = mContentResolver.query(getImageFoldersUri(), SUPPORT_IMAGE_PROJECTION, WHERE_CLAUSE, mIsFilterGif ? SUPPORT_IMAGE_TYPE_NOGIF : SUPPORT_IMAGE_TYPES, getSortOrder());
            if (imgaeCursor != null && imgaeCursor.getCount() > 0) {
                int count = imgaeCursor.getCount();
                while (imgaeCursor.moveToNext()) {
                    double id = imgaeCursor.getDouble(0);
                    String dirName = imgaeCursor.getString(1);
                    String imagePath = imgaeCursor.getString(2);
//                    if (isNotImageFile(imagePath)) {
//                        continue;
//                    }
                    int filesize = imgaeCursor.getInt(3);
                    String type = imgaeCursor.getString(4);
                    int width = imgaeCursor.getInt(5);
                    int height = imgaeCursor.getInt(6);
                    if (width == 0 || height == 0) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(imagePath, options);
                        width = options.outWidth;
                        height = options.outHeight;
                    }
                    if (width == 0 || height == 0) {
                        continue;
                    }
                    if (mIsFilterLongImg && isLongImage(width, height)) {//过滤长图
                        continue;
                    }
                    phoneImageInfo = new PhoneImageInfo(imagePath, filesize, type, width, height);
                    phoneImageInfo.setId((int) id);
                    phoneImageInfo.setAddTime(imgaeCursor.getLong(7));
                    if (imageFolderMap.containsKey(id)) {
                        imageFolder = imageFolderMap.get(id);
                        imageFolder.setmCount(imageFolder.getmCount() + 1);
                    } else {
                        imageFolder = new ImageFolder(dirName, null, 1);
                        imageFolder.setId(imgaeCursor.getDouble(imgaeCursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)));
                        if ((dirName.toLowerCase().equals("camera") && imagePath.contains("DCIM")) || "我的照片".equals(dirName)) {
                            imageFolder.setmName(mContext.getString(R.string.camera_image));
                            camaraFolder = imageFolder;
                        } else if (dirName.toLowerCase().equals("0")) {
                            imageFolder.setmName(mContext.getString(R.string.sdcard_image));
                        }
                        imageFolderMap.put(id, imageFolder);
                    }
                    imageFolder.getAllImages().add(phoneImageInfo);
                    allImageFolder.getAllImages().add(phoneImageInfo);
                    allImageFolder.setmCount(allImageFolder.getmCount() + 1);
                }
                if (count < mPageIndex * mPageSize) {
                    mImageHasMore = false;
                }
            }
        } catch (Exception e) {

        } finally {
            if (imgaeCursor != null) {
                imgaeCursor.close();
            }

        }
        return camaraFolder;
    }

    private PhoneImageInfo generateVideoInfo(Cursor cursor, int col_data, int col_duration, int col_mine_type, int col_id, int col_date_added, int col_width, int col_height) {

        String filePath = cursor.getString(col_data);
        if (!new File(filePath).exists()) {
            return null;
        }
        PhoneImageInfo videoInfo = new PhoneImageInfo();
        int duration = cursor.getInt(col_duration);
        String mimeType = cursor.getString(col_mine_type);
        videoInfo.setmPath(filePath);
        videoInfo.setmMimeType(mimeType);
        videoInfo.setDuration(duration);
        videoInfo.setId(cursor.getInt(col_id));
        videoInfo.setAddTime(cursor.getLong(col_date_added));
        videoInfo.setmWidth(cursor.getInt(col_width));
        videoInfo.setmHeight(cursor.getInt(col_height));
        Cursor thumbCursor = mContentResolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Video.Thumbnails.DATA,
                        MediaStore.Video.Thumbnails.VIDEO_ID
                },
                MediaStore.Video.Thumbnails.VIDEO_ID + "=?",
                new String[]{String.valueOf(videoInfo.getId())}, null);

        if (thumbCursor.moveToFirst()) {
            videoInfo.setmThumbPath(thumbCursor.getString(
                    thumbCursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA)));
        }
        thumbCursor.close();
        return videoInfo;
    }

    private void getVideoFile(HashMap<Double, ImageFolder> imageFolderMap, ImageFolder allVideoFolder, ImageFolder allImageFolder) {
        ImageFolder imageFolder = null;
        PhoneImageInfo phoneImageInfo = null;
        Cursor videoCursor = null;
        try {
            videoCursor = mContentResolver.query(getVideoFoldersUri(), SUPPORT_VIDEO_PROJECTION, String.format("%1$s IN (?, ?, ?, ?,?) AND %2$s > %3$d",
                    MediaStore.Video.Media.MIME_TYPE,
                    MediaStore.Video.Media.DURATION, 0), SUPPORT_VIDEO_TYPES, getVidoeSortOrder());
            int col_duration_video = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int col_mine_type_video = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE);
            int col_data_video = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int col_id_video = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int col_date_added_video = videoCursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED);
            int col_bucke_id = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID);
            int col_bucke_name = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
            int col_width = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH);
            int col_height = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT);
            int col_size = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int count = videoCursor.getCount();
            while (videoCursor.moveToNext()) {
                phoneImageInfo = generateVideoInfo(videoCursor, col_data_video, col_duration_video, col_mine_type_video, col_id_video, col_date_added_video, col_width, col_height);
                phoneImageInfo.setmSize(col_size);
                if (imageFolderMap.containsKey(videoCursor.getDouble(col_bucke_id))) {
                    imageFolder = imageFolderMap.get(videoCursor.getDouble(0));
                    imageFolder.setmCount(imageFolder.getmCount() + 1);
                } else {
                    imageFolder = new ImageFolder(videoCursor.getString(col_bucke_name), null, 1);
                    imageFolder.setId(videoCursor.getDouble(col_bucke_id));
                    imageFolderMap.put(videoCursor.getDouble(col_bucke_id), imageFolder);
                }
                imageFolder.getAllImages().add(phoneImageInfo);
                allVideoFolder.getAllImages().add(phoneImageInfo);
                allImageFolder.getAllImages().add(phoneImageInfo);
                allImageFolder.setmCount(allImageFolder.getmCount() + 1);
                allVideoFolder.setmCount(allVideoFolder.getmCount() + 1);
            }
            if (count < mPageIndex * mPageSize) {
                mVideoHasMore = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (videoCursor != null) {
                videoCursor.close();
            }

        }
    }

    public ImagesFetchTask perform() {
        if (Build.VERSION.SDK_INT >= 11) {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            execute();
        }
        return this;
    }

    private static boolean isNotImageFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }
        File file = new File(path);
        return !file.exists() || file.length() == 0;
    }

    private static Uri getImageFoldersUri() {
        Uri uri;
        uri = Images.Media.EXTERNAL_CONTENT_URI;
        return uri;
    }

    private static Uri getVideoFoldersUri() {
        Uri uri;
        uri = Images.Media.EXTERNAL_CONTENT_URI;
        return uri;
    }

    boolean isLongImage(int imgWidth, int imgHeight) {
        if ((imgWidth > PhoneInfo.screenWidth || imgHeight > PhoneInfo.screenHeight)) {
            if ((imgWidth / (float) imgHeight) > 2 || (imgHeight / (float) imgWidth) > 2) {
                return true;
            }
        }
        return false;
    }
}
