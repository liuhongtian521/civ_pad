package com.lncucc.authentication.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.ImageUtil;
import com.askia.common.util.MyToastUtils;
import com.askia.common.util.NV21ToBitmap;
import com.askia.common.util.faceUtils.DrawHelper;
import com.askia.common.widget.camera.CameraHelper;
import com.askia.common.widget.camera.CameraListener;
import com.askia.coremodel.rtc.Constants;
import com.askia.coremodel.viewmodel.FaceDetectorViewModel;
import com.bigdata.facedetect.FaceDetect;
import com.blankj.utilcode.util.LogUtils;
import com.unicom.facedetect.detect.FaceDetectManager;
import com.unicom.facedetect.detect.FaceDetectResult;

import java.io.ByteArrayOutputStream;
import java.io.File;

import a.a.a.a.a;
import io.reactivex.Observable;

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
    // 连续5帧在指定区 域
    protected int frames = 0;
    protected TextureView mPreview;
    private long startDectTime;
    private long startSearchTime;
    //连续50帧没有人脸数据
    protected int notface = 0;
    public FaceDetectorViewModel detectorViewModel;

    public String mSeCode;


    @Override
    public void onInit() {
    }

    @Override
    public void onInitViewModel() {
        detectorViewModel = ViewModelProviders.of(getActivity()).get(FaceDetectorViewModel.class);
    }


    @Override
    public void onSubscribeViewModel() {

    }

    protected abstract void setUI(FaceDetectResult detectResult);

    protected abstract void getmSeCode();

    protected abstract String getStuNo();

    protected abstract boolean isComputen();

    public void setmSeCode(String seCode) {
        mSeCode = seCode;
    }

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
                        , cameraId, isMirror, true, false);
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
                //人脸处理，检测照片中是否有人脸
                FaceDetect.FaceColorResult faceResult = FaceDetectManager.getInstance().checkFaceFromNV21(nv21, previewSize.width, previewSize.height, drawHelper.getCameraDisplayOrientation());
                if (faceResult == null || faceResult.faceBmp == null || faceResult.faceRect == null) {
//                    MyToastUtils.error("未检测到人脸", Toast.LENGTH_SHORT);
                    frames = 0;
                } else {
                    if (frames < 10) {
                        frames++;
                        return;
                    } else {
                        mFaceDecting = false;
                    }

                    YuvImage image = new YuvImage(nv21, ImageFormat.NV21, previewSize.width, previewSize.height, null);
                    ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
                    image.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 70, outputSteam);
                    byte[] jpegData = outputSteam.toByteArray();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    float[] feature = FaceDetectManager.getInstance().getFaceFeatureByData(jpegData);
                    FaceDetectResult detectResult = FaceDetectManager.getInstance().faceDetect(feature, 0.7f);
                    //获取人脸特征
//                    float[] feature = FaceDetectManager.getInstance().getLocalFaceFeatureByBGRData(faceResult.faceBmp, previewSize.width, previewSize.height, faceResult.keypoints);
                    //人脸对比
//                    FaceDetectResult detectResult = FaceDetectManager.getInstance().faceDetect(feature, 0.7f);
                    if (detectResult == null) {
                        frames = 0;
                        LogUtils.e("detect result ->    null");
                        goContinueDetectFace();
                        return;
                    }
                    Log.e("TagSnakesnake", "刷脸分数:" + detectResult.similarity);
//                    LogUtils.e("detect result ->", detectResult.similarity);
//                    Log.e("TagSnake", detectResult.faceId + ":" + detectResult.similarity + ":" + detectResult.faceNum);

                    frames = 0;
                    if (detectResult != null) {
                        byte[] newNav21 = new byte[nv21.length];
                        System.arraycopy(nv21, 0, newNav21, 0, nv21.length);//数据处理用的
                        NV21ToBitmap nv21Tool = new NV21ToBitmap(getContext());
                        Bitmap bitmap = nv21Tool.nv21ToBitmap(newNav21, previewSize.width, previewSize.height);
                        Rect faceRect = faceResult.faceRect;
                        if (faceRect != null) {
                            bitmap = ImageUtil.imageCrop(bitmap, faceRect);
                        }
                        bitmap = com.blankj.utilcode.util.ImageUtils.rotate(bitmap, 0, 0, 0);
                        if (mSeCode == null) {
                            getmSeCode();
                        }


                        if (isComputen()) {
                            com.blankj.utilcode.util.ImageUtils.save(bitmap,
                                    Constants.STU_EXPORT + File.separator + mSeCode + File.separator + "photo" + File.separator + getStuNo() + ".jpg",
                                    Bitmap.CompressFormat.PNG);
                        } else if (mSeCode != null && detectResult.faceNum != null && !"".equals(detectResult.faceNum))
                            com.blankj.utilcode.util.ImageUtils.save(bitmap,
                                    Constants.STU_EXPORT + File.separator + mSeCode + File.separator + "photo" + File.separator + detectResult.faceNum + ".jpg",
                                    Bitmap.CompressFormat.PNG);
//                        String base64 = ImageUtil.encodeImage(bitmap);
                        setUI(detectResult);
                        bitmap.recycle();
                    } else {
                        goContinueDetectFace();
                    }
                }
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

    public void closeFace() {
        mFaceDecting = false;
    }

    public void releaseCamera() {
        cameraHelper.stop();
    }

    public void startCamera() {
        cameraHelper.start();
    }

}
