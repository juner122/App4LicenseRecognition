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
    public void play() {

    }
}
