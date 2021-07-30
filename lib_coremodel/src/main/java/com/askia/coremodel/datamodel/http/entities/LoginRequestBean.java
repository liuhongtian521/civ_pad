package com.askia.coremodel.datamodel.http.entities;

import java.io.Serializable;

public class LoginRequestBean implements Serializable {
    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;
}
