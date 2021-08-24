package com.lncucc.authentication.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lncucc.authentication.R;

public class LogoutDialog extends BaseDialog{
    private View mView;
    private DialogClickBackListener mListener;

    public LogoutDialog(Context context, DialogClickBackListener listener) {
        super(context, R.style.DialogTheme);
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_logout,null);
        setContentView(mView);
        this.mListener = listener;
        initEvent();
    }

    private void initEvent(){
        mView.findViewById(R.id.rl_close).setOnClickListener(v -> dismiss());
        //cancel
        mView.findViewById(R.id.tv_cancel).setOnClickListener(v -> dismiss());
        //confirm
        mView.findViewById(R.id.tv_confirm).setOnClickListener(v -> mListener.backType(0));
    }
}