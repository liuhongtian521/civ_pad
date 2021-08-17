package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.operation.LogsUtil;
import com.blankj.utilcode.util.LogUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentLogClearBinding;
import com.lncucc.authentication.widgets.ConfirmDialog;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.ttsea.jrxbus2.RxBus2;

import org.jetbrains.annotations.NotNull;

import io.realm.Realm;

public class LogsClearFragment extends BaseFragment implements DialogClickBackListener {
    private FragmentLogClearBinding clearBinding;
    private ConfirmDialog confirmDialog;

    @Override
    public void onInit() {
        confirmDialog = new ConfirmDialog(getActivity(), this,getString(R.string.logs_clear_title),getString(R.string.logs_clear_content));
    }

    @Override
    public void onInitViewModel() {

    }

    public void clear(View view){
        if (confirmDialog != null){
            confirmDialog.show();
        }
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

    @Override
    public void dissMiss() {

    }

    @Override
    public void backType(int type) {
        if (type == 0){
            Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.deleteAll(), () -> {
                confirmDialog.dismiss();
                MyToastUtils.success("清除成功", Toast.LENGTH_SHORT);
            });
        }
    }
}
