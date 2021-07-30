package com.askia.coremodel.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.database.repository.SharedPreUtil;

import com.askia.coremodel.datamodel.face.FaceServer;
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


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class SettingViewModel extends BaseViewModel {
    private MutableLiveData<FaceServerResult> mFaceRegisterLiveData = new MutableLiveData<>();
    private MutableLiveData<List<File>> mFaceFileListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<File>> getFaceFileListLiveData() {
        return mFaceFileListLiveData;
    }

    // 维护一个下载地址的栈
    private Stack<QueryFaceZipsUrlsData.ResultBean.ImageListBean> mZipUrlList = new Stack<>();
    // 当前zip包的时间戳
    private String mCurrentZipTimestamp;

    public MutableLiveData<FaceServerResult> getFaceRegisterLiveData() {
        return mFaceRegisterLiveData;
    }



    /**
     * 分页查询本地的人脸库人脸文件
     *
     * @param context
     * @param pageNum
     * @param pageSize
     */
    public void queryLocalFaceFiles(Context context, int pageNum, int pageSize) {
        Observable.create((ObservableOnSubscribe<List<File>>) emitter ->
        {
            emitter.onNext(FaceServer.getInstance().getLocalFaceFiles(context, pageSize, pageNum));

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<File>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<File> result) {
                        mFaceFileListLiveData.postValue(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showLong("获取本地人脸图片失败：" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    // 获取远程人脸库下载地址  如果时间戳为null 则是全量更新 如果有值就是增量更新
    public void queryFacesUrls(String timeStamp, Context context, boolean cleanOld) {
        Log.e("lncucc", timeStamp + ":");
        NetDataRepository.queryFaceZipsUrls(timeStamp).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<QueryFaceZipsUrlsData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(QueryFaceZipsUrlsData data) {
                        Log.e("lncucc", data.getMessage());
                        if (data == null || data.isError()) {
                            FaceServerResult result = new FaceServerResult();
                            result.setErrorCode(-1);
                            result.setMsg("获取人脸库下载地址失败"+data.getMessage());
                            mFaceRegisterLiveData.postValue(result);
                            return;
                        }
                        if (data.getResult() == null || data.getResult().getImageList() == null || data.getResult().getImageList().size() == 0) {
                            FaceServerResult result = new FaceServerResult();
                            result.setErrorCode(-1);
                            result.setMsg("没有需要更新的人脸库了");
                            mFaceRegisterLiveData.postValue(result);
                            return;
                        }
                        Log.e("lncucc", data.getResult().getImageList() + "");
                        for (QueryFaceZipsUrlsData.ResultBean.ImageListBean _d : data.getResult().getImageList()) {
                            Log.e("lncucc", "人脸zip下载地址 ： " + _d.getUrl());
                            mZipUrlList.push(_d);
                        }
                        QueryFaceZipsUrlsData.ResultBean.ImageListBean zipUrlData = mZipUrlList.pop();
                        mCurrentZipTimestamp = zipUrlData.getTimeStamp();
                        downloadRemoteFaces(context, zipUrlData.getUrl(), cleanOld);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("TagSaneke",e.toString()+"");
                        FaceServerResult rulesData = new FaceServerResult();
                        if (e instanceof SocketTimeoutException) {
                            rulesData.setErrorCode(ResponseCode.ConnectTimeOut);
                            rulesData.setMsg("连接超时，请重试");
                        } else {
                            rulesData.setErrorCode(ResponseCode.ServerNotResponding);
                            rulesData.setMsg("服务器无响应，请重试");
                        }
                        mFaceRegisterLiveData.postValue(rulesData);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    // 本地人脸库导入
    public void localRegister(Context context, boolean cleanOld) {
        Observable.create((ObservableOnSubscribe<FaceServerResult>) emitter ->
        {
            // 清空人脸库
            int faceNum = FaceServer.getInstance().getFaceNumber(context);
            if (faceNum > 0 && cleanOld) {
                FaceServer.getInstance().clearAllFaces(context);
            }

            FaceServer.getInstance().registerMultyLocalFaces(context);
            FaceServerResult result = new FaceServerResult();
            result.setErrorCode(0);
            emitter.onNext(result);

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FaceServerResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(FaceServerResult result) {
                        mFaceRegisterLiveData.postValue(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        FaceServerResult result = new FaceServerResult();
                        result.setErrorCode(-1);
                        result.setMsg("人脸库导入出错：" + e.getMessage());
                        mFaceRegisterLiveData.postValue(result);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    // 下载指定路径的zip包
    public void downloadRemoteFaces(Context context, String url, boolean cleanOld) {

        File file = new File(FaceServer.READ_FACE_DIR);
        if (!file.exists()) {
            file.mkdir();
        }
        if (!file.isDirectory()) {
            file.delete();
            file.mkdir();
        }

        Log.e("lncucc", url);
        FileDownloader.getImpl().create(url)
                .setPath(FaceServer.READ_FACE_DIR + "/" + System.currentTimeMillis() + ".zip")
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
                        FaceHandleEvent event = new FaceHandleEvent();
                        event.setType(2);
                        event.setTotal(totalBytes / (1024));
                        event.setCurrent(soFarBytes / (1024));
                        RxBus2.getInstance().post(event);
                    }

                    //完成下载
                    @Override
                    protected void completed(BaseDownloadTask task) {
                        LogUtils.d("下载任务完成");
                        Log.d("TagSnake", "zip下载完成 ： " + task.getUrl());

                        remoteFacesUnzipImport(context, task.getPath(), cleanOld);
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
                        FaceServerResult result = new FaceServerResult();
                        result.setErrorCode(-1);
                        result.setMsg("人脸库下载出错 " + e.getCause());

                        mFaceRegisterLiveData.postValue(result);

                    }

                    //已存在相同下载
                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();
    }

    // 解压下载的zip包
    public void remoteFacesUnzipImport(Context context, String zipPath, boolean cleanOld) {
        Observable.create((ObservableOnSubscribe<FaceServerResult>) emitter ->
        {
            FaceHandleEvent event = new FaceHandleEvent();
            event.setType(3);
            RxBus2.getInstance().post(event);
//            ZipUtils.unzipFile(zipPath, FaceServer.READ_FACE_DIR);
            ZipFile zf = new ZipFile(zipPath, "GBK");
            Enumeration e = zf.getEntries();
            BufferedInputStream bi;
            while (e.hasMoreElements()) {
                ZipEntry ze2 = (ZipEntry) e.nextElement();
                String entryName = ze2.getName();
//                Log.v("lncucc", entryName);
                String path = FaceServer.READ_FACE_DIR + "/" + entryName;
                if (ze2.isDirectory()) {
                    File decompressDirFile = new File(path);
                    if (!decompressDirFile.exists()) {
                        decompressDirFile.mkdirs();
                    }
                } else {
                    String fileDir = path.substring(0, path.lastIndexOf("/"));
                    File fileDirFile = new File(fileDir);
                    if (!fileDirFile.exists()) {
                        fileDirFile.mkdirs();
                    }
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(FaceServer.READ_FACE_DIR + "/" + entryName));
                    bi = new BufferedInputStream(zf.getInputStream(ze2));
                    byte[] readContent = new byte[1024];
                    int readCount = bi.read(readContent);
                    while (readCount != -1) {
                        bos.write(readContent, 0, readCount);
                        readCount = bi.read(readContent);
                    }
                    bos.close();
                }
            }
            zf.close();
            Log.d("lncucc", "zip解压完成 ： " + zipPath);
            FaceServerResult result = new FaceServerResult();
            result.setErrorCode(0);
            emitter.onNext(result);

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FaceServerResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(FaceServerResult result) {
                        // 保存最后更新的时间戳
                        String oldZipTimestamp = SharedPreUtil.getInstance().getZipTimestamp();

                        if (TextUtils.isEmpty(oldZipTimestamp)) {
                            SharedPreUtil.getInstance().putZipTimestamp(mCurrentZipTimestamp);
                        }
                        if (!TextUtils.isEmpty(oldZipTimestamp) && mCurrentZipTimestamp.compareTo(oldZipTimestamp) > 0) {
                            Log.e("TagSnake zipsave",mCurrentZipTimestamp);
                            SharedPreUtil.getInstance().putZipTimestamp(mCurrentZipTimestamp);
                        }
                        if (mZipUrlList.empty()) {
                            // 删除zip包
                            File dir = new File(FaceServer.READ_FACE_DIR);
                            final File[] jpgFiles = dir.listFiles(new FilenameFilter() {
                                @Override
                                public boolean accept(File dir, String name) {
                                    return name.endsWith(".zip");
                                }
                            });
                            for (File zipFile : jpgFiles) {
                                zipFile.delete();
                            }
                            // 所有的zip包都下载解压完成 调用导入本地人脸库
                            FaceServerResult serverResult = new FaceServerResult();
                            localRegister(context, cleanOld);
                        } else {
                            QueryFaceZipsUrlsData.ResultBean.ImageListBean data = mZipUrlList.pop();
                            mCurrentZipTimestamp = data.getTimeStamp();
                            downloadRemoteFaces(context, data.getUrl(), cleanOld);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("lncucc", e.getMessage()+""+e.toString());
                        FaceServerResult result = new FaceServerResult();
                        result.setErrorCode(-1);
                        result.setMsg("人脸库导入出错：" + e.getMessage());
                        mFaceRegisterLiveData.postValue(result);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
