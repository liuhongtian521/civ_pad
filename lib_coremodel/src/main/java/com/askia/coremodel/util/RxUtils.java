package com.askia.coremodel.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class RxUtils {

    //倒计时  秒级别
    public static Disposable CountDown(final long time, @Nullable Consumer<Long> consumer, @NonNull Action completeAction) {
        if (consumer == null) {
            return Flowable.intervalRange(0, time + 1, 0, 1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(aLong -> time - aLong)
                    .doOnComplete(completeAction)
                    .subscribe();
        }
        return Flowable.intervalRange(0, time + 1, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(aLong -> time - aLong)
                .doOnNext(consumer)
                .doOnComplete(completeAction)
                .subscribe();
    }

    //请求相机权限
    @SuppressLint("CheckResult")
    public static <T extends Activity> void RequestCameraPermissions(T activity, Consumer<Boolean> consumer) {
        new RxPermissions(activity)
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(consumer);
    }

    //请求定位权限
    @SuppressLint("CheckResult")
    public static <T extends Activity> void RequestLocationPermisoon(T activity, Consumer<Boolean> consumer) {
        new RxPermissions(activity)
                .request(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE)
                .subscribe(consumer);
    }

    //请求读写
    @SuppressLint("CheckResult")
    public static <T extends Activity> void RequestReadAndWrite(T activity, Consumer<Boolean> consumer) {
        new RxPermissions(activity)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(consumer);
    }
}
