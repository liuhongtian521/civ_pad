package com.lncucc.authentication.fragments;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.widget.ImageView;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.askia.common.base.BaseFragment;
import com.askia.common.util.faceUtils.ConfigUtil;
import com.askia.common.util.faceUtils.DrawHelper;
import com.askia.common.util.faceUtils.FaceHelper;
import com.askia.common.util.faceUtils.FaceListener;
import com.askia.common.util.faceUtils.RequestFeatureStatus;
import com.askia.common.widget.camera.CameraHelper;
import com.askia.common.widget.camera.CameraListener;
import com.askia.common.widget.face.FaceRectView;
import com.askia.coremodel.viewmodel.FaceAuthViewModel;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseFaceAuthFragment extends BaseFragment {
    // 最大识别人脸数量
    private final int MAX_DETECT_NUM = 3;
    private final String TAG = "FaceAuthFragment";
    private CameraHelper cameraHelper;
    private DrawHelper drawHelper;
    private Camera.Size previewSize;
    protected boolean mIsMirror = false;
    /**
     * 优先打开的摄像头
     */
    private Integer rgbCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
    protected boolean isCameraInit = false;
    public boolean mFaceDecting = true;
    // 连续5帧在指定区域
    protected int frames = 0;
    protected TextureView mPreview;
    private long startDectTime;
    private long startSearchTime;
    //连续50帧没有人脸数据
    protected int notface = 0;

    @Override
    public void onInit() {
    }

    @Override
    public void onInitViewModel() {
    }


    @Override
    public void onSubscribeViewModel() {

    }

    protected abstract void setUI(boolean showFace);

    /**
     * 初始化相机
     */
    protected void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                previewSize = camera.getParameters().getPreviewSize();
                drawHelper = new DrawHelper(mPreview.getWidth(), mPreview.getHeight(), mPreview.getWidth(), mPreview.getHeight(), displayOrientation
                        , cameraId, isMirror, true, false);//false,false:查询机   true,false:消费机
                Log.i(TAG, "onCameraOpened: " + drawHelper.toString());
            }


            /**
             * 修改了判断，先判断有没有人脸 最后在判断在不在区域内
             * @param nv21
             * @param camera 相机实例
             */
            @Override
            public void onPreview(final byte[] nv21, Camera camera) {
                if (!mFaceDecting)
                    return;

                //人脸处理
            }

            @Override
            public void onCameraClosed() {
                Log.i(TAG, "onCameraClosed: ");
            }

            @Override
            public void onCameraError(Exception e) {
                Log.i(TAG, "onCameraError: " + e.getMessage());
            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
                if (drawHelper != null) {
                    drawHelper.setCameraDisplayOrientation(displayOrientation);
                }
                Log.i(TAG, "onCameraConfigurationChanged: " + cameraID + "  " + displayOrientation);
            }
        };

        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(mPreview.getWidth(), mPreview.getHeight()))
                .rotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(rgbCameraID != null ? rgbCameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(mIsMirror)
                .previewOn(mPreview)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
        isCameraInit = true;
        cameraHelper.start();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isCameraInit)
                cameraHelper.start();
        } else {
            //相当于Fragment的onPause
            cameraHelper.stop();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            cameraHelper.stop();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();
            if (!isCameraInit)
                cameraHelper.start();
        }
    }

    public void goContinueDetectFace() {
        mFaceDecting = true;
    }


}
