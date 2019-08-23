package com.eb.geaiche.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.bean.Technician;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

//员工绩效分配
public class MeeitsAdapter extends BaseQuickAdapter<Technician, BaseViewHolder> {

    Context context;
    boolean isShow;//true查看，分配
    Handler mHandler;

    public MeeitsAdapter(@Nullable List<Technician> data, Context c, boolean isShow, Handler mHandler) {
        super(R.layout.activity_meeits_list_item, data);
        this.context = c;
        this.isShow = isShow;
        this.mHandler = mHandler;
    }

    @Override
    protected void convert(BaseViewHolder helper, Technician item) {
        helper.addOnClickListener(R.id.tv_cal);

        helper.setText(R.id.tv_name, item.getUserName());

    }

    /**
     * @param percentage    百分比
     * @param deductionBase 基数
     */
    private String calculation(String percentage, String deductionBase) {
        String deduction;//提成额
        if (null == percentage)
            percentage = "0.0";


        BigDecimal db = new BigDecimal(deductionBase);//基数
        BigDecimal per = new BigDecimal(percentage);//百分比
        deduction = (db.multiply(per)).divide(new BigDecimal(100), 2, RoundingMode.UP).toString();


        return deduction;
    }

    //设置绩效数据
    private void setInfo(Technician item, String ded, String percentage, String deductionBase) {
        item.setDeduction(ded);
        item.setPercentage(percentage);
        item.setDeductionBase(deductionBase);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Technician item = getData().get(position);


        EditText priceBase = holder.getView(R.id.tv_price);
        EditText meeits = holder.getView(R.id.tv_meeits);//比例
        EditText tv_meeits_v = holder.getView(R.id.tv_meeits_v);//提成额
        TextView calculation = holder.getView(R.id.tv_cal);//计算按钮

        String percentage;//百分比
        String deduction;//提成额
        String deductionBase;//提成基数


        deductionBase = null == item.getDeductionBase() ? "0" : item.getDeductionBase();
        if (null != item.getPercentage())
            percentage = item.getPercentage();
        else
            percentage = "0.0";

        if (!isShow) {
            deduction = calculation(percentage, deductionBase);
        } else
            deduction = item.getDeduction();


        tv_meeits_v.setText(deduction);
        meeits.setText(percentage);
        priceBase.setText(deductionBase);


        if (!isShow) {

            priceBase.addTextChangedListener(new TextWatcher() {//调整基数
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    try {
                        calculation.setVisibility(View.VISIBLE);
                        tv_meeits_v.setVisibility(View.GONE);

                        getData().get(position).setDeductionBase(s.toString());


                    } catch (Exception e) {
                        ToastUtils.showToast("请输入正确的数值！");

                    }
                }
            });

            meeits.addTextChangedListener(new TextWatcher() {//调整比例
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {

                    try {

                        calculation.setVisibility(View.VISIBLE);
                        tv_meeits_v.setVisibility(View.GONE);
                        getData().get(position).setPercentage(s.toString());


                    } catch (Exception e) {
                        ToastUtils.showToast("请输入正确的数值！");

                    }
                }
            });

        } else {
            priceBase.setFocusable(false);
            priceBase.setFocusableInTouchMode(false);
            meeits.setFocusableInTouchMode(false);
            meeits.setFocusableInTouchMode(false);
            tv_meeits_v.setFocusableInTouchMode(false);
            tv_meeits_v.setFocusableInTouchMode(false);
        }

        calculation.setOnClickListener(v -> {
            calculation.setVisibility(View.GONE);
            tv_meeits_v.setVisibility(View.VISIBLE);
            String d;
            d = calculation(getData().get(position).getPercentage(), getData().get(position).getDeductionBase());

            tv_meeits_v.setText(d);
            getData().get(position).setDeduction(d);
            mHandler.sendMessage(Message.obtain());
        });

        getData().get(position).setDeduction(deduction);
        getData().get(position).setPercentage(percentage);
    }
}
