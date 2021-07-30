package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentExportBinding;

import org.jetbrains.annotations.NotNull;

/**
 * 数据导出
 */
public class ExportFragment extends BaseFragment {

    private FragmentExportBinding exportBinding;

    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        exportBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_export,container,false);
        return exportBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
