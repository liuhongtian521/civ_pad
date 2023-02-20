package com.lncucc.authentication.activitys;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.datamodel.data.StudentBean;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.RoomListAdapter;
import com.lncucc.authentication.databinding.ActRoomListBinding;

import java.util.List;

/**
 * Created by ymy
 * Description：
 * Date:2023/2/17 14:35
 */
@Route(path = ARouterPath.ROOM_LIST)
public class RoomListActivity extends BaseActivity {
    private ActRoomListBinding mBinding;
    String roomNo,seCode,examCode;
    private List<StudentBean> mDataList;
    private RoomListAdapter roomAdapter;

    @Override
    public void onInit() {
        roomNo = getIntent().getStringExtra("roomNo");
        seCode = getIntent().getStringExtra("seCode");
        examCode = getIntent().getStringExtra("examCode");
        if (null != roomNo){
            mBinding.tvRoom.setText("考场"+roomNo);
        }
        mBinding.llBack.setOnClickListener(v -> finish());
        mDataList = DBOperation.queryStudentByRoomAndSeCode(examCode,seCode,roomNo);
        roomAdapter = new RoomListAdapter(mDataList);
        mBinding.recycleStudent.setAdapter(roomAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        mBinding.recycleStudent.setLayoutManager(layoutManager);
    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public void onInitDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_room_list);
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
