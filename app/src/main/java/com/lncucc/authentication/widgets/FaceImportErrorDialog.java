package com.lncucc.authentication.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.FaceImportErrorAdapter;

import java.util.List;

/**
 * Created by ymy
 * Description：
 * Date:2022/7/12 13:22
 */
public class FaceImportErrorDialog extends BaseDialog{

    private View mView;
    private DialogClickBackListener mListener;
    private List<String> mList;
    private Context mContext;
    private RecyclerView recyclerView;
    private FaceImportErrorAdapter adapter;

    public FaceImportErrorDialog(@NonNull Context context, DialogClickBackListener listener, List<String> list) {
        super(context, R.style.DialogTheme);
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_re_import,null);
        setContentView(mView);
        this.mListener = listener;
        this.mList = list;
        this.mContext = context;
        initEvent();
    }

    private void initEvent(){
        mView.findViewById(R.id.rl_close).setOnClickListener(v -> dismiss());
        //confirm
        mView.findViewById(R.id.tv_close).setOnClickListener(v -> mListener.backType(0));

        recyclerView = mView.findViewById(R.id.rl_error_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new FaceImportErrorAdapter(mList);
        recyclerView.setAdapter(adapter);
    }
}
