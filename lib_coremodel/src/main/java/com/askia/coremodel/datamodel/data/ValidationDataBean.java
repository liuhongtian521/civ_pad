package com.askia.coremodel.datamodel.data;

import java.io.Serializable;

/**
 * Created by ymy
 * Description：
 * Date:2023/2/16 15:22
 */
public class ValidationDataBean implements Serializable {
    private int total; //总数

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNotValidation() {
        return notValidation;
    }

    public void setNotValidation(int notValidation) {
        this.notValidation = notValidation;
    }

    public int getPassValidation() {
        return passValidation;
    }

    public void setPassValidation(int passValidation) {
        this.passValidation = passValidation;
    }

    public int getDoubtValidation() {
        return doubtValidation;
    }

    public void setDoubtValidation(int doubtValidation) {
        this.doubtValidation = doubtValidation;
    }

    public int getNotPassValidation() {
        return notPassValidation;
    }

    public void setNotPassValidation(int notPassValidation) {
        this.notPassValidation = notPassValidation;
    }

    private int notValidation; //未验证
    private int passValidation;//通过验证
    private int doubtValidation;//存疑
    private int notPassValidation;//未通过
}
