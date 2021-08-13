package com.askia.coremodel.viewmodel;

import android.util.JsonWriter;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.util.JsonUtil;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.askia.coremodel.rtc.Constants.STU_EXPORT;

/**
 * 数据导出
 */
public class DataExportViewModel extends BaseViewModel {
    private String pwd = "123456";

    MutableLiveData<String> exportObservable = new MutableLiveData<>();
    private String exportPath = "";
    private final String fileName = "ea_verify_detail.json";

    public MutableLiveData<String> doExport() {
        return exportObservable;
    }

    public void doDataExport(String seCode) {
        //是否有导出数据
        List<DBExamExport> list = DBOperation.getExportBySeCode(seCode);
        if (list.isEmpty()) {
            exportObservable.postValue("暂无验证数据！");
            return;
        }

        //判断导出文件夹是否存在，不存在或文件数量为0则没有验证数据
        if (!FileUtils.isFileExists(STU_EXPORT) || FileUtils.listFilesInDir(STU_EXPORT).isEmpty()) {
            exportObservable.postValue("暂无验证数据！");
        } else {
            //拼接数据导出文件夹路径 STU_EXPORT + /seCode
            exportPath = STU_EXPORT + File.separator + seCode + File.separator + fileName;
            //文件存在先删除再创建
            FileUtils.createFileByDeleteOldFile(exportPath);
            //写入数据
            saveData2Local(list,exportPath);
        }
    }

    /**
     * 数据写入
     * @param exports 数据
     * @param localPath 写入路径
     */
    private void saveData2Local(List<DBExamExport> exports,String localPath){
        try {
            //list 2 jsonString
            String data = JsonUtil.JsonList2Str(exports);
            LogUtils.e("export data->", data);
            OutputStream fileOutputStream = new FileOutputStream(localPath);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("data write exception ->",e.getMessage());
        }
    }
}
