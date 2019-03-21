package com.zhangjian.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.zhangjian.coolweather.CoolApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class CookieUtil {
    private static final String SP_NAME = "cool_sp";
    private static CookieUtil instance;
    private static SharedPreferences sharedPreferences;

    private static String Now = "now";
    private static String Forecast = "forecast";
    private static String BingPic = "bing_pic";

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

    public void saveObj(String key, Object obj) {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            //将对象序列化写入byte缓存
            os.writeObject(obj);
            //将序列化的数据转为16进制保存
            String bytesToHexString = CommonUtil.bytesToHexString(bos.toByteArray());
            //保存该16进制数组
            editor.putString(key, bytesToHexString);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getObj(String key) {
        try {
            if (sharedPreferences.contains(key)) {
                String value = sharedPreferences.getString(key, "");
                if (TextUtils.isEmpty(value)) {
                    return null;
                } else {
                    //将16进制的数据转为数组，准备反序列化
                    byte[] stringToBytes = CommonUtil.stringToBytes(value);
                    ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                    ObjectInputStream is = new ObjectInputStream(bis);
                    //返回反序列化得到的对象
                    Object readObject = is.readObject();
                    return readObject;
                }
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNow() {
        return sharedPreferences.getString(Now, null);
    }

    public void setNow(String now) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Now, now);
        editor.apply();
    }

    public String getForecast() {
        return sharedPreferences.getString(Forecast, null);
    }

    public void setForecast(String forecast) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Forecast, forecast);
        editor.apply();
    }

    public String getBingPic() {
        return sharedPreferences.getString(BingPic, null);
    }

    public void setBingPic(String picPath) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(BingPic, picPath);
        editor.apply();
    }
}
