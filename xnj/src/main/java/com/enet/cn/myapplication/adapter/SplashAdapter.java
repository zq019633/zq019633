package com.enet.cn.myapplication.adapter;


import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by programmer on 2016/9/7 0007.
 */
public class SplashAdapter  extends PagerAdapter {


    private final ArrayList<ImageView> imgs;

    public SplashAdapter(FragmentManager supportFragmentManager, ArrayList<ImageView> imgs) {
        this.imgs=imgs;
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 创建当前要展示的条目
        ImageView imageView = imgs.get(position);
        // 把要展示的控件添加到ViewPager上
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

