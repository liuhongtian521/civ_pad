package com.askia.coremodel.datamodel.database.db;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DBExamPlan extends RealmObject implements Serializable {


    private String createBy;
    private Long createTime;
    private Long endTime;
    private String examCode;
    private String examName;
    private String examStatus;
    @PrimaryKey
    private String id;
    private Long startTime;
    private String sysOrgCode;
    private String updateBy;
    private Long updateTime;
    private String verifyStartTime;
    private String verifyEndTime;
    private String verifyIntervalTime = "3"; //识别间隔时间，默认3S v1.3.2添加

    public String getVerifyIntervalTime() {
        return verifyIntervalTime;
    }

    public void setVerifyIntervalTime(String verifyIntervalTime) {
        this.verifyIntervalTime = verifyIntervalTime;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    private String verifyCode;

    public String getVerifyStartTime() {
        return verifyStartTime;
    }

    public void setVerifyStartTime(String verifyStartTime) {
        this.verifyStartTime = verifyStartTime;
    }

    public String getVerifyEndTime() {
        return verifyEndTime;
    }

    public void setVerifyEndTime(String verifyEndTime) {
        this.verifyEndTime = verifyEndTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(String examStatus) {
        this.examStatus = examStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getSysOrgCode() {
        return sysOrgCode;
    }

    public void setSysOrgCode(String sysOrgCode) {
        this.sysOrgCode = sysOrgCode;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
