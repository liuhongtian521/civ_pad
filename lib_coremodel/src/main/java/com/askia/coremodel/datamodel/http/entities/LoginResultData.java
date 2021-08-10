package com.askia.coremodel.datamodel.http.entities;

import java.io.Serializable;

/**
 * Create bt she:
 *
 * @date 2021/8/10
 */
public class LoginResultData implements Serializable {
    private LoginUserInfoData userInfo;

    private String token;

    public LoginUserInfoData getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(LoginUserInfoData userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
