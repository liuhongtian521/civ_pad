package com.lncucc.authentication.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


import com.lncucc.authentication.R;

import kotlin.jvm.internal.PropertyReference0Impl;


public class ConfirmDialog extends BaseDialog{
    private View mView;
    private DialogClickBackListener mListener;

    public ConfirmDialog(Context context,DialogClickBackListener listener) {
        super(context, R.style.DialogTheme);
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm,null);
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
