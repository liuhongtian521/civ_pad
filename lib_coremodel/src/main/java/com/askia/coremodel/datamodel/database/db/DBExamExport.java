package com.askia.coremodel.datamodel.database.db;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Create bt she:
 * 数据导出
 * @date 2021/8/9
 */
public class DBExamExport extends RealmObject implements Serializable {
    @PrimaryKey
    private String id;
    private String stuNo;//考生号
    private String stuName;
    private String examineeId;//考生id
    private String verifyTime;//验证时间
    private String verifyResult = "0";//验证结果 0未验证 1成功 2失败 3存疑
    private String matchRate;//匹配率
    private String seCode;//场次码
    private String entrancePhotoUrl;//入场照片地址 base64
    private String examCode;//考试代码
    private String createBy;//创建人
    private String createTime;//创建时间
    private String updataUp;//
    private String updataTime;//
    private String sysOrgCode;//组织机构代码
    private String equipment;//设备id mac 地址int
    private String siteCode;//考点编号
    private String idCard;//身份证号码
    private String manualVerifyResult;//0 自动审核 1人工审核
    private String healthCode; //健康码
    private int upLoadStatus = 0; //上传状态 0未上传 1上传成功（只有在线导出模式使用，用于增量上传）
    private String roomNo; //考场编号 v1.3.2新增（验证数据统计模块需要）

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    private String orgCode; //考点代码

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
    public int getUpLoadStatus() {
        return upLoadStatus;
    }

    public void setUpLoadStatus(int upLoadStatus) {
        this.upLoadStatus = upLoadStatus;
    }

    public String getHealthCode() {
        return healthCode;
    }

    public void setHealthCode(String healthCode) {
        this.healthCode = healthCode;
    }

    public String getManualVerifyResult() {
        return manualVerifyResult;
    }

    public void setManualVerifyResult(String manualVerifyResult) {
        this.manualVerifyResult = manualVerifyResult;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getExamineeId() {
        return examineeId;
    }

    public void setExamineeId(String examineeId) {
        this.examineeId = examineeId;
    }

    public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getVerifyResult() {
        return verifyResult;
    }

    public void setVerifyResult(String verifyResult) {
        this.verifyResult = verifyResult;
    }

    public String getMatchRate() {
        return matchRate;
    }

    public void setMatchRate(String matchRate) {
        this.matchRate = matchRate;
    }

    public String getSeCode() {
        return seCode;
    }

    public void setSeCode(String seCode) {
        this.seCode = seCode;
    }

    public String getEntrancePhotoUrl() {
        return entrancePhotoUrl;
    }

    public void setEntrancePhotoUrl(String entrancePhotoUrl) {
        this.entrancePhotoUrl = entrancePhotoUrl;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdataUp() {
        return updataUp;
    }

    public void setUpdataUp(String updataUp) {
        this.updataUp = updataUp;
    }

    public String getUpdataTime() {
        return updataTime;
    }

    public void setUpdataTime(String updataTime) {
        this.updataTime = updataTime;
    }

    public String getSysOrgCode() {
        return sysOrgCode;
    }

    public void setSysOrgCode(String sysOrgCode) {
        this.sysOrgCode = sysOrgCode;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }
}
