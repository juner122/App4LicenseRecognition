package com.eb.geaiche.activity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.juner.mvp.Configure;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.GridImageAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.juner.mvp.bean.CarInfoRequestParameters;
import com.juner.mvp.bean.UpDataPicEntity;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.FullyGridLayoutManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

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
    TextView et_remarks;

    @BindView(R.id.recycler1)
    RecyclerView recyclerView1;
    @BindView(R.id.recycler2)
    RecyclerView recyclerView2;
    @BindView(R.id.recycler3)
    RecyclerView recyclerView3;

    PictureSelector pictureSelector;
    PictureSelector pictureSelector2;
    PictureSelector pictureSelector3;

    private GridImageAdapter adapter;
    private GridImageAdapter adapter2;
    private GridImageAdapter adapter3;

    private final static int requestCode1 = 1;
    private final static int requestCode2 = 2;
    private final static int requestCode3 = 3;

    @Override
    protected void init() {
        recyclerView1.setLayoutManager(new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        recyclerView2.setLayoutManager(new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        recyclerView3.setLayoutManager(new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        pictureSelector = PictureSelector.create(CarInfoActivity.this);
        pictureSelector2 = PictureSelector.create(CarInfoActivity.this);
        pictureSelector3 = PictureSelector.create(CarInfoActivity.this);

        adapter = new GridImageAdapter(CarInfoActivity.this, null, requestCode1, pictureSelector, null, false);
        adapter2 = new GridImageAdapter(CarInfoActivity.this, null, requestCode2, pictureSelector, null, false);
        adapter3 = new GridImageAdapter(CarInfoActivity.this, null, requestCode3, pictureSelector, null, false);

        recyclerView1.setAdapter(adapter);
        recyclerView2.setAdapter(adapter2);
        recyclerView3.setAdapter(adapter3);


        Api().showCarInfo(getIntent().getIntExtra(Configure.CARID, 0)).subscribe(new RxSubscribe<CarInfoRequestParameters>(this, true) {
            @Override
            protected void _onNext(CarInfoRequestParameters o) {
                tv_car_model.setText(o.getBrand() + "\t" + o.getName());
                tv_car_no.setText(o.getCarNo());
                et_remarks.setText(o.getPostscript());
                final List<LocalMedia> localMedia1 = new ArrayList<>();
                final List<LocalMedia> localMedia2 = new ArrayList<>();
                final List<LocalMedia> localMedia3 = new ArrayList<>();


                for (UpDataPicEntity u : o.getImagesList()) {

                    LocalMedia localMedia = new LocalMedia();
                    localMedia.setPath(u.getImageUrl());
                    localMedia.setId(u.getId());
                    if (u.getType() == 1) {
                        localMedia1.add(localMedia);
                    } else if (u.getType() == 2) {
                        localMedia2.add(localMedia);
                    } else {
                        localMedia3.add(localMedia);
                    }
                }

                if (localMedia1.size() != 0) {
                    adapter.setList(localMedia1);
                    adapter.setSelectMax(localMedia1.size());
                    adapter.setOnItemClickListener((position, v) -> {
                        if (localMedia1.size() > 0) {
                            // 预览图片 可自定长按保存路径
                            pictureSelector.themeStyle(R.style.picture_default_style).openExternalPreview(position, localMedia1);

                        }
                    });
                } else {
                    recyclerView1.setVisibility(View.GONE);
                }
                if (localMedia2.size() != 0) {

                    adapter2.setList(localMedia2);
                    adapter2.setSelectMax(localMedia2.size());
                    adapter2.setOnItemClickListener((position, v) -> {
                        if (localMedia2.size() > 0) {
                            // 预览图片 可自定长按保存路径
                            pictureSelector.themeStyle(R.style.picture_default_style).openExternalPreview(position, localMedia2);

                        }
                    });
                } else {
                    recyclerView2.setVisibility(View.GONE);

                }

                if (localMedia3.size() != 0) {

                    adapter3.setList(localMedia3);
                    adapter3.setSelectMax(localMedia3.size());
                    adapter3.setOnItemClickListener((position, v) -> {
                        if (localMedia3.size() > 0) {
                            // 预览图片 可自定长按保存路径
                            pictureSelector.themeStyle(R.style.picture_default_style).openExternalPreview(position, localMedia3);

                        }
                    });
                } else {

                    recyclerView3.setVisibility(View.GONE);
                }


            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);
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
