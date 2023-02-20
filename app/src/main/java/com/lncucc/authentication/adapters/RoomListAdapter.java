package com.lncucc.authentication.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.askia.coremodel.datamodel.data.StudentBean;
import com.lncucc.authentication.R;

import java.util.List;

/**
 * Created by ymy
 * Description：验证数据统计详情列表，考场按7887排列，考场没有超过30人的情况。
 * Date:2023/2/17 10:57
 */
public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.MyViewHolder> {

    private List<StudentBean> mDataList;

    public RoomListAdapter(List<StudentBean> dataList){
        //填充占位数据
        if (dataList.size() > 7 && dataList.size() < 30){
            dataList.add(7,new StudentBean());
        }else if (dataList.size() == 30){
            dataList.add(7,new StudentBean());
            dataList.add(31,new StudentBean());
        }
        this.mDataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // 7 8 8 7排列
        //绑定数据
        holder.bindData(mDataList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return 32; //固定 7887 所以写死
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void bindData(StudentBean bean,int position){
            if (position == 7 || position == 31){
                itemView.setBackgroundColor(Color.parseColor("#FF5F5F"));
            }else {
                ((TextView)itemView.findViewById(R.id.tv_stu_num)).setText(bean.getSeatNo());
                ((TextView)itemView.findViewById(R.id.tv_stu_name)).setText(bean.getName());
            }

        }
    }
}
