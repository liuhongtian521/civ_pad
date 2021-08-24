package com.askia.coremodel.datamodel.http.entities;

import java.io.Serializable;

/**
 * Create bt she:
 * 通用 实体类
 * 一个 int 可以做标志位或者进度数据
 * 文本描述作用
 *
 * @date 2021/8/19
 */
public class DounloadZipData implements Serializable {
    private int errorCode;
    private String msg;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
