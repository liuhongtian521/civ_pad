package com.lncucc.authentication.widgets.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.blankj.utilcode.util.LogUtils;
import com.lncucc.authentication.R;
import com.ttsea.jrxbus2.RxBus2;

import java.util.List;

import jsc.kit.wheel.base.WheelItem;
import jsc.kit.wheel.base.WheelItemView;

/**
 * 底部弹出 popup
 */

public class BottomPopUpWindow extends PopupWindow {

    private Context mContext;
    private List<DBExamArrange> mList;
    private WheelItemView wheelView;

    private TextView textView;

    public BottomPopUpWindow(Context context, List<DBExamArrange> list,String headText) {
        this.mContext = context;
        this.mList = list;
        initView(headText);
    }

    private void initView(String headText) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pop_arrange, null);
        view.findViewById(R.id.rl_close).setOnClickListener(v -> dismiss());
        textView = view.findViewById(R.id.head_info);
        if(null!=headText&&!headText.isEmpty()){
            textView.setText(headText);
        }
        view.findViewById(R.id.tv_confirm).setOnClickListener(v ->
            {
                dismiss();
                RxBus2.getInstance().post(0, wheelView.getSelectedIndex() + "");
            }
        );

        wheelView = view.findViewById(R.id.wheel);
        if (mList != null && mList.size() > 0) {
            WheelItem[] items = new WheelItem[mList.size()];
            for (int i = 0; i < mList.size(); i++) {
                items[i] = new WheelItem(mList.get(i).getSeName());
            }
            wheelView.setItems(items);
        }
        setOutsideTouchable(true);
        setContentView(view);
        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_photo_anim);
    }


}
