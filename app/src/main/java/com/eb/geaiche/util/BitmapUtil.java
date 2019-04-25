package com.eb.geaiche.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.ByteArrayOutputStream;

public class BitmapUtil {
    /**
     * convert Bitmap to byte array
     */

    public static byte[] bitmapToByte(Bitmap b) {

        ByteArrayOutputStream o = new ByteArrayOutputStream();

        b.compress(Bitmap.CompressFormat.PNG, 100, o);

        return o.toByteArray();

    }

    /**
     * 把bitmap转换成Base64编码String
     */

    public static String bitmapToString(Bitmap bitmap) {

        return Base64.encodeToString(bitmapToByte(bitmap), Base64.DEFAULT);

    }

    public static Bitmap createBitmapThumbnail(Bitmap bitMap, boolean needRecycle, int newHeight, int newWidth) {

        int width = bitMap.getWidth();

        int height = bitMap.getHeight();

        // 计算缩放比例

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

        // 取得想要缩放的matrix参数

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        // 得到新的图片

        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);

        if (needRecycle)

            bitMap.recycle();

        return newBitMap;

    }

    /**
     * @param magnification 缩放倍率
     */
    public static Bitmap createBitmapThumbnail(Bitmap bitMap, boolean needRecycle, float magnification) {

        int width = bitMap.getWidth();

        int height = bitMap.getHeight();


        // 取得想要缩放的matrix参数

        Matrix matrix = new Matrix();

        matrix.postScale(magnification, magnification);

        // 得到新的图片

        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);

        if (needRecycle)

            bitMap.recycle();

        return newBitMap;

    }

    public static Bitmap createBitmapThumbnail(Bitmap bitMap, boolean needRecycle, float scaleHeight, float scaleWidth) {

        int width = bitMap.getWidth();

        int height = bitMap.getHeight();

        // 计算缩放比例
        // 取得想要缩放的matrix参数

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        // 得到新的图片

        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);

        if (needRecycle)

            bitMap.recycle();

        return newBitMap;

    }


    /**
     * 给一张Bitmap添加水印文字。
     *
     * @param src      源图片
     * @param content  水印文本
     * @param textSize 水印字体大小 ，单位pix。
     * @param color    水印字体颜色。
     * @param x        起始坐标x
     * @param y        起始坐标y
     * @param recycle  是否回收
     * @return 已经添加水印后的Bitmap。
     */

    public static byte[] addTextWatermarkTOByte(Bitmap src, String content, int textSize, int color, float x, float y, boolean recycle) {

        if (isEmptyBitmap(src) || content == null)

            return null;

        Bitmap ret = src.copy(src.getConfig(), true);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Canvas canvas = new Canvas(ret);

        paint.setColor(color);

        paint.setTextSize(textSize);

        Rect bounds = new Rect();

        paint.getTextBounds(content, 0, content.length(), bounds);

        canvas.drawText(content, x, y, paint);

        if (recycle && !src.isRecycled())

            src.recycle();


        return bitmap2Bytes(ret);

    }

    public static byte[] bitmap2Bytes(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap对象是否为空。
     */

    public static boolean isEmptyBitmap(Bitmap src) {

        return src == null || src.getWidth() == 0 || src.getHeight() == 0;

    }


}
