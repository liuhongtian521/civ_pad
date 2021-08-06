package com.lncucc.authentication.activitys;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActDataviewBinding;

/**
 * Create bt she:
 *验证数据查看
 * @date 2021/8/5
 */
@Route(path = ARouterPath.DATA_VIEW)
public class DataViewActivity extends BaseActivity {
    private ActDataviewBinding mDataBinding;
    @Override
    public void onInit() {

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
