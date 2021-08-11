package com.askia.coremodel.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.datamodel.http.entities.CheckVersionData;

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

    public MutableLiveData<DBExaminee> getmCheckVersionData() {
        return mCheckVersionData;
    }


    public void quickPeople(String name) {
        //  先取本地数据
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {


            @Override
            public void execute(Realm realm) {
                RealmQuery<DBExaminee> realmQuery = realm.where(DBExaminee.class);
                realmQuery = realmQuery.equalTo("stuNo", name);
                RealmResults<DBExaminee> realmResults = realmQuery.findAll();
                if (realmResults == null || realmResults.size() == 0) {
                    mCheckVersionData.postValue(null);
                    return;
                }


            }
        });
    }
}
