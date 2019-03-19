package com.eb.geaiche.mvp.presenter;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.alivc.player.VcPlayerLog;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayerview.constants.PlayParameter;
import com.aliyun.vodplayerview.playlist.AlivcPlayListAdapter;
import com.aliyun.vodplayerview.playlist.AlivcVideoInfo;
import com.aliyun.vodplayerview.utils.ScreenUtils;
import com.aliyun.vodplayerview.view.tipsview.ErrorInfo;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.eb.geaiche.adapter.ResourcePojosAdapter;
import com.eb.geaiche.mvp.contacts.CourseInfoContacts;
import com.eb.geaiche.mvp.model.CourseInfoMdl;
import com.eb.geaiche.util.MathUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.CourseInfo;
import com.juner.mvp.bean.CourseRecord;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.PlayInfoList;
import com.juner.mvp.bean.ResourcePojos;
import com.juner.mvp.bean.Video;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CourseInfoPtr extends BasePresenter<CourseInfoContacts.CourseInfoUI> implements CourseInfoContacts.CourseInfoPtr {

    private CourseInfoContacts.CourseInfoMdl mdl;
    private Activity context;
    ResourcePojosAdapter adapter;
    AliyunVodPlayerView video_view;
    /**
     * get StsToken stats
     */
    private int currentVideoPosition;
    private AlivcPlayListAdapter alivcPlayListAdapter;
    private ArrayList<AlivcVideoInfo.Video> alivcVideoInfos;

    private long oldTime;

    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SS");
    String courseName;
    int resourceId;

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
        courseName = context.getIntent().getStringExtra("courseName");//课程名
        resourceId = context.getIntent().getIntExtra("resourceId", -1);//课程下的节id

        mdl.getInfo(id, new RxSubscribe<CourseInfo>(context, true) {
            @Override
            protected void _onNext(CourseInfo courseInfo) {
                getView().setInfo(courseInfo.getCourses());
                List<ResourcePojos> list = courseInfo.getResourcePojos();
                Collections.sort(list);


                adapter.setNewData(list);

                getAlivcVideoInfos(list);

                if (list.size() == 0) {
                    ToastUtils.showToast("暂无视频！");
                    return;
                }

                if (resourceId == -1) {//从课程列表进入
                    //播放第一个视频
                    onChangePlaySource(list.get(0).getCourseMv(), 0);

                } else {//从学习记录进入
                    int pastTime = context.getIntent().getIntExtra("pastTime", 0);//学习位置

                    for (ResourcePojos pojos : list) {
                        if (pojos.getId() == resourceId) {
                            onChangePlaySource(pojos.getCourseMv(), pastTime);//获取vid
                            break;
                        }
                    }

                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });


    }

    private void getAlivcVideoInfos(List<ResourcePojos> list) {


        for (ResourcePojos resourcePojos : list) {
            AlivcVideoInfo.Video video = new AlivcVideoInfo.Video();

            video.setCoverURL(resourcePojos.getCourseImg());//封面
            video.setVideoId(resourcePojos.getCourseMv());
            video.setTitle(resourcePojos.getName());
            video.setDuration(String.valueOf(resourcePojos.getTimeLength()));//视频长度
            alivcVideoInfos.add(video);

        }


    }


    @Override
    public void initPlayerView(AliyunVodPlayerView mAliyunVodPlayerView) {
        video_view = mAliyunVodPlayerView;
        //保持屏幕敞亮
        video_view.setKeepScreenOn(true);
        String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_save_cache";
        video_view.setPlayingCache(false, sdDir, 60 * 60 /*时长, s */, 300 /*大小，MB*/);
        video_view.setTheme(AliyunVodPlayerView.Theme.Blue);
        //video_view.setCirclePlay(true);
        video_view.setAutoPlay(true);
        video_view.setCirclePlay(false);

//        video_view.setOnPreparedListener(new MyPrepareListener((CourseInfoActivity) getView().getSelfActivity()));//设置准备监听器
//        video_view.setNetConnectedListener(new MyNetConnectedListener());//网络监听器
        video_view.setOnCompletionListener(new MyCompletionListener());//单个视频播放结束监听器
//        video_view.setOnFirstFrameStartListener(new MyFrameInfoListener());//首帧显示触发
//        video_view.setOnChangeQualityListener(new MyChangeQualityListener());//视频清晰度切换
        video_view.setOnStoppedListener(new MyStoppedListener());
//        video_view.setmOnPlayerViewClickListener(new MyPlayViewClickListener());
//        video_view.setOrientationChangeListener(new MyOrientationChangeListener());
//        video_view.setOnTimeExpiredErrorListener(new MyOnTimeExpiredErrorListener());
//        video_view.setOnShowMoreClickListener(new MyShowMoreClickLisener());
        video_view.setOnPlayStateBtnClickListener(new MyPlayStateBtnClickListener());//播放按钮点击监听
//        video_view.setOnSeekCompleteListener(new MySeekCompleteListener());//设置拖动结束监听事件
//        video_view.setOnSeekStartListener(new MySeekStartListener());//seek开始
        video_view.enableNativeLog();
    }


    /**
     * init视频列表tab
     */

    @Override
    public void initVideoListView(RecyclerView recyclerView) {

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        alivcVideoInfos = new ArrayList<>();
        alivcPlayListAdapter = new AlivcPlayListAdapter(context, alivcVideoInfos);
        recyclerView.setAdapter(alivcPlayListAdapter);
        alivcPlayListAdapter.setOnVideoListItemClick(new AlivcPlayListAdapter.OnVideoListItemClick() {
            @Override
            public void onItemClick(int position) {
                long currentClickTime = System.currentTimeMillis();
                // 防止快速点击
                if (currentClickTime - oldTime <= 2000) {
                    return;
                }
                PlayParameter.PLAY_PARAM_TYPE = "localSource";
                // 点击视频列表, 切换播放的视频
                onChangePlaySource(alivcVideoInfos.get(position).getVideoId(), 0);
                oldTime = currentClickTime;

            }
        });


    }



    private ErrorInfo currentError = ErrorInfo.Normal;

    @Override
    public void onNext() {
        if (currentError == ErrorInfo.UnConnectInternet) {
            // 此处需要判断网络和播放类型
            // 网络资源, 播放完自动波下一个, 无网状态提示ErrorTipsView
            // 本地资源, 播放完需要重播, 显示Replay, 此处不需要处理
            if ("vidsts".equals(PlayParameter.PLAY_PARAM_TYPE)) {
                video_view.showErrorTipView(4014, -1, "当前网络不可用");
            }
            return;
        }
        currentVideoPosition++;
        if (currentVideoPosition >= alivcVideoInfos.size() - 1) {
            //播放完最后一个 停止播放
            currentVideoPosition = 0;
            return;
        }

        if (alivcVideoInfos.size() > 0) {
            AlivcVideoInfo.Video video = alivcVideoInfos.get(currentVideoPosition);
            if (video != null) {
                onChangePlaySource(video.getVideoId(), 0);
            }
        }
    }




    private class MyCompletionListener implements IAliyunVodPlayer.OnCompletionListener {
        @Override
        public void onCompletion() {
            Log.d("阿里云播放信息", com.aliyun.vodplayer.R.string.toast_play_compleion + "\n");
            ToastUtils.showToast(getView().getSelfActivity().getResources().getString(com.aliyun.vodplayer.R.string.toast_play_compleion));
            onNext();
        }
    }
    private class MyStoppedListener implements IAliyunVodPlayer.OnStoppedListener {


        @Override
        public void onStopped() {

            //停止播放
            Log.d("阿里云播放信息", com.aliyun.vodplayer.R.string.log_play_stopped + "\n");

            addWatchLog();

        }
    }

    //保存观看进度
    @Override
    public void addWatchLog() {
        mdl.addWatchLog(getCourseRecord(), new RxSubscribe<NullDataEntity>(context, false) {
            @Override
            protected void _onNext(NullDataEntity entity) {
                Log.d("阿里云播放信息", "保存观看进度成功" + "\n");
            }
            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }


    private CourseRecord getCourseRecord() {
        ResourcePojos resourcePojos = adapter.getItem(currentVideoPosition);//资源


        CourseRecord courseRecord = new CourseRecord();//记录
        courseRecord.setCourseId(resourcePojos.getCourseId());
        courseRecord.setResourceId(resourcePojos.getId());
        courseRecord.setCourseName(courseName);
        courseRecord.setResourceName(resourcePojos.getName());
        courseRecord.setHistoryTime(MathUtil.percent(video_view.getCurrentPosition(), video_view.getDuration()));//获取播放的当前时间，单位为毫秒


        courseRecord.setPastTime(video_view.getCurrentPosition());//获取播放的当前时间，单位为毫秒

        courseRecord.setResourceImg(resourcePojos.getCourseImg());
        return courseRecord;
    }


    private class MyPlayStateBtnClickListener implements AliyunVodPlayerView.OnPlayStateBtnClickListener {


        @Override
        public void onPlayBtnClick(IAliyunVodPlayer.PlayerState playerState) {

            onPlayStateSwitch(playerState);
        }
    }


    public void onPlayStateSwitch(IAliyunVodPlayer.PlayerState playerState) {

        if (playerState == IAliyunVodPlayer.PlayerState.Started) {

            Log.d("阿里云播放信息", format.format(new Date()) + "暂停 \n");

        } else if (playerState == IAliyunVodPlayer.PlayerState.Paused) {

            Log.d("阿里云播放信息", format.format(new Date()) + " 开始 \n");
        }

    }


    //根据vid获取播放视频url
    private void onChangePlaySource(final String videoId, final int pastTime) {

        mdl.resourceUrl(videoId, new RxSubscribe<Video>(context, true) {
            @Override
            protected void _onNext(Video video) {

                String playUrl = "";

                for (PlayInfoList.PlayInfo playInfo : video.getPlayInfoList().getPlayInfo()) {
                    if (playInfo.getFormat().equals("mp4")) {
                        playUrl = playInfo.getPlayURL();
                    }
                }

                if (pastTime == 0)
                    changePlayURLSource(playUrl, video.getVideoBase().getTitle());
                else
                    changePlayURLSourceSeekTo(playUrl, video.getVideoBase().getTitle(), pastTime);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });
    }


    /**
     * 切换播放url资源
     * * @param title 切换视频的title
     */

    public void changePlayURLSource(String url, String title) {
        AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
        PlayParameter.PLAY_PARAM_URL = url;
        alsb.setSource(PlayParameter.PLAY_PARAM_URL);
        alsb.setTitle(title);
        AliyunLocalSource localSource = alsb.build();
        video_view.setLocalSource(localSource);
    }

    /**
     * 切换播放url资源 跳转到指定时间点的视频画面，时间单位为毫秒
     * * @param title 切换视频的title
     */

    public void changePlayURLSourceSeekTo(String url, String title, int s) {
        AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
        PlayParameter.PLAY_PARAM_URL = url;
        alsb.setSource(PlayParameter.PLAY_PARAM_URL);
        alsb.setTitle(title);
        AliyunLocalSource localSource = alsb.build();
        video_view.seekTo(s);
        video_view.setLocalSource(localSource);
    }


    @Override
    public void updatePlayerViewMode() {
        if (video_view != null) {
            int orientation = getView().getSelfActivity().getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                //转为竖屏了。
                //显示状态栏
                //                if (!isStrangePhone()) {
                //                    getSupportActionBar().show();
                //                }
                getView().showTitlebar();
                getView().getSelfActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                video_view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                //设置view的布局，宽高之类
                LinearLayout.LayoutParams aliVcVideoViewLayoutParams = (LinearLayout.LayoutParams) video_view
                        .getLayoutParams();
                aliVcVideoViewLayoutParams.height = (int) (ScreenUtils.getWidth(context) * 9.0f / 16);
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                //                if (!isStrangePhone()) {
                //                    aliVcVideoViewLayoutParams.topMargin = getSupportActionBar().getHeight();
                //                }

            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                getView().hideTitlebar();
                //转到横屏了。
                //隐藏状态栏
                if (!isStrangePhone()) {
                    getView().getSelfActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    video_view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }

                //设置view的布局，宽高
                LinearLayout.LayoutParams aliVcVideoViewLayoutParams = (LinearLayout.LayoutParams) video_view
                        .getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                //                if (!isStrangePhone()) {
                //                    aliVcVideoViewLayoutParams.topMargin = 0;
                //                }
            }

        }
    }

    private boolean isStrangePhone() {
        boolean strangePhone = "mx5".equalsIgnoreCase(Build.DEVICE)
                || "Redmi Note2".equalsIgnoreCase(Build.DEVICE)
                || "Z00A_1".equalsIgnoreCase(Build.DEVICE)
                || "hwH60-L02".equalsIgnoreCase(Build.DEVICE)
                || "hermes".equalsIgnoreCase(Build.DEVICE)
                || ("V4".equalsIgnoreCase(Build.DEVICE) && "Meitu".equalsIgnoreCase(Build.MANUFACTURER))
                || ("m1metal".equalsIgnoreCase(Build.DEVICE) && "Meizu".equalsIgnoreCase(Build.MANUFACTURER));

        VcPlayerLog.e("lfj1115 ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone);
        return strangePhone;
    }


}
