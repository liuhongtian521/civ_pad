package com.askia.coremodel.event;

import java.io.Serializable;

public class FaceDBHandleEvent implements Serializable {
    private int current;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    private String faceId;
}
