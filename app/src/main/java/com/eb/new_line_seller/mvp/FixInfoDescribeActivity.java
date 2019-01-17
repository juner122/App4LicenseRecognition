package com.eb.new_line_seller.mvp;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.mvp.contacts.FixInfoDesContacts;
import com.eb.new_line_seller.mvp.presenter.FixInfoDesPtr;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

//维修故障描述页面
public class FixInfoDescribeActivity extends BaseActivity<FixInfoDesContacts.FixInfoDesPtr> implements FixInfoDesContacts.FixInfoDesUI {

    @BindView(R.id.tv_car_no)
    TextView tv_car_no;

    @BindView(R.id.but_to_technician_list)
    TextView technician_list;//技师

    @BindView(R.id.but_set_date)
    TextView but_set_date;

    @BindView(R.id.et)
    EditText et;

    @BindViews({R.id.tv_re1, R.id.tv_re2, R.id.tv_re3, R.id.tv_re4, R.id.tv_re5, R.id.tv_re6})
    public List<TextView> textViews;

    @OnClick({R.id.but_to_technician_list, R.id.tv_fix_order, R.id.tv_enter_order})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.but_to_technician_list:
                getPresenter().toTechnicianListActivity();
                break;

            case R.id.tv_fix_order:
                getPresenter().quotationSave(true);//保存退出
                break;

            case R.id.tv_enter_order:
                getPresenter().quotationSave(false);//跳到订单页面
                break;


        }
    }

    @Override
    public int setLayoutResourceID() {
        return R.layout.activity_fix_info_describe;
    }

    @Override
    protected void init() {
        tv_title.setText("维修故障描述");
        getPresenter().getInfo();
        getPresenter().setTipClickListener(textViews);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                getPresenter().getDescribe(editable.toString());
            }
        });

    }


    @Override
    public FixInfoDesContacts.FixInfoDesPtr onBindPresenter() {
        return new FixInfoDesPtr(this);
    }

    @Override
    public void setCarNo(String carNo) {
        tv_car_no.setText(carNo);
    }

    @Override
    public void setTechnician(String technicians) {
        technician_list.setText(technicians);
    }

    @Override
    public void setDate(String date) {
        but_set_date.setText(date);
    }

    @Override
    public void setTip(String tip) {
        et.append(tip + ",");
    }

    @Override
    public void toFixInfoActivity(int id) {

        toActivity(FixInfoActivity.class, "id", id);
    }


}
