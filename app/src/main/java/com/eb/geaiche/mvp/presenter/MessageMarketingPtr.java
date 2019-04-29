package com.eb.geaiche.mvp.presenter;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.adapter.Brandadapter2;
import com.eb.geaiche.adapter.MessageModleAdapter;
import com.eb.geaiche.adapter.MessageModleAdapter2;
import com.eb.geaiche.adapter.MessageRecordAdapter;
import com.eb.geaiche.mvp.contacts.MessageMarketingContacts;
import com.eb.geaiche.mvp.model.MessageMarketingMdl;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.CommonPopupWindow;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.MessageModleEntity;
import com.juner.mvp.bean.MessageRecordEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.SmsTemplates;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class MessageMarketingPtr extends BasePresenter<MessageMarketingContacts.MessageMarketingUI> implements MessageMarketingContacts.MessageMarketingPtr {
    private MessageMarketingContacts.MessageMarketingMdl mdl;
    private Activity context;

    MessageModleAdapter messageModleAdapter;

    MessageRecordAdapter messageRecordAdapter;

    SmsTemplates pick_smsTemplates;//发送参数


    public MessageMarketingPtr(@NonNull MessageMarketingContacts.MessageMarketingUI view) {
        super(view);
        // 实例化 Model 层
        context = getView().getSelfActivity();
        mdl = new MessageMarketingMdl(context);


        messageModleAdapter = new MessageModleAdapter(null, context);
        messageRecordAdapter = new MessageRecordAdapter(null, context);

        initSmsTemplatesListPop();
    }


    @Override
    public void getRecordInfo(RecyclerView rv2) {
        rv2.setLayoutManager(new LinearLayoutManager(context));
        rv2.setAdapter(messageRecordAdapter);


        mdl.getSmsMarketLogs(new RxSubscribe<List<MessageRecordEntity>>(context, true) {
            @Override
            protected void _onNext(List<MessageRecordEntity> m) {
                messageRecordAdapter.setNewData(m);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);

            }
        });


    }


    @Override
    public void getModleInfo(RecyclerView rv3) {
        rv3.setLayoutManager(new LinearLayoutManager(context));
        rv3.setAdapter(messageModleAdapter);

        mdl.getSmsTemplates(new RxSubscribe<List<SmsTemplates>>(context, false) {
            @Override
            protected void _onNext(List<SmsTemplates> smsTemplates) {
                messageModleAdapter.setNewData(smsTemplates);
                messageModleAdapter2.setNewData(smsTemplates);


                if (smsTemplates.size() > 0) {
                    pick_smsTemplates = smsTemplates.get(0);
                    getView().setTitle(smsTemplates.get(0).getTitle());
                    getView().setContent(smsTemplates.get(0).getContent());

                }

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });


    }

    @Override
    public void showPopUp(View view) {
        popupWindow.showAsDropDown(view, view.getWidth(), 0);
    }

    CommonPopupWindow popupWindow;
    RecyclerView commonPopupRecyclerView;
    MessageModleAdapter2 messageModleAdapter2;


    //初始化popup
    private void initSmsTemplatesListPop() {

        messageModleAdapter2 = new MessageModleAdapter2(null);
        messageModleAdapter2.setOnItemClickListener((adapter, view, position) -> {
            popupWindow.dismiss();
            getView().setTitle(messageModleAdapter2.getData().get(position).getTitle());
            getView().setContent(messageModleAdapter2.getData().get(position).getContent());
            pick_smsTemplates = messageModleAdapter2.getData().get(position);
        });


        commonPopupRecyclerView = new RecyclerView(context);
        commonPopupRecyclerView.setAdapter(messageModleAdapter2);

        commonPopupRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        popupWindow = new CommonPopupWindow.Builder(context)
                .setView(commonPopupRecyclerView)
                .create();

    }


    @Override
    public void sendSms() {

        if (null == getView().getMemberList() || getView().getMemberList().size() == 0) {
            ToastUtils.showToast("请选择收信人！");
            return;
        }

        pick_smsTemplates.setUsers(getView().getMemberList());

        mdl.onSendSms(new RxSubscribe<NullDataEntity>(context, true) {
            @Override
            protected void _onNext(NullDataEntity nullDataEntity) {
                ToastUtils.showToast("发送成功！");

                finish();

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("发送失败！" + message);
            }
        }, pick_smsTemplates);

    }

}
