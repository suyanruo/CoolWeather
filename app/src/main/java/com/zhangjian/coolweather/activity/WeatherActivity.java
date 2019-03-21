package com.zhangjian.coolweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangjian.coolweather.service.AutoUpdateService;
import com.zhangjian.coolweather.R;
import com.zhangjian.coolweather.base.BaseActivity;
import com.zhangjian.coolweather.consis.UrlConst;
import com.zhangjian.coolweather.util.CookieUtil;
import com.zhangjian.coolweather.util.DataUtil;
import com.zhangjian.coolweather.util.HttpUtil;
import com.zhangjian.coolweather.util.ImageUtil;
import com.zhangjian.coolweather.util.ServiceUtil;

import java.io.IOException;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity {
    private static final String TAG = "WeatherActivity";

    private DrawerLayout mDrawerLayout;
    private Button mNavButton;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mBackgroundImage;
    private ScrollView mWeatherLayout;
    private TextView mTitleText;
    private TextView mUpdateText;
    private TextView mDegreeText;
    private TextView mInfoText;
    private LinearLayout mForecastLayout;
    private ProgressDialog progressDialog;
    private boolean loadNowFinished;
    private boolean loadForecastFinished;
    private String weatherId;

    @Override
    protected void init() {
        setContentView(R.layout.activity_weather);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavButton = findViewById(R.id.btn_nav);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);
        mBackgroundImage = findViewById(R.id.iv_background);
        mWeatherLayout = findViewById(R.id.sv_weather);
        mTitleText = findViewById(R.id.tv_title_city);
        mUpdateText = findViewById(R.id.tv_update_time);
        mDegreeText = findViewById(R.id.tv_degree);
        mInfoText = findViewById(R.id.tv_weather_info);
        mForecastLayout = findViewById(R.id.forecast_layout);


        String weatherString = CookieUtil.getInstance().getNow();
        String forecastString = CookieUtil.getInstance().getForecast();
        List<Now> weather = DataUtil.parse(weatherString, new TypeToken<List<Now>>() {});
        List<Forecast> forecasts = DataUtil.parse(forecastString, new TypeToken<List<Forecast>>() {});

        if (weather != null && forecasts != null) {
            // 当前天气和预报缓存均存在
            weatherId = weather.get(0).getBasic().getCid();
            showNowWeather(weather);
            showForecast(forecasts);
        } else {
            showDialog();
            weatherId = getIntent().getStringExtra("weather_id");
            if (weather != null) {
                // 无预报缓存
                showNowWeather(weather);
                loadNowFinished = true;
                getForecast(weatherId);
            } else if (forecasts != null) {
                // 无天气缓存
                getNowWeather(weatherId);
                loadForecastFinished = true;
                showForecast(forecasts);
            } else {
                // 无任何缓存
                showWeather(weatherId);
            }
        }
        // 加载背景图片
        String bingPic = CookieUtil.getInstance().getBingPic();
        if (bingPic != null) {
            ImageUtil.loadImage(this, bingPic, mBackgroundImage);
        } else {
            loadBackground();
        }

        mNavButton.setOnClickListener(view -> {
            mDrawerLayout.openDrawer(GravityCompat.START);
        });
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(() -> showWeather(weatherId));
    }

    public void changeCity(String cityId) {
        weatherId = cityId;
        mDrawerLayout.closeDrawers();
        mSwipeRefreshLayout.setRefreshing(true);
        showWeather(weatherId);
    }

    private void showWeather(String cityId) {
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
                        closeLoading();
                    }

                    @Override
                    public void onSuccess(List<Now> dataObject) {
                        loadNowFinished = true;
                        closeLoading();
                        Log.i(TAG, "onSuccess: " + new Gson().toJson(dataObject));
                        Log.e(TAG, "thread: " + Thread.currentThread().getName());
                        if (dataObject == null || dataObject.size() == 0) return;
                        CookieUtil.getInstance().setNow(DataUtil.toJson(dataObject));
                        showNowWeather(dataObject);
                        // 在获取天气信息成功后，启动自动刷新后台服务，首先判断是否已启动服务，若没有则启动
                        if (!ServiceUtil.isServiceRunning(WeatherActivity.this, "com.zhangjian.coolweather.service.AutoUpdateService")) {
                            Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
                            startService(intent);
                        }
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
                closeLoading();
            }

            @Override
            public void onSuccess(List<Forecast> list) {
                loadForecastFinished = true;
                closeLoading();
                Log.d(TAG, "onSuccess: " + new Gson().toJson(list));
                if (list == null || list.size() == 0) return;
                CookieUtil.getInstance().setForecast(DataUtil.toJson(list));
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

    private void loadBackground() {
        HttpUtil.getRequestAsyn(UrlConst.BING_LOAD_PIC, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(WeatherActivity.this, "load image fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    String result = response.body().string();
                    if (TextUtils.isEmpty(result)) return;
                    CookieUtil.getInstance().setBingPic(result);
                    runOnUiThread(() -> {
                        ImageUtil.loadImage(WeatherActivity.this, result, mBackgroundImage);
                    });
                }
            }
        });
    }

    private void closeLoading() {
        if (!loadNowFinished || !loadForecastFinished) return;
        if (progressDialog != null && progressDialog.isShowing()) {
            closeDialog();
        }
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
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
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
