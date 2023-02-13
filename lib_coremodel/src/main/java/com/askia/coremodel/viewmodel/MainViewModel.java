package com.askia.coremodel.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
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
import com.blankj.utilcode.util.TimeUtils;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private MutableLiveData<DBExamPlan> mDbExamPlan = new MutableLiveData<>();//考试
    private MutableLiveData<DBExamLayout> mDBExamLayout = new MutableLiveData<>();//考试数据
    private MutableLiveData<DBExamArrange> mDBexamArrange = new MutableLiveData<>();//场次信息

    public MutableLiveData<DBExamLayout> getmDBExamLayout() {
        return mDBExamLayout;
    }

    public MutableLiveData<DBExamArrange> getmDBexamArrange() {
        return mDBexamArrange;
    }

    public MutableLiveData<DBExamPlan> getmDbExamPlan() {
        return mDbExamPlan;
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void getExamCode(String examCode) {
        if (examCode != null) {
            DBExamPlan plan = DBOperation.getExamPlan(examCode);
            mDbExamPlan.postValue(plan);
        } else {
            List<DBExamPlan> planList = DBOperation.getExamPlan();
            if (planList.size() > 0)
                mDbExamPlan.postValue(planList.get(0));
            else
                mDbExamPlan.postValue(null);
        }
    }

    public void getSiteCode(String examCode) {
//        examCode = "GK2030";
        DBExamPlan dbExamPlan = DBOperation.getExamPlan(examCode);
        List<DBExamArrange> list = DBOperation.getExamArrange(examCode);
        long timeNow = System.currentTimeMillis();
        DBExamArrange back = null;
        for (DBExamArrange item : list) {
            Log.e("TagSnake", item.toString());
            long start = TimeUtils.string2Millis(item.getStartTime()) - Long.parseLong(dbExamPlan.getVerifyStartTime() == null ? "0" : dbExamPlan.getVerifyStartTime()) * 60 * 1000;
            //fixed 修正验证逻辑，去掉验证结束时间
            long end = TimeUtils.string2Millis(item.getEndTime() == null ? "0" : item.getEndTime());
            Log.e("TagSnake", "timeNow:" + timeNow + ":start:" + start + ":end:" + end);
            if (start < timeNow && end > timeNow) {
                back = item;
                break;
            }
            if (start > timeNow) {
                if (back == null) {
                    back = item;
                } else if (TimeUtils.string2Millis(back.getStartTime()) > TimeUtils.string2Millis(item.getStartTime())) {
                    back = item;
                }
            }
        }
        mDBexamArrange.postValue(back);
    }

    public void getExamLayout(String examCode) {
        List<DBExamLayout> layList = DBOperation.getDBExamLayout(examCode);
        if (layList == null || layList.size() == 0)
            mDBExamLayout.postValue(null);
        else
            mDBExamLayout.postValue(layList.get(0));
    }


}
