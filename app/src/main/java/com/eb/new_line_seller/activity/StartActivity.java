package com.eb.new_line_seller.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.eb.new_line_seller.Configure;

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

    }


    private void toActivity() {

        String token = new AppPreferences(this).getString(Configure.Token, "");
        if (token.equals(""))
            startActivity(new Intent(this, LoginActivity.class));
        else
            startActivity(new Intent(this, MainActivity.class));
        finish();

    }

}
