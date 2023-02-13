package com.lncucc.authentication.adapters;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by ymy
 * Description：
 * Date:2023/2/9 10:29
 */
public class AdvanceSessionAdapter extends BaseQuickAdapter<DBExamArrange, BaseViewHolder> {

    public AdvanceSessionAdapter(@Nullable List<DBExamArrange> data) {
        super(R.layout.item_fragment_session, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, DBExamArrange s) {
        baseViewHolder.setText(R.id.tv_name, s.getSubName());
        baseViewHolder.setText(R.id.tv_time_start, s.getStartTime());
        baseViewHolder.setText(R.id.tv_time_end, s.getEndTime());
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
