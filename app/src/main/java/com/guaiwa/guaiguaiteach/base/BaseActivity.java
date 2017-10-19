package com.guaiwa.guaiguaiteach.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by 80151689 on 2017-10-18.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView(){

    }
    /**
     * 简化findviewbyid转型
     *
     * @param resId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T obtainView(int resId) {
        View v = findViewById(resId);
        return (T) v;
    }

}
