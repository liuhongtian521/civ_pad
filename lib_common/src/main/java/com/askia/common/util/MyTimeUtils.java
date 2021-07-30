package com.askia.common.util;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.blankj.utilcode.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * time 工具类
 */

public class MyTimeUtils {
    public static int THEME_DEVICE_DEFAULT_LIGHT = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;
    public static int THEME_DEVICE_DEFAULT_DARK = AlertDialog.THEME_DEVICE_DEFAULT_DARK;
    public static int THEME_TRADITIONAL = AlertDialog.THEME_TRADITIONAL;
    public static int THEME_HOLO_LIGHT = AlertDialog.THEME_HOLO_LIGHT;
    public static int THEME_HOLO_DARK = AlertDialog.THEME_HOLO_DARK;
    private static DatePickerDialog mDatePickerDialog;//日期选择器

    private static SetTitleDatePickerDialog setTitleDatePickerDialog;

    /**
     * 将字符串时间转为Long时间
     *
     * @param time yyyy-MM-dd HH:mm:ss:SSS
     */
    public static Long getLongTimeOfSSS(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
        }
        return 0L;
    }

    /**
     * 将字符串时间转为Long时间
     *
     * @param time yyyy-MM-dd HH:mm:ss
     */
    public static Long getLongTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
        }
        return 0L;
    }


    /**
     * 获取当前日期是星期几<br>
     *
     * @param time
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(long time) {
        Date date = new Date(time);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 将字符串时间转为Long时间
     *
     * @param time yyyy-MM-dd
     */
    public static Long getLongTimeOfYMD(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
        }
        return 0L;
    }

    /**
     * 将Long时间转成String时间
     *
     * @return yyyy-MM-dd HH:mm:ss:SSS
     */
    public static String getStringTimeOfSSS(Long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return sdf.format(date);
    }

    /**
     * 将Long时间转成String时间
     *
     * @return HH:mm:
     */
    public static String getStringHourMinite(Long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(date);
    }

    /**
     * 获取  yyyy-MM-dd 星期几 HH:mm
     *
     * @return HH:mm:
     */
    public static String getCurrentFomatTime()
    {
        Long currentTime = System.currentTimeMillis();
        return getStringTimeOfYMD(currentTime) + " " + getWeekOfDate(currentTime) + " " + getStringHourMinite(currentTime);
    }

    /**
     * 将Long时间转成String时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getStringTime(Long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 将Long时间转成String时间
     *
     * @return yyyy-MM-dd
     */
    public static String getStringTimeOfYMD(Long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 当前的时间(设备)
     *
     * @return yyyy-MM-dd HH:mm:ss:SSS
     */
    public static String getDeviceTimeOfSSS() {
        String date = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            date = df.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date().getTime() + "";//当前时间的long字符串
        }
        return date;
    }

    /**
     * 当前的时间(设备)
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDeviceTime() {
        String date = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = df.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            date = new Date().getTime() + "";//当前时间的long字符串
        }
        return date;
    }

    /**
     * 当前的时间(年月日)
     *
     * @return yyyy-MM-dd
     */
    public static String getDeviceTimeOfYMD() {
        String date = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            date = df.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 当前的时间(年月)
     *
     * @return yyyy-MM
     */
    public static String getDeviceTimeOfYM() {
        String date = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
            date = df.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取某月最后一天(年月日)
     *
     * @return yyyy-MM
     */
    public static String getLastDayOfMonthOfYMD(int year, int month) {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    /**
     * 获取某月最后一天(日)
     */
    public static int getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        cal.set(Calendar.MONTH, month - 1);
        // 获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        return lastDay;
    }

    /**
     * 显示日期选择器
     *
     * @param themeLight true 白色背景; false 黑色背景
     */
    public static MyTimeUtils showDatePickerDialog(Context context, boolean themeLight, String title, int year, int month, int day, OnDatePickerListener onDateTimePickerListener) {
        int themeId = AlertDialog.THEME_HOLO_LIGHT;//默认白色背景
        if (!themeLight) {
            themeId = AlertDialog.THEME_HOLO_DARK;//黑色背景
        }
        return showDatePickerDialog(context, themeId, title, year, month, day, onDateTimePickerListener);
    }

    /**
     * 显示日期选择器, 默认白色背景
     */
    public static MyTimeUtils showDatePickerDialog(Context context, String title, int year, int month, int day, OnDatePickerListener onDateTimePickerListener) {
        return showDatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, title, year, month, day, onDateTimePickerListener);
    }

    /**
     * 显示日期选择器
     */
    public static MyTimeUtils showDatePickerDialog(Context context, int themeId, String title, int year, int month, int day,
                                                   final OnDatePickerListener onDateTimePickerListener) {
//        mDatePickerDialog = new DatePickerDialog(context, themeId, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                month = month + 1;//月份加一
//                if (onDateTimePickerListener != null) {
//                    onDateTimePickerListener.onConfirm(year, month, dayOfMonth);
//                }
//            }
//
//
//        }, year, month - 1, day);//月份减一

        setTitleDatePickerDialog = new SetTitleDatePickerDialog(context, themeId, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;//月份加一
                if (onDateTimePickerListener != null) {
                    onDateTimePickerListener.onConfirm(year, month, dayOfMonth);
                }
            }

        }, year, month - 1, day);//月份减一


        setTitleDatePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (onDateTimePickerListener != null) {
                    onDateTimePickerListener.onCancel();
                }
            }
        });

        if (!TextUtils.isEmpty(title)) {
            setTitleDatePickerDialog.setTitle(title);
        }

        setTitleDatePickerDialog.show();
        return new MyTimeUtils();
    }


    /**
     * 隐藏年, 只显示月和日
     */
    public void setYearGone() {
        setSpecialDatePicker(1);
    }

    /**
     * 隐藏日, 只显示年和月
     */
    public void setDayGone() {
        setSpecialDatePicker(2);
    }


    private void setSpecialDatePicker(int state) {
        try {
            DatePicker dp = setTitleDatePickerDialog.getDatePicker();

            NumberPicker view0 = (NumberPicker) ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(0); //获取最前一位的宽度
            NumberPicker view1 = (NumberPicker) ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(1); //获取中间一位的宽度
            NumberPicker view2 = (NumberPicker) ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2);//获取最后一位的宽度
            if (((ViewGroup) dp.getChildAt(0)).getChildCount() > 1) {
                ((ViewGroup) dp.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
            }
            //年的最大值为2100
            //月的最大值为11
            //日的最大值为28,29,30,31
            int value0 = view0.getMaxValue();//获取第一个View的最大值
            int value1 = view1.getMaxValue();//获取第二个View的最大值
            int value2 = view2.getMaxValue();//获取第三个View的最大值

            if (state == 1) {//隐藏年, 只显示月和日
                if (value0 > 252) {
                    view0.setVisibility(View.GONE);
                } else if (value1 > 252) {
                    view1.setVisibility(View.GONE);
                } else if (value2 > 252) {
                    view2.setVisibility(View.GONE);
                }
                //  view0.setVisibility(View.GONE);
            } else if (state == 2) {//隐藏日, 只显示年和月
                if (value0 > 25 && value0 < 252) {
                    view0.setVisibility(View.GONE);
                } else if (value1 > 25 && value1 < 252) {
                    view1.setVisibility(View.GONE);
                } else if (value2 > 25 && value2 < 252) {
                    view2.setVisibility(View.GONE);
                }
                dp.setCalendarViewShown(false);
                dp.setSpinnersShown(true);
            }
        } catch (Exception e) {
            ToastUtils.showLong(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 显示时间选择器
     */
    public static void showTimerPickerDialog(Context context, boolean themeLight, String title, int hourOfDay, int minute, boolean is24HourView, final OnTimerPickerListener onTimerPickerListener) {
        int themeId = AlertDialog.THEME_HOLO_LIGHT;//默认白色背景
        if (!themeLight) {
            themeId = AlertDialog.THEME_HOLO_DARK;//黑色背景
        }
        showTimerPickerDialog(context, themeId, title, hourOfDay, minute, is24HourView, onTimerPickerListener);
    }

    /**
     * 显示时间选择器, 默认白色背景
     */
    public static void showTimerPickerDialog(Context context, String title, int hourOfDay, int minute, boolean is24HourView, final OnTimerPickerListener onTimerPickerListener) {
        showTimerPickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, title, hourOfDay, minute, is24HourView, onTimerPickerListener);
    }

    /**
     * 显示时间选择器
     */
    public static void showTimerPickerDialog(Context context, int themeId, String title, int hourOfDay, int minute, boolean is24HourView, final OnTimerPickerListener onTimerPickerListener) {
        TimePickerDialog dialog = new TimePickerDialog(context, themeId, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (onTimerPickerListener != null) {
                    onTimerPickerListener.onConfirm(hourOfDay, minute);
                }
            }
        }, hourOfDay, minute, is24HourView);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (onTimerPickerListener != null) {
                    onTimerPickerListener.onCancel();
                }
            }
        });

        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        dialog.show();
    }

    /**
     * 日期选择器监听
     */
    public interface OnDatePickerListener {
        void onConfirm(int year, int month, int dayOfMonth);

        void onCancel();
    }

    /**
     * 时间选择器监听
     */
    public interface OnTimerPickerListener {
        void onConfirm(int hourOfDay, int minute);

        void onCancel();
    }

    public static class SetTitleDatePickerDialog extends DatePickerDialog implements
            DatePicker.OnDateChangedListener {

        public SetTitleDatePickerDialog(Context context, int theme,
                                        OnDateSetListener callBack, int year, int monthOfYear,
                                        int dayOfMonth) {
            super(context, theme, callBack, year, monthOfYear, dayOfMonth);
            // TODO Auto-generated constructor stub

        }


        public void onDateChanged(DatePicker view, int year, int month, int day) {
            // updateTitle(year, month, day);

        }
    }

}