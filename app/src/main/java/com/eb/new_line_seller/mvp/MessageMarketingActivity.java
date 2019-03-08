package com.eb.new_line_seller.mvp;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.mvp.contacts.MessageMarketingContacts;
import com.eb.new_line_seller.mvp.presenter.MessageMarketingPtr;
import com.juner.mvp.base.IBaseXPresenter;

public class MessageMarketingActivity extends BaseActivity<MessageMarketingContacts.MessageMarketingPtr> implements MessageMarketingContacts.MessageMarketingUI {


    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_message_marketing;
    }

    @Override
    protected void init() {
        tv_title.setText("短信营销");

    }


    @Override
    public MessageMarketingContacts.MessageMarketingPtr onBindPresenter() {
        return new MessageMarketingPtr(this);
    }
}
