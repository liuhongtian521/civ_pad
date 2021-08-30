package com.lncucc.authentication.widgets;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lncucc.authentication.R;
import com.qmuiteam.qmui.widget.QMUIProgressBar;

import org.jetbrains.annotations.NotNull;

public class DataLoadingDialog extends BaseDialog{
    private View mView;
    private QMUIProgressBar progressBar;
    private TextView textView;
    private TextView tvProgress;

    public DataLoadingDialog(@NonNull @NotNull Context context) {
        super(context);
        mView = getLayoutInflater().inflate(R.layout.dialog_data_loading,null);
        setContentView(mView);
        initView();
    }

    private void initView(){
        progressBar = mView.findViewById(R.id.progressBar);
        textView = mView.findViewById(R.id.tv_loading_msg);
        tvProgress = mView.findViewById(R.id.tv_progress);
    }

    public void setLoadingProgress(int progress, String message){
        progressBar.setProgress(progress,true);
        tvProgress.setText(progress + "%");
        textView.setText(message);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }
}
