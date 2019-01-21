package com.eb.new_line_seller.adapter;

import android.support.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.eb.new_line_seller.R;
import com.eb.new_line_seller.bean.MealEntity;
import com.eb.new_line_seller.bean.MealL0Entity;
import com.eb.new_line_seller.bean.MyMultipleItem;
import com.eb.new_line_seller.util.ToastUtils;

import java.util.List;


//纸卡录入 选择套卡
public class MealPickListAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {


    public MealPickListAdapter(@Nullable List<MultiItemEntity> data) {
        super(data);
        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.activity_pick_meal_list_item);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.activity_pick_meal_list_item_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case MyMultipleItem.FIRST_TYPE:
                final MealL0Entity m = (MealL0Entity) item;

                helper.setText(R.id.tv_name, m.getActivityName());


                final ImageView iv = helper.getView(R.id.iv_pick);
                helper.addOnClickListener(R.id.ll_item);
                pick(m.isExpanded(), iv);

                break;
            case MyMultipleItem.SECOND_TYPE:
                final MealEntity me = (MealEntity) item;
                helper.setText(R.id.tv_name, me.getName()).setText(R.id.tv_number, String.valueOf(me.getNumber()));
                helper.addOnClickListener(R.id.ib_reduce);
                helper.addOnClickListener(R.id.ib_plus);

                final EditText et = helper.getView(R.id.tv_number);

                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editable.toString().equals("")) return;


                        int now = Integer.parseInt(editable.toString());
                        int max = me.getMaxNum();


                        if (max < now) {
                            ToastUtils.showToast("不能超过最大数量！");
                            now = max;
                            et.setText("" + now);
                        } else if (now < 0) {
                            ToastUtils.showToast("不能少于0！");
                            now = 0;
                            et.setText("" + now);
                        }
                        ((MealEntity) getData().get(helper.getAdapterPosition())).setNumber(now);


                    }
                });

                break;

        }
    }


    private void pick(boolean isPick, ImageView iv) {
        if (isPick)
            iv.setImageResource(R.drawable.icon_pick2);
        else
            iv.setImageResource(R.drawable.icon_unpick2);


    }


}