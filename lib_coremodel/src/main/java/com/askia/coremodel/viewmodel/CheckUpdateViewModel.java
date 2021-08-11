package com.askia.coremodel.viewmodel;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.face.entities.FaceServerResult;
import com.askia.coremodel.datamodel.http.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.CheckVersionData;
import com.askia.coremodel.datamodel.http.entities.QueryFaceZipsUrlsData;
import com.askia.coremodel.datamodel.http.repository.NetDataRepository;
import com.askia.coremodel.event.FaceHandleEvent;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.ttsea.jrxbus2.RxBus2;

import java.io.File;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.askia.coremodel.rtc.Constants.apkPath;


/**
 * Create bt she:
 *
 * @date 2021/8/9
 */
public class CheckUpdateViewModel extends BaseViewModel {
    private MutableLiveData<CheckVersionData> mCheckVersionData = new MutableLiveData<>();

    public MutableLiveData<CheckVersionData> getmCheckVersionData() {
        return mCheckVersionData;
    }

    private MutableLiveData<String> dataObservable = new MutableLiveData<>();

    public MutableLiveData<String> getSdCardData() {
        return dataObservable;
    }

    public void checkVersion(String version) {
        NetDataRepository.checkVersion(version).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<CheckVersionData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(CheckVersionData data) {
                        mCheckVersionData.postValue(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TagSnake", Log.getStackTraceString(e));
                        CheckVersionData rulesData = new CheckVersionData();
                        if (e instanceof SocketTimeoutException) {
                            rulesData.setCode(ResponseCode.ConnectTimeOut);
                            rulesData.setMessage("连接超时，请重试");
                        } else {
                            rulesData.setCode(ResponseCode.ServerNotResponding);
                            rulesData.setMessage("服务器无响应，请重试");
                        }
                        mCheckVersionData.postValue(rulesData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public void DownApk(String path) {
        File file = new File(apkPath);

        if (!file.exists()) {
            file.mkdir();
        }
        if (!file.isDirectory()) {
            file.delete();
            file.mkdir();
        }
        FileDownloader.getImpl().create(path)
                .setPath(apkPath + "/download.apk")
                .setForceReDownload(true)
                .setListener(new FileDownloadListener() {
                    //等待
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d("TagSnake", "下载任务正在等待：" + soFarBytes + "/" + totalBytes);
                    }

                    //下载进度回调
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d("TagSnake", "下载任务正在下载：" + soFarBytes + "/" + totalBytes);
//                        FaceHandleEvent event = new FaceHandleEvent();
//                        event.setType(2);
//                        event.setTotal(totalBytes / (1024));
//                        event.setCurrent(soFarBytes / (1024));
//                        RxBus2.getInstance().post(event);
                    }

                    //完成下载
                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.d("TagSnake", "zip下载完成 ： " + task.getUrl());
                        dataObservable.postValue("success");
                    }

                    //暂停
                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d("TagSnake", "下载任务已经暂停：" + soFarBytes + "/" + totalBytes);
                    }

                    //下载出错
                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.d("TagSnake", "下载任务出错" + e.getCause());
//                        FaceServerResult result = new FaceServerResult();
//                        result.setErrorCode(-1);
//                        result.setMsg("人脸库下载出错 " + e.getCause());
                    }

                    //已存在相同下载
                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();
    }

}
