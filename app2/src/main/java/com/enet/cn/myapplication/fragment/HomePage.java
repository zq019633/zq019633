package com.enet.cn.myapplication.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.enet.cn.myapplication.Activity.DetailActivity;
import com.enet.cn.myapplication.MyApplication;
import com.enet.cn.myapplication.R;
import com.enet.cn.myapplication.Url.Url;
import com.enet.cn.myapplication.adapter.BannerAdapter;
import com.enet.cn.myapplication.adapter.CateGridViewAdapter;
import com.enet.cn.myapplication.adapter.GridViewAdapter;
import com.enet.cn.myapplication.bean.HomeBean.Carousel;
import com.enet.cn.myapplication.bean.HomeBean.Default;
import com.enet.cn.myapplication.bean.HomeBean.Home;
import com.enet.cn.myapplication.bean.HomeBean.News;
import com.enet.cn.myapplication.fragment.Base.BaseFragment;
import com.enet.cn.myapplication.interface_.ResultListener;
import com.enet.cn.myapplication.utils.Contants;
import com.enet.cn.myapplication.utils.VolleyUtil;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.jude.rollviewpager.hintview.TextHintView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by programmer on 2016/9/5 0005.
 */
public class HomePage extends BaseFragment implements View.OnClickListener {


    private View home_view;
    private RollPagerView home_view_pager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ListView home_lv;
    private ArrayList data = new ArrayList();

    private ArrayList infoList;

    private View currentDot;

    private GridView category_gridView;

    private LinearLayout mLLDot;
    private List<Carousel> carousellist;
    private GridView home_gridView;
    private List<News> news;
    private List<Default> mdefault;
    private boolean scrollFlag = false;// 标记是否滑动
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置


    private ScrollView scrollView;
    private TextView title;
    private Button toTopBtn;



    /**
     * 初始化布局文件
     */
    @Override
    protected void initView() {
        title = (TextView) stateLayout.findViewById(R.id.title_bar);
        scrollView = (ScrollView) stateLayout.findViewById(R.id.sv);
        home_view_pager = (RollPagerView) stateLayout.findViewById(R.id.home_view_pager);

        home_gridView = (GridView) stateLayout.findViewById(R.id.home_gridView);
        category_gridView = (GridView) stateLayout.findViewById(R.id.home_gridView_);
        toTopBtn = (Button) stateLayout.findViewById(R.id.top_btn);
        toTopBtn.setOnClickListener(this);
        home_view_pager.setHintView(new TextHintView(MyApplication.getContext()));

    }



    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

      VolleyPost();
     //   VolleyGet();

    }

    private void VolleyGet() {

            VolleyUtil.RequestDataInfo(getActivity(), null, Url.HOME, Home.class, new ResultListener<Home>() {
                @Override
                public void onResponse(Home bean) {
                    final Home home = bean;
                    Log.e("post", home.getData().getInfo().getCarousel().get(0).getImagepath());
                    carousellist = home.getData().getInfo().getCarousel();

                    news = home.getData().getInfo().getNews();
                    mdefault = home.getData().getInfo().getDefault();
                    Log.e("123post", "" + mdefault.get(0).getImagepath());
                    if (home != null) {
                        home_view_pager.setAdapter(new BannerAdapter(carousellist));
                        stateLayout.showContentView();
                    }
                    home_gridView.setAdapter(new GridViewAdapter(home.getData().getInfo().getNews()));
                    home_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            List<News> news = home.getData().getInfo().getNews();
                            Intent intent = new Intent(MyApplication.getContext(), DetailActivity.class);

                            intent.putExtra("new_id", news.get(position).getId());
                            intent.putExtra("new_name", news.get(position).getName());
                            intent.putExtra("new_catid", news.get(position).getCatid());
                            intent.putExtra("new_imagepath", news.get(position).getImagepath());
                            startActivity(intent);

                        }
                    });

                    category_gridView.setAdapter(new CateGridViewAdapter(home.getData().getInfo().getDefault()));
                    category_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            List<Default> aDefault = home.getData().getInfo().getDefault();
                            /**跳转到activity界面*/
                            switchActivity(aDefault, position);

                        }
                    });

                }

                @Override
                public void onFaild() {
                    Toast.makeText(MyApplication.getContext(), "网络连接失败，请检查网络", Toast.LENGTH_SHORT).show();
                }
            });

        }

    /**
     * post方式请求数据
     */
    private void VolleyPost() {

        /**
         * post的方式
         */
        HashMap<String, String> params = new HashMap<>();
        params.put("service", "Applist.index");
        VolleyUtil.RequestDataInfo(getActivity(), null, Url.HOME, Home.class, new ResultListener<Home>() {
            @Override
            public void onResponse(Home bean) {
                final Home home = bean;
                Log.e("post", home.getData().getInfo().getCarousel().get(0).getImagepath());
                carousellist = home.getData().getInfo().getCarousel();

                news = home.getData().getInfo().getNews();
                mdefault = home.getData().getInfo().getDefault();
                Log.e("123post", "" + mdefault.get(0).getImagepath());
                if (home != null) {
                    home_view_pager.setAdapter(new BannerAdapter(carousellist));
                    //轮播图的点击事件
                    home_view_pager.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                            List<News> aDefault = home.getData().getInfo().getNews();
                            if(position==0){

                                switchActivity2(aDefault,3);
                            }else if(position==1){

                                switchActivity2(aDefault,1);
                            }else if(position==2){
                                switchActivity2(aDefault,4);
                            }
                        }

                    });
                    stateLayout.showContentView();
                }
                home_gridView.setAdapter(new GridViewAdapter(home.getData().getInfo().getNews()));
                home_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        List<News> news = home.getData().getInfo().getNews();
                        Intent intent = new Intent(MyApplication.getContext(), DetailActivity.class);

                        intent.putExtra("new_id", news.get(position).getId());
                        intent.putExtra("new_name", news.get(position).getName());
                        intent.putExtra("new_catid", news.get(position).getCatid());
                        intent.putExtra("new_imagepath", news.get(position).getImagepath());
                        startActivity(intent);

                    }
                });

                category_gridView.setAdapter(new CateGridViewAdapter(home.getData().getInfo().getDefault()));
                category_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        List<Default> aDefault = home.getData().getInfo().getDefault();
                        /**跳转到activity界面*/
                        switchActivity(aDefault, position);

                    }
                });

            }

            @Override
            public void onFaild() {
                Toast.makeText(MyApplication.getContext(), "网络连接失败，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void switchActivity(List<Default> aDefault, int position) {

        Intent intent = new Intent(MyApplication.getContext(), DetailActivity.class);
        intent.putExtra("new_id", aDefault.get(position).getId());
        intent.putExtra("new_name", aDefault.get(position).getName());
        intent.putExtra("new_catid", aDefault.get(position).getCatid());
        intent.putExtra("new_imagepath", aDefault.get(position).getImagepath());
        startActivity(intent);
    }

    /**
     * 为了响应轮播图的点击事件
     * @param aDefault
     * @param position
     */
    private void switchActivity2(List<News> aDefault, int position) {

        Intent intent = new Intent(MyApplication.getContext(), DetailActivity.class);
        intent.putExtra("new_id", aDefault.get(position).getId());
        intent.putExtra("new_name", aDefault.get(position).getName());
        intent.putExtra("new_catid", aDefault.get(position).getCatid());
        intent.putExtra("new_imagepath", aDefault.get(position).getImagepath());
        startActivity(intent);
    }

    /**
     * 返回正常的界面，可以返回一个布局id，也可以返回一个View
     */
    @Override
    public Object getContentView() {
        return R.layout.fragment_home;
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_btn://
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                break;
        }
    }

}


