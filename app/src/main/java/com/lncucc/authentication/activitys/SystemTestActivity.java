package com.lncucc.authentication.activitys;

import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.APP;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.system.SystemTestBean;
import com.askia.coremodel.util.AssetsUtil;
import com.askia.coremodel.util.JsonUtil;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.SystemTestAdapter;
import com.lncucc.authentication.adapters.itemclick.ItemClickListener;
import com.lncucc.authentication.databinding.ActSystemTestBinding;
import com.unicom.facedetect.detect.FaceDetectInitListener;
import com.unicom.facedetect.detect.FaceDetectManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.realm.internal.android.JsonUtils;

import static com.askia.coremodel.rtc.Constants.VOICE_SETTING;

/**
 * 系统测试页
 */
@Route(path = ARouterPath.SYSTEM_TEST)
public class SystemTestActivity extends BaseActivity implements ItemClickListener {

    private ActSystemTestBinding testBinding;
    private final static int REQUEST_CODE_CAMERA = 0;
    private final static int REQUEST_CODE_SCREEN = 1;
    private final static int REQUEST_CODE_VOICE = 2;
    private final static int REQUEST_CODE_FACE = 3;
    private SystemTestBean bean;
    private String data;
    private SystemTestAdapter mAdapter;
    private String TAG;
    private Disposable mDisposable;
    private MediaPlayer mediaPlayer;


    @Override
    public void onInit() {
        findViewById(R.id.rl_left_back).setOnClickListener(v -> finish());
        ((TextView) findViewById(R.id.tv_title)).setText("系统测试");
        String local = SharedPreferencesUtils.getString(this, "local_state", "");
        if ("".equals(local)) {
            data = AssetsUtil.getJson(this, "system.json");
        } else {
            data = local;
        }
        bean = JsonUtil.Str2JsonBean(data, SystemTestBean.class);
        mAdapter = new SystemTestAdapter(bean.getData(), this::onItemClick);
        testBinding.recyclerSystemTest.setLayoutManager(new GridLayoutManager(this, 2));
        testBinding.recyclerSystemTest.setAdapter(mAdapter);
    }

    @Override
    public void onInitViewModel() {

    }

    private void testCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
        //5秒关闭相机
        mDisposable = Flowable.intervalRange(0, 5, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(along -> {
                })
                .doOnComplete(() -> {
                    finishActivity(REQUEST_CODE_CAMERA);
                    bean.getData().get(REQUEST_CODE_CAMERA).setState(1);
                    saveData2Local(bean);
                    mAdapter.notifyDataSetChanged();
                    if ("test_all".equals(TAG)) {
                        startActivityForResultByRouterNoParams(ARouterPath.SCREEN_TEST, REQUEST_CODE_SCREEN);
                    }
                })
                .subscribe();
    }

    @Override
    public void onInitDataBinding() {
        testBinding = DataBindingUtil.setContentView(this, R.layout.act_system_test);
        testBinding.setHandles(this);
    }

    @Override
    public void onSubscribeViewModel() {

    }

    @Override
    public void onItemClick(int position) {
        switch (position) {
            //camera
            case 0:
                testCamera();
                break;
            //screen
            case 1:
                startActivityForResultByRouterNoParams(ARouterPath.SCREEN_TEST, REQUEST_CODE_SCREEN);
                break;
            //voice
            case 2:
                voiceTest();
                break;
            //face
            case 3:
                testFace();
                break;
        }
    }

    //全部测试
    public void testAll(View view) {
        TAG = "test_all";
        //相机测试开始
        testCamera();
    }

    //人脸测试
    private void testFace() {
        if (!NetworkUtils.isConnected()){
            MyToastUtils.error("人脸初始化异常，请退出应用连接互联网后重启再试！",1);
            return;
        }
        FaceDetectManager.getInstance().init(this, "229b20394c0149dfb39995b87288dde8", new FaceDetectInitListener() {
            @Override
            public void onInitComplete() {
                bean.getData().get(REQUEST_CODE_FACE).setState(1);
                saveData2Local(bean);
                mAdapter.notifyDataSetChanged();
                MyToastUtils.error("人脸识别检查成功",1);
            }

            @Override
            public void onInitFailure(String s) {
                MyToastUtils.error("人脸初始化异常，请退出应用连接互联网后重启再试！",1);
            }
        });
    }

    private void saveData2Local(SystemTestBean bean) {
        String data = JsonUtil.JsonBean2Str(bean);
        SharedPreferencesUtils.putString(SystemTestActivity.this, "local_state", data);
    }

    //语音测试
    private void voiceTest() {
        boolean isOpen = SharedPreferencesUtils.getBoolean(this, VOICE_SETTING);
        if (isOpen) {
            mediaPlayer = MediaPlayer.create(this, R.raw.tongguo);
            mediaPlayer.start();
            bean.getData().get(2).setState(1);
            saveData2Local(bean);
            mAdapter.notifyDataSetChanged();
            if ("test_all".equals(TAG)) {
                testFace();
            }
        } else {
            MyToastUtils.error("请在系统设置模块，打开语音提示！", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mDisposable && !mDisposable.isDisposed()){
            mDisposable.dispose();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (data != null) {
                    bean.getData().get(REQUEST_CODE_CAMERA).setState(1);
                    saveData2Local(bean);
                    if ("test_all".equals(TAG)) {
                        startActivityForResultByRouterNoParams(ARouterPath.SCREEN_TEST, REQUEST_CODE_SCREEN);
                    }
                }
                break;
            case REQUEST_CODE_SCREEN:
                LogUtils.e("screen test ->", data);
                if (resultCode == RESULT_OK) {
                    bean.getData().get(REQUEST_CODE_SCREEN).setState(1);
                    saveData2Local(bean);
                    if ("test_all".equals(TAG)) {
//                        testFace();
                        voiceTest();
                    }
                }
                break;
            case REQUEST_CODE_VOICE:
                LogUtils.e("voice test ->", data);
                break;
        }
        mAdapter.notifyDataSetChanged();

    }
}
