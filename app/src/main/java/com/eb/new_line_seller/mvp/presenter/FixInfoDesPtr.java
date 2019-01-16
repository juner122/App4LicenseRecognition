package com.eb.new_line_seller.mvp.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.activity.ResultBack;
import com.eb.new_line_seller.activity.TechnicianListActivity;
import com.eb.new_line_seller.mvp.FixInfoDescribeActivity;
import com.eb.new_line_seller.mvp.contacts.FixInfoDesContacts;
import com.eb.new_line_seller.mvp.model.FixInfoDesMdl;
import com.eb.new_line_seller.util.String2Utils;
import com.juner.mvp.Configure;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.FixInfo;
import com.juner.mvp.bean.FixInfoEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.Technician;

import java.util.List;

public class FixInfoDesPtr extends BasePresenter<FixInfoDesContacts.FixInfoDesUI> implements FixInfoDesContacts.FixInfoDesPtr {


    List<Technician> technicians;//选择的技师
    FixInfoDesContacts.FixInfoDesMdl mdl;
    String carNo, userName, mobile, describe;
    int carId, userId;

    FixInfoEntity fixInfo;//请求对象

    public FixInfoDesPtr(@NonNull FixInfoDesContacts.FixInfoDesUI view) {
        super(view);
        mdl = new FixInfoDesMdl(view.getSelfActivity());
    }

    /**
     * 添加快捷tip
     */
    @Override
    public void setTipClickListener(List<TextView> textViews) {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.setBackgroundResource(R.drawable.button_background_b);
                getView().setTip(((TextView) view).getText().toString());

            }
        };
        for (TextView v : textViews) {
            v.setOnClickListener(clickListener);
        }

    }

    @Override
    public void getInfo() {

        carNo = getView().getSelfActivity().getIntent().getStringExtra(Configure.car_no);
        carId = getView().getSelfActivity().getIntent().getIntExtra(Configure.car_id, 0);
        userId = getView().getSelfActivity().getIntent().getIntExtra(Configure.user_id, 0);
        userName = getView().getSelfActivity().getIntent().getStringExtra(Configure.user_name);
        mobile = getView().getSelfActivity().getIntent().getStringExtra(Configure.moblie);
        getView().setCarNo(carNo);

    }

    @Override
    public void getDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public void toTechnicianListActivity() {

        ((FixInfoDescribeActivity) getView().getSelfActivity()).startActivityForResult(new Intent(getView().getSelfActivity(), TechnicianListActivity.class), new ResultBack() {
            @Override
            public void resultOk(Intent data) {
                technicians = data.getParcelableArrayListExtra("Technician");
                getView().setTechnician(String2Utils.getString(technicians));

            }
        });

    }

    @Override
    public void quotationSave() {

        fixInfo = new FixInfoEntity();
        fixInfo.setCarId(carId);
        fixInfo.setCarNo(carNo);
        fixInfo.setDescribe(describe);
        fixInfo.setMobile(mobile);
        fixInfo.setUserId(userId);
        fixInfo.setUserName(userName);
        fixInfo.setSysUserList(technicians);


        mdl.quotationSave(fixInfo, new RxSubscribe<NullDataEntity>(getView().getSelfActivity(), true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {

                getView().showToast("保存成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {
                getView().showToast("保存失败：" + message);
            }
        });

    }
}
