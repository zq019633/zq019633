package com.enet.cn.myapplication.Url;

/**
 * Created by programmer on 2016/9/6 0006.
 */
public interface Url {
    /**
     *

     String url = "http://dmxiao.i.enet.com.cn/provider/Public/Cms/index.php?service=Applist.appDetail&appid=29";
     */

    /**
     * http://dmxiao.i.enet.com.cn/provider/Public/Cms/index.php?service=Applist.index
     首页
     http://dmxiao.i.enet.com.cn/provider/Public/Cms/index.php?service=Applist.listDetail&listid=86
     榜单
     http://dmxiao.i.enet.com.cn/provider/Public/Cms/index.php?service=Applist.appDetail&appid=29
     app内容
     http://dmxiao.i.enet.com.cn/provider/Public/Cms/checkApiParams.php?service=Applist.appDetail&appid=29
     接口参数查询页
     *
     *
     *
     */




    //http://api.enet.com.cn/provider/Public/Cms/index.php?service=Applist.index
   // http://dmxiao.i.enet.com.cn/provider/Public/Cms/index.php?service=Applist.index

    /**
     * 主机
     */
    String HOST = "http://dmxiao.i.enet.com.cn/provider/Public/Cms/index.php?";
   // String HOST = "http://api.enet.com.cn/provider/Public/Cms/index.php?";

    /**
     * 首页
     */
    String HOME = HOST + "service=Applist.index";
    /**
     * 榜单
     */
    String TOP = HOST + "service=Applist.listDetail&listid=86";
    /**
     * app内容
     */
    String CONTENT = HOST + "service=Applist.appDetail&appid=29";
    String TEST="http://api.enet.com.cn/provider/Public/Cms/index.php?service=Applist.listDetail&listid=86";
    String HomePager="http://api.enet.com.cn/provider/Public/Cms/index.php?service=Applist.index";

}