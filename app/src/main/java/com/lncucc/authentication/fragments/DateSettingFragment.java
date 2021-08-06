package com.lncucc.authentication.fragments;

import android.content.Intent;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.apkfuns.logutils.LogUtils;
import com.askia.common.base.BaseFragment;
import com.askia.coremodel.viewmodel.DateSettingViewModel;
import com.askia.coremodel.viewmodel.LoginViewModel;
import com.lncucc.authentication.R;
import com.lncucc.authentication.activitys.LoginActivity;
import com.lncucc.authentication.databinding.FragmentDateSettingBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 数据导入
 */
public class DateSettingFragment extends BaseFragment {
    private FragmentDateSettingBinding dateSetting;
     DateSettingViewModel dateSettingViewModel;

    TextView timeSetBt;
    TextView timeWrap;
    Calendar cal;
    String year;
    String month;
    String day;
    String hour;
    String minute;
    String second;
    String my_time_1;
    String my_time_2;
    long sysTime;
    Timer timer;


    @Override
    public void onInit() {
        cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        sysTime= System.currentTimeMillis();//获取系统时
        timeSetBt = dateSetting.sbTimeSet;
        timeWrap = dateSetting.dateWrap;

        timeSetBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateClick();
            }
        });
        timeset();

         timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                timeset();
            }
        };
        timer.schedule(task,10,1000);
    }

    @Override
    public void onResume() {
        sysTime= System.currentTimeMillis();//获取系统时间
        LogUtils.e("重新执行");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    @Override
    public void onInitViewModel() {
        dateSettingViewModel = ViewModelProviders.of(getActivity()).get(DateSettingViewModel.class);
    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        dateSetting = DataBindingUtil.inflate(inflater, R.layout.fragment_date_setting, container, false);
        dateSetting.setViewmodel(dateSettingViewModel);
        return dateSetting.getRoot();
    }


    @Override
    public void onSubscribeViewModel() {

    }

    public void dateClick() {
        mActivity.startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));

    }

    public void timeset () {

//        year = String.valueOf(cal.get(Calendar.YEAR));
//        month = String.valueOf(cal.get(Calendar.MONTH) + 1);
//        day = String.valueOf(cal.get(Calendar.DATE));
//        if (cal.get(Calendar.AM_PM) == 0)
//            hour = String.valueOf(cal.get(Calendar.HOUR));
//        else
//            hour = String.valueOf(cal.get(Calendar.HOUR)+12);
//        minute = String.valueOf(cal.get(Calendar.MINUTE));
//        second = String.valueOf(cal.get(Calendar.SECOND));
//
//        my_time_1 = year + "-" + month + "-" + day + " ";
//        my_time_2 = hour + ":" + minute + ":" + second;
        sysTime = sysTime + 1000;
        LogUtils.e("时间戳", sysTime);
        dateSettingViewModel.timeStr.set(DateFormat.format("yyyy-MM-dd hh:mm:ss", sysTime).toString());
    }
}