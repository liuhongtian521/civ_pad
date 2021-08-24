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
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.LogoutDialog;

import org.jetbrains.annotations.NotNull;

/**
 * 退出登录
 */
public class LogoutSettingFragment extends BaseFragment implements DialogClickBackListener {
    private FragmentLogoutSettingBinding logoutSetting;
    private LogoutDialog logoutDialog;
    @Override
    public void onInit() {
        logoutDialog = new LogoutDialog(getActivity(),this);
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
        logoutDialog.show();
    }

    @Override
    public void dissMiss() {

    }

    @Override
    public void backType(int type) {
        if (logoutDialog != null){
            logoutDialog.dismiss();
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }
}
