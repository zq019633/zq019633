package com.enet.cn.myapplication.fragment.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enet.cn.myapplication.view.StateLayout;

/**
 * Created by programmer on 2016/9/5 0005.
 */
public abstract class BaseFragment extends Fragment {

    private View view;
    protected StateLayout stateLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (stateLayout == null) {
            // 说明Fragemnt的onCreateView方法是第一次执行
            stateLayout = StateLayout.newInstance(getActivity(), getContentView());
            initView();

            initData();
        } else {
            ViewGroup parent = (ViewGroup) stateLayout.getParent();
            if (parent != null) {
                parent.removeView(stateLayout);
            }
        }
        return stateLayout;
    }

    /**
     * 初始化布局文件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */

    protected abstract void initData();


    /**
     * 返回正常的界面，可以返回一个布局id，也可以返回一个View
     */
    public abstract Object getContentView();
}
