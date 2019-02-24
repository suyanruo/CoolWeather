package com.zhangjian.coolweather;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

public class CoolApplication extends Application {
    private static final String KEY_ID = "HE1902241339271005";
    private static final String KEY_CONTENT = "d5287c61b0004ff4933b818b1137ff24";

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
        LitePalApplication.initialize(sContext);

        HeConfig.init(KEY_ID, KEY_CONTENT);
        HeConfig.switchToFreeServerNode();
    }
}
