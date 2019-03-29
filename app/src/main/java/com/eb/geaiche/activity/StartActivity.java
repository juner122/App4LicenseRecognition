package com.eb.geaiche.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eb.geaiche.R;
import com.eb.geaiche.adapter.WecomePagerAdapter;
import com.eb.geaiche.mvp.LoginActivity2;
import com.eb.geaiche.service.GeTuiIntentService;
import com.eb.geaiche.service.GeTuiPushService;
import com.igexin.sdk.PushManager;
import com.juner.mvp.Configure;

import net.grandcentrix.tray.AppPreferences;

import java.util.ArrayList;
import java.util.List;


public class StartActivity extends PermissionsActivity implements View.OnClickListener {


    String[] strings = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    TextView tv_ske, tv_start;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);




        if (!isHavePermissionsList(strings)) {
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
        String token = new AppPreferences(this).getString(Configure.Token, "");


        if (token.equals("")) {
            init();
        }else {
            toActivity();
        }
    }

    private void init() {
        tv_ske = findViewById(R.id.tv_ske);
        tv_start = findViewById(R.id.tv_start);

        tv_ske.setOnClickListener(this);
        tv_start.setOnClickListener(this);

        List<View> views = new ArrayList<>();
        View v1 = getLayoutInflater().inflate(R.layout.activity_start_item1, null);
        View v2 = getLayoutInflater().inflate(R.layout.activity_start_item2, null);
        View v3 = getLayoutInflater().inflate(R.layout.activity_start_item3, null);

        views.add(v1);
        views.add(v2);
        views.add(v3);

        WecomePagerAdapter myPagerAdapter = new WecomePagerAdapter(views);
        final ViewPager viewPager = findViewById(R.id.vp);
        viewPager.setAdapter(myPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position == 2) {
                    tv_start.setVisibility(View.VISIBLE);
                } else {

                    tv_start.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ske:
                toActivity();
                break;

            case R.id.tv_start:
                toActivity();
                break;

        }
    }
}
