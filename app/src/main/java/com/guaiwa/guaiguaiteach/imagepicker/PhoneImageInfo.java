package com.guaiwa.guaiguaiteach.imagepicker;


import com.google.gson.Gson;

/**
 * Created by 80151689 on 2016-12-16.
 * 本地图片信息
 */
public class PhoneImageInfo implements Comparable {

    private String mPath;//原图
    private String mThumbPath;
    private int mSize;//图片大小
    private String mMimeType;//图片类型"image/jpeg","image/png", "image/gif", "image/jpg" , "image/x-ms-bmp"
    //从手机图片数据库里查询宽高未知则认为该图片是损坏的
    private int mWidth;//图片宽
    private int mHeight;//图片高
    private int duration; // 视频长度
    private int id; // id 用于获取视频缩略图
    private long addTime;//创建时间


    public PhoneImageInfo() {

    }

    public PhoneImageInfo(String mPath, int mSize, String mMimeType, int mWidth, int mHeight) {
        this.mPath = mPath;
        this.mSize = mSize;
        this.mMimeType = mMimeType;
        this.mHeight = mHeight;
        this.mWidth = mWidth;
    }

    public String getmPath() {
        return mPath;
    }

    public String getmThumbPath() {
        return mThumbPath;
    }

    public int getmSize() {
        return mSize;
    }

    public String getmMimeType() {
        return mMimeType;
    }

    public int getmWidth() {
        return mWidth;
    }

    public int getmHeight() {
        return mHeight;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public void setmThumbPath(String mThumbPath) {
        this.mThumbPath = mThumbPath;
    }

    public void setmSize(int mSize) {
        this.mSize = mSize;
    }

    public void setmMimeType(String mMimeType) {
        this.mMimeType = mMimeType;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }


    public String getVideoTime() {
        int time = duration / 1000;
        return String.format("%1$02d:%2$02d", time / 60, time % 60);
    }

    @Override
    public boolean equals(Object o) {
        PhoneImageInfo other = (PhoneImageInfo) o;
        return other.getmPath().equals(this.mPath);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof PhoneImageInfo) {
            PhoneImageInfo another = (PhoneImageInfo) o;
            if (this.addTime > another.getAddTime()) {
                return -1;//时间越大排最前
            } else if (this.addTime < another.getAddTime()) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }
}
