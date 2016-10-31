package com.enet.cn.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by programmer on 2016/9/5 0005.
 */
public class Myadapter extends FragmentPagerAdapter {

    private ArrayList pagerList;

    public Myadapter(FragmentManager fragmentManager, ArrayList list) {
        super(fragmentManager);

        this.pagerList=list;
    }



    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return (Fragment) pagerList.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return pagerList.size();
    }
}
