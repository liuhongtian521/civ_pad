package com.lncucc.authentication.activitys;

import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.viewmodel.ExaminationViewModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.ValidationDataAdapter;
import com.lncucc.authentication.databinding.ActDataviewBinding;
import com.lncucc.authentication.databinding.FragmentDataValidationBinding;
import com.lncucc.authentication.widgets.NoSwipeViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.verticaltablayout.VerticalTabLayout;

/**
 * Create bt she:
 *验证数据查看
 * @date 2021/8/5
 */
@Route(path = ARouterPath.DATA_VIEW)
public class DataViewActivity extends BaseActivity {
    private ActDataviewBinding mDataBinding;
    private List<DBExamExport> mList;
    private ValidationDataAdapter mAdapter;
    private List<DBExamExport> tempList = new ArrayList<>();


    @Override
    public void onInit() {
        mList = DBOperation.getVerifyList();
        tempList.clear();
        tempList.addAll(mList);
        mDataBinding.recList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ValidationDataAdapter(mList);
        mDataBinding.recList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {

        });
    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public void onInitDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.act_dataview);
        mDataBinding.setHandlers(this);
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
