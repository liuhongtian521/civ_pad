package com.lncucc.authentication.widgets;

/*
 * Created by 韩闯 on 2018/8/2.
 * 界面：
 **/

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class DialogView extends Dialog {
    //    style引用style样式
    public DialogView(Context context, int width, int height, View layout, int style) {

        super(context, style);

        setContentView(layout);

        Window window = getWindow();

        WindowManager.LayoutParams params = window.getAttributes();

        params.gravity = Gravity.CENTER;

        window.setAttributes(params);
    }
}
