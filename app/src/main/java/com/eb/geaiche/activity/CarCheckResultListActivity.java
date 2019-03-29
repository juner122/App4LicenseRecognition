package com.eb.geaiche.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eb.geaiche.R;
import com.eb.geaiche.adapter.CarCheckAdapter;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.CarCheckResul;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class CarCheckResultListActivity extends BaseActivity {
    @BindView(R.id.tv_add)
    View add;

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_car_check_result_list;
    }

    @Override
    protected void init() {
        tv_title.setText("车辆检查记录");

    }

    @BindView(R.id.rv)
    RecyclerView rv;

    CarCheckAdapter carCheckAdapter;

    @OnClick({R.id.tv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add:

                toInfoActivity(true);

                break;
        }
    }


    @Override
    protected void setUpView() {

        carCheckAdapter = new CarCheckAdapter(null);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(carCheckAdapter);
        carCheckAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int id = carCheckAdapter.getData().get(position).getId();
                int car_id = carCheckAdapter.getData().get(position).getCarId();
                String car_no = carCheckAdapter.getData().get(position).getCarNo();

                toInfoActivityforId(carCheckAdapter.getData().get(position).getType() != 1, id, car_id, car_no);

            }
        });


    }

    @Override
    protected void setUpData() {


    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新列表
        checkResultList();
    }

    private void checkResultList() {
        Api().checkResultList(getIntent().getIntExtra(Configure.car_id, -1)).subscribe(new RxSubscribe<List<CarCheckResul>>(this, true) {
            @Override
            protected void _onNext(List<CarCheckResul> carCheckResuls) {

                if (carCheckResuls.size() == 0) {
                    ToastUtils.showToast("暂无记录！");
                    return;
                }

                carCheckAdapter.setNewData(carCheckResuls);
            }

            @Override
            protected void _onError(String message) {

                ToastUtils.showToast(message);
            }
        });
    }


    /**
     * @param isfix 是否可修改
     */
    private void toInfoActivity(boolean isfix) {

        Intent intent = new Intent(this, CarCheckResultActivity.class);
        intent.putExtra(Configure.car_no, getIntent().getStringExtra(Configure.car_no));
        intent.putExtra(Configure.car_id, getIntent().getIntExtra(Configure.car_id, -1));
        intent.putExtra("isfix", isfix);
        intent.putExtra("id", 0);
        startActivity(intent);

    }

    /**
     * @param isfix 是否可修改
     */
    private void toInfoActivityforId(boolean isfix, int id, int carid, String carno) {

        Intent intent = new Intent(this, CarCheckResultActivity.class);
        intent.putExtra(Configure.car_no, carno);
        intent.putExtra(Configure.car_id, carid);
        intent.putExtra("isfix", isfix);
        intent.putExtra("id", id);
        startActivity(intent);

    }

}
