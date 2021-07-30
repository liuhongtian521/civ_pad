package com.askia.coremodel.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NetUtils {

    private static Context context;

    private NetUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static final int DISCONNECTED = 0;
    public static final int WIFI_CONNECTED = 1;
    public static final int ETHERNET_CONNECTED = 2;

    public static void init(Context context) {
        NetUtils.context = context.getApplicationContext();
    }

    public static int getNetConnStatus() {
        if (context == null) {
            return DISCONNECTED;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        int status = DISCONNECTED;

        if (wifiNetInfo != null && wifiNetInfo.isAvailable() && wifiNetInfo.isConnected()) {
            status = WIFI_CONNECTED;
        } else {
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                status = ETHERNET_CONNECTED;
            }
        }

        return status;
    }

    /**
     * 当前网络是否已连接
     */
    public static boolean isNetConnected() {
        if (context == null) {
            return false;
        }

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo[] infos = connectivity.getAllNetworkInfo();
                if (infos != null) {
                    for (NetworkInfo info : infos) {
                        if (info != null && info.isConnected()) {
                            // 判断当前网络是否已经连接
                            if (info.getState() == NetworkInfo.State.CONNECTED) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 当前网络是否已连接
     */
    public static LiveData<Boolean> netConnected() {
        MutableLiveData<Boolean> isNetConnect = new MutableLiveData<>();
        if (context == null) {
            isNetConnect.setValue(false);
            return isNetConnect;
        }

        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                // 获取网络连接管理的对象
                NetworkInfo[] infos = connectivity.getAllNetworkInfo();
                if (infos != null) {
                    for (NetworkInfo info : infos) {
                        if (info != null && info.isConnected()) {
                            // 判断当前网络是否已经连接
                            if (info.getState() == NetworkInfo.State.CONNECTED) {
                                isNetConnect.setValue(true);
                                return isNetConnect;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isNetConnect.setValue(false);
        return isNetConnect;
    }

    public static String getFormatTimeFromTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm");
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    public static String getFormatTimeFromTimestamp(long timestamp, String format) {
        if (TextUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm";
        }
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern(format);
        if (String.valueOf(timestamp).length() < 13) {
            timestamp = Long.valueOf(String.valueOf(timestamp) + "000");
        }
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param date
     * @return 当前日期是星期几
     */
    static public String getWeekOfDate(String date) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date myDate = new Date();
        try {
            myDate = myFormatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] weekDays = {  "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

}
