package com.enet.cn.myapplication.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.enet.cn.myapplication.MyApplication;
import com.enet.cn.myapplication.R;
import com.enet.cn.myapplication.bean.HomeBean.Default;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by programmer on 2016/9/12 0012.
 */
public class CateGridViewAdapter extends BaseAdapter {

    private  List<Default> aDefault;

    public CateGridViewAdapter(List<Default> aDefault) {
        this.aDefault=aDefault;
    }


    @Override
    public int getCount() {
        return aDefault.size();
    }


    @Override
    public Object getItem(int position) {
        Default newBeans=null;
        if(aDefault!=null){
            newBeans = this.aDefault.get(position);
        }
        return newBeans;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.item_gridview_home, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_grid);
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_grid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Default newsbean= (Default) getItem(position);

      /*  if(position==0){
            convertView=View.inflate(MyApplication.getContext(),R.layout.stick_action,null);

        }else {*/
            holder.tv_title.setText(newsbean.getName());
            ImageLoader.getInstance().displayImage(newsbean.getImagepath(), holder.imageView);
      //  }

        return convertView;
    }

    static class ViewHolder {
        TextView tv_title;
        ImageView imageView;
    }

}
