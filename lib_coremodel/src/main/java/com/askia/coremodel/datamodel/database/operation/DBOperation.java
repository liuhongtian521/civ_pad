package com.askia.coremodel.datamodel.database.operation;

import com.askia.coremodel.datamodel.database.db.DBDataVersion;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.datamodel.database.db.DBLogs;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 *
 */
public class DBOperation {

    /**
     * 获取考试计划列表
     *
     * @return
     */
    public static List<DBExamPlan> getExamPlan() {
        return Realm.getDefaultInstance().where(DBExamPlan.class).findAll();
    }

    /**
     * 获取考场安排列表
     *
     * @return
     */
    public static List<DBExamArrange> getDBExamArrange() {
        return Realm.getDefaultInstance().where(DBExamArrange.class).findAll();
    }

    /**
     * 获取考试考试编排表
     *
     * @return
     */
    public static List<DBExamLayout> getDBExamLayout() {
        return Realm.getDefaultInstance().where(DBExamLayout.class).findAll();
    }

    /**
     * 获取考生信息列表
     *
     * @return
     */
    public static List<DBExaminee> getDBExaminee() {
        return Realm.getDefaultInstance().where(DBExaminee.class).findAll();
    }

    /**
     * @return 获取考试编排表
     * @params 身份证准考证后6位
     */
    public static List<DBExamLayout> getDBExamLayoutByIdNo(String params) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        //身份证号
        query.like("idCard", "?*" + params, Case.SENSITIVE);
        //准考证号
        query.or().like("exReNum", "?*" + params,Case.SENSITIVE);
        query.endGroup();
        return query.findAll();
    }

    /**
     * 根据
     *
     * @param seCode 场次编码
     * @return 根据场次码获取当前场次下的 所有考场编号
     */
    public static List<DBExamLayout> getRoomList(String seCode) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("seCode", seCode);
        query.endGroup();
        return query.findAll();
    }

    public static List<DBExaminee> quickPeople(String name, String examCode) {
        RealmQuery<DBExaminee> query = Realm.getDefaultInstance().where(DBExaminee.class);
        query.beginGroup();
        query.equalTo("stuNo", name);
        query.equalTo("examCode", examCode);
        query.endGroup();
        return query.findAll();
    }

    /**
     *
     * @return 返回版本对象
     */
    public static DBDataVersion getVersion(){
        return Realm.getDefaultInstance().where(DBDataVersion.class).findAll().last();
    }

    /**
     * 根据id查看数据查看列表详细信息
     * @param id id
     * @return 当前场次学生信息
     */
    public static DBExamLayout getStudentInfo(String id){
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("id",id);
        query.endGroup();
        return query.findAll().first();
    }

    /**
     * 根据stuNo 查询居住地址
     * @param stuNo 学生编号
     * @return 居住信息
     */
    public static String getLiveAddress(String stuNo){
        return Realm.getDefaultInstance().where(DBExaminee.class).equalTo("stuNo",stuNo)
                .findFirst().getLiveAddr();
    }

    /**
     * 根据考试代码 学生编号获取详细信息
     * @param examCode 考试代码
     * @param stuNo 学生编号
     * @return 详细信息
     */
    public static DBExamLayout getStudentInfo(String examCode,String stuNo){
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("examCode",examCode);
        query.equalTo("stuNo",stuNo);
        query.endGroup();
        return query.findFirst();
    }

    /**
     * 根据场次代码 查询导出数据
     * @param seCode 场次编码
     * @return 导出数据列表
     */
    public static List<DBExamExport> getExportBySeCode(String seCode){
        RealmQuery<DBExamExport> query = Realm.getDefaultInstance().where(DBExamExport.class);
        query.beginGroup();
        query.equalTo("seCode",seCode);
        query.endGroup();
        return  query.findAll();
    }

    /**
     * 查询日志信息
     * @return 数据操作日志信息
     */
    public static List<DBLogs> getOperationLogs(){
        return Realm.getDefaultInstance().where(DBLogs.class).findAll();
    }

    /**
     *
     * @return 获取身份信息总数
     */
    public static String getCount(){
        return String.valueOf(Realm.getDefaultInstance().where(DBExamLayout.class).findAll().size());
    }

    /**
     *
     * @return 获取验证信息总数
     */
    public static String getAuthCount(){
        return String.valueOf(Realm.getDefaultInstance().where(DBExamExport.class).findAll().size());
    }

    /**
     * @return 获取验证数据列表
     */
    public static List<DBExamExport> getVerifyList(){
        return Realm.getDefaultInstance().where(DBExamExport.class).findAll();
    }

    /**
     * @return 获取验证数据查询
     * @params 身份证准考证后6位
     */
    public static List<DBExamExport> getDBExportByIdNo(String params) {
        RealmQuery<DBExamExport> query = Realm.getDefaultInstance().where(DBExamExport.class);
        query.beginGroup();
        //身份证号
        query.like("idCard", "?*" + params, Case.SENSITIVE);
        //准考证号
        query.or().like("stuNo", "?*" + params,Case.SENSITIVE);
        query.endGroup();
        return query.findAll();
    }
}
