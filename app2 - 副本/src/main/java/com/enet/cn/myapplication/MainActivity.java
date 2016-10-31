package com.enet.cn.myapplication;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.enet.cn.myapplication.adapter.Myadapter;
import com.enet.cn.myapplication.fragment.HomePage;
import com.enet.cn.myapplication.fragment.RcommendPage;
import com.enet.cn.myapplication.fragment.contributePage;

import java.util.ArrayList;

import static android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends AppCompatActivity {

    private RadioGroup rg;
    private RadioButton home;
    private RadioButton recommend;
    private RadioButton contribute;
    private ViewPager viewPager;
    private TextView tv_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initView();
        initData();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        rg = (RadioGroup) findViewById(R.id.tab_menu);
        home = (RadioButton) findViewById(R.id.home_page);
        viewPager = (ViewPager) findViewById(R.id.vp);
        recommend = (RadioButton) findViewById(R.id.recommend_page);
        contribute = (RadioButton) findViewById(R.id.contribute_page);
        tv_bar = (TextView) findViewById(R.id.title_bar);

        ArrayList list = new ArrayList();
        list.add(new HomePage());
        list.add(new RcommendPage());
        list.add(new contributePage());


        Myadapter myadapter = new Myadapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(myadapter);


        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.home_page:
                        viewPager.setCurrentItem(0, false);
                        tv_bar.setText("西南角");

                        break;
                    case R.id.recommend_page:
                        viewPager.setCurrentItem(1, false);
                        tv_bar.setText("推荐");
                        break;
                    case R.id.contribute_page:
                        viewPager.setCurrentItem(2, false);
                        tv_bar.setText("投稿");
                        break;

                }

            }
        });


    }


    /**
     * 初始化数据
     */
    private void initData() {
    }
}
