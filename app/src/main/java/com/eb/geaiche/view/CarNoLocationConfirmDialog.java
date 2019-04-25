package com.eb.geaiche.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.CarNoLocationAdpter;
import com.juner.mvp.bean.CarNoLocation;
import com.eb.geaiche.util.Locations;

import java.util.List;


public class CarNoLocationConfirmDialog extends Dialog {

    private Context context;
    private ClickListenerInterface clickListenerInterface;
    List<CarNoLocation> locations;

    String pick_location;

    public interface ClickListenerInterface {

        public void doConfirm(String s);

        public void doCancel();
    }

    public CarNoLocationConfirmDialog(Context context) {
        super(context, R.style.my_dialog);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        locations = Locations.getLocations();

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_pick_car_no_location, null);
        setContentView(view);


        RecyclerView recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 5));

        CarNoLocationAdpter c = new CarNoLocationAdpter(locations);
        recyclerView.setAdapter(c);

        c.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


                pick_location = locations.get(position).getLocation();

                clickListenerInterface.doConfirm(pick_location);
            }
        });


        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);


        tv_confirm.setOnClickListener(new clickListener());
        tv_cancel.setOnClickListener(new clickListener());

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
                case R.id.tv_confirm:
                    clickListenerInterface.doConfirm(pick_location);
                    break;
                case R.id.tv_cancel:
                    clickListenerInterface.doCancel();
                    break;
            }
        }

    }
}