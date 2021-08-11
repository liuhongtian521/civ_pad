package com.askia.coremodel.datamodel.database.db;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 数据包版本
 */
public class DBDataVersion extends RealmObject implements Serializable {

    @PrimaryKey
    private String id;
    private int data_version; //版本号
    private String exam_code;// 考试代码
    private String create_by;
    private String create_time;
    private String update_by;
    private String update_time;
    private String sys_org_code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getData_version() {
        return data_version;
    }

    public void setData_version(int data_version) {
        this.data_version = data_version;
    }

    public String getExam_code() {
        return exam_code;
    }

    public void setExam_code(String exam_code) {
        this.exam_code = exam_code;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getSys_org_code() {
        return sys_org_code;
    }

    public void setSys_org_code(String sys_org_code) {
        this.sys_org_code = sys_org_code;
    }

}
