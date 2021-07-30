package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentDataViewBinding;

/**
 * 数据查看
 */
public class DataViewFragment extends BaseFragment {
    private FragmentDataViewBinding viewBinding;
    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(LayoutInflater inflater, ViewGroup container) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_view,container,false);
        return viewBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
