package com.askia.coremodel.viewmodel;


import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.FileUtils;

import java.io.File;
import java.util.List;

import static com.askia.coremodel.rtc.Constants.UN_ZIP_PATH;

public class InitializeViewModel extends BaseViewModel{

    //进度
    public ObservableField<String> observableProgress = new ObservableField<>();

    private MutableLiveData<String> checkData = new MutableLiveData<>();

    private MutableLiveData<String> getLocalData(){
        return checkData;
    }

    public boolean hasExaData(){
        //解压文件夹是否存在
        if (FileUtils.isFileExists(UN_ZIP_PATH)){
            List<File> list = FileUtils.listFilesInDir(UN_ZIP_PATH);
            return !list.isEmpty();
        }else {
            //无数据
            return false;
        }
    }

}
