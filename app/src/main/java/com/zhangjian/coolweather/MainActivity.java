package com.zhangjian.coolweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_ID = "HE1902241339271005";
    private static final String KEY_CONTENT = "d5287c61b0004ff4933b818b1137ff24";

    private static final String url1 = "https://free-api.heweather.net/s6/air/now?location=CN101010100&key=d5287c61b0004ff4933b818b1137ff24";
    private static final String url2 = "https://api.heweather.net/s6/air?cityid=CN101010100&key=d5287c61b0004ff4933b818b1137ff24";
    private static final String url3 = "https://api.heweather.com/x3/weather?cityid=CN101010100&key=d5287c61b0004ff4933b818b1137ff24";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HeConfig.init(KEY_ID, KEY_CONTENT);
        HeConfig.switchToFreeServerNode();

        HeWeather.getWeatherNow(this, "CN101010100", Lang.CHINESE_SIMPLIFIED, Unit.METRIC,
                new HeWeather.OnResultWeatherNowBeanListener() {
                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: ", e);
                    }

                    @Override
                    public void onSuccess(List<Now> dataObject) {
                        Log.i(TAG, "onSuccess: " + new Gson().toJson(dataObject));
                    }
                });
    }

    private String getRequest() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url3)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
