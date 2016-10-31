package com.enet.cn.myapplication.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.multithreaddownload.DownloadInfo;
import com.aspsine.multithreaddownload.DownloadManager;
import com.enet.cn.myapplication.MyApplication;
import com.enet.cn.myapplication.R;
import com.enet.cn.myapplication.Url.Url;
import com.enet.cn.myapplication.adapter.DetailAdapter;
import com.enet.cn.myapplication.bean.TopBean.Apps;
import com.enet.cn.myapplication.bean.TopBean.Top;
import com.enet.cn.myapplication.interface_.ResultListener;
import com.enet.cn.myapplication.listener.OnItemClickListener;
import com.enet.cn.myapplication.service.DowService;
import com.enet.cn.myapplication.utils.Utils;
import com.enet.cn.myapplication.utils.VolleyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by programmer on 2016/9/13 0013.
 */
public class DetailActivity extends Activity implements AbsListView.OnScrollListener,OnItemClickListener<Apps> {
    private DownloadReceiver mReceiver;
    private TextView tv_title;
    private String new_id;
    private ListView detail_lv;
    private String new_name;
    private String new_imagepath;
    private String new_catid;
    private View footer;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList list;
    private Top n;
    private List<Apps> apps;
    private File mDownloadDir;
    private DetailAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        Bundle extras = getIntent().getExtras();
        new_id = extras.getString("new_id");
        new_name = extras.getString("new_name");
        new_catid = extras.getString("new_catid");
        new_imagepath = extras.getString("new_imagepath");
        mDownloadDir = new File(Environment.getExternalStorageDirectory(), "Download");



        initView();
        initData();
        Log.e("222","我走了="+mAdapter);

    }
    /**
     * 找到控件
     */
    private void initView() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
        ImageButton imageButton = (ImageButton) findViewById(R.id.back);
        imageButton.setVisibility(View.VISIBLE);
        tv_title = (TextView) findViewById(R.id.title_bar);
        detail_lv = (ListView) findViewById(R.id.detail_lv);

        detail_lv.setOnScrollListener(this);

      swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(DetailActivity.this, "正在刷新", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       VolleyGet();
                        Toast.makeText(DetailActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public void back(View v) {
        finish();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tv_title.setText(new_name);
        VolleyGet();



    }

    //请求网络数据
    private void VolleyGet() {
        VolleyUtil.RequestDataInfo(this, null, Url.HOST + "service=Applist.listDetail&listid=" + new_id, Top.class, new ResultListener<Top>() {
            @Override
            public void onResponse(Top bean) {
                n = bean;
                apps = n.getData().getInfo().getApps();
                mAdapter = new DetailAdapter(apps);

                detail_lv.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(DetailActivity.this);
                detail_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(MyApplication.getContext(), AppDetailsActivity.class);
                        intent.putExtra("new_id", new_id);
                        intent.putExtra("position", position);
                        intent.putExtra("name", apps.get(position).getName());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFaild() {

            }
        });

    }

    @Override
    public void onItemClick(View v, int position, Apps info) {
        Log.e("我是位置",""+position);
            DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(info.getDownloadurl());//根据穿过来的url判断是否有下载信息
            if (downloadInfo != null) {
                info.setProgress(downloadInfo.getProgress());
                info.setDownloadPerSize(Utils.getDownloadPerSize(downloadInfo.getFinished(), downloadInfo.getLength()));
                info.setStatus(Apps.STATUS_PAUSED);
            }



            if (info.getStatus() == Apps.STATUS_DOWNLOADING || info.getStatus() == Apps.STATUS_CONNECTING) {
                pause(info.getDownloadurl());
            } else if (info.getStatus() == Apps.STATUS_COMPLETE) {
                install(info);
            } else if (info.getStatus() == Apps.STATUS_INSTALLED) {
                unInstall(info);
            } else {
                download(position, info.getDownloadurl(),info);
                Log.e("222","info.getDownLoad="+info.getDownloadurl());



        }
    }

    private void download(int position, String tag, Apps info) {
        DowService.intentDownload(MyApplication.getContext(), position, tag, info);
    }

    private void pause(String tag) {
        DowService.intentPause(MyApplication.getContext(), tag);
    }

    private void pauseAll() {
        DowService.intentPauseAll(MyApplication.getContext());
    }

    private void install(Apps info) {
        Utils.installApp(MyApplication.getContext(), new File(mDownloadDir, info.getName() + ".apk"));
    }

    private void unInstall(Apps info) {
        Utils.unInstallApp(MyApplication.getContext(), info.getPkgname());
    }


    @Override
    public void onResume() {
        super.onResume();
        register();
    }

    @Override
    public void onPause() {
        super.onPause();
        unRegister();
    }

    private void register() {
        mReceiver = new DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DowService.ACTION_DOWNLOAD_BROAD_CAST);
        LocalBroadcastManager.getInstance(MyApplication.getContext()).registerReceiver(mReceiver, intentFilter);
    }

    private void unRegister() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(MyApplication.getContext()).unregisterReceiver(mReceiver);
        }
    }

    class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action == null || !action.equals(DowService.ACTION_DOWNLOAD_BROAD_CAST)) {
                return;
            }
            final int position = intent.getIntExtra(DowService.EXTRA_POSITION, -1);
            final Apps tmpInfo = (Apps) intent.getSerializableExtra(DowService.EXTRA_APP_INFO);
            if (tmpInfo == null || position == -1) {
                return;
            }
            final Apps appInfo = apps.get(position);
            final int status = tmpInfo.getStatus();
            switch (status) {
                case Apps.STATUS_CONNECTING:
                    appInfo.setStatus(Apps.STATUS_CONNECTING);
                    if (isCurrentListViewItemVisible(position)) {
                        DetailAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());



                    }
                    break;

                case Apps.STATUS_DOWNLOADING:
                    appInfo.setStatus(Apps.STATUS_DOWNLOADING);
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    if (isCurrentListViewItemVisible(position)) {
                        DetailAdapter.ViewHolder holder = getViewHolder(position);
                       /* holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());*/
                        holder.progressBar.setProgress(appInfo.getProgress());
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                    }
                    break;
                case Apps.STATUS_COMPLETE:
                    appInfo.setStatus(Apps.STATUS_COMPLETE);
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    File apk = new File(mDownloadDir, appInfo.getName() + ".apk");
                    if (apk.isFile() && apk.exists()) {
                        String packageName = Utils.getApkFilePackage(MyApplication.getContext(), apk);
                        appInfo.setPkgname(packageName);
                        if (Utils.isAppInstalled(MyApplication.getContext(), packageName)) {
                            appInfo.setStatus(Apps.STATUS_INSTALLED);
                        }
                    }

                    if (isCurrentListViewItemVisible(position)) {
                        DetailAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                     /*   holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());*/
                        holder.progressBar.setProgress(appInfo.getProgress());
                    }
                    break;

                case Apps.STATUS_PAUSED:
                    appInfo.setStatus(Apps.STATUS_PAUSED);
                    if (isCurrentListViewItemVisible(position)) {
                        DetailAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                    }
                    break;
                case Apps.STATUS_NOT_DOWNLOAD:
                    appInfo.setStatus(Apps.STATUS_NOT_DOWNLOAD);
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    if (isCurrentListViewItemVisible(position)) {
                        DetailAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                        holder.progressBar.setProgress(appInfo.getProgress());
                     /*   holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());*/
                    }
                    break;
                case Apps.STATUS_DOWNLOAD_ERROR:
                    appInfo.setStatus(Apps.STATUS_DOWNLOAD_ERROR);
                    appInfo.setDownloadPerSize("");
                    if (isCurrentListViewItemVisible(position)) {
                        DetailAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                    //    holder.tvDownloadPerSize.setText("");
                        holder.btnDownload.setText(appInfo.getButtonText());
                    }
                    break;
            }
        }
    }

    private boolean isCurrentListViewItemVisible(int position) {
        int first =detail_lv.getFirstVisiblePosition();
        int last = detail_lv.getLastVisiblePosition();
        return first <= position && position <= last;
    }

    private DetailAdapter.ViewHolder getViewHolder(int position) {
        int childPosition = position - detail_lv.getFirstVisiblePosition();
        View view = detail_lv.getChildAt(childPosition);
        return (DetailAdapter.ViewHolder) view.getTag();
    }
}