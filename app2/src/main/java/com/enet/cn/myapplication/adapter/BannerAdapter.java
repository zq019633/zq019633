package com.enet.cn.myapplication.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.enet.cn.myapplication.bean.HomeBean.Carousel;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by programmer on 2016/9/8 0008.
 */
public class BannerAdapter extends StaticPagerAdapter {
    private ArrayList<String> imagePathList;
    private List<Carousel> carousel;


    public BannerAdapter(List<Carousel> carousel) {
        this.carousel = carousel;

    }


    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return  carousel.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = position % carousel.size();


        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

         String downUrl= carousel.get(position).getImagepath();
        ImageLoader.getInstance().displayImage(downUrl, imageView);    // 下载图片并显示

        container.addView(imageView);
        return imageView;
    }



    @Override
    public View getView(ViewGroup container, int position) {

        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.FIT_XY);

        String downUrl= carousel.get(position).getImagepath();
        ImageLoader.getInstance().displayImage(downUrl, view);    // 下载图片并显示

        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }





    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}