package com.zhangjian.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhangjian.coolweather.CoolApplication;

public class CookieUtil {
    private static final String SP_NAME = "cool_sp";
    private static CookieUtil instance;
    private static SharedPreferences sharedPreferences;

    private static String Now = "now";
    private static String forecast = "forecast";

    private CookieUtil() {
        sharedPreferences = CoolApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static CookieUtil getInstance() {
        if (instance == null) {
            synchronized (CookieUtil.class) {
                if (instance == null) {
                    instance = new CookieUtil();
                }
            }
        }
        return instance;
    }

    public void setNow(String now) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Now, now);
        editor.apply();
    }
}
