package com.eb.geaiche.mvp.contacts;


import android.support.v7.widget.RecyclerView;

import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.CourseInfo;
import com.juner.mvp.bean.CourseRecord;
import com.juner.mvp.bean.Courses;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Video;


//纸卡录入
public class CourseInfoContacts {


    /**
     * view 层接口
     */
    public interface CourseInfoUI extends IBaseView {

        void setInfo(Courses courses);

        void showTitlebar();

        void hideTitlebar();


    }

    /**
     * presenter 层接口
     */
    public interface CourseInfoPtr extends IBasePresenter {

        void initRecyclerView(RecyclerView rv);

        void getInfo();

        void initPlayerView(AliyunVodPlayerView video_view);


        void initVideoListView(RecyclerView rv);



        void onNext();






        void updatePlayerViewMode();

        void addWatchLog();//保存观看进度

    }

    /**
     * model 层接口
     */
    public interface CourseInfoMdl {


        void getInfo(int id, RxSubscribe<CourseInfo> rxSubscribe);//页面数据接口

        void addWatchLog(CourseRecord courseRecord, RxSubscribe<NullDataEntity> rxSubscribe);//用户点击视频观看退出时访问，用来增加记录

        void resourceUrl(String videoId, RxSubscribe<Video> rxSubscribe);//根据vid获取视频


    }


}
