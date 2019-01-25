package com.eb.new_line_seller.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

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

    // 加水印 也可以加文字
    public static Bitmap watermarkBitmap(Bitmap src,
                                         String title) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        //需要处理图片太大造成的内存超过的问题,这里我的图片很小所以不写相应代码了
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        Paint paint = new Paint();

        //加入文字
        if (title != null) {
            String familyName = "宋体";
            Typeface font = Typeface.create(familyName, Typeface.BOLD);
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(Color.RED);
            textPaint.setTypeface(font);
            textPaint.setTextSize(22);
            //这里是自动换行的
            StaticLayout layout = new StaticLayout(title, textPaint, w, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
            layout.draw(cv);
            //文字就加左上角算了
            //cv.drawText(title,0,40,paint);
        }
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return newb;
    }
}
