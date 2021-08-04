package com.askia.coremodel.datamodel.database.db;


import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 考场安排
 */
public class DBExamArrange  extends RealmObject implements Serializable {

    private Long endTime;
    private String examCode;
    private String examPlanId;
    @PrimaryKey
    private String id;
    private String sessionsCode;
    private String sessionsName;
    private Long startTime;
    private String subjectCode;
    private String subjectName;

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

    public String getExamPlanId() {
        return examPlanId;
    }

    public void setExamPlanId(String examPlanId) {
        this.examPlanId = examPlanId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionsCode() {
        return sessionsCode;
    }

    public void setSessionsCode(String sessionsCode) {
        this.sessionsCode = sessionsCode;
    }

    public String getSessionsName() {
        return sessionsName;
    }

    public void setSessionsName(String sessionsName) {
        this.sessionsName = sessionsName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
