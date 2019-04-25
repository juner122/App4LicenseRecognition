package com.eb.geaiche.view;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.eb.geaiche.R;

public class MyRadioButton extends androidx.appcompat.widget.AppCompatRadioButton implements  CompoundButton.OnCheckedChangeListener {

    Context context;

    public MyRadioButton(Context context, String name, int i) {
        super(context);
        this.context = context;
        init(name, i);
    }


    private void init(String name, int i) {


        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 1, 0, 0);
        setPadding(0, 36, 0, 36);
        setText(name);
        setTextColor(Color.parseColor("#333333"));
        setBackground(context.getResources().getDrawable(R.drawable.radiobutton_background_a));
        setButtonDrawable(android.R.color.transparent);//隐藏单选圆形按钮
        setGravity(Gravity.CENTER);
        setLayoutParams(layoutParams);
        setTag(i);

        setOnCheckedChangeListener(this);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (b)
            compoundButton.setTextColor(Color.parseColor("#ffffff"));
        else
            compoundButton.setTextColor(Color.parseColor("#333333"));
    }


}
