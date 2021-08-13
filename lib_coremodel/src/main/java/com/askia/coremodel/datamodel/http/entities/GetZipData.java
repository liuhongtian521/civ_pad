package com.askia.coremodel.datamodel.http.entities;

import java.io.Serializable;

/**
 * Create bt she:
 * 压缩包下载
 * @date 2021/8/12
 */
public class GetZipData implements Serializable {

    private String fileUrl;//code或者下载地址
    private int result;//返回状态 0打包中 code    1 打包完成 下载地址
    private String msg;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
