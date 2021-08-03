package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentUserManageBinding;

import org.jetbrains.annotations.NotNull;

/**
 * 数据导入
 */
public class UserManageFragment extends BaseFragment {
    private FragmentUserManageBinding userManage;
    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        userManage = DataBindingUtil.inflate(inflater, R.layout.fragment_user_manage,container,false);
        return userManage.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
