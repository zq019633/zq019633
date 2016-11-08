package com.enet.cn.myapplication.fragment;


import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.enet.cn.myapplication.R;
import com.enet.cn.myapplication.fragment.Base.BaseFragment;

/**
 * Created by programmer on 2016/9/5 0005.
 */
public class RcommendPage extends BaseFragment {

    private View view;
    private ImageView iv_recommend;
    private ListView lv_recommend;


    /**
     * 初始化布局文件
     */
    @Override
    protected void initView() {
        iv_recommend = (ImageView) stateLayout.findViewById(R.id.iv_recommend);
        //  lv_recommend = (ListView) view.findViewById(R.id.lv_recommend);


    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {


    }



    /**
     * 返回正常的界面，可以返回一个布局id，也可以返回一个View
     */
    @Override
    public Object getContentView() {
        return R.layout.recommend;
    }
}