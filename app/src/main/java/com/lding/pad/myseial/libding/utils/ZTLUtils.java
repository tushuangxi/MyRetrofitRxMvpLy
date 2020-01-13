package com.lding.pad.myseial.libding.utils;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

import com.lding.pad.myseial.R;

public class ZTLUtils {
    Activity activity;

    public ZTLUtils(Activity activity) {
        this.activity = activity;
    }

    public void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            this.activity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            this.activity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemStatusManager tintManager = new SystemStatusManager(this.activity);
            tintManager.setStatusBarTintEnabled(true);
            // 设置状态栏的颜色
            tintManager.setStatusBarTintResource(R.color.holo_purple);
            this.activity.getWindow().getDecorView().setFitsSystemWindows(true);
        }
    }
}
