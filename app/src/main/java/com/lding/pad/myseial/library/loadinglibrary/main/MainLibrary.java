package com.lding.pad.myseial.library.loadinglibrary.main;

import android.os.SystemClock;
import java.util.Random;

/**
 * 主页面的加载库
 */
public class MainLibrary {
    public MainLibrary(Random random) {
        SystemClock.sleep(random.nextInt(5000));
    }

    public String initializedString() {
        return "初始化" + MainLibrary.class.getSimpleName();
    }
}
