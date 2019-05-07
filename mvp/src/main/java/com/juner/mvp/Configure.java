package com.juner.mvp;

public class Configure {
    public static final String ALLIANCE_URL = "https://apps.xgxshop.com/app/";//联盟版+
    public static final String NEW_ORDER_URL = "https://business.xgxshop.com/app/";// 新干线版




    public static final boolean APP_ALLIANCE = true;//  联盟版
//    public static final boolean APP_ALLIANCE = false;//  新干线版


//    public static final String BaseUrl = "http://222.111.88.115:8081/app/";//线下
    public static final String BaseUrl = APP_ALLIANCE ? ALLIANCE_URL : NEW_ORDER_URL;//线上




    public static final int Goods_TYPE_1 = 1;//商城商品
    public static final int Goods_TYPE_3 = 3;//工时服务
    public static final int Goods_TYPE_4 = 4;//配件
    public static final int Goods_TYPE_5 = 5;//套卡
    public static final String Goods_TYPE = "Goods_TYPE";//套卡


    public static final String carNumberRecognition = "https://api03.aliyun.venuscn.com/ocr/car-license";//OCR文字识别-车牌识别
    public static final String carVinRecognition = "https://vin.market.alicloudapi.com/api/predict/ocr_vin";//OCR文字识别-车辆vin识别
    public static final String carVinInfo = "https://ali-vin.showapi.com/vin";//OCR文字识别-车辆vin识别
    public static final String LinePathView_url = "/sdcard/qm.png";//用户签名图片
    public static final int limit_page = 20;//分页数量

    //七牛
    public static final String accessKey = "3iizTx8hScpHRaaNDNwDMbLAIr8X6-sKT1SxaLV7";
    public static final String secretKey = "NFD6KB9rTLY4xecjUOELsyQZweK50GyGsVvv1C82";
    public static final String Domain = "http://qiniu.xgxshop.com/";//域名 图片上传
    public static final String bucket = "aayc";//图片上传 空间名

    public static final String Token = "Token";//Token
    public static final String Balance = "Balance";


    public static final String car_no = "car_no";//车牌号
    public static final String user_id = "user_id";//
    public static final String moblie = "moblie";//
    public static final String moblie_s = "moblie_s";//
    public static final String car_id = "car_id";//
    public static final String user_name = "user_name";//
    public static final String order_on = "order_on";//

    public static final String shop_name = "shop_name";//
    public static final String shop_address = "shop_address";//
    public static final String shop_phone = "shop_phone";//·
    public static final String shop_user_name = "shop_user_name";//
    public static final String shop_info = "shop_info";//

    public static final String act_tag = "act_tag";//


    public static final String brand = "brand";//
    public static final String brandModdel = "brandModdel";//


    public static final String show_fragment = "fragment";//
    public static final String JSON_CART = "JSON_CART";//
    public static final String JSON_ServerCART = "JSON_ServerCART";//
    public static final String CARID = "carid";//
    public static final String CARINFO = "carinfo";//


    public static final String isShow = "show";//
    public static final String setProject = "setProject";//
    public static final String valueId = "valueId";//
    public static final String goodName = "goodName";//

    public static final String ORDERINFO = "orderInfo";//
    public static final String ORDERINFOID = "orderInfoId";//
    public static final String QUERYBYCARINFO = "QueryByCarInfo";//


    public static final String isFixOrder = "isFixOrder";//是否是修改订单
    public static final String pick_servers = "pick_servers";//选择的服务列表
    public static final String Class = "Calss";//选择的服务列表的activity


}
