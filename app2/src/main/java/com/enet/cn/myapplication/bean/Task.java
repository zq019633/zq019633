package com.enet.cn.myapplication.bean;

/**
 * Created by programmer on 2016/10/8 0008.
 */
public class Task {
    private String name;
    private boolean isDownload;
    private int progress;

    public Task() {}

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
