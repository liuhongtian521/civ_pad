package com.askia.common.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.R;
import com.askia.common.util.Utils;
import com.askia.common.widget.CountDownText;
import com.askia.common.widget.NetLoadingDialog;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import me.jessyan.autosize.AutoSize;


/**
 * <p>Activity基类 </p>
 *
 * @name BaseActivity
 */
public abstract class BaseActivity extends AppCompatActivity {

    private CountDownText mCountDownText;
    private NetLoadingDialog wNetLoadingDialog;


    /**
     * 封装的findViewByID方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewManager.getInstance().addActivity(this);
        LogUtils.d(this.getClass().getSimpleName() + "---onCreate()");

        // 初始化viewmodel
        onInitViewModel();
        // 初始化databing 绑定布局
        onInitDataBinding();
        // 初始化其他
        onInit();
        // 订阅viewmodel 数据变动事件
        onSubscribeViewModel();
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
    }

    /**
     *
     */
    public abstract void onInit();
    public abstract void onInitViewModel();
    public abstract void onInitDataBinding();
    public abstract void onSubscribeViewModel();

    @Override
    protected void onResume() {
        super.onResume();
        com.blankj.utilcode.util.LogUtils.d("=================this is " + this.getClass().getSimpleName() + "=================");
        ViewManager.getInstance().onResumeActivity(this);
        if (mCountDownText != null) {
            mCountDownText.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownText != null) {
            mCountDownText.cancel();
        }
        ViewManager.getInstance().finishActivity(this);
        LogUtils.d(this.getClass().getSimpleName() + "---onDestroy()");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    /**
     * Setup the toolbar.
     *
     * @param toolbar   toolbar
     * @param hideTitle 是否隐藏Title
     */
    protected void setupToolBar(Toolbar toolbar, boolean hideTitle) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            if (hideTitle) {
                //隐藏Title
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
    {
        AutoSize.autoConvertDensityOfGlobal(this);
        return super.onCreateView(parent, name, context, attrs);
    }

    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .add(frameId, fragment, fragment.getClass().getSimpleName())
                .disallowAddToBackStack()
                .commitAllowingStateLoss();

    }


    /**
     * 替换fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void replaceFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment, fragment.getClass().getSimpleName())
                .disallowAddToBackStack()
                .commitAllowingStateLoss();

    }


    /**
     * 隐藏fragment
     *
     * @param fragments
     */
    protected void hideFragment(BaseFragment... fragments) {
        Utils.checkNotNull(fragments);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (BaseFragment fragment : fragments) {
            if (fragment != null) {
                transaction.hide(fragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }


    /**
     * 显示fragment
     *
     * @param fragment
     */
    protected void showFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 移除fragment
     *
     * @param fragment
     */
    protected void removeFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.activity_down_in, R.anim.activity_down_out);
    }


    /**
     * 隐藏虚拟按键，并且全屏
     */
    public void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    // 通过ARouter 启动其他Activity 无传递参数
    public void startActivityByRouter(String path)
    {
        ARouter.getInstance().build(path).navigation();
    }
    // 通过ARouter 启动其他Activity 有传递参数
    public void startActivityByRouter(String path,Bundle b)
    {
        ARouter.getInstance().build(path).with(b).navigation();
    }
    // 通过ARouter 启动其他Activity 有传递参数, 使用拦截器
    public void startActivityByRouterWithInterceptor(String path,Bundle b,NavCallback callback)
    {
        ARouter.getInstance().build(path).with(b).navigation(this,callback);
    }
    // 通过Arouter 启动其他Activity 并期待返回结果  無传递参数
    public void startActivityForResultByRouter(String path,int requestCode)
    {
        ARouter.getInstance().build(path).navigation(BaseActivity.this, requestCode);
    }
    // 通过Arouter 启动其他Activity 并期待返回结果  有传递参数
    public void startActivityForResultByRouter(String path,int requestCode,Bundle b)
    {
        ARouter.getInstance().build(path).with(b).navigation(BaseActivity.this, requestCode);
    }

    // 通过Arouter 获取Fragment
    public BaseFragment getFragment(String path)
    {
        return (BaseFragment) ARouter.getInstance().build(path).navigation();
    }

    // 通过Arouter 获取Fragment,携带参数
    public BaseFragment getFragment(String path,Bundle b)
    {
        BaseFragment fragment = (BaseFragment) ARouter.getInstance().build(path).navigation();
        fragment.setArguments(b);
        return fragment;
    }

 /*   public void addCountDownView(ViewGroup parent, int second) {
        mCountDownText = new CountDownText(this,second, new CountDownText.CountDownFinishListener() {
            @Override
            public void onFinish() {
                startActivityByRouter(ARouterPath.QMAIN_ACTIVITY);
            }
        });
        if (parent instanceof FrameLayout) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(Utils.dp2px(this, 87), Utils.dp2px(this, 35));
            layoutParams.setMargins(Utils.dp2px(this, 823), Utils.dp2px(this, 10), Utils.dp2px(this, 30), Utils.dp2px(this, 489));
            parent.addView(mCountDownText, layoutParams);
        } else if (parent instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Utils.dp2px(this, 87), Utils.dp2px(this, 35));
            layoutParams.setMargins(Utils.dp2px(this, 823), Utils.dp2px(this, 10), Utils.dp2px(this, 30), Utils.dp2px(this, 489));
            parent.addView(mCountDownText, layoutParams);
        }
    }*/

    public void showNetDialog() {
        boolean dialogStatus = false;
        if (null != wNetLoadingDialog) {
            dialogStatus = wNetLoadingDialog.isShowing();
        }
        if (!dialogStatus) {
            wNetLoadingDialog = NetLoadingDialog.show(this, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog)
                {
                }
            });
        }
    }


    public void dismissNetDialog() {
        if (null != wNetLoadingDialog && wNetLoadingDialog.isShowing()) {
            wNetLoadingDialog.dismiss();
        }
    }

}
