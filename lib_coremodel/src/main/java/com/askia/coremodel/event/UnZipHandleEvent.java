package com.askia.coremodel.event;

import java.io.Serializable;

public class UnZipHandleEvent implements Serializable {
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getUnZipProcess() {
        return unZipProcess;
    }

    public void setUnZipProcess(int unZipProcess) {
        this.unZipProcess = unZipProcess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private int unZipProcess; //解压进度
    private String message; //解压信息
    private int code; //状态码
}
