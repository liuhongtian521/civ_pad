package com.askia.coremodel.datamodel.http;

public class ResponseCode {

    //服务器无响应
    public static int ServerNotResponding = -100;
    //连接超时
    public static int ConnectTimeOut = -99;
    //已被其他设备登陆-后台尚未加入
    public static int OthersLogined = -98;
    //账号已锁定-后台尚未加入
    public static int AccountLocked = -97;

    //响应成功
    public static int ResponseSuccessCode = 200;
    //响应成功
    public static int ResponseRradingSuccessCode = 100001;

    //一般状态：启用
    public static int USING = 100000;

    //一般状态：锁定
    public static int LOCK = 100005;

    //一般状态：禁用
    public static int DISABLE = 100010;

    //一般状态：成功
    public static int SUCCESS = 100001;

    //一般状态：失败
    public static int FAILURE = 100002;


    //返回状态码：操作失败
    public static int OPERATION_FAILED = 900000;

    //返回状态码：token为空
    public static int TOKEN_IS_BLANK = 900001;

    //返回状态码：token无效
    public static int TOKEN_INVALID = 900002;

    //返回状态码：token过期
    public static int token_expire = 900003;

    //返回状态码：验证码为空
    public static int code_is_blank = 900004;

    //返回状态码：验证码错误
    public static int code_error = 900005;

    //异常状态码：ContentType错误:必须是application/json
    public static int HEADERS_ERROR = 900010;

    //异常状态码：空指针
    public static int NULL_POINTER = 900011;

    //异常状态码：数据库相关异常
    public static int database_exception = 900020;

    //异常状态码：json格式错误或转换异常
    public static int json_exception = 900021;

    //异常状态码：类相关异常
    public static int class_exception = 900022;

    //订单已消费
    public static int order_already_consumed = 800001;

    public static int shengji=-123;

}
