package com.lncucc.authentication.activitys;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActLogsBinding;

/**
 * 日志信息
 */
@Route(path = ARouterPath.LOGS_ACTIVITY)
public class LogActivity extends BaseActivity {
    private ActLogsBinding logsBinding;

    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public void onInitDataBinding() {
        logsBinding = DataBindingUtil.setContentView(this, R.layout.act_logs);
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
