package com.lncucc.authentication.widgets;

import android.content.Context;
import android.view.View;
import android.widget.TableRow;
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

    /**
     * 设置数据导入进度 解压/数据插入
     * @param progress 当前进度
     * @param message 操作信息
     */
    public void setLoadingProgress(String progress, String message){
        int percent = 0;
        if (progress.contains(".")){
            percent = Integer.parseInt(progress.split("\\.")[0]);
        }else {
            percent = Integer.parseInt(progress);
        }
        progressBar.setProgress(percent,true);
        tvProgress.setText(percent + "%");
        textView.setText(message);
    }

    /**
     * 清除进度
     */
    public void clearLoadingProgress(){
        progressBar.setProgress(0, true);
        tvProgress.setText("0%");
        textView.setText("");
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
    }
}
