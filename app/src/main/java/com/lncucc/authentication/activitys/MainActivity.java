package com.lncucc.authentication.activitys;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.APP;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.MyTimeUtils;
import com.askia.coremodel.datamodel.database.repository.SharedPreUtil;
import com.askia.coremodel.datamodel.face.FaceServer;
import com.askia.coremodel.event.FaceHandleEvent;
import com.askia.coremodel.rtm.IMUtils;
import com.askia.coremodel.viewmodel.MainViewModel;
import com.askia.coremodel.viewmodel.SettingViewModel;
import com.blankj.utilcode.util.ToastUtils;
import com.lncucc.authentication.BuildConfig;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActMainBinding;
import com.lncucc.authentication.widgets.CommonPrgressDialog;
import com.lncucc.authentication.widgets.ErrorDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.beta.Beta;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.reactivex.functions.Consumer;


@Route(path = ARouterPath.MAIN_ACTIVITY)
public class MainActivity extends BaseActivity
{
    private ActMainBinding mDataBinding;
    private int mCurrentPlayPos = 0;
    private CommonPrgressDialog mCommonPrgressDialog;
    private SettingViewModel mSettingViewModel;
    private MainViewModel mMainViewModel;
    @Override
    public void onInit() {

        RxBus2.getInstance().register(this);
        startVideo("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        showNetDialog();
        // 检测人脸识别引擎激活状态
        mMainViewModel.checkActiveStatus(this);

        mDataBinding.tvDate.setText(MyTimeUtils.getCurrentFomatTime());
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(broadcastReceiver, filter);
        //广播的注册，其中Intent.ACTION_TIME_CHANGED代表时间设置变化的时候会发出该广播



        mDataBinding.tvSchoolName.setText("沈阳二中");
        mDataBinding.ivLogo.setImageResource(R.drawable.erzhong_logo);

    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.ACTION_TIME_TICK.equals(intent.getAction())){
                mDataBinding.tvDate.setText(MyTimeUtils.getCurrentFomatTime());
            }else if(intent.ACTION_TIME_CHANGED.equals(intent.getAction())){

            }
        }
    };

    @Override
    public void onInitViewModel() {
        mSettingViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public void onInitDataBinding() {
        mDataBinding = DataBindingUtil.setContentView(this,R.layout.act_main);
        mDataBinding.setHandlers(this);
    }

    @Override
    public void onSubscribeViewModel()
    {
        // 监听人脸导入
        mSettingViewModel.getFaceRegisterLiveData().observe(this, result -> {
            if (result.getErrorCode() == 0) {
                // 导入完成
                ToastUtils.showLong("同步完成，当前人脸库中有" + FaceServer.getInstance().getFaceNumber(this) + "条数据");
            }
            mCommonPrgressDialog.dismiss();
        });
        // 检测该设备是否已经激活过
        mMainViewModel.getGetActiveFileLiveData().observe(this,result ->{
            if(result.getAfCode() == com.arcsoft.face.ErrorInfo.MOK){
                // 如果已经激活，直接初始化
                mMainViewModel.initEngine(this);
            }else {
                // 其他情况尝试激活
                mMainViewModel.activeEngine(this,BuildConfig.ArcFaceAppId,BuildConfig.ArcFaceSdkKey);
            }
        });
        // 激活成功
        mMainViewModel.getActiveEngineLiveData().observe(this,result->{
            if(result.getAfCode() == com.arcsoft.face.ErrorInfo.MOK)
            {
                // 激活成功 则初始化人脸
                mMainViewModel.initEngine(this);
            }
            else {
                dismissNetDialog();
                // 激活失败弹框
                ErrorDialog dialog = new ErrorDialog(MainActivity.this, "人脸引擎激活失败:" + result.getAfCode());
                dialog.show();
            }
        });
        // 初始化成功
        mMainViewModel.getInitEngineLiveData().observe(this,result->{
            dismissNetDialog();
            if(result.getAfCode() == com.arcsoft.face.ErrorInfo.MOK)
            {
                // 初始化成功
                // 如果本地保存人脸库 则增量更新
                if (!TextUtils.isEmpty(SharedPreUtil.getInstance().getZipTimestamp())) {
                    mCommonPrgressDialog = new CommonPrgressDialog(this);
                    mCommonPrgressDialog.setTitle("人脸库同步");
                    mCommonPrgressDialog.show();
                    mSettingViewModel.queryFacesUrls(SharedPreUtil.getInstance().getZipTimestamp(), this, false);
                }
            }
            else
            {
                //  初始化失败
                ErrorDialog dialog = new ErrorDialog(MainActivity.this, "人脸引擎初始化失败:" + result.getAfCode());
                dialog.show();
            }
        });
    }

    private void startVideo(String url){
        mDataBinding.video.setVideoPath(url);
        mDataBinding.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 通过MediaPlayer设置循环播放
                mp.setLooping(true);
                mp.setVolume(0, 0);
                // OnPreparedListener中的onPrepared方法是在播放源准备完成后回调的，所以可以在这里开启播放
                mp.start();
            }
        });
        mDataBinding.video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.stop();
                String rawPath = "android.resource://" + getPackageName() + "/" + R.raw.wait;
                mDataBinding.video.setVideoPath(rawPath);
                mDataBinding.video.start();
                return true;
            }
        });
        mDataBinding.video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.setDisplay(null);
                mp.reset();
                mp.setDisplay(mDataBinding.video.getHolder());

            }
        });
        //  mDataBinding.video.setMediaController(mMediaController);
        mDataBinding.video.start();
    }

    @Subscribe
    public void onFaceHandleEvent(FaceHandleEvent event) {
        switch (event.getType()) {
            case 0:
                mCommonPrgressDialog.setDes("清除人脸库");
                break;
            case 1:
                mCommonPrgressDialog.setDes("人脸库导入中(" + event.getCurrent() + "/" + event.getTotal() + ")");
                int per = (event.getCurrent() * 100) / event.getTotal();
                mCommonPrgressDialog.setProgress(per);
                break;
            case 2:
                mCommonPrgressDialog.setDes("人脸库下载中(" + event.getCurrent() + "KB/" + event.getTotal() + "KB)");
                int per1 = (event.getCurrent() * 100) / event.getTotal();
                mCommonPrgressDialog.setProgress(per1);
                break;
            case 3:
                mCommonPrgressDialog.setDes("人脸库解压中");
                mCommonPrgressDialog.setProgress(0);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(mDataBinding.video != null && mCurrentPlayPos > 0)
        {
            mDataBinding.video.start();
            mDataBinding.video.seekTo(mCurrentPlayPos);
        }
        if(!APP.isUpdate)
            Beta.checkUpgrade(false,false);
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
    protected void onPause() {
        super.onPause();
        if(mDataBinding.video != null)
        {
            mCurrentPlayPos = mDataBinding.video.getCurrentPosition();
            mDataBinding.video.stopPlayback();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
        unregisterReceiver(broadcastReceiver);
        if(mDataBinding.video != null)
        {
            mDataBinding.video.stopPlayback();
        }
        //((APP)getApplication()).getTTSSyntherizer().release();
    }

    public void requestPermissions() {
        final RxPermissions rxPermission = new RxPermissions(MainActivity.this);
        rxPermission.requestEach(
                Manifest.permission.READ_PHONE_STATE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                // 如果用户接受了读写权限
                if(Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission.name) && permission.granted)
                {
                /*    // 如果没有初始化sdk
                    if(!ECApplication.getInstance().isHWSDKInit)
                    {
                        ECApplication.getInstance().initHWSDK();
                    }*/
                }
            }
        });
    }

    public void goIdentity(View view)
    {
        startActivityByRouter(ARouterPath.IDENTIFY_ACTIVITY);
    }

    @Override
    public void onBackPressed()
    {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public interface OnTabChangeListener {
        public void OnTabChanged(int index);
    }

}
