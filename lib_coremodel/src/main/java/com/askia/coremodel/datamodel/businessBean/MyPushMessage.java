package com.askia.coremodel.datamodel.businessBean;

import java.io.Serializable;

public class MyPushMessage implements Serializable
{

    /**
     * msgType : 推送消息类型  预留  暂时写空或者写死1都可以
     * publishTime : 发布时间 yyyy-mm-dd hh:mm:s
     * title : 标题
     * content : 详情
     */

    private String msgType;
    private String publishTime;
    private String title;
    private String content;
    private String msgId;
    private boolean isRead;
    private String msgName;

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
