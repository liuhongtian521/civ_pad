package com.lncucc.authentication.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lncucc.authentication.R;

/**
 * Created by ymy
 * Description：网络导出dialog，提示本地未上传比对数据数量
 * Date:2022/10/12 17:05
 */
public class ExportByNetDialog extends BaseDialog{
    private View mView;
    private DialogClickBackListener mListener;

    public ExportByNetDialog(Context context, DialogClickBackListener listener, String content) {
        super(context, R.style.DialogTheme);
        mView = LayoutInflater.from(context).inflate(R.layout.dialog_export,null);
        setContentView(mView);
        this.mListener = listener;
        //content
        ((TextView)mView.findViewById(R.id.tv_data_tips)).setText(content);
        initEvent();
    }

    private void initEvent(){
        mView.findViewById(R.id.rl_close).setOnClickListener(v -> mListener.dissMiss());
        //cancel
        mView.findViewById(R.id.tv_cancel).setOnClickListener(v -> mListener.dissMiss());
        //confirm
        mView.findViewById(R.id.tv_confirm).setOnClickListener(v -> mListener.backType(0));
    }


}
