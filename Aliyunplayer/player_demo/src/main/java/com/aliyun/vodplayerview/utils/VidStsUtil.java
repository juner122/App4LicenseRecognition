package com.aliyun.vodplayerview.utils;

import android.os.AsyncTask;
import android.os.Build;

import com.alivc.player.VcPlayerLog;
import com.aliyun.vodplayer.BuildConfig;
import com.aliyun.vodplayer.media.AliyunVidSts;
import com.aliyun.vodplayer.utils.HttpClientUtil;
import com.aliyun.vodplayerview.playlist.vod.core.AliyunVodHttpCommon;

import org.json.JSONObject;

/**
 * Created by pengshuang on 31/08/2017.
 */
public class VidStsUtil {


    private static final String TAG = VidStsUtil.class.getSimpleName();

    public static AliyunVidSts getVidSts(String videoId) {

        if(!BuildConfig.DEBUG) {
            //Do not get STS in debug mode, developers fill in their own STS
            return null;
        }else{
                try {
                    //以前的连接地址"https://demo-vod.cn-shanghai.aliyuncs.com/voddemo/CreateSecurityToken?BusinessType=vodai&TerminalType=pc&DeviceModel=iPhone9,2&UUID=59ECA-4193-4695-94DD-7E1247288&AppVersion=1.0.0&VideoId=" + videoId"
                    String stsUrl = AliyunVodHttpCommon.getInstance().getVodStsDomain() +
                            "voddemo/CreateSecurityToken?BusinessType=vodai&TerminalType=pc&DeviceModel=" +
                            Build.DEVICE + "&UUID=" + AliyunVodHttpCommon.generateRandom() + "&AppVersion=" + BuildConfig.VERSION_NAME +
                            "&VideoId=" + videoId;
                    String response = HttpClientUtil.doGet(stsUrl);

                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject securityTokenInfo = jsonObject.getJSONObject("SecurityTokenInfo");
                    if (securityTokenInfo == null) {

                        VcPlayerLog.e(TAG, "SecurityTokenInfo == null ");
                        return null;
                    }

                    String accessKeyId = securityTokenInfo.getString("AccessKeyId");
                    String accessKeySecret = securityTokenInfo.getString("AccessKeySecret");
                    String securityToken = securityTokenInfo.getString("SecurityToken");
                    String expiration = securityTokenInfo.getString("Expiration");
                    VcPlayerLog.e(TAG, "accessKeyId = " + accessKeyId + " , accessKeySecret = " + accessKeySecret +
                            " , securityToken = " + securityToken);


                    AliyunVidSts vidSts = new AliyunVidSts();

                    vidSts.setVid(videoId);
                    vidSts.setAcId(accessKeyId);
                    vidSts.setAkSceret(accessKeySecret);
                    vidSts.setSecurityToken(securityToken);
                    return vidSts;

                } catch (Exception e) {
                    VcPlayerLog.e(TAG, "e = " + e.getMessage());
                    return null;
                }
            }


    }

    public interface OnStsResultListener {
        void onSuccess(String vid,String akid, String akSecret, String token);

        void onFail();
    }

    public static void getVidSts(final String vid,final OnStsResultListener onStsResultListener) {
        AsyncTask<Void, Void, AliyunVidSts> asyncTask = new AsyncTask<Void, Void, AliyunVidSts>() {

            @Override
            protected AliyunVidSts doInBackground(Void... params) {
                return getVidSts(vid);
            }

            @Override
            protected void onPostExecute(AliyunVidSts s) {
                if (s == null) {
                    onStsResultListener.onFail();
                } else {
                    onStsResultListener.onSuccess(s.getVid(),s.getAcId(), s.getAkSceret(), s.getSecurityToken());
                }
            }
        };
        asyncTask.execute();

        return;
    }


}
