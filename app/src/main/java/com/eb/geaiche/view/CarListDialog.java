package com.eb.geaiche.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.UserlistListAdapter;
import com.juner.mvp.bean.UserEntity;

import java.util.List;


public class CarListDialog extends Dialog {

    private Context context;

    private ClickListenerInterface clickListenerInterface;


    UserlistListAdapter userlistListAdapter;

    public interface ClickListenerInterface {

        public void doSelectUser(UserEntity user);

        public void doAddUser();
    }

    public CarListDialog(Context context, List<UserEntity> users) {
        super(context, R.style.my_dialog);
        this.context = context;

        userlistListAdapter = new UserlistListAdapter(users, context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_carlist, null);
        setContentView(view);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new clickListener());

        RecyclerView rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically() {
                //解决ScrollView里存在多个RecyclerView时滑动卡顿的问题
                return false;
            }
        });
        rv.setAdapter(userlistListAdapter);


        userlistListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                clickListenerInterface.doSelectUser(((UserEntity) adapter.getData().get(position)));
            }
        });


        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.tv_cancel:
                    clickListenerInterface.doAddUser();
                    break;
            }
        }

    }
}