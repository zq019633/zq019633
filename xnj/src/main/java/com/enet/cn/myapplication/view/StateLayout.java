package com.enet.cn.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.enet.cn.myapplication.R;

/**
 * Created by programmer on 2016/9/8 0008.
 */
public class StateLayout extends FrameLayout {
    private View contentView;
    private View loadingView;

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StateLayout(Context context) {
        this(context,null);
    }


    /**
     * 创建一个StateLayout实现
     *
     * @param layoutIdOrView 可以传一个布局id，也可以传一个View
     */
    public static StateLayout newInstance(Context context, Object layoutIdOrView) {
        StateLayout stateLayout = (StateLayout) View.inflate(context, R.layout.state_layout, null);

        stateLayout.setContentView(layoutIdOrView);
        return stateLayout;
    }

    /**
     * 设置正常界面的View
     *
     * @param layoutIdOrView 可以传一个布局id，也可以传一个View
     */
    public void setContentView(Object layoutIdOrView) {
        if (layoutIdOrView instanceof Integer) {    // 如果是一个布局id
            int layoutId = (Integer) layoutIdOrView;
            contentView = View.inflate(getContext(), layoutId, null);
        } else {    // layoutIdOrView就是一个View
            contentView = (View) layoutIdOrView;
        }
        super.addView(contentView);    // 把正常界面的View设置到状态布局中
        contentView.setVisibility(View.GONE);    //  默认显示LoadingView
    }

    /**
     * 把xml中的View填充成Java对象的View
     */
    @Override
    protected void onFinishInflate() {
        loadingView = findViewById(R.id.loadingView);

        showLoadingView();
    }

    /**
     * 显示正在加载的View
     */
    public void showLoadingView() {
        showView(loadingView);
    }
    /** 显示正常界面的View */
    public void showContentView() {
        showView(contentView);
    }

    /**
     * 显示指定的View，并且隐藏其它的View
     *
     * @param view 指定要显示的View
     */
    private void showView(View view) {
        // getChildCount() 获取子孩子的数量
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);    // 获取子孩子
            child.setVisibility(view == child ? View.VISIBLE : View.GONE);
        }
    }

}
