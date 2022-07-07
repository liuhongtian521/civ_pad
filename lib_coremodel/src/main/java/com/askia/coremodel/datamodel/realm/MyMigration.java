package com.askia.coremodel.datamodel.realm;


import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (newVersion > oldVersion) {
            // 版本1迁移到版本2,添加健康码
            if (oldVersion == 1L) {
                schema.create("DBExamExport").
                        addField("healthCode", String.class);
                schema.create("DBExaminee").
                        addField("healthCode", String.class);
                schema.create("DBExamLayout").
                        addField("healthCode", String.class);
                oldVersion++;
            }
        }
    }
}
