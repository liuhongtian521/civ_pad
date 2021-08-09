package com.lncucc.authentication.adapters;

import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class SessionAdapter extends BaseQuickAdapter<DBExamArrange, BaseViewHolder> {
    public SessionAdapter(@Nullable List<DBExamArrange> data) {
        super(R.layout.item_session, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DBExamArrange s) {
        baseViewHolder.setText(R.id.tv_no,s.getSeCode());
        baseViewHolder.setText(R.id.tv_subject,s.getSubName());
        baseViewHolder.setText(R.id.tv_type,s.getSubName());
        baseViewHolder.setText(R.id.tv_name,s.getSeName());
        baseViewHolder.setText(R.id.tv_time, s.getStartTime() + "~" + s.getEndTime());
    }


}
