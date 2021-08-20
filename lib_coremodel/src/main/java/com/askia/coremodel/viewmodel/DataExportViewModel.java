package com.askia.coremodel.viewmodel;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.event.UnZipHandleEvent;
import com.askia.coremodel.util.IOUtil;
import com.askia.coremodel.util.JsonUtil;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.github.mjdev.libaums.fs.UsbFile;
import com.google.gson.Gson;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import net.lingala.zip4j.progress.ProgressMonitor;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static com.askia.coremodel.rtc.Constants.STU_EXPORT;

/**
 * 数据导出
 */
public class DataExportViewModel extends BaseViewModel {
    private String pwd = "123456";

    //导出到SDCard
    MutableLiveData<String> exportObservable = new MutableLiveData<>();
    //usb 写入
    MutableLiveData<String> usbWriteObservable = new MutableLiveData<>();
    //压缩
    MutableLiveData<UnZipHandleEvent> unZipObservable = new MutableLiveData<>();

    private String exportPath = "";
    private final String fileName = "ea_verify_detail.json";

    public MutableLiveData<String> doExport() {
        return exportObservable;
    }

    public MutableLiveData<String> write2UDisk() {
        return usbWriteObservable;
    }

    public MutableLiveData<UnZipHandleEvent> zip(){
        return unZipObservable;
    }


    public void doDataExport(String seCode) {
//        DBExamExport examExport = new DBExamExport();
//        examExport.setCreateBy("1");
//        examExport.setExamCode("222");
//        examExport.setSeCode("123");
//        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.copyToRealmOrUpdate(examExport);
//            }
//        });

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
            List<DBExamExport> tempList = new ArrayList<>();
            //copy value to fields
            tempList.addAll(Realm.getDefaultInstance().copyFromRealm(list));
            //写入数据
            saveData2Local(tempList, exportPath, seCode);
        }
    }

    /**
     * 数据写入
     *
     * @param exports   数据
     * @param localPath 写入路径
     */
    private void saveData2Local(List<DBExamExport> exports, String localPath, String seCode) {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            //list 2 jsonString
            String data = new Gson().toJson(exports);
            OutputStream fileOutputStream = new FileOutputStream(localPath);
            fileOutputStream.write(data.getBytes());
            fileOutputStream.close();
            String zipPath = STU_EXPORT;
            String filePath = STU_EXPORT + File.separator + seCode;
            compress2zip(zipPath, filePath, seCode);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NotNull String s) {
//                        exportObservable.postValue(s);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        LogUtils.e("compress error ->", e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 压缩文件
     *
     * @param zipPath  压缩文件路径
     * @param filePath 文件路径
     * @param seCode   场次号
     */
    private void compress2zip(String zipPath, String filePath, String seCode) {
        File zipPath_ = new File(zipPath);
        File filePath_ = new File(filePath);
        String macId = DeviceUtils.getAndroidID();
        // 生成的压缩文件
        ZipFile zipFile = new ZipFile(zipPath_ + "/" + seCode + "_" +macId+ ".zip");

        ZipParameters parameters = new ZipParameters();

        // 压缩方式
        parameters.setCompressionMethod(CompressionMethod.DEFLATE);

        // 压缩级别
        parameters.setCompressionLevel(CompressionLevel.NORMAL);

        // 是否设置加密文件
        parameters.setEncryptFiles(true);

        // 设置加密算法
        parameters.setEncryptionMethod(EncryptionMethod.AES);

        // 设置AES加密密钥的密钥强度
        parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);

        // 设置密码
        if (!TextUtils.isEmpty(pwd)) {
            zipFile.setPassword(pwd.toCharArray());
        }

        final ProgressMonitor monitor = zipFile.getProgressMonitor();
        new Thread(() -> {
            int percentDone = 0;
            UnZipHandleEvent zipHandleEvent = new UnZipHandleEvent();
            while (true) {
                percentDone = monitor.getPercentDone();
                LogUtils.e("zip success ->", percentDone + "");
                zipHandleEvent.setUnZipProcess(percentDone);
                zipHandleEvent.setMessage("正在压缩");
                unZipObservable.postValue(zipHandleEvent);
                if (percentDone >= 100) {
                    //解析
                    zipHandleEvent.setUnZipProcess(100);
                    zipHandleEvent.setCode(0);
                    zipHandleEvent.setMessage("压缩完成");
                    unZipObservable.postValue(zipHandleEvent);
                    break;
                }
            }
        }).start();

        if (filePath_.isDirectory()) {
            try {
                zipFile.addFolder(filePath_, parameters);
            } catch (ZipException e) {
                e.printStackTrace();
            }
        }
    }

    public void write2UDiskByOTG(File sourceFile, UsbFile file) {
        if (file != null) {
            Observable.create((ObservableOnSubscribe<String>) emitter -> {
                IOUtil.saveSDFile2OTG(sourceFile, file);
                emitter.onNext("已导入到U盘根目录下的ExamExport下");
            }).subscribeOn(Schedulers.io())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {
                            mDisposable.add(d);
                        }

                        @Override
                        public void onNext(@NotNull String s) {
                            usbWriteObservable.postValue(s);
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
