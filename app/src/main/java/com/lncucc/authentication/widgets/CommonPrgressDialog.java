package com.lncucc.authentication.widgets;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lncucc.authentication.R;

public class CommonPrgressDialog extends BaseDialog
{
    View mView;
    private ProgressBar mProgressBar;
    private TextView mTvTitle;
    private TextView mTvDes;

    //    style引用style样式
    public CommonPrgressDialog(Context context)
    {
        super(context, R.style.DialogTheme);
        mView = getLayoutInflater().inflate(R.layout.common_progress_dialog,null);
        setContentView(mView);

        mProgressBar = mView.findViewById(R.id.progress_bar);
        mTvTitle = mView.findViewById(R.id.tv_title);
        mTvDes = mView.findViewById(R.id.tv_des);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }


    @Override
    public void show() {
        mProgressBar.setProgress(0);
        super.show();
    }

    public void setProgress(int a)
    {
        mProgressBar.setProgress(a);
    }

    public void setTitle(String s)
    {
        mTvTitle.setText(s);
    }

    public void setDes(String s)
    {
        mTvDes.setText(s);
    }
}
