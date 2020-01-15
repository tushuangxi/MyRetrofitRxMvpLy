package com.lding.pad.myseial.library.loadinglibrary.splash;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.lding.pad.myseial.R;
import com.lding.pad.myseial.libding.rerxmvp.base.BaseActivity;
import com.lding.pad.myseial.libding.rerxmvp.base.BasePermissionsAndStackActivity;
import com.lding.pad.myseial.libding.rerxmvp.view.GetListRspActivity;
import com.lding.pad.myseial.libding.utils.SharedPrefsUtil;
import com.lding.pad.myseial.libding.utils.UiUtil;
import com.lding.pad.myseial.libding.utils.XPermission;
import com.lding.pad.myseial.libding.utils.ZTLUtils;
import com.lding.pad.myseial.library.loadinglibrary.conn.DemoApp;
import com.lding.pad.myseial.library.loadinglibrary.main.MainActivity;
import com.lding.pad.myseial.library.testloading.GuideActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import javax.inject.Named;
import dagger.Lazy;
import dagger.internal.Preconditions;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 启动页面, 初始化需要长时间加载的库
 * <p>
 * Created by wangchenlong on 16/7/27.
 * 在Android项目的应用启动前, 一般都需要加载若干功能库或者发送网络请求, 这些操作需要在首页加载前完成, 因此多数应用选择添加首屏广告或者Logo.
 * 既能提供充足的加载时间, 又能赚取商业利润和产品曝光. 最优的方案是根据耗时任务需要的时间, 设置首屏的显示时间. 本文使用Dagger与RxJava控制首页的显示时间.
 *
 *  //使用Dagger+RxJava的形式是处理网络请求的优秀做法. 应用的启动页处理耗时的数据加载, 对于提升用户体验而言, 非常重要.
 */

public class SplashActivity extends BasePermissionsAndStackActivity {

    @Inject Lazy<SplashLibrary> splashLibraryLazy; // 延迟闪屏库

    @Inject @Named(SplashModule.OBSERVABLE_SPLASH_LIBRARY)
    Observable<SplashLibrary> mObservable; // 闪屏库观察者 //观察SplashLibrary的延迟加载是否完成

    @Inject @Named(SplashModule.SPLASH_ACTIVITY)
    AtomicBoolean initialized; // 闪屏模块初始化

    private Subscription mSubscription; // 闪屏订阅者

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ZTLUtils(this).setTranslucentStatusAll();

        DemoApp.app(this).splashComponent().inject(this);

        // 检测依赖注入释是否成功
        Preconditions.checkNotNull(splashLibraryLazy);
        Preconditions.checkNotNull(mObservable);
        Preconditions.checkNotNull(initialized);


        //1、请求单个权限
//        requestPermissionsOne();
        //2、请求多个权限
        requestPermissionsMore();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    //使用RxJava加载启动库, 使用计算线程, 在成功后跳转主页面, 在失败后弹出信息提示. 注意在加载库的控制,
    // 保证只加载一次. 在页面关闭时, 取消注册订阅, 即unsubscribe()
    @Override
    protected void onStart() {
        super.onStart();

        // 初始化成功
        if (initialized.get()) {
            openMainAndFinish(this, splashLibraryLazy.get());
        } else {
            // 延迟加载数据
            mSubscription = mObservable
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccess, this::onError);
        }
    }

    // 加载成功
    private void onSuccess(SplashLibrary library) {
        initialized.set(true);
        openMainAndFinish(SplashActivity.this, library);
    }

    // 加载失败
    private void onError(Throwable e) {
        Toast.makeText(SplashActivity.this, R.string.error_fatal, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // 取消 注册订阅者
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 释放资源
        mSubscription = null;
    }

    private  void openMainAndFinish(@NonNull Activity activity, @NonNull SplashLibrary splashLibrary) {
        // 提示加载库完成
        Toast.makeText(activity, splashLibrary.initializedString(), Toast.LENGTH_SHORT).show();

        // 跳转主页面
//        Intent intent = new Intent(activity, MainActivity.class);
//        intent.putExtra(MainActivity.EXTRA_USEFUL_STRING, splashLibrary.usefulString());
//        activity.startActivity(intent);

        //登录
//        Intent intent = new Intent(activity, GetListRspActivity.class);
//        intent.putExtra(MainActivity.EXTRA_USEFUL_STRING, splashLibrary.usefulString());
//        activity.startActivity(intent);
//        activity.finish();


        checkGo();
    }

    private void requestPermissionsOne() {
//        doCallPhone();
//        doCamera();;
    }

    private void requestPermissionsMore() {
        sendPermission();
    }

    /**
     * 拨打电话
     */
    private void doCallPhone() {
        XPermission.requestPermissions(this, 100,new String[]{Manifest.permission.CALL_PHONE}, new XPermission.OnPermissionListener() {
            //权限申请成功时调用
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:18682555854"));
                startActivity(intent);
            }
            //权限被用户禁止时调用
            @Override
            public void onPermissionDenied() {
                //给出友好提示，并且提示启动当前应用设置页面打开权限
                XPermission.showTipsDialog(SplashActivity.this);
            }
        });
    }

    /**
     * 照相
     */
    private void doCamera() {
        XPermission.requestPermissions(this, 101, new String[]{Manifest.permission
                .CAMERA}, new XPermission.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
            }

            @Override
            public void onPermissionDenied() {
                XPermission.showTipsDialog(SplashActivity.this);
            }
        });
    }
    /**
     * 多个权限
     */
    private void sendPermission() {
        XPermission.requestPermissions(this, 102, permissions, new XPermission.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplication(),"申请成功！",Toast.LENGTH_SHORT).show();
//                delayEntryPage();
            }

            @Override
            public void onPermissionDenied() {
//                XPermission.showTipsDialog(SplashActivity.this);
            }
        });
    }

    private void delayEntryPage() {//
      /*  TimerTask task = new TimerTask() {
            @Override
            public void run() {
                readyGo(GetListRspActivity.class);
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000);*/

        UiUtil.postDelayed(new Runnable() {
            @Override
            public void run() {
//                readyGo(GetListRspActivity.class);


                checkGo();
            }
        }, 2000);
    }

    private  void checkGo() {
        boolean  isFirstRun = SharedPrefsUtil.getValue(SplashActivity.this, "isFirstRun","isFirstRun", true);
        if (!isFirstRun) {
            goHome();
        } else {
            SharedPrefsUtil.putValue(SplashActivity.this,"isFirstRun", "isFirstRun", false);
            goGuide();
        }
    }


    public void goHome() {
        switchTo(this,GetListRspActivity.class,true);
    }

    public void goGuide() {
        switchTo(this, GuideActivity.class,true);
    }

    //----------------------------------   其他功能   ---------------------------------------------------

    //不携带数据跳转Activity
    public static void switchTo(Activity activity, Class<? extends Activity> targetActivity, boolean needFinish) {
        if (activity == null) {
            return;
        }
        activity.startActivity(new Intent(activity, targetActivity));
        if (needFinish) {
            activity.finish();
        }
    }

}
