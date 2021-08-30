package com.askia.coremodel.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.http.ResponseCode;
import com.askia.coremodel.datamodel.http.download.StorageUtil;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.CheckVersionData;
import com.askia.coremodel.datamodel.http.entities.DounloadZipData;
import com.askia.coremodel.datamodel.http.entities.GetZipData;
import com.askia.coremodel.datamodel.http.entities.LoginData;
import com.askia.coremodel.datamodel.http.entities.SelectpalnbysitecodeData;
import com.askia.coremodel.datamodel.http.repository.NetDataRepository;
import com.askia.coremodel.event.FaceHandleEvent;
import com.askia.coremodel.rtc.Constants;
import com.askia.coremodel.rtc.FileUtil;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.ttsea.jrxbus2.RxBus2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Create bt she:
 *
 * @date 2021/8/17
 */
public class ZIPDownloadViewModel extends BaseViewModel {
    private MutableLiveData<GetZipData> zipDownload = new MutableLiveData<>();

    private MutableLiveData<SelectpalnbysitecodeData> selectExma = new MutableLiveData<>();

    private MutableLiveData<String> canGetNext = new MutableLiveData<>();

    private MutableLiveData<DounloadZipData> dounloadZipData = new MutableLiveData<>();

    public MutableLiveData<DounloadZipData> getDounloadZipData() {
        return dounloadZipData;
    }

    public MutableLiveData<String> getCanGetNext() {
        return canGetNext;
    }

    public MutableLiveData<SelectpalnbysitecodeData> getSelectExma() {
        return selectExma;
    }

    public MutableLiveData<GetZipData> getZipDownload() {
        return zipDownload;
    }

    public void writeTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            canGetNext.postValue("0");
        }

    }


    public void getExam(String siteCode) {
        Log.e("TagSnake up", siteCode);
        NetDataRepository.selectpalnbysitecode(siteCode).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<SelectpalnbysitecodeData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(SelectpalnbysitecodeData data) {
                        Log.e("TagSnake back", data.getMessage());
                        selectExma.postValue(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TagSnake back", Log.getStackTraceString(e));

                        SelectpalnbysitecodeData rulesData = new SelectpalnbysitecodeData();
                        if (e instanceof SocketTimeoutException) {
                            rulesData.setCode(ResponseCode.ConnectTimeOut);
                            rulesData.setMessage("连接超时，请重试");
                        } else {
                            rulesData.setCode(ResponseCode.ServerNotResponding);
                            rulesData.setMessage("服务器无响应，请重试");
                        }
                        selectExma.postValue(rulesData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void getDownloadOne(String examCode, String siteCode, String dataVersion) {
        Log.e("TagSnake getzip", examCode + ":" + siteCode + ":" + dataVersion);
        NetDataRepository.getpackurl(examCode, siteCode, dataVersion).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<GetZipData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(GetZipData data) {
                        Log.e("TagSnake zip", data.getResult().toString());
                        data.getResult().setExamCode(examCode);
                        zipDownload.postValue(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TagSnake", Log.getStackTraceString(e));
                        zipDownload.postValue(null);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public void downloadZip(GetZipData.ResultBean resultBean) {
        if (FileUtils.isFileExists(Constants.ZIP_PATH + File.separator + resultBean.getFilename())) {
            File file = FileUtils.getFileByPath(Constants.ZIP_PATH + File.separator + resultBean.getFilename());
            if (file != null)
                StorageUtil.deleteFile(file);
        }

        FileDownloader.getImpl().create(resultBean.getMinioUrl() + resultBean.getBucketName() + File.separator + resultBean.getFileUrl() + resultBean.getFilename())
                .setPath(Constants.ZIP_PATH + File.separator + resultBean.getFilename())
                .setForceReDownload(true)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d("TagSnake", "下载任务正在等待：" + soFarBytes + "/" + totalBytes);

                    }
                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d("TagSnake", "下载任务正在下载：" + soFarBytes + "/" + totalBytes);
                        FaceHandleEvent event = new FaceHandleEvent();
                        event.setType(2);
                        event.setTotal(totalBytes / (1024));
                        event.setCurrent(soFarBytes / (1024));
                        RxBus2.getInstance().post(event);
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        LogUtils.d("下载任务完成");
                        Log.d("TagSnake", "zip下载完成 ： " + task.getUrl());
                        DounloadZipData data = new DounloadZipData();
                        data.setErrorCode(0);
                        data.setMsg("下载完成");
                        dounloadZipData.postValue(data);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d("TagSnake", "下载任务已经暂停：" + soFarBytes + "/" + totalBytes);
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.d("TagSnake", "下载任务出错" + e.getCause());
                        DounloadZipData result = new DounloadZipData();
                        result.setErrorCode(-1);
                        result.setMsg("人脸库下载出错 " + e.getCause());
                        dounloadZipData.postValue(result);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();
    }


}
