package com.askia.common.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Toast;

import androidx.annotation.MainThread;

import com.askia.common.R;

import es.dmoral.toasty.Toasty;

/**
 * Toast相关工具类
 */
public class MyToastUtils {

    private static Context context;

    private MyToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        MyToastUtils.context = context.getApplicationContext();
        //Toast 初始化
        Toasty.Config.getInstance()
                .setErrorColor(MyToastUtils.context.getResources().getColor(R.color.text_black_4d))
                .setInfoColor(MyToastUtils.context.getResources().getColor(R.color.text_black_4d))
                .setSuccessColor(MyToastUtils.context.getResources().getColor(R.color.text_black_4d))
                .setWarningColor(MyToastUtils.context.getResources().getColor(R.color.text_black_4d))
                .tintIcon(false)
                .setToastTypeface(Typeface.DEFAULT)
                .setTextSize(12)
                .setTextColor(MyToastUtils.context.getResources().getColor(R.color.white))
                .apply();
    }

    @MainThread
    public static void error(String mes, int duration) {
        Toasty.error(MyToastUtils.context, mes, duration, false).show();
    }

    @MainThread
    public static void success(String mes, int duration) {
        Toasty.success(MyToastUtils.context, mes, duration, false).show();
    }

    @MainThread
    public static void info(String mes, int duration) {
        Toasty.info(MyToastUtils.context, mes, duration, false).show();
    }

    @MainThread
    public static void info(String mes) {
        info(mes, Toast.LENGTH_SHORT);
    }

    @MainThread
    public static void warning(String mes, int duration) {
        Toasty.warning(MyToastUtils.context, mes, duration, false).show();
    }

    @MainThread
    public static void normal(String mes, int duration) {
        Toasty.normal(MyToastUtils.context, mes).show();
    }

    @MainThread
    public static void showNetNoConnected() {
        Toasty.normal(MyToastUtils.context, "网络未连接，请检查网络").show();
    }
}