package com.askia.coremodel.event;

import java.io.Serializable;

public class FaceDBHandleEvent implements Serializable {
    private int current; //当前位置
    private String faceId; //人脸库id
    private int total; //照片总数
    private String message; //插入信息
    private int state; //是否全部插入完成 0false 1 true

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
