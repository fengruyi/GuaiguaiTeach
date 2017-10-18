package com.guaiwa.guaiguaiteach.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 80151689 on 2017-10-18.
 */

public class FileUtil {

    public static void checkFile(File file) {
        if (file != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    /**
     * 保存多媒体数据为文件.
     *
     * @param data     多媒体数据
     * @param fileName 保存文件名
     * @return 保存成功或失败
     */
    public static boolean save2File(InputStream data, String fileName) {
        File file = new File(fileName);
        FileOutputStream fos = null;
        try {
            // 文件或目录不存在时,创建目录和文件.
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            // 写入数据
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len;
            while ((len = data.read(b)) > -1) {
                fos.write(b, 0, len);
            }
            fos.close();

            return true;
        } catch (IOException ex) {

            return false;
        }
    }

}
