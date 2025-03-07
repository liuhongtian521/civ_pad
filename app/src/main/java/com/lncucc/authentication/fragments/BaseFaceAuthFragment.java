package com.lncucc.authentication.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.TextureView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.common.util.NV21ToBitmap;
import com.askia.common.util.faceUtils.DrawHelper;
import com.askia.common.widget.camera.CameraHelper;
import com.askia.common.widget.camera.CameraListener;
import com.askia.coremodel.datamodel.face.FaceMsgBase;
import com.askia.coremodel.rtc.Constants;
import com.askia.coremodel.util.ImageUtil;
import com.askia.coremodel.viewmodel.FaceDetectorViewModel;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.bigdata.facedetect.FaceDetect;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.unicom.facedetect.detect.FaceDetectManager;
import com.unicom.facedetect.detect.FaceDetectResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;

import a.a.a.a.a;
import io.reactivex.Observable;

import static com.askia.coremodel.rtc.Constants.CAMERA_DEFAULT;

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
    private Integer rgbCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
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
    private boolean isSavedSuccess = false;


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

    CameraListener cameraListener;

    Handler handler;

    /**
     * 初始化相机
     */
    protected void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        cameraListener = new CameraListener() {
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
                mFaceDecting = false;
                if (handler != null)
                    handler.post(() -> {
                        FaceDetect.FaceColorResult faceResult = FaceDetectManager.getInstance().checkFaceFromNV21(nv21, previewSize.width, previewSize.height, drawHelper.getCameraDisplayOrientation());
                        if (faceResult == null || faceResult.faceBmp == null || faceResult.faceRect == null) {
                            frames = 0;
                            goContinueDetectFace();
                        } else {
                            if (frames < 2) {
                                frames++;
                                goContinueDetectFace();
                            } else {
                                frames = 0;
                                //人脸识别
                                YuvImage image = new YuvImage(nv21, ImageFormat.NV21, previewSize.width, previewSize.height, null);
                                ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
                                byte[] jpegData = null;
                                image.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 80, outputSteam);
                                jpegData = outputSteam.toByteArray();

                                float[] feature = FaceDetectManager.getInstance().getFaceFeatureByData(jpegData);
                                FaceDetectResult detectResult = FaceDetectManager.getInstance().faceDetect(feature, 0.85f);
                                //faceNum = "011412100080"
                                if (detectResult == null) {
                                    Log.e("TagSnakesnake", "detect result ->    null");
                                } else {
                                    String path = "";
                                    if (isComputen()) {
                                        path = Constants.STU_EXPORT + File.separator + mSeCode + File.separator + "photo" + File.separator + getStuNo() + ".jpg";
                                    } else if (mSeCode != null && detectResult.faceNum != null && !"".equals(detectResult.faceNum)) {
                                        path = Constants.STU_EXPORT + File.separator + mSeCode + File.separator + "photo" + File.separator + detectResult.faceNum + ".jpg";
                                    }
                                    if (!"".equals(path)) {
                                        Bitmap bitmap = null;
                                        BitmapFactory.Options options = new BitmapFactory.Options();
                                        Log.e("TagSnakesnake", "刷脸分数:" + detectResult.similarity);

                                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                                        bitmap = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length, options);
                                        if (faceResult.faceRect != null) {
                                            bitmap = ImageUtil.imageCrop(bitmap, faceResult.faceRect);
                                        }

                                        if (FileUtils.isFileExists(path)) {
                                            File file = FileUtils.getFileByPath(path);
                                            if (file != null)
                                                file.delete();
                                        }
                                        bitmap = ImageUtil.converBitmap(bitmap);// com.blankj.utilcode.util.ImageUtils.rotate(bitmap, 0, 0, 0);
                                        bitmap = ImageUtil.sampleSize(bitmap);
                                        isSavedSuccess = com.blankj.utilcode.util.ImageUtils.save(bitmap, path, Bitmap.CompressFormat.PNG);
                                        bitmap.recycle();
                                    }
                                }
                                Message message = new Message();
                                message.obj = detectResult;
                                if (handler != null) {
                                    handler.sendMessage(message);
                                }
                            }
                        }
                    });
//                FaceDetect.FaceColorResult faceResult = FaceDetectManager.getInstance().checkFaceFromNV21(nv21, previewSize.width, previewSize.height, drawHelper.getCameraDisplayOrientation());

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
        isCameraInit = true;
        setCameraHelper(1);
    }


    public void setCameraHelper(int type) {
        this.type = type;
        rgbCameraID = SPUtils.getInstance().getInt(CAMERA_DEFAULT, 0);
        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(mPreview.getWidth(), mPreview.getHeight()))
                .rotation(getActivity().getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(rgbCameraID != null ? rgbCameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(mIsMirror)
                .previewOn(mPreview)
                .rotation(type)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();
        startCamera();
//        cameraHelper.start();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isCameraInit)
                startCamera();
//                cameraHelper.start();
        } else {
            //相当于Fragment的onPause
            cameraHelper.stop();
        }
    }

    int type = 1;//旋转模式


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            cameraHelper.stop();
            return;
        } else {  // 在最前端显示 相当于调用了onResume();
            if (!isCameraInit)
                startCamera();
//                cameraHelper.start();
        }
    }

    public void goContinueDetectFace() {
        mFaceDecting = true;
    }

    public void closeFace() {
        mFaceDecting = false;
    }

    public void releaseCamera() {
        if (handler != null) {
            mHandlerThread.quit();

            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        cameraHelper.stop();
    }

    HandlerThread mHandlerThread;

    public void startCamera() {
        if (handler == null) {
            mHandlerThread = new HandlerThread("faceThread");
            mHandlerThread.start();
            Looper mLooper = mHandlerThread.getLooper();
            handler = new Handler(mLooper) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    setUI((FaceDetectResult) msg.obj);
                }
            };
        }
        cameraHelper.start();
        goContinueDetectFace();
    }

}
