package com.lncucc.authentication.adapters;


import android.widget.CheckBox;

import com.askia.coremodel.datamodel.data.DataImportBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 数据导入dialog
 */
public class DataImportDialogAdapter extends BaseQuickAdapter<DataImportBean, BaseViewHolder> {
    public DataImportDialogAdapter(List<DataImportBean> data) {
        super(R.layout.item_dialog_data_import,data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DataImportBean dataImportBean) {
        baseViewHolder.setText(R.id.tv_item_title,dataImportBean.getFileName());
        CheckBox checkBox = baseViewHolder.itemView.findViewById(R.id.check);
        checkBox.setChecked(dataImportBean.isChecked());
    }
}
