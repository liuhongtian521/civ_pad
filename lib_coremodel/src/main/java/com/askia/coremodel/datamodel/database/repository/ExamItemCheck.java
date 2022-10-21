package com.askia.coremodel.datamodel.database.repository;

import java.io.Serializable;

/**
 * Created by ymy
 * Description：
 * Date:2022/10/20 18:07
 */
public class ExamItemCheck implements Serializable {


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked = true;

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    private String roomNo;
}
