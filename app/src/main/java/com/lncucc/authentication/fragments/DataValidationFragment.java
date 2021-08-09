package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentDataValidationBinding;

import org.jetbrains.annotations.NotNull;

/**
 * 验证数据查看
 */
public class DataValidationFragment extends BaseFragment {
    private FragmentDataValidationBinding mBinding;

    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_validation,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
