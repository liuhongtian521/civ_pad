package com.lncucc.authentication.fragments;

import android.text.TextUtils;
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
import com.askia.coremodel.datamodel.database.operation.LogsUtil;
import com.askia.coremodel.viewmodel.DataClearViewModel;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentDataClearBinding;
import com.lncucc.authentication.widgets.ConfirmDialog;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.PassWordClickCallBack;
import com.lncucc.authentication.widgets.PassWordDialog;

import org.jetbrains.annotations.NotNull;

/**
 * 数据清空
 */
public class DataClearFragment extends BaseFragment implements DialogClickBackListener, PassWordClickCallBack {
    private FragmentDataClearBinding clearBinding;
    private DataClearViewModel viewModel;
    private ConfirmDialog confirmDialog;
    private PassWordDialog passWordDialog;
    private boolean isClearImportData = false;
    private boolean isClearAuthData = false;

    @Override
    public void onInit() {
        confirmDialog = new ConfirmDialog(getActivity(), this,getString(R.string.data_clear_tip),getString(R.string.data_clear_content));
        passWordDialog = new PassWordDialog(getActivity(), this);
    }

    @Override
    public void onInitViewModel() {
        viewModel = ViewModelProviders.of(getActivity()).get(DataClearViewModel.class);
    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        clearBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_clear, container, false);
        clearBinding.setClicks(this);
        return clearBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {
        viewModel.delImportData().observe(this, result -> {
            LogUtils.e("导入数据result ->", result);
            LogsUtil.saveOperationLogs("导入数据清空");
            MyToastUtils.success(result, Toast.LENGTH_SHORT);
        });
        viewModel.delVerifyData().observe(this, result ->{
            LogUtils.e("验证数据result ->", result);
            LogsUtil.saveOperationLogs("验证数据清空");
        });
    }

    /**
     * 数据删除确认dialog
     */
    public void showConfirmDialog() {
        if (null != confirmDialog) {
            confirmDialog.show();
        }
    }

    public void clear(View view) {
        isClearImportData = clearBinding.checkboxImport.isChecked();
        isClearAuthData = clearBinding.checkboxAuth.isChecked();

        if (!isClearAuthData && !isClearImportData) {
            MyToastUtils.error("请选择清空方式!", Toast.LENGTH_SHORT);
        } else {
            showConfirmDialog();
        }
    }

    @Override
    public void dissMiss() {

    }

    @Override
    public void backType(int type) {
        if (type == 0 && passWordDialog != null) {
            confirmDialog.dismiss();
            passWordDialog.show();
        }
    }

    @Override
    public void confirm(String pwd) {

        String localPwd = SharedPreferencesUtils.getString(getActivity(), "pwd", "123456");
        if (!TextUtils.isEmpty(localPwd) && localPwd.equals(pwd)) {
            if (null != passWordDialog) {
                passWordDialog.clearPwd();
                passWordDialog.dismiss();
            }

            //密码正确 清空逻辑
            if (isClearImportData) {
                viewModel.delImport();
            } else if (isClearAuthData) {
                viewModel.delAuthData();
            } else if (isClearImportData && isClearAuthData) {
                viewModel.delImport();
                viewModel.delAuthData();
            }
        } else {
            MyToastUtils.error("密码错误！", Toast.LENGTH_SHORT);
        }
    }
}
