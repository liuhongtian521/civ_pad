package com.lncucc.authentication.adapters;

import android.widget.CheckBox;

import com.askia.coremodel.datamodel.data.DataImportListBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DataImportAdapter extends BaseQuickAdapter<DataImportListBean, BaseViewHolder> {


    public DataImportAdapter(@Nullable List<DataImportListBean> data) {
        super(R.layout.item_data_import, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DataImportListBean dataImportListBean) {
        baseViewHolder.setText(R.id.tv_item_title,dataImportListBean.getTitle());
        CheckBox checkBox = baseViewHolder.itemView.findViewById(R.id.check);
        checkBox.setChecked(dataImportListBean.isChecked());
    }
}
