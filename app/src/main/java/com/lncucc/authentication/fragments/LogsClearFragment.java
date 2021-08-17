package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentLogClearBinding;

import org.jetbrains.annotations.NotNull;

public class LogsClearFragment extends BaseFragment {
    private FragmentLogClearBinding clearBinding;

    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {

    }

    public void clear(View view){

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        clearBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_clear,container,false);
        clearBinding.setHandles(this);
        return clearBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
