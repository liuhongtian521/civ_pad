package com.lncucc.authentication.widgets;

/*
 * Created by 韩闯 on 2018/8/2.
 * 界面：
 **/

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lncucc.authentication.R;

public class ErrorDialog extends BaseDialog
{
    View mView;

    //    style引用style样式
    public ErrorDialog(Context context,String hint)
    {
        super(context,R.style.ErrorDialogTheme);
        mView = getLayoutInflater().inflate(R.layout.dialog_error,null);
        setContentView(mView);

        ((TextView)findViewById(R.id.tv_error_msg)).setText(hint);
        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }
}
