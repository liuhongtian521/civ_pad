package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.event.DataImportEvent;
import com.blankj.utilcode.util.LogUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.DataViewAdapter;
import com.lncucc.authentication.databinding.FragmentDataViewBinding;
import com.lncucc.authentication.widgets.StudentInfoDialog;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

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
            LogUtils.e("item id ->", DBOperation.getStudentInfo(tempList.get(position).getId()));
            itemInfo = DBOperation.getStudentInfo(tempList.get(position).getId());
            if (infoDialog != null && itemInfo != null){
                infoDialog.showDialog(itemInfo);
            }
        });
        RxBus2.getInstance().register(this);
        viewBinding.rlDataView.setAdapter(mAdapter);
    }

    @Override
    public void onInitViewModel() {

    }

    @Subscribe(receiveStickyEvent = true)
    public void onImportDataEvent(DataImportEvent event){
        LogUtils.e("data import ->",event);
        if (event.getCode() == 0){
            if (tempList.size() == 0){
                mList = DBOperation.getDBExamLayoutByIdNo(viewBinding.editExamNumber.getText().toString());
                if (mList.size() > 0){
                    tempList.addAll(mList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
    }
}
