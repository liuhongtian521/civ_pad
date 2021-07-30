package com.askia.common.util;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.askia.common.R;


public class PageCountDownTimer extends CountDownTimer
{
    private TextView mTextView;
    private Context mContext;
    private Handler.Callback mCallback;
    /**
     * @param textView   The TextView
     *
     *
     * @param millisInFuture The number of millis in the future from the call
     *       to {@link #start()} until the countdown is done and {@link #onFinish()}
     *       is called.
     * @param countDownInterval The interval along the way to receiver
     *       {@link #onTick(long)} callbacks.
     */
    public PageCountDownTimer(Context context, TextView textView, long millisInFuture, long countDownInterval, Handler.Callback callback) {
        super(millisInFuture, countDownInterval);
        this.mTextView = textView;
        mContext = context;
        mCallback = callback;
    }

    /**
     * 倒计时期间会调用
     * @param millisUntilFinished
     */
    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false); //设置不可点击
        mTextView.setText(millisUntilFinished / 1000 + "s"); //设置倒计时时间
    }

    /**
     * 倒计时完成后调用
     */
    @Override
    public void onFinish() {
        Message msg = Message.obtain();
        msg.what = 100;
        mCallback.handleMessage(msg);
    }
}
