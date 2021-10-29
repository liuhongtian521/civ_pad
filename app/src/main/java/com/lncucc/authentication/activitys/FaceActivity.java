package com.lncucc.authentication.activitys;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.TextureView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.askia.common.widget.camera.CameraHelper;
import com.askia.common.widget.camera.CameraListener;
import com.lncucc.authentication.R;

/*
* 这个页面是相机展示的activity页面，根据需要可以改成fragment
*
 */
public class FaceActivity extends AppCompatActivity implements ViewTreeObserver.OnGlobalLayoutListener {

    private CameraHelper cameraHelper;
    private Camera.Size previewSize;
    private Integer cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;//后置还是前置
    TextureView previewView;
    int faceHaveTime = 0;
    boolean canFindFace = true;
    int wight, height = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        previewView = findViewById(R.id.preview);
        previewView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }


    @Override
    public void onGlobalLayout() {
        previewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        initAll();
    }

    private void initAll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
            attributes.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            getWindow().setAttributes(attributes);
        }
        // Activity启动后就锁定为启动时的方向
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            default:
                break;
        }

        initCamera();
    }

    private void initCamera() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        CameraListener cameraListener = new CameraListener() {
            @Override
            public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
                previewSize = camera.getParameters().getPreviewSize();
            }

            @Override
            public void onPreview(byte[] data, Camera camera) {
                if (!canFindFace)
                    return;

//                数据处理
            }

            @Override
            public void onCameraClosed() {

            }

            @Override
            public void onCameraError(Exception e) {

            }

            @Override
            public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
            }
        };


        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(previewView.getMeasuredWidth(), previewView.getMeasuredHeight()))
                .rotation(getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(cameraID != null ? cameraID : Camera.CameraInfo.CAMERA_FACING_FRONT)
                .isMirror(false)
                .previewOn(previewView)
                .cameraListener(cameraListener)
                .build();
        cameraHelper.init();

        if (cameraHelper != null) {
            cameraHelper.start();
        }
    }

}