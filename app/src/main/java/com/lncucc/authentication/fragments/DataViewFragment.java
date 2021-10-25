package com.lncucc.authentication.fragments;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.DataViewAdapter;
import com.lncucc.authentication.databinding.FragmentDataViewBinding;
import com.lncucc.authentication.widgets.StudentInfoDialog;

import org.jetbrains.annotations.NotNull;

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
    private LinearLayoutManager mLinearLayoutManager;
    private int page = 1;
    private final int pageSize = 15;

    @Override
    public void onInit() {
        initData();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        viewBinding.rlDataView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new DataViewAdapter(getActivity(), tempList);
        infoDialog = new StudentInfoDialog(getActivity(), itemInfo);

        mAdapter.setItemClickListener(position -> {
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

    private void queryStudent() {
        page = 1;
        String queryParams = viewBinding.editExamNumber.getText().toString().trim();
        mList = DBOperation.getDBExamLayoutByIdNo(queryParams);
        if (mList.size() > 0) {
            tempList.clear();
            if (mList.size() >= pageSize) {
                tempList.addAll(mList.subList(0, pageSize));
            } else {
                tempList.addAll(mList);
            }
            mAdapter.notifyDataSetChanged();
        } else {
            MyToastUtils.error("没有查询到该考生信息！", Toast.LENGTH_SHORT);
        }
        KeyboardUtils.hideSoftInput(getActivity());
    }

    private void initEvent() {

        if (tempList.size() > 0){
            viewBinding.rlDataView.setLoadingMoreEnabled(true);
        }else {
            viewBinding.rlDataView.setLoadingMoreEnabled(false);
        }
//        viewBinding.rlDataView.setLoadingMoreEnabled(true);
        viewBinding.rlDataView.setLoadingMoreProgressStyle(ProgressStyle.BallScaleRipple);
        viewBinding.rlDataView.getDefaultFootView().setLoadingDoneHint("加载完成");
        viewBinding.rlDataView.getDefaultFootView().setNoMoreHint("没有更多数据了");
        viewBinding.rlDataView.getDefaultFootView().setLoadingHint("加载中...");
        viewBinding.rlDataView.setPullRefreshEnabled(false);
        viewBinding.editExamNumber.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                queryStudent();
            }
            return false;
        });

        viewBinding.rlDataView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                LogUtils.e("onLoadMore ->", "load more");
                page++;
                loadMore();
            }
        });


    }


    @Override
    public void onSubscribeViewModel() {

    }

    private void loadMore() {

        handler.postDelayed(() -> {
            int total = mList.size();
            int current = tempList.size();
            Message message = new Message();
            if (current >= total) {
                message.what = 0;
                handler.sendMessage(message);
            } else {
                int index = (page - 1) * pageSize;
                int toIndex = page * pageSize;
                if (page * pageSize > total) {
                    toIndex = total - (page - 1) * pageSize + current;
//                    toIndex = total;
                }
                List<DBExamLayout> list = mList.subList(index, toIndex);
                tempList.addAll(list);
                message.what = 1;
                handler.sendMessage(message);
            }
        }, 1000);
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            super.handleMessage(msg);
            viewBinding.rlDataView.loadMoreComplete();
            if (msg.what == 0){
                page--;
                MyToastUtils.error("没有更多数据了！",Toast.LENGTH_SHORT);
            }else {
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        mList = DBOperation.getDBExamLayoutByIdNo(viewBinding.editExamNumber.getText().toString());
        tempList.clear();
        if (mList.size() >= pageSize) {
            tempList.addAll(mList.subList(0, pageSize));
        } else {
            tempList.addAll(mList);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (viewBinding != null) {
                page = 1;
                initData();
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
