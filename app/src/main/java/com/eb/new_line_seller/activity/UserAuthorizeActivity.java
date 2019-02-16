package com.eb.new_line_seller.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.mvp.FixInfoActivity;
import com.juner.mvp.Configure;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;
import static com.bumptech.glide.request.RequestOptions.skipMemoryCacheOf;

public class UserAuthorizeActivity extends BaseActivity {

    String iv_lpv_url = "";//签名图片 七牛云url
    @BindView(R.id.iv_lpv)
    ImageView iv_lpv;

    @OnClick({R.id.tv_confirm,R.id.ll_autograph})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_confirm:
                Intent intent = new Intent(this, FixInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("from", 101);
                bundle.putString("lpv_url", iv_lpv_url);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.ll_autograph:
                toActivity(AutographActivity.class, "class", "UserAuthorize");
                break;
        }


    }

    @Override
    protected void init() {

        tv_title.setText("客户授权凭证");

    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_user_authorize;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Glide.with(this)
                .asDrawable()
                .load(Uri.fromFile(new File(Configure.LinePathView_url)))
                .apply(diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(skipMemoryCacheOf(true))
                .into(iv_lpv);

        iv_lpv_url = intent.getStringExtra(Configure.Domain);
    }


}
