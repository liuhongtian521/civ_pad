package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.askia.common.base.BaseFragment;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.blankj.utilcode.util.LogUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.SessionAdapter;
import com.lncucc.authentication.databinding.FragmentSessionBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 场次查看
 */
public class SessionFragment extends BaseFragment {
    private FragmentSessionBinding mBinding;
    private List<DBExamArrange> mlist;

    @Override
    public void onInit() {
        mlist = DBOperation.getDBExamArrange();
        mBinding.examRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        SessionAdapter adapter = new SessionAdapter(mlist);
        mBinding.examRecycler.setAdapter(adapter);
    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_session,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
