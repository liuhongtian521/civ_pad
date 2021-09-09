package com.askia.coremodel.datamodel.face;

import com.bigdata.facedetect.FaceDetect;
import com.unicom.facedetect.detect.FaceDetectResult;

import java.io.Serializable;

/**
 * Create bt she:
 *
 * @date 2021/9/9
 */
public class FaceMsgBase implements Serializable {
    private FaceDetectResult faceColorResult;
    private int type;

    public FaceDetectResult getFaceColorResult() {
        return faceColorResult;
    }

    public void setFaceColorResult(FaceDetectResult faceColorResult) {
        this.faceColorResult = faceColorResult;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
