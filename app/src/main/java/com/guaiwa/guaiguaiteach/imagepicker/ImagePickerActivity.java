package com.guaiwa.guaiguaiteach.imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.guaiwa.guaiguaiteach.R;
import com.guaiwa.guaiguaiteach.base.BaseActivity;
import com.guaiwa.guaiguaiteach.picturview.imagebrowser.ImageBrowserActivity;
import com.guaiwa.guaiguaiteach.widget.GridItemDecoration;

import java.util.ArrayList;


public class ImagePickerActivity extends BaseActivity implements ImagePickAction {
    protected ImagePickPresenter pickPresenter;
    protected RecyclerView recyclerView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_image_picker);
        super.onCreate(savedInstanceState);
        pickPresenter = new ImagePickPresenter(this);
        getImages();
    }

    @Override
    public void initView() {
        recyclerView = obtainView(R.id.rlv_images);
    }

    @Override
    public void getImages() {
        pickPresenter.loadImages();
    }

    @Override
    public void showImageFolder(final ArrayList<ImageFolder> imageFolders) {
        imageAdapter = new ImageAdapter(R.layout.image_picker_image_item, imageFolders.get(0).getAllImages());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new GridItemDecoration(3, 2, true));
        recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                imageAdapter.getItem(position);
                ImageBrowserActivity.imagePath = imageFolders.get(0).getAllImagesPath();
                ImageBrowserActivity.selectIndex = position;
                Intent intent = new Intent(ImagePickerActivity.this, ImageBrowserActivity.class);
                //android V4包的类,用于两个activity转场时的缩放效果实现
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
                try {
                    ActivityCompat.startActivity(ImagePickerActivity.this, intent, optionsCompat.toBundle());
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    startActivity(intent);
                }
            }
        });
    }

}
