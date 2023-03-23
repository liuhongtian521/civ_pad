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
import com.askia.coremodel.datamodel.database.operation.LogsUtil;
import com.askia.coremodel.viewmodel.DateSettingViewModel;
import com.askia.coremodel.viewmodel.LoginViewModel;
import com.blankj.utilcode.util.TimeUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.activitys.LoginActivity;
import com.lncucc.authentication.databinding.FragmentDateSettingBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * 时间设置
 */
public class DateSettingFragment extends BaseFragment {
    private FragmentDateSettingBinding dateSetting;
     DateSettingViewModel dateSettingViewModel;
    public final CompositeDisposable mDisposable = new CompositeDisposable();


    @Override
    public void onInit() {

        dateSetting.sbTimeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateClick();
            }
        });
        initTimer();
    }

    private void initTimer() {
        Observable.interval(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NotNull Long aLong) {
                        com.blankj.utilcode.util.LogUtils.e("current time ->", System.currentTimeMillis());
                        Date date = TimeUtils.millis2Date(System.currentTimeMillis());
                        String current = TimeUtils.date2String(date);
                        dateSetting.dateWrap.setText(current);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            initTimer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
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
        LogsUtil.saveOperationLogs("时间校正");
        mActivity.startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
    }

}