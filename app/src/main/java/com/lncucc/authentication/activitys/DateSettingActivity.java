package com.lncucc.authentication.activitys;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.base.ViewManager;
import com.askia.coremodel.datamodel.database.operation.LogsUtil;
import com.askia.coremodel.event.UniAuthInfoEvent;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActDateSettingBinding;
import com.lncucc.authentication.widgets.calendar.CustomDayView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.ttsea.jrxbus2.RxBus2;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class DateSettingActivity extends BaseActivity {

    private ActDateSettingBinding dateSettingBinding;
    CoordinatorLayout content;
    MonthPager monthPager;
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private CalendarDate currentDate;
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    public final CompositeDisposable mDisposable = new CompositeDisposable();
    private boolean isFromUni = false;

    @Override
    public void onInit() {
        ((TextView) findViewById(R.id.tv_title)).setText("时间校正");
        findViewById(R.id.rl_left_back).setVisibility(View.GONE);
        content = findViewById(R.id.content);
        monthPager = findViewById(R.id.calendar_view);
        monthPager.setViewHeight(Utils.dpi2px(this, 200));
        initCurrentDate();
        initCalendarView();
        initTimer();
        requestPermissions();
    }

    @Override
    public void onInitViewModel() {
    }

    private void requestPermissions() {
        final RxPermissions rxPermission = new RxPermissions(DateSettingActivity.this);
        rxPermission.request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,

                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
        ).subscribe(aBoolean -> {
            if (aBoolean) {
                //申请的权限全部允许
//                goToMain();
                getUniParams();
            } else {
                //只要有一个权限被拒绝，就会执行
                new QMUIDialog.MessageDialogBuilder(DateSettingActivity.this)
                        .setTitle("权限异常")
                        .setMessage("没有允许全部权限授权，将无法正常使用。")
                        .addAction("退出应用", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                ViewManager.getInstance().exitApp(DateSettingActivity.this);
                            }
                        })
                        .setCanceledOnTouchOutside(false)
                        .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
            }
        });
    }

    /**
     * 获取从Uni跳转带过来的参数
     */
    public void getUniParams(){
        Intent intent = getIntent();
        if (intent == null) return;
        Bundle bundle = intent.getExtras();
        if (bundle == null) return;
        UniAuthInfoEvent event = new UniAuthInfoEvent();
        for (String key: bundle.keySet()){
            //获取uni传递的用户信息
            Log.e("uni app userInfo->", bundle.get(key).toString());
            String value = bundle.get(key).toString();
            if ("userName".equals(key)){
                event.setUserName(value);
            }else {
                event.setPassWord(value);
            }
        }
        if (event.getUserName() != null && event.getPassWord() != null){
            isFromUni = true;
        }
        //注册粘性事件
        RxBus2.getInstance().postStickyEvent(event);
    }


    private void initCurrentDate() {
        currentDate = new CalendarDate();
        dateSettingBinding.dateTitle.setText(currentDate.year + "年" + currentDate.month + "月");
    }

    private void initTimer() {
        Observable.interval(1000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NotNull Long aLong) {
                        Date date = TimeUtils.millis2Date(System.currentTimeMillis());
                        dateSettingBinding.tvHour.setText(date.getHours() + "");
                        dateSettingBinding.tvMin.setText(date.getMinutes() + "");
                        dateSettingBinding.tvSec.setText(date.getSeconds() + "");
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     */
    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(this, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                this,
                onSelectDateListener,
                CalendarAttr.CalendarType.MONTH,
                CalendarAttr.WeekArrayType.Sunday,
                customDayView);
        initMarkData();
        initMonthPager();
    }

    private void initMarkData() {
        HashMap<String, String> markData = new HashMap<>();
        markData.put("2017-8-9", "1");
        markData.put("2017-7-9", "0");
        markData.put("2017-6-9", "1");
        markData.put("2017-6-10", "0");
        calendarAdapter.setMarkData(markData);
    }

    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    dateSettingBinding.dateTitle.setText(date.year + "年" + date.month + "月");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initListener() {
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                monthPager.selectOtherMonth(offset);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTimer();
        initCalendarView();
        initCurrentDate();
        getUniParams();
    }

    /**
     * 点击进入系统
     * @param view
     */
    public void loginSystem(View view) {
        if (autoLogin() && !isFromUni){
            Bundle _b = new Bundle();
            String code = SharedPreferencesUtils.getString(this,"code");
            _b.putString("code", code);
            _b.putInt("type", 1);
            startActivityByRouter(ARouterPath.INITIALIZE_ACTIVITY, _b);
        }else {
//            startActivityByRouter(ARouterPath.LOGIN_ACTIVITY);
            //清除密码
            SharedPreferencesUtils.putString(this, "password", "");
            //清除orgCode
            SharedPreferencesUtils.putString(this, "code", "");
            Intent intent = new Intent(this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        finish();
    }

    /**
     * 新增自动登录
     */
    private boolean autoLogin(){
        String account = SharedPreferencesUtils.getString(this,"account");
        String password = SharedPreferencesUtils.getString(this, "password");
        String code = SharedPreferencesUtils.getString(this,"code");
        //账号 密码 OrgCode都不为空 触发自动登录
        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(code);
    }

    public void setting(View view) {
        LogsUtil.saveOperationLogs("时间校正");
        startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
    }

    @Override
    public void onInitDataBinding() {
        dateSettingBinding = DataBindingUtil.setContentView(this, R.layout.act_date_setting);
        dateSettingBinding.setHandles(this);
    }

    @Override
    public void onSubscribeViewModel() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }
}
