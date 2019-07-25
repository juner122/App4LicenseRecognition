package com.kernal.smartvision.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kernal.smartvisionocr.utils.SharedPreferencesHelper;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteToPCTask extends AsyncTask<String, String, String> {
    private static final String BOUNDARY = "----WebKitFormBoundaryT1HoybnYeFOGFlBR";
    private int flag;
    private int ReturnHttpFile = 0;
    private Context context;
    private String error = "";
    private String returnFTPMessage;
    private String ActionName = "";
    public static String httpPath = "http://feedback.wintone.com.cn//emailServer/servlet/UploadFileTxt?fileName=" ;
    public WriteToPCTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Toast toast;
        View layout;
        TextView text;
        if (this.ReturnHttpFile == 1) {
            toast = new Toast(this.context);
            layout = LayoutInflater.from(this.context).inflate(this.context.getResources().getIdentifier("toast_layout", "layout", this.context.getPackageName()), (ViewGroup)null);
            text = (TextView)layout.findViewById(this.context.getResources().getIdentifier("showToastInformation", "id", this.context.getPackageName()));
            text.setText(this.context.getString(this.context.getResources().getIdentifier("upload_success", "string", this.context.getPackageName())));
            toast.setGravity(17, 0, 0);
            toast.setView(layout);
            toast.show();
            SharedPreferencesHelper.putString(this.context, "uploadOverFlag", "success");
        } else if (this.ReturnHttpFile == 2) {
            toast = new Toast(this.context);
            layout = LayoutInflater.from(this.context).inflate(this.context.getResources().getIdentifier("toast_layout", "layout", this.context.getPackageName()), (ViewGroup)null);
            text = (TextView)layout.findViewById(this.context.getResources().getIdentifier("showToastInformation", "id", this.context.getPackageName()));
            text.setText(this.context.getString(this.context.getResources().getIdentifier("URL_Failed", "string", this.context.getPackageName())));
            toast.setGravity(17, 0, 0);
            toast.setView(layout);
            toast.show();
            SharedPreferencesHelper.putString(this.context, "uploadOverFlag", "failed");
        } else if (this.ReturnHttpFile == 0) {
            toast = new Toast(this.context);
            layout = LayoutInflater.from(this.context).inflate(this.context.getResources().getIdentifier("toast_layout", "layout", this.context.getPackageName()), (ViewGroup)null);
            text = (TextView)layout.findViewById(this.context.getResources().getIdentifier("showToastInformation", "id", this.context.getPackageName()));
            text.setText(this.context.getString(this.context.getResources().getIdentifier("upload_File_failed", "string", this.context.getPackageName())));
            toast.setGravity(17, 0, 0);
            toast.setView(layout);
            toast.show();
            SharedPreferencesHelper.putString(this.context, "uploadOverFlag", "failed");
        }

    }

    @Override
    protected String doInBackground(String... params) {
        String url = SharedPreferencesHelper.getString(this.context, "URL", httpPath);
        this.ReturnHttpFile = this.upLoadFileByHttpProtocol(httpPath, params[0].substring(params[0].lastIndexOf("/") + 1, params[0].length()), params[0], params[1]);
        return "";
    }

    private static String generateFileName(String fileName) {
        DateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        String formatDate = format.format(new Date());
        return fileName + formatDate;
    }

    public int upLoadFileByHttpProtocol(String path, String fileName, String filePath, String txtcontent) {
        boolean var5 = false;

        byte returnMessage;
        try {
            URL url = new URL(path + fileName + "&txtcontent=" + txtcontent);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "text/html");
            BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            boolean var12 = false;

            int numReadByte;
            while((numReadByte = fileInputStream.read(bytes, 0, 1024)) > 0) {
                out.write(bytes, 0, numReadByte);
            }

            out.flush();
            fileInputStream.close();
            new DataInputStream(connection.getInputStream());
            returnMessage = 1;
            return returnMessage;
        } catch (Exception var14) {
            var14.printStackTrace();
            returnMessage = 0;
            return returnMessage;
        }
    }
}
