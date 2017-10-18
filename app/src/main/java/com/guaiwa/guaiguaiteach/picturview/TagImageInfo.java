package com.guaiwa.guaiguaiteach.picturview;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.guaiwa.guaiguaiteach.bean.IBean;


/**
 * 带标签信息的图片
 * Created by 80092574 on 2016-05-04.
 */
public class TagImageInfo implements IBean {
    private static final String TAG = TagImageInfo.class.getSimpleName();
    private String path;//图片地址
    private String srcPath;//原图地址
    private int width;
    private int height;

    private String tools_extra;

    private boolean hasSrcCache;//是否有原图缓存

    public TagImageInfo() {

    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setWidth(int width) {
        this.width = width;

    }

    public void setHeight(int height) {
        this.height = height;

    }

    public String getPath() {
        return path;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
        if (srcPath != null) {
            setHasSrcCache(ImagePipelineFactory.getInstance().getMainFileCache().hasKey(new SimpleCacheKey(srcPath)));
        }
    }

    public void setTools_extra(String tools_extra) {
        this.tools_extra = tools_extra;
    }

    public String getTools_extra() {
        return tools_extra;
    }

    public boolean isHasSrcCache() {
        return hasSrcCache;
    }

    public void setHasSrcCache(boolean hasSrcCache) {
        this.hasSrcCache = hasSrcCache;
    }


}
