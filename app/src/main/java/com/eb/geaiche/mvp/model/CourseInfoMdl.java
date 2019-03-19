package com.eb.geaiche.mvp.model;

import android.content.Context;


import com.eb.geaiche.mvp.contacts.CourseInfoContacts;
import com.eb.geaiche.util.HttpUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.CourseInfo;
import com.juner.mvp.bean.CourseRecord;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Video;


public class CourseInfoMdl extends BaseModel implements CourseInfoContacts.CourseInfoMdl {
    Context context;

    public CourseInfoMdl(Context context) {
        this.context = context;
    }

    @Override
    public void getInfo(int id, RxSubscribe<CourseInfo> rxSubscribe) {

        sendRequest(HttpUtils.getApi().courseInfo(getToken(context), id).compose(RxHelper.<CourseInfo>observe()), rxSubscribe);

    }

    @Override
    public void addWatchLog(CourseRecord courseRecord, RxSubscribe<NullDataEntity> rxSubscribe) {
        sendRequest(HttpUtils.getApi().addWatchLog(getToken(context), courseRecord).compose(RxHelper.<NullDataEntity>observe()), rxSubscribe);
    }

    @Override
    public void resourceUrl(String videoId, RxSubscribe<Video> rxSubscribe) {
        sendRequest(HttpUtils.getApi().resourceUrl(getToken(context), videoId).compose(RxHelper.<Video>observe()), rxSubscribe);
    }
}
