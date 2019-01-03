package com.eb.new_line_seller.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eb.new_line_seller.api.ApiLoader;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

import static com.eb.new_line_seller.Configure.ORDERINFO;

public abstract class BaseFragment extends Fragment {
    public Context mContext;
    ApiLoader apiLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(setLayoutResourceID(), container,
                false);
        ButterKnife.bind(this, rootView);
        mContext = getContext();
        setUpView();
        setView(rootView);

        Log.d(setTAG(), "---->>>>onCreateView");
        return rootView;
    }

    public ApiLoader Api() {
        if (apiLoader == null)
            apiLoader = new ApiLoader(getContext());
        return apiLoader;

    }


    /**
     * fragment可见的时候操作，取代onResume，且在可见状态切换到可见的时候调用
     */
    protected void onVisible() {
        Log.d(setTAG(), "---->>>>onVisible");
    }

    /**
     * fragment不可见的时候操作,onPause的时候,以及不可见的时候调用
     */
    protected void onHidden() {
        Log.d(setTAG(), "---->>>>onHidden");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onVisible();
        } else {
            onHidden();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(setTAG(), "---->>>>onResume");
        if (isAdded() && !isHidden()) {//用isVisible此时为false，因为mView.getWindowToken为null
            onVisible();
        }


    }

    @Override
    public void onPause() {

        super.onPause();
        Log.d(setTAG(), "---->>>>onPause");
        if (isVisible())
            onHidden();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(setTAG(), "---->>>>onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(setTAG(), "---->>>>onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(setTAG(), "---->>>>onDestroy");
    }

    /**
     * 此方法用于返回Fragment设置ContentView的布局文件资源ID
     *
     * @return 布局文件资源ID
     */
    public abstract int setLayoutResourceID();

    /**
     * 一些View的相关操作
     */
    protected abstract void setUpView();

    protected void setView(View v) {
    }


    protected void toActivity(Class c) {

        startActivity(new Intent(getContext(), c));

    }

    protected void toActivity(Class c, String key, String str) {


        Intent intent = new Intent(this.getActivity(), c);

        intent.putExtra(key, str);
        startActivity(intent);

    }

    protected void toActivity(Class c, String key, int str) {


        Intent intent = new Intent(this.getActivity(), c);

        intent.putExtra(key, str);
        startActivity(intent);

    }


    protected void toActivity(Class c, Parcelable p, String key) {


        Intent intent = new Intent(getActivity(), c);
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, p);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    protected void sendOrderInfo(Class c, Parcelable p) {


        Intent intent = new Intent(getActivity(), c);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ORDERINFO, p);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    protected abstract String setTAG();


    protected void msgManagement(double what) {


    }

    /**
     * 发送不延时的消息
     *
     * @param what 发送的消息
     */
    public void sendMsg(final double what) {

        Observable.empty().observeOn(AndroidSchedulers.mainThread()).doOnComplete(new Action() {
            @Override
            public void run() {
                msgManagement(what);
            }
        }).subscribe();
    }

}
