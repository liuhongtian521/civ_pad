package com.askia.coremodel.viewmodel;

import android.os.Environment;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.progress.ProgressMonitor;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * 数据导入viewModel
 */
public class DataImportViewModel extends BaseViewModel {

    public ObservableField<Boolean> netImport = new ObservableField<>(false);
    public ObservableField<Boolean> usbImport = new ObservableField<>(false);
    public ObservableField<Boolean> sdCardImport = new ObservableField<>(true);
    private MutableLiveData<String> sdCardLiveData = new MutableLiveData<>();
    private String pwd = "123456";

    public MutableLiveData<String> getSdCardData() {
        return sdCardLiveData;
    }

    private final String dataPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "Examination";
    private final String unZipPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "ExModel";


    /**
     * 校验sdcard根目录下是否存在examination文件
     * 并解压.zip文件到Examination/data下
     */
    public void checkZipFile() {
        if (FileUtils.isFileExists(dataPath)) {
            //文件夹存在，获取zip list
            List<File> list = FileUtils.listFilesInDir(dataPath);
            if (list != null && list.size() > 0) {
                try {
                    for (int i = 0; i < list.size(); i++) {
                        //压缩包路径
                        String path = dataPath + File.separator + list.get(i).getName();
                        //解压存放路径
                        String toPath = unZipPath + File.separator + list.get(i).getName().split("\\.")[0];
                        // 生成的压缩文件
                        ZipFile zipFile = new ZipFile(path);
                        // 设置密码
                        zipFile.setPassword(pwd.toCharArray());
                        //解压进度
                        final ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
                        Thread thread = new Thread(() -> {
                            int percentDone = 0;
                            while (true){
                                percentDone = progressMonitor.getPercentDone();
                                sdCardLiveData.postValue(percentDone + "");
                                LogUtils.e("percentDone ->", percentDone + "");
                                if (percentDone >= 100){
                                    break;
                                }
                            }
                        });
                        thread.start();
                        // 解压缩所有文件以及文件夹
                        zipFile.extractAll(toPath);
                    }
                } catch (IOException e) {
                    LogUtils.e("unzip exception->", e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            sdCardLiveData.postValue("请检查压缩包存放地址是否正确！");
        }
    }

    /**
     * 读取本地json
     */
    private void getExDataFromLocal(){

    }

    public void doSdCardImport() {
        checkZipFile();
    }

}
