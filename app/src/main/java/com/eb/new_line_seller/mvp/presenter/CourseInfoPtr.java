package com.eb.new_line_seller.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.alivc.player.VcPlayerLog;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.AliyunVidSts;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayerview.activity.AliyunPlayerSettingActivity;
import com.aliyun.vodplayerview.activity.AliyunPlayerSkinActivity;
import com.aliyun.vodplayerview.constants.PlayParameter;
import com.aliyun.vodplayerview.playlist.AlivcPlayListAdapter;
import com.aliyun.vodplayerview.playlist.AlivcPlayListManager;
import com.aliyun.vodplayerview.playlist.AlivcVideoInfo;
import com.aliyun.vodplayerview.utils.FixedToastUtils;
import com.aliyun.vodplayerview.utils.ScreenUtils;
import com.aliyun.vodplayerview.utils.VidStsUtil;
import com.aliyun.vodplayerview.view.control.ControlView;
import com.aliyun.vodplayerview.view.tipsview.ErrorInfo;
import com.aliyun.vodplayerview.widget.AliyunScreenMode;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.eb.new_line_seller.adapter.ResourcePojosAdapter;
import com.eb.new_line_seller.mvp.BaseActivity;
import com.eb.new_line_seller.mvp.CourseInfoActivity;
import com.eb.new_line_seller.mvp.contacts.CourseInfoContacts;
import com.eb.new_line_seller.mvp.model.CourseInfoMdl;
import com.eb.new_line_seller.util.MathUtil;
import com.eb.new_line_seller.util.ToastUtils;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.CourseInfo;
import com.juner.mvp.bean.CourseRecord;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.PlayInfoList;
import com.juner.mvp.bean.ResourcePojos;
import com.juner.mvp.bean.Video;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CourseInfoPtr extends BasePresenter<CourseInfoContacts.CourseInfoUI> implements CourseInfoContacts.CourseInfoPtr {

    private CourseInfoContacts.CourseInfoMdl mdl;
    private Activity context;
    ResourcePojosAdapter adapter;

    private static final String DEFAULT_URL = "https://outin-754ae5212e7311e99d0b00163e1c955c.oss-cn-shanghai.aliyuncs.com/sv/553a6976-16924038886/553a6976-16924038886.mp4?Expires=1551091405&OSSAccessKeyId=LTAI8bKSZ6dKjf44&Signature=dqaia6elxI67jZSw3KXmFX9kMo4%3D";
    private static final String DEFAULT_VID = "6e783360c811449d8692b2117acc9212";
    AliyunVodPlayerView video_view;
    /**
     * get StsToken stats
     */
    private boolean inRequest;
    private int currentVideoPosition;
    private AlivcPlayListAdapter alivcPlayListAdapter;
    private ArrayList<AlivcVideoInfo.Video> alivcVideoInfos;

    private long oldTime;
    private List<String> logStrs = new ArrayList<>();
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
        PlayParameter.PLAY_PARAM_URL = DEFAULT_URL;
        String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_save_cache";
        video_view.setPlayingCache(false, sdDir, 60 * 60 /*时长, s */, 300 /*大小，MB*/);
        video_view.setTheme(AliyunVodPlayerView.Theme.Blue);
        //video_view.setCirclePlay(true);
        video_view.setAutoPlay(true);
        video_view.setCirclePlay(false);

        video_view.setOnPreparedListener(new MyPrepareListener((CourseInfoActivity) getView().getSelfActivity()));//设置准备监听器
        video_view.setNetConnectedListener(new MyNetConnectedListener());
        video_view.setOnCompletionListener(new MyCompletionListener());
        video_view.setOnFirstFrameStartListener(new MyFrameInfoListener());
        video_view.setOnChangeQualityListener(new MyChangeQualityListener());
        video_view.setOnStoppedListener(new MyStoppedListener());
        video_view.setmOnPlayerViewClickListener(new MyPlayViewClickListener());
        video_view.setOrientationChangeListener(new MyOrientationChangeListener());
        video_view.setOnUrlTimeExpiredListener(new MyOnUrlTimeExpiredListener());
        video_view.setOnTimeExpiredErrorListener(new MyOnTimeExpiredErrorListener());
        video_view.setOnShowMoreClickListener(new MyShowMoreClickLisener());
        video_view.setOnPlayStateBtnClickListener(new MyPlayStateBtnClickListener());
        video_view.setOnSeekCompleteListener(new MySeekCompleteListener());
        video_view.setOnSeekStartListener(new MySeekStartListener());
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
//                changePlaySource(position);
                onChangePlaySource(alivcVideoInfos.get(position).getVideoId(), 0);
                oldTime = currentClickTime;

            }
        });


    }


    /**
     * 获取播放列表数据
     */
    @Override
    public void loadPlayList() {

//        AlivcPlayListManager.getInstance().fetchPlayList(PlayParameter.PLAY_PARAM_AK_ID,
//                PlayParameter.PLAY_PARAM_AK_SECRE,
//                PlayParameter.PLAY_PARAM_SCU_TOKEN, new AlivcPlayListManager.PlayListListener() {
//                    @Override
//                    public void onPlayList(int code, final ArrayList<AlivcVideoInfo.Video> videos) {
//                        context.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (alivcVideoInfos != null && alivcVideoInfos.size() == 0) {
//                                    alivcVideoInfos.clear();
//                                    alivcVideoInfos.addAll(videos);
//                                    alivcPlayListAdapter.notifyDataSetChanged();
//                                    // 请求sts成功后, 加载播放资源,和视频列表
//                                    AlivcVideoInfo.Video video = alivcVideoInfos.get(0);
//                                    PlayParameter.PLAY_PARAM_URL = video.getVideoId();
//                                    setPlaySource();
//                                }
//                            }
//                        });
//                    }
//                });
        if (alivcVideoInfos != null && alivcVideoInfos.size() > 0) {
            // 请求sts成功后, 加载播放资源,和视频列表
            AlivcVideoInfo.Video video = alivcVideoInfos.get(0);
            PlayParameter.PLAY_PARAM_URL = video.getCoverURL();
            setPlaySource();
        } else {
            ToastUtils.showToast("暂无视频！");
        }

    }


    @Override
    public void setPlaySource() {
        if ("localSource".equals(PlayParameter.PLAY_PARAM_TYPE)) {
            AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
            alsb.setSource(PlayParameter.PLAY_PARAM_URL);
            Uri uri = Uri.parse(PlayParameter.PLAY_PARAM_URL);
            if ("rtmp".equals(uri.getScheme())) {
                alsb.setTitle("");
            }
            AliyunLocalSource localSource = alsb.build();
            if (video_view != null) {
                video_view.setLocalSource(localSource);
            }


        } else if ("vidsts".equals(PlayParameter.PLAY_PARAM_TYPE)) {
            if (!inRequest) {
                AliyunVidSts vidSts = new AliyunVidSts();
                vidSts.setVid(PlayParameter.PLAY_PARAM_VID);
                vidSts.setAcId(PlayParameter.PLAY_PARAM_AK_ID);
                vidSts.setAkSceret(PlayParameter.PLAY_PARAM_AK_SECRE);
                vidSts.setSecurityToken(PlayParameter.PLAY_PARAM_SCU_TOKEN);
                if (video_view != null) {
                    video_view.setVidSts(vidSts);
                }

            }
        }
    }

    @Override
    public void setInRequest(boolean is) {

    }

    @Override
    public void setCurrentError(ErrorInfo error) {

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

    @Override
    public void onStsSuccess(String mVid, String akid, String akSecret, String token) {
        PlayParameter.PLAY_PARAM_VID = mVid;
        PlayParameter.PLAY_PARAM_AK_ID = akid;
        PlayParameter.PLAY_PARAM_AK_SECRE = akSecret;
        PlayParameter.PLAY_PARAM_SCU_TOKEN = token;

        inRequest = false;

        // 视频列表数据为0时, 加载列表
        if (alivcVideoInfos != null && alivcVideoInfos.size() == 0) {
            alivcVideoInfos.clear();
            loadPlayList();
        }
    }

    /**
     * 切换播放资源
     *
     * @param position 需要播放的数据在集合中的下标
     */
    private void changePlaySource(int position) {
        currentVideoPosition = position;
        AlivcVideoInfo.Video video = alivcVideoInfos.get(position);

        changePlayURLSource(video.getCoverURL(), video.getTitle());


    }


    public void reNetConnected(boolean isReconnect) {
        currentError = ErrorInfo.Normal;
        if (isReconnect) {

            // 如果当前播放列表为空, 网络重连后需要重新请求sts和播放列表, 其他情况不需要
            if (alivcVideoInfos != null && alivcVideoInfos.size() == 0) {
//                VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, new MyStsListener());
            }
        }
    }


    private class MyPrepareListener implements IAliyunVodPlayer.OnPreparedListener {


        private WeakReference<CourseInfoActivity> activityWeakReference;

        public MyPrepareListener(CourseInfoActivity skinActivity) {
            activityWeakReference = new WeakReference<>(skinActivity);
        }

        @Override
        public void onPrepared() {
            CourseInfoActivity activity = activityWeakReference.get();
            if (activity != null) {
                Log.d("阿里云播放信息", com.aliyun.vodplayer.R.string.toast_prepare_success + "\n");
//                ToastUtils.showToast(getView().getSelfActivity().getResources().getString(com.aliyun.vodplayer.R.string.toast_prepare_success));
//                ToastUtils.showToast("开始播放");
            }
        }

    }

    /**
     * 判断是否有网络的监听
     */
    private class MyNetConnectedListener implements AliyunVodPlayerView.NetConnectedListener {


        @Override
        public void onReNetConnected(boolean isReconnect) {


//            reNetConnected(isReconnect);

        }

        @Override
        public void onNetUnConnected() {

            setCurrentError(ErrorInfo.UnConnectInternet);

        }
    }

    public class MyOnTimeExpiredErrorListener implements IAliyunVodPlayer.OnTimeExpiredErrorListener {


        @Override
        public void onTimeExpiredError() {

            VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, new RetryExpiredSts());

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

    private class MyFrameInfoListener implements IAliyunVodPlayer.OnFirstFrameStartListener {


        @Override
        public void onFirstFrameStart() {
            firstFrameStart();

        }
    }

    private class MyChangeQualityListener implements IAliyunVodPlayer.OnChangeQualityListener {


        @Override
        public void onChangeQualitySuccess(String finalQuality) {


            logStrs.add(format.format(new Date()) + getView().getSelfActivity().getString(com.aliyun.vodplayer.R.string.log_change_quality_success));
            ToastUtils.showToast(getView().getSelfActivity().getResources().getString(com.aliyun.vodplayer.R.string.log_change_quality_success));

        }

        @Override
        public void onChangeQualityFail(int code, String msg) {

            logStrs.add(format.format(new Date()) + getView().getSelfActivity().getString(com.aliyun.vodplayer.R.string.log_change_quality_fail) + " : " + msg);
            ToastUtils.showToast(getView().getSelfActivity().getResources().getString(com.aliyun.vodplayer.R.string.log_change_quality_fail));

        }
    }

    private class MyStoppedListener implements IAliyunVodPlayer.OnStoppedListener {


        @Override
        public void onStopped() {

            //停止播放
            Log.d("阿里云播放信息", com.aliyun.vodplayer.R.string.log_play_stopped + "\n");
//            ToastUtils.showToast(getView().getSelfActivity().getResources().getString(com.aliyun.vodplayer.R.string.log_play_stopped));

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


    private class MyPlayViewClickListener implements AliyunVodPlayerView.OnPlayerViewClickListener {
        @Override
        public void onClick(AliyunScreenMode screenMode, AliyunVodPlayerView.PlayViewType viewType) {

            // 如果当前的Type是Download, 就显示Download对话框
            if (viewType == AliyunVodPlayerView.PlayViewType.Download) {
                IAliyunVodPlayer.PlayerState playerState = video_view.getPlayerState();
                if (video_view.isPlaying()) {
//                showAddDownloadView(screenMode);
                }

            }

        }
    }


    private class MyOrientationChangeListener implements AliyunVodPlayerView.OnOrientationChangeListener {


        @Override
        public void orientationChange(boolean from, AliyunScreenMode currentMode) {

//            activity.hideDownloadDialog(from, currentMode);
//            activity.hideShowMoreDialog(from, currentMode);
        }
    }


    private class MyOnUrlTimeExpiredListener implements IAliyunVodPlayer.OnUrlTimeExpiredListener {

        @Override
        public void onUrlTimeExpired(String s, String s1) {

            AliyunVidSts vidSts = VidStsUtil.getVidSts(s);
            PlayParameter.PLAY_PARAM_VID = vidSts.getVid();
            PlayParameter.PLAY_PARAM_AK_SECRE = vidSts.getAkSceret();
            PlayParameter.PLAY_PARAM_AK_ID = vidSts.getAcId();
            PlayParameter.PLAY_PARAM_SCU_TOKEN = vidSts.getSecurityToken();

            if (video_view != null) {
                video_view.setVidSts(vidSts);
            }

        }
    }

    private class MyShowMoreClickLisener implements ControlView.OnShowMoreClickListener {


        @Override
        public void showMore() {


        }
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


    private class MySeekCompleteListener implements IAliyunVodPlayer.OnSeekCompleteListener {


        @Override
        public void onSeekComplete() {


            Log.d("阿里云播放信息", format.format(new Date()) + getView().getSelfActivity().getString(com.aliyun.vodplayer.R.string.log_seek_completed) + "\n");
        }
    }

    private class MySeekStartListener implements AliyunVodPlayerView.OnSeekStartListener {
        @Override
        public void onSeekStart() {
            Log.d("阿里云播放信息", format.format(new Date()) + getView().getSelfActivity().getString(com.aliyun.vodplayer.R.string.log_seek_start) + "\n");

        }
    }

    /**
     * 因为鉴权过期,而去重新鉴权
     */
    private class RetryExpiredSts implements VidStsUtil.OnStsResultListener {


        @Override
        public void onSuccess(String vid, String akid, String akSecret, String token) {

//            onStsRetrySuccess(vid, akid, akSecret, token);

        }

        @Override
        public void onFail() {

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
     * 切换播放url资源 跳转到指定时间点的视频画面，时间单位为秒
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


    /**
     * 切换播放url资源 使用vid+playAuth方式播放
     * * @param title 切换视频的title
     */

    public void changePlayPlayAuthSource(String mVid, String title, String authInfo) {


        AliyunPlayAuth.AliyunPlayAuthBuilder aliyunPlayAuthBuilder = new AliyunPlayAuth.AliyunPlayAuthBuilder();
        aliyunPlayAuthBuilder.setVid(mVid);
        aliyunPlayAuthBuilder.setPlayAuth(authInfo);
        aliyunPlayAuthBuilder.setQuality(IAliyunVodPlayer.QualityValue.QUALITY_LOW);
        aliyunPlayAuthBuilder.setTitle(title);
        AliyunPlayAuth mPlayAuth = aliyunPlayAuthBuilder.build();

        video_view.setAuthInfo(mPlayAuth);


    }

    public void firstFrameStart() {
        Map<String, String> debugInfo = video_view.getAllDebugInfo();
        long createPts = 0;
        if (debugInfo.get("create_player") != null) {
            String time = debugInfo.get("create_player");
            createPts = (long) Double.parseDouble(time);
            logStrs.add(format.format(new Date(createPts)) + getView().getSelfActivity().getString(com.aliyun.vodplayer.R.string.log_player_create_success));
        }
        if (debugInfo.get("open-url") != null) {
            String time = debugInfo.get("open-url");
            long openPts = (long) Double.parseDouble(time) + createPts;
            logStrs.add(format.format(new Date(openPts)) + getView().getSelfActivity().getString(com.aliyun.vodplayer.R.string.log_open_url_success));
        }
        if (debugInfo.get("find-stream") != null) {
            String time = debugInfo.get("find-stream");
            long findPts = (long) Double.parseDouble(time) + createPts;
            logStrs.add(format.format(new Date(findPts)) + getView().getSelfActivity().getString(com.aliyun.vodplayer.R.string.log_request_stream_success));
        }
        if (debugInfo.get("open-stream") != null) {
            String time = debugInfo.get("open-stream");
            long openPts = (long) Double.parseDouble(time) + createPts;
            logStrs.add(format.format(new Date(openPts)) + getView().getSelfActivity().getString(com.aliyun.vodplayer.R.string.log_start_open_stream));
        }
        logStrs.add(format.format(new Date()) + getView().getSelfActivity().getString(com.aliyun.vodplayer.R.string.log_first_frame_played));
        for (String log : logStrs) {
            Log.d("阿里云播放信息", log + "\n");
        }
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
