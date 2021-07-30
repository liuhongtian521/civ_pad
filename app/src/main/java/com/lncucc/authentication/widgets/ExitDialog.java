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

import com.askia.common.base.ViewManager;
import com.lncucc.authentication.R;

public class ExitDialog extends BaseDialog
{
    View mView;

    //    style引用style样式
    public ExitDialog(Context context)
    {
        super(context,R.style.DialogTheme);
        mView = getLayoutInflater().inflate(R.layout.dialog_exit,null);
        setContentView(mView);

        findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ViewManager.getInstance().exitApp(context);
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
