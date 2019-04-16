package com.eb.geaiche.mvp.model;

import android.content.Context;

import com.eb.geaiche.mvp.contacts.CustomContacts;
import com.eb.geaiche.util.HttpUtils;
import com.juner.mvp.api.http.RxHelper;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.model.BaseModel;
import com.juner.mvp.bean.Component;
import com.juner.mvp.bean.FixParts2item;
import com.juner.mvp.bean.FixService2item;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Project;
import com.juner.mvp.bean.ShopProject;

import java.util.List;

import retrofit2.http.Field;

public class CustomMdl extends BaseModel implements CustomContacts.CustomMdl {
    Context context;

    public CustomMdl(Context context) {
        this.context = context;
    }

    @Override
    public void componentSave(Component component, RxSubscribe<Component> rxSubscribe) {

        sendRequest(HttpUtils.getFix().componentSave(getToken(context), component).compose(RxHelper.<Component>observe()), rxSubscribe);

    }

    @Override
    public void componentFirstCategory(RxSubscribe<List<FixParts2item>> rxSubscribe) {


        sendRequest(HttpUtils.getFix().componentFirstCategory(getToken(context)).compose(RxHelper.<List<FixParts2item>>observe()), rxSubscribe);
    }

    @Override
    public void componentSecondCategory(int parent_id, RxSubscribe<List<FixParts2item>> rxSubscribe) {


        sendRequest(HttpUtils.getFix().componentSecondCategory(getToken(context), parent_id).compose(RxHelper.<List<FixParts2item>>observe()), rxSubscribe);
    }


    @Override
    public void addShopService(ShopProject shopProject, RxSubscribe<ShopProject> rxSubscribe) {

        sendRequest(HttpUtils.getFix().addShopService(getToken(context), shopProject).compose(RxHelper.<ShopProject>observe()), rxSubscribe);

    }

    //添加自定服务或配件 3服务4零件
    @Override
    public void xgxshopgoodsSave(Project project, RxSubscribe<String> rxSubscribe) {

        sendRequest(HttpUtils.getFix().xgxshopgoodsSave(getToken(context),project).compose(RxHelper.<String>observe()), rxSubscribe);

    }

    @Override
    public void firstService(RxSubscribe<List<FixService2item>> rxSubscribe) {
        sendRequest(HttpUtils.getFix().firstService(getToken(context)).compose(RxHelper.<List<FixService2item>>observe()), rxSubscribe);
    }

    @Override
    public void secondService(int parent_id, RxSubscribe<List<FixService2item>> rxSubscribe) {
        sendRequest(HttpUtils.getFix().secondService(getToken(context), parent_id).compose(RxHelper.<List<FixService2item>>observe()), rxSubscribe);
    }




}
