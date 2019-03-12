package com.eb.new_line_seller.mvp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.MainActivity;
import com.eb.new_line_seller.activity.ResultBack;
import com.juner.mvp.Configure;
import com.juner.mvp.base.BaseXActivity;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * View的实现
 * 实现IBaseView中方法，并继承BaseXActivity
 */
public abstract class BaseActivity<P extends IBasePresenter> extends BaseXActivity<P> implements IBaseView {
    @BindView(R.id.head_view)
    View head_view;
    @BindView(R.id.tv_title_r)
    protected TextView tv_title_r;

    @BindView(R.id.tv_title)
    protected TextView tv_title;

    protected void hideHeadView() {
        head_view.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        //设置布局
        setContentView(setLayoutResourceID());
        ButterKnife.bind(this);
        init();

    }


    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(msg, getSelfActivity());
    }

    @OnClick(R.id.tv_back)
    protected void onClick() {
        finish();
    }

    protected void toActivity(Class c) {

        startActivity(new Intent(this, c));

    }

    public void setRTitle(String str) {
        tv_title_r.setVisibility(View.VISIBLE);
        tv_title_r.setText(str);
    }

    protected void toActivity(Class c, String key, int str) {


        Intent intent = new Intent(this, c);
        intent.putExtra(key, str);
        startActivity(intent);

    }

    protected void toActivity(Class c, String key, String str) {


        Intent intent = new Intent(this, c);
        intent.putExtra(key, str);
        startActivity(intent);

    }

    protected void toActivity(Class c, Parcelable p, String key) {


        Intent intent = new Intent(this, c);
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, p);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    protected void toActivity(Class c, List p, String key) {


        Intent intent = new Intent(this, c);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) p);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    protected void toActivity(Class c, ArrayList p, String key) {


        Intent intent = new Intent(this, c);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(key, p);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    protected void toMain(int fragment_p) {

        Intent intent2 = new Intent(this, MainActivity.class);
        intent2.putExtra(Configure.show_fragment, fragment_p);
        startActivity(intent2);

    }

    private ArrayList<ResultBack> list = new ArrayList<>();//保存所有activity返回的回调，跟随activity生命周期

    public synchronized void startActivityForResult(Intent intent, ResultBack resultBack) {
        if (list.indexOf(resultBack) < 0) {
            list.add(resultBack);
            startActivityForResult(intent, list.size() - 1);//requestCode就是list的Index
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == list) return;
        if (resultCode == RESULT_OK) {
            list.get(requestCode).resultOk(data);
        } else {
            list.get(requestCode).resultElse(resultCode, data);
        }
        list.set(requestCode, null);//释放已返回的ResultBack，不用remove防止乱序
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public abstract int setLayoutResourceID();

    protected abstract void init();
}
