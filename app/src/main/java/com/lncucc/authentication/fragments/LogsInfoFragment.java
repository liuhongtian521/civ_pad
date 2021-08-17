package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.askia.common.base.BaseFragment;
import com.askia.coremodel.datamodel.database.db.DBLogs;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.datamodel.database.operation.LogsUtil;
import com.blankj.utilcode.util.LogUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.LogsInfoAdapter;
import com.lncucc.authentication.adapters.LogsPointAdapter;
import com.lncucc.authentication.databinding.FragmentLoginfoBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 日志信息fragment
 */

public class LogsInfoFragment extends BaseFragment {

    private FragmentLoginfoBinding loginfoBinding;
    private LogsInfoAdapter infoAdapter;
    private LogsPointAdapter pointAdapter;
    private List<DBLogs> mList;


    @Override
    public void onInit() {
        mList = DBOperation.getOperationLogs();
        LogUtils.e("logs list ->", mList);
        infoAdapter = new LogsInfoAdapter(mList);
        pointAdapter = new LogsPointAdapter(mList);
        loginfoBinding.recycleLogs.setLayoutManager(new LinearLayoutManager(getActivity()));
        loginfoBinding.rlPoint.setLayoutManager(new LinearLayoutManager(getActivity()));
        loginfoBinding.recycleLogs.setAdapter(infoAdapter);
        loginfoBinding.rlPoint.setAdapter(pointAdapter);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (loginfoBinding != null){
            mList = DBOperation.getOperationLogs();
            infoAdapter.notifyDataSetChanged();
            pointAdapter.notifyDataSetChanged();
        }
    }
}
