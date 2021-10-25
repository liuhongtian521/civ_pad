package com.askia.coremodel.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.database.db.DBExamLayout;
import com.askia.coremodel.datamodel.database.operation.DBOperation;

/**
 * 人工审核viewModel
 */
public class ManualCheckViewModel extends BaseViewModel {

    //模糊搜索学生
    private MutableLiveData<DBExamLayout> dbLayoutObservable = new MutableLiveData<>();

    public MutableLiveData<DBExamLayout> getStudentInfo() {
        return dbLayoutObservable;
    }

    /**
     * 学生搜索
     *
     * @param type      搜索类型
     * @param condition 搜索条件
     * @param examCode  考试代码
     * @param seCode    场次码
     */
    public void fetchStudentInfo(int type, String condition, String examCode, String seCode) {
        DBExamLayout mDbExamLayout;
        //准考证查询
        if (type == 0) {
            mDbExamLayout = DBOperation.getStudentByExamCode(condition, examCode, seCode);
        } else {
            //身份证查询
            mDbExamLayout = DBOperation.getStudentByIdCard(condition, examCode, seCode);
        }
        dbLayoutObservable.postValue(mDbExamLayout);
    }
}
