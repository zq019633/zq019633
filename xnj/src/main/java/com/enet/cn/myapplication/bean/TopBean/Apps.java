/**
 * Copyright 2016 aTool.org
 */
package com.enet.cn.myapplication.bean.TopBean;

import java.io.Serializable;

;

/**
 * Auto-generated: 2016-09-13 15:50:0
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class Apps  implements Serializable{
    public static final int STATUS_NOT_DOWNLOAD = 0;//没有下载
    public static final int STATUS_CONNECTING = 1;//
    public static final int STATUS_CONNECT_ERROR = 2;//正在下载
    public static final int STATUS_DOWNLOADING = 3;
    public static final int STATUS_PAUSED = 4;
    public static final int STATUS_DOWNLOAD_ERROR = 5;
    public static final int STATUS_COMPLETE = 6;
    public static final int STATUS_INSTALLED = 7;


    private int progress;

    private int status;
    private String downloadPerSize;

    private String id;
    private String name;
    private String imagepath;
    private String comment;
    private String installcount;
    private String size;
    private String imageurl;
    private String cate;
    private String url;
    private String uid;
    private String downloadurl;
    private String iosurl;
    private String imagename;
    private String pkgname;
    private boolean isProgress;

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    private boolean isDownload;

    public boolean isProgress() {
        return isProgress;
    }

    public void setProgress(boolean progress) {
        isProgress = progress;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
    public String getImagepath() {
        return imagepath;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getComment() {
        return comment;
    }

    public void setInstallcount(String installcount) {
        this.installcount = installcount;
    }
    public String getInstallcount() {
        return installcount;
    }

    public void setSize(String size) {
        this.size = size;
    }
    public String getSize() {
        return size;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
    public String getImageurl() {
        return imageurl;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }
    public String getCate() {
        return cate;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUid() {
        return uid;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }
    public String getDownloadurl() {
        return downloadurl;
    }

    public void setIosurl(String iosurl) {
        this.iosurl = iosurl;
    }
    public String getIosurl() {
        return iosurl;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }
    public String getImagename() {
        return imagename;
    }

    public void setPkgname(String pkgname) {
        this.pkgname = pkgname;
    }
    public String getPkgname() {
        return pkgname;
    }





    public String getDownloadPerSize() {
        return downloadPerSize;
    }

    public void setDownloadPerSize(String downloadPerSize) {
        this.downloadPerSize = downloadPerSize;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusText() {
        switch (status) {
            case STATUS_NOT_DOWNLOAD:
                return "Not Download";
            case STATUS_CONNECTING:
                return "Connecting";
            case STATUS_CONNECT_ERROR:
                return "Connect Error";
            case STATUS_DOWNLOADING:
                return "Downloading";
            case STATUS_PAUSED:
                return "Pause";
            case STATUS_DOWNLOAD_ERROR:
                return "Download Error";
            case STATUS_COMPLETE:
                return "Complete";
            case STATUS_INSTALLED:
                return "Installed";
            default:
                return "Not Download";
        }
    }

    public String getButtonText() {
        switch (status) {
            case STATUS_NOT_DOWNLOAD:
                return "下载";
            case STATUS_CONNECTING:
                return "取消";
            case STATUS_CONNECT_ERROR:
                return "Try Again";
            case STATUS_DOWNLOADING:
                return "Pause";
            case STATUS_PAUSED:
                return "Resume";
            case STATUS_DOWNLOAD_ERROR:
                return "Try Again";
            case STATUS_COMPLETE:
                return "Install";
            case STATUS_INSTALLED:
                return "UnInstall";
            default:
                return "Download";
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

}