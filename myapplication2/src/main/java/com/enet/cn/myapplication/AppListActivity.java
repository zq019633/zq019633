package com.enet.cn.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.enet.cn.myapplication.R;

/**
 * Created by programmer on 2016/9/27 0027.
 */
public class AppListActivity  extends AppCompatActivity{

    public static final class TYPE {
        public static final int TYPE_LISTVIEW = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

         findViewById(R.id.listView);
    }
}
