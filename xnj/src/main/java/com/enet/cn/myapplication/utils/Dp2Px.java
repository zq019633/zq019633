package com.enet.cn.myapplication.utils;

import com.enet.cn.myapplication.MyApplication;

/**
 * Created by programmer on 2016/9/7 0007.
 */
public class Dp2Px {
    // dp转换成px
    public static int dp2px(int dp) {
        // px = dp * 密度比 0.75， 1 ， 1.5 ， 2
        // 获取手机的密度比
        float density = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (density * dp + 0.5);// 手动四舍五入
    }



}
