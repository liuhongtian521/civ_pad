package com.lncucc.authentication.activitys;

import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.blankj.utilcode.util.LogUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActScreenBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

@Route(path = ARouterPath.SCREEN_TEST)
public class ScreenTestActivity extends BaseActivity {

    private ActScreenBinding screenBinding;
    public Disposable mDisposable;

    @Override
    public void onInit() {
        //倒计时5秒关闭屏幕测试
        mDisposable = Flowable.intervalRange(0,5,0,1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> LogUtils.e("count down ->", aLong))
                .doOnComplete(() -> {
                    setResult(RESULT_OK);
                    finish();
                })
                .subscribe();
    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public void onInitDataBinding() {
        screenBinding = DataBindingUtil.setContentView(this,R.layout.act_screen);
    }

    @Override
    public void onSubscribeViewModel() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null){
            mDisposable.dispose();
        }
    }
}
