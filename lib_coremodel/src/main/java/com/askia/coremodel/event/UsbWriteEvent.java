package com.askia.coremodel.event;

import java.io.Serializable;

public class UsbWriteEvent implements Serializable {

    private int code;

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

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    private String message;
    private boolean result;
}
