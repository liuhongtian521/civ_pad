package com.askia.coremodel.datamodel.data;

import com.github.mjdev.libaums.fs.UsbFile;

import java.io.Serializable;

public class DataImportBean implements Serializable {

    public int getImportType() {
        return importType;
    }

    public void setImportType(int importType) {
        this.importType = importType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private int importType;
    private String filePath;
    private String fileName;

    public UsbFile getUsbFile() {
        return usbFile;
    }

    public void setUsbFile(UsbFile usbFile) {
        this.usbFile = usbFile;
    }

    private UsbFile usbFile;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    private boolean isChecked =false;
}
