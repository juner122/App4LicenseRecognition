package com.frank.plate.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frank.plate.api.ApiLoader;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

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


        Log.d(setTAG(), "---->>>>onCreateView");
        return rootView;
    }

    public ApiLoader Api() {
        if (apiLoader == null)
            apiLoader = new ApiLoader();
        return apiLoader;

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(setTAG(), "---->>>>onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(setTAG(), "---->>>>onPause");
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


    protected void toActivity(Class c) {

        startActivity(new Intent(getContext(), c));

    }

    protected void toActivity(Class c, String key, String str) {


        Intent intent = new Intent(this.getActivity(), c);

        intent.putExtra(key, str);
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
