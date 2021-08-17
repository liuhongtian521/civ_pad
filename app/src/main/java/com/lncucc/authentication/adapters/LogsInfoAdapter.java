package com.lncucc.authentication.adapters;

import com.askia.coremodel.datamodel.database.db.DBLogs;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 日志信息adapter
 */
public class LogsInfoAdapter extends BaseQuickAdapter<DBLogs, BaseViewHolder> {

    public LogsInfoAdapter(@Nullable List<DBLogs> data) {
        super(R.layout.item_logs_info, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DBLogs dbLogs) {
            baseViewHolder.setText(R.id.tv_date,dbLogs.getOperationTime());
            baseViewHolder.setText(R.id.tv_operation,dbLogs.getOperationInstruction());
    }
}
