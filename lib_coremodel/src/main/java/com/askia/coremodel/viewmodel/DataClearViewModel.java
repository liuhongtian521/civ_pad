package com.askia.coremodel.viewmodel;


import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.FileUtils;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmAsyncTask;

import static com.askia.coremodel.rtc.Constants.UN_ZIP_PATH;
import static com.askia.coremodel.rtc.Constants.ZIP_PATH;

public class DataClearViewModel extends BaseViewModel {

    public MutableLiveData<String> dataClearObservable = new MutableLiveData<>();

    public MutableLiveData<String> delImportData() {
        return dataClearObservable;
    }

    /**
     * 清空导入数据
     */
    public void delImport() {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        //清空.zip
                        FileUtils.deleteAllInDir(ZIP_PATH);
                        //清空解压文件
                        FileUtils.deleteAllInDir(UN_ZIP_PATH);
                        //清空数据库
                        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.deleteAll(), () -> dataClearObservable.postValue("数据库删除成功"), error -> dataClearObservable.postValue("数据库删除异常"));
                    }

                    @Override
                    public void onNext(@NotNull String s) {
                        dataClearObservable.postValue("操作成功！");
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        dataClearObservable.postValue("操作异常，请稍后重试！");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
