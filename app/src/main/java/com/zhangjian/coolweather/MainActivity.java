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

//        HeWeather.getWeatherNow(this, "CN101010100", Lang.CHINESE_SIMPLIFIED, Unit.METRIC,
//                new HeWeather.OnResultWeatherNowBeanListener() {
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i(TAG, "onError: ", e);
//                    }
//
//                    @Override
//                    public void onSuccess(List<Now> dataObject) {
//                        Log.i(TAG, "onSuccess: " + new Gson().toJson(dataObject));
//                        Log.e(TAG, "thread: " + Thread.currentThread().getName());
//                    }
//                });
        FragmentUtil.setRootFragment(this, R.id.root_main, new ChooseAreaFragment());
    }

    @Override
    public void onBackPressed() {
        ChooseAreaFragment fragment = (ChooseAreaFragment) FragmentUtil.getLastFragment(this);
        fragment.backPressed();
    }
}
