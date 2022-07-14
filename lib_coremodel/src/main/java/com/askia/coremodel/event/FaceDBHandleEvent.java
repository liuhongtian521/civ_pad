package com.askia.coremodel.event;

import java.io.Serializable;
import java.util.List;

public class FaceDBHandleEvent implements Serializable {
    private int current; //当前位置
    private String faceId; //人脸库id
    private int total; //照片总数
    private String message; //插入信息
    private int state; //是否全部插入完成 0//错误 1//完成 2//人脸库插入异常
    private String faceNum; //照片标识

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }

    private List<String> errorList; //人脸库插入异常集合

    public String getFaceNum() {
        return faceNum;
    }
    public void setFaceNum(String faceNum) {
        this.faceNum = faceNum;
    }
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    public int getCurrent() {
        return current;
    }
    public void setCurrent(int current) {
        this.current = current;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }
}
