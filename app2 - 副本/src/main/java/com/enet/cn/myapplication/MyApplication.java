package com.enet.cn.myapplication;


import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.aspsine.multithreaddownload.DownloadConfiguration;
import com.aspsine.multithreaddownload.DownloadManager;
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

        mContext = getApplicationContext();

        queue = Volley.newRequestQueue(getApplicationContext());


    initImageLoader(getContext());
        initDownloader();

    }
    //初始化下载相关
    private void initDownloader() {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(10);
        configuration.setThreadNum(3);
        DownloadManager.getInstance().init(getApplicationContext(), configuration);
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
