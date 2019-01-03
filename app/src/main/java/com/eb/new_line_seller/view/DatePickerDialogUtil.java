package com.eb.new_line_seller.view;

import android.app.DatePickerDialog;
import android.content.Context;

import java.util.Calendar;

public class DatePickerDialogUtil {

    int mYear;
    int mMonth;
    int mDay;

    DatePickerDialog pickerDialog;
    DatePickerDialog.OnDateSetListener dateSetListener;
    Context mContext;

    public DatePickerDialogUtil(Context context, DatePickerDialog.OnDateSetListener listener) {
        mContext = context;
        dateSetListener = listener;

        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }
    public void show(int y,int m,int d) {

        pickerDialog = new DatePickerDialog(mContext, DatePickerDialog.THEME_DEVICE_DEFAULT_DARK,
                dateSetListener,
                y, m, d);
        pickerDialog.show();
    }


}
