package com.lncucc.authentication.activitys;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActChooseVenueBinding;

/**
 * Create bt she:
 *选择考场
 * @date 2021/8/5
 */
@Route(path = ARouterPath.CHOOSE_VENVE)
public class ChooseVenveActivity extends BaseActivity {
    private ActChooseVenueBinding mDataBinding;
    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public void onInitDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.act_choose_venue);
        mDataBinding.setHandlers(this);
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
