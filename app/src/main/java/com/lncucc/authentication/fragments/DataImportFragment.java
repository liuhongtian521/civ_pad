package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.viewmodel.DataImportViewModel;
import com.blankj.utilcode.util.LogUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentImportBinding;
import com.ttsea.jrxbus2.RxBus2;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 数据导入
 */
public class DataImportFragment extends BaseFragment {
    private FragmentImportBinding leadInBinding;
    private DataImportViewModel viewModel;
    private ArrayList<Boolean> importList;

    @Override
    public void onInit() {
        RxBus2.getInstance().register(this);
    }

    public void initEvent() {
        leadInBinding.sbNet.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.netImport.set(isChecked));
        leadInBinding.sbUsb.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.usbImport.set(isChecked));
        leadInBinding.sbSdcard.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.sdCardImport.set(isChecked));
    }

    @Override
    public void onInitViewModel() {
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(DataImportViewModel.class);
    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        leadInBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_import, container, false);
        leadInBinding.setViewmodel(viewModel);
        leadInBinding.setClick(this);
        initEvent();
        return leadInBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {
        viewModel.getSdCardData().observe(this, result -> {
            if ("100".equals(result)){
                MyToastUtils.error("解压成功", Toast.LENGTH_SHORT);
            }
        });
    }

    public void importData(View view) {
        importList = new ArrayList<>();
        importList.add(viewModel.netImport.get());
        importList.add(viewModel.usbImport.get());
        importList.add(viewModel.sdCardImport.get());
        int count = 0;
        for (int i = 0; i < importList.size(); i++) {
            if (importList.get(i)) {
                count++;
            }
        }
        if (count > 1) {
            MyToastUtils.error("只能选择一种导入方式!", Toast.LENGTH_SHORT);
            return;
        }
        if (count == 0) {
            MyToastUtils.error("请选择一种导入方式!", Toast.LENGTH_SHORT);
            return;
        }

        if (viewModel.netImport.get()) {
            LogUtils.e("网络导入");
        } else if (viewModel.usbImport.get()) {
            LogUtils.e("usb导入");
        } else {
            viewModel.doSdCardImport();
        }


    }
}
