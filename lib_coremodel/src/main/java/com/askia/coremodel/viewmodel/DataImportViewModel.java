package com.askia.coremodel.viewmodel;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.data.DataImportBean;
import com.askia.coremodel.datamodel.data.DataImportListBean;
import com.askia.coremodel.datamodel.database.db.DBDataVersion;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.event.FaceDBHandleEvent;
import com.askia.coremodel.event.UnZipHandleEvent;
import com.askia.coremodel.event.UsbWriteEvent;
import com.askia.coremodel.rtc.FileUtil;
import com.askia.coremodel.util.JsonUtil;
import com.blankj.utilcode.util.CloseUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileInputStream;
import com.unicom.facedetect.detect.FaceDetectManager;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static com.askia.coremodel.rtc.Constants.UN_ZIP_PATH;
import static com.askia.coremodel.rtc.Constants.ZIP_PATH;


/**
 * 数据导入viewModel
 */
public class DataImportViewModel extends BaseViewModel {

    //usb
    private MutableLiveData<UsbWriteEvent> usbImportObservable = new MutableLiveData<>();
    //unzip
    private MutableLiveData<UnZipHandleEvent> unZipObservable = new MutableLiveData<>();
    //face db insert
    private MutableLiveData<FaceDBHandleEvent> faceDbObservable = new MutableLiveData<>();

    private String pwd = "123456";

    public MutableLiveData<UnZipHandleEvent> doZipHandle() {
        return unZipObservable;
    }

    public MutableLiveData<FaceDBHandleEvent> doFaceDBHandle() {
        return faceDbObservable;
    }

    public MutableLiveData<UsbWriteEvent> usbWriteObservable() {
        return usbImportObservable;
    }


    /**
     * 解析解压json
     *
     * @param path 解压文件路径
     */
    public void getExDataFromLocal(String path) {
        List<File> list = FileUtils.listFilesInDir(path);
        if (list != null && list.size() > 0) {
            //空
            for (int i = 0; i < list.size(); i++) {
                //是否是json文件
                if (list.get(i).isFile()) {
                    try {
                        insert2db(path + File.separator + list.get(i).getName(), list.get(i).getName());
                    } catch (Exception e) {
                        Log.e("TagSnake 01", Log.getStackTraceString(e));
                    }

                } else {
                    //人脸照片
                    try {
                        pushFaceImage(path + File.separator + "photo");
                    } catch (Exception e) {
                        Log.e("TagSnake 02", Log.getStackTraceString(e));
                    }
                }
            }
        }
    }


    /**
     * 考试数据插入
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     */
    private void insert2db(String filePath, String fileName) {

        switch (fileName) {
            //考试计划表
            case "ea_exam_plan.json":
                List<DBExamPlan> planBean = JsonUtil.file2JsonArray(filePath, DBExamPlan.class);
                Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.copyToRealmOrUpdate(planBean));
                break;
            //考试安排表
            case "ea_exam_arrange.json":
                List<DBExamArrange> arrangeBean = JsonUtil.file2JsonArray(filePath, DBExamArrange.class);
                Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.copyToRealmOrUpdate(arrangeBean));
                break;
            //考试编排表
            case "ea_exam_layout.json":
                List<DBExamLayout> layoutBean = JsonUtil.file2JsonArray(filePath, DBExamLayout.class);
                Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.copyToRealmOrUpdate(layoutBean));
                break;
            //考生信息表
            case "ea_examinee.json":
                List<DBExaminee> amineeBean = JsonUtil.file2JsonArray(filePath, DBExaminee.class);
                Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.copyToRealmOrUpdate(amineeBean));
                break;
            //版本号
            case "ea_data_version.json":
                List<DBDataVersion> versionBean = JsonUtil.file2JsonArray(filePath, DBDataVersion.class);
                Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.copyToRealmOrUpdate(versionBean));
                break;
            default:
                break;
        }
    }

    private void pushFaceImage(String filePath) {
        Observable.create((ObservableOnSubscribe<FaceDBHandleEvent>) emitter ->
        {
            //文件夹相片数量
            List<File> photoList = FileUtils.listFilesInDir(filePath);
            LogUtils.e("photo list size->", photoList.size());
            if (photoList.isEmpty())
                return;
            FaceDBHandleEvent event = new FaceDBHandleEvent();
            int current = 1;
            for (File file : photoList) {
                String faceNumber = file.getName().split("\\.")[0];
                try {
                    byte[] bytes = FileUtil.getBytesByFile(file.getPath());
                    //根据faceNumber获取人脸库中是否有此信息
//                    boolean isHave = FaceDetectManager.getInstance().fetchByFaceNumber(faceNumber);
                    event.setTotal(photoList.size());
                    event.setCurrent(current);
                    //如果人脸库中没有此人则把照片插入人脸库
//                    if (!isHave) {
                    String faceId = FaceDetectManager.getInstance().addFace(faceNumber, faceNumber, bytes);
                    //照片总数
                    event.setFaceId(faceId);
                    if (current == photoList.size()) {
                        //人脸库插入完成
                        event.setState(1);
                    } else {
                        event.setState(0);
                    }
                    current++;

                    emitter.onNext(event);
                } catch (Exception e) {
                    Log.e("TagSnake 03", Log.getStackTraceString(e));
                }

            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FaceDBHandleEvent>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(FaceDBHandleEvent result) {
                        if (result.getState() == 1) {
                            removeZipFile();
                        }
                        faceDbObservable.postValue(result);
                        LogUtils.e("faceid ->", result.getFaceId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void readZipFromUDisk(UsbFile usbFile) {
        Observable.create((ObservableOnSubscribe<UsbWriteEvent>) emitter -> {
//            UsbFile descFile = usbFile;
            InputStream is = null;
            UsbWriteEvent event = new UsbWriteEvent();

            //如果多个压缩包 进行批量复制到sdcard
//                for (UsbFile file : descFile.listFiles()) {
            is = new UsbFileInputStream(usbFile);
            String path = ZIP_PATH + File.separator + usbFile.getName();
            OutputStream os = null;
            File targetFile = new File(path);
            File folder = new File(ZIP_PATH);
            boolean hasFolder = FileUtils.createOrExistsDir(ZIP_PATH);
            if (hasFolder) {
                try {
                    os = new BufferedOutputStream(new FileOutputStream(targetFile));
                    //获取文件大小
                    long fileLength = usbFile.getLength();
                    long current = 0l;

                    byte data[] = new byte[8192];
                    int len;
                    while ((len = is.read(data, 0, 8192)) != -1) {
                        os.write(data, 0, len);
                        //文件写入进度
                        current = current + len;
                        LogUtils.e("file current->", current);
                        event.setCurrent(current);
                        event.setTotal(fileLength);
                        Thread.sleep(100);
                        if (current < fileLength) {
                            event.setMessage("正在复制数据包...");
                            event.setCode(1);
                            emitter.onNext(event);
                        } else {
                            event.setMessage("复制完成");
                            event.setCode(0);
                            event.setZipPath(path);
                            event.setFileName(usbFile.getName());
                            emitter.onNext(event);
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    CloseUtils.closeIO(is, os);
                }
            }


//                }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UsbWriteEvent>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NotNull UsbWriteEvent usbWriteEvent) {
                        usbImportObservable.postValue(usbWriteEvent);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        UsbWriteEvent event = new UsbWriteEvent();
                        event.setCode(1);
                        event.setMessage(e.getMessage());
                        usbImportObservable.postValue(event);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 校验sdcard根目录下是否存在examination文件
     * 并解压.zip文件到Examination/data下
     */
    public void doUnzip(LifecycleOwner owner, DataImportBean bean) {
        UnZipHandleEvent unZipHandleEvent = new UnZipHandleEvent();
        if (FileUtils.isFileExists(ZIP_PATH)) {
            //文件夹存在，获取zip list
            List<File> list = FileUtils.listFilesInDir(ZIP_PATH);
            if (list != null && list.size() > 0) {
                try {
//                    for (int i = 0; i < list.size(); i++) {
                        //压缩包路径
                        String path = ZIP_PATH + File.separator + bean.getFileName();
                        //文件名
                        String fileName = bean.getFileName().split("\\.")[0];
                        //解压存放路径
                        String toPath = UN_ZIP_PATH + File.separator + fileName;
                        // 生成的压缩文件
                        ZipFile zipFile = new ZipFile(path);
                        // 设置密码
                        zipFile.setPassword(pwd.toCharArray());
                        //解压进度
                        ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
                        Thread thread = new Thread(() -> {
                            int percentDone = 0;
                            LogUtils.e("life owner ->", "thread created");
                            while (true) {
                                percentDone = progressMonitor.getPercentDone();
                                unZipHandleEvent.setUnZipProcess(percentDone);
                                unZipHandleEvent.setMessage("正在解压中...");
                                unZipObservable.postValue(unZipHandleEvent);
                                if (percentDone >= 100) {
                                    unZipHandleEvent.setCode(0);
                                    unZipHandleEvent.setUnZipProcess(percentDone);
                                    unZipHandleEvent.setFilePath(toPath);
                                    unZipHandleEvent.setZipPath(path);
                                    unZipHandleEvent.setMessage("解压完成");
                                    unZipObservable.postValue(unZipHandleEvent);
                                    break;
                                }
                            }
                        });
                        thread.start();
                        owner.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
                            if (event == Lifecycle.Event.ON_DESTROY) {
                                thread.interrupt();
                                progressMonitor.setCancelAllTasks(true);
                            }
                        });
                        // 解压缩所有文件以及文件夹
                        try {
                            zipFile.setRunInThread(true);
                            zipFile.extractAll(toPath);
                        } catch (ZipException e) {
                            e.printStackTrace();
                        }
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                unZipHandleEvent.setCode(-1);
                unZipHandleEvent.setUnZipProcess(-1);
                unZipHandleEvent.setMessage("当前文件夹内没有压缩包，请检查存放位置！");
                unZipObservable.postValue(unZipHandleEvent);
            }
        } else {
            unZipHandleEvent.setCode(-1);
            unZipHandleEvent.setUnZipProcess(-1);
            unZipHandleEvent.setMessage("请检查压缩包存放地址是否正确");
            unZipObservable.postValue(unZipHandleEvent);
        }
    }

    public void removeZipFile() {
        File folder = new File(ZIP_PATH);
        File[] files = folder.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                //获取.zip文件
                if (file.getName().toUpperCase().contains(".ZIP")) {
                    FileUtils.deleteFile(file);
                }
            }
        }
    }

    /**
     * 获取USB 路径下的数据包
     *
     * @param usbFile u盘文件夹路径
     */
    public List<DataImportBean> fetchDataFromUsb(UsbFile usbFile) {
        List<DataImportBean> list = new ArrayList<>();
        if (usbFile == null) return list;
        DataImportBean bean;

        try {
            for (UsbFile file : usbFile.listFiles()) {
                bean = new DataImportBean();
                bean.setFileName(file.getName());
                bean.setImportType(1);
                bean.setUsbFile(file);
                list.add(bean);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<DataImportBean> fetchDataFromSdCard(){
        List<DataImportBean> list = new ArrayList<>();
        DataImportBean bean;

        File folder = new File(ZIP_PATH);
        boolean hasFolder = FileUtils.createOrExistsDir(ZIP_PATH);

        if (hasFolder){
            for (File file : folder.listFiles()){
                String zipPath = ZIP_PATH + File.separator + file.getName();
                bean = new DataImportBean();
                bean.setFileName(file.getName());
                bean.setFilePath(zipPath);
                list.add(bean);
            }
        }
        return list;
    }
}
