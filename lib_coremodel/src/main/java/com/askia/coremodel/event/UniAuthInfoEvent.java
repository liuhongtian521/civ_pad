package com.askia.coremodel.event;

import java.io.Serializable;

/**
 * Created by ymy
 * Description：
 * Date:2022/6/29 16:24
 */
public class UniAuthInfoEvent implements Serializable {
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    private String userName;
    private String passWord;
}
