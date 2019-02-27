package com.aliyun.vodplayerview.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import com.alivc.player.VcPlayerLog;

public class BaseActivity extends AppCompatActivity {


    protected boolean isStrangePhone() {
        boolean strangePhone = "mx5".equalsIgnoreCase(Build.DEVICE)
            || "Redmi Note2".equalsIgnoreCase(Build.DEVICE)
            || "Z00A_1".equalsIgnoreCase(Build.DEVICE)
            || "hwH60-L02".equalsIgnoreCase(Build.DEVICE)
            || "hermes".equalsIgnoreCase(Build.DEVICE)
            || ("V4".equalsIgnoreCase(Build.DEVICE) && "Meitu".equalsIgnoreCase(Build.MANUFACTURER))
            || ("m1metal".equalsIgnoreCase(Build.DEVICE) && "Meizu".equalsIgnoreCase(Build.MANUFACTURER));

        VcPlayerLog.e("lfj1115 ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone);
        return strangePhone;
    }
}
