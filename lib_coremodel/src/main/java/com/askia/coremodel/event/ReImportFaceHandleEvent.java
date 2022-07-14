package com.askia.coremodel.event;

import java.io.Serializable;

/**
 * Created by ymy
 * Description：人脸插入失败记录实体
 * Date:2022/7/12 13:48
 */
public class ReImportFaceHandleEvent implements Serializable {

    public String getFaceNumber() {
        return faceNumber;
    }

    public void setFaceNumber(String faceNumber) {
        this.faceNumber = faceNumber;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String faceNumber;//考试编号
    private String path;//图片路径
}
