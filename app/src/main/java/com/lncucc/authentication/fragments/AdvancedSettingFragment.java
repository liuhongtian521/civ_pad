package com.lncucc.authentication.fragments;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.db.DBExamArrange;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.adapters.AdvanceSessionAdapter;
import com.lncucc.authentication.databinding.FragmentAdvancedSettingBinding;
import com.lncucc.authentication.widgets.PassWordClickCallBack;
import com.lncucc.authentication.widgets.VerifyCodeDialog;
import com.ttsea.jrxbus2.RxBus2;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 高级设置
 *
 * @edit ymy
 * @description v1.3.2 删除验证结束时间，新增识别间隔字段
 */
public class AdvancedSettingFragment extends BaseFragment implements PassWordClickCallBack {
    private FragmentAdvancedSettingBinding advancedSetting;
    private VerifyCodeDialog dialog;
    private String sVerifyTime;
    private String mVerifyIntervalTime; //识别间隔

    @Override
    public void onInit() {
        List<DBExamPlan> list = DBOperation.getExamPlan();
        //所有考试场次
        List<DBExamArrange> arrSessionList = DBOperation.getAllExamArrange();
        String time = "";
        if (list != null && list.size() > 0) {
            time = list.get(0).getVerifyStartTime() == null ? "0" : list.get(0).getVerifyStartTime();
            //设置开始时间
            advancedSetting.edtStartTime.setText(time);
            //设置识别间隔
            advancedSetting.edtVerifyIntervalTime.setText(list.get(0).getVerifyIntervalTime());
        }
        //用于操作查出集合
        List<DBExamArrange> relArrange = new ArrayList<>();
        for (DBExamArrange o: arrSessionList) {
            DBExamArrange dbExamArrange = new DBExamArrange();
            dbExamArrange.setStartTime(o.getStartTime());
            dbExamArrange.setEndTime(o.getEndTime());
            dbExamArrange.setSubName(o.getSubName());
            relArrange.add(dbExamArrange);
        }
        //计算时间
        if(!"0".equals(time)) {
            for (int i = 0; i < relArrange.size(); i++) {
                Date date;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(relArrange.get(i).getStartTime());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MINUTE,-Integer.valueOf(time));
                String relDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
                relArrange.get(i).setStartTime(relDate);
            }
        }
        dialog = new VerifyCodeDialog(getActivity(),this);
        advancedSetting.recyclerSession.setLayoutManager(new LinearLayoutManager(getActivity()));
        advancedSetting.recyclerSession.setAdapter(new AdvanceSessionAdapter(relArrange));
    }

    @Override
    public void onInitViewModel() {

    }

    @Override
    public View onInitDataBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        advancedSetting = DataBindingUtil.inflate(inflater, R.layout.fragment_advanced_setting, container, false);
        advancedSetting.setHandles(this);
        return advancedSetting.getRoot();
    }

    @Override
    public void onSubscribeViewModel() {

    }

    public void confirm(View view) {
        sVerifyTime = advancedSetting.edtStartTime.getText().toString();
        mVerifyIntervalTime = advancedSetting.edtVerifyIntervalTime.getText().toString();
        if (TextUtils.isEmpty(sVerifyTime)) {
            MyToastUtils.error("请设置验证时间", Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(mVerifyIntervalTime)){
            MyToastUtils.error("请设置识别间隔", Toast.LENGTH_LONG);
            return;
        }
        dialog.show();
    }

    @Override
    public void confirm(String pwd) {
        String localPwd = SharedPreferencesUtils.getString(getActivity(), "password", "123456");
        if (null != localPwd && localPwd.equals(pwd)){
            //写入开始验证时间和识别间隔
            DBOperation.updateVerifyTime(sVerifyTime,mVerifyIntervalTime);
            MyToastUtils.success("设置成功", Toast.LENGTH_SHORT);
            RxBus2.getInstance().postStickyEvent( "success");
        }else {
            MyToastUtils.error("密码输入错误！",Toast.LENGTH_SHORT);
        }
        dialog.dismiss();
    }

    @Override
    public void dismiss() {

    }
}
