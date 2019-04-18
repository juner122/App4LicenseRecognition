package com.eb.geaiche.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eb.geaiche.R;

public class ImageUtils {


    public static void load(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.icon_placeholder)                //加载成功之前占位图
                .error(R.mipmap.icon_placeholder)                    //加载错误之后的错误图
//                .override(400,400)                                //指定图片的尺寸
//                //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
//                .fitCenter()
//                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
//                .centerCrop()
//                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
//                .skipMemoryCache(true)                            //跳过内存缓存
//                .diskCacheStrategy(DiskCacheStrategy.ALL)        //缓存所有版本的图像
//                .diskCacheStrategy(DiskCacheStrategy.NONE)        //跳过磁盘缓存
//                .diskCacheStrategy(DiskCacheStrategy.DATA)        //只缓存原来分辨率的图片
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)    //只缓存最终的图片
                ;


        Glide.with(context)
                .load(url)
                .apply(options)
                .into((imageView));

    }


}
