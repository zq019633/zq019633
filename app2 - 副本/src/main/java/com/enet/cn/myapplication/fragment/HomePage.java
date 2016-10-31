package com.enet.cn.myapplication.fragment;


import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by programmer on 2016/9/5 0005.
 */
public class HomePage extends BaseFragment {


    private View home_view;
    private ViewPager home_view_pager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ListView home_lv;
    private ArrayList data = new ArrayList();

    private ArrayList infoList;

    private View currentDot;

    private GridView category_gridView;
    private View[] dots;
    private LinearLayout mLLDot;
    private List<Carousel> carousellist;
    private GridView home_gridView;
    private List<News> news;
    private List<Default> mdefault;


    /**
     * 切换广告条
     */


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Contants.SWITCH_BANNER:
                    switchBanner();
                    break;
            }
        }

        ;
    };
    private ScrollView scrollView;
    private TextView title;


    /**
     * 切换广告条
     */
    public void switchBanner() {
        int currentItem = home_view_pager.getCurrentItem();    // 获取当前ViewPager显示的位置
        home_view_pager.setCurrentItem(currentItem + 1);    // 切换到下一页
        handler.removeMessages(Contants.SWITCH_BANNER);                // 把之前的消息都移除掉
        handler.sendEmptyMessageDelayed(Contants.SWITCH_BANNER, 3000);    // 3秒后切换下一页
    }


    @Override
    public void onDestroy() {
        handler.removeMessages(Contants.SWITCH_BANNER);    // 移除切换广告条的消息
        super.onDestroy();
    }

    /**
     * 初始化布局文件
     */
    @Override
    protected void initView() {
        title = (TextView) stateLayout.findViewById(R.id.title_bar);
        scrollView = (ScrollView) stateLayout.findViewById(R.id.sv);
        home_view_pager = (ViewPager) stateLayout.findViewById(R.id.home_view_pager);
        mLLDot = (LinearLayout) stateLayout.findViewById(R.id.ll_dot);
        home_gridView = (GridView) stateLayout.findViewById(R.id.home_gridView);
        category_gridView = (GridView) stateLayout.findViewById(R.id.home_gridView_);



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
                        //创建点的集合
                        dots = new View[home.getData().getInfo().getCarousel().size()];
                        for (int i = 0; i < home.getData().getInfo().getCarousel().size(); i++) {
                            //根据图片的张数，创建相应的个数的点
                            createDot(i);
                        }
                        home_view_pager.setCurrentItem(home_view_pager.getAdapter().getCount() / 2);
                        home_view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                changed(position);
                            }

                            @Override
                            public void onPageSelected(int position) {
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                            }
                        });

                        home_view_pager.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_DOWN:// 按下
                                        Toast.makeText(getContext(), "图片被点击了", Toast.LENGTH_SHORT).show();
                                        // 当参数为null时，handler将移除所有的回调和消息
                                        handler.removeCallbacksAndMessages(null);
                                        break;
                                    case MotionEvent.ACTION_CANCEL:// 事件取消
                                        // 给handler发一条消息即可让它自动继续轮播
                                        handler.sendEmptyMessageDelayed(0, 3000);
                                        break;
                                    case MotionEvent.ACTION_UP:// 抬起
                                        handler.sendEmptyMessageDelayed(0, 3000);
                                        break;
                                    default:
                                        break;
                                }
                                return false;
                            }
                        });
                        handler.sendEmptyMessageDelayed(Contants.SWITCH_BANNER, 3000);
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
                    //创建点的集合
                    dots = new View[home.getData().getInfo().getCarousel().size()];
                    for (int i = 0; i < home.getData().getInfo().getCarousel().size(); i++) {
                        //根据图片的张数，创建相应的个数的点
                        createDot(i);
                    }
                    home_view_pager.setCurrentItem(home_view_pager.getAdapter().getCount() / 2);
                    home_view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            changed(position);
                        }

                        @Override
                        public void onPageSelected(int position) {
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                        }
                    });

                    home_view_pager.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:// 按下
                                    Toast.makeText(getContext(), "图片被点击了", Toast.LENGTH_SHORT).show();
                                    // 当参数为null时，handler将移除所有的回调和消息
                                    handler.removeCallbacksAndMessages(null);
                                    break;
                                case MotionEvent.ACTION_CANCEL:// 事件取消
                                    // 给handler发一条消息即可让它自动继续轮播
                                    handler.sendEmptyMessageDelayed(0, 3000);
                                    break;
                                case MotionEvent.ACTION_UP:// 抬起
                                    handler.sendEmptyMessageDelayed(0, 3000);
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }
                    });
                    handler.sendEmptyMessageDelayed(Contants.SWITCH_BANNER, 3000);
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
     * 创建点
     *
     * @param i
     */
    private void createDot(int i) {
        //保存创建的点
        dots[i] = new View(getContext());
        //LayoutParams : 设置view的属性
        LinearLayout.LayoutParams params = new LayoutParams(11, 11);
        //设置背景图片
        dots[i].setBackgroundResource(R.drawable.selector_dot);
        params.rightMargin = 5;//设置距离右边的距离
        //设置属性给view
        dots[i].setLayoutParams(params);
        //将view添加到点的容器中显示
        mLLDot.addView(dots[i]);
    }

    private void changed(int position) {
        position = position % carousellist.size();

        if (currentDot != null) {
            currentDot.setSelected(false);
        }

        //设置下一个点是白色的点
        dots[position].setSelected(true);
        //保存白色的点
        currentDot = dots[position];
    }

    /**
     * 返回正常的界面，可以返回一个布局id，也可以返回一个View
     */
    @Override
    public Object getContentView() {
        return R.layout.fragment_home;
    }





}


