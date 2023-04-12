package com.lncucc.authentication.activitys;

import static com.askia.coremodel.rtc.Constants.FULL_SCREEN_FLAG;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.APP;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.viewmodel.MainViewModel;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActMainBinding;
import com.lncucc.authentication.widgets.ErrorDialog;
import com.lncucc.authentication.widgets.PopExamPlan;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


@Route(path = ARouterPath.MAIN_ACTIVITY)
public class MainActivity extends BaseActivity {
    private ActMainBinding mDataBinding;
    //选择考试计划
    private PopExamPlan mPopExamPlan;
    private MainViewModel mViewModel;
    private String mSeCode;//场次码
    private String mExamCode;
    private boolean isComparison = false;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    long timeStart, timeEnd;
    String mVerifyStartTime, mVerifyEndTime;
    private ArrayList<String> mExamCodeList = new ArrayList<>();
    private Disposable mDisposable;


    private final int TIME = 1000;  //每隔1s执行一次.

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                timer();
                handler.postDelayed(this, TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public void timer() {
        long nowTime = System.currentTimeMillis();
        if (timeStart > 0){
            if (nowTime > timeStart) {
                if (!APP.isInitFaceSuccess) {
                    return;
                }
                navigate2AuthActivity();
            } else {
                long betten = timeStart - nowTime;
                long hour = betten / (1000 * 60 * 60);//小时
                long min = (betten - (hour * 1000 * 60 * 60)) / (1000 * 60);
                long sercend = (betten - (hour * 1000 * 60 * 60) - (min * 1000 * 60)) / 1000;
                mDataBinding.tvTime.setText((hour < 10 ? "0" + hour : hour) + ":" + (min < 10 ? "0" + min : min) + ":" + (sercend < 10 ? "0" + sercend : sercend));
            }
        }
    }

    private void navigate2AuthActivity(){
        //此处添加延时跳转，避免重复跳转
        mDisposable = Flowable.intervalRange(0,2,0,1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> LogUtils.e("count down ->", aLong))
                .doOnComplete(() -> {
                    Bundle _d = new Bundle();
                    _d.putString("mExamCode", mExamCode);
                    _d.putStringArrayList("list", mExamCodeList);
                    _d.putLong("startTIME", timeStart);
                    _d.putLong("endTIME", timeEnd);
                    startActivityByRouter(ARouterPath.IDENTIFY_ACTIVITY, _d);
                    finish();
                })
                .subscribe();
    }

    //修改考试编号
    public void semExamCode(String mExamCode) {
        if (mExamCode == null)
            return;
        timeStart = 0;
        timeEnd = 0;
        mDataBinding.tvSite.setText("");
        mDataBinding.tvTime.setText("");
        mDataBinding.tvExaminationTime.setText("");
        mDataBinding.tvVerificationTime.setText("");
        mDataBinding.tvSuject.setText("");
        this.mExamCode = mExamCode;
        //获取场次数据
        mViewModel.getSiteCode(mExamCode);
        mViewModel.getExamLayout(mExamCode);
    }

    @Override
    public void onInit() {

        if (!APP.isInitFaceSuccess) {
            ErrorDialog errorDialog = new ErrorDialog(this, "人脸扫描初始化失败，无法启动身份认证功能");
            errorDialog.show();
            //Toast.makeText(this, "人脸扫描初始化失败，无法启动身份认证功能", Toast.LENGTH_SHORT).show();
        }

        mPopExamPlan = new PopExamPlan(this, new PopExamPlan.PopListener() {
            @Override
            public void close(DBExamPlan dbExamPlan) {
                mPopExamPlan.dismiss();
                mDataBinding.tvName.setText(dbExamPlan.getExamName());
                semExamCode(dbExamPlan.getExamCode());
            }
        });

        mPopExamPlan.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mDataBinding.ivChooseExam.setImageResource(R.drawable.icon_blue_arrow_down);
                isComparison = false;
            }
        });


        mDataBinding.tvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
//                finish();
            }
        });

        mDataBinding.tvChooseExamination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle _b = new Bundle();
                _b.putString("SE_CODE", mSeCode);
                startActivityForResultByRouter(ARouterPath.CHOOSE_VENVE, 1211, _b);
            }
        });

        mDataBinding.lineTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBinding.ivChooseExam.setImageResource(R.drawable.icon_blue_arrow_up);
                mPopExamPlan.setIndex(mExamCode);
//                mPopExamPlan.showAtLocation(mDataBinding.bgMain, Gravity.BOTTOM, 0, 0);
                mPopExamPlan.setFocusable(false);
                mPopExamPlan.showAtLocation(mDataBinding.bgMain, Gravity.CENTER, 0, 0);
                mPopExamPlan.getContentView().setSystemUiVisibility(FULL_SCREEN_FLAG);
                mPopExamPlan.setFocusable(true);
                mPopExamPlan.update();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1211 && resultCode == 1) {
            mExamCodeList.addAll(data.getStringArrayListExtra("list"));
        }
    }


    @Override
    public void onInitViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public void onInitDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.act_main);
        mDataBinding.setHandlers(this);
    }

    @Override
    public void onSubscribeViewModel() {
        //获取考试极化
        mViewModel.getmDbExamPlan().observe(this, new Observer<DBExamPlan>() {
            @Override
            public void onChanged(DBExamPlan dbExamPlan) {
                if (dbExamPlan != null) {
                    mDataBinding.tvName.setText(dbExamPlan.getExamName());

                    mVerifyStartTime = dbExamPlan.getVerifyStartTime() == null ? "0" : dbExamPlan.getVerifyStartTime();
                    mVerifyEndTime = dbExamPlan.getVerifyEndTime() == null ? "0" : dbExamPlan.getVerifyEndTime();

                    semExamCode(dbExamPlan.getExamCode());
                }
            }
        });

        //获取场次数据
        mViewModel.getmDBexamArrange().observe(this, new Observer<DBExamArrange>() {
            @Override
            public void onChanged(DBExamArrange dbExamArrange) {
                if (dbExamArrange == null) {
                    Toast.makeText(getApplicationContext(), "没有将要进行的考试", Toast.LENGTH_LONG).show();
                    return;
                }
                //获取场次数据
                mSeCode = dbExamArrange.getSeCode();
                mDataBinding.tvSuject.setText(dbExamArrange.getSeName());
                mDataBinding.tvExaminationTime.setText(String.format("%s~%s", dbExamArrange.getStartTime(), dbExamArrange.getEndTime()));
                //v1.3.2 修改验证结束时间每科考试结束时间
                timeEnd = TimeUtils.string2Millis(dbExamArrange.getEndTime());
                //获取上一场考试结束时间
                DBExamArrange lastedArrange = DBOperation.queryLatestExam(dbExamArrange.getStartTime());
                //当前考试开始时间
                long currentExamStartTime = TimeUtils.string2Millis(dbExamArrange.getStartTime());
                if (null != lastedArrange && null != lastedArrange.getEndTime()){
                    //判断当前场次的结束时间和上一场考试开始时间的间隔 是否小于验证开始时间
                    if (currentExamStartTime - TimeUtils.string2Millis(lastedArrange.getEndTime()) >= (Long.parseLong(mVerifyStartTime) * 60 * 1000)){
                        //倒计时开始时间= 考试开始时间
                        timeStart = TimeUtils.string2Millis(dbExamArrange.getStartTime()) - (Long.parseLong(mVerifyStartTime) * 60 * 1000);
                    }else {
                        timeStart = TimeUtils.string2Millis(lastedArrange.getEndTime());
                        Log.e("上一场考试结束时间:",lastedArrange.getEndTime());
                    }
                }else {
                    timeStart = TimeUtils.string2Millis(dbExamArrange.getStartTime()) - (Long.parseLong(mVerifyStartTime) * 60 * 1000);
                }
                mDataBinding.tvVerificationTime.setText(simpleDateFormat.format(new Date(timeStart)) + "~" + simpleDateFormat.format(new Date(timeEnd)));
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, TIME); // 在初始化方法里.
                timer();
            }
        });

        //获取考场编排数据
        mViewModel.getmDBExamLayout().observe(this, new Observer<DBExamLayout>() {
            @Override
            public void onChanged(DBExamLayout dbExamLayout) {
                if (dbExamLayout != null)
                    mDataBinding.tvSite.setText(dbExamLayout.getSiteName());
                else
                    mDataBinding.tvSite.setText("");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.getExamCode(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mSeCode != null) {
            handler.postDelayed(runnable, TIME); // 在初始化方法里.
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mDisposable && !mDisposable.isDisposed()){
            mDisposable.dispose();
        }
    }


    @Override
    public void onBackPressed() {
        new QMUIDialog.MessageDialogBuilder(MainActivity.this)
                .setTitle("提示")
                .setMessage("是否退出应用？")
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_POSITIVE, (dialog, index) -> {
                    dialog.dismiss();
                    finish();
                })
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
    }

}
