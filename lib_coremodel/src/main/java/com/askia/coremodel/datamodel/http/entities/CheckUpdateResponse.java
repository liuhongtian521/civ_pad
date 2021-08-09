package com.askia.coremodel.datamodel.http.entities;

/**
 * 升级检测
 */
public class CheckUpdateResponse extends BaseResponseData {

    private String result;
    private Long timestamp;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
