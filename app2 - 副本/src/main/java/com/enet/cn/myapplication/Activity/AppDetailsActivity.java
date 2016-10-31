package com.enet.cn.myapplication.Activity;

import android.animation.ValueAnimator;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.enet.cn.myapplication.R;
import com.enet.cn.myapplication.Url.Url;
import com.enet.cn.myapplication.bean.TopBean.Apps;
import com.enet.cn.myapplication.bean.TopBean.Top;
import com.enet.cn.myapplication.download.DownloadInfo;
import com.enet.cn.myapplication.download.DownloadService;
import com.enet.cn.myapplication.interface_.ResultListener;
import com.enet.cn.myapplication.service.NotifitionService;
import com.enet.cn.myapplication.utils.ApkUtils;
import com.enet.cn.myapplication.utils.ImageLoader;
import com.enet.cn.myapplication.utils.VolleyUtil;
import com.enet.cn.myapplication.view.StateLayout;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.HttpHandler.State;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.util.List;

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
    private ScrollView scroll_view;
    private TextView tv_size;

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
    private com.enet.cn.myapplication.download.DownloadManager downloadManager;
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

    }

    private void initView() {

        downloadManager = DownloadService.getDownloadManager(getApplicationContext());
        title_bar = (TextView) findViewById(R.id.title_bar);

// 第一块内容：应用信息，比如应用名称、评分、大小、版本
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_cate = (TextView) findViewById(R.id.tv_cate);
        tv_name = (TextView) findViewById(R.id.tv_name);
        //rating_bar = (RatingBar) findViewById(R.id.rating_bar);
        tv_download_number = (TextView) findViewById(R.id.tv_download_number);
        iv_desc_arrow = (ImageView) findViewById(R.id.iv_desc_arrow);
        scroll_view = (ScrollView) findViewById(R.id.scroll_view);
        tv_size = (TextView) findViewById(R.id.tv_size);


        // 初始化下载模块相关的控件
        pb_download_progress = (ProgressBar) findViewById(R.id.pb_download_progress);
        tv_download_desc = (TextView) findViewById(R.id.tv_download_desc);


        ImageButton imageButton = (ImageButton) findViewById(R.id.back);
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 初始化简介的控件
        ll_desc = (LinearLayout) findViewById(R.id.ll_desc);
        tv_desc = (TextView) findViewById(R.id.tv_desc);

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
     * 初始化数据
     */

    private void initData() {
        title_bar.setText(app_name);
        title_bar.setTextSize(13);
        VolleyGet();
        pb_download_progress.setOnClickListener(this);
    }

    private void VolleyGet() {
        VolleyUtil.RequestDataInfo(this, null, Url.HOST + "service=Applist.listDetail&listid=" + new_id, Top.class, new ResultListener<Top>() {
            @Override
            public void onResponse(Top bean) {
                Top n = bean;
                List<Apps> apps = n.getData().getInfo().getApps();
                showScreenInfo(apps);
                Log.e("sd", apps.get(1).getName());
                refreshUI();

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


            tv_download_number.setText("下载量：" + substring+"万");

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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, NotifitionService.class);
        intent.putExtra("image", imagepath);
        intent.putExtra("name", name);
        startService(intent);
        downloadApk();
    }

    /**
     * 下载应用
     */
    private void downloadApk() {
        DownloadStateListener downloadStateListener = new DownloadStateListener();
        DownloadInfo downloadInfo = downloadManager.getDownloadInfoByAppId(id);
        if (downloadInfo == null) {
            // 如果原来没有下载过，则开始一个新的下载
            String target = "/sdcard/xnjapp/" + name + ".apk";


            try {
                downloadManager.addNewDownload(id, downloadurl,
                        name,        // 保存到数据库时候的一个应用名称
                        target,        // 指定保存路径和保存的文件名
                        true,        // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                        false,        // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                        downloadStateListener);        // 用于接收下载状态的监听器
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
        } else {
            // 如果原来下载过，则要么暂停，要继续
            HttpHandler.State state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                case STARTED:
                case LOADING:
                    // 如果原来是等待、开始、正在下载，则暂停
                    try {
                        downloadManager.stopDownload(downloadInfo);    // 暂停下载
                    } catch (DbException e) {
                        LogUtils.e(e.getMessage(), e);
                    }
                    break;
                case CANCELLED:
                case FAILURE:
                    // 如果原来暂停了下载，或者下载失败了，则重新下载，如果暂停的话是断点续传地下载
                    try {
                        downloadManager.resumeDownload(downloadInfo, downloadStateListener);    // 第二个参数用于接收下载状态
                    } catch (DbException e) {
                        LogUtils.e(e.getMessage(), e);
                    }
                    break;
                case SUCCESS:
                    ApkUtils.install(downloadInfo.getFileSavePath());
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 下载状态监听器
     */
    private class DownloadStateListener extends RequestCallBack<File> {

        /**
         * 下载失败
         */
        @Override
        public void onFailure(HttpException arg0, String arg1) {
            refreshUI();
        }

        /**
         * 下载成功
         */
        @Override
        public void onSuccess(ResponseInfo<File> arg0) {
            refreshUI();
        }

        /**
         * 取消下载
         */
        @Override
        public void onCancelled() {
            refreshUI();
        }

        /**
         * 正在下载
         */
        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            refreshUI();
        }

        /**
         * 开始下载
         */
        @Override
        public void onStart() {
            refreshUI();
        }

    }


    /**
     * 刷新UI
     */
    public void refreshUI() {
        DownloadInfo downloadInfo = downloadManager.getDownloadInfoByAppId(id);
        if (downloadInfo == null) {
            // 如果原来没有下载过，则显示成下载
            tv_download_desc.setText("下载");
            return;
        }

        HttpHandler.State state = downloadInfo.getState();    // 获取当前下载信息的下载状态
        switch (state) {
            case WAITING:
            case STARTED:
            case LOADING:
            case CANCELLED:// 如果原来是等待、开始、正在下载或者暂停的状态，则需要显示下载进度
                if (downloadInfo.getFileLength() > 0) {
                    // 计算下载的百分比
                    int percent = (int) (downloadInfo.getProgress() * 100 / downloadInfo.getFileLength());
                    pb_download_progress.setProgress(percent);
                    tv_download_desc.setText(percent + "%");
                } else {
                    pb_download_progress.setProgress(0);
                    tv_download_desc.setText("0%");
                }

                if (state == State.CANCELLED) {
                    // 如果是暂停了，则显示一个"继续"
                    tv_download_desc.setText("继续");
                }
                break;
            case SUCCESS:
                tv_download_desc.setText("安装");
                pb_download_progress.setProgress(100);
                break;
            case FAILURE:
                tv_download_desc.setText("重试");
                break;
            default:
                break;
        }
    }

}