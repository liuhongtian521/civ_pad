package com.lncucc.authentication.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kyleduo.switchbutton.SwitchButton;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentBaseSettingBinding;
import com.qmuiteam.qmui.widget.QMUISlider;

import org.jetbrains.annotations.NotNull;

import static com.askia.coremodel.rtc.Constants.DEFAULT_SOUND_SETTING;
import static com.askia.coremodel.rtc.Constants.KEY_SOUND;
import static com.askia.coremodel.rtc.Constants.SOUND_SETTING;
import static com.askia.coremodel.rtc.Constants.VOICE_SETTING;

/**
 * 数据导入
 */
public class BaseSettingFragment extends BaseFragment {
    private FragmentBaseSettingBinding baseSetting;
    private AudioManager mgr;
    QMUISlider mSlider;
    SwitchButton sbSound;
    SwitchButton sbSoundT;

    @Override
    public void onInit() {
        mgr = (AudioManager) mActivity.getSystemService(mActivity.AUDIO_SERVICE);
        mSlider = baseSetting.slider;
        sbSound = baseSetting.sbSound;
        sbSoundT = baseSetting.sbTable;
        sbSound.setChecked(true);
        requestPermission();
        //滑动音量初始化
        int soundProgress = SharedPreferencesUtils.getInt(getActivity(), DEFAULT_SOUND_SETTING,50);
        try {
            mSlider.setCurrentProgress(soundProgress);
            mgr.setStreamVolume(AudioManager.STREAM_SYSTEM, soundProgress * mgr.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) / 100, AudioManager.FLAG_PLAY_SOUND);
            mgr.setStreamVolume(AudioManager.STREAM_MUSIC, soundProgress * mgr.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) / 100, AudioManager.FLAG_PLAY_SOUND);
            //静音设置初始化
            int silentSound = SharedPreferencesUtils.getInt(getActivity(),SOUND_SETTING,0);
            mgr.setStreamVolume(AudioManager.STREAM_SYSTEM, silentSound * mgr.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) / 100, AudioManager.FLAG_PLAY_SOUND);
            sbSound.setChecked(silentSound == 0);
            //语音提示初始化
            boolean voice = SharedPreferencesUtils.getBoolean(getActivity(),VOICE_SETTING,true);
            LogUtils.e("voice test ->", voice);
            baseSetting.soundTip.setChecked(voice);
            if (voice){
                mgr.setStreamVolume(AudioManager.STREAM_MUSIC, soundProgress * mgr.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) / 100, AudioManager.FLAG_PLAY_SOUND);
            }else {
                mgr.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_PLAY_SOUND);
            }
        }catch (Exception e){
            Log.e("System Service error->", e.getMessage());
        }


        // 静音设置
        sbSound.setOnCheckedChangeListener((compoundButton, b) -> {
            try {
                if (b) {
                    mgr.setStreamVolume(AudioManager.STREAM_SYSTEM, 0 , AudioManager.FLAG_PLAY_SOUND);
//                mgr.setStreamVolume(AudioManager.STREAM_MUSIC, 0,AudioManager.FLAG_PLAY_SOUND);
                    SharedPreferencesUtils.putInt(getActivity(),SOUND_SETTING,0);
                    Settings.System.putInt(mActivity.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0);
                    SharedPreferencesUtils.putInt(getActivity(), KEY_SOUND, 0);
                    MyToastUtils.success("设置成功", Toast.LENGTH_SHORT);
                } else {
//                mgr.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 1);//取消静音
                    mgr.setStreamVolume(AudioManager.STREAM_SYSTEM, 100 , AudioManager.FLAG_PLAY_SOUND);
//                mgr.setStreamVolume(AudioManager.STREAM_MUSIC, soundProgress,AudioManager.FLAG_PLAY_SOUND);
                    Settings.System.putInt(mActivity.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 1);
                    SharedPreferencesUtils.putInt(getActivity(), KEY_SOUND, 1);
                    SharedPreferencesUtils.putInt(getActivity(),SOUND_SETTING,100);
                    MyToastUtils.success("设置成功", Toast.LENGTH_SHORT);
                }
            }catch (Exception e){
                Log.e("System Service error->", e.getMessage());
            }
        });

//        mSlider.setCurrentProgress(100);
        mSlider.setCallback(new QMUISlider.Callback() {

            @Override
            public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {
                LogUtils.e("change", progress);

            }

            @Override
            public void onTouchDown(QMUISlider slider, int progress, int tickCount, boolean hitThumb) {
                LogUtils.e("11", progress);
            }

            @Override
            public void onTouchUp(QMUISlider slider, int progress, int tickCount) {
                try {
                    // slider 滑动后设置声音
                    mgr.setStreamVolume(AudioManager.STREAM_SYSTEM, progress * mgr.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) / 100, AudioManager.FLAG_PLAY_SOUND);
                    mgr.setStreamVolume(AudioManager.STREAM_MUSIC, progress * mgr.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) / 100, AudioManager.FLAG_PLAY_SOUND);
                    SharedPreferencesUtils.putInt(getActivity(), DEFAULT_SOUND_SETTING, progress);
                }catch (Exception e){
                    Log.e("System Service error->", e.getMessage());
                }


            }

            @Override
            public void onStartMoving(QMUISlider slider, int progress, int tickCount) {

            }

            @Override
            public void onStopMoving(QMUISlider slider, int progress, int tickCount) {

            }
        });
        //语音提示
        baseSetting.soundTip.setOnCheckedChangeListener((buttonView, isChecked) -> {
            try {
                if (isChecked){
                    mgr.setStreamVolume(AudioManager.STREAM_MUSIC, soundProgress * mgr.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) / 100, AudioManager.FLAG_PLAY_SOUND);
                }else {
                    mgr.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_PLAY_SOUND);
                }
            }catch (Exception e){
                Log.e("System Service error->", e.getMessage());
            }

            SharedPreferencesUtils.putBoolean(getActivity(),VOICE_SETTING,isChecked);
        });
    }

    private void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getActivity())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivityForResult(intent,1);
            }
        }
    }

    @Override
    public void onInitViewModel() {
        // TODO 牛逼
    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        baseSetting = DataBindingUtil.inflate(inflater, R.layout.fragment_base_setting, container, false);
        return baseSetting.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }

    public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
