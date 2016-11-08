package com.enet.cn.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.enet.cn.myapplication.MainActivity;
import com.enet.cn.myapplication.R;
import com.enet.cn.myapplication.adapter.SplashAdapter;
import com.enet.cn.myapplication.utils.CacheUtil;
import com.enet.cn.myapplication.utils.Contants;
import com.enet.cn.myapplication.utils.Dp2Px;

import java.util.ArrayList;

/**
 * Created by programmer on 2016/9/7 0007.
 */
public class SplashActivity extends AppCompatActivity {

    private ArrayList<ImageView> imgs;
    private ViewPager splash_vp;
    private LinearLayout ll_guide_points;
    private ImageView iv_guide_redpoint;
    private Button bt_guide_start;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 使屏幕不显示标题栏(必须要在setContentView方法执行前执行)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏，使内容全屏显示(必须要在setContentView方法执行前执行)
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        initView();
        // 取是否打开过应用的标记
        boolean isAppFirstOpen = CacheUtil.getBoolean(getApplicationContext(), Contants.IS_APP_FIRST_OPEN, true);
        if (isAppFirstOpen) {
            // 是第一次打开，跳到引导界面
            System.out.println("是第一次打开，跳到引导界面");
        } else {
            // 已经打开过，跳到主界面
            System.out.println("已经打开过，跳到主界面");

            startActivity(new Intent(this, MainActivity.class));
            finish();//关掉自己
        }
    }
    /**
     * 初始化布局文件
     */
    private void initView() {
        splash_vp = (ViewPager) findViewById(R.id.splash_pager);
        ll_guide_points = (LinearLayout) findViewById(R.id.ll_guide_points);
        iv_guide_redpoint = (ImageView) findViewById(R.id.iv_guide_redpoint);
        bt_guide_start = (Button) findViewById(R.id.bt_guide_start);
        initData();
        SplashAdapter splashAdapter = new SplashAdapter(getSupportFragmentManager(), imgs);
        splash_vp.setAdapter(splashAdapter);
        //给ViewPager设置监听
        splash_vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int redPointX = (int) ((positionOffset + position) * Dp2Px.dp2px(20));
                // 不停的设置红点的左边距，达到红点移动的效果
                // 获取控件的宽高属性对象
                android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) iv_guide_redpoint
                        .getLayoutParams();
                // 修改左边距
                params.leftMargin = redPointX;
                iv_guide_redpoint.setLayoutParams(params);
            }

            // 当ViewPager滑动到某一页时，调用
            @Override
            public void onPageSelected(int position) {
                // 当滑动到最后一页时，让开始按钮展示出来
                if (position == imgs.size() - 1) {
                    bt_guide_start.setVisibility(View.VISIBLE);
                } else {
                    bt_guide_start.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    /**
     * 初始化数据
     */
    private void initData() {
        int[] imgIds = new int[]{R.mipmap.splash, R.mipmap.splash,
                R.mipmap.splash};
        // 创建保存ImageView的集合
        imgs = new ArrayList<ImageView>();
        for (int i = 0; i < imgIds.length; i++) {
            // 把每一个图片转成ImageView
            ImageView imageView = new ImageView(this);
            // 把图片设置给ImageView
            // 手动给ImageView设置缩放类型
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(imgIds[i]);
            // // 会自动把图片按照控件的宽高缩放
            // imageView.setBackgroundResource(imgIds[i]);
            imgs.add(imageView);

            // 创建小灰点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.guide_point_gray);
            // 设置宽高 导包时需要看自己属于哪个父容器，要那个如容器的包
            // 需要把宽高转换成 10dp代表的一个像素
            int dp2px = Dp2Px.dp2px(10);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px, dp2px);
            point.setLayoutParams(params);
            // 设置灰点的边距
            if (i != 0) {
                params.leftMargin = dp2px;
            }
            ll_guide_points.addView(point);
        }
    }
    // 开始按钮点击
    public void start(View v) {
        // 保存标记，记录用户已经打开过应用
        CacheUtil.putBoolean(this, Contants.IS_APP_FIRST_OPEN, false);
        // 进入主界面
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
