package com.askia.coremodel.datamodel.data;

import java.io.Serializable;

/**
 * Created by ymy
 * Description：
 * Date:2023/2/17 13:33
 */
public class StudentBean implements Serializable {

    private String seatNo;//座位编号
    private String name;
    private String seCode;
    private String examCode;
    private String roomNo;

    public String getValidationState() {
        return validationState;
    }

    public void setValidationState(String validationState) {
        this.validationState = validationState;
    }

    private String validationState;//验证状态

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeCode() {
        return seCode;
    }

    public void setSeCode(String seCode) {
        this.seCode = seCode;
    }

    public String getExamCode() {
        return examCode;
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
}
