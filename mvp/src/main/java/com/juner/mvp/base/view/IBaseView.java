package com.juner.mvp.base.view;

import com.juner.mvp.base.IBaseXView;


/**
 * View的接口
 *
 * 补充一些会常用到的方法
 */
public interface IBaseView extends IBaseXView {

    /**
     * 显示提示
     * @param msg
     */
    void showToast(String msg);

}
