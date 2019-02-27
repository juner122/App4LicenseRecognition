package com.aliyun.vodplayerview.constants;

/**
 * 播放参数, 包含:
 * vid, vidSts, akId, akSecre, scuToken
 */
public class PlayParameter {


    /**
     * type, 用于区分播放类型, 默认为vidsts播放
     * vidsts: vid类型
     * localSource: url类型
     */
    public static String PLAY_PARAM_TYPE = "localSource";

    /**
     * vid, 初始为: 9fb028c29acb421cb634c77cf4ebe078
     */
    public static String PLAY_PARAM_VID = "b09e305248f3467ab1fa69b8ff2b352e";

    /**
     * akId
     */
    public static String PLAY_PARAM_AK_ID = "LTAI5e50w6G5ilWV";

    /**
     * akSecre
     */
    public static String PLAY_PARAM_AK_SECRE = "XQpPdLVIlypke2rjdOc1MEPeGaPJxl";

    /**
     * scuToken
     */
    public static String PLAY_PARAM_SCU_TOKEN = "";

    /**
     * url类型的播放地址, 初始为:http://player.alicdn.com/video/aliyunmedia.mp4
     */
    public static String PLAY_PARAM_URL = "http://player.alicdn.com/video/aliyunmedia.mp4";

}
