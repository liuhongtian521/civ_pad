package com.askia.coremodel.datamodel.database.db;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DBLogs extends RealmObject implements Serializable {
    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationInstruction() {
        return operationInstruction;
    }

    public void setOperationInstruction(String operationInstruction) {
        this.operationInstruction = operationInstruction;
    }

    @PrimaryKey
    private String operationTime; //时间
    private String operationInstruction; //操作记录

}
