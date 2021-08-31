package com.lncucc.authentication.fragments;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.blankj.utilcode.util.KeyboardUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.ValidationDataAdapter;
import com.lncucc.authentication.databinding.FragmentDataValidationBinding;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.PeopleMsgDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 验证数据查看
 */
public class DataValidationFragment extends BaseFragment implements DialogClickBackListener {
    private FragmentDataValidationBinding mBinding;
    private List<DBExamExport> mList;
    private ValidationDataAdapter mAdapter;
    private List<DBExamExport> tempList = new ArrayList<>();
    private PeopleMsgDialog peopleMsgDialog;

    @Override
    public void onInit() {
        mList = DBOperation.getVerifyList();
        tempList.clear();
        tempList.addAll(mList);
        mBinding.rlDataView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ValidationDataAdapter(tempList);
        mBinding.rlDataView.setAdapter(mAdapter);

        peopleMsgDialog = new PeopleMsgDialog(getActivity(),this);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            DBExamExport examExport = DBOperation.getExamportById(tempList.get(position).getId());
            peopleMsgDialog.setMsg(examExport);
            peopleMsgDialog.show();
        });

        mBinding.editExamNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    query(null);
                }
                return false;
            }
        });
    }

    @Override
    public void onInitViewModel() {

    }

    public void query(View view) {
        String queryParams = mBinding.editExamNumber.getText().toString().trim();
        mList = DBOperation.getDBExportByIdNo(queryParams);
        KeyboardUtils.hideSoftInput(getActivity());
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

    @Override
    public void dissMiss() {
        peopleMsgDialog.dismiss();
    }

    @Override
    public void backType(int type) {

    }
}
