package com.askia.coremodel.datamodel.http.entities;

import java.io.Serializable;

public class BaseResponseData implements Serializable
{
    private int code;
    private String message;
    private boolean success;

    public boolean isError()
    {
        return !success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
