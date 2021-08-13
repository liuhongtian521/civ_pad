package com.lncucc.authentication.widgets;

import android.content.Context;
import android.view.View;


import com.lncucc.authentication.R;


public class ConfirmDialog extends BaseDialog{
    private View mView;

    public ConfirmDialog(Context context,String title,String message,DialogClickBackListener listener) {
        super(context, R.style.DialogTheme);
    }


}
