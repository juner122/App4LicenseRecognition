package com.eb.geaiche.wxapi;


import android.content.Context;

import androidx.annotation.NonNull;

import com.juner.mvp.Configure;
import com.juner.mvp.bean.PayInfo;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//微信支付sign签名类
public class WXPayHelper {
    public IWXAPI api;
    private PayReq req;

    private String appId;
    private String prePayId;
    private String partNerId;
    private String partNerSecret;//商户密钥

    public WXPayHelper(Context context, String appId, String prePayId, String partNerId, String partNerSecret) {
        this.appId = appId;
        this.prePayId = prePayId;
        this.partNerId = partNerId;
        this.partNerSecret = partNerSecret;
        api = WXAPIFactory.createWXAPI(context, this.appId,false);
    }
    public WXPayHelper(Context context) {
        api = WXAPIFactory.createWXAPI(context, this.appId,false);
    }

    /**
     * 向微信服务器发起的支付请求
     */
    public void pay() {
        req = new PayReq();
        req.appId = appId;//APP-ID
        req.partnerId = partNerId;//    商户号
        req.prepayId = prePayId;//  预付款ID
        req.nonceStr = String.valueOf(System.nanoTime());                   //随机数
        req.timeStamp = String.valueOf(System.currentTimeMillis());   //时间戳
        req.packageValue = "Sign=WXPay";//固定值Sign=WXPay
        req.sign = getSign();//签名
        api.registerApp(Configure.APP_ID);
        api.sendReq(req);
    }


    /**
     * 微信支付
     * https://pay.weixin.qq.com/wiki/doc/api/app.php?chapter=9_12&index=2
     * <p>
     * appid 微信分配的公众账号ID
     * partnerid 微信支付分配的商户号
     * prepayid 微信返回的支付交易会话ID
     * noncestr 随机字符串，不长于32位。推荐随机数生成算法 https://pay.weixin.qq.com/wiki/doc/api/app.php?chapter=4_3
     * timestamp 时间戳，请见接口规则-参数规定 https://pay.weixin.qq.com/wiki/doc/api/app.php?chapter=4_2
     * packageX 暂填写固定值Sign=WXPay
     * sign 签名，详见签名生成算法
     * extData 额外的标记，未知
     */
    public void pay(PayInfo payInfo) {
        req = new PayReq();
        req.appId = payInfo.getPayInfo().getAppId();//appID
        req.partnerId = payInfo.getPayInfo().getPartnerid();//商户号
        req.prepayId = payInfo.getPayInfo().getPrepayId();//预支付交易会话ID
        req.nonceStr = payInfo.getPayInfo().getNoncestr();//随机字符串
        req.timeStamp = payInfo.getPayInfo().getTimestamp().toString();
        req.packageValue = "Sign=WXPay";//暂填写固定值Sign=WXPay
        req.sign = payInfo.getPayInfo().getPaySign();//签名

        api.sendReq(req);
    }



    @NonNull
    private String getSign() {
        Map<String, String> map = new HashMap<>();
        map.put("appid", req.appId);
        map.put("partnerid", req.partnerId);
        map.put("prepayid", req.prepayId);
        map.put("package", req.packageValue);
        map.put("noncestr", req.nonceStr);
        map.put("timestamp", req.timeStamp);

        ArrayList<String> sortList = new ArrayList<>();
        sortList.add("appid");
        sortList.add("partnerid");
        sortList.add("prepayid");
        sortList.add("package");
        sortList.add("noncestr");
        sortList.add("timestamp");
        Collections.sort(sortList);

        StringBuilder md5 = new StringBuilder();
        int size = sortList.size();
        for (int k = 0; k < size; k++) {
            if (k == 0) {
                md5.append(sortList.get(k)).append("=").append(map.get(sortList.get(k)));
            } else {
                md5.append("&").append(sortList.get(k)).append("=").append(map.get(sortList.get(k)));
            }
        }
        String stringSignTemp = md5+"&key="+partNerSecret;

        return Md5(stringSignTemp).toUpperCase();
    }

    private String Md5(String s) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] e = s.getBytes(StandardCharsets.UTF_8);
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(e);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }
            return new String(str);
        } catch (Exception var10) {
            return null;
        }
    }
}
