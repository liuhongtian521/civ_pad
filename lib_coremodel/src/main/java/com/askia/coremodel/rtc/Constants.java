package com.askia.coremodel.rtc;

import android.os.Environment;

import java.io.File;

import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class Constants {
    private static final int BEAUTY_EFFECT_DEFAULT_CONTRAST = BeautyOptions.LIGHTENING_CONTRAST_NORMAL;
    private static final float BEAUTY_EFFECT_DEFAULT_LIGHTNESS = 0.7f;
    private static final float BEAUTY_EFFECT_DEFAULT_SMOOTHNESS = 0.5f;
    private static final float BEAUTY_EFFECT_DEFAULT_REDNESS = 0.1f;

    public static final BeautyOptions DEFAULT_BEAUTY_OPTIONS = new BeautyOptions(
            BEAUTY_EFFECT_DEFAULT_CONTRAST,
            BEAUTY_EFFECT_DEFAULT_LIGHTNESS,
            BEAUTY_EFFECT_DEFAULT_SMOOTHNESS,
            BEAUTY_EFFECT_DEFAULT_REDNESS);

    public static VideoEncoderConfiguration.VideoDimensions[] VIDEO_DIMENSIONS = new VideoEncoderConfiguration.VideoDimensions[] {
            VideoEncoderConfiguration.VD_320x240,
            VideoEncoderConfiguration.VD_480x360,
            VideoEncoderConfiguration.VD_640x360,
            VideoEncoderConfiguration.VD_640x480,
            new VideoEncoderConfiguration.VideoDimensions(960, 540),
            VideoEncoderConfiguration.VD_1280x720
    };

    public static final String PREF_NAME = "io.agora.openlive";
    public static final int DEFAULT_PROFILE_IDX = 5;
    public static final String PREF_RESOLUTION_IDX = "pref_profile_index";
    public static final String PREF_ENABLE_STATS = "pref_enable_stats";

    public static final String KEY_CLIENT_ROLE = "key_client_role";
    public static final String ACTION_USB_PERMISSION = "com.lncucc.authentication.USB_PERMISSION";

    public static final String CAMERA_DEFAULT = "camera_default";
    public static final String KEY_SOUND = "key_sound";
    public static final String SOUND_SETTING = "sound_setting";
    public static final String VOICE_SETTING = "voice_setting";
    public static final String DEFAULT_SOUND_SETTING = "default_sound_setting";
    public static final String VOICE_SOUND = "voice_sound";

    //apk下载地址
    public static final String apkPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "CivDownload";
    //压缩包路径
    public static final String ZIP_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "Examination";
    //压缩包路径
    public static final String STU_EXPORT= Environment.getExternalStorageDirectory().getPath() + File.separator + "ExamExport";
    //解压路径
    public static final String UN_ZIP_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "ExModel";
}
