package com.lncucc.authentication.activitys;

import android.Manifest;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.base.ViewManager;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActSplashBinding;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.unicom.facedetect.detect.FaceDetectInitListener;
import com.unicom.facedetect.detect.FaceDetectManager;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class SplashActivity extends BaseActivity
{
    public final CompositeDisposable mDisposable = new CompositeDisposable();

    private ActSplashBinding mActSplashBinding;
    @Override
    public void onInit()
    {
        requestPermissions();
    }

    private void requestPermissions() {
        final RxPermissions rxPermission = new RxPermissions(SplashActivity.this);
        rxPermission.request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,

                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
        ).subscribe(aBoolean -> {
            if (aBoolean){
                //申请的权限全部允许
                goToMain();
            }else{
                //只要有一个权限被拒绝，就会执行
                new QMUIDialog.MessageDialogBuilder(SplashActivity.this)
                        .setTitle("权限异常")
                        .setMessage("没有允许全部权限授权，将无法正常使用大话机功能。")
                        .addAction("退出应用", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                ViewManager.getInstance().exitApp(SplashActivity.this);
                            }
                        })
                        .setCanceledOnTouchOutside(false)
                        .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
            }
        });
    }


    private void goToMain()
    {
        Observable.timer(3*1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong)
                    {
//                        startActivityByRouter(ARouterPath.MAIN_ACTIVITY);
//                        finish();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public void onInitDataBinding() {
        mActSplashBinding = DataBindingUtil.setContentView(this, R.layout.act_splash);
        mActSplashBinding.setHandlers(this);
    }

    @Override
    public void onSubscribeViewModel() {

    }

    public void toLogin(View view){
        startActivityByRouter(ARouterPath.LOGIN_ACTIVITY);
    }

    public void toExamination(View view){
    }

    /**
     * 设置系统时间
     *
     * @param view button view
     */
    public void setSystemTime(View view){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }


}
