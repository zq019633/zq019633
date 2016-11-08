package com.enet.cn.myapplication;


import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.enet.cn.myapplication.view.CircularAnim;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.model.HttpHeaders;
import com.lzy.okhttputils.model.HttpParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.logging.Handler;

/**
 * Created by programmer on 2016/9/5 0005.
 */
public class MyApplication extends Application {


    private static Handler handler;
    /** 获取一个可以运行到UI线程的Handler */
    public static Handler getHandler() {
        return handler;
    }
    /**
     * 全局的上下文.
     */
    private static Context mContext;

    /**获取Context.
     * @return
     */
    public static Context getContext(){
        return mContext;
    }
    /**
     * 初始化volley
     */
    // 建立请求队列
    public static RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        CircularAnim.init(700, 500, R.color.t);
        mContext = getApplicationContext();

        queue = Volley.newRequestQueue(getApplicationContext());


    initImageLoader(getContext());

        initOkGo();

    }

    private void initOkGo() {

        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //所有的 header 都 不支持 中文
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //所有的 params 都 支持 中文
        params.put("commonParamsKey2", "这里支持中文参数");

        //必须调用初始化
        OkHttpUtils.init(this);
        //以下都不是必须的，根据需要自行选择
        OkHttpUtils.getInstance()//
                .debug("OkHttpUtils")                                              //是否打开调试
                .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                 //全局的写入超时时间
                //.setCookieStore(new MemoryCookieStore())                           //cookie使用内存缓存（app退出后，cookie消失）
                //.setCookieStore(new PersistentCookieStore())                       //cookie持久化存储，如果cookie不过期，则一直有效
                .addCommonHeaders(headers)                                         //设置全局公共头
                .addCommonParams(params);
    }



    //初始化图片
    private void initImageLoader(Context context) {
       DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.progressbar)		// 设置图片下载加载的时候显示的默认图
                .showImageForEmptyUri(R.drawable.progressbar)	// 设置图片网址为空的时候显示的默认图
                .showImageOnFail(R.drawable.progressbar)		// 设置图片下载失败的时候显示的默认图
                .cacheInMemory(true)		// 缓存到内存中
                .cacheOnDisk(true)			// 缓存到磁盘中
                .considerExifParams(true)	// 参考图片信息，比如说旋转信息
                //		.displayer(new RoundedBitmapDisplayer(20))	// 设置图片为圆角
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)	// 线程优先级
                .denyCacheImageMultipleSizesInMemory()		// 禁止在内存里面缓存图片的多种尺寸
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())	// 磁盘缓存文件名的生成器
                .tasksProcessingOrder(QueueProcessingType.LIFO)	// 任务处理顺序
                .writeDebugLogs() // Remove for release app	// 写调试信息，发布注释掉
                .defaultDisplayImageOptions(options)		// 显示图片的默认选项
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);



    }

    public static RequestQueue getHttpQueue() {
        return queue;
    }


}
