package com.lncucc.authentication.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.askia.common.util.faceUtils.DrawHelper;
import com.askia.common.widget.camera.CameraHelper;
import com.askia.common.widget.camera.CameraListener;
import com.askia.coremodel.rtc.Constants;
import com.askia.coremodel.rtc.FileUtil;
import com.askia.coremodel.util.ImageUtil;
import com.bigdata.facedetect.FaceDetect;
import com.blankj.utilcode.util.ImageUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.widgets.camera.AutoFitTextureView;
import com.unicom.facedetect.detect.FaceDetectManager;
import com.unicom.facedetect.detect.FaceDetectResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;

import io.reactivex.disposables.Disposable;

/**
 * 人工审核人脸比对
 */
@Route(path = ARouterPath.TAKE_PHOTO)
public class TakePhotoActivity extends AppCompatActivity {

    private CameraHelper cameraHelper;
    AutoFitTextureView previewView;
    public Disposable mDisposable;
    //学生编号
    private String stuNo, seCode;
    //查询图片的路径
    private String sPath;
    protected int frames = 0;
    private DrawHelper drawHelper;
    private Camera.Size previewSize;
    Handler handler;
    private boolean mDetecting = true;
    private String similarity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_takephoto);
        previewView = findViewById(R.id.preview);
        initCamera();
        stuNo = getIntent().getStringExtra("stuNo");
        seCode = getIntent().getStringExtra("seCode");
        sPath = getIntent().getStringExtra("path");
        initHandler();
        initEvent();
    }

    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        previewView.setAspectRatio(1280, 720);
        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                previewSize = camera.getParameters().getPreviewSize();
                drawHelper = new DrawHelper(previewView.getWidth(),
                        previewView.getHeight(),
                        previewView.getWidth(),
                        previewView.getHeight(),
                        displayOrientation,
                        cameraId,
                        isMirror,
                        true,
                        false);
            }

            @Override
            public void onPreview(byte[] data, Camera camera) {
                if (!mDetecting)
                    return;
                mDetecting = false;
                //倒计时结束
                if (handler != null) {
                    handler.post(() -> {
                        FaceDetect.FaceColorResult faceResult = FaceDetectManager.getInstance()
                                .checkFaceFromNV21(data, previewSize.width, previewSize.height, drawHelper.getCameraDisplayOrientation());
                        if (faceResult == null || faceResult.faceBmp == null || faceResult.faceRect == null) {
                            frames = 0;
                            mDetecting = true;
                        } else {
                            if (frames < 2) {
                                frames++;
                                mDetecting = true;
                            } else {
                                frames = 0;
                                YuvImage image = new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height, null);
                                ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
                                byte[] jpegData;
                                image.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 80, outputSteam);
                                jpegData = outputSteam.toByteArray();
                                //人脸比对
//                                float[] feature = FaceDetectManager.getInstance().getFaceFeatureByData(jpegData);
//                                FaceDetectResult detectResult = FaceDetectManager.getInstance().faceDetect(feature, 0.80f);

                                Bitmap bitmap = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length);

                                if (faceResult.faceRect != null) {
                                    bitmap = ImageUtil.imageCrop(bitmap, faceResult.faceRect);
                                }

                                String imgPath = Constants.STU_EXPORT + File.separator + seCode + File.separator + "photo" + File.separator + stuNo + ".jpg";
                                bitmap = ImageUtil.converBitmap(bitmap);
                                bitmap = ImageUtil.sampleSize(bitmap);
                                //保存图片
                                boolean success = ImageUtils.save(bitmap, imgPath, Bitmap.CompressFormat.PNG);
                                bitmap.recycle();

                                byte[] b1 = FileUtil.getBytesByFile(sPath);
                                //添加人脸1：1 比对搜索的学生和当前拍照的人进行比对
                                FaceDetect detect = new FaceDetect();
                                float result = detect.nativeCheckFace(jpegData,b1,false);
                                similarity = new DecimalFormat("0.00000").format(result);
                                //保存成功,释放bitmap,并关闭当前页
                                if (success) {
                                    cameraHelper.stop();
                                }
                            }
                        }
                    });
                }

            }

            @Override
            public void onCameraClosed() {
                Intent intent = new Intent();
                intent.putExtra("similarity", similarity);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onCameraError(Exception e) {

            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
            }
        };

        //默认调用后置摄像头
        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(previewView.getMeasuredWidth(), previewView.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(0)
                .isMirror(false)
                .previewOn(previewView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();

        if (cameraHelper != null) {
            cameraHelper.start();
        }
    }

    private void initEvent(){
        findViewById(R.id.iv_close).setOnClickListener(v -> {
            //关闭当前页面
            finish();
        });
    }

    HandlerThread mHandlerThread;

    private void initHandler() {
        if (handler == null) {
            mHandlerThread = new HandlerThread("faceThread");
            mHandlerThread.start();
            Looper mLooper = mHandlerThread.getLooper();
            handler = new Handler(mLooper) {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                }
            };
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        if (handler != null) {
            mHandlerThread.quit();
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        mDetecting = false;
    }
}
