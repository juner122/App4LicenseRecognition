package com.eb.geaiche.adapter;


import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class WecomePagerAdapter extends PagerAdapter {
    private List<View> views;

    public WecomePagerAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // return super.instantiateItem(container, position);
        View view = views.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView(views.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return super.getPageTitle(position);
        return "标题" + position;
    }
}
