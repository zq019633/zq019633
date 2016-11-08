package com.enet.cn.myapplication.utils;

import android.Manifest;

import com.enet.cn.myapplication.MyApplication;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpOptions;

/**
 * Created by programmer on 2016/10/17 0017.
 */
public class checkPermissions {
    public static void Permission() {
        Acp.getInstance(MyApplication.getContext()).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        //以下为自定义提示语、按钮文字
                        .setDeniedMessage("您拒绝存储权限申请，下载功能将不能正常使用，您可以去设置页面重新授权")
                        .setDeniedCloseBtn("关闭")
                        .setDeniedSettingBtn("设置权限")
                        .setRationalMessage("下载功能需要您授权，否则将不能正常使用")
                        .setRationalBtn("我知道了")
                        .build(),
                null);
    }
}
