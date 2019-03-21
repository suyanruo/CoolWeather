package com.zhangjian.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangjian.coolweather.consis.UrlConst;
import com.zhangjian.coolweather.util.CookieUtil;
import com.zhangjian.coolweather.util.DataUtil;
import com.zhangjian.coolweather.util.HttpUtil;

import java.io.IOException;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    private static final String TAG = "AutoUpdateService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBackImage();

        long triggerAtTime = SystemClock.elapsedRealtime() + 6 * 60 * 60 * 1000;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        String weatherString = CookieUtil.getInstance().getNow();
        List<Now> weather = DataUtil.parse(weatherString, new TypeToken<List<Now>>() {});
        if (weather == null || weather.size() == 0) return;
        String cityId = weather.get(0).getBasic().getCid();
        if (TextUtils.isEmpty(cityId)) return;
        HeWeather.getWeatherNow(this, cityId, Lang.CHINESE_SIMPLIFIED, Unit.METRIC,
                new HeWeather.OnResultWeatherNowBeanListener() {
                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "get weather fail", e);
                    }

                    @Override
                    public void onSuccess(List<Now> dataObject) {
                        Log.i(TAG, "onSuccess: " + new Gson().toJson(dataObject));
                        if (dataObject == null || dataObject.size() == 0) return;
                        CookieUtil.getInstance().setNow(DataUtil.toJson(dataObject));
                    }
                });
        HeWeather.getWeatherForecast(this, cityId, new HeWeather.OnResultWeatherForecastBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "get forecast fail", throwable);
            }

            @Override
            public void onSuccess(List<Forecast> list) {
                if (list == null || list.size() == 0) return;
                CookieUtil.getInstance().setForecast(DataUtil.toJson(list));
            }
        });
    }

    private void updateBackImage() {
        HttpUtil.getRequestAsyn(UrlConst.BING_LOAD_PIC, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "load bing fail");;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null) {
                    String result = response.body().string();
                    if (TextUtils.isEmpty(result)) return;
                    CookieUtil.getInstance().setBingPic(result);
                }
            }
        });
    }
}
