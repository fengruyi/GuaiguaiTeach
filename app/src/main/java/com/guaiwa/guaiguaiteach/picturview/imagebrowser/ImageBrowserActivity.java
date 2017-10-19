package com.guaiwa.guaiguaiteach.picturview.imagebrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.guaiwa.guaiguaiteach.R;
import com.guaiwa.guaiguaiteach.base.BaseActivity;
import com.guaiwa.guaiguaiteach.widget.viewpager.CustomViewpager;

import java.util.ArrayList;

public class ImageBrowserActivity extends BaseActivity {
    CustomViewpager customViewpager;
    TextView textView;
    public static ArrayList<String> imagePath;
    public static int selectIndex;
    int totalImageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_image_browser);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        customViewpager = obtainView(R.id.vp_images);
        textView = obtainView(R.id.tv_num);
        ImageBrowserAdapter adapter = new ImageBrowserAdapter(imagePath, this);
        totalImageCount = adapter.getCount();
        customViewpager.setAdapter(adapter);
        customViewpager.setCurrentItem(selectIndex);
        customViewpager.setOnPageChangeListener(new CustomViewpager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int var1, float var2, int var3) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText((position + 1) + "/" + totalImageCount);
            }

            @Override
            public void onPageScrollStateChanged(int var1) {

            }
        });
        textView.setText((selectIndex + 1) + "/" + totalImageCount);
    }
}
