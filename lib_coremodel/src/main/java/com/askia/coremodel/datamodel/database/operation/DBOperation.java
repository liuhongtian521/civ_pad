package com.askia.coremodel.datamodel.database.operation;

import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.db.DBExaminee;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;

/**
 *
 */
public class DBOperation {

    /**
     * 获取考试计划列表
     * @return
     */
    public static List<DBExamPlan> getExamPlan(){
        return Realm.getDefaultInstance().where(DBExamPlan.class).findAll();
    }

    /**
     * 获取考场安排列表
     * @return
     */
    public static List<DBExamArrange> getDBExamArrange(){
        return Realm.getDefaultInstance().where(DBExamArrange.class).findAll();
    }

    /**
     * 获取考试考试编排表
     * @return
     */
    public static List<DBExamLayout> getDBExamLayout(){
        return Realm.getDefaultInstance().where(DBExamLayout.class).findAll();
    }

    /**
     * 获取考生信息列表
     * @return
     */
    public static List<DBExaminee> getDBExaminee(){
        return Realm.getDefaultInstance().where(DBExaminee.class).findAll();
    }

    /**
     * @params 身份证准考证后6位
     *
     * @return 获取考试编排表
     */
    public static List<DBExamLayout> getDBExamLayoutByIdNo(String params){
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        //身份证号
        query.like("idCard","?*"+params, Case.SENSITIVE);
        //准考证号
        query.or().equalTo("exReNum","?*" + params);
        query.endGroup();
        return query.findAll();
    }


}
