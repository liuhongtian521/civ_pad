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
import com.lncucc.authentication.databinding.FragmentDisplaySettingBinding;


import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

/**
 * 数据导入
 */
public class DisplaySettingFragment extends BaseFragment {
    private FragmentDisplaySettingBinding displaySetting;
    LinearLayout linear ;  // 切换外部linear盒子
    TextView te1; // 前置摄像头
    TextView te2; // 后置摄像头
    String currentPos; // 当前选择的是哪个摄像头  前置"1"  后置"2"
    @Override
    public void onInit() {
    linear = displaySetting.CamSet;
    te1 = displaySetting.cameraSetBtn1;
    te2 = displaySetting.cameraSetBtn2;
        currentPos = "2";
        setState();
    linear.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            setState();
        }
     });
    }



    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        displaySetting = DataBindingUtil.inflate(inflater, R.layout.fragment_display_setting,container,false);
        return displaySetting.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }



    public void clickCameraSet(View view) {
        LogUtils.e("点击");
    }

    public void setState () {
        if (currentPos == "1") {
            // 当前是前置摄像头  该进行后置摄像头的设置
            currentPos = "2";
            te2.setBackgroundResource(R.drawable.bg_network_btn);
            te2.setTextColor(Color.parseColor("#FFFFFF"));
            te1.setBackgroundColor(Color.TRANSPARENT);
            te1.setTextColor(Color.parseColor("#666666"));
            return;
        }
        else {
            // 当前是后置摄像头  该进行前置摄像头的切换
            currentPos = "1";
            te1.setBackgroundResource(R.drawable.bg_network_btn);
            te1.setTextColor(Color.parseColor("#FFFFFF"));
            te2.setBackgroundColor(Color.TRANSPARENT);
            te2.setTextColor(Color.parseColor("#666666"));
            return;
        }
    }
    }
