package com.eb.new_line_seller.mvp.model;

import android.content.Context;


import com.eb.new_line_seller.mvp.contacts.CourseInfoContacts;
import com.eb.new_line_seller.util.HttpUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.CourseInfo;



public class CourseInfoMdl extends BaseModel implements CourseInfoContacts.CourseInfoMdl {
    Context context;

    public CourseInfoMdl(Context context) {
        this.context = context;
    }

    @Override
    public void getInfo(int id, RxSubscribe<CourseInfo> rxSubscribe) {

        sendRequest(HttpUtils.getApi().courseInfo(getToken(context),id).compose(RxHelper.<CourseInfo>observe()), rxSubscribe);

    }
}
