package com.aliyun.vodplayerview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.vodplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置界面, 主要是UrlPlayFragment和VidPlayFragment的寄生Activity
 * Created by Mulberry on 2018/4/4.
 */
public class AliyunPlayerSettingActivity extends FragmentActivity implements
        OnClickListener, OnNotifyActivityListener, Handler.Callback {

    /**
     * 移除 Fragment 的Message
     */
    private static final int REMOVE_FRAGMENT_MSG = 0x0001;
    private TextView tvVidplay;
    private TextView tvUrlplay;
    private ImageView ivVidplay;
    private ImageView ivUrlplay;
    private Button btnStartPlayer;

    private ArrayList<Fragment> fragmentArrayList;

    private static final int FRAGMENT_VID_PLAY = 0;
    private static final int FRAGMENT_URL_PLAY = 1;
    AliyunVidPlayFragment aliyunVidPlayFragment;
    AliyunUrlPlayFragment aliyunUrlPlayFragment;
    private ImageView ivBack;
    private List<Fragment> interimFragmentList;
    private Handler mHandler;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler(Looper.getMainLooper(), this);
        setContentView(R.layout.activity_player_setting_layout);
        tvVidplay = (TextView) findViewById(R.id.tv_vidplay);
        tvUrlplay = (TextView) findViewById(R.id.tv_urlplay);
        ivVidplay = (ImageView) findViewById(R.id.iv_vidplay);
        ivUrlplay = (ImageView) findViewById(R.id.iv_urlplay);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        btnStartPlayer = (Button) findViewById(R.id.btn_start_player);

        fragmentArrayList = new ArrayList<Fragment>();
        aliyunVidPlayFragment = new AliyunVidPlayFragment();
        aliyunUrlPlayFragment = new AliyunUrlPlayFragment();
        aliyunVidPlayFragment.setOnNotifyActivityListener(this);
        aliyunUrlPlayFragment.setOnNotifyActivityListener(this);
        fragmentArrayList.add(aliyunVidPlayFragment);
        fragmentArrayList.add(aliyunUrlPlayFragment);

        tvVidplay.setOnClickListener(this);
        tvUrlplay.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        btnStartPlayer.setOnClickListener(this);

        ivVidplay.setActivated(true);
        ivUrlplay.setActivated(false);

        changeFragment(FRAGMENT_VID_PLAY);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_vidplay) {

            changeFragment(FRAGMENT_VID_PLAY);
            ivVidplay.setActivated(true);
            ivUrlplay.setActivated(false);

        } else if (i == R.id.tv_urlplay) {

            changeFragment(FRAGMENT_URL_PLAY);
            ivUrlplay.setActivated(true);
            ivVidplay.setActivated(false);

        } else if (i == R.id.btn_start_player) {
            if (mCurrentFrgment instanceof AliyunVidPlayFragment) {
                aliyunVidPlayFragment.startToPlayerByVid();
            } else if (mCurrentFrgment instanceof AliyunUrlPlayFragment) {
                aliyunUrlPlayFragment.startPlayerByUrl();
            }
        } else if (i == R.id.iv_back) {
            finish();
        }
    }

    private Fragment mCurrentFrgment;

    /**
     * use index to change fragment
     *
     * @param index
     */
    private void changeFragment(int index) {
        if (findViewById(R.id.player_settings_content) != null) {
            interimFragmentList = new ArrayList<>();
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (null != mCurrentFrgment) {
                ft.hide(mCurrentFrgment);
            }
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentArrayList.get(index).getClass().getName());

            if (null == fragment) {
                fragment = fragmentArrayList.get(index);
            }
            mCurrentFrgment = fragment;

            /**
             * monkey 测试的错误日志   java.lang.IllegalStateException: Fragment already added: AliyunUrlPlayFragment
             */
            if (!fragment.isAdded() && !interimFragmentList.contains(fragment)) {
                ft.add(R.id.player_settings_content, fragment, fragment.getClass().getName());
                interimFragmentList.add(fragment);
            } else {
                ft.show(fragment);
            }
            ft.commit();
            mHandler.obtainMessage(REMOVE_FRAGMENT_MSG, fragment).sendToTarget();


        }
    }

    @Override
    public void onNotifyActivity() {
        finish();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case REMOVE_FRAGMENT_MSG:
                Fragment fragment = (Fragment) msg.obj;
                interimFragmentList.remove(fragment);
                break;
            default:
                break;
        }
        return false;
    }
}
