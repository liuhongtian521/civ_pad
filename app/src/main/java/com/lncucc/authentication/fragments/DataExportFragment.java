package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.operation.LogsUtil;
import com.askia.coremodel.viewmodel.DataExportViewModel;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentExportBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据导出
 */
public class DataExportFragment extends BaseFragment {
    private List<Boolean> list;

    private FragmentExportBinding exportBinding;
    private DataExportViewModel exportViewModel;

    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {
        exportViewModel = ViewModelProviders.of(getActivity()).get(DataExportViewModel.class);
    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        exportBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_export, container, false);
        exportBinding.setHandles(this);
        return exportBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {
        exportViewModel.doExport().observe(this, result -> {
            if ("导出成功".equals(result)){
                LogsUtil.saveOperationLogs("数据导出");
            }
            MyToastUtils.error(result, Toast.LENGTH_SHORT);
        });
    }

    public void export(View view) {
        list = new ArrayList<>();
        list.add(exportBinding.sbNet.isChecked());
        list.add(exportBinding.sbUsb.isChecked());
        list.add(exportBinding.sbSdcard.isChecked());

        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i)) {
                count++;
            }
        }
        if (count > 1) {
            MyToastUtils.error("只能选择一种导入方式!", Toast.LENGTH_SHORT);
            return;
        }
        if (count == 0) {
            MyToastUtils.error("请选择一种导出方式!", Toast.LENGTH_SHORT);
            return;
        }

        if (exportBinding.sbNet.isChecked()) {
            MyToastUtils.error("敬请期待！", Toast.LENGTH_SHORT);
        } else if (exportBinding.sbUsb.isChecked()) {
            MyToastUtils.error("敬请期待！", Toast.LENGTH_SHORT);
        } else {
            exportViewModel.doDataExport("123");
        }
    }
}
