package com.askia.coremodel.datamodel.database.operation;

import com.askia.coremodel.datamodel.database.db.DBLogs;
import com.blankj.utilcode.util.TimeUtils;

import io.realm.Realm;

/**
 * 日志Util
 */
public class LogsUtil {

    /**
     * @param operations 插入操作日志
     */
    public static void saveOperationLogs(String operations) {
        DBLogs logs = new DBLogs();
        logs.setOperationTime(TimeUtils.millis2String(System.currentTimeMillis()));
        logs.setOperationInstruction(operations);
        Realm.getDefaultInstance().executeTransactionAsync(realm -> realm.copyToRealmOrUpdate(logs));
    }
}
