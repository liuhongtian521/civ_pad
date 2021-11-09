package com.askia.coremodel.viewmodel;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.datamodel.http.entities.UpLoadResult;
import com.askia.coremodel.datamodel.http.repository.NetDataRepository;
import com.askia.coremodel.datamodel.http.requestBody.ProgressListener;
import com.askia.coremodel.datamodel.http.requestBody.UploadRequestBody;
import com.askia.coremodel.event.UnZipHandleEvent;
import com.askia.coremodel.event.Write2OGTEvent;
import com.askia.coremodel.event.ZipUploadEvent;
import com.askia.coremodel.util.IOUtil;
import com.askia.coremodel.util.JsonUtil;
import com.askia.coremodel.util.NetUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.askia.coremodel.rtc.Constants.STU_EXPORT;

/**
 * 数据导出
 */
public class DataExportViewModel extends BaseViewModel {
    private String pwd = "Ut9RKOo8d4NCrnll";

    //导出到SDCard
    MutableLiveData<String> exportObservable = new MutableLiveData<>();
    //usb 写入
    MutableLiveData<Write2OGTEvent> usbWriteObservable = new MutableLiveData<>();
    //压缩
    MutableLiveData<UnZipHandleEvent> unZipObservable = new MutableLiveData<>();
    //导出数据上传
    MutableLiveData<UpLoadResult> upLoadObservable = new MutableLiveData<>();

    private String exportPath = "";
    private final String fileName = "ea_verify_detail.json";

    public MutableLiveData<String> doExport() {
        return exportObservable;
    }

    public MutableLiveData<Write2OGTEvent> write2UDisk() {
        return usbWriteObservable;
    }

    public MutableLiveData<UnZipHandleEvent> zip(){
        return unZipObservable;
    }

    public MutableLiveData<UpLoadResult> upLoad() {
        return upLoadObservable;
    }


    public void doDataExport(String seCode) {
//        DBExamExport examExport = new DBExamExport();
//        examExport.setCreateBy("1");
//        examExport.setExamCode("222");
//        examExport.setSeCode(seCode);
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
            boolean isExit = FileUtils.createFileByDeleteOldFile(exportPath);
            //遍历 历史导出文件压缩包
            List<File> fileList = FileUtils.listFilesInDir(STU_EXPORT);
            for (File file: fileList){
                if (file.getName().contains(".zip")){
                    FileUtils.deleteFile(file);
                }
            }
            List<DBExamExport> tempList = new ArrayList<>();
            //copy value to fields
            tempList.addAll(Realm.getDefaultInstance().copyFromRealm(list));
            if (isExit){
                //写入数据
                saveData2Local(tempList, exportPath, seCode);
            }
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
        String zipFilePath = zipPath_ + "/" + seCode + "_" +macId+ ".zip";
        String zipName = seCode + "_" +macId+ ".zip";
        // 生成的压缩文件
        ZipFile zipFile = new ZipFile(zipFilePath);

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
                zipHandleEvent.setUnZipProcess(percentDone);
                zipHandleEvent.setCode(1);
                zipHandleEvent.setFileName(zipName);
                zipHandleEvent.setFilePath(zipFilePath);
                zipHandleEvent.setMessage("正在压缩...");
                unZipObservable.postValue(zipHandleEvent);
                if (percentDone >= 100) {
                    //解析
                    zipHandleEvent.setUnZipProcess(100);
                    zipHandleEvent.setCode(0);
                    zipHandleEvent.setFileName(zipName);
                    zipHandleEvent.setFilePath(zipFilePath);
                    zipHandleEvent.setMessage("压缩完成");
                    unZipObservable.postValue(zipHandleEvent);
                    break;
                }
            }
        }).start();

        if (filePath_.isDirectory()) {
            try {
                zipFile.setRunInThread(true);
                zipFile.addFolder(filePath_, parameters);
            } catch (ZipException e) {
                e.printStackTrace();
            }
        }
    }

    //sdcard 写入到U盘
    public void write2UDiskByOTG(File sourceFile, UsbFile file) {
        if (file != null) {
            Write2OGTEvent event = new Write2OGTEvent();
            Observable.create((ObservableOnSubscribe<Write2OGTEvent>) emitter -> {
                IOUtil.saveSDFile2OTG(sourceFile, file, new IOUtil.IOProcess() {
                    @Override
                    public void onProcessDoneListener(long current, long total) {
                        LogUtils.e("current buffer length ->", current,total);
                        event.setCurrent(current);
                        event.setTotal(total);
                        if (current < total) {
                            event.setMessage("正在写入到U盘中...");
                            event.setCode(1);
                            emitter.onNext(event);
                        } else {
                            event.setMessage("导出完成");
                            event.setCode(0);
                            emitter.onNext(event);
                        }
                    }
                });
                emitter.onNext(null);
            }).subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Write2OGTEvent>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {
                            mDisposable.add(d);
                        }

                        @Override
                        public void onNext(@NotNull Write2OGTEvent s) {
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

    public void postData(String examCode,String siteCode,String seCode,String filePath){
        Map<String,String> map = new HashMap<>();
        map.put("examCode",examCode);
        map.put("siteCode",siteCode);
        map.put("seCode",seCode);
        map.put("equipment",DeviceUtils.getAndroidID());
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        File file = new File(filePath);
//        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        UpLoadResult event = new UpLoadResult();
        UploadRequestBody uploadRequestBody = new UploadRequestBody(file, (hasWrittenLen, totalLen) -> {
            LogUtils.e("upload progress total->", totalLen,"hasWritten->",hasWrittenLen);
            event.setCurrent(hasWrittenLen);
            event.setTotal(totalLen);
            if (hasWrittenLen < totalLen){
                event.setState(1);
                event.setInfo("正在上传...");
            }else {
                event.setState(0);
                event.setInfo("上传成功");
            }
            upLoadObservable.postValue(event);
        });

//        builder.addFormDataPart("file",file.getName(),body);
        MultipartBody.Part multipartBody =MultipartBody.Part.createFormData("file",file.getName(),uploadRequestBody);

        NetDataRepository.verifyfacecontrast(map,multipartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<UpLoadResult>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NotNull UpLoadResult upLoadResult) {
                        Log.e("TagSnake back",upLoadResult.getMessage());
                        upLoadObservable.postValue(upLoadResult);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.e("TagSnake err",Log.getStackTraceString(e));

                        UpLoadResult bean = new UpLoadResult();
                        bean.setMessage(e.getMessage());
                        bean.setSuccess(false);
                        upLoadObservable.postValue(bean);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
