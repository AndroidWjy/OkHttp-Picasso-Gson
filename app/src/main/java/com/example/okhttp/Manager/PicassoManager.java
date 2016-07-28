package com.example.okhttp.Manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * 封装picasso常用方法
 * Created by Administrator on 2016/7/27.
 */
public class PicassoManager {
    /**
     * 按照指定大小加载图片
     *
     * @param context
     * @param path
     * @param width
     * @param height
     * @param imageView
     */
    public static void loadImageWithSize(Context context, String path, int width, int height, ImageView imageView) {
        Picasso.with(context).load(path).resize(width, height).centerCrop().into(imageView);
    }

    /**
     * 后台下载可以有默认图片填充
     *
     * @param context
     * @param path
     * @param resID
     * @param imageView
     */
    public static void loadImageWithHodler(Context context, String path, int resID, ImageView imageView) {
        Picasso.with(context).load(path).placeholder(resID).fit().into(imageView);
    }

    /**
     * 自定义裁剪图片
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadImageWithCrop(Context context, String path, ImageView imageView) {
        Picasso.with(context).load(path).transform(new CropTransformation()).into(imageView);
    }

    static class CropTransformation implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            //裁剪图片，根据x,y的起始位置
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != null) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "wjy";
        }
    }
}
