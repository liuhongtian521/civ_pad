package com.askia.coremodel.rtm;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewManager;

import com.askia.coremodel.datamodel.database.repository.SharedPreUtil;
import com.askia.coremodel.event.CommonImEvent;
import com.askia.coremodel.util.JsonUtil;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.ttsea.jrxbus2.RxBus2;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmStatusCode;

public class IMUtils
{
    private Context mContext;
    private static IMUtils sIMUtils;
    private ChatManager mChatManager;
    private RtmClient mRtmClient;
    private String mAppId;

    public static IMUtils getInstance(Context context)
    {
        if(sIMUtils == null)
            sIMUtils = new IMUtils(context);
        return sIMUtils;
    }

    public IMUtils(Context context)
    {
        mContext = context;
    }

    public void init(String appId)
    {
        mAppId = appId;

        // 声网IM
        mChatManager = new ChatManager(mContext);
        mChatManager.init(mAppId);
        mRtmClient = mChatManager.getRtmClient();

        addImListener();
    }

    public void login(String account,ResultCallback<Void> resultCallback)
    {
        if(mRtmClient == null)
        {
            init(SharedPreUtil.getInstance().getAgoraid());
        }
        mRtmClient.login(null, account,resultCallback);
    }



    public void logout(ResultCallback<Void> resultCallback)
    {
        if(mRtmClient != null)
            mRtmClient.logout(resultCallback);
    }

    public RtmClient getRtmClient()
    {
        return mRtmClient;
    }

    public void addImListener()
    {
        mChatManager.registerListener(new MyRtmClientListener());
    }

    class MyRtmClientListener implements RtmClientListener {

        @Override
        public void onConnectionStateChanged(final int state, int reason) {
          /*  switch (state) {
                case RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING:
                    showToast(getString(R.string.reconnecting));
                    break;
                case RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED:
                    showToast(getString(R.string.account_offline));
                    setResult(MessageUtil.ACTIVITY_RESULT_CONN_ABORTED);
                    finish();
                    break;
            }*/
          LogUtils.d("qinyy","IM onConnectionStateChanged state " + state + "  reason " + reason);
        }

        @Override
        public void onMessageReceived(final RtmMessage message, final String peerId) {
            LogUtils.d("qinyy","IM onMessageReceived " + message.getText());
            if(TextUtils.isEmpty(message.getText()))
            {
                return;
            }
            CommonImEvent event = JsonUtil.Str2JsonBean(message.getText(), CommonImEvent.class);
            RxBus2.getInstance().post(event);
           /* String content = message.getText();
            if (peerId.equals(mPeerId)) {
                MessageBean messageBean = new MessageBean(peerId, content,false);
                messageBean.setBackground(getMessageColor(peerId));
                mMessageBeanList.add(messageBean);
                mMessageAdapter.notifyItemRangeChanged(mMessageBeanList.size(), 1);
                mRecyclerView.scrollToPosition(mMessageBeanList.size() - 1);
            } else {
                MessageUtil.addMessageBean(peerId, content);
            }*/
        }

        @Override
        public void onTokenExpired() {

        }


    }

}
