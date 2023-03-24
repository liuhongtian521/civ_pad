package com.askia.coremodel.datamodel.database.operation;

import android.annotation.SuppressLint;
import android.os.Build;

import com.askia.coremodel.datamodel.data.ExamExportGroupBean;
import com.askia.coremodel.datamodel.data.StudentBean;
import com.askia.coremodel.datamodel.data.ValidationDataBean;
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
import com.blankj.utilcode.util.StringUtils;
import com.ttsea.jrxbus2.RxBus2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
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

    /**
     * fixed 产品修改要查询当前考点当前考试 所选考场的验证学生数
     * @param seCode 场次
     * @param examCode 考试
     * @param list 选中考场的集合
     * @return 当前考点当前考试 所选考场的验证学生数
     */
    @SuppressLint("CheckResult")
    public static int getDBExamExportNumber(String seCode, String examCode, List<DBExamLayout> list) {
        RealmQuery<DBExamExport> query = Realm.getDefaultInstance().where(DBExamExport.class);
        query.beginGroup();
        query.equalTo("seCode", seCode);
        query.equalTo("examCode", examCode);
        query.endGroup();
        //当前场次考生验证信息集合
        List<DBExamExport> exportList = query.findAll();
        AtomicInteger total = new AtomicInteger();
        //查询选中考场中的验证考生数量
        Observable.fromIterable(exportList)
                .flatMap(item ->
                        //二次迭代过滤考场号相同的数据
                        Observable.fromIterable(list).filter(layout ->item.getRoomNo().equals(layout.getRoomNo())))
                .count()
                .subscribe(count -> total.set(Integer.parseInt(count + "")));
        return total.get();
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
     * @param seCode 场次编码
     * @return 根据场次码获取当前场次下的 所有考场编号
     * she 8.23修改 去重
     */
    public static List<DBExamLayout> getRoomList(String seCode) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("seCode", seCode);
        query.endGroup();
        return query.distinct("roomNo").sort("roomNo",Sort.ASCENDING);
    }
    public static DBExamArrange getDBExamArrangeList(String seCode) {
        RealmQuery<DBExamArrange> query = Realm.getDefaultInstance().where(DBExamArrange.class);
        query.equalTo("seCode", seCode);
        List<DBExamArrange> dbExamArranges = query.findAll();

        return dbExamArranges.get(0);
    }
    public static List<DBExamLayout> getAllLayout(String examCode,String roomNo) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.equalTo("examCode", examCode);
        query.equalTo("roomNo", roomNo);
        return query.findAll();
    }
    /**
     * 查询当前场次下 选中的考场
     *
     * @param seCode 场次代码
     * @return 考场集合
     */
    public static List<DBExamLayout> getSelectedRoomList(String seCode) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("seCode", seCode);
        query.endGroup();
        //直接查询有问题
        List<DBExamLayout> layoutList = new ArrayList<>();
        for (DBExamLayout layout : query.distinct("roomNo")) {
            if (layout.isChecked()) {
                layoutList.add(layout);
            }
        }
        return layoutList;
    }

    /**
     * @param seCode   场次码
     * @param examCode 考试代码
     * @param roomList 所选考场的集合
     * @return 当前考试当前场次下所选考场的所有考生人数
     * @description fixed修正原有查询方法，在场次和考试代码条件的基础上添加用户选择的考场，
     * 返回当前考试当前场次下所选考场的所有考生。
     * @edit ymy
     */
    public static int getStudentNumber(String examCode, String seCode, List<String> roomList) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("examCode", examCode);
        query.equalTo("seCode", seCode);
        query.endGroup();
        List<DBExamLayout> list = new ArrayList<>();
        for (DBExamLayout item : query.findAll()) {
            if (roomList.contains(item.getRoomNo())) {
                list.add(item);
            }
        }
        return list.size();
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
    public static DBExamLayout getStudentInfo(String examCode, String stuNo, String seCode) {
        RealmQuery<DBExamLayout> query = Realm.getDefaultInstance().where(DBExamLayout.class);
        query.beginGroup();
        query.equalTo("examCode", examCode);
        query.equalTo("stuNo", stuNo);
        query.equalTo("seCode", seCode);
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
     * 查询刷脸验证间隔时间
     *
     * @return 刷脸验证间隔时间
     */
    public static int getIntervalVerifyTime() {
        return Integer.parseInt(Objects.requireNonNull(Realm.getDefaultInstance().where(DBExamPlan.class).findFirst()).getVerifyIntervalTime());
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
        return Realm.getDefaultInstance().where(DBLogs.class).findAllSorted("operationTime",Sort.DESCENDING);
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
     * @param seCode 场次（新增场次参数）
     * @return 获取验证数据查询
     */
    public static List<DBExamExport> getDBExportByIdNo(String params, String seCode) {
        RealmQuery<DBExamExport> query = Realm.getDefaultInstance().where(DBExamExport.class)
                .beginGroup()
                .equalTo("seCode", seCode)
                .endGroup()
                .beginGroup()
                .like("idCard", "?*" + params, Case.SENSITIVE)
                .or()
                .like("stuNo", "?*" + params, Case.SENSITIVE)
                .endGroup();
        return query.findAllSorted("verifyTime", Sort.DESCENDING);
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
        DBAccount account = Realm.getDefaultInstance().where(DBAccount.class).findFirst();
        return  account != null ? account.getCode(): "";
    }

    /**
     * 上传数据时判断内置orgCode与导出数据内的sitCode是否相同，不相同则拒绝导出
     *
     * @return 根据内置orgCode去导出数据中查询，如果数量不一致则拒绝上传
     */
    public static boolean isAffiliationCurrentSite(String orgCode) {
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
            if (currentExamStartTime.equals(Objects.requireNonNull(planList.get(i)).getStartTime())) {
                position = i;
            }
        }
        return position - 1 >= 0 ? planList.get(position - 1) : new DBExamArrange();
    }

    /**
     * 查询当前场次数据验证情况，并按考场编号升序排列
     *
     * @param seCode 场次代码
     * @return 验证数据集合
     */
    @SuppressLint("CheckResult")
    public static List<ExamExportGroupBean> queryValidationDataBySeCode(String seCode) {
        List<DBExamExport> list = Realm.getDefaultInstance().where(DBExamExport.class)
                .beginGroup()
                .equalTo("seCode", seCode)
                .endGroup()
                .distinct("roomNo").sort("roomNo", Sort.ASCENDING);
        List<ExamExportGroupBean> groupList = new ArrayList<>();
        //打散验证数据的list,按roomNo进行分组并计算每个考场学生的验证数量
        Observable.fromIterable(list)
                .groupBy(DBExamExport::getRoomNo)
                .flatMapSingle(groupedObservable -> groupedObservable.toList()
                .map(examExports ->
                    //返回重组后的ExamExportGroupBean
                    integrateGroup(groupedObservable.getKey(), examExports, seCode)
                ))
                .toList()
                .subscribe((Consumer<List<ExamExportGroupBean>>) groupList::addAll);
        return groupList;
    }
    public static List<ExamExportGroupBean> getAllExaminationHall(List<ExamExportGroupBean> examExportGroupBeans,String seCode,String examCode){
        List<ExamExportGroupBean> relExamExportGroupBeans = new ArrayList<>();
        List<DBExamLayout> dbExamLayouts = DBOperation.getRoomList(seCode);

        if(null != examExportGroupBeans && examExportGroupBeans.size()>0){
            for (int j = 0; j <dbExamLayouts.size() ; j++) {
                Boolean flag = false;
                for (int i = 0; i < examExportGroupBeans.size(); i++) {
                    if(dbExamLayouts.get(j).getRoomNo().equals(examExportGroupBeans.get(i).getRoomNo())){
                        flag = true;
                     }
                }
                if(!flag){
                    ExamExportGroupBean examExportGroupBean = new ExamExportGroupBean();
                    examExportGroupBean.setRoomNo(dbExamLayouts.get(j).getRoomNo());
                    examExportGroupBean.setDoubtValidation(0);
                    examExportGroupBean.setTotal(30);
                    examExportGroupBean.setNotValidation(0);
                    examExportGroupBean.setPassValidation(0);
                    examExportGroupBean.setNotPassValidation(0);
                    examExportGroupBean.setIsNewFlag(true);
                    examExportGroupBeans.add(examExportGroupBean);
                }
            }
            relExamExportGroupBeans.addAll(examExportGroupBeans);
        }else{
            for (int i = 0; i < dbExamLayouts.size(); i++) {
                ExamExportGroupBean examExportGroupBean = new ExamExportGroupBean();
                examExportGroupBean.setRoomNo(dbExamLayouts.get(i).getRoomNo());
                examExportGroupBean.setDoubtValidation(0);
                examExportGroupBean.setIsNewFlag(true);
                examExportGroupBean.setTotal(30);
                examExportGroupBean.setNotValidation(0);
                examExportGroupBean.setPassValidation(0);
                examExportGroupBean.setNotPassValidation(0);
                relExamExportGroupBeans.add(examExportGroupBean);
            }
        }
        for (int i = 0; i < relExamExportGroupBeans.size(); i++) {
            List<DBExamLayout> list = Realm.getDefaultInstance().where(DBExamLayout.class).beginGroup()
                    .equalTo("examCode", examCode)
                    .equalTo("seCode", seCode)
                    .equalTo("roomNo", relExamExportGroupBeans.get(i).getRoomNo())
                    .endGroup()
                    .findAllSorted("seatNo",Sort.ASCENDING);
            if(relExamExportGroupBeans.get(i).getIsNewFlag()){
                relExamExportGroupBeans.get(i).setTotal(list.size());
                relExamExportGroupBeans.get(i).setNotValidation(list.size());
            }
        }
        return relExamExportGroupBeans;
    }
    /**
     * 数据整理
     *
     * @param seCode 场次
     * @param roomNo 考场
     * @param list   验证数据
     * @return ExamExportGroupBean
     */
    private static ExamExportGroupBean integrateGroup(String roomNo, List<DBExamExport> list, String seCode) {
        //创建一个新的集合
        ExamExportGroupBean bean = new ExamExportGroupBean(roomNo, list);
        //当前场次当前考场的学生总数
        int total = getCurrentRoomStuNum(seCode, roomNo);
        //已通过
        int pass = queryCurrentRoomStuValidationState(seCode, roomNo, "1");
        //未通过
        int notPass = queryCurrentRoomStuValidationState(seCode, roomNo, "2");
        //存疑
        int doubt = queryCurrentRoomStuValidationState(seCode, roomNo, "3");
        //未验证
        int notValidation = total - pass - notPass - doubt;
        bean.setTotal(total);
        bean.setPassValidation(pass);
        bean.setNotPassValidation(notPass);
        bean.setDoubtValidation(doubt);
        bean.setNotValidation(notValidation);
        return bean;
    }


    /**
     * 根据场次和考场查询当前考场学生总数
     *
     * @param seCode 场次
     * @param roomNo 考场
     * @return 当前考场学生总数
     */
    private static int getCurrentRoomStuNum(String seCode, String roomNo) {
        return Realm.getDefaultInstance().where(DBExamLayout.class)
                .beginGroup()
                .equalTo("seCode", seCode)
                .equalTo("roomNo", roomNo)
                .endGroup()
                .findAll()
                .size();
    }

    /**
     * 根据场次和考场查询当前考场学生验证状态
     *
     * @param seCode 场次
     * @param roomNo 考场
     * @param state  验证状态 1成功 2失败 3存疑
     * @return 当前考场学生验证状态
     */
    private static int queryCurrentRoomStuValidationState(String seCode, String roomNo, String state) {
        return Realm.getDefaultInstance().where(DBExamExport.class)
                .beginGroup()
                .equalTo("seCode", seCode)
                .equalTo("roomNo", roomNo)
                .equalTo("verifyResult", state)
                .endGroup()
                .findAll()
                .size();
    }

    /**
     * @param seCode   场次
     * @param examCode 考试代码
     * @return 根据场次和考试代码 返回验证数量
     */
    public static ValidationDataBean queryStuNumBySeCode(String seCode, String examCode) {
        ValidationDataBean bean = new ValidationDataBean();
        //获取当前考试，当前场次下考生总数
        int total = Realm.getDefaultInstance().where(DBExamLayout.class).beginGroup()
                .equalTo("examCode", examCode)
                .equalTo("seCode", seCode)
                .endGroup()
                .findAll()
                .size();
        int passValidation = queryStudentNumByState(examCode, seCode, "1").size();
        int doubtValidation = queryStudentNumByState(examCode, seCode, "3").size();
        int notPassValidation = queryStudentNumByState(examCode, seCode, "2").size();
        int notValidation = total - passValidation - doubtValidation - notPassValidation;
        bean.setPassValidation(passValidation);
        bean.setDoubtValidation(doubtValidation);
        bean.setNotPassValidation(notPassValidation);
        bean.setNotValidation(notValidation);
        bean.setTotal(total);
        return bean;
    }

    /**
     * @param seCode       场次
     * @param examCode     考试代码
     * @param verifyResult 验证状态
     * @return 已验证考生的列表
     */
    private static List<DBExamExport> queryStudentNumByState(String examCode, String seCode, String verifyResult) {
        return Realm.getDefaultInstance().where(DBExamExport.class).beginGroup()
                .equalTo("examCode", examCode)
                .equalTo("seCode", seCode)
                .equalTo("verifyResult", verifyResult)
                .endGroup()
                .findAll();
    }

    /**
     * 根据场次和考场查询当前考场学生验证状态
     *
     * @param examCode 考试代码
     * @param seCode   场次
     * @param roomNo   考场
     * @param stuNo    学号
     * @return 考生对象
     */
    private static DBExamExport queryStuValidationState(String examCode, String seCode, String roomNo, String stuNo) {
        return Realm.getDefaultInstance().where(DBExamExport.class)
                .beginGroup()
                .equalTo("seCode", seCode)
                .equalTo("roomNo", roomNo)
                .equalTo("stuNo", stuNo)
                .equalTo("examCode", examCode)
                .endGroup()
                .findFirst();
    }

    /**
     * @param examCode 考试代码
     * @param seCode   场次
     * @param roomNo   考场
     * @return 考生列表
     */
    public static List<StudentBean> queryStudentByRoomAndSeCode(String examCode, String seCode, String roomNo) {
        //构建初始化座位表数组 用座位号去匹配
        List<StudentBean> relStudentBean = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            StudentBean studentBean = new StudentBean();
            if(i<9) {
                studentBean.setSeatNo("0"+String.valueOf(i + 1));
                studentBean.setName("");
            }else{
                studentBean.setSeatNo(String.valueOf(i+1));
                studentBean.setName("      无人");
            }
            relStudentBean.add(studentBean);
        }
        //获取考生列表
        List<DBExamLayout> list = Realm.getDefaultInstance().where(DBExamLayout.class).beginGroup()
                .equalTo("examCode", examCode)
                .equalTo("seCode", seCode)
                .equalTo("roomNo", roomNo)
                .endGroup()
                .findAllSorted("seatNo",Sort.ASCENDING);
        StudentBean stuBean;
        List<StudentBean> stuList = new ArrayList<>();
        for (DBExamLayout bean : list) {
            stuBean = new StudentBean();
            stuBean.setRoomNo(bean.getRoomNo());
            stuBean.setExamCode(examCode);
            stuBean.setName(bean.getStuName());
            stuBean.setSeatNo(bean.getSeatNo());
            stuBean.setSeCode(bean.getSeCode());
            String validationState;
            if (null == queryStuValidationState(examCode, seCode, roomNo, bean.getStuNo())) {
                validationState = "0";
                stuBean.setId("");
            } else {
                validationState = queryStuValidationState(examCode, seCode, roomNo, bean.getStuNo()).getVerifyResult();
                stuBean.setId(queryStuValidationState(examCode,seCode,roomNo,bean.getStuNo()).getId());
            }
            stuBean.setValidationState(validationState);
            stuList.add(stuBean);
        }
        for (int i = 0; i < relStudentBean.size(); i++) {
            for (StudentBean bean : stuList){
                if(relStudentBean.get(i).getSeatNo().equals(bean.getSeatNo())){
                    relStudentBean.set(i,bean);
                }
            }
        }
        //蛇形排序
        List<StudentBean> resultStudentBean = new ArrayList<>();
        int firstNum = 6;
        int endNum = 22;
        for (int j = 0; j < relStudentBean.size(); j++) {
            if(j<7){
               resultStudentBean.add(relStudentBean.get(firstNum));
               firstNum--;
            }else if(j>14&&j<23){
                resultStudentBean.add(relStudentBean.get(endNum));
                endNum--;
            }else{
                resultStudentBean.add(relStudentBean.get(j));
            }


        }

        return resultStudentBean;
    }

}
