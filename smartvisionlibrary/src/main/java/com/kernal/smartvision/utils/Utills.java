package com.kernal.smartvision.utils;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.Time;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by user on 2017/7/19.
 * 工具类
 */

public class Utills {
    private static ProgressDialog mypDialog;
    public  static boolean PaySucsess = false;
    public static String pictureName() {
        String str = "";
        Time t = new Time();
        t.setToNow();
        int year = t.year;
        int month = t.month + 1;
        int date = t.monthDay;
        int hour = t.hour;
        int minute = t.minute;
        int second = t.second;
        if(month < 10) {
            str = String.valueOf(year) + "0" + month;
        } else {
            str = String.valueOf(year) + String.valueOf(month);
        }

        if(date < 10) {
            str = str + "0" + date;
        } else {
            str = str + String.valueOf(date);
        }

        if(hour < 10) {
            str = str + "0" + hour;
        } else {
            str = str + String.valueOf(hour);
        }
        if(minute < 10) {
            str = str + "0" + minute;
        } else {
            str = str + String.valueOf(minute);
        }
        if(second < 10) {
            str = str + "0" + second;
        } else {
            str = str + String.valueOf(second);
        }
        return str;
    }

    public static boolean CheckInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();
        if (net_info != null) {
            for (int i = 0; i < net_info.length; i++) {
                // 判断获得的网络状态是否是处于连接状态
                if (net_info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
    @TargetApi(19)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void initImageLoader(Context context){
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                // 设置下载的图片是否缓存在内存中
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisk(false)
                // 设置图片在下载前是否重置，复位
                .resetViewBeforeLoading(false)
                // 保留Exif信息
                .considerExifParams(false)
                // 设置图片的解码类型
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context.getApplicationContext())
                //即保存的每个缓存文件的最大长宽
                .memoryCacheExtraOptions(400, 400)
                //设置缓存的详细信息，最好不要设置这个
                // 线程池内加载的数量
                .threadPoolSize(5)
                //线程优先级  default Thread.NORM_PRIORITY - 1
                .threadPriority(Thread.NORM_PRIORITY)
                // default FIFO
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCacheSizePercentage(13)
                // default
                .diskCache(new UnlimitedDiskCache(StorageUtils.getCacheDirectory(context.getApplicationContext(),true)))
                //硬盘缓存50MB
                .diskCacheSize(50 * 1024 * 1024)
                //缓存的File数量
                .diskCacheFileCount(100)
                //将保存的时候的URI名称用HASHCODE加密
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                // default
                .imageDownloader(new BaseImageDownloader(context.getApplicationContext()))
                // default
                .imageDecoder(new BaseImageDecoder(false))
                // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                // default
                .defaultDisplayImageOptions(imageOptions)
                .build();
        ImageLoader.getInstance().init(config);

    }

    //旋转图片
    public static void rotatePic(String path,int rotation,Context context){
        System.out.println("http存储路径:"+path);
        File file=new File(path);
        if(!file.exists()){
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Matrix matrix = new Matrix();
        matrix.reset();
        rotation = 360 - rotation;
        if (rotation == 90) {
            matrix.setRotate(90);
        } else if (rotation == 180) {
            matrix.setRotate(180);
        } else if (rotation == 270) {
            matrix.setRotate(270);
        }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(),
                bitmap.getHeight(),matrix,true);

        String PATH = Environment.getExternalStorageDirectory().toString()
                + "/DCIM/Camera/";

        File dirs = new File(PATH);
        if (!dirs.exists()) {
            dirs.mkdirs();
        }

        file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(file));

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" +path )));


    }


    /**
     * @param data1
     * @param type  0 为保存全图 目的为了报错显示图片，其他为了点击拍照按钮，传入正图给核心
     */
    public static String savePicture(byte[] data1, int type, Camera.Size size, int rotation) {
        if(type==0){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inPurgeable = true;
            options.inInputShareable = true;
            YuvImage yuvimage = new YuvImage(data1, ImageFormat.NV21, size.width, size.height, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            yuvimage.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, baos);
            String picPathString2 = "";
            String PATH = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";
            String name =  com.kernal.smartvisionocr.utils.Utills.pictureName();
            picPathString2 = PATH + "smartVisitionComplete" + name + ".jpg";
            File dirs = new File(PATH);
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            File file = new File(picPathString2);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(picPathString2);
                outStream.write(baos.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    outStream.close();
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return picPathString2;
        }else{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inPurgeable = true;
            options.inInputShareable = true;
            YuvImage yuvimage = new YuvImage(data1, ImageFormat.NV21, size.width, size.height, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            yuvimage.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, baos);
            Bitmap bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.size(), options);

          /*  bitmap = Bitmap.createBitmap(bitmap, regionPos[0], regionPos[1], regionPos[2]-regionPos[0],
                    regionPos[3]-regionPos[1]);*/

            Matrix matrix = new Matrix();
            matrix.reset();
            if (rotation == 90) {
                matrix.setRotate(90);
            } else if (rotation == 180) {
                matrix.setRotate(180);
            } else if (rotation == 270) {
                matrix.setRotate(270);
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            String picPathString2 = "";
            String PATH = Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/";
            String name =  com.kernal.smartvisionocr.utils.Utills.pictureName();
            picPathString2 = PATH + "smartVisitionComplete" + name + ".jpg";
            File file = new File(picPathString2);
            if (!file.exists()) {
                file.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(bitmap!=null&&!bitmap.isRecycled()){
                    bitmap.recycle();
                    bitmap=null;
                }
            }
            return picPathString2;
        }
    }

}
