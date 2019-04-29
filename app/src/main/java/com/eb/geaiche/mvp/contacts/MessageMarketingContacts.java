package com.eb.geaiche.mvp.contacts;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.eb.geaiche.adapter.Brandadapter2;
import com.eb.geaiche.adapter.MessageModleAdapter2;
import com.juner.mvp.api.http.RxSubscribe;
import com.juner.mvp.base.presenter.IBasePresenter;
import com.juner.mvp.base.view.IBaseView;
import com.juner.mvp.bean.MemberEntity;
import com.juner.mvp.bean.MessageRecordEntity;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.SmsTemplates;

import java.util.List;

/**
 * 登录页面契约类Contacts
 * <p>
 * 将View、Presenter、Model中的实现接口写在一起，看起来更加规范清晰，方便查找
 */
public class MessageMarketingContacts {
    /**
     * view 层接口
     */
    public interface MessageMarketingUI extends IBaseView {

        void setTitle(String title);

        void setContent(String content);

        List<MemberEntity> getMemberList();

        void setSendNum(int num);
    }

    /**
     * presenter 层接口
     */
    public interface MessageMarketingPtr extends IBasePresenter {
        void getRecordInfo(RecyclerView rv2);//

        void getModleInfo(RecyclerView rv3);//

        void showPopUp(View view);

        void sendSms();
    }

    /**
     * model 层接口
     */
    public interface MessageMarketingMdl {

        //获取短信模板列表
        void getSmsTemplates(RxSubscribe<List<SmsTemplates>> rxSubscribe);

        //获取短信推送记录列表
        void getSmsMarketLogs(RxSubscribe<List<MessageRecordEntity>> rxSubscribe);

        //发送短信
        void onSendSms(RxSubscribe<NullDataEntity> rxSubscribe, SmsTemplates smsTemplates);
    }

}
