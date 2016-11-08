package com.enet.cn.myapplication.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.enet.cn.myapplication.MyApplication;
import com.enet.cn.myapplication.R;
import com.enet.cn.myapplication.Url.Url;
import com.enet.cn.myapplication.adapter.DetailAdapter;
import com.enet.cn.myapplication.bean.TopBean.Apps;
import com.enet.cn.myapplication.bean.TopBean.Top;
import com.enet.cn.myapplication.interface_.ResultListener;
import com.enet.cn.myapplication.listener.OnItemClickListener;
import com.enet.cn.myapplication.utils.VolleyUtil;
import com.enet.cn.myapplication.utils.checkPermissions;
import com.enet.cn.myapplication.view.CircularAnim;
import com.github.lzyzsd.randomcolor.RandomColor;
import com.mingle.widget.LoadingView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by programmer on 2016/9/13 0013.
 */
public class DetailActivity extends AppCompatActivity implements AbsListView.OnScrollListener,OnItemClickListener<Apps> {

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

    private boolean isDown;
    private ArrayList downlist;
    private boolean isd;

    private LoadingView loadingView;
    private Button openManager;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        Bundle extras = getIntent().getExtras();
        new_id = extras.getString("new_id");
        new_name = extras.getString("new_name");
        new_catid = extras.getString("new_catid");
        new_imagepath = extras.getString("new_imagepath");
        mDownloadDir = new File(Environment.getExternalStorageDirectory(),"Download");

        initView();
        initData();


        /**
         * 检查权限
         */
        checkPermissions.Permission();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mAdapter!=null){
        mAdapter.notifyDataSetChanged();
    }}
    //引入menu布局
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.manager_menu, menu);
        return true;
    }
    //处理menu事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.down:
                startActivity(new Intent(MyApplication.getContext(),DownloadManagerActivity.class));
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 找到控件
     */
    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);

        detail_lv = (ListView) findViewById(R.id.detail_lv);
       loadingView = (LoadingView) findViewById(R.id.loadView);

        detail_lv.setOnScrollListener(this);
        initSwipRefreshLayout();

        detail_lv.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0)
                    swipeRefreshLayout.setEnabled(true);
                else
                    swipeRefreshLayout.setEnabled(false);

            }
        });
    }
    /**
     * 初始化
     */
    private void initSwipRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        VolleyGet();
                        Toast.makeText(DetailActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);}

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


    /**
     * 初始化数据
     */
    private void initData() {
        initToolBar();
        VolleyGet();
    }

    /**
     * 初始化toolBar
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
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle(new_name);
        //通过CollapsingToolbarLayout修改字体颜色

        //生成随机的颜色
        RandomColor randomColor = new RandomColor();
        int color = randomColor.randomColor();
        mCollapsingToolbarLayout.setExpandedTitleColor(color);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
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

                        final Intent intent = new Intent(MyApplication.getContext(), AppDetailsActivity.class);
                        intent.putExtra("new_id", new_id);
                        intent.putExtra("position", position);
                        intent.putExtra("name", apps.get(position).getName());
                        CircularAnim.fullActivity(DetailActivity.this, view)
                              /*  .colorOrImageRes(R.color.colorAccent)*/
                                .go(new CircularAnim.OnAnimationEndListener() {
                                    @Override
                                    public void onAnimationEnd() {
                                        startActivity(intent);
                                    }
                                });

                    }
                });
            }

            @Override
            public void onFaild() {
            }
        });
    }

    /**
     * test
     * @param v
     * @param position
     * @param info
     * @param isDownload
     */
    @Override
    public void onItemClick(View v, int position, Apps info ,Boolean isDownload) {

        Log.e("我是位置",""+position);

    }



}