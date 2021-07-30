package com.askia.coremodel.datamodel.database.db;

import java.io.Serializable;

/**
 * 用户登陆信息
 * HHY
 * ObservableField 目的为实现数据改变自动改变View
 */

public class UserLoginData implements Serializable {
    //设备mac
    private String devMac;
    //设备型号
    private String devModel;
    //是否显示欢迎页面
    private boolean showWelcomPage;
    //设备系统版本号
    private String SDKVersionName;
    //程序版本号
    private String versionNum;
    //设备是否 rooted
    private boolean isDeviceRooted;
    //设备厂商
    private String manufacturer;
    //用户名
    private String phoneNum = "";
    //密码
    private String userPwd = "";
    //身份类型
    private String type = "";
    //IMEI
    private String IMEINum = "";
    //教师姓名
    private String teacherName = "";
    //用户姓名
    private String userName = "";
    //教师电话
    private String teacherPhone = "";
    //班级ID
    private String classId = "";
    //接口服务器url
    private String apiUrl = "";
    //学校名称
    private String schoolName = "";
    //教室id
    private String classroomId = "";

    //班级名称
    private String className = "";

    // 宿舍id
    private String dormId = "";
    // 宿舍code
    private String dormCode = "";
    // 宿舍名称
    private String dormName = "";

    // 首页布局类型
    private int homeLayoutType = 1;

    public String getDormCode() {
        return dormCode;
    }

    public void setDormCode(String dormCode) {
        this.dormCode = dormCode;
    }

    public int getHomeLayoutType() {
        return homeLayoutType;
    }

    public void setHomeLayoutType(int homeLayoutType) {
        this.homeLayoutType = homeLayoutType;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getDevMac() {
        return devMac;
    }

    public void setDevMac(String devMac) {
        this.devMac = devMac;
    }

    public String getDevModel() {
        return devModel;
    }

    public void setDevModel(String devModel) {
        this.devModel = devModel;
    }

    public boolean isShowWelcomPage() {
        return showWelcomPage;
    }

    public void setShowWelcomPage(boolean showWelcomPage) {
        this.showWelcomPage = showWelcomPage;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public String getSDKVersionName() {
        return SDKVersionName;
    }

    public void setSDKVersionName(String SDKVersionName) {
        this.SDKVersionName = SDKVersionName;
    }

    public boolean isDeviceRooted() {
        return isDeviceRooted;
    }

    public void setDeviceRooted(boolean deviceRooted) {
        isDeviceRooted = deviceRooted;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIMEINum() {
        return IMEINum;
    }

    public void setIMEINum(String IMEINum) {
        this.IMEINum = IMEINum;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public void setTeacherPhone(String teacherPhone) {
        this.teacherPhone = teacherPhone;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDormId() {
        return dormId;
    }

    public void setDormId(String dormId) {
        this.dormId = dormId;
    }

    public String getDormName() {
        return dormName;
    }

    public void setDormName(String dormName) {
        this.dormName = dormName;
    }

    @Override
    public String toString() {
        return "UserLoginData{" +
                "devMac='" + devMac + '\'' +
                ", devModel='" + devModel + '\'' +
                ", showWelcomPage=" + showWelcomPage +
                ", SDKVersionName='" + SDKVersionName + '\'' +
                ", versionNum='" + versionNum + '\'' +
                ", isDeviceRooted=" + isDeviceRooted +
                ", manufacturer='" + manufacturer + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", type='" + type + '\'' +
                ", IMEINum='" + IMEINum + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", userName='" + userName + '\'' +
                ", teacherPhone='" + teacherPhone + '\'' +
                ", classId='" + classId + '\'' +
                ", apiUrl='" + apiUrl + '\'' +
                ", dormId" + dormId + '\'' +
                ", dormName" + dormName + '\'' +
                ", schoolName" + schoolName + '\'' +
                ", classroomId" + classroomId + '\'' +
                ", className" + className + '\'' +
                '}';
    }
}


