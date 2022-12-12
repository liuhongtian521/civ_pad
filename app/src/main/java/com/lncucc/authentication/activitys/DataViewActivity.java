package com.lncucc.authentication.activitys;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.ValidationDataAdapter;
import com.lncucc.authentication.databinding.ActDataviewBinding;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.PeopleMsgDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Create bt she:
 * 验证数据查看
 *
 * @date 2021/8/5
 */
@Route(path = ARouterPath.DATA_VIEW)
public class DataViewActivity extends BaseActivity implements DialogClickBackListener {
    private ActDataviewBinding mDataBinding;
    private List<DBExamExport> mList;
    private ValidationDataAdapter mAdapter;
    private List<DBExamExport> tempList = new ArrayList<>();
    private PeopleMsgDialog peopleMsgDialog;


    @Override
    public void onInit() {
        findViewById(R.id.rel_title1).setOnClickListener(v -> finish());
        String seCode = getIntent().getStringExtra("seCode");
        mList = DBOperation.getVerifyListBySeCode(seCode);
        peopleMsgDialog = new PeopleMsgDialog(this, this);
        tempList.clear();
        tempList.addAll(mList);
        mDataBinding.recList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ValidationDataAdapter(mList);
        mDataBinding.recList.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            DBExamExport examExport = DBOperation.getExamExportById(tempList.get(position).getId());
            peopleMsgDialog.setMsg(examExport);
            peopleMsgDialog.show();
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

    @Override
    public void dissMiss() {
        peopleMsgDialog.dismiss();
    }

    @Override
    public void backType(int type) {

    }
}
