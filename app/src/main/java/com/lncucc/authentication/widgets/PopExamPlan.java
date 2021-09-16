package com.lncucc.authentication.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.askia.common.widget.SpacesItemDecoration;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.PopExamPlanAdapter;
import com.lncucc.authentication.generated.callback.OnClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Create bt she:
 * 选择考试计划
 *
 * @date 2021/8/16
 */
public class PopExamPlan extends PopupWindow {
    private Context context;

    List<DBExamPlan> list;

    PopExamPlanAdapter mAdapter;

    int chooseItem = -1;

    private PopListener popListener;

    public PopExamPlan(Context context, PopListener popListener) {
        super(context);
        this.context = context;
        this.popListener = popListener;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mMenuView = inflater.inflate(R.layout.pop_examplan, null);

        ImageView back = mMenuView.findViewById(R.id.iv_close);
        RecyclerView recList = mMenuView.findViewById(R.id.rec_pop_list);
        Button btnSure = mMenuView.findViewById(R.id.btn_pop_sure);

        list = DBOperation.getExamPlan();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseItem < 0) {
                    Toast.makeText(context, "请选择一个考场", Toast.LENGTH_SHORT).show();
                } else {
                    popListener.close(list.get(chooseItem));
                }
            }
        });


        mAdapter = new PopExamPlanAdapter(context, list, chooseItem, new PopExamPlanAdapter.OnItemListener() {
            @Override
            public void onItemClick(int item) {
                Log.e("TagSnake", item + ":item click");
                chooseItem = item;
                mAdapter.setChooseInt(chooseItem);
                mAdapter.notifyDataSetChanged();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(layoutManager);
        //设置Item间距
        recList.addItemDecoration(new SpacesItemDecoration(0));
        // 设置适配器
        recList.setAdapter(mAdapter);


        setContentView(mMenuView);
        setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);
        setAnimationStyle(R.style.PopAnim);
    }

    public void setIndex(String examCode) {
        chooseItem = -1;
        for (int index = 0; index < list.size(); index++) {
            if (list.get(index).getExamCode().equals(examCode)) {
                chooseItem = index;
            }
        }


        mAdapter.setChooseInt(chooseItem);
        mAdapter.notifyDataSetChanged();
    }


    public interface PopListener {
        void close(DBExamPlan dbExamPlan);
    }
}
