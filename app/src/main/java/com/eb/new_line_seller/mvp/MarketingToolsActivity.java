package com.eb.new_line_seller.mvp;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.BaseActivity;
import com.eb.new_line_seller.util.ToastUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.OnClick;

public class MarketingToolsActivity extends BaseActivity {


    @OnClick({R.id.ll1, R.id.ll2})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll1:

                //短信营销

                toActivity(MessageMarketingActivity.class);
                break;
            case R.id.ll2:
                //分享营销
//                ToastUtils.showToast("开发中");
                UMImage thumb =  new UMImage(this, R.mipmap.s_p_c);


                new ShareAction(this).withText("关注小程序").withMedia(thumb).setDisplayList(SHARE_MEDIA.DINGTALK,SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN,SHARE_MEDIA.QQ)
                        .setCallback(shareListener).open();
                break;

        }
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {

//            ToastUtils.showToast("成功了");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {

            ToastUtils.showToast("失败" + t.getMessage());
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {

//            ToastUtils.showToast("取消了");
        }
    };

    @Override
    protected void init() {

        tv_title.setText("营销工具");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_marketing_tools;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
}
