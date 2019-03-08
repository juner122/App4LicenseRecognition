package com.eb.new_line_seller.mvp.presenter;

import android.app.Activity;

import com.eb.new_line_seller.mvp.contacts.MessageMarketingContacts;
import com.eb.new_line_seller.mvp.model.MessageMarketingMdl;
import com.juner.mvp.base.presenter.BasePresenter;

import io.reactivex.annotations.NonNull;

public class MessageMarketingPtr extends BasePresenter<MessageMarketingContacts.MessageMarketingUI> implements MessageMarketingContacts.MessageMarketingPtr {
    private MessageMarketingContacts.MessageMarketingMdl mMessageMarketingMdl;
    private Activity context;


    public MessageMarketingPtr(@NonNull MessageMarketingContacts.MessageMarketingUI view) {
        super(view);
        // 实例化 Model 层
        mMessageMarketingMdl = new MessageMarketingMdl();
        context = getView().getSelfActivity();
    }


}
