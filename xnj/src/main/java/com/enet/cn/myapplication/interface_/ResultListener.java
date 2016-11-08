package com.enet.cn.myapplication.interface_;

/**
 * 请求网络返回数据的监听器接口
 *
 * @param <T> JAVABEAN类型
 */
public interface ResultListener<T> {

    public void onResponse(T bean);

    public void onFaild();
}