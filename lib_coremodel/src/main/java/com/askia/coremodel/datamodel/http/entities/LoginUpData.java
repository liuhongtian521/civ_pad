package com.askia.coremodel.datamodel.http.entities;

import com.askia.coremodel.datamodel.http.params.BaseRequestParams;

import java.io.Serializable;

/**
 * Create bt she:
 *
 * @date 2021/8/20
 */
public class LoginUpData extends BaseRequestParams {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
