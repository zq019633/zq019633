package com.enet.cn.myapplication.Activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.enet.cn.myapplication.MyApplication;
import com.enet.cn.myapplication.R;
import com.enet.cn.myapplication.Url.Url;
import com.enet.cn.myapplication.bean.TopBean.Apps;
import com.enet.cn.myapplication.bean.TopBean.Top;
import com.enet.cn.myapplication.interface_.ResultListener;
import com.enet.cn.myapplication.utils.ApkUtils;
import com.enet.cn.myapplication.utils.VolleyUtil;
import com.enet.cn.myapplication.utils.checkPermissions;
import com.enet.cn.myapplication.view.StateLayout;
import com.github.lzyzsd.randomcolor.RandomColor;
import com.lzy.okhttpserver.download.DownloadInfo;
import com.lzy.okhttpserver.download.DownloadManager;
import com.lzy.okhttpserver.download.DownloadService;
import com.lzy.okhttpserver.listener.DownloadListener;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.GetRequest;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by programmer on 2016/9/18 0018.
 */
public class AppDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_icon;
    private TextView tv_name;
    private RatingBar rating_bar;
    private TextView tv_download_number;
    private TextView tv_version;
    private TextView tv_date;
    private NestedScrollView scroll_view;
    private TextView tv_size;
    private MyListener listener;
    private StateLayout stateLayout;
    private LinearLayout ll_save_icon;
    private LinearLayout ll_save_desc;
    private ImageView iv_arrow;
    private LinearLayout ll_save_root;

    /**
     * 指示安全模块的文字是否是显示的，true代表显示
     */
    private boolean saveDescIsOpen;
    /**
     * 指示简介的文字是否是显示的，true代表显示
     */
    private boolean descIsOpen;

    private LinearLayout ll_desc;
    private ImageView iv_desc_arrow;
    private TextView tv_desc;
    /**
     * 简介内容的原始高度
     */
    private int originalHeight;

    private String new_id;
    private int position;
    private TextView tv_cate;
    private ProgressBar pb_download_progress;
    private String downloadurl;
    private String downUrl;
    private String name;
    private TextView tv_download_desc;
    private String imagepath;
    private String id;
    private TextView title_bar;
    private String app_name;
    private String size;
    private String pkgname;
    private String uid;
    private ImageButton iv_share;


    private TextView downloadSize;
    private TextView tvProgress;
    private TextView netSpeed;
    private ProgressBar pbProgress;
    private Button download;
    private Button remove;
    private Button restart;

    private DownloadInfo downloadInfo;

    private DownloadManager downloadManager;
    private List<Apps> apps;

    private RelativeLayout rldown;
    private CollapsingToolbarLayout cp;
    private Toolbar mToolbar;
    private CircleImageView i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_detail);

        Bundle extras = getIntent().getExtras();
        new_id = extras.getString("new_id");
        position = extras.getInt("position");
        app_name = extras.getString("name");
        initView();
        initData();
        if(downloadInfo==null) {

            netSpeed.setVisibility(View.INVISIBLE);
            downloadSize.setVisibility(View.INVISIBLE);;
            tvProgress.setVisibility(View.INVISIBLE);


        }
        /**
         * 检查权限
         */
        checkPermissions.Permission();

    }

    //处理menu事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_item1:
                startActivity(new Intent(MyApplication.getContext(),DownloadManagerActivity.class));
                return true;

            case R.id.action_search:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain"); // 分享发送的数据类型
                String msg = "推荐给大家一个不错的App,链接是:" + downloadurl;
                 // 分享的内容
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                 // 目标应用选择对话框的标题
                startActivity(Intent.createChooser(intent, "选择分享" + name + "到："));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //引入menu布局
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void initView() {
      /*  title_bar = (TextView) findViewById(R.id.title_bar);*/

// 第一块内容：应用信息，比如应用名称、评分、大小、版本
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_cate = (TextView) findViewById(R.id.tv_cate);
        tv_name = (TextView) findViewById(R.id.tv_name);
        //rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        tv_download_number = (TextView) findViewById(R.id.tv_download_number);
        iv_desc_arrow = (ImageView) findViewById(R.id.iv_desc_arrow);
        scroll_view = (NestedScrollView) findViewById(R.id.scroll_view);
        tv_size = (TextView) findViewById(R.id.tv_size);

        cp = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        initCollapsingTl();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolBar();


        // 初始化简介的控件
        ll_desc = (LinearLayout) findViewById(R.id.ll_desc);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        i = (CircleImageView) findViewById(R.id.imageView);

        downloadSize = (TextView) findViewById(R.id.downloadSize);
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        netSpeed = (TextView) findViewById(R.id.netSpeed);
        pbProgress = (ProgressBar) findViewById(R.id.pbProgress);
        download = (Button) findViewById(R.id.start);
      //  remove = (Button) findViewById(R.id.remove);
        restart = (Button) findViewById(R.id.restart);

        downloadManager = DownloadService.getDownloadManager();
        download.setOnClickListener(this);

        restart.setOnClickListener(this);
        listener = new MyListener();


        // 通过这个方式可以监听View的布局发生改变
        tv_desc.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                originalHeight = tv_desc.getHeight();
                if (originalHeight > 0) {
                    // 一定要记得移除这个监听器，只要onLayout方法重新被执行了，则这个onGlobalLayout方法就会被调用
                    tv_desc.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void initCollapsingTl() {
        cp.setTitle(app_name);
        //生成随机颜色
        RandomColor randomColor = new RandomColor();
        int color = randomColor.randomColor();
        cp.setExpandedTitleColor(color);//设置还没收缩时状态下字体颜色
        cp.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
    }

    /**
     * 初始化数据
     */

    private void initData() {
        VolleyGet();
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (downloadInfo != null) {
            refreshUi(downloadInfo);
        }else if(downloadInfo==null) {
            netSpeed.setVisibility(View.INVISIBLE);

            downloadSize.setVisibility(View.INVISIBLE);;
            tvProgress.setVisibility(View.INVISIBLE);

        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadInfo != null) downloadInfo.removeListener();
    }

    @Override
    public void onClick(View v) {
        //每次点击的时候，需要更新当前对象
        downloadInfo = downloadManager.getDownloadInfo(apps.get(position).getDownloadurl());
        if (v.getId() == download.getId()) {
            if (downloadInfo == null) {
                sendNotification();//发送通知
                GetRequest request = OkHttpUtils.get(apps.get(position).getDownloadurl())//
                        .headers("headerKey1", "headerValue1")//
                        .headers("headerKey2", "headerValue2")//
                        .params("paramKey1", "paramValue1")//
                        .params("paramKey2", "paramValue2");
                downloadManager.addTask(apps.get(position).getDownloadurl(), request, listener);
                return;
            }
            switch (downloadInfo.getState()) {
                case DownloadManager.PAUSE:
                case DownloadManager.NONE:
                case DownloadManager.ERROR:
                    downloadManager.addTask(downloadInfo.getUrl(), downloadInfo.getRequest(), listener);


                    break;
                case DownloadManager.DOWNLOADING:
                    downloadManager.pauseTask(downloadInfo.getUrl());
                    break;
                case DownloadManager.FINISH:
                    if (ApkUtils.isAvailable(this, new File(downloadInfo.getTargetPath()))) {
                        ApkUtils.uninstall(this, ApkUtils.getPackageName(this, downloadInfo.getTargetPath()));
                    } else {
                        ApkUtils.install(this, new File(downloadInfo.getTargetPath()));
                    }
                    break;
            }

        } else if (v.getId() == restart.getId()) {
            if (downloadInfo == null) {
                Toast.makeText(this, "请先下载任务", Toast.LENGTH_SHORT).show();
                return;
            }
            downloadManager.restartTask(downloadInfo.getUrl());

        }
    }

    /**
     * 发送通知
     */
    private void sendNotification() {
        NotificationManager nm = (NotificationManager) MyApplication.getContext()
                .getSystemService(Activity.NOTIFICATION_SERVICE);
        NotificationCompat.Builder cBuilder = new NotificationCompat.Builder(MyApplication.getContext());
        //设置想要展示的数据内容
        Intent intent_noti = new Intent(MyApplication.getContext(),DownloadManagerActivity.class);
        //文件的类型，从tomcat里面找
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
    }

    private class MyListener extends DownloadListener {

        @Override
        public void onProgress(DownloadInfo downloadInfo) {
            refreshUi(downloadInfo);
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
            System.out.println("onFinish");
            Toast.makeText(AppDetailsActivity.this, "下载完成:" + downloadInfo.getTargetPath(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
            System.out.println("onError");
            if (errorMsg != null)
                Toast.makeText(AppDetailsActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 刷新Ui
     * @param downloadInfo
     */
    private void refreshUi(DownloadInfo downloadInfo) {

        String downloadLength = Formatter.formatFileSize(AppDetailsActivity.this, downloadInfo.getDownloadLength());
        String totalLength = Formatter.formatFileSize(AppDetailsActivity.this, downloadInfo.getTotalLength());
        netSpeed.setVisibility(View.VISIBLE);
        downloadSize.setVisibility(View.VISIBLE);
        tvProgress.setVisibility(View.VISIBLE);
        downloadSize.setText(downloadLength + "/" + totalLength);
        String networkSpeed = Formatter.formatFileSize(AppDetailsActivity.this, downloadInfo.getNetworkSpeed());

        netSpeed.setText(networkSpeed + "/s");
        tvProgress.setText((Math.round(downloadInfo.getProgress() * 10000) * 1.0f / 100) + "%");
        pbProgress.setMax((int) downloadInfo.getTotalLength());
        pbProgress.setProgress((int) downloadInfo.getDownloadLength());
        switch (downloadInfo.getState()) {
            case DownloadManager.NONE:
                download.setText("下载");
                break;
            case DownloadManager.DOWNLOADING:
                download.setText("暂停");

                break;
            case DownloadManager.PAUSE:
                download.setText("继续");
                break;
            case DownloadManager.WAITING:
                download.setText("等待");
                break;
            case DownloadManager.ERROR:
                download.setText("出错");
                break;
            case DownloadManager.FINISH:
                if (ApkUtils.isAvailable(AppDetailsActivity.this, new File(downloadInfo.getTargetPath()))) {
                    download.setText("卸载");
                } else {
                    download.setText("安装");
                }
                break;
        }

    }

    /**
     * 请求网络
     */
    private void VolleyGet() {
        VolleyUtil.RequestDataInfo(this, null, Url.HOST + "service=Applist.listDetail&listid=" + new_id, Top.class, new ResultListener<Top>() {
            @Override
            public void onResponse(Top bean) {
                Top n = bean;
                apps = n.getData().getInfo().getApps();
                showScreenInfo(apps);
                Log.e("sd", apps.get(1).getName());

                downloadInfo = downloadManager.getDownloadInfo(apps.get(position).getDownloadurl());
                if (downloadInfo != null) {
                    //如果任务存在，把任务的监听换成当前页面需要的监听
                    downloadInfo.setListener(listener);
                    //需要第一次手动刷一次，因为任务可能处于下载完成，暂停，等待状态，此时是不会回调进度方法的
                    refreshUi(downloadInfo);
                }
                com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(downUrl, i);

            }

            @Override
            public void onFaild() {

            }
        });

    }
    /**
     * 展示应用的图标
     *
     * @param apps
     */
    private void showScreenInfo(List<Apps> apps) {
        //应用名称
        name = apps.get(position).getName();
        tv_name.setText(name);

        //应用图标
        downUrl = apps.get(position).getImagepath();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(downUrl, iv_icon);    // 下载图片并显示
        imagepath = apps.get(position).getImagepath();

        //下载量
        String installcount = apps.get(position).getInstallcount();
        if (installcount.indexOf("万") == -1) {
          /*  Double aDouble = Double.valueOf(installcount);
            DecimalFormat df = new DecimalFormat("#,###.0");
            String xs = df.format(aDouble);*/
            String substring = installcount.substring(0, installcount.length() - 4);
            tv_download_number.setText("下载量：" + substring + "万");

        }

        tv_download_number.setText("下载量：" + installcount);

        //大小
        size = apps.get(position).getSize();

        //因为后台返回的数据有的包含了M 有的直接是字节 ， 加了这两个判断 进行区分
        if (size.indexOf("M") == -1) {
            long l = Long.valueOf(size).longValue();
            tv_size.setText("应用大小：" + Formatter.formatShortFileSize(this, l));
        } else {
            tv_size.setText("应用大小：" + size);
        }
        //描述
        String comment = apps.get(position).getComment();
        tv_desc.setText(comment);
        ll_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descDisplayToggle();
            }
        });

        //类型
        String cate = apps.get(position).getCate();
        tv_cate.setText("应用类型：" + cate);
        downloadurl = apps.get(position).getDownloadurl();
        id = apps.get(position).getId();
        uid = apps.get(position).getUid();
        apps.get(position).getUrl();
        Log.e("我是id", id);
        Log.e("我是下载地址", downloadurl);

    }

    /**
     * 应用简介显示的开关，，要么开，要么关
     */
    private void descDisplayToggle() {
        int allLineHeight = getAllLineHeight();
        ValueAnimator valueAnimator;
        if (descIsOpen) {
            // 如果简介原来是打开的，则关闭：从完整的高变成7行的高
            valueAnimator = ValueAnimator.ofInt(allLineHeight, originalHeight);
            iv_desc_arrow.setImageResource(R.mipmap.arrow_down);
        } else {
            // 如果简介原来是关闭的，则打开 ：从7行的高变成完整的高
            valueAnimator = ValueAnimator.ofInt(originalHeight, allLineHeight);
            iv_desc_arrow.setImageResource(R.mipmap.arrow_up);
        }

        descIsOpen = !descIsOpen;

        // 监听ValueAnimator模拟出来的数值
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int height = (Integer) valueAnimator.getAnimatedValue();
                // 把高设置给简介内容的TextView
                tv_desc.getLayoutParams().height = height;
                tv_desc.requestLayout();    // 通知View刷新布局参数

                // 让TextView的高出来多少，则ScrllView就向上滚动多少
                scroll_view.scrollBy(0, height);    // scrollBy方法是在原来的基础上再滚动的
            }
        });

        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

    /**
     * 获取简介所有文本全部显示的时候的高度
     */
    private int getAllLineHeight() {
        TextView textView = new TextView(this);

        textView.setTextSize(14);    // 这个字体大小，一定要和布局里面的简介内容的TextView保持一致
        CharSequence text = tv_desc.getText();    // 取出简介里面的所有文本
        textView.setText(text);

        // 测量新的TextView，宽度的测试规格和原来的tv_desc保持一致
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(tv_desc.getWidth(), View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);

        return textView.getMeasuredHeight();
    }
}