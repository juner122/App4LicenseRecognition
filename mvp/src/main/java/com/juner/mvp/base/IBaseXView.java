package com.juner.mvp.base;

import android.app.Activity;


/**
 * View层的接口基类
 *
 * 这是Activity和Fragment需要实现的基类接口，里面只是实现了一个获取Activity的方法，
 * 主要用于在Presenter中需要使用Context对象时调用，
 * 不直接在Presenter中创建Context对象，最大程度的防止内存泄漏
 */
public interface IBaseXView {

    /**
     * 获取 Activity 对象
     *
     * @return activity
     */
    <T extends Activity> T getSelfActivity();
}
