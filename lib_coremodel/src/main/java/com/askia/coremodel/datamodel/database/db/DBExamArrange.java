package com.askia.coremodel.datamodel.database.db;


import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 考场安排
 */
public class DBExamArrange  extends RealmObject implements Serializable {


    @PrimaryKey
    private String id;
    private String createBy;
    private Long createTime;
    private String startTime;
    private String endTime;
    private String examCode;
    private String seCode;
    private String seName;
    private String subCode;
    private String subName;
    private String sysOrgCode;
    private String updateBy;
    private Long updateTime;

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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeCode() {
        return seCode;
    }

    public void setSeCode(String seCode) {
        this.seCode = seCode;
    }

    public String getSeName() {
        return seName;
    }

    public void setSeName(String seName) {
        this.seName = seName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
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

    @Override
    public String toString() {
        return "DBExamArrange{" +
                "id='" + id + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime=" + createTime +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", examCode='" + examCode + '\'' +
                ", seCode='" + seCode + '\'' +
                ", seName='" + seName + '\'' +
                ", subCode='" + subCode + '\'' +
                ", subName='" + subName + '\'' +
                ", sysOrgCode='" + sysOrgCode + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
