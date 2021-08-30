package com.askia.coremodel.viewmodel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.datamodel.database.repository.DBRepository;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.CheckVersionData;
import com.askia.coremodel.datamodel.http.entities.UPMsgData;
import com.askia.coremodel.datamodel.http.entities.UpLoadResult;
import com.askia.coremodel.datamodel.http.repository.NetDataRepository;
import com.askia.coremodel.rtc.Constants;
import com.askia.coremodel.util.DeviceUtils;
import com.askia.coremodel.util.Utils;
import com.blankj.utilcode.util.TimeUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Create bt she:
 * 身份验证
 *
 * @date 2021/8/11
 */
public class AuthenticationViewModel extends BaseViewModel {
    private MutableLiveData<DBExaminee> mCheckVersionData = new MutableLiveData<>();
    private MutableLiveData<DBExamLayout> mSeat = new MutableLiveData<>();
    private MutableLiveData<DBExamLayout> mStudent = new MutableLiveData<>();
    private MutableLiveData<List<DBExamArrange>> mArrange = new MutableLiveData<>();
    private MutableLiveData<Integer> mLayoutSize = new MutableLiveData<>();
    private MutableLiveData<DBExamPlan> mDBExamPlan = new MutableLiveData<>();
    private MutableLiveData<DBExamArrange> mDBExamArrange = new MutableLiveData<>();

    private MutableLiveData<Integer> mCanSign = new MutableLiveData<>();

    private MutableLiveData<Integer> mDBExamExportNumber = new MutableLiveData<>();

    public MutableLiveData<Integer> getmDBExamExportNumber() {
        return mDBExamExportNumber;
    }

    public MutableLiveData<Integer> getmCanSign() {
        return mCanSign;
    }

    private MutableLiveData<DBExamExport> mDBExamExport = new MutableLiveData<>();

    public MutableLiveData<DBExamExport> getmDBExamExport() {
        return mDBExamExport;
    }

    public MutableLiveData<DBExamArrange> getmDBExamArrange() {
        return mDBExamArrange;
    }

    public MutableLiveData<DBExamPlan> getmDBExamPlan() {
        return mDBExamPlan;
    }

    public MutableLiveData<Integer> getmLayoutSize() {
        return mLayoutSize;
    }

    public MutableLiveData<List<DBExamArrange>> getmArrange() {
        return mArrange;
    }

    public MutableLiveData<DBExaminee> getmCheckVersionData() {
        return mCheckVersionData;
    }

    public MutableLiveData<DBExamLayout> getmStudent() {
        return mStudent;
    }

    public MutableLiveData<DBExamLayout> getmSeat() {
        return mSeat;
    }

    public void getSize(String seCode) {
        int size = DBOperation.getRoomList(seCode).size();
        mLayoutSize.postValue(size);
    }


    public void qucikArrange(String examCode) {
        List<DBExamArrange> list = DBOperation.getExamArrange(examCode);
        mArrange.postValue(list);
    }

    public void getPlanByCode(String examCode) {
        DBExamPlan dbExamPlan = DBOperation.getExamPlan(examCode);
        List<DBExamArrange> list = DBOperation.getExamArrange(examCode);
        long timeNow = System.currentTimeMillis();
        DBExamArrange back = null;
        for (DBExamArrange item : list) {
            long start = Long.valueOf(TimeUtils.string2Millis(item.getStartTime())) - (Long.valueOf(dbExamPlan.getVerifyStartTime() == null ? "0" : dbExamPlan.getVerifyStartTime()) * 60 * 1000);
            long end = Long.valueOf(TimeUtils.string2Millis(item.getStartTime())) + (Long.valueOf(dbExamPlan.getVerifyEndTime() == null ? "0" : dbExamPlan.getVerifyEndTime()) * 60 * 1000);
            if (start < timeNow && end > timeNow) {
                back = item;
                break;
            }
        }
        mDBExamArrange.postValue(back);
    }


    public void quickPeople(String name, String examCode) {
        List<DBExaminee> list = DBOperation.quickPeople(name, examCode);
        if (list == null || list.size() == 0) {
            Log.e("TagSnake", "list == null || list.size() == 0");
            mCheckVersionData.postValue(null);
        } else {
            Log.e("TagSnake", "list>0");
            mCheckVersionData.postValue(list.get(0));
        }
    }

    public void getSeatAbout(String stuNo, String examCode, String seCode) {
        DBExamLayout mDbExamLayout = DBOperation.getStudentInfoTwo(examCode, stuNo, seCode);
        mSeat.postValue(mDbExamLayout);
    }

    public void getStudent(int type, String msg, String examCode, String seCode) {
        if (type == 0) {
            DBExamLayout mDbExamLayout = DBOperation.getStudtByNum(msg, examCode, seCode);
            mStudent.postValue(mDbExamLayout);
        } else {
            DBExamLayout mDbExamLayout = DBOperation.getStudentByCode(msg, examCode, seCode);
            mStudent.postValue(mDbExamLayout);
        }
    }

    public void canSign(String id) {
        mCanSign.postValue(DBOperation.getDBExamExport(id));
    }

    public void getExamNumber(String seCode, String examCode) {
        mDBExamExportNumber.postValue(DBOperation.getDBExamExportNumber(seCode, examCode));
    }

    public void setMsg(DBExamLayout dbExamLayout, String time, String type, String number, String id, String cardNo) {
        Log.e("TagSnake", type + ":状态" + "::" + time);
        DBExamExport db = new DBExamExport();
        db.setId(dbExamLayout.getId());
        db.setStuNo(dbExamLayout.getStuNo());
        db.setStuName(dbExamLayout.getStuName());
        db.setExamineeId(id);
        db.setVerifyTime(time);
        db.setVerifyResult(type);
        db.setMatchRate(number.substring(0, 4));
        db.setSeCode(dbExamLayout.getSeCode());
        db.setEquipment(DeviceUtils.getDeviceSN());
        db.setExamCode(dbExamLayout.getExamCode());
        db.setSysOrgCode(dbExamLayout.getSysOrgCode());
        db.setSiteCode(dbExamLayout.getSiteCode());
        db.setIdCard(cardNo);
        DBOperation.setDBExamExport(db);
        mDBExamExport.postValue(db);
        upMsg(db);

    }

    private void upMsg(DBExamExport dbExamExport) {
        Log.e("TagSnake up", "upMsg");

        UPMsgData db = new UPMsgData();
        db.setId(dbExamExport.getId());
        db.setStuNo(dbExamExport.getStuNo());
        db.setStuName(dbExamExport.getStuName());
        db.setExamineeId(dbExamExport.getExamineeId());
        Date date = new Date(Long.valueOf(dbExamExport.getVerifyTime()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.format(date);
        db.setVerifyTime(sdf.format(date));
        db.setVerifyResult(dbExamExport.getVerifyResult());
        db.setMatchRate(dbExamExport.getMatchRate());
        db.setSeCode(dbExamExport.getSeCode());
        db.setEquipment(DeviceUtils.getDeviceSN());
        db.setExamCode(dbExamExport.getExamCode());
        db.setSysOrgCode(dbExamExport.getSysOrgCode());
        db.setSiteCode(dbExamExport.getSiteCode());
        db.setIdCard(dbExamExport.getIdCard());
//        db.setEntrancePhotoUrl();
        String pathT = Constants.STU_EXPORT + File.separator + db.getSeCode() + File.separator + "photo" + File.separator + db.getStuNo() + ".jpg";
        try {
            db.setEntrancePhotoUrl(Utils.encodeBase64File(pathT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("TagSnake up", db.toString());
        NetDataRepository.uploadverifydetail(convertPostBody(db))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<BaseResponseData>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NotNull BaseResponseData baseResponseData) {
                        Log.e("TagSnake back", baseResponseData.getMessage());
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.e("TagSnake err", Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
