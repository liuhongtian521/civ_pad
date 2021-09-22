package com.lncucc.authentication.fragments;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.askia.common.base.BaseFragment;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.db.DBExamPlan;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.FragmentAdvancedSettingBinding;
import com.lncucc.authentication.widgets.PassWordClickCallBack;
import com.lncucc.authentication.widgets.VerifyCodeDialog;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.realm.Realm;

/**
 * 高级设置
 */
public class AdvancedSettingFragment extends BaseFragment implements PassWordClickCallBack {
    private FragmentAdvancedSettingBinding advancedSetting;
    private VerifyCodeDialog dialog;
    private String sVerifyTime;
    private String eVerifyTime;

    @Override
    public void onInit() {
        List<DBExamPlan> list = DBOperation.getExamPlan();
        if (list != null && list.size() > 0) {
            //设置开始时间
            advancedSetting.edtStartTime.setText(list.get(0).getVerifyStartTime() == null ? "0" : list.get(0).getVerifyStartTime());
            //设置结束时间
            advancedSetting.edtEndTime.setText(list.get(0).getVerifyEndTime() == null ? "0" : list.get(0).getVerifyEndTime());
        }
        dialog = new VerifyCodeDialog(getActivity(),this);
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
        eVerifyTime = advancedSetting.edtEndTime.getText().toString();
        if (TextUtils.isEmpty(sVerifyTime) || TextUtils.isEmpty(eVerifyTime)) {
            MyToastUtils.error("请设置验证时间", Toast.LENGTH_LONG);
        } else {
            dialog.show();
        }
    }

    @Override
    public void confirm(String pwd) {
//        if (DBOperation.getSingleExamPlan() != null){
        //产品确认修改为登录密码判断

        String localPwd = SharedPreferencesUtils.getString(getActivity(), "password", "123456");
        if (null != localPwd && localPwd.equals(pwd)){
            //写入验证时间
            DBOperation.updateVerifyTime(sVerifyTime, eVerifyTime);

            MyToastUtils.success("设置成功", Toast.LENGTH_SHORT);
        }else {
            MyToastUtils.error("密码输入错误！",Toast.LENGTH_SHORT);
        }
//        }else {
//            MyToastUtils.error("暂无验证码,请重新导入重试",Toast.LENGTH_SHORT);
//        }
        dialog.dismiss();
    }

    @Override
    public void dismiss() {

    }
}
