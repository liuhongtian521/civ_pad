package com.lncucc.authentication.activitys;

import android.view.View;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActDateSettingBinding;

public class DateSettingActivity extends BaseActivity {

    private ActDateSettingBinding dateSettingBinding;

    @Override
    public void onInit() {
        ((TextView) findViewById(R.id.tv_title)).setText("时间校正");
        findViewById(R.id.rl_left_back).setVisibility(View.GONE);
    }

    @Override
    public void onInitViewModel() {

    }

    public void loginSystem(View view) {
        startActivityByRouter(ARouterPath.LOGIN_ACTIVITY);
    }

    @Override
    public void onInitDataBinding() {
        dateSettingBinding = DataBindingUtil.setContentView(this, R.layout.act_date_setting);
        dateSettingBinding.setHandles(this);
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
