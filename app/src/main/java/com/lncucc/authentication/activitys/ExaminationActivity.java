package com.lncucc.authentication.activitys;


import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.viewmodel.ExaminationViewModel;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActExaminationBinding;

/**
 * 考试管理
 */
@Route(path = ARouterPath.EXAMINIATION_ACTIVITY)
public class ExaminationActivity extends BaseActivity {
    private ActExaminationBinding mExaminationBinding;
    private ExaminationViewModel mExaminationViewModel;

    @Override
    public void onInit() {

    }

    @Override
    public void onInitViewModel() {
        mExaminationViewModel = ViewModelProviders.of(this).get(ExaminationViewModel.class);
    }

    @Override
    public void onInitDataBinding() {
        mExaminationBinding = DataBindingUtil.setContentView(this, R.layout.act_examination);
        mExaminationBinding.setViewmodel(mExaminationViewModel);
        mExaminationBinding.setOnclick(new ProxyClick());
    }

    @Override
    public void onSubscribeViewModel() {

    }

    public class ProxyClick {
        //场次查看
        public void handleExaminationData(){

        }

        //验证数据查看
        public void handleExaminationNow(){

        }
    }
}
