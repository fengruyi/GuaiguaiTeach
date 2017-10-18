package com.guaiwa.guaiguaiteach.apptest;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.guaiwa.guaiguaiteach.R;

public class TabTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_test);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.id_tabLayout);
        for (int i=0;i<15;i++){
            tabLayout.addTab(tabLayout.newTab().setText("tab"+i));
        }
    }
}
