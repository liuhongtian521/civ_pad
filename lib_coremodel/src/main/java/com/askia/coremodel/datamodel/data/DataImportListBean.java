package com.askia.coremodel.datamodel.data;

import java.io.Serializable;

public class DataImportListBean implements Serializable {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private String title;
    private boolean isChecked;
}
