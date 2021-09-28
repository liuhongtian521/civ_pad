package com.lncucc.authentication.activitys;


import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.datamodel.manager.ManagerItemBean;
import com.askia.coremodel.util.AssetsUtil;
import com.askia.coremodel.util.JsonUtil;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.ManagerSettingAdapter;
import com.lncucc.authentication.databinding.ActManagerBinding;

/**
 * 管理员设置
 */
@Route(path = ARouterPath.MANAGER_SETTING_ACTIVITY)
public class ManagerSettingActivity extends BaseActivity {

    private ActManagerBinding mBinding;
    private RecyclerView mRecyclerView;

    @Override
    public void onInit() {
        findViewById(R.id.rl_left_back).setOnClickListener(v-> finish());
        ((TextView)findViewById(R.id.tv_title)).setText("管理员设置");
        mRecyclerView = findViewById(R.id.rl_manager);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        String data = AssetsUtil.getJson(this,"manager.json");
        ManagerItemBean managerBean = JsonUtil.Str2JsonBean(data,ManagerItemBean.class);
        ManagerSettingAdapter adapter = new ManagerSettingAdapter(managerBean.getItem());
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            switch (position){
                case 0:
                    startActivityByRouter(ARouterPath.EXAMINIATION_ACTIVITY);
                    break;
                case 1:
                    startActivityByRouter(ARouterPath.DATA_SERVICE_ACTIVITY);
                    break;
                case 2:
                    startActivityByRouter(ARouterPath.SYSTEM_SETTING);
                    break;
                case 3:
                    startActivityByRouter(ARouterPath.LOGS_ACTIVITY);
                    break;
                case 4:
                    startActivityByRouter(ARouterPath.SYSTEM_TEST);
                    break;
                case 5:
                    startActivityByRouter(ARouterPath.SYSTEM_INFO);
                    break;
                default:
                    break;

            }
        });
    }

    @Override
    public void onInitViewModel() {

    }


    @Override
    public void onInitDataBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.act_manager);
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
