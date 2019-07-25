package com.kernal.smartvision.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.kernal.smartvision.ocr.OcrTypeHelper;

public final class RectFindView extends View {

    /**
     * 刷新界面的时间
     */
    private static final long ANIMATION_DELAY = 25L;

    /**
     * 四周边框的宽度
     */
    private static final int FRAME_LINE_WIDTH = 4;
    private Rect frame;
    private int width, height;
    private Paint paint;
    //  private int screenWidth,screenHeight;

    private Context context;
    private DisplayMetrics dm;
    private String drawText;
    private float leftPointX, leftPointY, rectWidth, rectHeight, namePositionX, namePositionY;
    private String nameTextColor = "#ffffff";
    private String drawColor = "#4A9DE3";
    private int nameTextSize = 40;
    private int TestTextZize = 15;


    public RectFindView(Context context, OcrTypeHelper ocrTypeHelper, int srcWidth, int srcHeight) {
        super(context);
        paint = new Paint();
        this.context = context;
        // 获取当前屏幕
        dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        //  screenWidth = dm.widthPixels;
        //  screenHeight = dm.heightPixels;
        this.width = srcWidth;
        this.height = srcHeight;

        if (ocrTypeHelper != null) {
            try {
                this.leftPointX = ocrTypeHelper.leftPointXPercent;
                this.leftPointY = ocrTypeHelper.leftPointYPercent;
                this.rectWidth = ocrTypeHelper.widthPercent;
                this.rectHeight = ocrTypeHelper.heightPercent;
                this.namePositionX = ocrTypeHelper.namePositionXPercent;
                this.namePositionY = ocrTypeHelper.namePositionYPercent;
                this.drawText = ocrTypeHelper.ocrTypeName;
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        //  width = canvas.getWidth();
        // height = canvas.getHeight();


        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        float density = dm.density;        // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        paint.setColor(Color.parseColor(nameTextColor));
        paint.setTextSize(TestTextZize * density);
        /**
         * 这个矩形就是中间显示的那个框框
         */
        frame = new Rect((int) (leftPointX * width),
                (int) (height * leftPointY),
                (int) ((leftPointX + rectWidth) * width),
                (int) (height * (leftPointY + rectHeight)));

        paint.setColor(Color.parseColor("#999999"));
        if (dm.densityDpi > 320) {
            paint.setTextSize(Float.valueOf(35));
            canvas.drawText(context.getString(getResources().getIdentifier(
                    "please_collect", "string", context.getPackageName())), (int) (namePositionX * width), namePositionY * height, paint);
        } else if (dm.densityDpi == 320) {
            paint.setTextSize(Float.valueOf(35));
            canvas.drawText(context.getString(getResources().getIdentifier(
                    "please_collect", "string", context.getPackageName())), (int) (namePositionX * width * 0.9), namePositionY * height, paint);
        } else {
            paint.setTextSize(Float.valueOf(35));

            canvas.drawText(context.getString(getResources().getIdentifier(
                    "please_collect", "string", context.getPackageName())), (int) (namePositionX * width * 0.75), namePositionY * height, paint);
        }
        if (frame == null) {
            return;
        }
        // 画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
        // 扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
        paint.setColor(Color.argb(48, 0, 0, 0));
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1,
                paint);
        canvas.drawRect(frame.right + 1, frame.top, width,
                frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        // 绘制两个像素边宽的绿色线框
        paint.setColor(Color.parseColor(drawColor));
        canvas.drawRect(frame.left + FRAME_LINE_WIDTH - 2, frame.top,
                frame.right - FRAME_LINE_WIDTH + 2, frame.top
                        + FRAME_LINE_WIDTH, paint);// 上边
        canvas.drawRect(frame.left + FRAME_LINE_WIDTH - 2, frame.top,
                frame.left + FRAME_LINE_WIDTH + 2, frame.bottom
                        + FRAME_LINE_WIDTH, paint);// 左边
        canvas.drawRect(frame.right - FRAME_LINE_WIDTH - 2, frame.top,
                frame.right - FRAME_LINE_WIDTH + 2, frame.bottom
                        + FRAME_LINE_WIDTH, paint);// 右边
        canvas.drawRect(frame.left + FRAME_LINE_WIDTH - 2,
                frame.bottom, frame.right - FRAME_LINE_WIDTH + 2,
                frame.bottom + FRAME_LINE_WIDTH, paint);// 底边
        fresh();
    }

    public void fresh() {
        postInvalidateDelayed(ANIMATION_DELAY, 0, 0, (int) (width * 0.8), height);
    }

}
