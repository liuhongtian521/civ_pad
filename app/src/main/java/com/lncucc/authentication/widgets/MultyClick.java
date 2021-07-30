package com.lncucc.authentication.widgets;

import android.os.Handler;

/**
 * Created by qinyy on 2016/11/17.
 */
public class MultyClick
{
    //params
    private int mClickTimes;
    private int mMillionSeconds;
    private onActivateListener mOnActivateListener;
    //
    private Runnable mRunnable;
    private int mCount;

    public MultyClick(int clickTimes, int millionSeconds, onActivateListener listener)
    {
        mClickTimes = clickTimes;
        mMillionSeconds = millionSeconds;
        mOnActivateListener = listener;
        initRunnable();
    }

    private void initRunnable()
    {
        mRunnable = new Runnable()
        {
            public void run()
            {
                mCount = 0;
            }
        };
    }

    private Handler mHandler = new Handler();

    public void onClick()
    {
        if (0 == mCount)
        {
            mHandler.postDelayed(mRunnable, mMillionSeconds);
        }
        mCount++;
        if (mClickTimes == mCount)
        {
            mOnActivateListener.onActivate();
        }
    }

    public interface onActivateListener
    {
        void onActivate();
    }

}
