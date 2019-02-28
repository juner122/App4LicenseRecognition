package com.aliyun.vodplayerview.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alivc.player.AliyunErrorCode;
import com.alivc.player.VcPlayerLog;
import com.aliyun.vodplayer.R;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunMediaInfo;
import com.aliyun.vodplayer.media.AliyunPlayAuth;
import com.aliyun.vodplayer.media.AliyunVidSts;
import com.aliyun.vodplayer.media.AliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayer.media.IAliyunVodPlayer.PlayerState;
import com.aliyun.vodplayerview.constants.PlayParameter;
import com.aliyun.vodplayerview.theme.ITheme;
import com.aliyun.vodplayerview.utils.FixedToastUtils;
import com.aliyun.vodplayerview.utils.ImageLoader;
import com.aliyun.vodplayerview.utils.NetWatchdog;
import com.aliyun.vodplayerview.utils.OrientationWatchDog;
import com.aliyun.vodplayerview.utils.ScreenUtils;
import com.aliyun.vodplayerview.view.control.ControlView;
import com.aliyun.vodplayerview.view.control.ControlView.OnDownloadClickListener;
import com.aliyun.vodplayerview.view.gesture.GestureDialogManager;
import com.aliyun.vodplayerview.view.gesture.GestureView;
import com.aliyun.vodplayerview.view.guide.GuideView;
import com.aliyun.vodplayerview.view.interfaces.ViewAction;
import com.aliyun.vodplayerview.view.more.SpeedValue;
import com.aliyun.vodplayerview.view.quality.QualityView;
import com.aliyun.vodplayerview.view.speed.SpeedView;
import com.aliyun.vodplayerview.view.tipsview.TipsView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;


/*
 * Copyright (C) 2010-2018 Alibaba Group Holding Limited.
 */

/**
 * UI播放器的主要实现类。 通过ITheme控制各个界面的主题色。 通过各种view的组合实现UI的界面。这些view包括： 用户手势操作的{@link GestureView} 控制播放，显示信息的{@link
 * ControlView} 显示清晰度列表的{@link QualityView} 倍速选择界面{@link SpeedView} 用户使用引导页面{@link GuideView} 用户提示页面{@link TipsView}
 * 以及封面等。 view 的初始化是在{@link #initVideoView}方法中实现的。 然后是对各个view添加监听方法，处理对应的操作，从而实现与播放器的共同操作
 */
public class AliyunVodPlayerView extends RelativeLayout implements ITheme {

    private static final String TAG = AliyunVodPlayerView.class.getSimpleName();

    /**
     * 判断VodePlayer 是否加载完成
     */
    private Map<AliyunMediaInfo,Boolean> hasLoadEnd = new HashMap<>();

    //视频画面
    private SurfaceView mSurfaceView;
    //手势操作view
    private GestureView mGestureView;
    //皮肤view
    private ControlView mControlView;
    //清晰度view
    private QualityView mQualityView;
    //倍速选择view
    private SpeedView mSpeedView;
    //引导页view
    private GuideView mGuideView;
    //封面view
    private ImageView mCoverView;
    //播放器
    private AliyunVodPlayer mAliyunVodPlayer;
    //手势对话框控制
    private GestureDialogManager mGestureDialogManager;
    //网络状态监听
    private NetWatchdog mNetWatchdog;
    //屏幕方向监听
    private OrientationWatchDog mOrientationWatchDog;
    //Tips view
    private TipsView mTipsView;
    //锁定竖屏
    private IAliyunVodPlayer.LockPortraitListener mLockPortraitListener = null;
    //是否锁定全屏
    private boolean mIsFullScreenLocked = false;
    //当前屏幕模式
    private AliyunScreenMode mCurrentScreenMode = AliyunScreenMode.Small;
    //是不是在seek中
    private boolean inSeek = false;
    //播放是否完成
    private boolean isCompleted = false;
    //用来记录前后台切换时的状态，以供恢复。
    private PlayerState mPlayerState;
    //媒体信息
    private AliyunMediaInfo mAliyunMediaInfo;
    //整体缓冲进度
    private int mCurrentBufferPercentage = 0;
    //进度更新计时器
    private ProgressUpdateTimer mProgressUpdateTimer = new ProgressUpdateTimer(this);
    //解决bug,进入播放界面快速切换到其他界面,播放器仍然播放视频问题
    private VodPlayerLoadEndHandler vodPlayerLoadEndHandler = new VodPlayerLoadEndHandler(this);

    //目前支持的几种播放方式
    private AliyunPlayAuth mAliyunPlayAuth;
    private AliyunLocalSource mAliyunLocalSource;
    private AliyunVidSts mAliyunVidSts;

    //对外的各种事件监听
    private IAliyunVodPlayer.OnInfoListener mOutInfoListener = null;
    private IAliyunVodPlayer.OnErrorListener mOutErrorListener = null;
    private IAliyunVodPlayer.OnRePlayListener mOutRePlayListener = null;
    private IAliyunVodPlayer.OnPcmDataListener mOutPcmDataListener = null;
    private IAliyunVodPlayer.OnAutoPlayListener mOutAutoPlayListener = null;
    private IAliyunVodPlayer.OnPreparedListener mOutPreparedListener = null;
    private IAliyunVodPlayer.OnCompletionListener mOutCompletionListener = null;
    private IAliyunVodPlayer.OnSeekCompleteListener mOuterSeekCompleteListener = null;
    private IAliyunVodPlayer.OnChangeQualityListener mOutChangeQualityListener = null;
    private IAliyunVodPlayer.OnFirstFrameStartListener mOutFirstFrameStartListener = null;
    private IAliyunVodPlayer.OnTimeExpiredErrorListener mOutTimeExpiredErrorListener = null;
    private IAliyunVodPlayer.OnUrlTimeExpiredListener mOutUrlTimeExpiredListener = null;
    //对外view点击事件监听
    private OnPlayerViewClickListener mOnPlayerViewClickListener = null;
    // 连网断网监听
    private NetConnectedListener mNetConnectedListener = null;
    // 横屏状态点击更多
    private ControlView.OnShowMoreClickListener mOutOnShowMoreClickListener;
    /**
     * 播放按钮点击监听
     */

    private OnPlayStateBtnClickListener onPlayStateBtnClickListener;

    private float currentSpeed;
    private int currentVolume;
    private int currentScreenBrigtness;

    public AliyunVodPlayerView(Context context) {
        super(context);
        initVideoView();
    }

    public AliyunVodPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView();
    }

    public AliyunVodPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initVideoView();
    }

    /**
     * 初始化view
     */
    private void initVideoView() {
        //初始化播放用的surfaceView
        initSurfaceView();
        //初始化播放器
        initAliVcPlayer();
        //初始化封面
        initCoverView();
        //初始化手势view
        initGestureView();
        //初始化清晰度view
        initQualityView();
        //初始化控制栏
        initControlView();
        //初始化倍速view
        initSpeedView();
        //初始化指引view
        initGuideView();
        //初始化提示view
        initTipsView();
        //初始化网络监听器
        initNetWatchdog();
        //初始化屏幕方向监听
        initOrientationWatchdog();
        //初始化手势对话框控制
        initGestureDialogManager();
        //默认为蓝色主题
        setTheme(Theme.Blue);
        //先隐藏手势和控制栏，防止在没有prepare的时候做操作。
        hideGestureAndControlViews();
    }

    /**
     * 更新UI播放器的主题
     *
     * @param theme 支持的主题
     */
    @Override
    public void setTheme(Theme theme) {
        //通过判断子View是否实现了ITheme的接口，去更新主题
        int childCounts = getChildCount();
        for (int i = 0; i < childCounts; i++) {
            View view = getChildAt(i);
            if (view instanceof ITheme) {
                ((ITheme)view).setTheme(theme);
            }
        }
    }

    /**
     * 切换播放速度
     *
     * @param speedValue
     */
    public void changeSpeed(SpeedValue speedValue) {
        if (speedValue == SpeedValue.One) {
            currentSpeed = 1.0f;
        } else if (speedValue == SpeedValue.OneQuartern) {
            currentSpeed = 1.25f;
        } else if (speedValue == SpeedValue.OneHalf) {
            currentSpeed = 1.5f;
        } else if (speedValue == SpeedValue.Twice) {
            currentSpeed = 2.0f;
        }
        mAliyunVodPlayer.setPlaySpeed(currentSpeed);
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;

    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentVolume(int progress) {
        this.currentVolume = progress;
        mAliyunVodPlayer.setVolume(progress);
    }

    public int getCurrentVolume() {
        return mAliyunVodPlayer.getVolume();
    }

    public void setCurrentScreenBrigtness(int progress) {
        this.currentScreenBrigtness = progress;
        mAliyunVodPlayer.setScreenBrightness(progress);
    }

    public int getCurrentScreenBrigtness() {
        return mAliyunVodPlayer.getScreenBrightness();
    }

    public void updateScreenShow() {
        mControlView.updateDownloadBtn();
    }

    /**
     * UI播放器支持的主题
     */
    public static enum Theme {
        /**
         * 蓝色主题
         */
        Blue,
        /**
         * 绿色主题
         */
        Green,
        /**
         * 橙色主题
         */
        Orange,
        /**
         * 红色主题
         */
        Red
    }

    /**
     * 隐藏手势和控制栏
     */
    private void hideGestureAndControlViews() {
        if (mGestureView != null) {
            mGestureView.hide(ViewAction.HideType.Normal);
        }
        if (mControlView != null) {
            mControlView.hide(ViewAction.HideType.Normal);
        }
    }

    /**
     * 初始化网络监听
     */
    private void initNetWatchdog() {
        Context context = getContext();
        mNetWatchdog = new NetWatchdog(context);
        mNetWatchdog.setNetChangeListener(new MyNetChangeListener(this));
        mNetWatchdog.setNetConnectedListener(new MyNetConnectedListener(this));

    }

    private void onWifiTo4G() {
        VcPlayerLog.d(TAG, "onWifiTo4G");

        //如果已经显示错误了，那么就不用显示网络变化的提示了。
        if (mTipsView.isErrorShow()) {
            return;
        }

        //wifi变成4G，先暂停播放
        pause();

        //隐藏其他的动作,防止点击界面去进行其他操作
        mGestureView.hide(ControlView.HideType.Normal);
        mControlView.hide(ControlView.HideType.Normal);

        //显示网络变化的提示
        if (!isLocalSource() && mTipsView != null) {
            mTipsView.showNetChangeTipView();
        }
    }

    private void on4GToWifi() {
        VcPlayerLog.d(TAG, "on4GToWifi");
        //如果已经显示错误了，那么就不用显示网络变化的提示了。
        if (mTipsView.isErrorShow()) {
            return;
        }

        //隐藏网络变化的提示
        if (mTipsView != null) {
            mTipsView.hideNetErrorTipView();
        }
    }

    private void onNetDisconnected() {
        VcPlayerLog.d(TAG, "onNetDisconnected");
        //网络断开。
        // NOTE： 由于安卓这块网络切换的时候，有时候也会先报断开。所以这个回调是不准确的。
    }

    private static class MyNetChangeListener implements NetWatchdog.NetChangeListener {

        private WeakReference<AliyunVodPlayerView> viewWeakReference;

        public MyNetChangeListener(AliyunVodPlayerView aliyunVodPlayerView) {
            viewWeakReference = new WeakReference<AliyunVodPlayerView>(aliyunVodPlayerView);
        }

        @Override
        public void onWifiTo4G() {
            AliyunVodPlayerView aliyunVodPlayerView = viewWeakReference.get();
            if (aliyunVodPlayerView != null) {
                aliyunVodPlayerView.onWifiTo4G();
            }
        }

        @Override
        public void on4GToWifi() {
            AliyunVodPlayerView aliyunVodPlayerView = viewWeakReference.get();
            if (aliyunVodPlayerView != null) {
                aliyunVodPlayerView.on4GToWifi();
            }
        }

        @Override
        public void onNetDisconnected() {
            AliyunVodPlayerView aliyunVodPlayerView = viewWeakReference.get();
            if (aliyunVodPlayerView != null) {
                aliyunVodPlayerView.onNetDisconnected();
            }
        }
    }

    /**
     * 初始化屏幕方向旋转。用来监听屏幕方向。结果通过OrientationListener回调出去。
     */
    private void initOrientationWatchdog() {
        final Context context = getContext();
        mOrientationWatchDog = new OrientationWatchDog(context);
        mOrientationWatchDog.setOnOrientationListener(new InnerOrientationListener(this));
    }

    private static class InnerOrientationListener implements OrientationWatchDog.OnOrientationListener {

        private WeakReference<AliyunVodPlayerView> playerViewWeakReference;

        public InnerOrientationListener(AliyunVodPlayerView playerView) {
            playerViewWeakReference = new WeakReference<AliyunVodPlayerView>(playerView);
        }

        @Override
        public void changedToLandScape(boolean fromPort) {
            AliyunVodPlayerView playerView = playerViewWeakReference.get();
            if (playerView != null) {
                playerView.changedToLandScape(fromPort);
            }
        }

        @Override
        public void changedToPortrait(boolean fromLand) {
            AliyunVodPlayerView playerView = playerViewWeakReference.get();
            if (playerView != null) {
                playerView.changedToPortrait(fromLand);
            }
        }
    }

    /**
     * 屏幕方向变为横屏。
     *
     * @param fromPort 是否从竖屏变过来
     */
    private void changedToLandScape(boolean fromPort) {

//        mControlView.showTitlebarBackBtn();
        //如果不是从竖屏变过来，也就是一直是横屏的时候，就不用操作了
        if (!fromPort) {
            return;
        }
        //屏幕由竖屏转为横屏
        if (mCurrentScreenMode == AliyunScreenMode.Full) {
            //全屏情况转到了横屏

        } else if (mCurrentScreenMode == AliyunScreenMode.Small) {
            changeScreenMode(AliyunScreenMode.Full);
        }

        if (orientationChangeListener != null) {
            orientationChangeListener.orientationChange(fromPort, mCurrentScreenMode);
        }


    }

    /**
     * 屏幕方向变为竖屏
     *
     * @param fromLand 是否从横屏转过来
     */
    private void changedToPortrait(boolean fromLand) {
//        mControlView.hideTitlebarBackBtn();
        //屏幕转为竖屏
        if (mIsFullScreenLocked) {
            return;
        }

        if (mCurrentScreenMode == AliyunScreenMode.Full) {
            //全屏情况转到了竖屏
            if (getLockPortraitMode() == null) {
                //没有固定竖屏，就变化mode
                if (fromLand) {
                    changeScreenMode(AliyunScreenMode.Small);
                } else {
                    //如果没有转到过横屏，就不让他转了。防止竖屏的时候点横屏之后，又立即转回来的现象
                }
            } else {
                //固定竖屏了，竖屏还是竖屏，不用动
            }
        } else if (mCurrentScreenMode == AliyunScreenMode.Small) {
            //竖屏的情况转到了竖屏
        }

        if (orientationChangeListener != null) {
            orientationChangeListener.orientationChange(fromLand, mCurrentScreenMode);
        }


    }

    /**
     * 初始化手势的控制类
     */
    private void initGestureDialogManager() {
        Context context = getContext();
        if (context instanceof Activity) {
            mGestureDialogManager = new GestureDialogManager((Activity)context);
        }
    }

    /**
     * 初始化提示view
     */
    private void initTipsView() {

        mTipsView = new TipsView(getContext());
        //设置tip中的点击监听事件
        mTipsView.setOnTipClickListener(new TipsView.OnTipClickListener() {
            @Override
            public void onContinuePlay() {
                VcPlayerLog.d(TAG, "playerState = " + mAliyunVodPlayer.getPlayerState());
                //继续播放。如果没有prepare或者stop了，需要重新prepare
                mTipsView.hideAll();
                if (mAliyunVodPlayer.getPlayerState() == PlayerState.Idle ||
                    mAliyunVodPlayer.getPlayerState() == PlayerState.Stopped) {
                    if (mAliyunPlayAuth != null) {
                        prepareAuth(mAliyunPlayAuth);
                    } else if (mAliyunVidSts != null) {
                        prepareVidsts(mAliyunVidSts);
                    } else if (mAliyunLocalSource != null) {
                        //setLocalSource(mAliyunLocalSource);
                        prepareLocalSource(mAliyunLocalSource);
                    }
                } else {
                    start();
                }

            }

            @Override
            public void onStopPlay() {
                // 结束播放
                mTipsView.hideAll();
                stop();

                Context context = getContext();
                if (context instanceof Activity) {
                    ((Activity)context).finish();
                }
            }

            @Override
            public void onRetryPlay() {
                //重试
                reTry();
            }

            @Override
            public void onReplay() {
                //重播
                rePlay();
            }
        });
        addSubView(mTipsView);
    }

    /**
     * 重试播放，会从当前位置开始播放
     */
    public void reTry() {

        isCompleted = false;
        inSeek = false;

        int currentPosition = mControlView.getVideoPosition();
        VcPlayerLog.d(TAG, " currentPosition = " + currentPosition);

        if (mTipsView != null) {
            mTipsView.hideAll();
        }
        if (mControlView != null) {
            mControlView.reset();
            //防止被reset掉，下次还可以获取到这些值
            mControlView.setVideoPosition(currentPosition);
        }
        if (mGestureView != null) {
            mGestureView.reset();
        }

        if (mAliyunVodPlayer != null) {

            //显示网络加载的loading。。
            if (mTipsView != null) {
                mTipsView.showNetLoadingTipView();
            }
            //seek到当前的位置再播放
            /*
                isLocalSource()判断不够,有可能是sts播放,也有可能是url播放,还有可能是sd卡的视频播放,
                如果是后两者,需要走if,否则走else
             */
            if (isLocalSource() || isUrlSource()) {
                mAliyunVodPlayer.prepareAsync(mAliyunLocalSource);
            } else {
                mAliyunVodPlayer.prepareAsync(mAliyunVidSts);
            }
            mAliyunVodPlayer.seekTo(currentPosition);
        }

    }

    /**
     * 重播，将会从头开始播放
     */
    public void rePlay() {

        isCompleted = false;
        inSeek = false;

        if (mTipsView != null) {
            mTipsView.hideAll();
        }
        if (mControlView != null) {
            mControlView.reset();
        }
        if (mGestureView != null) {
            mGestureView.reset();
        }

        if (mAliyunVodPlayer != null) {
            //显示网络加载的loading。。
            if (mTipsView != null) {
                mTipsView.showNetLoadingTipView();
            }
            //重播是从头开始播
            mAliyunVodPlayer.replay();
        }

    }

    /**
     * 重置。包括一些状态值，view的状态等
     */
    private void reset() {
        isCompleted = false;
        inSeek = false;

        if (mTipsView != null) {
            mTipsView.hideAll();
        }
        if (mControlView != null) {
            mControlView.reset();
        }
        if (mGestureView != null) {
            mGestureView.reset();
        }
        stop();
    }

    /**
     * 初始化封面
     */
    private void initCoverView() {
        mCoverView = new ImageView(getContext());
        //这个是为了给自动化测试用的id
        mCoverView.setId(R.id.custom_id_min);
        addSubView(mCoverView);
    }

    /**
     * 初始化控制栏view
     */
    private void initControlView() {
        mControlView = new ControlView(getContext());
        addSubView(mControlView);

        //设置播放按钮点击
        mControlView.setOnPlayStateClickListener(new ControlView.OnPlayStateClickListener() {
            @Override
            public void onPlayStateClick() {
                switchPlayerState();
            }
        });
        //设置进度条的seek监听
        mControlView.setOnSeekListener(new ControlView.OnSeekListener() {
            @Override
            public void onSeekEnd(int position) {
                if(mControlView != null){
                    mControlView.setVideoPosition(position);
                }
                if (isCompleted) {
                    //播放完成了，不能seek了
                    inSeek = false;
                } else {

                    //拖动结束后，开始seek
                    seekTo(position);

                    inSeek = true;
                    if (onSeekStartListener != null) {
                        onSeekStartListener.onSeekStart();
                    }
                }
            }

            @Override
            public void onSeekStart() {
                //拖动开始
                inSeek = true;
            }
        });
        //菜单按钮点击
        mControlView.setOnMenuClickListener(new ControlView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                //点击之后显示倍速界面
                //根据屏幕模式，显示倍速界面
                mSpeedView.show(mCurrentScreenMode);
            }
        });
        mControlView.setOnDownloadClickListener(new OnDownloadClickListener() {
            @Override
            public void onDownloadClick() {
                //点击下载之后弹出不同清晰度选择下载dialog
                // 如果当前播放视频时url类型, 不允许下载
                if ("localSource".equals(PlayParameter.PLAY_PARAM_TYPE)) {
                    FixedToastUtils.show(getContext(), getResources().getString(R.string.slivc_not_support_url));
                    return;
                }
                if (mOnPlayerViewClickListener != null) {
                    mOnPlayerViewClickListener.onClick(mCurrentScreenMode, PlayViewType.Download);
                }
            }
        });
        //清晰度按钮点击
        mControlView.setOnQualityBtnClickListener(new ControlView.OnQualityBtnClickListener() {

            @Override
            public void onQualityBtnClick(View v, List<String> qualities, String currentQuality) {
                //显示清晰度列表
                mQualityView.setQuality(qualities, currentQuality);
                mQualityView.showAtTop(v);
            }

            @Override
            public void onHideQualityView() {
                mQualityView.hide();
            }
        });
        //点击锁屏的按钮
        mControlView.setOnScreenLockClickListener(new ControlView.OnScreenLockClickListener() {
            @Override
            public void onClick() {
                lockScreen(!mIsFullScreenLocked);
            }
        });
        //点击全屏/小屏按钮
        mControlView.setOnScreenModeClickListener(new ControlView.OnScreenModeClickListener() {
            @Override
            public void onClick() {
                AliyunScreenMode targetMode;
                if (mCurrentScreenMode == AliyunScreenMode.Small) {
                    targetMode = AliyunScreenMode.Full;
                } else {
                    targetMode = AliyunScreenMode.Small;
                }

                changeScreenMode(targetMode);
                if (mCurrentScreenMode == AliyunScreenMode.Full) {
                    mControlView.showMoreButton();
                } else if (mCurrentScreenMode == AliyunScreenMode.Small) {
                    mControlView.hideMoreButton();
                }
            }
        });
        //点击了标题栏的返回按钮
        mControlView.setOnBackClickListener(new ControlView.OnBackClickListener() {
            @Override
            public void onClick() {

                if (mCurrentScreenMode == AliyunScreenMode.Full) {
                    //设置为小屏状态
                    changeScreenMode(AliyunScreenMode.Small);
                } else if (mCurrentScreenMode == AliyunScreenMode.Small) {
                    //小屏状态下，就结束活动
                    Context context = getContext();
                    if (context instanceof Activity) {
                        ((Activity)context).finish();
                    }
                }

                if (mCurrentScreenMode == AliyunScreenMode.Small) {
                    mControlView.hideMoreButton();
                }
            }
        });

        // 横屏下显示更多
        mControlView.setOnShowMoreClickListener(new ControlView.OnShowMoreClickListener() {
            @Override
            public void showMore() {
                if (mOutOnShowMoreClickListener != null) {
                    mOutOnShowMoreClickListener.showMore();
                }
            }
        });

        // 截屏
        mControlView.setOnScreenShotClickListener(new ControlView.OnScreenShotClickListener() {
            @Override
            public void onScreenShotClick() {
                if(!mIsFullScreenLocked){
                    FixedToastUtils.show(getContext(), "功能正在开发中, 敬请期待....");
                }
            }
        });

        // 录制
        mControlView.setOnScreenRecoderClickListener(new ControlView.OnScreenRecoderClickListener() {
            @Override
            public void onScreenRecoderClick() {
                if(!mIsFullScreenLocked){
                    FixedToastUtils.show(getContext(), "功能正在开发中, 敬请期待....");
                }
            }
        });
    }

    /**
     * 锁定屏幕。锁定屏幕后，只有锁会显示，其他都不会显示。手势也不可用
     *
     * @param lockScreen 是否锁住
     */
    public void lockScreen(boolean lockScreen) {
        mIsFullScreenLocked = lockScreen;
        if (mControlView != null) {
            mControlView.setScreenLockStatus(mIsFullScreenLocked);
        }
        if (mGestureView != null) {
            mGestureView.setScreenLockStatus(mIsFullScreenLocked);
        }
    }

    /**
     * 初始化清晰度列表
     */
    private void initQualityView() {
        mQualityView = new QualityView(getContext());
        addSubView(mQualityView);
        //清晰度点击事件
        mQualityView.setOnQualityClickListener(new QualityView.OnQualityClickListener() {
            @Override
            public void onQualityClick(String quality) {
                //进行清晰度的切换
                if (mTipsView != null) {
                    mTipsView.showNetLoadingTipView();
                }

                stopProgressUpdateTimer();
                mAliyunVodPlayer.changeQuality(quality);

            }
        });
    }

    /**
     * 初始化倍速view
     */
    private void initSpeedView() {
        mSpeedView = new SpeedView(getContext());
        addSubView(mSpeedView);

        //倍速点击事件
        mSpeedView.setOnSpeedClickListener(new SpeedView.OnSpeedClickListener() {
            @Override
            public void onSpeedClick(SpeedView.SpeedValue value) {
                float speed = 1.0f;
                if (value == SpeedView.SpeedValue.Normal) {
                    speed = 1.0f;
                } else if (value == SpeedView.SpeedValue.OneQuartern) {
                    speed = 1.25f;
                } else if (value == SpeedView.SpeedValue.OneHalf) {
                    speed = 1.5f;
                } else if (value == SpeedView.SpeedValue.Twice) {
                    speed = 2.0f;
                }

                //改变倍速
                if (mAliyunVodPlayer != null) {
                    mAliyunVodPlayer.setPlaySpeed(speed);
                }

                mSpeedView.setSpeed(value);
            }

            @Override
            public void onHide() {
                //当倍速界面隐藏之后，显示菜单按钮
            }
        });

    }

    /**
     * 初始化引导view
     */
    private void initGuideView() {
        mGuideView = new GuideView(getContext());
        addSubView(mGuideView);
    }

    /**
     * 切换播放状态。点播播放按钮之后的操作
     */
    private void switchPlayerState() {
        PlayerState playerState = mAliyunVodPlayer.getPlayerState();
        if (playerState == PlayerState.Started) {
            pause();
        } else if (playerState == PlayerState.Paused || playerState == PlayerState.Prepared) {
            start();
        }
        if (onPlayStateBtnClickListener != null) {
            onPlayStateBtnClickListener.onPlayBtnClick(playerState);
        }
    }

    /**
     * 初始化手势view
     */
    private void initGestureView() {
        mGestureView = new GestureView(getContext());
        addSubView(mGestureView);

        //设置手势监听
        mGestureView.setOnGestureListener(new GestureView.GestureListener() {

            @Override
            public void onHorizontalDistance(float downX, float nowX) {
                //水平滑动调节seek。
                // seek需要在手势结束时操作。
                long duration = mAliyunVodPlayer.getDuration();
                long position = mAliyunVodPlayer.getCurrentPosition();
                long deltaPosition = 0;

                if (mAliyunVodPlayer.getPlayerState() == PlayerState.Prepared ||
                    mAliyunVodPlayer.getPlayerState() == PlayerState.Paused ||
                    mAliyunVodPlayer.getPlayerState() == PlayerState.Started) {
                    //在播放时才能调整大小
                    deltaPosition = (long)(nowX - downX) * duration / getWidth();
                }

                if (mGestureDialogManager != null) {
                    mGestureDialogManager.showSeekDialog(AliyunVodPlayerView.this, (int)position);
                    mGestureDialogManager.updateSeekDialog(duration, position, deltaPosition);
                }
            }

            @Override
            public void onLeftVerticalDistance(float downY, float nowY) {
                //左侧上下滑动调节亮度
                int changePercent = (int)((nowY - downY) * 100 / getHeight());

                if (mGestureDialogManager != null) {
                    mGestureDialogManager.showBrightnessDialog(AliyunVodPlayerView.this);
                    int brightness = mGestureDialogManager.updateBrightnessDialog(changePercent);
                    mAliyunVodPlayer.setScreenBrightness(brightness);
                }
            }

            @Override
            public void onRightVerticalDistance(float downY, float nowY) {
                //右侧上下滑动调节音量
                int changePercent = (int)((nowY - downY) * 100 / getHeight());
                int volume = mAliyunVodPlayer.getVolume();

                if (mGestureDialogManager != null) {
                    mGestureDialogManager.showVolumeDialog(AliyunVodPlayerView.this, volume);
                    int targetVolume = mGestureDialogManager.updateVolumeDialog(changePercent);
                    currentVolume = targetVolume;
                    mAliyunVodPlayer.setVolume(targetVolume);//通过返回值改变音量
                }
            }

            @Override
            public void onGestureEnd() {
                //手势结束。
                //seek需要在结束时操作。
                if (mGestureDialogManager != null) {
                    mGestureDialogManager.dismissBrightnessDialog();
                    mGestureDialogManager.dismissVolumeDialog();

                    int seekPosition = mGestureDialogManager.dismissSeekDialog();
                    if (seekPosition >= mAliyunVodPlayer.getDuration()) {
                        seekPosition = (int)(mAliyunVodPlayer.getDuration() - 1000);
                    }

                    if (seekPosition >= 0) {
                        seekTo(seekPosition);
                        inSeek = true;
                    }
                }
            }

            @Override
            public void onSingleTap() {
                //单击事件，显示控制栏
                if(mControlView != null){
                    if (mControlView.getVisibility() != VISIBLE) {
                        mControlView.show();
                    } else {
                        mControlView.hide(ControlView.HideType.Normal);
                    }
                }
            }

            @Override
            public void onDoubleTap() {
                //双击事件，控制暂停播放
                switchPlayerState();
            }
        });
    }

    /**
     * 初始化播放器显示view
     */
    private void initSurfaceView() {
        mSurfaceView = new SurfaceView(getContext().getApplicationContext());
        addSubView(mSurfaceView);

        SurfaceHolder holder = mSurfaceView.getHolder();
        //增加surfaceView的监听
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                VcPlayerLog.d(TAG, " surfaceCreated = surfaceHolder = " + surfaceHolder);
                mAliyunVodPlayer.setDisplay(surfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width,
                                       int height) {
                VcPlayerLog.d(TAG,
                    " surfaceChanged surfaceHolder = " + surfaceHolder + " ,  width = " + width + " , height = "
                        + height);
                mAliyunVodPlayer.surfaceChanged();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                VcPlayerLog.d(TAG, " surfaceDestroyed = surfaceHolder = " + surfaceHolder);
            }
        });
    }

    /**
     * 初始化播放器
     */
    private void initAliVcPlayer() {
        mAliyunVodPlayer = new AliyunVodPlayer(getContext());
        mAliyunVodPlayer.enableNativeLog();
        //设置准备回调
        mAliyunVodPlayer.setOnPreparedListener(new IAliyunVodPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                if (mAliyunVodPlayer == null) {
                    return;
                }

                mAliyunMediaInfo = mAliyunVodPlayer.getMediaInfo();
                if (mAliyunMediaInfo == null) {
                    return;
                }
                //防止服务器信息和实际不一致
                mAliyunMediaInfo.setDuration((int)mAliyunVodPlayer.getDuration());
                //使用用户设置的标题
                mAliyunMediaInfo.setTitle(getTitle(mAliyunMediaInfo.getTitle()));
                mAliyunMediaInfo.setPostUrl(getPostUrl(mAliyunMediaInfo.getPostUrl()));
                mControlView.setMediaInfo(mAliyunMediaInfo, mAliyunVodPlayer.getCurrentQuality());
                mControlView.setHideType(ViewAction.HideType.Normal);
                mGestureView.setHideType(ViewAction.HideType.Normal);
                mControlView.show();
                mGestureView.show();
                if (mTipsView != null) {
                    mTipsView.hideNetLoadingTipView();
                }

                setCoverUri(mAliyunMediaInfo.getPostUrl());
                //准备成功之后可以调用start方法开始播放
                if (mOutPreparedListener != null) {
                    mOutPreparedListener.onPrepared();
                }
            }
        });
        //播放器出错监听
        mAliyunVodPlayer.setOnErrorListener(new IAliyunVodPlayer.OnErrorListener() {
            @Override
            public void onError(int errorCode, int errorEvent, String errorMsg) {
                if (errorCode == AliyunErrorCode.ALIVC_ERR_INVALID_INPUTFILE.getCode()) {
                    //当播放本地报错4003的时候，可能是文件地址不对，也有可能是没有权限。
                    //如果是没有权限导致的，就做一个权限的错误提示。其他还是正常提示：
                    int storagePermissionRet = ContextCompat.checkSelfPermission(
                        AliyunVodPlayerView.this.getContext().getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (storagePermissionRet != PackageManager.PERMISSION_GRANTED) {
                        errorMsg = AliyunErrorCode.ALIVC_ERR_NO_STORAGE_PERMISSION.getDescription(getContext());
                    } else if (!NetWatchdog.hasNet(getContext())) {
                        //也可能是网络不行
                        errorCode = AliyunErrorCode.ALIVC_ERR_NO_NETWORK.getCode();
                        errorMsg = AliyunErrorCode.ALIVC_ERR_NO_NETWORK.getDescription(getContext());
                    }
                }

                //关闭定时器
                stopProgressUpdateTimer();

                if (mTipsView != null) {
                    mTipsView.hideAll();
                }
                //出错之后解锁屏幕，防止不能做其他操作，比如返回。
                lockScreen(false);

                showErrorTipView(errorCode, errorEvent, errorMsg);

                if (mOutErrorListener != null) {
                    mOutErrorListener.onError(errorCode, errorEvent, errorMsg);
                }

            }
        });

        //播放器加载回调
        mAliyunVodPlayer.setOnLoadingListener(new IAliyunVodPlayer.OnLoadingListener() {
            @Override
            public void onLoadStart() {
                if (mTipsView != null) {
                    mTipsView.showBufferLoadingTipView();
                }
            }

            @Override
            public void onLoadEnd() {
                if (mTipsView != null) {
                    mTipsView.hideBufferLoadingTipView();
                }
                if (mAliyunVodPlayer.isPlaying()) {
                    mTipsView.hideErrorTipView();
                }
                hasLoadEnd.put(mAliyunMediaInfo,true);
                vodPlayerLoadEndHandler.sendEmptyMessage(1);
            }

            @Override
            public void onLoadProgress(int percent) {
                if (mTipsView != null) {
                    mTipsView.updateLoadingPercent(percent);
                }
            }
        });
        //播放结束
        mAliyunVodPlayer.setOnCompletionListener(new IAliyunVodPlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {
                inSeek = false;
                //关闭定时器
                stopProgressUpdateTimer();

                //如果当前播放资源是本地资源时, 再显示replay
                if (mTipsView != null && isLocalSource()) {
                    //隐藏其他的动作,防止点击界面去进行其他操作
                    mGestureView.hide(ViewAction.HideType.End);
                    mControlView.hide(ViewAction.HideType.End);
                    mTipsView.showReplayTipView();
                }

                if (mOutCompletionListener != null) {
                    mOutCompletionListener.onCompletion();
                }
            }
        });
        mAliyunVodPlayer.setOnBufferingUpdateListener(new IAliyunVodPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(int percent) {
                mCurrentBufferPercentage = percent;
            }
        });
        //播放信息监听
        mAliyunVodPlayer.setOnInfoListener(new IAliyunVodPlayer.OnInfoListener() {
            @Override
            public void onInfo(int arg0, int arg1) {
                if (mOutInfoListener != null) {
                    mOutInfoListener.onInfo(arg0, arg1);
                }
            }
        });
        //切换清晰度结果事件
        mAliyunVodPlayer.setOnChangeQualityListener(new IAliyunVodPlayer.OnChangeQualityListener() {
            @Override
            public void onChangeQualitySuccess(String finalQuality) {
                //切换成功后就开始播放
                mControlView.setCurrentQuality(finalQuality);
                start();

                startProgressUpdateTimer();

                if (mTipsView != null) {
                    mTipsView.hideNetLoadingTipView();
                }
                if (mOutChangeQualityListener != null) {
                    mOutChangeQualityListener.onChangeQualitySuccess(finalQuality);
                }
            }

            @Override
            public void onChangeQualityFail(int code, String msg) {
                //失败的话，停止播放，通知上层
                if (mTipsView != null) {
                    mTipsView.hideNetLoadingTipView();
                }
                if (code == CODE_SAME_QUALITY) {
                    if (mOutChangeQualityListener != null) {
                        mOutChangeQualityListener.onChangeQualitySuccess(mAliyunVodPlayer.getCurrentQuality());
                    }
                } else {
                    stop();
                    if (mOutChangeQualityListener != null) {
                        mOutChangeQualityListener.onChangeQualityFail(code, msg);
                    }
                }
            }
        });
        //重播监听
        mAliyunVodPlayer.setOnRePlayListener(new IAliyunVodPlayer.OnRePlayListener() {
            @Override
            public void onReplaySuccess() {
                //重播、重试成功
                mTipsView.hideAll();
                mGestureView.show();
                mControlView.show();
                mControlView.setMediaInfo(mAliyunMediaInfo, mAliyunVodPlayer.getCurrentQuality());
                //重播自动开始播放,需要设置播放状态
                if (mControlView != null) {
                    mControlView.setPlayState(ControlView.PlayState.Playing);
                }
                //开始启动更新进度的定时器
                startProgressUpdateTimer();
                if (mOutRePlayListener != null) {
                    mOutRePlayListener.onReplaySuccess();
                }
            }
        });
        //自动播放
        mAliyunVodPlayer.setOnAutoPlayListener(new IAliyunVodPlayer.OnAutoPlayListener() {
            @Override
            public void onAutoPlayStarted() {
                //自动播放开始,需要设置播放状态
                if (mControlView != null) {
                    mControlView.setPlayState(ControlView.PlayState.Playing);
                }
                if (mOutAutoPlayListener != null) {
                    mOutAutoPlayListener.onAutoPlayStarted();
                }
            }
        });
        //seek结束事件
        mAliyunVodPlayer.setOnSeekCompleteListener(new IAliyunVodPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete() {
                inSeek = false;

                if (mOuterSeekCompleteListener != null) {
                    mOuterSeekCompleteListener.onSeekComplete();
                }
            }
        });
        //PCM原始数据监听
        mAliyunVodPlayer.setOnPcmDataListener(new IAliyunVodPlayer.OnPcmDataListener() {
            @Override
            public void onPcmData(byte[] data, int size) {
                if (mOutPcmDataListener != null) {
                    mOutPcmDataListener.onPcmData(data, size);
                }
            }
        });
        //第一帧显示
        mAliyunVodPlayer.setOnFirstFrameStartListener(new IAliyunVodPlayer.OnFirstFrameStartListener() {
            @Override
            public void onFirstFrameStart() {
                //开始启动更新进度的定时器
                startProgressUpdateTimer();

                mCoverView.setVisibility(GONE);
                if (mOutFirstFrameStartListener != null) {
                    mOutFirstFrameStartListener.onFirstFrameStart();
                }
            }
        });
        //url过期监听
        mAliyunVodPlayer.setOnUrlTimeExpiredListener(new IAliyunVodPlayer.OnUrlTimeExpiredListener() {
            @Override
            public void onUrlTimeExpired(String vid, String quality) {
                System.out.println("abc : onUrlTimeExpired");
                if (mOutUrlTimeExpiredListener != null) {
                    mOutUrlTimeExpiredListener.onUrlTimeExpired(vid, quality);
                }
            }
        });

        //请求源过期信息
        mAliyunVodPlayer.setOnTimeExpiredErrorListener(new IAliyunVodPlayer.OnTimeExpiredErrorListener() {
            @Override
            public void onTimeExpiredError() {
                System.out.println("abc : onTimeExpiredError");
                Log.e("radish : ", "onTimeExpiredError: " + mAliyunMediaInfo.getVideoId());
                if(mTipsView != null){
                    mTipsView.hideAll();
                    mTipsView.showErrorTipViewWithoutCode("鉴权过期");
                    mTipsView.setOnTipClickListener(new TipsView.OnTipClickListener() {
                        @Override
                        public void onContinuePlay() {
                        }

                        @Override
                        public void onStopPlay() {
                        }

                        @Override
                        public void onRetryPlay() {
                            if (mOutTimeExpiredErrorListener != null) {
                                mOutTimeExpiredErrorListener.onTimeExpiredError();
                            }
                        }

                        @Override
                        public void onReplay() {
                        }
                    });
                }
            }
        });

        mAliyunVodPlayer.setDisplay(mSurfaceView.getHolder());
    }

    /**
     * 获取从源中设置的标题 。 如果用户设置了标题，优先使用用户设置的标题。 如果没有，就使用服务器返回的标题
     *
     * @param title 服务器返回的标题
     * @return 最后的标题
     */
    private String getTitle(String title) {
        String finalTitle = title;
        if (mAliyunLocalSource != null) {
            finalTitle = mAliyunLocalSource.getTitle();
        } else if (mAliyunPlayAuth != null) {
            finalTitle = mAliyunPlayAuth.getTitle();
        } else if (mAliyunVidSts != null) {
            finalTitle = mAliyunVidSts.getTitle();
        }

        if (TextUtils.isEmpty(finalTitle)) {
            return title;
        } else {
            return finalTitle;
        }
    }

    /**
     * 获取从源中设置的封面 。 如果用户设置了封面，优先使用用户设置的封面。 如果没有，就使用服务器返回的封面
     *
     * @param postUrl 服务器返回的封面
     * @return 最后的封面
     */
    private String getPostUrl(String postUrl) {
        String finalPostUrl = postUrl;
        if (mAliyunLocalSource != null) {
            finalPostUrl = mAliyunLocalSource.getCoverPath();
        } else if (mAliyunPlayAuth != null) {

        }

        if (TextUtils.isEmpty(finalPostUrl)) {
            return postUrl;
        } else {
            return finalPostUrl;
        }
    }

    /**
     * 获取整体缓冲进度
     *
     * @return 整体缓冲进度
     */
    public int getBufferPercentage() {
        if (mAliyunVodPlayer != null) {
            return mCurrentBufferPercentage;
        }
        return 0;
    }

    /**
     * 判断是否是本地资源
     * @return
     */
    private boolean isLocalSource() {
        String scheme = null;
        if ("vidsts".equals(PlayParameter.PLAY_PARAM_TYPE)) {
            return false;
        }
        if ("localSource".equals(PlayParameter.PLAY_PARAM_TYPE)) {
            Uri parse = Uri.parse(PlayParameter.PLAY_PARAM_URL);
            scheme = parse.getScheme();
        }
        return scheme == null;
    }

    /**
     * 判断是否是Url播放资源
     */
    private boolean isUrlSource(){
        String scheme = null;
        if ("vidsts".equals(PlayParameter.PLAY_PARAM_TYPE)) {
            return false;
        }else{
            Uri parse = Uri.parse(PlayParameter.PLAY_PARAM_URL);
            scheme = parse.getScheme();
            return scheme != null;
        }
    }


    /**
     * 获取视频时长
     *
     * @return 视频时长
     */
    public int getDuration() {
        if (mAliyunVodPlayer != null && mAliyunVodPlayer.isPlaying()) {
            return (int)mAliyunVodPlayer.getDuration();
        }

        return 0;
    }

    /**
     * 获取当前位置
     *
     * @return 当前位置
     */
    public int getCurrentPosition() {
        if (mAliyunVodPlayer != null && mAliyunVodPlayer.isPlaying()) {
            return (int)mAliyunVodPlayer.getCurrentPosition();
        }

        return 0;
    }

    /**
     * 显示错误提示
     *
     * @param errorCode  错误码
     * @param errorEvent 错误事件
     * @param errorMsg   错误描述
     */
    public void showErrorTipView(int errorCode, int errorEvent, String errorMsg) {
        pause();
        stop();

        if (mControlView != null) {
            mControlView.setPlayState(ControlView.PlayState.NotPlaying);
        }

        if (mTipsView != null) {
            //隐藏其他的动作,防止点击界面去进行其他操作
            mGestureView.hide(ViewAction.HideType.End);
            mControlView.hide(ViewAction.HideType.End);
            mCoverView.setVisibility(GONE);
            mTipsView.showErrorTipView(errorCode, errorEvent, errorMsg);
        }
    }

    private void hideErrorTipView() {

        if (mTipsView != null) {
            //隐藏其他的动作,防止点击界面去进行其他操作
            mTipsView.hideErrorTipView();
        }
    }

    /**
     * addSubView 添加子view到布局中
     *
     * @param view 子view
     */
    private void addSubView(View view) {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(view, params);//添加到布局中
    }

    /**
     * 改变屏幕模式：小屏或者全屏。
     *
     * @param targetMode {@link AliyunScreenMode}
     */
    public void changeScreenMode(AliyunScreenMode targetMode) {
        VcPlayerLog.d(TAG, "mIsFullScreenLocked = " + mIsFullScreenLocked + " ， targetMode = " + targetMode);

        AliyunScreenMode finalScreenMode = targetMode;

        if (mIsFullScreenLocked) {
            finalScreenMode = AliyunScreenMode.Full;
        }

        //这里可能会对模式做一些修改
        if (targetMode != mCurrentScreenMode) {
            mCurrentScreenMode = finalScreenMode;
        }

        Context context = getContext();
        if (context instanceof Activity) {
            if (finalScreenMode == AliyunScreenMode.Full) {
                if (getLockPortraitMode() == null) {
                    //不是固定竖屏播放。
                    ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    //如果是固定全屏，那么直接设置view的布局，宽高
                    ViewGroup.LayoutParams aliVcVideoViewLayoutParams = getLayoutParams();
                    aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                }
            } else if (finalScreenMode == AliyunScreenMode.Small) {

                if (getLockPortraitMode() == null) {
                    //不是固定竖屏播放。
                    ((Activity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    //如果是固定全屏，那么直接设置view的布局，宽高
                    ViewGroup.LayoutParams aliVcVideoViewLayoutParams = getLayoutParams();
                    aliVcVideoViewLayoutParams.height = (int)(ScreenUtils.getWidth(context) * 9.0f / 16);
                    aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                }
            }
        }

        if (mControlView != null) {
            mControlView.setScreenModeStatus(finalScreenMode);
        }

        if (mSpeedView != null) {
            mSpeedView.setScreenMode(finalScreenMode);
        }

        mGuideView.setScreenMode(finalScreenMode);

    }

    /**
     * 获取当前屏幕模式：小屏、全屏
     *
     * @return 当前屏幕模式
     */
    public AliyunScreenMode getScreenMode() {
        return mCurrentScreenMode;
    }

    /**
     * 设置准备事件监听
     *
     * @param onPreparedListener 准备事件
     */
    public void setOnPreparedListener(IAliyunVodPlayer.OnPreparedListener onPreparedListener) {
        mOutPreparedListener = onPreparedListener;
    }

    /**
     * 设置错误事件监听
     *
     * @param onErrorListener 错误事件监听
     */
    public void setOnErrorListener(IAliyunVodPlayer.OnErrorListener onErrorListener) {
        mOutErrorListener = onErrorListener;
    }

    /**
     * 设置信息事件监听
     *
     * @param onInfoListener 信息事件监听
     */
    public void setOnInfoListener(IAliyunVodPlayer.OnInfoListener onInfoListener) {
        mOutInfoListener = onInfoListener;
    }

    /**
     * 设置播放完成事件监听
     *
     * @param onCompletionListener 播放完成事件监听
     */
    public void setOnCompletionListener(IAliyunVodPlayer.OnCompletionListener onCompletionListener) {
        mOutCompletionListener = onCompletionListener;
    }

    /**
     * 设置改变清晰度事件监听
     *
     * @param l 清晰度事件监听
     */
    public void setOnChangeQualityListener(IAliyunVodPlayer.OnChangeQualityListener l) {
        mOutChangeQualityListener = l;
    }

    /**
     * 设置重播事件监听
     *
     * @param onRePlayListener 重播事件监听
     */
    public void setOnRePlayListener(IAliyunVodPlayer.OnRePlayListener onRePlayListener) {
        mOutRePlayListener = onRePlayListener;
    }

    /**
     * 设置自动播放事件监听
     *
     * @param l 自动播放事件监听
     */
    public void setOnAutoPlayListener(IAliyunVodPlayer.OnAutoPlayListener l) {
        mOutAutoPlayListener = l;
    }

    /**
     * 设置PCM数据监听
     *
     * @param l PCM数据监听
     */
    public void setOnPcmDataListener(IAliyunVodPlayer.OnPcmDataListener l) {
        mOutPcmDataListener = l;
    }

    /**
     * 设置源超时监听
     *
     * @param l 源超时监听
     */
    public void setOnTimeExpiredErrorListener(IAliyunVodPlayer.OnTimeExpiredErrorListener l) {
        mOutTimeExpiredErrorListener = l;
    }

    /**
     * 设置鉴权过期监听，在鉴权过期前一分钟回调
     *
     * @param listener
     */
    public void setOnUrlTimeExpiredListener(IAliyunVodPlayer.OnUrlTimeExpiredListener listener) {
        this.mOutUrlTimeExpiredListener = listener;
    }

    /**
     * 设置首帧显示事件监听
     *
     * @param onFirstFrameStartListener 首帧显示事件监听
     */
    public void setOnFirstFrameStartListener(IAliyunVodPlayer.OnFirstFrameStartListener onFirstFrameStartListener) {
        mOutFirstFrameStartListener = onFirstFrameStartListener;
    }

    /**
     * 设置seek结束监听
     *
     * @param onSeekCompleteListener seek结束监听
     */
    public void setOnSeekCompleteListener(IAliyunVodPlayer.OnSeekCompleteListener onSeekCompleteListener) {
        mOuterSeekCompleteListener = onSeekCompleteListener;
    }

    /**
     * 设置停止播放监听
     *
     * @param onStoppedListener 停止播放监听
     */
    public void setOnStoppedListener(IAliyunVodPlayer.OnStoppedListener onStoppedListener) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setOnStoppedListner(onStoppedListener);
        }
    }

    /**
     * 设置加载状态监听
     *
     * @param onLoadingListener 加载状态监听
     */
    public void setOnLoadingListener(IAliyunVodPlayer.OnLoadingListener onLoadingListener) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setOnLoadingListener(onLoadingListener);
        }
    }

    /**
     * 设置缓冲监听
     *
     * @param onBufferingUpdateListener 缓冲监听
     */
    public void setOnBufferingUpdateListener(IAliyunVodPlayer.OnBufferingUpdateListener onBufferingUpdateListener) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        }
    }

    /**
     * 设置视频宽高变化监听
     *
     * @param onVideoSizeChangedListener 视频宽高变化监听
     */
    public void setOnVideoSizeChangedListener(IAliyunVodPlayer.OnVideoSizeChangedListener onVideoSizeChangedListener) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
        }
    }

    /**
     * 设置循环播放开始监听
     *
     * @param onCircleStartListener 循环播放开始监听
     */
    public void setOnCircleStartListener(IAliyunVodPlayer.OnCircleStartListener onCircleStartListener) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setOnCircleStartListener(onCircleStartListener);
        }
    }

    /**
     * 设置PlayAuth的播放方式
     *
     * @param aliyunPlayAuth auth
     */
    public void setAuthInfo(AliyunPlayAuth aliyunPlayAuth) {
        if (mAliyunVodPlayer == null) {
            return;
        }
        //重置界面
        clearAllSource();
        reset();

        mAliyunPlayAuth = aliyunPlayAuth;

        if (mControlView != null) {
            mControlView.setForceQuality(aliyunPlayAuth.isForceQuality());
        }

        //4G的话先提示
        if (!isLocalSource() && NetWatchdog.is4GConnected(getContext())) {
            if (mTipsView != null) {
                mTipsView.showNetChangeTipView();
            }
        } else {
            //具体的准备操作
            prepareAuth(aliyunPlayAuth);
        }
    }

    /**
     * 通过playAuth prepare
     *
     * @param aliyunPlayAuth 源
     */
    private void prepareAuth(AliyunPlayAuth aliyunPlayAuth) {
        if (mTipsView != null) {
            mTipsView.showNetLoadingTipView();
        }
        if (mControlView != null) {
            mControlView.setIsMtsSource(false);
        }
        if (mQualityView != null) {
            mQualityView.setIsMtsSource(false);
        }
        mAliyunVodPlayer.prepareAsync(aliyunPlayAuth);
    }

    /**
     * 清空之前设置的播放源
     */
    private void clearAllSource() {
        mAliyunPlayAuth = null;
        mAliyunVidSts = null;
        mAliyunLocalSource = null;
    }

    /**
     * 设置本地播放源
     *
     * @param aliyunLocalSource 本地播放源
     */
    public void setLocalSource(AliyunLocalSource aliyunLocalSource) {
        if (mAliyunVodPlayer == null) {
            return;
        }

        clearAllSource();
        reset();

        mAliyunLocalSource = aliyunLocalSource;

        if (mControlView != null) {
            mControlView.setForceQuality(true);
        }

        if (!isLocalSource() && NetWatchdog.is4GConnected(getContext())) {
            if (mTipsView != null) {
                mTipsView.showNetChangeTipView();
            }
        } else {
            prepareLocalSource(aliyunLocalSource);
        }

    }

    /**
     * prepare本地播放源
     *
     * @param aliyunLocalSource 本地播放源
     */
    private void prepareLocalSource(AliyunLocalSource aliyunLocalSource) {
        if (mControlView != null) {
            mControlView.setForceQuality(true);
        }
        if (mControlView != null) {
            mControlView.setIsMtsSource(false);
        }

        if (mQualityView != null) {
            mQualityView.setIsMtsSource(false);
        }
        System.out.println("abc : prepared = " + aliyunLocalSource.getSource() + " --- " + mAliyunVodPlayer.getPlayerState());
        mAliyunVodPlayer.prepareAsync(aliyunLocalSource);
        System.out.println("abc : preparedAfter = " + mAliyunVodPlayer.getPlayerState());
    }

    /**
     * 准备vidsts源
     *
     * @param vidSts 源
     */
    public void setVidSts(AliyunVidSts vidSts) {
        if (mAliyunVodPlayer == null) {
            return;
        }

        clearAllSource();
        reset();

        mAliyunVidSts = vidSts;

        if (mControlView != null) {
            mControlView.setForceQuality(vidSts.isForceQuality());
        }

        if (NetWatchdog.is4GConnected(getContext())) {
            if (mTipsView != null) {
                mTipsView.showNetChangeTipView();
            }
        } else {
            prepareVidsts(vidSts);
        }
    }

    /**
     * 准备vidsts 源
     *
     * @param vidSts
     */
    private void prepareVidsts(AliyunVidSts vidSts) {
        if (mTipsView != null) {
            mTipsView.showNetLoadingTipView();
        }
        if (mControlView != null) {
            mControlView.setIsMtsSource(false);
        }

        if (mQualityView != null) {
            mQualityView.setIsMtsSource(false);
        }
        mAliyunVodPlayer.prepareAsync(vidSts);
    }

    /**
     * 设置封面信息
     *
     * @param uri url地址
     */
    public void setCoverUri(String uri) {
        if (mCoverView != null && !TextUtils.isEmpty(uri)) {
            (new ImageLoader(mCoverView)).loadAsync(uri);
            mCoverView.setVisibility(isPlaying() ? GONE : VISIBLE);
        }
    }

    /**
     * 设置封面id
     *
     * @param resId 资源id
     */
    public void setCoverResource(int resId) {
        if (mCoverView != null) {
            mCoverView.setImageResource(resId);
            mCoverView.setVisibility(isPlaying() ? GONE : VISIBLE);
        }
    }

    /**
     * 设置边播边存
     *
     * @param enable      是否开启。开启之后会根据maxDuration和maxSize决定有无缓存。
     * @param saveDir     保存目录
     * @param maxDuration 单个文件最大时长 秒
     * @param maxSize     所有文件最大大小 MB
     */
    public void setPlayingCache(boolean enable, String saveDir, int maxDuration, long maxSize) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setPlayingCache(enable, saveDir, maxDuration, maxSize);
        }
    }

    /**
     * 设置缩放模式
     *
     * @param scallingMode 缩放模式
     */
    public void setVideoScalingMode(IAliyunVodPlayer.VideoScalingMode scallingMode) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setVideoScalingMode(scallingMode);
        }
    }

    /**
     * 开始进度条更新计时器
     */
    private void startProgressUpdateTimer() {
        if (mProgressUpdateTimer != null) {
            mProgressUpdateTimer.removeMessages(0);
            mProgressUpdateTimer.sendEmptyMessageDelayed(0, 1000);
        }
    }

    /**
     * 停止进度条更新计时器
     */
    private void stopProgressUpdateTimer() {
        if (mProgressUpdateTimer != null) {
            mProgressUpdateTimer.removeMessages(0);
        }
    }

    /**
     * 进度更新计时器
     */
    private static class ProgressUpdateTimer extends Handler {

        private WeakReference<AliyunVodPlayerView> viewWeakReference;

        ProgressUpdateTimer(AliyunVodPlayerView aliyunVodPlayerView) {
            viewWeakReference = new WeakReference<AliyunVodPlayerView>(aliyunVodPlayerView);
        }

        @Override
        public void handleMessage(Message msg) {
            AliyunVodPlayerView aliyunVodPlayerView = viewWeakReference.get();
            if (aliyunVodPlayerView != null) {
                aliyunVodPlayerView.handleProgressUpdateMessage(msg);
            }
            super.handleMessage(msg);
        }
    }

    /**
     * 当VodPlayer 没有加载完成的时候,调用onStop 去暂停视频,
     * 会出现暂停失败的问题。
     */
    private static class VodPlayerLoadEndHandler extends Handler{

        private WeakReference<AliyunVodPlayerView> weakReference;

        private boolean intentPause;

        public VodPlayerLoadEndHandler(AliyunVodPlayerView aliyunVodPlayerView){
            weakReference = new WeakReference<>(aliyunVodPlayerView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                intentPause = true;
            }
            if(msg.what == 1){
                AliyunVodPlayerView aliyunVodPlayerView = weakReference.get();
                if(aliyunVodPlayerView != null && intentPause){
                    aliyunVodPlayerView.onStop();
                    intentPause = false;
                }
            }
        }
    }

    /**
     * 处理进度更新消息
     *
     * @param msg
     */
    private void handleProgressUpdateMessage(Message msg) {
        if (mAliyunVodPlayer != null && !inSeek) {
            mControlView.setVideoBufferPosition(mAliyunVodPlayer.getBufferingPosition());
            mControlView.setVideoPosition((int)mAliyunVodPlayer.getCurrentPosition());
        }
        //解决bug：在Prepare中开始更新的时候，不会发送更新消息。
        startProgressUpdateTimer();
    }

    /**
     * 在activity调用onResume的时候调用。 解决home回来后，画面方向不对的问题
     */
    public void onResume() {
        if (mIsFullScreenLocked) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                changeScreenMode(AliyunScreenMode.Small);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                changeScreenMode(AliyunScreenMode.Full);
            }
        }

        if (mNetWatchdog != null) {
            mNetWatchdog.startWatch();
        }

        if (mOrientationWatchDog != null) {
            mOrientationWatchDog.startWatch();
        }

        //从其他界面过来的话，也要show。
        if (mControlView != null) {
            mControlView.show();
        }

        //onStop中记录下来的状态，在这里恢复使用
        resumePlayerState();
    }


    /**
     * 暂停播放器的操作
     */
    public void onStop() {
        if(!(hasLoadEnd != null && hasLoadEnd.size() > 0)){
            vodPlayerLoadEndHandler.sendEmptyMessage(0);
            return ;
        }
        if (mNetWatchdog != null) {
            mNetWatchdog.stopWatch();
        }
        if (mOrientationWatchDog != null) {
            mOrientationWatchDog.stopWatch();
        }

        //保存播放器的状态，供resume恢复使用。
        savePlayerState();
    }

    /**
     * Activity回来后，恢复之前的状态
     */
    private void resumePlayerState() {
        if (mAliyunVodPlayer == null) {
            return;
        }
        if (mPlayerState == PlayerState.Paused) {
            pause();
        } else if (mPlayerState == PlayerState.Started) {
            if (isLocalSource()) {
                reTry();
            } else {
                start();
            }
        }
    }

    /**
     * 保存当前的状态，供恢复使用
     */
    private void savePlayerState() {
        if (mAliyunVodPlayer == null) {
            return;
        }

        mPlayerState = mAliyunVodPlayer.getPlayerState();
        //然后再暂停播放器
        //如果希望后台继续播放，不需要暂停的话，可以注释掉pause调用。
        pause();

    }

    /**
     * 获取媒体信息
     *
     * @return 媒体信息
     */
    public AliyunMediaInfo getMediaInfo() {
        if (mAliyunVodPlayer != null) {
            return mAliyunVodPlayer.getMediaInfo();
        }

        return null;
    }

    /**
     * 活动销毁，释放
     */
    public void onDestroy() {
        stop();
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.release();
        }

        stopProgressUpdateTimer();
        mProgressUpdateTimer = null;

        mSurfaceView = null;
        mGestureView = null;
        mControlView = null;
        mCoverView = null;
        mAliyunVodPlayer = null;
        mGestureDialogManager = null;
        if (mNetWatchdog != null) {
            mNetWatchdog.stopWatch();
        }
        mNetWatchdog = null;
        mTipsView = null;
        mAliyunMediaInfo = null;
        if (mOrientationWatchDog != null) {
            mOrientationWatchDog.destroy();
        }
        mOrientationWatchDog = null;
        if(hasLoadEnd != null){
            hasLoadEnd.clear();
        }
    }

    /**
     * 是否处于播放状态：start或者pause了
     *
     * @return 是否处于播放状态
     */
    public boolean isPlaying() {
        if (mAliyunVodPlayer != null) {
            return mAliyunVodPlayer.isPlaying();
        }
        return false;
    }

    /**
     * 获取播放器状态
     *
     * @return 播放器状态
     */
    public PlayerState getPlayerState() {
        if (mAliyunVodPlayer != null) {
            return mAliyunVodPlayer.getPlayerState();
        }
        return null;
    }

    /**
     * 开始播放
     */
    public void start() {
        if (mControlView != null) {
            mControlView.setPlayState(ControlView.PlayState.Playing);
        }
        //显示其他的动作
        //mControlView.setHideType(ViewAction.HideType.Normal);
        //mGestureView.setHideType(ViewAction.HideType.Normal);
        mGestureView.show();
        mControlView.show();

        if (mAliyunVodPlayer == null) {
            return;
        }

        PlayerState playerState = mAliyunVodPlayer.getPlayerState();
        if (playerState == PlayerState.Paused || playerState == PlayerState.Prepared || mAliyunVodPlayer.isPlaying()) {
            mAliyunVodPlayer.start();
        }

    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mControlView != null) {
            mControlView.setPlayState(ControlView.PlayState.NotPlaying);
        }

        if (mAliyunVodPlayer == null) {
            return;
        }

        PlayerState playerState = mAliyunVodPlayer.getPlayerState();
        if (playerState == PlayerState.Started || mAliyunVodPlayer.isPlaying()) {
            mAliyunVodPlayer.pause();
        }
    }

    /**
     * 停止播放
     */
    private void stop() {
        Boolean hasLoadedEnd = null;
        AliyunMediaInfo mediaInfo = null;
        if(mAliyunVodPlayer != null && hasLoadEnd != null){
            mediaInfo = mAliyunVodPlayer.getMediaInfo();
            hasLoadedEnd = hasLoadEnd.get(mediaInfo);
        }

        if (mAliyunVodPlayer != null && hasLoadedEnd != null) {
            mAliyunVodPlayer.stop();

        }
        if (mControlView != null) {
            mControlView.setPlayState(ControlView.PlayState.NotPlaying);
        }
        if(hasLoadEnd != null){
            hasLoadEnd.remove(mediaInfo);
        }
    }

    /**
     * seek操作
     *
     * @param position 目标位置
     */
    public void seekTo(int position) {
        if (mAliyunVodPlayer == null) {
            return;
        }
        inSeek = true;
        mAliyunVodPlayer.seekTo(position);
        mAliyunVodPlayer.start();
        if (mControlView != null) {
            mControlView.setPlayState(ControlView.PlayState.Playing);
        }
    }

    /**
     * 设置是否显示标题栏
     *
     * @param show true:是
     */
    public void setTitleBarCanShow(boolean show) {
        if (mControlView != null) {
            mControlView.setTitleBarCanShow(show);
        }
    }

    /**
     * 设置是否显示控制栏
     *
     * @param show true:是
     */
    public void setControlBarCanShow(boolean show) {
        if (mControlView != null) {
            mControlView.setControlBarCanShow(show);
        }

    }

    /**
     * 开启底层日志
     */
    public void enableNativeLog() {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.enableNativeLog();
        }
    }

    /**
     * 关闭底层日志
     */
    public void disableNativeLog() {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.disableNativeLog();
        }
    }

    /**
     * 设置线程池
     *
     * @param executorService 线程池
     */
    public void setThreadExecutorService(ExecutorService executorService) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setThreadExecutorService(executorService);
        }
    }

    /**
     * 获取SDK版本号
     *
     * @return SDK版本号
     */
    public String getSDKVersion() {
        return AliyunVodPlayer.getSDKVersion();
    }

    /**
     * 获取播放surfaceView
     *
     * @return 播放surfaceView
     */
    public SurfaceView getPlayerView() {
        return mSurfaceView;
    }

    /**
     * 设置自动播放
     *
     * @param auto true 自动播放
     */
    public void setAutoPlay(boolean auto) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setAutoPlay(auto);
        }
    }

    /**
     * 获取底层的一些debug信息
     *
     * @return debug信息
     */
    public Map<String, String> getAllDebugInfo() {
        if (mAliyunVodPlayer != null) {
            return mAliyunVodPlayer.getAllDebugInfo();
        }
        return null;
    }

    /**
     * 设置锁定竖屏监听
     *
     * @param listener 监听器
     */
    public void setLockPortraitMode(IAliyunVodPlayer.LockPortraitListener listener) {
        mLockPortraitListener = listener;
    }

    /**
     * 锁定竖屏
     *
     * @return 竖屏监听器
     */
    public IAliyunVodPlayer.LockPortraitListener getLockPortraitMode() {
        return mLockPortraitListener;
    }

    /**
     * 让home键无效
     *
     * @param keyCode 按键
     * @param event   事件
     * @return 是否处理。
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mIsFullScreenLocked && (keyCode != KeyEvent.KEYCODE_HOME)) {
            return false;
        }
        return true;
    }

    /**
     * 截图功能
     *
     * @return 图片
     */
    public Bitmap snapShot() {
        if (mAliyunVodPlayer != null) {
            return mAliyunVodPlayer.snapShot();
        }

        return null;
    }

    /**
     * 设置循环播放
     *
     * @param circlePlay true:循环播放
     */
    public void setCirclePlay(boolean circlePlay) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setCirclePlay(circlePlay);
        }
    }

    /**
     * 设置播放时的镜像模式
     *
     * @param mode 镜像模式
     */
    public void setRenderMirrorMode(IAliyunVodPlayer.VideoMirrorMode mode) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setRenderMirrorMode(mode);
        }
    }

    /**
     * 设置播放时的旋转方向
     *
     * @param rotate 旋转角度
     */
    public void setRenderRotate(IAliyunVodPlayer.VideoRotate rotate) {
        if (mAliyunVodPlayer != null) {
            mAliyunVodPlayer.setRenderRotate(rotate);
        }
    }

    /**
     * 播放按钮点击listener
     */
    public interface OnPlayStateBtnClickListener {
        void onPlayBtnClick(PlayerState playerState);
    }

    /**
     * 设置播放状态点击监听
     *
     * @param listener
     */
    public void setOnPlayStateBtnClickListener(OnPlayStateBtnClickListener listener) {
        this.onPlayStateBtnClickListener = listener;
    }

    private OnSeekStartListener onSeekStartListener;

    /**
     * seek开始监听
     */

    public interface OnSeekStartListener {
        void onSeekStart();
    }

    public void setOnSeekStartListener(OnSeekStartListener listener) {
        this.onSeekStartListener = listener;
    }

    /**
     * Player View Click Type
     */
    public enum PlayViewType {
        /**
         * click download view
         */
        Download,
        /**
         * click screen cast
         */
        ScreenCast
    }

    public interface OnPlayerViewClickListener {
        void onClick(AliyunScreenMode screenMode, PlayViewType viewType);
    }

    /**
     * 设置播放器view点击事件监听，目前只对外暴露下载按钮和投屏按钮
     *
     * @param mOnPlayerViewClickListener
     */
    public void setmOnPlayerViewClickListener(
        OnPlayerViewClickListener mOnPlayerViewClickListener) {
        this.mOnPlayerViewClickListener = mOnPlayerViewClickListener;
    }

    /**
     * 屏幕方向改变监听接口
     */
    public interface OnOrientationChangeListener {
        /**
         * 屏幕方向改变
         *
         * @param from        从横屏切换为竖屏, 从竖屏切换为横屏
         * @param currentMode 当前屏幕类型
         */
        void orientationChange(boolean from, AliyunScreenMode currentMode);
    }

    private OnOrientationChangeListener orientationChangeListener;

    public void setOrientationChangeListener(
        OnOrientationChangeListener listener) {
        this.orientationChangeListener = listener;
    }

    /**
     * 断网/连网监听
     */
    private class MyNetConnectedListener implements NetWatchdog.NetConnectedListener {
        public MyNetConnectedListener(AliyunVodPlayerView aliyunVodPlayerView) {}

        @Override
        public void onReNetConnected(boolean isReconnect) {
            if (mNetConnectedListener != null) {
                mNetConnectedListener.onReNetConnected(isReconnect);
            }
        }

        @Override
        public void onNetUnConnected() {
            if (mNetConnectedListener != null) {
                mNetConnectedListener.onNetUnConnected();
            }
        }
    }

    public void setNetConnectedListener(NetConnectedListener listener) {
        this.mNetConnectedListener = listener;
    }

    /**
     * 判断是否有网络的监听
     */
    public interface NetConnectedListener {
        /**
         * 网络已连接
         */
        void onReNetConnected(boolean isReconnect);

        /**
         * 网络未连接
         */
        void onNetUnConnected();
    }

    /**
     * 横屏下显示更多
     */
    public interface OnShowMoreClickListener {
        void showMore();
    }

    public void setOnShowMoreClickListener(
        ControlView.OnShowMoreClickListener listener) {
        this.mOutOnShowMoreClickListener = listener;
    }
}
