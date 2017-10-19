package com.guaiwa.guaiguaiteach.imagepicker;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.guaiwa.guaiguaiteach.R;
import com.guaiwa.guaiguaiteach.common.utils.PhoneInfo;
import com.guaiwa.guaiguaiteach.imageloader.FrescoUtil;
import com.guaiwa.guaiguaiteach.widget.MyImageView;

import java.io.File;
import java.util.List;

import static com.guaiwa.guaiguaiteach.MyApplication.context;

/**
 * Created by 80151689 on 2017-10-18.
 */

public class ImageAdapter extends BaseQuickAdapter<PhoneImageInfo, BaseViewHolder> {
    private final int imageItemSize;
    RelativeLayout.LayoutParams lp;

    public ImageAdapter(@LayoutRes int layoutResId, @Nullable List<PhoneImageInfo> data) {
        super(layoutResId, data);
        imageItemSize = (PhoneInfo.getScreenWidth(context) - PhoneInfo.dip2px(context, 8)) / 3;
        lp = new RelativeLayout.LayoutParams(imageItemSize, imageItemSize);
    }


    @Override
    protected void convert(BaseViewHolder helper, PhoneImageInfo item) {
        MyImageView imageView = helper.getView(R.id.image_view);
        if (imageView.getLayoutParams() != lp) {
            imageView.setLayoutParams(lp);
        }
        Uri uri = Uri.fromFile(new File(item.getmPath()));
        FrescoUtil.showFixSizeImage(imageView, uri);
    }
}
