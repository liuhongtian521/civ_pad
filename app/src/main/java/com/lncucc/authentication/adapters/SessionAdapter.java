package com.lncucc.authentication.adapters;

import android.graphics.Color;

import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
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
        String examName = DBOperation.getExamName(s.getExamCode()).getExamName();
        baseViewHolder.setText(R.id.tv_subject, s.getSubName());
        baseViewHolder.setText(R.id.tv_type, s.getSeName());
        baseViewHolder.setText(R.id.tv_name, examName);
        baseViewHolder.setText(R.id.tv_time, s.getStartTime() + "~" + s.getEndTime());
        int position = baseViewHolder.getLayoutPosition();
        String color = "";
        if (position % 2 != 0) {
            color = "#F9F9F9";
        } else {
            color = "#ffffff";
        }
        baseViewHolder.itemView.setBackgroundColor(Color.parseColor(color));
    }


}
