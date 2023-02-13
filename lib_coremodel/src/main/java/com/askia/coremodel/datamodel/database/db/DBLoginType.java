package com.askia.coremodel.datamodel.database.db;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by ymy
 * Description：
 * Date:2023/2/6 17:02
 */
public class DBLoginType extends RealmObject implements Serializable {
    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    private String loginType; //登录类型 0管理员账号 1 DBAccount表中内置账号 2网络登录 3缓存登录
}
