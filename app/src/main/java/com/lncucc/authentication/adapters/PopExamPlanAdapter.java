package com.lncucc.authentication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.lncucc.authentication.R;

import java.util.List;

/**
 * Create bt she:
 *
 * @date 2021/8/16
 */
public class PopExamPlanAdapter extends RecyclerView.Adapter<PopExamPlanAdapter.ViewHolder> {


    Context context;
    List<DBExamPlan> list;
    int chooseInt;
    OnItemListener onItemListener;

    public PopExamPlanAdapter(Context context, List<DBExamPlan> list, int chooseInt, OnItemListener onItemListener) {
        this.context = context;
        this.list = list;
        this.chooseInt = chooseInt;
        this.onItemListener = onItemListener;
    }

    public void setChooseInt(int chooseInt) {
        this.chooseInt = chooseInt;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pop_list, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.e("TagSnake", position + "::" + chooseInt);

        if (position == chooseInt) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.tvItemName.setText(list.get(position).getExamName());

        holder.lineBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.onItemClick(position);
            }
        });
//        line_bg_itm
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lineBg;
        TextView tvItemName;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            lineBg = itemView.findViewById(R.id.line_bg_itm);

            tvItemName = itemView.findViewById(R.id.tv_item_pop_list);
            checkBox = itemView.findViewById(R.id.check_item_pop);
        }
    }

    public interface OnItemListener {
        void onItemClick(int item);
    }

}
