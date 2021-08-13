package com.askia.coremodel.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.datamodel.database.repository.DBRepository;
import com.askia.coremodel.datamodel.http.entities.CheckVersionData;
import com.askia.coremodel.util.DeviceUtils;

import java.util.List;

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

    public MutableLiveData<DBExaminee> getmCheckVersionData() {
        return mCheckVersionData;
    }

    public MutableLiveData<DBExamLayout> getmSeat() {
        return mSeat;
    }

    public void quickPeople(String name, String examCode) {

        List<DBExaminee> list = DBOperation.quickPeople(name, examCode);
        if (list == null || list.size() == 0)
            mCheckVersionData.postValue(null);
        else
            mCheckVersionData.postValue(list.get(0));
    }

    public void getSeatAbout(String stuNo, String examCode) {
        DBExamLayout mDbExamLayout = DBOperation.getStudentInfo(examCode, stuNo);
        mSeat.postValue(mDbExamLayout);
    }


    public void setMsg(DBExaminee dbExaminee, String base64, String time, String type, String number) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                DBExamExport dbExamExport = new DBExamExport();
                dbExamExport.setId(dbExaminee.getStuNo() + dbExamExport.getSe_code());
                dbExamExport.setStu_no(dbExaminee.getStuNo());
                dbExamExport.setExaminee_id(dbExaminee.getId());
                dbExamExport.setVerify_time(time);
                dbExamExport.setVerify_result(type);
                dbExamExport.setMatch_rate(number);
                dbExamExport.setSe_code(dbExamExport.getSe_code());
                dbExamExport.setEntrance_photo_url(base64);
                dbExamExport.setExam_code(dbExamExport.getExam_code());
                dbExamExport.setSys_org_code(dbExamExport.getSys_org_code());
                dbExamExport.setEquipment(DeviceUtils.getDeviceSN());
                realm.insertOrUpdate(dbExamExport);
            }
        });
    }
}
