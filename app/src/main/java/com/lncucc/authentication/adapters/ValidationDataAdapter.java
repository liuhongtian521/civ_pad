package com.lncucc.authentication.adapters;

import android.graphics.Color;

import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ValidationDataAdapter extends BaseQuickAdapter<DBExamExport, BaseViewHolder> {

    public ValidationDataAdapter(@Nullable List<DBExamExport> data) {
        super(R.layout.item_validation_data, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, DBExamExport s) {
        baseViewHolder.setText(R.id.tv_stu_name, TimeUtils.millis2String(Long.parseLong(s.getVerifyTime())));
        baseViewHolder.setText(R.id.tv_stu_num, s.getStuName());
        baseViewHolder.setText(R.id.tv_stu_exa_num, s.getIdCard());
        String healthStatus = "";
        switch (s.getHealthCode()){
            case "0":
                healthStatus = "绿码";
                break;
            case "1":
                healthStatus = "黄码";
                break;
            case "2":
                healthStatus = "红码";
                break;
            case "3":
            case "":
                healthStatus = "未知";
                break;
        }
        baseViewHolder.setText(R.id.tv_item_health_code,healthStatus);
        baseViewHolder.setText(R.id.tv_exam, s.getVerifyResult());
        int position = baseViewHolder.getLayoutPosition();
        String color = "";
        if (position % 2 != 0) {
            color = "#F9F9F9";
        } else {
            color = "#ffffff";
        }
        baseViewHolder.itemView.setBackgroundColor(Color.parseColor(color));

        switch (s.getVerifyResult()) {
            case "1":
                baseViewHolder.setBackgroundResource(R.id.iv_state,R.mipmap.icon_pass);
                baseViewHolder.setText(R.id.tv_exam,"通过");
                break;
            case "2":
                baseViewHolder.setBackgroundResource(R.id.iv_state,R.mipmap.icon_not_pass);
                baseViewHolder.setText(R.id.tv_exam,"未通过");
                break;
            case "3":
                baseViewHolder.setBackgroundResource(R.id.iv_state,R.mipmap.icon_doubt);
                baseViewHolder.setText(R.id.tv_exam,"存疑");
                break;
            default:
                break;

        }
    }


}
