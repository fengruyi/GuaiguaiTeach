package com.guaiwa.guaiguaiteach.common.download;


import android.os.StatFs;
import android.text.TextUtils;

import com.facebook.common.internal.Closeables;
import com.guaiwa.guaiguaiteach.common.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * Created by 80151689 on 2016-12-12.
 * 统一保存文件任务，耗时任务，需要在任务线程调用
 */
public class CommonSaveFileTask {

    private static final String TAG = CommonSaveFileTask.class.getSimpleName();
    public static final int SAVE_SUCCESS = 0;
    public static final int SAVE_FAILED_STORAGE_FULL = 1;
    public static final int SAVE_FAILED_LOW_MEMORY = 2;
    public static final int SAVE_FAILED_COMPRESS_FAILED = 3;
    public static final int CREATE_FILE_FAILED = 4;
    private int whatMsg = -1;
    private String savePath;//文件保存路径
    private String errorMsg;//失败信息

    private CommonSaveFileTask() {

    }

    public interface ProgressHandler {
        void updateProgress(long total, long current);
    }

    public interface IUnZipListener {
        void unzipSuccess();
    }

    public static CommonSaveFileTask newInstance() {
        return new CommonSaveFileTask();
    }

    public String getSavePath() {
        return savePath;
    }

    public String getErrorMsg() {
        switch (whatMsg) {
            case SAVE_FAILED_STORAGE_FULL:
                errorMsg = "not enough storage available";
                break;
            case SAVE_FAILED_COMPRESS_FAILED:
                errorMsg = "error occurred while save picture";
                break;
            case CREATE_FILE_FAILED:
                errorMsg = "error  occurred while create file";
                break;
        }
        return errorMsg;
    }

    public int getWhatMsg() {
        return whatMsg;
    }


    /**
     * @param inputStream  文件输入流
     * @param fileFullPath 文件绝对路径名，包括文件后缀名
     */
    public boolean save(InputStream inputStream, String fileFullPath, long totalSize) {
        return save(inputStream, fileFullPath, totalSize, null);
    }

    /**
     * @param inputStream  文件输入流
     * @param fileFullPath 文件绝对路径名，包括文件后缀名
     */
    public boolean save(InputStream inputStream, String fileFullPath, long totalSize, ProgressHandler progressHandler) {
        savePath = fileFullPath;
        OutputStream outputStream = null;
        File createFile = new File(fileFullPath);
        FileUtil.checkFile(createFile);
        String fileName = createFile.getName();
        String tempfilePath = fileFullPath.replace(fileName.substring(fileName.lastIndexOf(".") + 1), "tt");
        File tempFile = new File(tempfilePath);
        try {
            if (checkCreateFileOk(totalSize, createFile.getParent())) {
                byte[] fileReader = new byte[2048];
                long fileSizeDownloaded = 0;
                outputStream = new FileOutputStream(tempfilePath);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    if (progressHandler != null) {
                        progressHandler.updateProgress(totalSize, fileSizeDownloaded);
                    }

                }
                outputStream.flush();
                tempFile.renameTo(createFile);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            whatMsg = CREATE_FILE_FAILED;
            return false;
        } finally {
            try {
                Closeables.closeQuietly(inputStream);
                Closeables.close(outputStream, true);
            } catch (IOException e) {
            }
        }
    }


    /**
     * @param fileSizeByte 将要保存文件的大小
     * @param dir          文件根目录
     * @return
     */
    private boolean checkCreateFileOk(long fileSizeByte, String dir) {
        File file = new File(dir);
        if (!file.exists() && !file.mkdirs()) {
            whatMsg = CREATE_FILE_FAILED;
            return false;
        }
        StatFs statFs = new StatFs(dir);
        long blockSize = statFs.getBlockSize();
        long blocks = statFs.getAvailableBlocks();
        long availableSpare = (blocks * blockSize);
        if (fileSizeByte < availableSpare) {
            return true;
        } else {
            whatMsg = SAVE_FAILED_STORAGE_FULL;
            return false;
        }
    }

    /**
     * 资源包解压，以zipFile文件名为目录名
     *
     * @param zipFile    zip文件全路径名
     * @param folderPath 解压目录
     * @return
     */
    public synchronized boolean upZipFileForSolution(String zipFile, String folderPath) {
        String dir = zipFile.substring(zipFile.lastIndexOf("/") + 1, zipFile.indexOf(".zip"));
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            try {
                boolean flag = true;
                String folderName = "";
                ZipFile zf = new ZipFile(zipFile);
                for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
                    ZipEntry entry = ((ZipEntry) entries.nextElement());
                    if (flag) {
                        folderName = entry.getName();
                        if (!TextUtils.isEmpty(folderName)) {
                            if (folderName.contains(File.separator)) {
                                folderName = folderName.substring(0, folderName.indexOf(File.separator));
                            }
                        }
                        flag = false;
                    }
                    in = zf.getInputStream(entry);
                    String str;
                    //避免拼接中出现斜杠
                    if (folderPath.endsWith("/")) {
                        str = folderPath + entry.getName();
                    } else {
                        str = folderPath + File.separator + entry.getName();
                    }
                    str = str.replace(folderName, dir);
                    File desFile = new File(str);
                    if (!desFile.exists() && str.lastIndexOf('/') != (str.length() - 1)) {
                        File fileParentDir = desFile.getParentFile();
                        if (!fileParentDir.exists()) {
                            fileParentDir.mkdirs();
                        }
                        desFile.createNewFile();
                        out = new FileOutputStream(desFile);
                        byte buffer[] = new byte[1024];
                        int realLength;
                        while ((realLength = in.read(buffer)) > 0) {
                            out.write(buffer, 0, realLength);
                        }
                    }
                }

                zf.close();

                return true;
            } catch (Exception e) {
                return false;
            } finally {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            }
        } catch (Exception e) {
            return false;
        }
    }
}
