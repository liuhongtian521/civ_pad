package com.lncucc.authentication.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.utils.TextUtils;
import com.askia.common.util.MyToastUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.lncucc.authentication.R;


public class PassWordDialog extends BaseDialog{

    private View mView;
    private PassWordClickCallBack callback;
    private EditText editText;
    private Context mContext;

    public PassWordDialog(Context context, PassWordClickCallBack listener) {
        super(context);
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_pwd,null);
        this.mContext = context;
        this.callback = listener;
        setContentView(mView);
        setCanceledOnTouchOutside(false);
        editText = mView.findViewById(R.id.edt_confirm_pwd);

        mView.findViewById(R.id.rl_close).setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(getWindow().getDecorView());
            callback.dismiss();
        });
        //cancel
        mView.findViewById(R.id.tv_pwd_cancel).setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(getWindow().getDecorView());
            callback.dismiss();
        });
        //confirm
        mView.findViewById(R.id.tv_pwd_confirm).setOnClickListener(v -> {
            String pwd = editText.getText().toString();
            if (TextUtils.isEmpty(pwd)){
                MyToastUtils.error("请输入密码", Toast.LENGTH_SHORT);
            }else {
                callback.confirm(pwd);
            }
        });
    }

    public void clearPwd(){
        editText.setText("");
    }
}
