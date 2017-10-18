package com.guaiwa.guaiguaiteach.imagepicker;

import java.util.ArrayList;
import java.util.List;

public class ImageFolder {
    private double id;
    private String mName;//图片文件夹名
    private String mPath;//封面照片
    private int mCount;//照片数
    private List<PhoneImageInfo> imageInfos = new ArrayList<>();
    private boolean isSelected;
    private ArrayList<String> imagePathlist;


    public ImageFolder(String name) {
        mName = name;
    }

    public ImageFolder(String name, String path, int count) {
        mName = name;
        mPath = path;
        mCount = count;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getName() {
        return mName;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String p) {
        mPath = p;
    }

    public int getmCount() {
        return mCount;
    }

    public void setmCount(int mCount) {
        this.mCount = mCount;
    }

    public List<PhoneImageInfo> getAllImages() {
        return imageInfos;
    }

    public ArrayList<String> getAllImagesPath() {
        if (imagePathlist == null) {
            imagePathlist = new ArrayList<>();
            if (imageInfos != null) {
                for (PhoneImageInfo phoneImageInfo : imageInfos) {
                    if (phoneImageInfo != null && phoneImageInfo.getmMimeType() != null && phoneImageInfo.getmMimeType().contains("image")) {
                        imagePathlist.add(phoneImageInfo.getmPath());
                    }
                }
            }
        }
        return imagePathlist;
    }

    public void recycle() {
        if (imageInfos != null) {
            imageInfos.clear();
        }
        if (imagePathlist != null) {
            imagePathlist.clear();
        }
    }
}
