package com.askia.coremodel.event;

import java.io.Serializable;

public class FaceDBHandleEvent implements Serializable {
    private int current; //当前位置
    private String faceId; //人脸库id
    private int total; //照片总数
    private String message; //插入信息

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
