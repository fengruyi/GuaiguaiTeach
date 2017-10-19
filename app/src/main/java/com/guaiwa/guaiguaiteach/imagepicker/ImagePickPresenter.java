package com.guaiwa.guaiguaiteach.imagepicker;

import com.guaiwa.guaiguaiteach.MyApplication;
import com.guaiwa.guaiguaiteach.base.BasePreSenter;

import java.util.ArrayList;


/**
 * Created by 80151689 on 2017-10-19.
 */

public class ImagePickPresenter extends BasePreSenter<ImagePickAction> {


    public ImagePickPresenter(ImagePickAction loadAction) {
        super(loadAction);
    }

    public void loadImages() {
        ImagesFetchTask mImagesFetchTask = new ImagesFetchTask(MyApplication.context, new ABasyncTask.Callback<ArrayList<ImageFolder>>() {
            @Override
            public void onPostExecute(ArrayList<ImageFolder> imageFolders) {
                loadAction.showImageFolder(imageFolders);
            }

            @Override
            public void onTaskCancelled() {

            }
        });
        mImagesFetchTask
                .setIsFilterImage(false)
                .setIsFilterVideo(true)
                .setIsFilterGif(true)
                .setmIsFilterLongImg(true)
                .setmIsHasCamara(false);
        mImagesFetchTask.perform();

    }

    @Override
    public void onDestroy() {

    }
}
