package com.guaiwa.guaiguaiteach.imagepicker;


import com.guaiwa.guaiguaiteach.base.BaseAction;

import java.util.ArrayList;

/**
 * Created by 80151689 on 2017-10-19.
 */

public interface  ImagePickAction extends BaseAction{

    void getImages();
    void showImageFolder(ArrayList<ImageFolder> imageFolders);
}
