package com.askia.coremodel.event;

import java.io.Serializable;

/**
 * Created by ymy
 * Description：导出数据上传状态更新
 * Date:2022/11/21 17:17
 */
public class ExportDataEvent implements Serializable {
    private String seCode;//场次编码

    public String getSeCode() {
        return seCode;
    }

    public void setSeCode(String seCode) {
        this.seCode = seCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    private boolean isSuccess;//状态是否更新成功
}
