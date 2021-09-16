package com.lncucc.authentication.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.itemclick.ItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DataViewAdapter extends RecyclerView.Adapter<DataViewAdapter.ViewHolder> {

    private Context context;
    private List<DBExamLayout> list;
    private ItemClickListener mItemClickListener;

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;

    }

    public DataViewAdapter(Context context, List<DBExamLayout> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_data_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.tvName.setText(list.get(position).getStuName());
        holder.tvNum.setText(list.get(position).getIdCard());
        holder.tvExamNum.setText(list.get(position).getExReNum());
        holder.tvExam.setText(list.get(position).getSeName());
        holder.linearLayout.setOnClickListener(v -> mItemClickListener.onItemClick(position));
        String color = "";
        if (position % 2 != 0) {
            color = "#F9F9F9";
        } else {
            color = "#ffffff";
        }
        holder.linearLayout.setBackgroundColor(Color.parseColor(color));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvNum;
        TextView tvExamNum;
        TextView tvExam;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_stu_name);
            tvNum = itemView.findViewById(R.id.tv_stu_num);
            tvExamNum = itemView.findViewById(R.id.tv_stu_exa_num);
            tvExam = itemView.findViewById(R.id.tv_exam);
            linearLayout = itemView.findViewById(R.id.ll_item_container);
        }
    }
}
