package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentDataClearBinding;

import org.jetbrains.annotations.NotNull;

/**
 * 数据清空
 */
public class DataClearFragment extends BaseFragment {
    private FragmentDataClearBinding clearBinding;

    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        clearBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_clear,container,false);
        return clearBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
