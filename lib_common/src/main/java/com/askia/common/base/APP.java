package com.askia.common.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.apkfuns.logutils.LogUtils;
import com.askia.common.R;
import com.askia.common.util.BuglyUtils;
import com.askia.common.util.MyToastUtils;
import com.askia.common.util.ScreenUtil;
import com.askia.common.util.baidutts.AutoCheck;
import com.askia.common.util.baidutts.InitConfig;
import com.askia.common.util.baidutts.MySyntherizer;
import com.askia.common.util.baidutts.NonBlockSyntherizer;
import com.askia.common.util.baidutts.OfflineResource;
import com.askia.common.util.baidutts.UiMessageListener;
import com.askia.common.util.receiver.UsbStateChangeReceiver;
import com.askia.coremodel.datamodel.database.repository.SharedPreUtil;
import com.askia.coremodel.datamodel.realm.MyMigration;
import com.askia.coremodel.datamodel.realm.RealmConstant;
import com.askia.coremodel.event.AuthenticationEvent;
import com.askia.coremodel.event.CommonImEvent;
import com.askia.coremodel.event.LoginSuccessEvent;
import com.askia.coremodel.rtc.AgoraEventHandler;
import com.askia.coremodel.rtc.Constants;
import com.askia.coremodel.rtc.EngineConfig;
import com.askia.coremodel.rtc.EventHandler;
import com.askia.coremodel.rtc.FileUtil;
import com.askia.coremodel.rtc.PrefManager;
import com.askia.coremodel.rtc.StatsManager;
import com.askia.coremodel.rtm.ChatManager;
import com.askia.coremodel.rtm.IMUtils;
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import com.bumptech.glide.Glide;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.beta.ui.UILifecycleListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;
import com.unicom.facedetect.detect.FaceDetectInitListener;
import com.unicom.facedetect.detect.FaceDetectManager;
import com.unicom.facedetect.log.DiagnosisLog;
import com.unicom.facedetect.log.DiagnosisType;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;

import org.greenrobot.eventbus.ThreadMode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.agora.rtc.RtcEngine;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.askia.common.util.baidutts.MainHandlerConstant.INIT_SUCCESS;
import static com.askia.coremodel.rtc.Constants.ACTION_USB_PERMISSION;
import static com.askia.coremodel.rtc.Constants.CAMERA_DEFAULT;


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
    public boolean isInitFaceSuccess = false;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /*    public APP() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.askia.common.base.APP",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }*/

//    private Handler mMainHandler = new Handler() {
//        /*
//         * @param msg
//         */
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Log.d("qyytts", msg.obj + "");
//            if (msg.what == INIT_SUCCESS) {
//                // 引擎初始化成功马上播放
//
//            }
//            if (!TextUtils.isEmpty((CharSequence) msg.obj) && String.valueOf(msg.obj).contains("播放结束回调")) {
//
//            } else if (!TextUtils.isEmpty((CharSequence) msg.obj) && (String.valueOf(msg.obj).contains("错误") || String.valueOf(msg.obj).contains("失败"))) {
//
//            }
//        }
//    };/*  LogUtils.getLog2FileConfig().configLog2FileEnable(true);
//        LogUtils.getLog2FileConfig().configLog2FilePath("/mnt/sdcard/ddswlog/");*/

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
        //初始化摄像头前置
        SharedPreferencesUtils.putInt(this,CAMERA_DEFAULT, 1);
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

        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).asBitmap().load(path).into(imageView);
            }
        });

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
        // 声网IM
    //    IMUtils.getInstance().init(getApplicationContext(), mAgoraApøpId);


        RxBus2.getInstance().register(this);
//        initFace();
//      /*  Bugly.init(getApplicationContext(), "注册时申请的APPID", false);
        // bugly report
        /*CrashReport.setIsDevelopmentDevice(getApplicationContext(), false);
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
            public Map<String, String> onCrashHandleStart(int crashType, String errorType,
                                                          String errorMessage, String errorStack)
            {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("crashType", "" + crashType);
                map.put("errorType", "" + errorType);
                map.put("errorMessage", "" + errorMessage);
                map.put("errorStack", "" + errorStack);
                return map;
            }

            @Override
            public byte[] onCrashHandleStart2GetExtraDatas(int crashType, String errorType,
                                                           String errorMessage, String errorStack)
            {
                try {
                    return "Extra data.".getBytes("UTF-8");
                } catch (Exception e) {
                    return null;
                }
            }

        });
        CrashReport.initCrashReport(getApplicationContext(),strategy);*/
    }

//    public void initFace(){
//        FaceDetectManager.getInstance().init(getApplicationContext(), "229b20394c0149dfb39995b87288dde8", new FaceDetectInitListener() {
//            @Override
//            public void onInitComplete() {
//                Log.e("init face","success");
//            }
//
//            @Override
//            public void onInitFailure(String errorMessage) {
//                Log.e("init face","failure");
//            }
//        });
//    }
    public boolean initAgora(String agoraAppId) {
        mAgoraAppId = agoraAppId;
        // 声网
        try {
            mRtcEngine = RtcEngine.create(getApplicationContext(), mAgoraAppId, mHandler);
            mRtcEngine.setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            mRtcEngine.enableVideo();
            mRtcEngine.setLogFile(FileUtil.initializeLogFile(this));
            initConfig();

            // 声网IM
            IMUtils.getInstance(getApplicationContext()).init( mAgoraAppId);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            new QMUIDialog.MessageDialogBuilder(APP.this)
                    .setTitle("错误")
                    .setMessage("音视频模块初始化错误，请联系运维人员或稍后重启应用重试！错误原因:" + e.getMessage())
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                        }
                    })
                    .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();
            MyToastUtils.info("音视频模块初始化失败");
            return false;
        }
    }


    @Subscribe
    public void onAuthenticationEvent(AuthenticationEvent event) {
      /*  MyToastUtils.info("检测到您的账号已在其他终端登录,请重新登录");
        ViewManager.getInstance().finishAllActivity();
        ARouter.getInstance().build(ARouterPath.LOGIN_ACTIVITY).navigation();*/
        //Context context = ViewManager.getOperatorActivity();
   /*     new QMUIDialog.MessageDialogBuilder(ViewManager.getInstance().currentActivity())
                .setTitle("登录异常")
                .setMessage("检测到您的账号已在其他终端登录，点击确定重新登录。")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addAction(0, "确定", QMUIDialogAction.ACTION_PROP_POSITIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        ViewManager.getInstance().finishAllActivity();
                        ARouter.getInstance().build(ARouterPath.LOGIN_ACTIVITY).navigation();
                    }
                })
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show();*/
    }

    @Subscribe
    public void onLoginSuccessEvent(LoginSuccessEvent event) {
     /*   // 未推送设置别名
        if(SharedPreUtil.getInstance().getUser() != null && !TextUtils.isEmpty(SharedPreUtil.getInstance().getUser().getIdNumber()))
            JPushInterface.setAlias(getApplicationContext(),(int)(Math.random() * 1000), EncryptUtils.encryptMD5ToString(SharedPreUtil.getInstance().getUser().getIdNumber()));
        else
            JPushInterface.deleteAlias(getApplicationContext(),(int)(Math.random() * 1000));*/
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

    public EngineConfig engineConfig() {
        return mGlobalConfig;
    }

    public RtcEngine rtcEngine() {
        if(mRtcEngine == null)
        {
            initAgora(SharedPreUtil.getInstance().getAgoraid());
        }
        return mRtcEngine;
    }

    public StatsManager statsManager() {
        return mStatsManager;
    }

    public void registerEventHandler(EventHandler handler) {
        mHandler.addHandler(handler);
    }

    public void removeEventHandler(EventHandler handler) {
        mHandler.removeHandler(handler);
    }


    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handler发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    public void initialTts() {
//        if(synthesizer != null)
//            return;
//        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
//        SpeechSynthesizerListener listener = new UiMessageListener(mMainHandler);

        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
//        InitConfig initConfig = new InitConfig("17726385", "Gq19GeFvvXAGoioqmCtUiqTK", "dlmK2pXdyaTjdvLj6auqwBoT56gSG4qt", ttsMode, getParams(), listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
//        AutoCheck.getInstance(getApplicationContext()).check(initConfig, new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == 100) {
//                    AutoCheck autoCheck = (AutoCheck) msg.obj;
//                    synchronized (autoCheck) {
//                        String message = autoCheck.obtainDebugMessage();
//                        // toPrint(message); // 可以用下面一行替代，在logcat中查看代码
//                        // Log.w("AutoCheckMessage", message);
//                    }
//                }
//            }
//
//        });
//        synthesizer = new NonBlockSyntherizer(this, initConfig, mMainHandler); // 此处可以改为MySyntherizer 了解调用过程

    }


    /**
     * 合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        try {
            OfflineResource offlineResource = createOfflineResource(OfflineResource.VOICE_FEMALE);
            // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
            params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
            params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE,
                    offlineResource.getModelFilename());
        } catch (Exception e) {
            MyToastUtils.info("读取语音资源失败，可能会影响语音播报功能，请稍后重试");
        }

        return params;
    }


    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            /*   toPrint("【error】:copy files from assets failed." + e.getMessage());*/
        }
        return offlineResource;
    }

}
