package com.zhangjian.coolweather.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhangjian.coolweather.R;
import com.zhangjian.coolweather.base.BaseActivity;
import com.zhangjian.coolweather.fragment.ChooseAreaFragment;
import com.zhangjian.coolweather.util.FragmentUtil;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void init() {
        setContentView(R.layout.activity_main);

        FragmentUtil.setRootFragment(this, R.id.root_main, new ChooseAreaFragment());
    }

    @Override
    public void onBackPressed() {
        ChooseAreaFragment fragment = (ChooseAreaFragment) FragmentUtil.getLastFragment(this);
        fragment.backPressed();
    }
}
