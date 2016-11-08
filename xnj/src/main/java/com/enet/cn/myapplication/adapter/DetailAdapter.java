package com.enet.cn.myapplication.adapter;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enet.cn.myapplication.Activity.DownloadManagerActivity;
import com.enet.cn.myapplication.MyApplication;
import com.enet.cn.myapplication.R;
import com.enet.cn.myapplication.bean.TopBean.Apps;
import com.enet.cn.myapplication.listener.OnItemClickListener;
import com.enet.cn.myapplication.utils.AppCacheUtils;
import com.lzy.okhttpserver.download.DownloadManager;
import com.lzy.okhttpserver.download.DownloadService;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.GetRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by programmer on 2016/9/13 0013.
 */
public class DetailAdapter extends BaseAdapter {

    private OnItemClickListener mListener;
    private List<Apps> apps;
    private DownloadManager downloadManager;


    public DetailAdapter(List<Apps> apps) {
        this.apps = apps;


    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apps.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_app, null);
            holder = new ViewHolder();
            holder.text_top = (TextView) convertView.findViewById(R.id.app_top);
            holder.imageView1 = (ImageView) convertView.findViewById(R.id.apps_iv1);
            holder.imageView2 = (ImageView) convertView.findViewById(R.id.apps_iv2);
            holder.textView = (TextView) convertView.findViewById(R.id.apps_tv);
            holder.download  = (Button) convertView.findViewById(R.id.download);


            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        final Apps appInfo = apps.get(position);

        final Button download = (Button) convertView.findViewById(R.id.download);
        downloadManager = DownloadService.getDownloadManager();
        if (downloadManager.getDownloadInfo(apps.get(position).getDownloadurl()) != null) {
            download.setText("已在队列");


            download.setEnabled(false);
        } else {
            download.setText("安装");
            download.setEnabled(true);
        }

        // Glide.with(getContext()).load(apk.getIconUrl()).error(R.mipmap.ic_launcher).into(icon);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager nm = (NotificationManager) MyApplication.getContext()
                        .getSystemService(Activity.NOTIFICATION_SERVICE);
                NotificationCompat.Builder cBuilder = new NotificationCompat.Builder(MyApplication.getContext());

                //设置想要展示的数据内容
                Intent intent_noti = new Intent(MyApplication.getContext(),DownloadManagerActivity.class);

                //文件的类型，从tomcat里面找
                // intent_noti.setDataAndType(Uri.fromFile(mFile), "application/vnd.android.package-archive");
                PendingIntent rightPendIntent = PendingIntent.getActivity(MyApplication.getContext(),
                        0, intent_noti, PendingIntent.FLAG_UPDATE_CURRENT);

                cBuilder.setContentIntent(rightPendIntent);// 该通知要启动的Intent
                cBuilder.setSmallIcon(R.mipmap.down);// 设置顶部状态栏的小图标
                cBuilder.setTicker("新的下载任务");// 在顶部状态栏中的提示信息

                cBuilder.setContentTitle("西南角下载管理");// 设置通知中心的标题
                cBuilder.setContentText("点击进入");// 设置通知中心中的内容
                cBuilder.setWhen(System.currentTimeMillis());

                Notification notification = cBuilder.build();
                // 发送该通知
                nm.notify(1, notification);





                if (downloadManager.getDownloadInfo(appInfo.getDownloadurl()) != null) {
                    Toast.makeText(MyApplication.getContext(), "任务已经在下载列表中", Toast.LENGTH_SHORT).show();
                } else {
                    GetRequest request = OkHttpUtils.get(apps.get(position).getDownloadurl())//
                            .headers("headerKey1", "headerValue1")//
                            .headers("headerKey2", "headerValue2")//
                            .params("paramKey1", "paramValue1")//
                            .params("paramKey2", "paramValue2");
                    downloadManager.addTask(appInfo.getDownloadurl(), request, null);
                    AppCacheUtils.getInstance(MyApplication.getContext()).put(apps.get(position).getDownloadurl(),appInfo);
                    download.setText("已在队列");
                    download.setEnabled(false);
                }
            }
        });


        if (position > 2) {
            holder.text_top.setVisibility(View.VISIBLE);
            holder.text_top.setTextSize(15);
            holder.text_top.setText(position+1 + "");
        } else {
            holder.text_top.setVisibility(View.INVISIBLE);
        }
        if (position == 0) {
            holder.imageView1.setVisibility(View.VISIBLE);
            holder.imageView1.setImageResource(R.mipmap.first);
        }else if(position==1){
            holder.imageView1.setVisibility(View.VISIBLE);
            holder.imageView1.setImageResource(R.mipmap.two);
        }else if(position==2){
            holder.imageView1.setVisibility(View.VISIBLE);
            holder.imageView1.setImageResource(R.mipmap.other);
        }

        else {
            holder.imageView1.setVisibility(View.INVISIBLE);
        }
       holder.textView.setText(appInfo.getName());
        ImageLoader.getInstance().displayImage(appInfo.getImagepath(), holder.imageView2);


        return convertView;
    }


    public final static class ViewHolder{
       public TextView text_top;
        public   ImageView imageView1;
        public   ImageView imageView2;
        public   TextView textView;
        public  Button download;
        public   Button btnDownload;



    }
}
