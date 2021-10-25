package com.askia.coremodel.datamodel.database.operation;

import com.askia.coremodel.datamodel.database.db.DBDataVersion;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.datamodel.database.db.DBLogs;
import com.blankj.utilcode.util.LogUtils;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;

/**
 * 数据库管理
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

    public static DBExamPlan getExamPlan(String examCode) {
        return Realm.getDefaultInstance().where(DBExamPlan.class).equalTo("examCode", examCode)
                .findFirst();
    }

    public static DBExamPlan getSingleExamPlan() {
        return Realm.getDefaultInstance().where(DBExamPlan.class).findFirst();
    }

    /**
     * 获取考试名称
     *
     * @param examCode
     * @return
     */
    public static DBExamPlan getExamName(String examCode) {
        return Realm.getDefaultInstance().where(DBExamPlan.class).equalTo("examCode", examCode)
                .findFirst();
    }


    /**
     * 获取考场安排列表
     *
     * @return
     */
    public static List<DBExamArrange> getDBExamArrange() {
        RealmQuery<DBExamArrange> query = Realm.getDefaultInstance().where(DBExamArrange.class);
        return query.distinct("seCode");

//        return Realm.getDefaultInstance().where(DBExamArrange.class).distinct("seCode").fin;
    }

    /**
     * 场次查看
     * @return
     */
    public static List<DBExamArrange> getAllExamArrange() {
        //根据时间排序
        return Realm.getDefaultInstance().where(DBExamArrange.class).findAll().sort("startTime");
    }

    public static List<DBExamArrange> getDBExamArrange(String examCode) {
        return Realm.getDefaultInstance().where(DBExamArrange.class).findAll();
    }


    public static void setDBExamExport(DBExamExport db) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(db);
            }
        });
    }

    public static int getDBExamExport(String id) {
        RealmQuery<DBExamExport> query = Realm.getDefaultInstance().where(DBExamExport.class);
        query.beginGroup();
        query.equalTo("id", id);
        query.endGroup();
        return query.findAll().size();
    }

    public static int getDBExamExportNumber(String seCode, String examCode) {
        RealmQuery<DBExamExport> query = Realm.getDefaultInstance().where(DBExamExport.class);
        query.beginGroup();
        query.equalTo("seCode", seCode);
        query.equalTo("examCode", examCode);
        query.endGroup();
        return query.findAll().size();
    }

    /**
     * 获取考试考试编排表
     *
     * @return
     */
    public static List<DBExamLayout> getDBExamLayout() {
        return Realm.getDefaultInstance().where(DBExamLayout.class).findAll();
    }

    public static List<DBExamLayout> getDBExamLayout(String examCode) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("examCode", examCode);
        query.endGroup();
        return query.findAll();
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
        query.or().like("exReNum", "?*" + params, Case.SENSITIVE);
        query.endGroup();
        return query.findAll();
    }

    /**
     * 根据
     *
     * @param seCode 场次编码
     * @return 根据场次码获取当前场次下的 所有考场编号
     * she 8.23修改 去重
     */
    public static List<DBExamLayout> getRoomList(String seCode) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("seCode", seCode);
        query.endGroup();
        return query.distinct("roomNo");
    }

    public static int getStudentNumber(String examCode, String seCode) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("examCode", examCode);
        query.equalTo("seCode", seCode);
        query.endGroup();
        return query.findAll().size();
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
     * @return 返回版本对象
     */
    public static DBDataVersion getVersion() {
        return Realm.getDefaultInstance().where(DBDataVersion.class).findAll().size() == 0 ?
                null : Realm.getDefaultInstance().where(DBDataVersion.class).findAll().last();
    }

    /**
     * 根据id查看数据查看列表详细信息
     *
     * @param id id
     * @return 当前场次学生信息
     */
    public static DBExamLayout getStudentInfo(String id) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("id", id);
        query.endGroup();
        return query.findAll().first();
    }

    /**
     * 根据stuNo 查询居住地址
     *
     * @param stuNo 学生编号
     * @return 居住信息
     */
    public static String getLiveAddress(String stuNo) {
        return Realm.getDefaultInstance().where(DBExaminee.class).equalTo("stuNo", stuNo)
                .findFirst().getLiveAddr();
    }

    /**
     * 根据考试代码 学生编号获取详细信息
     *
     * @param examCode 考试代码
     * @param stuNo    学生编号
     * @return 详细信息
     */
    public static DBExamLayout getStudentInfo(String examCode, String stuNo, String secode) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("examCode", examCode);
        query.equalTo("stuNo", stuNo);
        query.equalTo("seCode", secode);
        query.endGroup();
        return query.findFirst();
    }

    public static void updateVerifyTime(String startTime, String endTime) {

        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
                    DBExamPlan plan = realm.where(DBExamPlan.class).findFirst();
                    if (plan != null){
                        plan.setVerifyStartTime(startTime);
                        plan.setVerifyEndTime(endTime);
                        realm.copyToRealmOrUpdate(plan);
                    }
                },
                () -> LogUtils.e("verify time insert success ->", "insert success!"),
                error -> LogUtils.e("verify time insert error->", error.getMessage()));
    }


    /**
     * 根据场次代码 查询导出数据
     *
     * @param seCode 场次编码
     * @return 导出数据列表
     */
    public static List<DBExamExport> getExportBySeCode(String seCode) {
        RealmQuery<DBExamExport> query = Realm.getDefaultInstance().where(DBExamExport.class);
        query.beginGroup();
        query.equalTo("seCode", seCode);
        query.endGroup();
        return query.findAll();
    }

    /**
     * 根据考试代码 学生编号 场次代码 获取详细信息
     *
     * @param examCode 考试代码
     * @param stuNo    学生编号
     * @param seCode   场次代码
     * @return 详细信息
     */
    public static DBExamLayout getStudentInfoTwo(String examCode, String stuNo, String seCode) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("examCode", examCode);
        query.equalTo("stuNo", stuNo);
        query.equalTo("seCode", seCode);
        query.endGroup();
        return query.findFirst();
    }

    public static DBExamLayout getStudtByNum(String examNum, String examCode, String seCode) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("exReNum", examNum);
        query.equalTo("examCode", examCode);
        query.equalTo("seCode", seCode);
        query.endGroup();
        return query.findFirst();
    }

    public static DBExamLayout getStudentByCode(String idCode, String examCode, String seCode) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("idCard", idCode);
        query.equalTo("examCode", examCode);
        query.equalTo("seCode", seCode);
        query.endGroup();
        return query.findFirst();
    }

    /**
     * 根据准考证模糊查询学生信息
     * @param examNum 准考证号
     * @param examCode 考试编号
     * @param seCode 场次号
     * @return 学生信息
     */
    public static DBExamLayout getStudentByExamCode(String examNum, String examCode, String seCode){
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        //身份证号
        query.like("exReNum", "?*" + examNum, Case.SENSITIVE);
        query.equalTo("examCode", examCode);
        query.equalTo("seCode", seCode);
        query.endGroup();
        return query.findFirst();
    }

    /**
     * 根据准考证模糊查询学生信息
     * @param idNo 身份证号
     * @param examCode 考试编号
     * @param seCode 场次号
     * @return 学生信息
     */
    public static DBExamLayout getStudentByIdCard(String idNo, String examCode, String seCode){
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        //身份证号
        query.like("idCard", "?*" + idNo, Case.SENSITIVE);
        query.equalTo("examCode", examCode);
        query.equalTo("seCode", seCode);
        query.endGroup();
        return query.findFirst();
    }

    /*通过考试代码来获取场次
     * examCode 考试代码
     * */
    public static List<DBExamArrange> getExamArrange(String examCode) {
        RealmQuery<DBExamArrange> query = Realm.getDefaultInstance().where(DBExamArrange.class);
        query.beginGroup();
        query.equalTo("examCode", examCode);
        query.endGroup();
        return query.findAll();
    }

    /**
     * 查询日志信息
     *
     * @return 数据操作日志信息
     */
    public static List<DBLogs> getOperationLogs() {
        return Realm.getDefaultInstance().where(DBLogs.class).findAll();
    }

    /**
     * @return 获取身份信息总数
     */
    public static String getCount() {
        return String.valueOf(Realm.getDefaultInstance().where(DBExamLayout.class).findAll().size());
    }

    /**
     * @return 获取验证信息总数
     */
    public static String getAuthCount() {
        return String.valueOf(Realm.getDefaultInstance().where(DBExamExport.class).findAll().size());
    }

    /**
     * @return 获取验证数据列表
     */
    public static List<DBExamExport> getVerifyList() {
        return Realm.getDefaultInstance().where(DBExamExport.class).findAllSorted("verifyTime", Sort.DESCENDING);
    }

    /**
     * 根据场次编码获取 验证数据信息
     * @param seCode 场次码
     * @return
     */
    public static List<DBExamExport> getVerifyListBySeCode(String seCode){
        RealmQuery<DBExamExport> query = Realm.getDefaultInstance().where(DBExamExport.class);
        query.beginGroup();
        query.equalTo("seCode",seCode, Case.SENSITIVE);
        query.endGroup();
        return query.findAll();
    }

    /**
     * 根据examCode 获取sitCode
     *
     * @return siteCode
     */
    public static String getSiteCode(String examCode) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("examCode", examCode, Case.SENSITIVE);
        query.endGroup();
        return query.findFirst().getSiteCode();
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
        query.or().like("stuNo", "?*" + params, Case.SENSITIVE);
        query.endGroup();
        return query.findAll();
    }

    /**
     * @param id id
     * @return 根据id获取验证信息详情
     */
    public static DBExamExport getExamportById(String id) {
        RealmQuery<DBExamExport> query = Realm.getDefaultInstance().where(DBExamExport.class);
        query.beginGroup();
        query.equalTo("id", id, Case.SENSITIVE);
        query.endGroup();
        return query.findFirst();
    }

//    /**
//     * @param page page
//     * @param pageSize pageSize
//     * @return 分页查询
//     */
//    public static List<DBExamLayout> getMore(int page, int pageSize){
//        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
//        query.beginGroup();
//        query.l
//
//    }
}
