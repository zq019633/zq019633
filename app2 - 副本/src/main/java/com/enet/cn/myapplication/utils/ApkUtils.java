package com.enet.cn.myapplication.utils;

import android.content.Intent;
import android.net.Uri;

import com.enet.cn.myapplication.MyApplication;

public class ApkUtils {

	/**
	 * 安装apk
	 * @param apkFilePath apk所在路径
	 */
	public static void install(String apkFilePath){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//指定apk文件路径,安装apk
		intent.setDataAndType(Uri.parse("file://"+apkFilePath),"application/vnd.android.package-archive");
		MyApplication.getContext().startActivity(intent);
	}
}
