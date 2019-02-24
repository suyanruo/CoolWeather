package com.zhangjian.coolweather.util;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

    private static OkHttpClient client = new OkHttpClient();

    public static void getRequestAsyn(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static String getRequestSyn(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null)
                return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
