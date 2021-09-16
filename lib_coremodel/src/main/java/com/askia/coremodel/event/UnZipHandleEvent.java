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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private int unZipProcess; //解压进度
    private String message; //解压信息
    private String fileName; //压缩包名称
    private String filePath; //解压路径

    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    private String zipPath; //压缩包路径
    private int code; //状态码

    @Override
    public String toString() {
        return "UnZipHandleEvent{" +
                "unZipProcess=" + unZipProcess +
                ", message='" + message + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", code=" + code +
                '}';
    }
}
