package com.frank.plate.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.frank.plate.Configure;
import com.frank.plate.R;
import com.frank.plate.adapter.CarInfoPicAdpter;
import com.frank.plate.api.RxSubscribe;
import com.frank.plate.bean.CarInfoRequestParameters;
import com.frank.plate.bean.UpDataPicEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

//订单中的车辆信息，只负责显示数据
public class CarInfoActivity extends BaseActivity {

    @BindView(R.id.tv_car_no)
    TextView tv_car_no;
    @BindView(R.id.tv_car_model)
    TextView tv_car_model;
    @BindView(R.id.et_remarks)
    EditText et_remarks;

    @BindView(R.id.recycler1)
    RecyclerView recyclerView1;
    @BindView(R.id.recycler2)
    RecyclerView recyclerView2;
    @BindView(R.id.recycler3)
    RecyclerView recyclerView3;


    @Override
    protected void init() {
        recyclerView1.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        recyclerView3.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));


        Api().showCarInfo(getIntent().getIntExtra(Configure.CARID, 0)).subscribe(new RxSubscribe<CarInfoRequestParameters>(this, true) {
            @Override
            protected void _onNext(CarInfoRequestParameters o) {
                tv_car_model.setText(o.getBrand() + "\t" + o.getName());
                tv_car_no.setText(o.getCarNo());
                List<UpDataPicEntity> list = new ArrayList<>();
                List<UpDataPicEntity> list2 = new ArrayList<>();
                List<UpDataPicEntity> list3 = new ArrayList<>();

                for (UpDataPicEntity u : o.getImagesList()) {
                    if (u.getType()==1) {
                        list.add(u);
                    } else if (u.getType()==2) {
                        list2.add(u);
                    } else {
                        list3.add(u);
                    }

                }
                recyclerView1.setAdapter(new CarInfoPicAdpter(list, CarInfoActivity.this));
                recyclerView2.setAdapter(new CarInfoPicAdpter(list2, CarInfoActivity.this));
                recyclerView3.setAdapter(new CarInfoPicAdpter(list3, CarInfoActivity.this));

            }

            @Override
            protected void _onError(String message) {
                Toast.makeText(CarInfoActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void setUpView() {
        tv_title.setText("车况详情");
    }

    @Override
    protected void setUpData() {

    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_car_info;
    }
}
