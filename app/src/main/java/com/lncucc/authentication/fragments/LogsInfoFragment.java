package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentLoginfoBinding;

import org.jetbrains.annotations.NotNull;

/**
 * 日志信息fragment
 */

public class LogsInfoFragment extends BaseFragment {

    private FragmentLoginfoBinding loginfoBinding;


    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        loginfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_loginfo,container,false);
        return loginfoBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
