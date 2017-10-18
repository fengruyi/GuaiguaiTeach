package com.guaiwa.guaiguaiteach.common.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import com.guaiwa.guaiguaiteach.base.ResultData;
import com.guaiwa.guaiguaiteach.common.download.DownLoadTask;
import com.guaiwa.guaiguaiteach.common.download.DownloadManager;
import com.guaiwa.guaiguaiteach.config.FileConstant;

/**
 * Created by 80151689 on 2017-10-17.
 */

public class UpgradeManager {
    String apkUrl = "https://www.qhfax.com/path/down.myapp.com/AndroidQhfax.apk";
    String savePath = FileConstant.APK_PATH;
    UpgradeListener upgradeListener;
    Activity activity;

    public UpgradeManager(Activity activity) {
        this.activity = activity;
    }

    public interface UpgradeListener {
        void notifyUpgrade(ResultData resultData);
    }

    public void checkUpgrade(UpgradeListener upgradeListener) {
        this.upgradeListener = upgradeListener;
        getApkUpgradeInfo();
    }

    //获取服务器apk最新版本信息
    private void getApkUpgradeInfo() {
        new AlertDialog.Builder(activity).setTitle("升级提示")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        downloadApk();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void downloadApk() {
        DownloadManager.getInstance().startFileDownload(apkUrl, savePath, new DownLoadTask.DownLoadListener() {
            @Override
            public void updateProgress(long total, long current) {

            }

            @Override
            public void onSuccess() {
                Logger.d("UpgradeManager", "download onSuccess:");
                CommonUtil.install(activity, savePath);
            }

            @Override
            public void onFailue(Throwable e) {
                Logger.d("UpgradeManager", "download failue:" + e);
            }
        });
    }


}
