package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.lncucc.authentication.R;
import com.lncucc.authentication.activitys.AuthenticationActivity;
import com.lncucc.authentication.databinding.FraFaceBinding;
import com.unicom.facedetect.detect.FaceDetectResult;

/**
 * Create bt she:
 * fragment 人脸展示
 *
 * @date 2021/8/6
 */
@Route(path = ARouterPath.FACE_SHOW_ACTIVITY)
public class FaceShowFragment extends BaseFaceAuthFragment {
    private FraFaceBinding mDataBinding;

    @Override
    public void onInit() {
        super.onInit();
        mIsMirror = false;
        //在布局结束后才做初始化操作
        mDataBinding.preview.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (!isCameraInit)
                initCamera();
        });

        mPreview = mDataBinding.preview;
        mPreview.setScaleX(-1);
        mPreview.setRotationY(180); // 镜面对称
    }

    @Override
    protected void setUI(FaceDetectResult detectResult) {
        //获取到了数据
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((AuthenticationActivity) getActivity()).getFace(detectResult);
            }
        });
    }

    @Override
    protected void getmSeCode() {
        mSeCode = ((AuthenticationActivity) getActivity()).getmSeCode();
    }

    @Override
    protected String getStuNo() {
        return ((AuthenticationActivity) getActivity()).getStuNo();
    }

    @Override
    protected boolean isComputen() {
        return ((AuthenticationActivity) getActivity()).isComparison();
    }

    public void setToUp(int type) {
        releaseCamera();

        setCameraHelper(type);
    }


    @Override
    public View onInitDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fra_face, container, false);
        return mDataBinding.getRoot();
    }
}
