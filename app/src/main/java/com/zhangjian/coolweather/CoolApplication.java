package com.zhangjian.coolweather;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

public class CoolApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
        LitePalApplication.initialize(sContext);
    }
}
