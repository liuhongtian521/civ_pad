package com.askia.coremodel.datamodel.database.db;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Create bt she:
 * 数据导出
 * @date 2021/8/9
 */
public class DBExamExport extends RealmObject implements Serializable {
    @PrimaryKey
    private String id;
    private String stu_no;//考生号
    private String examinee_id;//考生id
    private String verify_pack_id;//验证包id 不传
    private String verify_time;//验证时间
    private String verify_result;//验证结果 0未验证 1成功 2失败（存疑） 3未验证
    private String match_rate;//匹配率
    private String se_code;//场次码
    private String entrance_photo_url;//入场照片地址 不传
    private String exam_code;//考试代码
    private String create_by;//创建人
    private String create_time;//
    private String updata_up;//
    private String updata_time;//
    private String sys_org_code;//组织机构代码
    private String equipment;//设备id mac 地址

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStu_no() {
        return stu_no;
    }

    public void setStu_no(String stu_no) {
        this.stu_no = stu_no;
    }

    public String getExaminee_id() {
        return examinee_id;
    }

    public void setExaminee_id(String examinee_id) {
        this.examinee_id = examinee_id;
    }

    public String getVerify_pack_id() {
        return verify_pack_id;
    }

    public void setVerify_pack_id(String verify_pack_id) {
        this.verify_pack_id = verify_pack_id;
    }

    public String getVerify_time() {
        return verify_time;
    }

    public void setVerify_time(String verify_time) {
        this.verify_time = verify_time;
    }

    public String getVerify_result() {
        return verify_result;
    }

    public void setVerify_result(String verify_result) {
        this.verify_result = verify_result;
    }

    public String getMatch_rate() {
        return match_rate;
    }

    public void setMatch_rate(String match_rate) {
        this.match_rate = match_rate;
    }

    public String getSe_code() {
        return se_code;
    }

    public void setSe_code(String se_code) {
        this.se_code = se_code;
    }

    public String getEntrance_photo_url() {
        return entrance_photo_url;
    }

    public void setEntrance_photo_url(String entrance_photo_url) {
        this.entrance_photo_url = entrance_photo_url;
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

    public String getUpdata_up() {
        return updata_up;
    }

    public void setUpdata_up(String updata_up) {
        this.updata_up = updata_up;
    }

    public String getUpdata_time() {
        return updata_time;
    }

    public void setUpdata_time(String updata_time) {
        this.updata_time = updata_time;
    }

    public String getSys_org_code() {
        return sys_org_code;
    }

    public void setSys_org_code(String sys_org_code) {
        this.sys_org_code = sys_org_code;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
}
