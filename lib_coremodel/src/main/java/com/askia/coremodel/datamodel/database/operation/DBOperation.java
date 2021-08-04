package com.askia.coremodel.datamodel.database.operation;

import com.ttsea.jrxbus2.RxBus2;

import io.realm.Realm;

public class DBOperation {

    /**
     *
     * @param t
     * @param <T>
     * @param object
     */
    public <T> void insert(Class<T> t,Object object){
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                RxBus2.getInstance().post(null);
            }
        });
    }
}
