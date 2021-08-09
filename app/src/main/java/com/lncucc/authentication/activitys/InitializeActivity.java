package com.lncucc.authentication.activitys;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.viewmodel.InitializeViewModel;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActInitializeBinding;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * 初始化
 */
@Route(path = ARouterPath.INITIALIZE_ACTIVITY)
public class InitializeActivity extends BaseActivity {

    private ActInitializeBinding actInitializeBinding;
    private InitializeViewModel initializeViewModel;
    private Disposable mDisposable;

    @Override
    public void onInit() {
        //计时器
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NotNull Long aLong) {
                        if (aLong <= 100){
                            actInitializeBinding.qmProcess.setProgress(Integer.parseInt(aLong + ""));
                            actInitializeBinding.tvProgress.setText(aLong + "%");
                        }else {
                            startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
                            finish();
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        //请求接口判断是否有新数据，有数据更新
        //有更新下载数据 解压 建表
        //无更新 判断本地是否有数据 无->管理员设置页 有->判断是否在识别时间范围内 在范围内？人脸识别页面: 进入首页
    }

    /**
     * 请求接口 获取更新数据
     */
    private void checkDataByNet(){

    }

    /**
     * 检查本地数据
     */
    private void checkDataByLocal(){
        //检查本地数据
        if (initializeViewModel.hasExaData()){
            //判断时间范围
            //获取当前时间戳
            long currentTime = System.currentTimeMillis();
            //获取验证开始时间
            //获取验证结束时间


        }else {
            //管理员设置页
            startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
            this.finish();
        }
    }

    @Override
    public void onInitViewModel() {
        initializeViewModel = ViewModelProviders.of(this).get(InitializeViewModel.class);
    }

    @Override
    public void onInitDataBinding() {
        actInitializeBinding = DataBindingUtil.setContentView(this, R.layout.act_initialize);
    }

    @Override
    public void onSubscribeViewModel() {

    }
}
