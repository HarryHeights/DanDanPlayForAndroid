package com.xyoye.dandanplay2.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xyoye.dandanplay2.ui.weight.RoundImageView;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by YE on 2018/7/15.
 */
public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        return new RoundImageView(context);
    }
}
