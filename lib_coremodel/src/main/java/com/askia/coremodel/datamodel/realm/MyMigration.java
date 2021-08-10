package com.askia.coremodel.datamodel.realm;

import com.blankj.utilcode.util.LogUtils;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        LogUtils.e("oldVersion->",oldVersion + "newVersion ->",newVersion);
        RealmSchema schema = realm.getSchema();
        if(newVersion > oldVersion)
        {
            // 版本1迁移到版本2
            if (oldVersion == 1L) {
            schema.create("MessageBean").addPrimaryKey("msgId").addField("msgType",String.class).addField("publishTime",String.class).addField("title",String.class)
            .addField("content",String.class).addField("isRead",Boolean.class);
            oldVersion++;
            }
            // 版本2迁移到版本3
            if (oldVersion == 2L) {
            /*schema.create("Person").addField("name",String.class)
                    .addField("age",int.class);
            oldVersion++;*/
                schema.get("MessageBean").addField("msgName",String.class);
                oldVersion++;
            }
            // 版本3迁移到版本4
            if (oldVersion == 3L) {
            /*schema.create("Person").addField("name",String.class)
                    .addField("age",int.class);
            oldVersion++;*/
                schema.get("MessageBean").addField("user",String.class);
                oldVersion++;
            }
        }
    }
}
