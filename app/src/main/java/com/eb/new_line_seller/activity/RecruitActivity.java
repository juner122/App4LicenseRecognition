package com.eb.new_line_seller.activity;


import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.eb.new_line_seller.R;

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
                content_url = Uri.parse("https://qy.58.com/36379426/?PGTID=0d302408-0000-3494-3d3e-2ce93178655e&ClickID=3");

                break;
            case R.id.ll2:

                content_url = Uri.parse("https://jobs.51job.com/all/co264282.html");
                break;
            case R.id.ll3:

                content_url = Uri.parse("http://search.chinahr.com/gz/job/?key=%E6%96%B0%E5%B9%B2%E7%BA%BF");
                break;
            case R.id.ll4:

                content_url = Uri.parse("http://www.ganji.com/gongsi/105965025/");
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
