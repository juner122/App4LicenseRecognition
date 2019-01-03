package com.eb.new_line_seller.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

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
}
