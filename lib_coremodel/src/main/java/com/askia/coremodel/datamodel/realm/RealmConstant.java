package com.askia.coremodel.datamodel.realm;

import io.realm.RealmConfiguration;

public class RealmConstant
{
    public static int VERSON = 1;

    public static RealmConfiguration getRealmConfig()
    {
        return new RealmConfiguration.Builder()
                .name("CIV.realm")
                .schemaVersion(RealmConstant.VERSON)
                .deleteRealmIfMigrationNeeded()
                .build();
    }
}
