package com.eb.geaiche.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.eb.geaiche.mvp.LoginActivity2;
import com.eb.geaiche.service.GeTuiIntentService;
import com.eb.geaiche.service.GeTuiPushService;
import com.igexin.sdk.PushManager;
import com.juner.mvp.Configure;

import net.grandcentrix.tray.AppPreferences;

public class StartActivity extends PermissionsActivity {


    String[] strings = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (isHavePermissionsList(strings))
            toActivity();
        else {
            checkPermissions(strings, 300, new PermissionsResultListener() {
                @Override
                public void onSuccessful(int[] grantResults) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(StartActivity.this, "同意权限", Toast.LENGTH_SHORT).show();
                            toActivity();
                        } else {
                            Toast.makeText(StartActivity.this, "拒绝权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure() {
                    Toast.makeText(StartActivity.this, "失败", Toast.LENGTH_SHORT).show();
                }
            });
        }



        //初始化个推
        PushManager.getInstance().initialize(this.getApplicationContext(), GeTuiPushService.class);
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), GeTuiIntentService.class);

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        toActivity();
    }

    private void toActivity() {

        String token = new AppPreferences(this).getString(Configure.Token, "");
        if (token.equals(""))
//            startActivity(new Intent(this, LoginActivity.class));
            startActivity(new Intent(this, LoginActivity2.class));
        else
            startActivity(new Intent(this, MainActivity.class));
        finish();

    }

}
