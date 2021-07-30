package com.askia.common.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;

import com.askia.common.R;
import com.askia.common.util.Utils;
import com.wang.avi.AVLoadingIndicatorView;
import com.yanyusong.y_divideritemdecoration.Dp2Px;


public class NetLoadingDialog extends AlertDialog {
    ImageView img;
    private Context mContext;
    private AVLoadingIndicatorView avi;
    private TextView mTvHint;
    public NetLoadingDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public NetLoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mContext = context;

    }

    public NetLoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;

    }

    public static NetLoadingDialog show(Context context, boolean cancelable, OnCancelListener cancelListener) {
        NetLoadingDialog dialog = new NetLoadingDialog(context, cancelable, cancelListener);
        dialog.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    dialog.cancel();
                }
                return false;
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }


    public static NetLoadingDialog show(Context context, boolean cancelable, OnCancelListener cancelListener, String hint) {
        NetLoadingDialog dialog = new NetLoadingDialog(context, cancelable, cancelListener);
        dialog.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    dialog.cancel();
                }
                return false;
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.w_dialog_net_loading);
        WindowManager.LayoutParams lp;
        lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.dimAmount = 0; // 去背景遮盖
        lp.alpha = 1.0f;
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        getWindow().setAttributes(lp);

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        mTvHint = (TextView) findViewById(R.id.tv_hint);


        avi.show();
        // WImageLoader.getInstance().loadResImage(getContext(), com.ryan.baselibrary.R.drawable.loading,img);
    }

}
