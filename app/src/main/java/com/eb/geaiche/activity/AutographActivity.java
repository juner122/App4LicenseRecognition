package com.eb.geaiche.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.eb.geaiche.mvp.FixInfoActivity;
import com.eb.geaiche.mvp.FixInfoDescribeActivity;
import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.util.Auth;
import com.eb.geaiche.util.CommonUtil;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.LinePathView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class AutographActivity extends BaseActivity {

    private static final String TAG = "签名";
    @BindView(R.id.lpv)
    LinePathView lpv;//手写签名

    @OnClick({R.id.tv_fix_order, R.id.tv_enter_order})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_fix_order:

                lpv.clear();
                lpv.setBackColor(Color.WHITE);
                lpv.setPaintWidth(5);
                break;

            case R.id.tv_enter_order:
                if (lpv.getTouched()) {
                    try {
                        lpv.save(Configure.LinePathView_url, true, 10);
                        setResult(100);
                        upPic();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showToast("您没有签名~");
                }


                break;


        }

    }

    private ProgressDialog dialog;

    //上传签名图片
    private void upPic() {
        String key = "pic_" + CommonUtil.getTimeStame();
        String path = Configure.LinePathView_url;

        dialog = new ProgressDialog(this);
        dialog.setMessage("签名提交中...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        UploadManager uploadManager = new UploadManager();
        UploadOptions uploadOptions = new UploadOptions(null, null, false,
                new UpProgressHandler() {
                    public void progress(String key, double percent) {
                        Log.i(TAG, key + ": " + "上传进度:" + percent);//上传进度
                        if (percent == 1.0)//上传进度等于1.0说明上传完成,通知 完成任务+1
                        {
//                            ToastUtils.showToast("签名上传成功");
                            if (null != dialog)
                                dialog.dismiss();
                        }
                    }
                }, null);


        uploadManager.put(path, key, Auth.create(Configure.accessKey, Configure.secretKey).uploadToken(Configure.bucket), new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        // info.error中包含了错误信息，可打印调试
                        // 上传成功后将key值上传到自己的服务器
                        if (info.isOK()) {
                            Log.i(TAG, "upList      ResponseInfo: " + info + "\nkey::" + key);
                            if (null != dialog)
                                dialog.dismiss();


                            if (getIntent().getStringExtra("class").equals("FixInfoDescribe"))
                                toActivity(FixInfoDescribeActivity.class, Configure.Domain, Configure.Domain + key);
                            if (getIntent().getStringExtra("class").equals("UserAuthorize")) {
                                Intent intent = new Intent(AutographActivity.this, FixInfoActivity.class);
                                intent.putExtra(Configure.Domain, Configure.Domain + key);
                                intent.putExtra("from", 101);
                                startActivity(intent);
                            }
                            if (getIntent().getStringExtra("class").equals("MakeOrderSuccess"))
                                toActivity(MakeOrderSuccessActivity.class, Configure.Domain, Configure.Domain + key);

                            if (getIntent().getStringExtra("class").equals("MakeOrder"))
                                toActivity(MakeOrderActivity.class, Configure.Domain, Configure.Domain + key);
                            if (getIntent().getStringExtra("class").equals("OrderInfo"))
                                toActivity(OrderInfoActivity.class, Configure.Domain, Configure.Domain + key);


                        } else {
                            Log.i(TAG, "签名上传失败！");
                        }
                    }
                }, uploadOptions
        );

    }


    @Override
    protected void init() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != dialog)
            dialog.dismiss();
    }

    //横竖屏切换防止重载
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
