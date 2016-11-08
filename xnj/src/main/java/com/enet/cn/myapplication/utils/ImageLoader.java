package com.enet.cn.myapplication.utils;

/**
 * Created by programmer on 2016/9/8 0008.
 */
public class ImageLoader {
    private  static  ImageLoader imageloader=null;
    public ImageLoader(){}
    public static synchronized ImageLoader getInsatnce(){
        if(null == imageloader){
            imageloader = new ImageLoader();
        }
        return imageloader;
    }
}
