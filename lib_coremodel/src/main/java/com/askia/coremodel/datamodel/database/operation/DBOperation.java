package com.askia.coremodel.datamodel.database.operation;

import com.askia.coremodel.datamodel.database.db.DBAccount;
import com.askia.coremodel.datamodel.database.db.DBDataVersion;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamExport;
import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.db.DBExaminee;
import com.askia.coremodel.datamodel.database.db.DBLogs;
import com.askia.coremodel.event.ExportDataEvent;
import com.blankj.utilcode.util.LogUtils;
import com.ttsea.jrxbus2.RxBus2;

import java.util.List;
import java.util.Objects;

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
     * @return 考试计划列表
     */
    public static List<DBExamPlan> getExamPlan() {
        return Realm.getDefaultInstance().where(DBExamPlan.class).findAll();
    }

    public static DBExamPlan getExamPlan(String examCode) {
        return Realm.getDefaultInstance().where(DBExamPlan.class).equalTo("examCode", examCode)
                .findFirst();
    }

    /**
     * 获取考试名称
     *
     * @param examCode 考试代码
     * @return 考试名称
     */
    public static DBExamPlan getExamName(String examCode) {
        return Realm.getDefaultInstance().where(DBExamPlan.class).equalTo("examCode", examCode)
                .findFirst();
    }


    /**
     * 获取考场安排列表
     *
     * @return 考试安排
     */
    public static List<DBExamArrange> getDBExamArrange() {
        RealmQuery<DBExamArrange> query = Realm.getDefaultInstance().where(DBExamArrange.class);
        return query.distinct("seCode");
    }

    /**
     * 场次查看
     *
     * @return 场次列表
     */
    public static List<DBExamArrange> getAllExamArrange() {
        //根据时间排序
        return Realm.getDefaultInstance().where(DBExamArrange.class).findAll().sort("startTime");
    }

    public static void setDBExamExport(DBExamExport db) {
        Realm.getDefaultInstance().executeTransaction(realm -> realm.insertOrUpdate(db));
    }

    /**
     * 查询健康码
     *
     * @param id 编排表id
     * @return 健康码标识
     */
    public static String getHealthCode(String id) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("id", id);
        query.endGroup();
        return Objects.requireNonNull(query.findFirst()).getHealthCode();
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
     * @return 考试考试编排表
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
     * @return 考生信息列表
     */
    public static List<DBExaminee> getDBExaminee() {
        return Realm.getDefaultInstance().where(DBExaminee.class).findAll();
    }

    /**
     * @param params 身份证准考证后6位
     * @return 获取考试编排表
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
        return Objects.requireNonNull(Realm.getDefaultInstance().where(DBExaminee.class).equalTo("stuNo", stuNo)
                .findFirst()).getLiveAddr();
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

    /**
     * v1.3.2 验证结束时间修改为每科考试结束时间
     *
     * @param startTime    开始验证时间
     * @param intervalTime 验证间隔
     */
    public static void updateVerifyTime(String startTime, String intervalTime) {

        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
                    DBExamPlan plan = realm.where(DBExamPlan.class).findFirst();
                    if (plan != null) {
                        plan.setVerifyStartTime(startTime);
                        plan.setVerifyIntervalTime(intervalTime);
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
     * 根据场次编码更新导出数据是否实时上传成功的状态
     * 数据导出成功后调用（包含网络导出、SD卡导出、U盘导出）
     *
     * @param seCode 场地编码
     */
    public static void modifyExportDataState(String seCode) {
        //创建事件总线event
        ExportDataEvent event = new ExportDataEvent();
        int eventCode = 22;
        Realm.getDefaultInstance().executeTransactionAsync(realm -> {
            //根据场次编码和上传状态查询导出数据
            RealmQuery<DBExamExport> query = Realm.getDefaultInstance().where(DBExamExport.class);
            query.beginGroup();
            query.equalTo("seCode", seCode);
            query.equalTo("upLoadStatus", 0);
            query.endGroup();
            //导出数据中，未实时上传的dataList
            List<DBExamExport> dataList = query.findAll();
            for (DBExamExport item : dataList) {
                //更新导出成功数据的上传状态
                item.setUpLoadStatus(1);
            }

            //event添加场次码，验证数据使用
            event.setSeCode(seCode);
            //异步更新数据上传状态
            realm.insertOrUpdate(dataList);
        }, () -> {
            //上传状态更新成功
            event.setSuccess(true);
            RxBus2.getInstance().post(eventCode, event);
        }, error -> {
            //上传状态更新失败
            event.setSuccess(false);
            RxBus2.getInstance().post(eventCode, event);
        });
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
     *
     * @param examNum  准考证号
     * @param examCode 考试编号
     * @param seCode   场次号
     * @return 学生信息
     */
    public static DBExamLayout getStudentByExamCode(String examNum, String examCode, String seCode) {
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
     *
     * @param idNo     身份证号
     * @param examCode 考试编号
     * @param seCode   场次号
     * @return 学生信息
     */
    public static DBExamLayout getStudentByIdCard(String idNo, String examCode, String seCode) {
        DBExamLayout examLayout;
        if (idNo.contains("X")) {
            RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
            query.beginGroup();
            query.equalTo("examCode", examCode);
            query.equalTo("seCode", seCode);
            query.like("idCard", "?*" + idNo);
            query.endGroup();
            examLayout = query.findFirst();
            if (examLayout == null) {
                RealmQuery<DBExamLayout> querySmall = Realm.getDefaultInstance().where(DBExamLayout.class);
                querySmall.beginGroup();
                querySmall.equalTo("examCode", examCode);
                querySmall.equalTo("seCode", seCode);
                querySmall.like("idCard", "?*" + idNo.toLowerCase());
                querySmall.endGroup();
                examLayout = querySmall.findFirst();
            }
        } else {
            RealmQuery<DBExamLayout> queryNormal = Realm.getDefaultInstance().where(DBExamLayout.class);
            queryNormal.beginGroup();
            queryNormal.equalTo("examCode", examCode);
            queryNormal.equalTo("seCode", seCode);
            queryNormal.like("idCard", "?*" + idNo);
            queryNormal.endGroup();
            examLayout = queryNormal.findFirst();
        }
        return examLayout;
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
     *
     * @param seCode 场次码
     * @return 验证数据信息
     */
    public static List<DBExamExport> getVerifyListBySeCode(String seCode) {
        RealmQuery<DBExamExport> query = Realm.getDefaultInstance().where(DBExamExport.class);
        query.beginGroup();
        query.equalTo("seCode", seCode, Case.SENSITIVE);
        query.endGroup();
        return query.findAllSorted("verifyTime", Sort.DESCENDING);
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
        return Objects.requireNonNull(query.findFirst()).getSiteCode();
    }

    /**
     * @param params 身份证准考证后6位
     * @return 获取验证数据查询
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
    public static DBExamExport getExamExportById(String id) {
        RealmQuery<DBExamExport> query = Realm.getDefaultInstance().where(DBExamExport.class);
        query.beginGroup();
        query.equalTo("id", id, Case.SENSITIVE);
        query.endGroup();
        return query.findFirst();
    }

    /**
     * 更新数据上传状态
     *
     * @param data   上传数据
     * @param status 上传状态 0失败 1成功
     */
    public static void updateDataStatus(DBExamExport data, int status) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            data.setUpLoadStatus(status);
            realm.insertOrUpdate(data);
        });
    }

    /**
     * 查询实时上传失败的数据条数
     *
     * @param seCode 场次代码
     * @return num 当前场次未上传的数据数量
     */
    public static int getDataUpLoadFailedNum(String seCode) {
        RealmQuery<DBExamExport> query = Realm.getDefaultInstance().where(DBExamExport.class);
        query.beginGroup();
        query.equalTo("upLoadStatus", 0);
        query.equalTo("seCode", seCode, Case.SENSITIVE);
        query.endGroup();
        return query.findAll().size();
    }

    /**
     * 查询本地数据库验证账号密码一致性
     *
     * @param userName 账号
     * @param password 密码
     * @return 查询结果大于0 匹配
     */
    public static boolean isMatchingWithLocal(String userName, String password) {
        RealmQuery<DBAccount> query = Realm.getDefaultInstance().where(DBAccount.class);
        query.beginGroup();
        query.equalTo("code", userName);
        query.equalTo("password", password);
        query.endGroup();
        return query.findAll().size() != 0;
    }

    /**
     * 查询账号表中的code，用于校验数据导出一致性使用
     *
     * @return orgCode
     */
    public static String queryOrgCode() {
        return Objects.requireNonNull(Realm.getDefaultInstance().where(DBAccount.class).findFirst()).getCode();
    }

    /**
     * 上传数据时判断内置orgCode与导出数据内的sitCode是否相同，不相同则拒绝导出
     *
     * @return 根据内置orgCode去导出数据中查询，如果数量不一致则拒绝上传
     */
    public static boolean isAffiliationCurrentSite() {
        //获取orgCode
        String orgCode = queryOrgCode();
        return Realm.getDefaultInstance().where(DBExamExport.class).findAll().size()
                == Realm.getDefaultInstance().where(DBExamExport.class).beginGroup()
                .equalTo("siteCode", orgCode)
                .endGroup()
                .findAll()
                .size();
    }

    /**
     * 查询导入的账号,密码
     *
     * @return 账号密码
     */
    public static DBAccount queryInnerAccount() {
        return Realm.getDefaultInstance().where(DBAccount.class).findFirst();
    }

    /**
     * 查询距离当前考试最近的一场考试，（避免设置的验证开始时间过长，导致验证时间重合）
     *
     * @param currentExamStartTime 当前考试的开始时间
     * @return 上一场考试object
     */
    public static DBExamArrange queryLatestExam(String currentExamStartTime) {
        List<DBExamArrange> planList = Realm.getDefaultInstance().where(DBExamArrange.class)
                .findAllSorted("startTime", Sort.ASCENDING);
        int position = 0;
        for (int i = 0; i < planList.size(); i++) {
            assert planList.get(i) != null;
            if (currentExamStartTime.equals(planList.get(i).getStartTime())) {
                position = i;
            }
        }
        return position - 1 >= 0 ? planList.get(position - 1) : new DBExamArrange();
    }

}
