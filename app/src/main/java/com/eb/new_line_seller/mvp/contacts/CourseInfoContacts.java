package com.eb.new_line_seller.mvp.contacts;


import android.support.v7.widget.RecyclerView;
import android.view.SurfaceView;

import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayerview.playlist.AlivcVideoInfo;
import com.aliyun.vodplayerview.utils.VidStsUtil;
import com.aliyun.vodplayerview.view.tipsview.ErrorInfo;
import com.aliyun.vodplayerview.widget.AliyunScreenMode;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.CourseInfo;
import com.juner.mvp.bean.Courses;

import java.util.ArrayList;


//纸卡录入
public class CourseInfoContacts {


    /**
     * view 层接口
     */
    public interface CourseInfoUI extends IBaseView {

        void setInfo(Courses courses);





    }

    /**
     * presenter 层接口
     */
    public interface CourseInfoPtr extends IBasePresenter {

        void initRecyclerView(RecyclerView rv);

        void getInfo();

        void initPlayerView(AliyunVodPlayerView video_view);

        void requestVidSts();
        void initVideoListView(RecyclerView rv);

        void loadPlayList();
        void onNext();
        void onStsSuccess(String vid, final String akid, final String akSecret, final String token);

        void setPlaySource();

        void setInRequest(boolean is);
        void setCurrentError(ErrorInfo error);
        void updatePlayerViewMode();



    }

    /**
     * model 层接口
     */
    public interface CourseInfoMdl {


        void getInfo(int id, RxSubscribe<CourseInfo> rxSubscribe);//页面数据接口


    }


}
