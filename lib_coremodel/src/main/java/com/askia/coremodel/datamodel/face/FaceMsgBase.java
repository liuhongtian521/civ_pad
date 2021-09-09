package com.askia.coremodel.datamodel.face;

import com.bigdata.facedetect.FaceDetect;

import java.io.Serializable;

/**
 * Create bt she:
 *
 * @date 2021/9/9
 */
public class FaceMsgBase implements Serializable {
    private FaceDetect.FaceColorResult faceColorResult;
    private byte[] nv21;
    public FaceDetect.FaceColorResult getFaceColorResult() {
        return faceColorResult;
    }

    public void setFaceColorResult(FaceDetect.FaceColorResult faceColorResult) {
        this.faceColorResult = faceColorResult;
    }

    public byte[] getNv21() {
        return nv21;
    }

    public void setNv21(byte[] nv21) {
        this.nv21 = nv21;
    }
}
