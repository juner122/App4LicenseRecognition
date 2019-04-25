package com.eb.geaiche.mvp.presenter;

import android.app.Activity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.adapter.MessageModleAdapter;
import com.eb.geaiche.adapter.MessageRecordAdapter;
import com.eb.geaiche.mvp.contacts.MessageMarketingContacts;
import com.eb.geaiche.mvp.model.MessageMarketingMdl;
import com.juner.mvp.base.presenter.BasePresenter;
import com.juner.mvp.bean.MessageModleEntity;
import com.juner.mvp.bean.MessageRecordEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

public class MessageMarketingPtr extends BasePresenter<MessageMarketingContacts.MessageMarketingUI> implements MessageMarketingContacts.MessageMarketingPtr {
    private MessageMarketingContacts.MessageMarketingMdl mMessageMarketingMdl;
    private Activity context;

    MessageModleAdapter messageModleAdapter;

    MessageRecordAdapter messageRecordAdapter;


    public MessageMarketingPtr(@NonNull MessageMarketingContacts.MessageMarketingUI view) {
        super(view);
        // 实例化 Model 层
        mMessageMarketingMdl = new MessageMarketingMdl();
        context = getView().getSelfActivity();

        messageModleAdapter = new MessageModleAdapter(null, context);
        messageRecordAdapter = new MessageRecordAdapter(null, context);


    }


    @Override
    public void getRecordInfo(RecyclerView rv2) {
        rv2.setLayoutManager(new LinearLayoutManager(context));
        rv2.setAdapter(messageRecordAdapter);
        List<MessageRecordEntity> data = new ArrayList<>();
        data.add(new MessageRecordEntity());
        data.add(new MessageRecordEntity());
        data.add(new MessageRecordEntity());
        data.add(new MessageRecordEntity());
        data.add(new MessageRecordEntity());
        data.add(new MessageRecordEntity());
        data.add(new MessageRecordEntity());
        data.add(new MessageRecordEntity());
        data.add(new MessageRecordEntity());

        messageRecordAdapter.setNewData(data);
    }


    @Override
    public void getModleInfo(RecyclerView rv3) {
        rv3.setLayoutManager(new LinearLayoutManager(context));
        rv3.setAdapter(messageModleAdapter);

        List<MessageModleEntity> data = new ArrayList<>();

        data.add(new MessageModleEntity());
        data.add(new MessageModleEntity());
        data.add(new MessageModleEntity());
        data.add(new MessageModleEntity());
        data.add(new MessageModleEntity());
        data.add(new MessageModleEntity());
        data.add(new MessageModleEntity());
        data.add(new MessageModleEntity());
        data.add(new MessageModleEntity());
        data.add(new MessageModleEntity());

        messageModleAdapter.setNewData(data);
    }
}
