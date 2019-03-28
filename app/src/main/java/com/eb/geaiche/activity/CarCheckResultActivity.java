package com.eb.geaiche.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.CarCheckItemAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.eb.geaiche.view.TabEntity;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.CarCheckResul;
import com.juner.mvp.bean.CheckOptions;
import com.juner.mvp.bean.NullDataEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


//同时处理新增和修改检测报告
public class CarCheckResultActivity extends BaseActivity {

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_car_check_result;
    }

    @BindView(R.id.rv)
    RecyclerView rv;

    @BindView(R.id.tv_dec)
    EditText tv_dec;

    @BindView(R.id.ll_bottom)
    View ll_bottom;

    @BindView(R.id.but_2)
    View but_2;

    @BindView(R.id.but_3)
    View but_3;

    @OnClick({R.id.but_1, R.id.but_2, R.id.but_3})
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.but_1://生成
                if (id == 0) {
                    checkOutResult(1);
                } else {
                    updateCheckResult(1);
                }


                break;
            case R.id.but_2://暂存
                checkOutResult(0);
                break;
            case R.id.but_3://修改
                updateCheckResult(0);
                break;
        }
    }


    @BindView(R.id.tl_button_bar)
    CommonTabLayout commonTabLayout;
    private String[] mTitles = {"发动机", "刹车系统", "底盘系统", "转向系统", "供电系统", "车身附件", "灯光照明"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();


    CarCheckItemAdapter carCheckItemAdapter;

    String car_no;


    boolean isFix;//是否能修改
    int id;//检测报告id;
    List<CheckOptions> list = new ArrayList<>();

    @Override
    protected void init() {

        for (int i = 0; i < mTitles.length; i++) {

            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        commonTabLayout.setTabData(mTabEntities);
        commonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                carCheckItemAdapter.setNewData(getList(position + 1));

            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        car_no = getIntent().getStringExtra(Configure.car_no);

        tv_title.setText(car_no);
        isFix = getIntent().getBooleanExtra("isfix", false);
        if (isFix) {
            ll_bottom.setVisibility(View.VISIBLE);
        } else {

            ll_bottom.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setUpView() {

        rv.setLayoutManager(new LinearLayoutManager(this));
        carCheckItemAdapter = new CarCheckItemAdapter(null, isFix);
        rv.setAdapter(carCheckItemAdapter);
        if (isFix) {
            carCheckItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                    int id = ((CheckOptions) adapter.getData().get(position)).getId();
                    int selected = ((CheckOptions) adapter.getData().get(position)).getSelected();
                    if (selected == 1) {
                        selected = 0;
                    } else {
                        selected = 1;
                    }

                    for (int i = 0; i < list.size(); i++) {
                        if (id == list.get(i).getId()) {
                            list.get(i).setSelected(selected);
                            break;
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected void setUpData() {
        id = getIntent().getIntExtra("id", 0);

        if (id == 0) {
            //隐藏修改
            but_2.setVisibility(View.VISIBLE);
            but_3.setVisibility(View.GONE);

            //获取新的检修项列表
            Api().queryCheckOptions().subscribe(new RxSubscribe<List<CheckOptions>>(this, true) {
                @Override
                protected void _onNext(List<CheckOptions> checkOptions) {
                    list = checkOptions;
                    carCheckItemAdapter.setNewData(getList(1));
                }

                @Override
                protected void _onError(String message) {
                    ToastUtils.showToast(message);
                }
            });
        } else {

            //显示修改
            but_2.setVisibility(View.GONE);
            but_3.setVisibility(View.VISIBLE);

            //获取旧的检修项去修改
            Api().checkResultDetail(id).subscribe(new RxSubscribe<CarCheckResul>(this, true) {
                @Override
                protected void _onNext(CarCheckResul carCheckResul) {
                    list = carCheckResul.getOptionsList();
                    tv_dec.setText(null == carCheckResul.getPostscript() || carCheckResul.getPostscript().equals("") ? "暂无结论" : carCheckResul.getPostscript());


                    carCheckItemAdapter.setNewData(getList(1));


                    if (carCheckResul.getType() == 1) {//已完成的报告，不能编辑
                        tv_dec.setFocusable(false);

                    }
                }

                @Override
                protected void _onError(String message) {

                    ToastUtils.showToast(message);
                }
            });

        }
    }


    //暂存或者生成检测报告
    private void checkOutResult(int type) {


        Api().checkOutResult(getCarCheckResul(type)).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity entity) {

                ToastUtils.showToast("操作成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);

            }
        });

    }

    //修改暂存的门店检测报告 type为1的不可访问此接口
    private void updateCheckResult(int type) {


        Api().updateCheckResult(getCarCheckResul(type)).subscribe(new RxSubscribe<NullDataEntity>(this, true) {
            @Override
            protected void _onNext(NullDataEntity entity) {

                ToastUtils.showToast("操作成功！");
                finish();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(message);
            }
        });

    }


    /**
     * @param type 检验报告type
     */
    private CarCheckResul getCarCheckResul(int type) {
        CarCheckResul checkResul = new CarCheckResul();
        checkResul.setType(type);
        checkResul.setId(id);
        checkResul.setPostscript(tv_dec.getText().toString());
        checkResul.setCarId(getIntent().getIntExtra(Configure.car_id, -1));
        checkResul.setCarNo(car_no);
        checkResul.setOptionsList(list);
        return checkResul;

    }

    /**
     * @param type 检修选项type
     * @return 根据type返回的检修选项集合
     */
    private List<CheckOptions> getList(int type) {

        List<CheckOptions> optionsList = new ArrayList<>();


        for (CheckOptions co : list) {
            if (co.getType() == type) {
                optionsList.add(co);
            }
        }
        return optionsList;

    }
}
