package com.askia.coremodel.datamodel.database.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.askia.coremodel.event.LoginSuccessEvent;
import com.blankj.utilcode.util.LogUtils;
import com.ttsea.jrxbus2.RxBus2;

import java.io.IOException;
import java.io.StreamCorruptedException;

public class SharedPreUtil {

    // 用户名key
    public final static String YEWUYUAN = "yewuyuan";
    public final static String USER_INFO = "user_info";
    public final static String ADVERTISING = "advertising";
    public final static String ACCOUNT = "account";
    public final static String PASSWORD = "password";
    public final static String PHONENUM = "phonenum";
    public final static String AGORAID = "agoraid";

    // zip包时间戳
    public final static String KEY_ZIP_TIMESTAMP = "KEY_ZIP_TIMESTAMP";


    public static String num = "";
    private static SharedPreUtil s_SharedPreUtil;

    private SharedPreferences msp;

    // 初始化，一般在应用启动之后就要初始化
    public static synchronized void initSharedPreference(Context context) {
        if (s_SharedPreUtil == null) {
            s_SharedPreUtil = new SharedPreUtil(context);
        }
    }

    /**
     * 获取唯一的instance
     *
     * @return
     */
    public static synchronized SharedPreUtil getInstance() {
        return s_SharedPreUtil;
    }

    private SharedPreUtil(Context context) {
        msp = context.getSharedPreferences("SharedPreUtil",
                Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPref() {
        return msp;
    }

    public synchronized void putAccount(String account) {

        Editor editor = msp.edit();
        editor.putString(ACCOUNT, account);
        editor.commit();
    }

    public synchronized String getAgoraid() {
        String str = msp.getString(SharedPreUtil.AGORAID,"");
        return str;
    }

    public synchronized void setAgoraid(String agoraid) {

        Editor editor = msp.edit();
        editor.putString(AGORAID, agoraid);
        editor.commit();
    }

    public synchronized String getAccount() {
        String str = msp.getString(SharedPreUtil.ACCOUNT,"");
        return str;
    }

    public synchronized void putPhoneNum(String phoneNum) {

        Editor editor = msp.edit();
        editor.putString(PHONENUM, phoneNum);
        editor.commit();
    }

    public synchronized String getPhonenum() {
        String str = msp.getString(SharedPreUtil.PHONENUM,"");
        return str;
    }

    public synchronized void putPassword(String psw) {

        Editor editor = msp.edit();
        editor.putString(PASSWORD, psw);
        editor.commit();
    }

    public synchronized String getPassword() {
        String str = msp.getString(SharedPreUtil.PASSWORD,"");
        return str;
    }

    public synchronized void putyewuyuan(int timestamp) {

        Editor editor = msp.edit();
        editor.putInt(YEWUYUAN, timestamp);
        editor.commit();
    }

    public synchronized int getyewuyuan() {
        int str = msp.getInt(SharedPreUtil.YEWUYUAN,1);
        return str;
    }

    public synchronized void putZipTimestamp(String timestamp) {

        Editor editor = msp.edit();
        LogUtils.d("put zip time stamp " + timestamp);
        editor.putString(KEY_ZIP_TIMESTAMP, timestamp);
        editor.commit();
    }

    public synchronized String getZipTimestamp() {
        String str = msp.getString(SharedPreUtil.KEY_ZIP_TIMESTAMP, "");
        LogUtils.d("get zip time stamp " + str);
        return str;
    }

}