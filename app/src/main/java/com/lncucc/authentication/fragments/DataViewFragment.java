package com.lncucc.authentication.fragments;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
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
import com.lncucc.authentication.databinding.FragmentDataViewBinding;
import com.lncucc.authentication.widgets.StudentInfoDialog;

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
    private StudentInfoDialog infoDialog;
    private DBExamLayout itemInfo;

    @Override
    public void onInit() {
        mList = DBOperation.getDBExamLayoutByIdNo(viewBinding.editExamNumber.getText().toString());
        tempList.clear();
        tempList.addAll(mList);
        viewBinding.rlDataView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DataViewAdapter(tempList);
        infoDialog = new StudentInfoDialog(getActivity(), itemInfo);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            itemInfo = DBOperation.getStudentInfo(tempList.get(position).getId());
            if (infoDialog != null && itemInfo != null) {
                infoDialog.showDialog(itemInfo);
            }
        });
        viewBinding.rlDataView.setAdapter(mAdapter);
        initEvent();
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
        queryStudent();
    }

    private void queryStudent(){
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

    private void initEvent(){
        viewBinding.editExamNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    queryStudent();
                }
                return false;
            }
        });
    }



    @Override
    public void onSubscribeViewModel() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (viewBinding != null) {
                mList = DBOperation.getDBExamLayoutByIdNo(viewBinding.editExamNumber.getText().toString());
                tempList.clear();
                tempList.addAll(mList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
