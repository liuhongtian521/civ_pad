package com.askia.coremodel.viewmodel;


import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.unicom.facedetect.detect.FaceDetectManager;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;

import static com.askia.coremodel.rtc.Constants.STU_EXPORT;
import static com.askia.coremodel.rtc.Constants.UN_ZIP_PATH;
import static com.askia.coremodel.rtc.Constants.ZIP_PATH;

public class DataClearViewModel extends BaseViewModel {

    public MutableLiveData<String> dataClearObservable = new MutableLiveData<>();
    public MutableLiveData<String> dataVerifyObservable = new MutableLiveData<>();

    public MutableLiveData<String> delImportData() {
        return dataClearObservable;
    }

    public MutableLiveData<String> delVerifyData(){
        return dataVerifyObservable;
    }

    /**
     * 清空导入数据
     */
    public void delImport() {
        //导出
        if (!FileUtils.isFileExists(ZIP_PATH)){
            dataClearObservable.postValue("暂无导入数据");
            return;
        }

        if ((FileUtils.listFilesInDir(ZIP_PATH).size() == 0 || FileUtils.listFilesInDir(ZIP_PATH) == null)
                && (FileUtils.listFilesInDir(UN_ZIP_PATH).size() == 0 || FileUtils.listFilesInDir(UN_ZIP_PATH) == null)) {
            dataClearObservable.postValue("暂无导入数据");
            return;
        }


        //清空数据库
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            realm.delete(DBExamLayout.class);
            realm.delete(DBExamArrange.class);
            realm.delete(DBExaminee.class);
            realm.delete(DBExamPlan.class);
        }, () -> {
            dataClearObservable.postValue("数据库删除成功");
            Observable.create((ObservableOnSubscribe<String>) emitter -> {
                //清空.zip
                boolean zipClear = FileUtils.deleteAllInDir(ZIP_PATH);
                //清空解压文件
                boolean dirClear = FileUtils.deleteAllInDir(UN_ZIP_PATH);
                //人脸库是否有数据
                int totalFace = FaceDetectManager.getInstance().totalNumberOfFace();
                if (totalFace > 0) {
                    //清空人脸库
                    boolean result = FaceDetectManager.getInstance().clearAllFace();
                    LogUtils.e("face clear ->", result);
                    if (result & zipClear & dirClear) {
                        emitter.onNext("操作成功");
                    }
                } else {
                    if (zipClear && dirClear) {
                        emitter.onNext("操作成功");
                    }
                }

            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {
                            mDisposable.add(d);
                        }

                        @Override
                        public void onNext(@NotNull String s) {
                            dataClearObservable.postValue(s);
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {
                            dataClearObservable.postValue("操作异常，请稍后重试！");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }, error -> dataClearObservable.postValue("数据库删除异常"));
    }

    /**
     * 清空验证数据
     */
    public void delAuthData() {
        if (FileUtils.listFilesInDir(STU_EXPORT) == null || FileUtils.listFilesInDir(STU_EXPORT).size() == 0){
            dataVerifyObservable.postValue("暂无验证数据");
            return;
        }

        //清空数据库
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            //清空验证数据
            realm.delete(DBExamExport.class);
        }, () -> {
            dataVerifyObservable.postValue("验证数据库清理成功！");
            Observable.create((ObservableOnSubscribe<String>) emitter -> {
                //清空验证数据文件夹
                boolean result = FileUtils.deleteAllInDir(STU_EXPORT);
                if (result) {
                    emitter.onNext("操作成功！");
                }

            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(@NotNull Disposable d) {
                            mDisposable.add(d);
                        }

                        @Override
                        public void onNext(@NotNull String s) {
                            dataVerifyObservable.postValue(s);
                        }

                        @Override
                        public void onError(@NotNull Throwable e) {
                            dataVerifyObservable.postValue("操作异常，请稍后重试！");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }, error -> dataVerifyObservable.postValue("数据库删除异常"));
    }
}
