package com.eb.geaiche.activity;


import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.eb.geaiche.R;

import butterknife.OnClick;

public class RecruitActivity extends BaseActivity {
    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_recruit;
    }

    @OnClick({R.id.ll1, R.id.ll2, R.id.ll3, R.id.ll4})
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("");


        switch (v.getId()) {
            case R.id.ll1:
                content_url = Uri.parse("https://jianli.58.com/?from=pc_topbar_link_job");

                break;
            case R.id.ll2:

                content_url = Uri.parse("https://ehire.51job.com/");
                break;
            case R.id.ll3:

                content_url = Uri.parse("https://signup.zhipin.com/?intent=1&ka=header-boss");
                break;
            case R.id.ll4:

                content_url = Uri.parse("https://ihr.zhaopin.com/register_2_pc/?invuserid=100822&invtp=8&invmode=3");
                break;
        }

        intent.setData(content_url);
        startActivity(intent);

    }


    @Override
    protected void init() {
        tv_title.setText("招聘");
    }

    @Override
    protected void setUpView() {

    }

    @Override
    protected void setUpData() {

    }


}
