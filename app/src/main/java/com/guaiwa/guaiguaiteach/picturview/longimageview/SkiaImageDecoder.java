package com.guaiwa.guaiguaiteach.picturview.longimageview;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;

/**
 * Default implementation of {@link ImageDecoder}
 * using Android's {@link BitmapFactory}, based on the Skia library. This
 * works well in most circumstances and has reasonable performance, however it has some problems
 * with grayscale, indexed and CMYK images.
 */
public class SkiaImageDecoder implements ImageDecoder {

    private static final String FILE_PREFIX = "file://";//文件
    private static final String ASSET_PREFIX = FILE_PREFIX + "/android_asset/";//资源
    private static final String RESOURCE_PREFIX = ContentResolver.SCHEME_ANDROID_RESOURCE + "://";//资源前缀

    /***
     * 解码图像
     * @param context Application context. A reference may be held, but must be cleared on recycle.
     * @param uri URI of the image.
     * @return
     * @throws Exception
     */
    @Override
    public Bitmap decode(Context context, Uri uri) throws Exception {
        String uriString = uri.toString();
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap;
        options.inPreferredConfig = Bitmap.Config.RGB_565;//设置解码图像格式
        if (uriString.startsWith(RESOURCE_PREFIX)) {//判断是否为Drawable目录下文件
            Resources res;
            String packageName = uri.getAuthority();
            if (context.getPackageName().equals(packageName)) {
                res = context.getResources();
            } else {
                PackageManager pm = context.getPackageManager();
                res = pm.getResourcesForApplication(packageName);
            }

            int id = 0;
            List<String> segments = uri.getPathSegments();
            int size = segments.size();
            if (size == 2 && segments.get(0).equals("drawable")) {
                String resName = segments.get(1);
                id = res.getIdentifier(resName, "drawable", packageName);
            } else if (size == 1 && TextUtils.isDigitsOnly(segments.get(0))) {
                try {
                    id = Integer.parseInt(segments.get(0));
                } catch (NumberFormatException ignored) {
                }
            }

            bitmap = BitmapFactory.decodeResource(context.getResources(), id, options);
        } else if (uriString.startsWith(ASSET_PREFIX)) {
            String assetName = uriString.substring(ASSET_PREFIX.length());
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(assetName), null, options);
        } else if (uriString.startsWith(FILE_PREFIX)) {
            bitmap = BitmapFactory.decodeFile(uriString.substring(FILE_PREFIX.length()), options);
        } else {
            ContentResolver contentResolver = context.getContentResolver();
            bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, options);
        }
        if (bitmap == null) {
            throw new RuntimeException("图像区域解码器返回null，可能不支持该格式");
        }
        return bitmap;
    }
}
