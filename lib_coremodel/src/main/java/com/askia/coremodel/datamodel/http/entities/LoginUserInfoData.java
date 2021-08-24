package com.askia.coremodel.datamodel.http.entities;

import java.io.Serializable;

/**
 * Create bt she:
 *
 * @date 2021/8/10
 */
public class LoginUserInfoData implements Serializable {

    private String id;
    private String username;
    private String realname;
    private String avatar;
    private String birthday;
    private int sex;
    private String phone;
    private String orgCode;
    private String post;
    private String workNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    @Override
    public String toString() {
        return "LoginUserInfoData{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", realname='" + realname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birthday='" + birthday + '\'' +
                ", sex=" + sex +
                ", phone='" + phone + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", post='" + post + '\'' +
                ", workNo='" + workNo + '\'' +
                '}';
    }
}
