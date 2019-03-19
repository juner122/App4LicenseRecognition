package com.eb.geaiche.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.eb.geaiche.R;
import com.eb.geaiche.mvp.FixInfoActivity;
import com.juner.mvp.Configure;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;
import static com.bumptech.glide.request.RequestOptions.skipMemoryCacheOf;

public class UserAuthorizeActivity extends BaseActivity {

    String iv_lpv_url = "";//签名图片 七牛云url
    @BindView(R.id.iv_lpv)
    ImageView iv_lpv;
    @BindView(R.id.iv_enter)
    ImageView iv_enter;

    @BindView(R.id.ll_autograph)
    View ll_autograph;
    @BindView(R.id.ll_bottom)
    View ll_bottom;

    @OnClick({R.id.tv_confirm, R.id.ll_autograph})
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

        int type = getIntent().getIntExtra("type", -1);//0 待确认  1 已确认 已出单

        if (type == 0) {


        } else if (type == 1) {
            showAuthorize(getIntent().getStringExtra("lpv_url"));
            iv_enter.setVisibility(View.INVISIBLE);
            ll_autograph.setOnClickListener(null);
            ll_bottom.setVisibility(View.GONE);
        }


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
        iv_lpv_url = intent.getStringExtra(Configure.Domain);
        showAuthorize(iv_lpv_url);

    }

    private void showAuthorize(String iv_lpv_url) {
        Glide.with(this)
                .asDrawable()
                .load(iv_lpv_url)
                .apply(diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(skipMemoryCacheOf(true))
                .into(iv_lpv);
    }


}
