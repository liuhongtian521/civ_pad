package com.askia.coremodel.viewmodel;

import android.util.Log;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.database.db.DBDataVersion;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamInfo;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.event.ZipHandleEvent;
import com.askia.coremodel.rtc.FileUtil;
import com.askia.coremodel.util.JsonUtil;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.unicom.facedetect.detect.FaceDetectManager;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.progress.ProgressMonitor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static com.askia.coremodel.rtc.Constants.ZIP_PATH;
import static com.askia.coremodel.rtc.Constants.UN_ZIP_PATH;


/**
 * 数据导入viewModel
 */
public class DataImportViewModel extends BaseViewModel {

    public ObservableField<Boolean> netImport = new ObservableField<>(false);
    public ObservableField<Boolean> usbImport = new ObservableField<>(false);
    public ObservableField<Boolean> sdCardImport = new ObservableField<>(true);

    private MutableLiveData<String> dataObservable = new MutableLiveData<>();
    //usb
    private MutableLiveData<String> usbImportObservable = new MutableLiveData<>();
    //zip
    private MutableLiveData<ZipHandleEvent> zipObservable = new MutableLiveData<>();

    private String pwd = "123456";

    public MutableLiveData<String> getSdCardData() {
        return dataObservable;
    }

    /**
     * 校验sdcard根目录下是否存在examination文件
     * 并解压.zip文件到Examination/data下
     */
    public void checkZipFile() {
        if (FileUtils.isFileExists(ZIP_PATH)) {
            //文件夹存在，获取zip list
            List<File> list = FileUtils.listFilesInDir(ZIP_PATH);
            if (list != null && list.size() > 0) {
                try {
                    for (int i = 0; i < list.size(); i++) {
                        //压缩包路径
                        String path = ZIP_PATH + File.separator + list.get(i).getName();
                        //文件名
                        String fileName = list.get(i).getName().split("\\.")[0];
                        //解压存放路径
                        String toPath = UN_ZIP_PATH + File.separator + fileName;
                        // 生成的压缩文件
                        ZipFile zipFile = new ZipFile(path);
                        // 设置密码
                        zipFile.setPassword(pwd.toCharArray());
                        //解压进度
                        final ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
                        int finalI = i;
                        Thread thread = new Thread(() -> {
                            int percentDone = 0;
                            while (true) {
                                percentDone = progressMonitor.getPercentDone();
                                dataObservable.postValue(percentDone + "");
                                if (percentDone >= 100) {
                                    //解析
                                    LogUtils.e("unzip success ->", fileName);
                                    getExDataFromLocal(toPath);
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
            }else {
                dataObservable.postValue("当前文件夹内没有压缩包，请检查存放位置！");
            }
        } else {
            dataObservable.postValue("请检查压缩包存放地址是否正确！");
        }
    }

    /**
     * 解析解压json
     *
     * @param path 解压文件路径
     */
    private void getExDataFromLocal(String path) {
        List<File> list = FileUtils.listFilesInDir(path);
        for (int i = 0; i < list.size(); i++) {
            //是否是json文件
            if (list.get(i).isFile()) {
                try {
                    LogUtils.e("json file name ->", list.get(i).getName());
                    insert2db(path + File.separator + list.get(i).getName(), list.get(i).getName());
                }catch (Exception e){
                    Log.e("TagSnake 01", Log.getStackTraceString(e));
                }

            } else {
                //人脸照片
                try {
                    pushFaceImage(path + File.separator + "photo");
                }catch (Exception e){
                    Log.e("TagSnake 02", Log.getStackTraceString(e));
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
                List<DBDataVersion> versionBean = JsonUtil.file2JsonArray(filePath,DBDataVersion.class);
                Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.copyToRealmOrUpdate(versionBean));
                break;
            default:
                break;
        }
    }

    private void pushFaceImage(String filePath) {
        Observable.create((ObservableOnSubscribe<String>) emitter ->
        {
            //文件夹相片数量
            List<File> photoList = FileUtils.listFilesInDir(filePath);
            LogUtils.e("photo list size->", photoList.size());
            if (photoList.isEmpty()) return;

            for (File file : photoList) {
                String faceNumber = file.getName().split("\\.")[0];
                LogUtils.e("photo name->", faceNumber);
                try {

                byte[] bytes = FileUtil.readFile(file);
                String faceId = FaceDetectManager.getInstance().addFace(faceNumber, faceNumber, bytes);
                emitter.onNext(faceId);
                }catch (Exception e){
                    Log.e("TagSnake 03", Log.getStackTraceString(e));
                }

            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(String result) {
                        LogUtils.e("faceid ->", result);
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

    public void doSdCardImport() {
        checkZipFile();
    }

}
