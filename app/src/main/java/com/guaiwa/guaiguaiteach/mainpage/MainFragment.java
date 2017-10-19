package com.guaiwa.guaiguaiteach.mainpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guaiwa.guaiguaiteach.R;
import com.guaiwa.guaiguaiteach.base.BaseFragment;
import com.guaiwa.guaiguaiteach.imagepicker.ImagePickerActivity;

/**
 * Created by 80151689 on 2017-10-19.
 */

public class MainFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        view.findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ImagePickerActivity.class));
            }
        });
        return view;
    }


}
