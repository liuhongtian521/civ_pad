package com.lncucc.authentication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.askia.common.base.ARouterPath;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FraFaceBinding;

/**
 * Create bt she:
 *fragment 人脸展示
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
    }

    @Override
    protected void setUI(boolean showFace) {
        //获取到了数据

    }

    @Override
    public View onInitDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.fra_face, container, false);
        return mDataBinding.getRoot();
    }
}
