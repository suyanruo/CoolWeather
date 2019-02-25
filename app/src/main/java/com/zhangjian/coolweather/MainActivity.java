package com.zhangjian.coolweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhangjian.coolweather.fragment.ChooseAreaFragment;
import com.zhangjian.coolweather.util.FragmentUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentUtil.setRootFragment(this, R.id.root_main, new ChooseAreaFragment());
    }

    @Override
    public void onBackPressed() {
        ChooseAreaFragment fragment = (ChooseAreaFragment) FragmentUtil.getLastFragment(this);
        fragment.backPressed();
    }
}
