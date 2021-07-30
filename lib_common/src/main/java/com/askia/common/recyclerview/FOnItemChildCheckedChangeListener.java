package com.askia.common.recyclerview;

import android.view.ViewGroup;
import android.widget.CompoundButton;

/**
 * @ClassName FOnItemChildCheckedChangeListener
 * @author  WangHuanyu
 * @todo AdapterView和RecyclerView的item中子控件选中状态变化事件监听器
 * @date 2016/9/13 16:17
 */
public interface FOnItemChildCheckedChangeListener {
    void onItemChildCheckedChanged(ViewGroup parent, CompoundButton childView, int position,
                                   boolean isChecked);
}