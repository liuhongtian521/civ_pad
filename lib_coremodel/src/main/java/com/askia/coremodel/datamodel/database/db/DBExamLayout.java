package com.askia.coremodel.datamodel.database.db;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 考试编排
 */
public class DBExamLayout extends RealmObject implements Serializable {

    private String admissionCard;
    private String arrangeId;
    private String examRoomId;
    private String examineeId;
    @PrimaryKey
    private String id;
    private String seatNo;

    public String getAdmissionCard() {
        return admissionCard;
    }

    public void setAdmissionCard(String admissionCard) {
        this.admissionCard = admissionCard;
    }

    public String getArrangeId() {
        return arrangeId;
    }

    public void setArrangeId(String arrangeId) {
        this.arrangeId = arrangeId;
    }

    public String getExamRoomId() {
        return examRoomId;
    }

    public void setExamRoomId(String examRoomId) {
        this.examRoomId = examRoomId;
    }

    public String getExamineeId() {
        return examineeId;
    }

    public void setExamineeId(String examineeId) {
        this.examineeId = examineeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }
}
