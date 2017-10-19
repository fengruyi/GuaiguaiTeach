package com.guaiwa.guaiguaiteach.mainpage;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.guaiwa.guaiguaiteach.MyApplication;
import com.guaiwa.guaiguaiteach.R;
import com.guaiwa.guaiguaiteach.base.ItemFragment;
import com.guaiwa.guaiguaiteach.base.dummy.DummyContent;


public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {
    String TAG = "MainActivity";
    private final Class mFragments[] = {
            MainFragment.class,
            ItemFragment.newInstance(20).getClass(),
            ItemFragment.newInstance(5).getClass(),
            ItemFragment.newInstance(30).getClass(),
    };
    private final int[] mTabItemIconArray = {
            android.R.drawable.ic_menu_camera,
            android.R.drawable.ic_menu_agenda,
            android.R.drawable.ic_menu_call,
            android.R.drawable.ic_menu_compass,
    };
    private String[] mTabItemStringArray = MyApplication.context.getResources().getStringArray(R.array.array_main_tab_item);
    FragmentTabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTab();
    }


    private void initTab(){
        // 找到TabHost
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (int i = 0; i < mFragments.length; i++) {
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(mTabItemStringArray[i]).setIndicator(getTabItemView(i));

            tabHost.addTab(tabSpec, mFragments[i], null);
        }
    }

    private View getTabItemView(int index){
        View view;
        TextView tabText;
        view = getLayoutInflater().inflate(R.layout.tabhost_item_layout, null);

        tabText = (TextView) view.findViewById(R.id.text_tab);
        tabText.setCompoundDrawablesWithIntrinsicBounds(0, mTabItemIconArray[index], 0, 0);
        tabText.setText(mTabItemStringArray[index]);
        tabText.setTag(index);

        return view;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
