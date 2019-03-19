package com.eb.geaiche.mvp;

import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.eb.geaiche.R;
import com.eb.geaiche.mvp.contacts.CourseInfoContacts;
import com.eb.geaiche.mvp.presenter.CourseInfoPtr;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.juner.mvp.bean.Courses;


import java.util.ArrayList;

import butterknife.BindView;


//课程信息页面
public class CourseInfoActivity extends BaseActivity<CourseInfoContacts.CourseInfoPtr> implements CourseInfoContacts.CourseInfoUI {


    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.head_view)
    View head_view;//标题栏

    @BindView(R.id.ll_info)
    LinearLayout ll_info;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tv_text)
    TextView tv_text;

    @BindView(R.id.video_view)
    AliyunVodPlayerView video_view;

    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"课程介绍", "课程目录"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_course_info2;
    }

    @Override
    protected void init() {
        tv_title.setText(getIntent().getStringExtra("courseName"));
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                if (position == 0) {
                    rv.setVisibility(View.GONE);
                    ll_info.setVisibility(View.VISIBLE);
                } else {
                    rv.setVisibility(View.VISIBLE);
                    ll_info.setVisibility(View.GONE);
                }

            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        showTitlebar();
        getPresenter().getInfo();
        getPresenter().initRecyclerView(rv);
        getPresenter().initPlayerView(video_view);
        getPresenter().initVideoListView(rv);

    }

    @Override
    public CourseInfoContacts.CourseInfoPtr onBindPresenter() {
        return new CourseInfoPtr(this);
    }

    @Override
    public void setInfo(Courses courses) {
        title.setText(courses.getCourseName());
//        tv_price.setText(String.format("￥%s", courses.getCoursePrice()));
        tv_price.setText("免费");
        tv_type.setText(String.format("适用人群：%s", courses.getSuitable()));
        tv_number.setText(String.format("学习人次：%s人", courses.getPageView()));
        tv_text.setText(Html.fromHtml(courses.getCourseMarke()));
    }

    @Override
    public void showTitlebar() {
        head_view.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideTitlebar() {
        head_view.setVisibility(View.GONE);
    }



    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().updatePlayerViewMode();
        if (video_view != null) {
            video_view.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (video_view != null) {
            getPresenter().addWatchLog();//保存进度
            video_view.onStop();

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getPresenter().updatePlayerViewMode();
    }

    @Override
    protected void onDestroy() {
        if (video_view != null) {
            video_view.onDestroy();
            video_view = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (video_view != null) {
            boolean handler = video_view.onKeyDown(keyCode, event);
            if (!handler) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //解决某些手机上锁屏之后会出现标题栏的问题。
        getPresenter().updatePlayerViewMode();
    }


    class TabEntity implements CustomTabEntity {
        public String title;

        public TabEntity(String title) {
            this.title = title;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return 0;
        }

        @Override
        public int getTabUnselectedIcon() {
            return 0;
        }

    }
}
