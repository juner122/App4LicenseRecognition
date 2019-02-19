package com.eb.new_line_seller.mvp.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SurfaceView;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.eb.new_line_seller.adapter.ResourcePojosAdapter;
import com.eb.new_line_seller.mvp.contacts.CourseInfoContacts;
import com.eb.new_line_seller.mvp.model.CourseInfoMdl;
import com.eb.new_line_seller.util.ToastUtils;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.CourseInfo;
import com.juner.mvp.bean.ResourcePojos;

import java.util.Collections;
import java.util.List;

public class CourseInfoPtr extends BasePresenter<CourseInfoContacts.CourseInfoUI> implements CourseInfoContacts.CourseInfoPtr {

    private CourseInfoContacts.CourseInfoMdl mdl;
    private Activity context;
    ResourcePojosAdapter adapter;

    AliVcMediaPlayer mPlayer;//播放器

    public CourseInfoPtr(@NonNull CourseInfoContacts.CourseInfoUI view) {
        super(view);
        context = view.getSelfActivity();
        mdl = new CourseInfoMdl(context);
        adapter = new ResourcePojosAdapter(context, null);
    }

    @Override
    public void initRecyclerView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });

        rv.setAdapter(adapter);



    }

    @Override
    public void getInfo() {
        int id = context.getIntent().getIntExtra("id", -1);

        mdl.getInfo(id, new RxSubscribe<CourseInfo>(context, true) {
            @Override
            protected void _onNext(CourseInfo courseInfo) {


                getView().setInfo(courseInfo.getCourses());


                List<ResourcePojos> list = courseInfo.getResourcePojos();

                Collections.sort(list);
                adapter.setNewData(list);

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }

    @Override
    public void initSurfaceView(SurfaceView surfaceView) {

        mPlayer = new AliVcMediaPlayer(context, surfaceView);

        mPlayer.setPreparedListener(new MediaPlayer.MediaPlayerPreparedListener() {
            @Override
            public void onPrepared() {
                //准备完成时触发
            }
        });
        mPlayer.setPcmDataListener(new MediaPlayer.MediaPlayerPcmDataListener() {
            @Override
            public void onPcmData(byte[] bytes, int i) {
                //音频数据回调接口，在需要处理音频时使用，如拿到视频音频，然后绘制音柱。
            }
        });

        mPlayer.setErrorListener(new MediaPlayer.MediaPlayerErrorListener() {
            @Override
            public void onError(int i, String msg) {
                //错误发生时触发，错误码见接口文档
            }
        });
        mPlayer.setCompletedListener(new MediaPlayer.MediaPlayerCompletedListener() {
            @Override
            public void onCompleted() {
                //视频正常播放完成时触发
            }
        });
        mPlayer.setSeekCompleteListener(new MediaPlayer.MediaPlayerSeekCompleteListener() {
            @Override
            public void onSeekCompleted() {
                //视频seek完成时触发
            }
        });

        mPlayer.setCircleStartListener(new MediaPlayer.MediaPlayerCircleStartListener() {
            @Override
            public void onCircleStart() {
                //循环播放开始
            }
        });
        //SEI数据回调
        mPlayer.setSEIDataListener(new MediaPlayer.MediaPlayerSEIDataListener() {
            @Override
            public void onSeiUserUnregisteredData(String data) {
                //解析SEI数据，在这里可以展示题目信息或答案信息
            }
        });



    }

    @Override
    public void play() {
        if (mPlayer != null) {
            mPlayer.prepareAndPlay("http://player.alicdn.com/video/aliyunmedia.mp4");
        }
        //开始播放
        mPlayer.play();


    }
}
