package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.blankj.utilcode.util.LogUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentDateSettingBinding;

import org.jetbrains.annotations.NotNull;

/**
 * 数据导入
 */
public class DateSettingFragment extends BaseFragment {
    private FragmentDateSettingBinding dateSetting;

    @Override
    public void onInit() {

    }



    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        dateSetting = DataBindingUtil.inflate(inflater, R.layout.fragment_date_setting,container,false);
        return dateSetting.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }



    public void clickCameraSet(View view) {
        LogUtils.e("点击");
    }

}
