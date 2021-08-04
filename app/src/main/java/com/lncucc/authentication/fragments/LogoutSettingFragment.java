package com.lncucc.authentication.fragments;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.blankj.utilcode.util.LogUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentLogoutSettingBinding;


import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

/**
 * 数据导入
 */
public class LogoutSettingFragment extends BaseFragment {
    private FragmentLogoutSettingBinding logoutSetting;
    LinearLayout linear ;  // 切换外部linear盒子
    TextView te1; // 前置摄像头
    TextView te2; // 后置摄像头
    String currentPos; // 当前选择的是哪个摄像头  前置"1"  后置"2"
    @Override
    public void onInit() {

    }



    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        logoutSetting = DataBindingUtil.inflate(inflater, R.layout.fragment_logout_setting,container,false);
        return logoutSetting.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }



    public void clickCameraSet(View view) {
        LogUtils.e("点击");
    }

}
