package com.lncucc.authentication.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lncucc.authentication.R;

/**
 * Created by ymy
 * Description：
 * Date:2022/11/23 10:20
 */
public class UpDateDialog extends BaseDialog{
    private View mView;
    private DialogClickBackListener mListener;

    public UpDateDialog(@NonNull Context context,DialogClickBackListener listener) {
        super(context, R.style.DialogTheme);
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_update,null);
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
