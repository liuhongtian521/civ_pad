package com.lncucc.authentication.fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.lncucc.authentication.R;
import com.lncucc.authentication.activitys.LoginActivity;
import com.lncucc.authentication.databinding.FragmentLogoutSettingBinding;

import org.jetbrains.annotations.NotNull;

/**
 * 退出登录
 */
public class LogoutSettingFragment extends BaseFragment {
    private FragmentLogoutSettingBinding logoutSetting;
    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        logoutSetting = DataBindingUtil.inflate(inflater, R.layout.fragment_logout_setting,container,false);
        logoutSetting.setHandles(this);
        return logoutSetting.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }

    public void logout(View view){
        getActivity().finish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

}
