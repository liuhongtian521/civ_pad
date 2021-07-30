package com.askia.coremodel.datamodel.realm.bean;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmModule;


public class TransFilesRecord extends RealmObject
{
    @PrimaryKey
    private int id;
    private String filePath;
    private boolean isRead;

    public String getFilePath() {
        return filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
