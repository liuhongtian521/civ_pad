package com.lncucc.authentication.activitys;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActAuthenticationBinding;

/**
 * Create bt she:
 * 身份验证
 *
 * @date 2021/8/5
 */
@Route(path = ARouterPath.IDENTIFY_ACTIVITY)
public class AuthenticationActivity extends BaseActivity {

    private ActAuthenticationBinding mDataBinding;

    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public void onInitDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.act_authentication);
        mDataBinding.setHandlers(this);
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
