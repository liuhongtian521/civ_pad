package com.askia.coremodel.event;

import java.io.Serializable;
import java.util.List;

public class CommonImEvent implements Serializable
{
    private String msgType;
    private String channelID;
    private String userIdNum;
    private String siteNum;
    private String fromSiteNum;
    private String queueNum;
    private String siteType;
    private String action;
    private String recordCode;
    private String thirdAgoraId;
    private String thirdSiteNum;
    private String totalSize;
    private String seatNumber;
    private String qrCode;
    private String taxpayerID;
    private String taxpayerName;
    private String money;
    private String certificatePath;
    private String serviceCategory;
    // 收款税务机关代码
    private String skswjgdm;
    // 电子税票号码
    private String dzsp;

    public String getSkswjgdm() {
        return skswjgdm;
    }

    public void setSkswjgdm(String skswjgdm) {
        this.skswjgdm = skswjgdm;
    }

    public String getDzsp() {
        return dzsp;
    }

    public void setDzsp(String dzsp) {
        this.dzsp = dzsp;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getTaxpayerID() {
        return taxpayerID;
    }

    public void setTaxpayerID(String taxpayerID) {
        this.taxpayerID = taxpayerID;
    }

    public String getTaxpayerName() {
        return taxpayerName;
    }

    public void setTaxpayerName(String taxpayerName) {
        this.taxpayerName = taxpayerName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    private List<String> filePathList;

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public List<String> getFilePathList() {
        return filePathList;
    }

    public void setFilePathList(List<String> filePathList) {
        this.filePathList = filePathList;
    }

    public String getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getUserIdNum() {
        return userIdNum;
    }

    public void setUserIdNum(String userIdNum) {
        this.userIdNum = userIdNum;
    }

    public String getSiteNum() {
        return siteNum;
    }

    public void setSiteNum(String siteNum) {
        this.siteNum = siteNum;
    }

    public String getFromSiteNum() {
        return fromSiteNum;
    }

    public void setFromSiteNum(String fromSiteNum) {
        this.fromSiteNum = fromSiteNum;
    }

    public String getQueueNum() {
        return queueNum;
    }

    public void setQueueNum(String queueNum) {
        this.queueNum = queueNum;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getThirdAgoraId() {
        return thirdAgoraId;
    }

    public void setThirdAgoraId(String thirdAgoraId) {
        this.thirdAgoraId = thirdAgoraId;
    }

    public String getThirdSiteNum() {
        return thirdSiteNum;
    }

    public void setThirdSiteNum(String thirdSiteNum) {
        this.thirdSiteNum = thirdSiteNum;
    }
}
