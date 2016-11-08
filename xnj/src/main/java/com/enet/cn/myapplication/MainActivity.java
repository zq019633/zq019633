package com.enet.cn.myapplication;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.enet.cn.myapplication.Activity.DownloadManagerActivity;
import com.enet.cn.myapplication.adapter.Myadapter;
import com.enet.cn.myapplication.fragment.HomePage;
import com.enet.cn.myapplication.fragment.RcommendPage;
import com.enet.cn.myapplication.fragment.contributePage;

import java.util.ArrayList;

import static android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioGroup rg;
    private RadioButton home;
    private RadioButton recommend;
    private RadioButton contribute;
    private ViewPager viewPager;
    private TextView tv_bar;
    private Button openManager;

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
        openManager = (Button) findViewById(R.id.openManager);
        openManager.setVisibility(View.VISIBLE);
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
        openManager.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, DownloadManagerActivity.class));
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确认退出西南角吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        MainActivity.this.finish();

                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();

}
}
