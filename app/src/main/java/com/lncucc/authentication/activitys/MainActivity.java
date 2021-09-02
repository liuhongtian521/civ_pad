package com.lncucc.authentication.activitys;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.MyTimeUtils;
import com.askia.common.util.PageCountDownTimer;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.viewmodel.AuthenticationViewModel;
import com.askia.coremodel.viewmodel.MainViewModel;
import com.blankj.utilcode.util.TimeUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActMainBinding;
import com.lncucc.authentication.widgets.PopExamPlan;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.functions.Consumer;


@Route(path = ARouterPath.MAIN_ACTIVITY)
public class MainActivity extends BaseActivity {
    private ActMainBinding mDataBinding;
    //选择考试计划
    private PopExamPlan mPopExamPlan;
    private MainViewModel mViewModel;
    private String mSeCode;//场次码
    private String mExanCode;
    private boolean isComparison = false;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    long timeStart, timeEnd;
    String mVerifyStartTime, mVerifyEndTime;
    private ArrayList<String> mExamCodeList = new ArrayList<>();


    private int TIME = 1000;  //每隔1s执行一次.

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
//        Log.e("TagSnake", "now" + nowTime + ":staet" + timeStart + ":end" + timeEnd);
        if (nowTime > timeStart) {
            Bundle _d = new Bundle();
            _d.putString("exanCode", mExanCode);
            _d.putStringArrayList("list", mExamCodeList);
            _d.putLong("startTIME", timeStart);
            _d.putLong("endTIME", timeEnd);
            startActivityByRouter(ARouterPath.IDENTIFY_ACTIVITY, _d);
            finish();
        } else {
            long betten = timeStart - nowTime;
            long hour = betten / (1000 * 60 * 60);//小时
            long min = (betten - (hour * 1000 * 60 * 60)) / (1000 * 60);
            long sercend = (betten - (hour * 1000 * 60 * 60) - (min * 1000 * 60)) / 1000;
            mDataBinding.tvTime.setText((hour < 10 ? "0" + hour : hour) + ":" + (min < 10 ? "0" + min : min) + ":" + (sercend < 10 ? "0" + sercend : sercend));
        }
    }

    public void semExanCode(String mExanCode) {
        if (mExanCode == null)
            return;
        Log.e("TagSnake",mExanCode);
        this.mExanCode = mExanCode;
        //获取场次数据
        mViewModel.getSiteCode(mExanCode);
        mViewModel.getExamLayout(mExanCode);
    }

    @Override
    public void onInit() {

        mPopExamPlan = new PopExamPlan(this, new PopExamPlan.PopListener() {
            @Override
            public void close(DBExamPlan dbExamPlan) {
                mDataBinding.tvName.setText(dbExamPlan.getExamName());
                semExanCode(dbExamPlan.getExamCode());
            }
        });

        mPopExamPlan.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mDataBinding.ivChooseExam.setImageResource(R.drawable.icon_todown);
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

        mDataBinding.ivChooseExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBinding.ivChooseExam.setImageResource(R.drawable.icon_toup);
                mPopExamPlan.showAtLocation(mDataBinding.bgMain, Gravity.BOTTOM, 0, 0);
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
        mViewModel.getmDbExamPlan().observe(this, new Observer<DBExamPlan>() {
            @Override
            public void onChanged(DBExamPlan dbExamPlan) {
                if (dbExamPlan != null) {
                    mDataBinding.tvName.setText(dbExamPlan.getExamName());

                    mVerifyStartTime = dbExamPlan.getVerifyStartTime() == null ? "0" : dbExamPlan.getVerifyStartTime();
                    mVerifyEndTime = dbExamPlan.getVerifyEndTime() == null ? "0" : dbExamPlan.getVerifyEndTime();

                    semExanCode(dbExamPlan.getExamCode());
                }
            }
        });

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
//                String start = simpleDateFormat.format(new Date(Long.valueOf(dbExamArrange.getStartTime())));
//                String end = simpleDateFormat.format(new Date(Long.valueOf(dbExamArrange.getEndTime())));

//                Log.e("TagSnake", TimeUtils.string2Millis(dbExamArrange.getStartTime()) + "::" + dbExamArrange.getStartTime()+"::"+mVerifyStartTime);

                mDataBinding.tvExaminationTime.setText(dbExamArrange.getStartTime() + "~" + dbExamArrange.getEndTime());
                timeStart = Long.valueOf(TimeUtils.string2Millis(dbExamArrange.getStartTime())) - (Long.valueOf(mVerifyStartTime) * 60 * 1000);
                timeEnd = Long.valueOf(TimeUtils.string2Millis(dbExamArrange.getStartTime())) + (Long.valueOf(mVerifyEndTime) * 60 * 1000);
                mDataBinding.tvVerificationTime.setText(simpleDateFormat.format(new Date(timeStart)) + "~" + simpleDateFormat.format(new Date(timeEnd)));
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, TIME); // 在初始化方法里.
                timer();
            }
        });

        mViewModel.getmDBExamLayout().observe(this, new Observer<DBExamLayout>() {
            @Override
            public void onChanged(DBExamLayout dbExamLayout) {
                mDataBinding.tvSite.setText(dbExamLayout.getSiteName());
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
        mViewModel.getExamCode();
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
    }

    public void requestPermissions() {
        final RxPermissions rxPermission = new RxPermissions(MainActivity.this);
        rxPermission.requestEach(
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                // 如果用户接受了读写权限
                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission.name) && permission.granted) {
                /*    // 如果没有初始化sdk
                    if(!ECApplication.getInstance().isHWSDKInit)
                    {
                        ECApplication.getInstance().initHWSDK();
                    }*/
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        new QMUIDialog.MessageDialogBuilder(MainActivity.this)
                .setTitle("提示")
                .setMessage("是否退出应用？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        //         showNetDialog();
                        finish();
                    }
                })
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
    }


    public interface OnTabChangeListener {
        public void OnTabChanged(int index);
    }

}
