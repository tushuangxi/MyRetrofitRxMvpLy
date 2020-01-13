package com.lding.pad.myseial.libding.rerxmvp.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.lding.pad.myseial.libding.utils.AppActivityManager;
import com.lding.pad.myseial.libding.utils.XPermission;
import com.lding.pad.myseial.libding.widget.loading.LoadingActivityDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

//extends RxAppCompatActivity    或者  extends AppCompatActivity
public abstract class BaseActivity extends AppCompatActivity {

    public static List<Activity> allActivity1 = new ArrayList<>();//这个区别于MainService的allActivity ，包含了所有的。而main里不包含注册登陆等。便于用户首次登陆后退出

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetcontentView();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutId());

        AppActivityManager.getInstance().addActivity(this);//新建时添加到栈
        ButterKnife.bind(this);//绑定Activity 必须在setContentView之后

        allActivity1.add(this);
        initView();
        initArgsData();
    }


    /**
     * 设置layout前配置
     */
    private void doBeforeSetcontentView() {
        //保持竖屏
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        // 无标题
//        NoTitleWindow();
        // 设置竖屏

    }

    protected void NoTitleWindow() {
        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
    }

    protected abstract int getLayoutId();

    protected void initArgsData() {
    }

    protected abstract void initView();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.e("BaseActivity", "onDestroy: " + getClass().getSimpleName());
        AppActivityManager.getInstance().killActivity(this);
    }


    /**
     * @param clazz
     * @param bundle 跳转页面
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void readyGo(Class<?> clazz) {
        readyGo(clazz, null);
    }
    /**
     * @param clazz  目标Activity
     * @param bundle 数据
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        readyGo(clazz, bundle);
        finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz       目标Activity
     * @param requestCode 发送判断值
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz       目标Activity
     * @param requestCode 发送判断值
     * @param bundle      数据
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }
    /**
     * Android M 全局权限申请回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        XPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
