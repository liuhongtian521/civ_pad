package com.askia.common.base;

import static com.askia.coremodel.rtc.Constants.AUTO_BASE_URL;
import static com.askia.coremodel.rtc.Constants.CAMERA_DEFAULT;
import static com.askia.coremodel.rtc.Constants.VOICE_SETTING;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.util.BuglyUtils;
import com.askia.common.util.MyToastUtils;
import com.askia.common.util.ScreenUtil;
import com.askia.common.util.baidutts.MySyntherizer;
import com.askia.common.util.baidutts.OfflineResource;
import com.askia.coremodel.datamodel.database.repository.SharedPreUtil;
import com.askia.coremodel.datamodel.http.ApiConstants;
import com.askia.coremodel.datamodel.realm.MyMigration;
import com.askia.coremodel.datamodel.realm.RealmConstant;
import com.askia.coremodel.event.AuthenticationEvent;
import com.askia.coremodel.event.LoginSuccessEvent;
import com.askia.coremodel.rtc.AgoraEventHandler;
import com.askia.coremodel.rtc.Constants;
import com.askia.coremodel.rtc.EngineConfig;
import com.askia.coremodel.rtc.EventHandler;
import com.askia.coremodel.rtc.FileUtil;
import com.askia.coremodel.rtc.PrefManager;
import com.askia.coremodel.rtc.StatsManager;
import com.askia.coremodel.rtm.IMUtils;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.Utils;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.ui.UILifecycleListener;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;
import com.unicom.facedetect.detect.FaceDetectInitListener;
import com.unicom.facedetect.detect.FaceDetectManager;
import com.unicom.facedetect.log.DiagnosisLog;
import com.unicom.facedetect.log.DiagnosisType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.agora.rtc.RtcEngine;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.realm.Realm;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;


/**
 * 要想使用BaseApplication，必须在组件中实现自己的Application，并且继承BaseApplication；
 * 组件中实现的Application必须在debug包中的AndroidManifest.xml中注册，否则无法使用；
 * 组件的Application需置于java/debug文件夹中，不得放于主代码；
 * 组件中获取Context的方法必须为:Utils.getContext()，不允许其他写法；
 *
 * @name APP
 */
public class APP extends Application {
    private boolean isBackGround = true;
    public String mAgoraAppId = "";
    private RtcEngine mRtcEngine;
    private EngineConfig mGlobalConfig = new EngineConfig();
    private AgoraEventHandler mHandler = new AgoraEventHandler();
    private StatsManager mStatsManager = new StatsManager();
    public static boolean isUpdate = false;
    /*百度语音start*/
    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    protected TtsMode ttsMode = TtsMode.MIX;
    protected String offlineVoice = OfflineResource.VOICE_FEMALE;
    // 主控制类，所有合成控制方法从这个类开始
    protected MySyntherizer synthesizer;
    private Boolean isTTSInit = false;
    public static boolean isInitFaceSuccess = false;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        com.askia.common.util.Utils.init(this);
        registerActivityLifeCallback();

        SharedPreUtil.initSharedPreference(this);
        ARouter.init(this);
        // logUtil init
        Utils.init(this);
        // 数据库初始化
        Realm.init(this);
        //初始化摄像头修改为后置
//        SharedPreferencesUtils.putInt(this,CAMERA_DEFAULT, 0);
        //初始化语音提示
        boolean isOpen = SharedPreferencesUtils.getBoolean(this,VOICE_SETTING,true);
        String url = SharedPreferencesUtils.getString(this,AUTO_BASE_URL, ApiConstants.HOST);
        //设置上次保存的IP
        RetrofitUrlManager.getInstance().setGlobalDomain(url);
        SharedPreferencesUtils.putBoolean(this,VOICE_SETTING,isOpen);
        try {
            Realm.migrateRealm(RealmConstant.getRealmConfig(),new MyMigration());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 文件下载
        FileDownloader.setup(this);
        FileDownloadLog.NEED_LOG = true;
        com.blankj.utilcode.util.LogUtils.getConfig().setLogHeadSwitch(false).setBorderSwitch(false).setLogSwitch(
                true).setLog2FileSwitch(false);
        Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {
                isUpdate = true;
                Log.d("qinyy","Upgrade dialog create");
            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override

            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {

            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {
                isUpdate = false;
                Log.d("qinyy","Upgrade dialog onDestroy");
            }
        };

        BuglyUtils.init(this, BuglyUtils.APP_ID);

        FaceDetectManager.getInstance().init(getApplicationContext(), "229b20394c0149dfb39995b87288dde8", new FaceDetectInitListener() {
            @Override
            public void onInitComplete() {
                isInitFaceSuccess = true;
                Log.e("init face", "success");
                DiagnosisLog.setDebugModel(true);
                DiagnosisLog.d(DiagnosisType.DETECT,"onInitComplete");
            }

            @Override
            public void onInitFailure(String errorMessage) {
                isInitFaceSuccess = false;
                Log.e("init face error", errorMessage);
            }
        });


        RxBus2.getInstance().register(this);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        RtcEngine.destroy();
        RxBus2.getInstance().unRegister(this);
        getTTSSyntherizer().release();
        IMUtils.getInstance(getApplicationContext()).logout(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {

            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public MySyntherizer getTTSSyntherizer() {
        return synthesizer;
    }

    private void registerActivityLifeCallback() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (isBackGround) {
                    ScreenUtil.hideBottomUIMenu(getApplicationContext());
                    isBackGround = false;
                    LogUtils.i("APP回到了前台");
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        if (level == TRIM_MEMORY_UI_HIDDEN) {
            isBackGround = true;
            ScreenUtil.showBottomUIMenu(getApplicationContext());
            LogUtils.d("进入后台");
        }
    }

    private void initConfig() {
        SharedPreferences pref = PrefManager.getPreferences(getApplicationContext());
        mGlobalConfig.setVideoDimenIndex(pref.getInt(
                Constants.PREF_RESOLUTION_IDX, Constants.DEFAULT_PROFILE_IDX));

        // true 打开调试Log false 关闭
        boolean showStats = pref.getBoolean(Constants.PREF_ENABLE_STATS, true);
        mGlobalConfig.setIfShowVideoStats(showStats);
        mStatsManager.enableStats(showStats);
    }

}
