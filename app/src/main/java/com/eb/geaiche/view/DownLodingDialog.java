package com.eb.geaiche.view;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.eb.geaiche.R;
import com.eb.geaiche.activity.MainActivity;
import com.eb.geaiche.api.ApiLoader;
import com.eb.geaiche.api.RxSubscribe;
import com.eb.geaiche.util.FileUtil;
import com.eb.geaiche.util.ToastUtils;
import com.juner.mvp.Configure;
import com.juner.mvp.bean.NullDataEntity;
import com.juner.mvp.bean.VersionInfo;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;


public class DownLodingDialog extends Dialog {

    private Context context;


    private DownLodingDialog.ClickListenerInterface clickListenerInterface;

    public void setClicklistener(DownLodingDialog.ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    public interface ClickListenerInterface {


        void doCancel();
    }

    String string = "";
    String url;
    String path;//下载文件的存储绝对路径
    String versionName;//app版本号


    TextView tv_title, tv_text, tv_download_info;
    View ll_dlInfo;

    ProgressBar progressBar, pb3;
    View ll;

    BaseDownloadTask downloadTask;//下载task

    boolean isDowning;//是否开始下载

    public DownLodingDialog(Context context) {
        super(context, R.style.my_dialog);
        this.context = context;

    }


    /**
     * @param str       版本更新信息
     * @param url       apk下载url
     * @param appVision 版本号
     */
    public DownLodingDialog(Context context, String str, String url, String appVision) {
        super(context, R.style.my_dialog);
        this.context = context;
        string = str;
        this.versionName = appVision;
        this.url = url;


        String filePath;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//外部存储卡
            filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        } else {
            filePath = Environment.getExternalStorageDirectory().getPath();
        }

        path = filePath + File.separator + "geaiche" + "-v" + appVision + ".apk";


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();


        //初始化下载
        FileDownloader.setup(context);
        downloadTask = FileDownloader.getImpl().create(url);


    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_down_loading, null);
        setContentView(view);

        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        pb3 = view.findViewById(R.id.pb3);//
        ll = view.findViewById(R.id.ll);
        tv_text = view.findViewById(R.id.tv_text);
        tv_title = view.findViewById(R.id.title);
        progressBar = view.findViewById(R.id.pb);
        ll_dlInfo = view.findViewById(R.id.ll_dl_info);
        tv_download_info = view.findViewById(R.id.tv_download_info);

        tv_text.setText(Html.fromHtml(string));


        tv_confirm.setOnClickListener(new clickListener());
        tv_cancel.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);

        setCancelable(false);


        progressBar.setMax(1000);
    }


    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_confirm:
                    if (FileUtil.isExistence(path)) {//是否已存在
                        openAPK();
                    } else {
                        if (!isDowning)//防止多次点击
                        {
//                            startDL();
                            updateAppLog();
                        }
                    }
                    break;
                case R.id.tv_cancel:
                    FileDownloader.getImpl().pauseAll();

                    clickListenerInterface.doCancel();
                    break;
            }
        }

    }


    //记录用户更新
    private void updateAppLog() {

        new ApiLoader(context).updateAppLog(versionName).subscribe(new RxSubscribe<String>(context, true) {
            @Override
            protected void _onNext(String nullDataEntity) {
                startDL();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast("更新失败，请重试！" + message);
            }
        });
    }

    //开始下载
    private void startDL() {
        progressBar.setVisibility(View.VISIBLE);

        pb3.setVisibility(View.VISIBLE);

        downloadTask
                .setPath(path)//下载文件的存储绝对路径
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        tv_title.setText("下载中");
                        tv_text.setVisibility(View.GONE);
                        ll_dlInfo.setVisibility(View.VISIBLE);
                        tv_download_info.setText(soFarBytes + "B/" + totalBytes + "B");
                        isDowning = true;
                        pb3.setVisibility(View.GONE);


                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {


                        int progress = (int) ((new BigDecimal((float) soFarBytes / totalBytes).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue()) * 1000);

                        progressBar.setProgress(progress);

                        tv_download_info.setText(soFarBytes + "B/" + totalBytes + "B");
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {


                        openAPK();
                        progressBar.setProgress(1000);
                        tv_title.setText("下载完成");

                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();

        isDowning = true;
    }


    /**
     * 安装apk
     */
    private void openAPK() {


        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = (new File(path));
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //参数1:上下文, 参数2:Provider主机地址 和配置文件中保持一致,参数3:共享的文件
        Uri apkUri;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//版本在7.0以上是不能直接通过uri访问的
            apkUri = FileProvider.getUriForFile(context, "com.eb.geaiche.fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            apkUri = Uri.fromFile(file);
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


}