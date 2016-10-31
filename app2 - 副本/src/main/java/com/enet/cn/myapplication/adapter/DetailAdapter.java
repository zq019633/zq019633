package com.enet.cn.myapplication.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.enet.cn.myapplication.MyApplication;
import com.enet.cn.myapplication.R;
import com.enet.cn.myapplication.bean.TopBean.Apps;
import com.enet.cn.myapplication.listener.OnItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by programmer on 2016/9/13 0013.
 */
public class DetailAdapter extends BaseAdapter {

    private OnItemClickListener mListener;
    private List<Apps> apps;




    public DetailAdapter(List<Apps> apps) {
        this.apps = apps;


    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apps.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_app, null);
            holder = new ViewHolder();
            holder.text_top = (TextView) convertView.findViewById(R.id.app_top);
            holder.imageView1 = (ImageView) convertView.findViewById(R.id.apps_iv1);
            holder.imageView2 = (ImageView) convertView.findViewById(R.id.apps_iv2);
            holder.textView = (TextView) convertView.findViewById(R.id.apps_tv);


            holder.btnDownload = (Button) convertView.findViewById(R.id.btnDownload);
            holder.tvDownloadPerSize = (TextView) convertView.findViewById(R.id.tv_download_number);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);




            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Apps appInfo = apps.get(position);

        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("11111","我被点击了");
                if (mListener != null) {

                    mListener.onItemClick(v, position,appInfo);

                }
            }
        });
        if (position > 2) {
            holder.text_top.setVisibility(View.VISIBLE);
            holder.text_top.setTextSize(15);
            holder.text_top.setText(position + "");
        } else {
            holder.text_top.setVisibility(View.INVISIBLE);
        }
        if (position == 0) {
            holder.imageView1.setVisibility(View.VISIBLE);
        } else {
            holder.imageView1.setVisibility(View.INVISIBLE);
        }
        holder.textView.setText(appInfo.getName());
        ImageLoader.getInstance().displayImage(appInfo.getImagepath(), holder.imageView2);


        return convertView;
    }


    public final static class ViewHolder{
       public TextView text_top;
        public   ImageView imageView1;
        public   ImageView imageView2;
        public   TextView textView;


        public   Button btnDownload;
        public   TextView tvDownloadPerSize;
        public  ProgressBar progressBar;
        public  TextView tvStatus;




    }
}
