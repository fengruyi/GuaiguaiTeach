package com.guaiwa.guaiguaiteach.picturview.imagebrowser;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.guaiwa.guaiguaiteach.picturview.LoadingDraweeview;
import com.guaiwa.guaiguaiteach.widget.viewpager.CustomPagerAdapter;


import java.util.ArrayList;

/***
 * @author marks.luo
 * @Description: ()
 * @date:2017-07-12 18:26
 */
public class ImageBrowserAdapter extends CustomPagerAdapter {
    private ArrayList<String> mUrlList;
    private LoadingDraweeview mCurrentView;
    private Context mContext;

    public ImageBrowserAdapter(ArrayList<String> urlList, Context context) {
        mUrlList = urlList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mUrlList == null ? 0 : mUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        final LoadingDraweeview loadingDraweeview = new LoadingDraweeview(mContext, mUrlList.get(position));
        try {
            container.addView(loadingDraweeview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            loadingDraweeview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) mContext).finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadingDraweeview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (LoadingDraweeview) object;
    }

    public LoadingDraweeview getPrimaryItem() {
        return mCurrentView;
    }

}
