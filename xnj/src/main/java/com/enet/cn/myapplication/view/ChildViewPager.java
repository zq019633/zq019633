package com.enet.cn.myapplication.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ChildViewPager extends ViewPager {

    private int downX;
    private int downY;
    private int distanceX;
    private int moveX;
    private int moveY;
    private int distanceY;

    public ChildViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true); // 让时间传递下来

                break;

            case MotionEvent.ACTION_MOVE:
                moveX = (int) ev.getX();
                moveY = (int) ev.getY();
                distanceX = (int) (moveX - downX);
                distanceY = (int) (moveY - downY);
                if (Math.abs(distanceX) > Math.abs(distanceY)) {//如果往左滑动的距离 大于上下滑动的距离  执行下面的操作
                    // 证明是左右滑动
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {//其他情况  (上下滑动 )
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
