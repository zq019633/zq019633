package com.enet.cn.myapplication;

import android.app.Application;

import com.aspsine.multithreaddownload.DownloadConfiguration;
import com.aspsine.multithreaddownload.DownloadManager;

/**
 * Created by programmer on 2016/9/27 0027.
 */
public class Myapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initDownloader();
    }
    private void initDownloader() {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(10);
        configuration.setThreadNum(3);
        DownloadManager.getInstance().init(getApplicationContext(), configuration);
    }
}
