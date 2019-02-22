package com.eb.new_line_seller.mvp.contacts;


import android.support.v7.widget.RecyclerView;
import android.view.SurfaceView;

import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.CourseInfo;
import com.juner.mvp.bean.Courses;



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

        void play();

    }

    /**
     * model 层接口
     */
    public interface CourseInfoMdl {


        void getInfo(int id, RxSubscribe<CourseInfo> rxSubscribe);//页面数据接口


    }


}
