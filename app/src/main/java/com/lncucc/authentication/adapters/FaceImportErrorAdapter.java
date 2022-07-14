package com.lncucc.authentication.adapters;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.lncucc.authentication.R;

import java.util.List;

/**
 * Created by ymy
 * Description：
 * Date:2022/7/13 14:17
 */
public class FaceImportErrorAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public FaceImportErrorAdapter(List<String> data) {
        super(R.layout.item_face_error, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.tv_face_num, s + ".jpg");
        int position = baseViewHolder.getAdapterPosition();
        if (position % 2 == 0){
            baseViewHolder.setBackgroundColor(R.id.rl_face_error_item, Color.parseColor("#FFFFFF"));
        }else {
            baseViewHolder.setBackgroundColor(R.id.rl_face_error_item, Color.parseColor("#F9F9F9"));
        }
    }
}
