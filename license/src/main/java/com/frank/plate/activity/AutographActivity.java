package com.frank.plate.activity;


import android.graphics.Color;
import android.view.View;

import com.frank.plate.R;
import com.frank.plate.util.ToastUtils;
import com.frank.plate.view.LinePathView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class AutographActivity extends BaseActivity {

    @BindView(R.id.lpv)
    LinePathView lpv;//手写签名

    @OnClick({R.id.tv_fix_order, R.id.tv_enter_order})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_fix_order:
                lpv.clear();
                lpv.setBackColor(Color.WHITE);
                lpv.setPaintWidth(5);
                lpv.setPenColor(Color.BLACK);
                break;

            case R.id.tv_enter_order:
                if (lpv.getTouched()) {
                    try {
                        lpv.save("/sdcard/qm.png", true, 10);
                        setResult(100);
                        ToastUtils.showToast("签名保存在/sdcard/qm.png");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showToast("您没有签名~");
                }


                break;


        }

    }


    @Override
    protected void init() {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏
        tv_title.setText("客户签名");
        lpv.setPaintWidth(5);

    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_autograph;
    }


}
