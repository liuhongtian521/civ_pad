package com.askia.coremodel.event;

import java.io.Serializable;

public class FaceHandleEvent implements Serializable
{
    // 0 清除人脸库 1 导入人脸库 2 下载远程人脸库 3 解压人脸库
    private int type;
    private int total;
    private int current;
    private String errorMsg;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
