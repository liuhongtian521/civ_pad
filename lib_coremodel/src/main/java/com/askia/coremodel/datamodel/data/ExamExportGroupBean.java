package com.askia.coremodel.datamodel.data;

import com.askia.coremodel.datamodel.database.db.DBExamExport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ymy
 * Description：
 * Date:2023/2/16 11:18
 */
public class ExamExportGroupBean implements Serializable {
    private String roomNo;//考场编号
    private List<DBExamExport> exportList; //当前考场下的验证信息

    private int total; //总数
    private int notValidation; //未验证
    private int passValidation;//通过验证
    private int doubtValidation;//存疑
    private int notPassValidation;//未通过

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    public int getNotValidation() {
        return notValidation;
    }

    public void setNotValidation(int notValidation) {
        this.notValidation = notValidation;
    }

    public int getPassValidation() {
        return passValidation;
    }

    public void setPassValidation(int passValidation) {
        this.passValidation = passValidation;
    }

    public int getDoubtValidation() {
        return doubtValidation;
    }

    public void setDoubtValidation(int doubtValidation) {
        this.doubtValidation = doubtValidation;
    }

    public int getNotPassValidation() {
        return notPassValidation;
    }

    public void setNotPassValidation(int notPassValidation) {
        this.notPassValidation = notPassValidation;
    }

    public List<DBExamExport> getExportList() {
        return exportList;
    }

    public void setExportList(List<DBExamExport> exportList) {
        this.exportList = exportList;
    }

    public ExamExportGroupBean(String key, List<DBExamExport> item) {
        this.roomNo = key;
        this.exportList = item;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
}
