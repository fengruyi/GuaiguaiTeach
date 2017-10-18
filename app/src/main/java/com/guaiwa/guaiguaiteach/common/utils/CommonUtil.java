package com.guaiwa.guaiguaiteach.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


import java.io.File;

/**
 * Created by 80151689 on 2017-10-17.
 */

public class CommonUtil {

    /**
     * 通过隐式意图调用系统安装程序安装APK
     */
    public static void install(Context context, String apkPath) {
        File file = new File(apkPath);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(
                    file),
                    "application/vnd.android.package-archive");
            context.startActivity(intent);
        }
    }
}
