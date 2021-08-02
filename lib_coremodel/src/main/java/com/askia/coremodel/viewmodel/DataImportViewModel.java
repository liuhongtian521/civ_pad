package com.askia.coremodel.viewmodel;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.File;
import java.util.List;


/**
 * 数据导入viewModel
 */
public class DataImportViewModel extends BaseViewModel{

    public ObservableField<Boolean> netImport = new ObservableField<>(true);
    public ObservableField<Boolean> usbImport = new ObservableField<>(false);
    public ObservableField<Boolean> sdCardImport = new ObservableField<>(false);
    private MutableLiveData<String> sdCardLiveData = new MutableLiveData<>();

    public MutableLiveData<String> getSdCardData(){
        return sdCardLiveData;
    }

    private String dataPath = "/mnt/sdcard/examination";


    /**
     * 校验sdcard根目录下是否存在examination文件
     */
    public void checkZipFile(){
        if (FileUtils.isFileExists(dataPath)){
            //文件夹存在
            sdCardLiveData.postValue("文件夹存在");
        }else {
            sdCardLiveData.postValue("文件夹不存在");
        }
    }

    public void doSdCardImport(){
        checkZipFile();
    }

}
