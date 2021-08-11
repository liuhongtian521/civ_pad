package com.lncucc.authentication.activitys;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.blankj.utilcode.util.LogUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.ChooseVenueAdapter;
import com.lncucc.authentication.adapters.DataViewAdapter;
import com.lncucc.authentication.databinding.ActChooseVenueBinding;

import java.util.List;
import java.util.Objects;

/**
 * Create bt she:
 *选择考场
 * @date 2021/8/5
 */
@Route(path = ARouterPath.CHOOSE_VENVE)
public class ChooseVenveActivity extends BaseActivity {
    private ChooseVenueAdapter mAdapter;
    private ActChooseVenueBinding mDataBinding;
    private List<DBExamLayout> mList;
    @Override
    public void onInit() {
        mDataBinding.llBack.setOnClickListener(v -> finish());
        String seCode = getIntent().getStringExtra("SE_CODE");

        mList = DBOperation.getRoomList(seCode);
        mAdapter = new ChooseVenueAdapter(mList);
        mDataBinding.recChoose.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.recChoose.setAdapter(mAdapter);
        LogUtils.e("fetch room list by seCode->",mList);
    }

    @Override
    public void onInitViewModel() {

    }

    public void confirm(View view){
        int sum = 0;
        for (int i = 0; i < mList.size(); i ++){
            if (((CheckBox) Objects.requireNonNull(mAdapter.getViewByPosition(i, R.id.cx_ex))).isChecked()){
                sum ++;
            }
        }
        Intent intent = new Intent();
        intent.putExtra("count",sum);
        setResult(1,intent);
        finish();
    }

    @Override
    public void onInitDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.act_choose_venue);
        mDataBinding.setHandlers(this);
        mDataBinding.setHandlers(this);
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
