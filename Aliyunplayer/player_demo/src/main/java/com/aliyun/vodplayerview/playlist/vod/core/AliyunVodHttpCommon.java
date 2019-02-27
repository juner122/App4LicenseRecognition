package com.aliyun.vodplayerview.playlist.vod.core;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.UUID;

/**
 * Created by Mulberry on 2017/11/2.
 */

public class AliyunVodHttpCommon {

    /**
     * sts host
     */
    private static String VOD_STS_DOMAIN = "https://demo-vod.cn-shanghai.aliyuncs.com/";
    private static String VOD_DOMAIN = "https://vod.cn-shanghai.aliyuncs.com/";
    public static final String  HTTP_METHOD = "GET";

    private static AliyunVodHttpCommon instance = null;

    private AliyunVodHttpCommon(){

    }

    public static AliyunVodHttpCommon getInstance(){
        if(instance == null){
            synchronized (AliyunVodHttpCommon.class){
                if(instance == null){
                    instance = new AliyunVodHttpCommon();
                }
            }
        }
        return instance;
    }

    public static class Action{
        public static final String CREATE_UPLOAD_IMAGE = "CreateUploadImage";
        public static final String CREATE_UPLOAD_VIDEO = "CreateUploadVideo";
        public static final String REFRESH_UPLOAD_VIDEO = "RefreshUploadVideo";
        public static final String GET_VIDEO_LIST = "GetVideoList";
    }

    public static class Status{
        public static final String NORMAL = "Normal";
    }

    public  static class ImageType{
        public static final String IMAGETYPE_COVER = "cover";
        public static final String IMAGETYPE_WATERMARK = "watermark";
    }

    public static class ImageExt{
        public static final String IMAGEEXT_PNG = "png";
        public static final String IMAGEEXT_JPG = "jpg";
        public static final String IMAGEEXT_JPEG = "jpeg";
    }

    public static class Format{
        public static final String FORMAT_JSON = "json";
        public static final String FORMAT_XML = "xml";
    }

    public static class CateId {
        // 推荐视频分类, 便于演示的视频,  值固定写死 
        public static final String CATEID = "472183517";
    }

    public static final String COMMON_API_VERSION = "2017-03-21";
    public static final String COMMON_TIMESTAMP = generateTimestamp();

    public static final String COMMON_SIGNATURE = "HMAC-SHA1";
    public static final String COMMON_SIGNATURE_METHOD = "HMAC-SHA1";
    public static final String COMMON_SIGNATUREVERSION = "1.0";
    public static final String COMMON_SIGNATURE_NONCE = generateRandom();
    public static final String COMON_NO_TRANSCODEMODE = "NoTranscode";
    public static final String COMON_FAST_TRANSCODEMODE = "FastTranscode";


    /*生成当前UTC时间戳Time*/
    public static String generateTimestamp() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    public static String generateRandom() {
        String signatureNonce = UUID.randomUUID().toString();
        return signatureNonce;
    }

    public String getVodDomain() {
        return VOD_DOMAIN;
    }

    public void setVodDomain(String vodDomain){
        if(TextUtils.isEmpty(vodDomain)){
            return ;
        }
        VOD_DOMAIN = vodDomain;
    }

    public String getVodStsDomain() {
        return VOD_STS_DOMAIN;
//        return VOD_DOMAIN;
    }

    public void setVodStsDomain(String vodStsDomain) {
        if(TextUtils.isEmpty(vodStsDomain)){
            return ;
        }
        VOD_STS_DOMAIN = vodStsDomain;
    }

}
