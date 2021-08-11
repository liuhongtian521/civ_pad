package com.lncucc.authentication.adapters;

import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class DataViewAdapter extends BaseQuickAdapter<DBExamLayout, BaseViewHolder> {
    public DataViewAdapter(@Nullable List<DBExamLayout> data) {
        super(R.layout.item_data_view, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DBExamLayout s) {
        baseViewHolder.setText(R.id.tv_stu_no,s.getId());
        baseViewHolder.setText(R.id.tv_stu_name,s.getStuName());
        baseViewHolder.setText(R.id.tv_stu_num,s.getIdCard());
        baseViewHolder.setText(R.id.tv_stu_exa_num,s.getExReNum());
        baseViewHolder.setText(R.id.tv_exam, s.getSeName());
    }


}
