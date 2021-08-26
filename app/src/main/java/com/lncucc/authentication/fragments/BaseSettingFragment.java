package com.lncucc.authentication.fragments;

import android.media.AudioManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kyleduo.switchbutton.SwitchButton;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentBaseSettingBinding;
import com.qmuiteam.qmui.widget.QMUISlider;

import org.jetbrains.annotations.NotNull;

/**
 * 数据导入
 */
public class BaseSettingFragment extends BaseFragment  {
    private FragmentBaseSettingBinding baseSetting;
    private AudioManager mgr;
    QMUISlider mSlider;
    SwitchButton sbSound;
    SwitchButton sbSoundT;

    @Override
    public void onInit() {
        mgr = (AudioManager) mActivity.getSystemService(mActivity.AUDIO_SERVICE);
        mSlider = baseSetting.slider;
        sbSound= baseSetting.sbSound;
        sbSoundT = baseSetting.sbTable;
        sbSound.setChecked(true);


        // 设置键盘音开启关闭
        sbSoundT.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // 开启
                Settings.System.putInt(mActivity.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0);
                MyToastUtils.success("设置成功",Toast.LENGTH_SHORT);
            } else {
                // 关闭
                Settings.System.putInt(mActivity.getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 1);
                MyToastUtils.success("设置成功",Toast.LENGTH_SHORT);
            }
        });
        // 静音设置
        sbSound.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                mgr.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE , 0);// 设置静音
                MyToastUtils.success("设置成功",Toast.LENGTH_SHORT);
            } else {
                mgr.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_UNMUTE, 1);//取消静音
                MyToastUtils.success("设置成功",Toast.LENGTH_SHORT);
            }
        });

        mSlider.setCurrentProgress(100);
        mSlider.setCallback(new QMUISlider.Callback(){

            @Override
            public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {
                LogUtils.e("change",progress);

            }

            @Override
            public void onTouchDown(QMUISlider slider, int progress, int tickCount, boolean hitThumb) {
                LogUtils.e("11",progress);
            }

            @Override
            public void onTouchUp(QMUISlider slider, int progress, int tickCount) {
                // slider 滑动后设置声音
                mgr.setStreamVolume(AudioManager.STREAM_SYSTEM, progress * mgr.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) / 100, AudioManager.FLAG_PLAY_SOUND);


            }

            @Override
            public void onStartMoving(QMUISlider slider, int progress, int tickCount) {

            }

            @Override
            public void onStopMoving(QMUISlider slider, int progress, int tickCount) {

            }
        });
    }

    @Override
    public void onInitViewModel() {
        // TODO 牛逼
    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        baseSetting = DataBindingUtil.inflate(inflater, R.layout.fragment_base_setting,container,false);
        return baseSetting.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }

    public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {

    }
    


}
