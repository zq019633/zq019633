package com.enet.cn.myapplication.bean.HomeBean;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Info {


    @SerializedName("default")
    private List<Default> mdefault;




    private List<News> news;
    private List<Carousel> carousel;
    public void setDefault(List<Default>mdefault) {
        this.mdefault = mdefault;
    }
    public List<Default> getDefault() {
        return mdefault;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
    public List<News> getNews() {
        return news;
    }

    public void setCarousel(List<Carousel> carousel) {
        this.carousel = carousel;
    }
    public List<Carousel> getCarousel() {
        return carousel;
    }

}