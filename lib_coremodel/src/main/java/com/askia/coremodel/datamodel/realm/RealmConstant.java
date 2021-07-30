package com.askia.coremodel.datamodel.realm;

import io.realm.RealmConfiguration;

public class RealmConstant
{
    public static int VERSON = 4;

    public static RealmConfiguration getRealmConfig()
    {
        return new RealmConfiguration.Builder()
                .name("DDSW")
                .schemaVersion(RealmConstant.VERSON)
                .build();
    }
}
