package com.askia.coremodel.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.database.repository.SharedPreUtil;
import com.askia.coremodel.datamodel.face.FaceEngineManager;
import com.askia.coremodel.datamodel.face.FaceServer;
import com.askia.coremodel.datamodel.face.entities.FaceEngineResult;
import com.askia.coremodel.datamodel.face.entities.FaceServerResult;
import com.askia.coremodel.datamodel.http.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.QueryFaceZipsUrlsData;
import com.askia.coremodel.datamodel.http.repository.NetDataRepository;
import com.askia.coremodel.event.FaceHandleEvent;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.ttsea.jrxbus2.RxBus2;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.net.SocketTimeoutException;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends BaseViewModel {
    private MutableLiveData<FaceEngineResult> mActiveEngineLiveData = new MutableLiveData<>();
    private MutableLiveData<FaceEngineResult> mInitEngineLiveData = new MutableLiveData<>();
    private MutableLiveData<FaceEngineResult> mGetActiveFileLiveData = new MutableLiveData<>();

    public MutableLiveData<FaceEngineResult> getActiveEngineLiveData() {
        return mActiveEngineLiveData;
    }

    public MutableLiveData<FaceEngineResult> getInitEngineLiveData() {
        return mInitEngineLiveData;
    }

    public MutableLiveData<FaceEngineResult> getGetActiveFileLiveData() {
        return mGetActiveFileLiveData;
    }


    public void initEngine(Context context) {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            int activeCode = FaceEngineManager.initEngine(context);
            emitter.onNext(activeCode);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        FaceEngineResult result = new FaceEngineResult();
                        result.setAfCode(activeCode);
                        mInitEngineLiveData.postValue(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        FaceEngineResult result = new FaceEngineResult();
                        result.setAfCode(-99);
                        mInitEngineLiveData.postValue(result);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void activeEngine(Context context, String appid, String sdkkey) {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            int activeCode = FaceEngineManager.activeEngine(context, appid, sdkkey);
            emitter.onNext(activeCode);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        FaceEngineResult result = new FaceEngineResult();
                        result.setAfCode(activeCode);
                        mActiveEngineLiveData.postValue(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        FaceEngineResult result = new FaceEngineResult();
                        result.setAfCode(-99);
                        mActiveEngineLiveData.postValue(result);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void checkActiveStatus(Context context) {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            int activeCode = FaceEngineManager.getActiveFileInfo(context);
            emitter.onNext(activeCode);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        FaceEngineResult result = new FaceEngineResult();
                        result.setAfCode(activeCode);
                        mGetActiveFileLiveData.postValue(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        FaceEngineResult result = new FaceEngineResult();
                        result.setAfCode(-99);
                        mGetActiveFileLiveData.postValue(result);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
