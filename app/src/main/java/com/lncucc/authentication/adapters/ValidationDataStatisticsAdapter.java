package com.lncucc.authentication.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.askia.coremodel.datamodel.data.ExamExportGroupBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;

import java.util.List;

/**
 * Created by ymy
 * Description：验证数据统计adapter
 * Date:2023/2/15 16:16
 */
public class ValidationDataStatisticsAdapter extends BaseQuickAdapter<ExamExportGroupBean, BaseViewHolder> {


    public ValidationDataStatisticsAdapter(@Nullable List<ExamExportGroupBean> data) {
        super(R.layout.item_validation_data_statistics, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ExamExportGroupBean dbExamExport) {
        baseViewHolder.setText(R.id.tv_session, dbExamExport.getRoomNo());
        baseViewHolder.setText(R.id.tv_person_num,dbExamExport.getTotal() + "人");
        baseViewHolder.setText(R.id.tv_not_validation,dbExamExport.getNotValidation() + "");
        baseViewHolder.setText(R.id.tv_validation_pass,dbExamExport.getPassValidation() + "");
        baseViewHolder.setText(R.id.tv_validation_doubt,dbExamExport.getDoubtValidation() + "");
        baseViewHolder.setText(R.id.tv_no_pass,dbExamExport.getNotPassValidation() + "");
    }
}
