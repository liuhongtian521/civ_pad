package com.lncucc.authentication.adapters;

import com.askia.coremodel.datamodel.database.db.DBLogs;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LogsPointAdapter extends BaseQuickAdapter<DBLogs, BaseViewHolder> {

    private List<DBLogs> mList;
    public LogsPointAdapter(@Nullable List<DBLogs> data) {
        super(R.layout.item_logs_point, data);
        this.mList = data;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DBLogs dbLogs) {
        boolean isLast = baseViewHolder.getLayoutPosition() == mList.size() - 1;
        baseViewHolder.setVisible(R.id.v_line,!isLast);
    }
}