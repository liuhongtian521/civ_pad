package com.askia.common.base;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.askia.common.util.Utils;
import com.askia.common.widget.CountDownText;
import com.blankj.utilcode.util.LogUtils;
import com.yanyusong.y_divideritemdecoration.Dp2Px;

public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;
    private View mRootView;
    private CountDownText mCountDownText;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 初始化viewmodel
        onInitViewModel();
        // 初始化databing 绑定布局
        mRootView = onInitDataBinding(inflater, container);
        // 初始化其他
        onInit();
        // 订阅viewmodel 数据变动事件
        onSubscribeViewModel();
        return mRootView;
    }

    public abstract void onInit();

    public abstract void onInitViewModel();

    public abstract View onInitDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    public abstract void onSubscribeViewModel();


    /**
     * 获取宿主Activity
     *
     * @return BaseActivity
     */
    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }


    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getHoldingActivity().addFragment(fragment, frameId);

    }


    /**
     * 替换fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void replaceFragment(BaseFragment fragment, @IdRes int frameId) {
        Utils.checkNotNull(fragment);
        getHoldingActivity().replaceFragment(fragment, frameId);
    }


    /**
     * 隐藏fragment
     *
     * @param fragment
     */
    protected void hideFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getHoldingActivity().hideFragment(fragment);
    }


    /**
     * 显示fragment
     *
     * @param fragment
     */
    protected void showFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getHoldingActivity().showFragment(fragment);
    }


    /**
     * 移除Fragment
     *
     * @param fragment
     */
    protected void removeFragment(BaseFragment fragment) {
        Utils.checkNotNull(fragment);
        getHoldingActivity().removeFragment(fragment);

    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment() {
        getHoldingActivity().popFragment();
    }

    public void resolveResponseErrorCode(int code, String mes) {
        //  ((BaseActivity) getActivity()).resolveResponseErrorCode(code,mes);
    }
/*
    public void addCountDownView(ViewGroup parent,int second) {
        mCountDownText = new CountDownText(getActivity(),second, new CountDownText.CountDownFinishListener() {
            @Override
            public void onFinish() {
                if(((BaseActivity) getActivity()) != null && getUserVisibleHint())
                    ((BaseActivity) getActivity()).startActivityByRouter(ARouterPath.QMAIN_ACTIVITY);
            }
        });
        if (parent instanceof FrameLayout) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(Utils.dp2px(getActivity(), 87), Utils.dp2px(getActivity(), 35));
            layoutParams.setMargins(Utils.dp2px(getActivity(), 660), Utils.dp2px(getActivity(), 15), Utils.dp2px(getActivity(), 50), Utils.dp2px(getActivity(), 489));
            parent.addView(mCountDownText, layoutParams);
        } else if (parent instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(Utils.dp2px(getActivity(), 87), Utils.dp2px(getActivity(), 35));
            layoutParams.setMargins(Utils.dp2px(getActivity(), 660), Utils.dp2px(getActivity(), 15), Utils.dp2px(getActivity(), 50), Utils.dp2px(getActivity(), 489));
            parent.addView(mCountDownText, layoutParams);
        }
    }*/

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mCountDownText != null) {
                mCountDownText.start();
            }
            com.blankj.utilcode.util.LogUtils.d("=================this is " + this.getClass().getSimpleName() + "=================");
            //相当于Fragment的onResume
        } else {
            //相当于Fragment的onPause
            if (mCountDownText != null) {
                mCountDownText.cancel();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            //相当于Fragment的onPause
            if (mCountDownText != null) {
                mCountDownText.cancel();
            }
        } else {  // 在最前端显示 相当于调用了onResume();
            if (mCountDownText != null) {
                mCountDownText.start();
            }
        }
    }

    //显示Dialog
    public void showLogadingDialog() {
        ((BaseActivity) getActivity()).showNetDialog();
    }

    public void closeLogadingDialog() {
        ((BaseActivity) getActivity()).dismissNetDialog();
    }

}
