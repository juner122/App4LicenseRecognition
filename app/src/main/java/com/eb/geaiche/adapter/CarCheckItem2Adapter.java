package com.eb.geaiche.adapter;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.eb.geaiche.R;
import com.eb.geaiche.util.CarCheckTypeUtil;
import com.juner.mvp.bean.CheckOptions;

import java.util.List;


//检查细节修改项
public class CarCheckItem2Adapter extends BaseQuickAdapter<CheckOptions, BaseViewHolder> {


    public CarCheckItem2Adapter(@Nullable List<CheckOptions> data) {
        super(R.layout.activity_car_check_result_item2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckOptions item) {


        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_info, item.getDescribe());

        helper.setText(R.id.tv_type, String.format("(%s)", CarCheckTypeUtil.getType(item.getType())));


    }


}
