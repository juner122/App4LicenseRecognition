package com.eb.geaiche.activity;

import android.content.Intent;

public abstract class ResultBack {

    public abstract void resultOk(Intent data);

    public void resultElse(int resultCode, Intent data) {
        //可以吧，如果要处理其他情况只需重写此方法。大部分时候我们是不需要的，故写成空方法。
    }

}
