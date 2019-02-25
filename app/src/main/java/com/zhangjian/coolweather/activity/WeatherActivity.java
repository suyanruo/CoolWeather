package com.zhangjian.coolweather.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhangjian.coolweather.R;
import com.zhangjian.coolweather.base.BaseActivity;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class WeatherActivity extends BaseActivity {
    private static final String TAG = "WeatherActivity";

    private ScrollView mWeatherLayout;
    private TextView mTitleText;
    private TextView mUpdateText;
    private TextView mDegreeText;
    private TextView mInfoText;
    private LinearLayout mForecastLayout;
    private ProgressDialog progressDialog;
    private boolean loadNowFinished;
    private boolean loadForecastFinished;

    @Override
    protected void init() {
        setContentView(R.layout.activity_weather);

        mWeatherLayout = findViewById(R.id.sv_weather);
        mTitleText = findViewById(R.id.tv_title_city);
        mUpdateText = findViewById(R.id.tv_update_time);
        mDegreeText = findViewById(R.id.tv_degree);
        mInfoText = findViewById(R.id.tv_weather_info);
        mForecastLayout = findViewById(R.id.forecast_layout);

        String weatherId = getIntent().getStringExtra("weather_id");
        showWeather(weatherId);
    }

    private void showWeather(String cityId) {
        showDialog();
        getNowWeather(cityId);
        getForecast(cityId);
    }

    private void getNowWeather(String cityId) {
        HeWeather.getWeatherNow(this, cityId, Lang.CHINESE_SIMPLIFIED, Unit.METRIC,
                new HeWeather.OnResultWeatherNowBeanListener() {
                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: ", e);
                        loadForecastFinished = true;
                    }

                    @Override
                    public void onSuccess(List<Now> dataObject) {
                        loadNowFinished = true;
                        closeDialog();
                        Log.i(TAG, "onSuccess: " + new Gson().toJson(dataObject));
                        Log.e(TAG, "thread: " + Thread.currentThread().getName());
                        if (dataObject == null || dataObject.size() == 0) return;
                        showNowWeather(dataObject);
                    }
                });
    }

    private void showNowWeather(List<Now> nowList) {
        Now now = nowList.get(0);
        NowBase nowBase = now.getNow();
        String degree = nowBase.getTmp() + "°C";
        StringBuilder info = new StringBuilder();
        info.append(nowBase.getCond_txt()).append(nowBase.getWind_dir()).append(nowBase.getWind_sc()).append("级");
        mTitleText.setText(now.getBasic().getLocation());
        mUpdateText.setText(now.getUpdate().getLoc());
        mDegreeText.setText(degree);
        mInfoText.setText(info.toString());
    }

    private void getForecast(String cityId) {
        HeWeather.getWeatherForecast(this, cityId, new HeWeather.OnResultWeatherForecastBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "onError: ", throwable);
                loadForecastFinished = true;
            }

            @Override
            public void onSuccess(List<Forecast> list) {
                loadForecastFinished = true;
                closeDialog();
                Log.d(TAG, "onSuccess: " + new Gson().toJson(list));
                if (list == null || list.size() == 0) return;
                showForecast(list);
            }
        });
    }

    private void showForecast(List<Forecast> forecastList) {
        Forecast forecast = forecastList.get(0);
        mForecastLayout.removeAllViews();
        for (ForecastBase forecastBase : forecast.getDaily_forecast()) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, mForecastLayout, false);
            TextView dateText = view.findViewById(R.id.tv_date);
            TextView infoText = view.findViewById(R.id.tv_info);
            TextView maxText = view.findViewById(R.id.tv_max);
            TextView minText = view.findViewById(R.id.tv_min);
            dateText.setText(forecastBase.getDate());
            infoText.setText(forecastBase.getCond_txt_d() + forecastBase.getWind_dir());
            maxText.setText(forecastBase.getTmp_max() + "°C");
            minText.setText(forecastBase.getTmp_min() + "°C");
            mForecastLayout.addView(view);
        }
    }

    private void showDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("加载中。。。");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeDialog() {
        if (progressDialog != null && loadNowFinished && loadForecastFinished) {
            progressDialog.dismiss();
        }
    }
}
