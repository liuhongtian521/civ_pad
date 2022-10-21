package com.lncucc.authentication.activitys;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.datamodel.database.repository.ExamItemCheck;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.ChooseVenueAdapter;
import com.lncucc.authentication.callback.VenveItemClick;
import com.lncucc.authentication.databinding.ActChooseVenueBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import io.realm.Realm;

/**
 * Create bt she:
 * 选择考场
 *
 * @date 2021/8/5
 */
@Route(path = ARouterPath.CHOOSE_VENVE)
public class ChooseVenveActivity extends BaseActivity implements VenveItemClick {
    private ChooseVenueAdapter mAdapter;
    private ActChooseVenueBinding mDataBinding;
    private List<DBExamLayout> mList;
    private boolean defaultTag = true;
    private Realm realm;
    private List<ExamItemCheck> mTList = new ArrayList<>();

    @Override
    public void onInit() {
        mDataBinding.llBack.setOnClickListener(v -> finish());
        String seCode = getIntent().getStringExtra("SE_CODE");
        realm = Realm.getDefaultInstance();
        mList = DBOperation.getRoomList(seCode);
        copy2TList();
        mAdapter = new ChooseVenueAdapter(mTList, this);
        mDataBinding.recChoose.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.recChoose.setAdapter(mAdapter);
    }

    private void copy2TList(){
        mTList.clear();
        for (DBExamLayout layout: mList){
            ExamItemCheck d = new ExamItemCheck();
            d.setChecked(layout.isChecked());
            d.setRoomNo(layout.getRoomNo());
            mTList.add(d);
        }
    }

    @Override
    public void onInitViewModel() {
    }

    public void confirm(View view) {
        ArrayList<String> idList = new ArrayList<>();
        for (int i = 0; i < mTList.size(); i++) {
            if (mTList.get(i).isChecked()) {
                idList.add(mTList.get(i).getRoomNo());
                int finalI = i;

            }
            boolean isChecked = mTList.get(i).isChecked();
            int finalI = i;
            realm.executeTransaction(realm -> {
                DBExamLayout layout = mList.get(finalI);
                layout.setChecked(isChecked);
            });
        }


        if (idList.size() == 0){
            MyToastUtils.error("请选择考场！", Toast.LENGTH_SHORT);
            return;
        }

        Intent intent = new Intent();
        intent.putStringArrayListExtra("list", idList);
        setResult(1, intent);
        finish();
    }

    //全部选择按钮 || 取消全选
    public void chooseAll(View view) {

        for (int i = 0; i < mTList.size(); i++) {
            boolean isChecked = mTList.get(i).isChecked();
            //全选 -> 取消全选
            if (defaultTag) {
                if (isChecked) {
                    mTList.get(i).setChecked(!isChecked);
                }
            } else {
                if (!isChecked) {
                    mTList.get(i).setChecked(!isChecked);
                }
            }
        }
        defaultTag = !defaultTag;
        if (defaultTag) {
            mDataBinding.tvChooseAll.setText("取消全选");
        } else {
            mDataBinding.tvChooseAll.setText("全选");
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onInitDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.act_choose_venue);
        mDataBinding.setHandlers(this);
    }

    @Override
    public void onSubscribeViewModel() {

    }

    @Override
    public void onItemClickListener(int position) {
        //全选状态 当前item取消选中改变 按钮文字
       boolean isChecked = !mTList.get(position).isChecked();
       mTList.get(position).setChecked(isChecked);
       if (!isChecked){
           defaultTag = false;
           mDataBinding.tvChooseAll.setText("全选");
       }else {
           boolean isHasChecked = true;
           defaultTag = true;
           for (int i = 0; i < mList.size(); i++) {
               boolean itemChecked = mTList.get(i).isChecked();
               if (!itemChecked){
                   isHasChecked = false;
                   defaultTag = false;
               }
           }

           if (isHasChecked){
               mDataBinding.tvChooseAll.setText("取消全选");
           }
       }
    }
}
