package com.enet.cn.myapplication.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.enet.cn.myapplication.Activity.NewActivity;
import com.enet.cn.myapplication.R;
import com.enet.cn.myapplication.utils.NotifyUtil;

/**
 * 作者：yaochangliang on 2016/5/28 19:11
 * 邮箱：yaochangliang159@sina.com
 */
public class NotifitionService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private String name;
    private String image;
    private int requestCode = (int) SystemClock.uptimeMillis();
    private NotificationCompat.Builder cBuilder;
    private NotificationManager nm;
    private Notification notification;
    private NotifyUtil currentNotify;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();





    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        name = intent.getStringExtra("name");
        image = intent.getStringExtra("image");
        Log.e("test", "执行onStartCommand");
        Log.e("我是服务里的", image);


        Intent intent_noti = new Intent(getApplicationContext(),NewActivity.class);
        PendingIntent rightPendIntent = PendingIntent.getActivity(this,
                requestCode, intent_noti, PendingIntent.FLAG_UPDATE_CURRENT);
        int smallIcon = R.mipmap.icon;
        String ticker = "正在更新"+name;
        //实例化工具类，并且调用接口
        NotifyUtil notify7 = new NotifyUtil(this, 7);


        notify7.notify_progress(rightPendIntent, smallIcon, ticker, name+"升级程序", "正在下载中");
        currentNotify = notify7;
        return super.onStartCommand(intent, flags, startId);

    }

}
