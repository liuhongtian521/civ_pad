package com.lncucc.authentication.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.data.DataImportBean;
import com.blankj.utilcode.util.StringUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.DataImportDialogAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class DataImportDialog extends BaseDialog {
    private View mView;
    private RecyclerView mRecyclerView;
    private DataImportDialogAdapter mAdapter;
    private List<DataImportBean> mList = new ArrayList<>();
    private Context mContext;
    private TextView tvCancel;
    private TextView tvOk;
    private DataImportDialogListener mClickListener;
    private int index;

    public DataImportDialog(@NonNull @NotNull Context context, DataImportDialogListener clickBackListener) {
        super(context);
        this.mContext = context;
        this.mClickListener = clickBackListener;
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_import_list, null);
        setCanceledOnTouchOutside(false);
        setContentView(mView);
        initView();
        initEvent();
    }

    private void initView() {
        tvCancel = mView.findViewById(R.id.tv_pwd_cancel);
        tvOk = mView.findViewById(R.id.tv_pwd_confirm);
        mRecyclerView = mView.findViewById(R.id.recycler_view);
        mAdapter = new DataImportDialogAdapter(mList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initEvent() {
        //dialog dismiss
        mView.findViewById(R.id.rl_close).setOnClickListener(v -> dismiss());

        tvCancel.setOnClickListener(v -> mClickListener.cancel());

        tvOk.setOnClickListener(v -> {
            if (mList.size() == 0){
                MyToastUtils.error("当前文件夹内没有数据包，请检查后重试!",Toast.LENGTH_SHORT);
            }else {
                mClickListener.confirm(index);
            }
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            index = position;
            for (int i = 0; i < mList.size(); i++) {
                mList.get(i).setChecked(i == position);
            }
            mAdapter.notifyDataSetChanged();
        });
    }


    public void showImportDialog(int type, List<DataImportBean> list) {
        mList.clear();
        mList.addAll(list);
        mAdapter.notifyDataSetChanged();
        if (type == 0) {
            tvCancel.setText("跳过");
        }
        this.show();
    }
}
