package com.lncucc.authentication.fragments;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.blankj.utilcode.util.LogUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.DataViewAdapter;
import com.lncucc.authentication.adapters.SessionAdapter;
import com.lncucc.authentication.databinding.FragmentDataViewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据查看
 */
public class DataViewFragment extends BaseFragment {
    private FragmentDataViewBinding viewBinding;
    private List<DBExamLayout> mList;
    private List<DBExamLayout> tempList = new ArrayList<>();
    private DataViewAdapter mAdapter;

    @Override
    public void onInit() {
        mList = DBOperation.getDBExamLayoutByIdNo("");
        tempList.addAll(mList);
        viewBinding.rlDataView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DataViewAdapter(tempList);
        viewBinding.rlDataView.setAdapter(mAdapter);
    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(LayoutInflater inflater, ViewGroup container) {
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_view, container, false);
        viewBinding.setHandles(this);
        return viewBinding.getRoot();
    }

    public void query(View view) {
        String queryParams = viewBinding.editExamNumber.getText().toString().trim();
        mList = DBOperation.getDBExamLayoutByIdNo(queryParams);
        if (mList.size() > 0) {
            tempList.clear();
            tempList.addAll(mList);
            mAdapter.notifyDataSetChanged();
        } else {
            MyToastUtils.error("没有查询到该考生信息！", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
