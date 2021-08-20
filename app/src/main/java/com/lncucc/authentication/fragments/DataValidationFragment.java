package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.ValidationDataAdapter;
import com.lncucc.authentication.databinding.FragmentDataValidationBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证数据查看
 */
public class DataValidationFragment extends BaseFragment {
    private FragmentDataValidationBinding mBinding;
    private List<DBExamExport> mList;
    private ValidationDataAdapter mAdapter;
    private List<DBExamExport> tempList = new ArrayList<>();

    @Override
    public void onInit() {
        mList = DBOperation.getVerifyList();
        tempList.clear();
        tempList.addAll(mList);
        mBinding.rlDataView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ValidationDataAdapter(mList);
        mBinding.rlDataView.setAdapter(mAdapter);
    }

    @Override
    public void onInitViewModel() {

    }

    public void query(View view) {
        String queryParams = mBinding.editExamNumber.getText().toString().trim();
        mList = DBOperation.getDBExportByIdNo(queryParams);
        if (mList.size() > 0) {
            tempList.clear();
            tempList.addAll(mList);
            mAdapter.notifyDataSetChanged();
        } else {
            MyToastUtils.error("没有查询到该考生信息！", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_validation,container,false);
        mBinding.setHandles(this);
        return mBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
