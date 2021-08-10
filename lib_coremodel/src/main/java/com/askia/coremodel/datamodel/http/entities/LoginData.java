package com.askia.coremodel.datamodel.http.entities;

/**
 * Create bt she:
 *
 * @date 2021/8/10
 */
public class LoginData extends BaseResponseData {

    private  LoginResultData result;

    public LoginResultData getResult() {
        return result;
    }

    public void setResult(LoginResultData result) {
        this.result = result;
    }
}
