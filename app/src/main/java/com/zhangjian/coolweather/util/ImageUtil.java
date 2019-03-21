package com.zhangjian.coolweather.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageUtil {

    public static void loadImage(Context context, String path, ImageView imageView) {
        Glide
                .with(context)
                .load(path)
                .into(imageView);
    }
}
