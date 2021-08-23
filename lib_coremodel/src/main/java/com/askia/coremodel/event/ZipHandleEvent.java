package com.askia.coremodel.event;

import java.io.Serializable;

public class ZipHandleEvent implements Serializable {
    private String zipPath;

    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSeCode() {
        return seCode;
    }

    public void setSeCode(String seCode) {
        this.seCode = seCode;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    private int progress;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message = "正在解压";
    private String filePath;
    private String seCode;
    private boolean result;
}
