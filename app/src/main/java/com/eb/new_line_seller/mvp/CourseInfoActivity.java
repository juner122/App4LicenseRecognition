package com.eb.new_line_seller.mvp;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alivc.player.VcPlayerLog;
import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunVidSts;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;

import com.aliyun.vodplayerview.constants.PlayParameter;
import com.aliyun.vodplayerview.playlist.AlivcPlayListAdapter;
import com.aliyun.vodplayerview.playlist.AlivcPlayListManager;
import com.aliyun.vodplayerview.playlist.AlivcVideoInfo;
import com.aliyun.vodplayerview.utils.FixedToastUtils;
import com.aliyun.vodplayerview.utils.ScreenUtils;
import com.aliyun.vodplayerview.utils.VidStsUtil;
import com.aliyun.vodplayerview.view.tipsview.ErrorInfo;
import com.aliyun.vodplayerview.widget.AliyunScreenMode;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.MainActivity;
import com.eb.new_line_seller.mvp.contacts.CourseInfoContacts;
import com.eb.new_line_seller.mvp.presenter.CourseInfoPtr;
import com.eb.new_line_seller.util.ToastUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.juner.mvp.bean.Courses;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


//课程信息页面
public class CourseInfoActivity extends BaseActivity<CourseInfoContacts.CourseInfoPtr> implements CourseInfoContacts.CourseInfoUI {


    @BindView(R.id.rv)
    RecyclerView rv;

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
        tv_title.setText("哥爱车学院");
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

        getPresenter().requestVidSts();
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
        tv_price.setText(String.format("￥%s", courses.getCoursePrice()));
        tv_type.setText(String.format("适用人群：%s", courses.getCourseType()));
        tv_number.setText(String.format("学习人次：%s人", courses.getPageView()));
        tv_text.setText(courses.getCourseMarke());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPresenter().setPlaySource();
        getPresenter().loadPlayList();
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
