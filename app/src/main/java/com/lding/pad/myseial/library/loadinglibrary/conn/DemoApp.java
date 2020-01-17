package com.lding.pad.myseial.library.loadinglibrary.conn;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.lding.pad.myseial.libding.utils.LibLoader;
import com.lding.pad.myseial.library.cockroach.Cockroach;
import com.lding.pad.myseial.library.loadinglibrary.main.DaggerMainComponent;
import com.lding.pad.myseial.library.loadinglibrary.main.MainComponent;
import com.lding.pad.myseial.library.loadinglibrary.main.MainModule;
import com.lding.pad.myseial.library.loadinglibrary.splash.DaggerSplashComponent;
import com.lding.pad.myseial.library.loadinglibrary.splash.SplashComponent;
import com.lding.pad.myseial.library.loadinglibrary.splash.SplashModule;
import com.squareup.leakcanary.LeakCanary;


/**
 * Demo的Application
 */

public class DemoApp extends Application {

    private static Context mContext;

    private DemoComponent mDemoComponent; // 应用组件
    private MainComponent mMainComponent; // 主页组件
    private SplashComponent mSplashComponent; // 闪屏组件

    @Override
    public void onCreate() {
        super.onCreate();
        //获取context
        mContext = getApplicationContext();

        LibLoader.init(this);//接收主module传递的上下文，提供给lib内部需要context的工具类使用
        LeakCanary.install(this); // 内存泄露

        //安装 降低Android非必要crash
        toCockroach();
        //卸载 降低Android非必要crash
//        unCockroach();

        StrictMode.enableDefaults();

        // 应用组件初始化
        mDemoComponent = DaggerDemoComponent.builder()
                .demoModule(new DemoModule(this))
                .build();
    }

    private void unCockroach() {
        // 卸载代码
        Cockroach.uninstall();
    }

    private void toCockroach() {

        Cockroach.install(new Cockroach.ExceptionHandler() {
            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException
            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("Cockroach", thread + "\n" + throwable.toString());
                            throwable.printStackTrace();
                            Toast.makeText(mContext, "Exception Happend\n" + thread + "\n" + throwable.toString(), Toast.LENGTH_SHORT).show();
//                        throw new RuntimeException("..."+(i++));
                        } catch (Throwable e) {

                        }
                    }
                });
            }
        });
    }

    //创建一个静态的方法，以便获取context对象
    public static Context getContext(){
        return mContext;
    }

    // 获得应用组件
    @Nullable
    public DemoComponent demoComponent() {
        return mDemoComponent;
    }

    // 主页组件
    @Nullable
    public MainComponent mainComponent() {
        if (mMainComponent == null) {
            mMainComponent = DaggerMainComponent.builder()
                    .demoComponent(demoComponent())
                    .mainModule(new MainModule())
                    .build();
        }
        return mMainComponent;
    }

    // 闪屏组件
    @NonNull
    public SplashComponent splashComponent() {
        if (mSplashComponent == null) {
            mSplashComponent = DaggerSplashComponent.builder()
                    .demoComponent(demoComponent())
                    .splashModule(new SplashModule())
                    .build();
        }

        return mSplashComponent;
    }

    // 释放闪屏组件
    public void releaseSplashComponent() {
        mSplashComponent = null;
    }

    // 释放主页组件
    public void releaseMainComponent() {
        mMainComponent = null;
    }

    /**
     * 返回应用的Application
     *
     * @param context 上下文
     * @return 当前Application
     */
    @NonNull
    public static DemoApp app(@NonNull Context context) {
        return (DemoApp) context.getApplicationContext();
    }
}
