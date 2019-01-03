package com.eb.new_line_seller.view;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * EditText 限制输入整数和小数 的位数
 * 默认 整数位无限制，小数位 最多2位
 */
public class DecimalInputTextWatcher implements TextWatcher {

    private static final int DEFAULT_DECIMAL_DIGITS = 2;//默认  小数的位数   2 位

    private EditText editText;
    private int decimalDigits;// 小数的位数
    private int integerDigits;// 整数的位数

    public DecimalInputTextWatcher(EditText editText) {
        this.editText = editText;
        this.decimalDigits = DEFAULT_DECIMAL_DIGITS;
    }

    public DecimalInputTextWatcher(EditText editText, int decimalDigits) {
        this.editText = editText;
        if (decimalDigits <= 0)
            throw new RuntimeException("decimalDigits must > 0");
        this.decimalDigits = decimalDigits;
    }

    public DecimalInputTextWatcher(EditText editText, int integerDigits, int decimalDigits) {
        this.editText = editText;
        if (integerDigits <= 0)
            throw new RuntimeException("integerDigits must > 0");
        if (decimalDigits <= 0)
            throw new RuntimeException("decimalDigits must > 0");
        this.decimalDigits = decimalDigits;
        this.integerDigits = integerDigits;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String s = editable.toString();
        editText.removeTextChangedListener(this);

        if (s.contains(".")) {
            if (integerDigits > 0) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerDigits + decimalDigits + 1)});
            }
            if (s.length() - 1 - s.indexOf(".") > decimalDigits) {
                s = s.substring(0,
                        s.indexOf(".") + decimalDigits + 1);
                editable.replace(0, editable.length(), s.trim());//不输入超出位数的数字
            }
        } else {
            if (integerDigits > 0) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(integerDigits + 1)});
                if (s.length() > integerDigits) {
                    s = s.substring(0, integerDigits);
                    editable.replace(0, editable.length(), s.trim());
                }
            }

        }
        if (s.trim().equals(".")) {//小数点开头，小数点前补0
            s = "0" + s;
            editable.replace(0, editable.length(), s.trim());
        }
        if (s.startsWith("0") && s.trim().length() > 1) {//多个0开头，只输入一个0
            if (!s.substring(1, 2).equals(".")) {
                editable.replace(0, editable.length(), "0");
            }
        }
        editText.addTextChangedListener(this);
    }
}
