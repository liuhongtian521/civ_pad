package com.askia.coremodel.datamodel.database.repository;

import android.app.Application;

public class DBRepository {

    public static void init(Application context) {
        SharedPreUtil.initSharedPreference(context);
    }
/*    //查询用户登陆信息
    public static HttpLoginBean QueryUserLoginData() {
        HttpLoginBean userLoginData = SharedPreUtil.getInstance().getUser();
        return userLoginData;
    }

    //保存用户登陆信息
    public static void StoreUserLoginData(HttpLoginBean userLoginData) {
        SharedPreUtil.getInstance().putUserInfo(userLoginData);
    }*/
}
