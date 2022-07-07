package com.lncucc.authentication.fragments;

import android.content.ContentResolver;
import android.graphics.Color;
import android.hardware.Camera;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentDisplaySettingBinding;
import com.qmuiteam.qmui.widget.QMUISlider;

import org.jetbrains.annotations.NotNull;

import static com.askia.coremodel.rtc.Constants.CAMERA_DEFAULT;

/**
 * 数据导入
 */
public class DisplaySettingFragment extends BaseFragment {
    private FragmentDisplaySettingBinding displaySetting;
    LinearLayout linear;  // 切换外部linear盒子
    TextView te1; // 前置摄像头
    TextView te2; // 后置摄像头
    int currentPos; // 当前选择的是哪个摄像头  前置"1"  后置"0"
    QMUISlider sliderBrightness;

    @Override
    public void onInit() {
        linear = displaySetting.CamSet;
        te1 = displaySetting.cameraSetBtn1;
        te2 = displaySetting.cameraSetBtn2;
        sliderBrightness = displaySetting.sliderBrightness;
        sliderBrightness.setCurrentProgress(getScreenBrightness());
        cameraSet();
        sliderBrightness.setCallback(new QMUISlider.Callback() {
            @Override
            public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {

            }

            @Override
            public void onTouchDown(QMUISlider slider, int progress, int tickCount, boolean hitThumb) {

            }

            @Override
            public void onTouchUp(QMUISlider slider, int progress, int tickCount) {
                saveScreenBrightness(progress);
            }

            @Override
            public void onStartMoving(QMUISlider slider, int progress, int tickCount) {

            }

            @Override
            public void onStopMoving(QMUISlider slider, int progress, int tickCount) {

            }
        });
//        currentPos = SharedPreferencesUtils.getInt(getActivity(), CAMERA_DEFAULT, 0);
        currentPos = SPUtils.getInstance().getInt(CAMERA_DEFAULT,0);
        setState();
        linear.setOnClickListener(v -> {
            if (currentPos == 0) {
                currentPos = 1;
            } else {
                currentPos = 0;
            }
//            SharedPreferencesUtils.putInt(getActivity(), CAMERA_DEFAULT, currentPos);
            SPUtils.getInstance().put(CAMERA_DEFAULT,currentPos);
            setState();
        });
    }


    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        displaySetting = DataBindingUtil.inflate(inflater, R.layout.fragment_display_setting, container, false);
        return displaySetting.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }


    public void clickCameraSet(View view) {
        LogUtils.e("点击");
    }

    public void setState() {
        if (currentPos == 1) {
            // 当前是前置摄像头  该进行后置摄像头的设置
            te1.setBackgroundResource(R.drawable.bg_network_btn);
            te1.setTextColor(Color.parseColor("#FFFFFF"));
            te2.setBackgroundColor(Color.TRANSPARENT);
            te2.setTextColor(Color.parseColor("#666666"));
        } else {
            // 当前是后置摄像头  该进行前置摄像头的切换
//            SharedPreferencesUtils.putInt(getActivity(), CAMERA_DEFAULT, currentPos);
            te2.setBackgroundResource(R.drawable.bg_network_btn);
            te2.setTextColor(Color.parseColor("#FFFFFF"));
            te1.setBackgroundColor(Color.TRANSPARENT);
            te1.setTextColor(Color.parseColor("#666666"));
        }
    }

    public void setScrennManualMode() {
        ContentResolver contentResolver = getActivity().getContentResolver();
        try {
            int mode = Settings.System.getInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int getScreenBrightness() {
        // 获取屏幕亮度值
        ContentResolver contentResolver = getActivity().getContentResolver();
        int defVal = 125;
        return 100 * Settings.System.getInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, defVal) / 255;
    }

    private void saveScreenBrightness(int process) {
        // 当屏幕亮度模式为0即手动调节时，可以通过如下代码设置屏幕亮度：
        setScrennManualMode();
        ContentResolver contentResolver = getActivity().getContentResolver();
        int value = process * 255 / 100; // 设置亮度值为255
        Settings.System.putInt(contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, value);
    }

    private void setWindowBrightness(int brightness) {
        // 设置当前窗口亮度
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }

    public void cameraSet() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        LogUtils.e("camera info ==>>>>>", info.facing, Camera.CameraInfo.CAMERA_FACING_FRONT);
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // 前置
            currentPos = 1;
        } else {
            // 后置
            currentPos = 0;
        }
        setState();
    }
}
