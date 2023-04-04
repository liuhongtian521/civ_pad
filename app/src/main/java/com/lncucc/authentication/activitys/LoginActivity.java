package com.lncucc.authentication.activitys;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.utils.TextUtils;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.base.HandleEvent;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.event.UniAuthInfoEvent;
import com.askia.coremodel.util.NetUtils;
import com.askia.coremodel.util.SignUtils;
import com.askia.coremodel.util.Utils;
import com.askia.coremodel.viewmodel.LoginViewModel;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActLoginBinding;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.ErrorDialog;
import com.lncucc.authentication.widgets.IPSettingDialog;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

/**
 * LoginActivity
 */
@Route(path = ARouterPath.LOGIN_ACTIVITY)
public class LoginActivity extends BaseActivity implements DialogClickBackListener {

    private ActLoginBinding loginBinding;
    private LoginViewModel loginViewModel;
    private EditText txtPassword;
    private ImageView imageView;
    //ip setting
    private IPSettingDialog dialog;

    @Override
    public void onInit() {
        RxBus2.getInstance().register(this);
        txtPassword = findViewById(R.id.edt_pwd);
        imageView = findViewById(R.id.iv_pwd_switch);
        String defaultAccount = SharedPreferencesUtils.getString(this, "account", "");
        dialog = new IPSettingDialog(this,this);
        loginViewModel.account.set(defaultAccount);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onInitViewModel() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }
    @org.greenrobot.eventbus.Subscribe
    public  void  onFaceInitFailureEvent(HandleEvent event){
        ErrorDialog errorDialog = new ErrorDialog(this, "人脸认证SDK初始化失败，请确认是否连接互联网，之后重启APP");
        errorDialog.show();
    }
    @Override
    public void onInitDataBinding() {
        loginBinding = DataBindingUtil.setContentView(this, R.layout.act_login);
        loginBinding.setViewmodel(loginViewModel);
        loginBinding.setClick(new ProxyClick());
    }


    @Subscribe(receiveStickyEvent = true)
    public void onReceiveUserInfoEvent(UniAuthInfoEvent event) {
        //uni跳过过来后，执行鉴权流程
        loginViewModel.account.set(event.getUserName());
        loginViewModel.password.set(event.getPassWord());
        doLogin();
    }

    @Override
    public void onSubscribeViewModel() {

        loginViewModel.getLoginDate().observe(this, loginData -> {
            if (loginData.isSuccess()) {
                SharedPreferencesUtils.putString(getApplicationContext(), "account", loginViewModel.account.get());
                SharedPreferencesUtils.putString(getApplicationContext(), "password", loginViewModel.password.get());
                SharedPreferencesUtils.putString(getApplicationContext(), "code", loginData.getResult().getUserInfo().getOrgCode());
                Bundle _b = new Bundle();
                _b.putString("code", loginData.getResult().getUserInfo().getOrgCode());
                _b.putInt("type", 1);
                startActivityByRouter(ARouterPath.INITIALIZE_ACTIVITY, _b);
                finish();
            } else {
                MyToastUtils.error("账号密码错误！", Toast.LENGTH_SHORT);
            }
        });
    }

    private void doLogin() {
        String userName = loginViewModel.account.get();
        String password = loginViewModel.password.get();
        //添加内置管理员账号
        if ("SFYZadmin".equals(userName) && "SFYA@sfyz".equals(password)){
            toMainOrManagerActivity(userName,password);
            return;
        }
        // 需求1.3.2 添加本地数据库账号密码登录逻辑。
        // 数据来源-> 数据包导入时account.json文件 DBAccount文件
        if (DBOperation.isMatchingWithLocal(userName,password)){
            SharedPreferencesUtils.putString(getApplicationContext(), "code", DBOperation.queryOrgCode());
            toMainOrManagerActivity(userName,password);
            return;
        }
        //有网络联网登录，没有网络尝试读取本地缓存
        if (NetUtils.isNetConnected()) {
            loginViewModel.login(userName, SignUtils.encryptByPublic(password));
        } else {
            String account = SharedPreferencesUtils.getString(this, "account", userName);
            String passwordLocal = SharedPreferencesUtils.getString(this, "password", password);
            //本地账号密码登录
            if (account.equals(userName) && passwordLocal.equals(password)) {
                toMainOrManagerActivity(userName,password);
            } else {
                MyToastUtils.error("账号密码错误！", Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    public void dissMiss() {
        KeyboardUtils.toggleSoftInput();
    }

    @Override
    public void backType(int type) {
        if (type == 0 && dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            KeyboardUtils.toggleSoftInput();
        }
    }

    private void toMainOrManagerActivity(String userName,String pwd){
        SharedPreferencesUtils.putString(this, "account", userName);
        SharedPreferencesUtils.putString(this, "password", pwd);
        if (DBOperation.getDBExamArrange() != null && DBOperation.getDBExamArrange().size() > 0) {
            startActivityByRouter(ARouterPath.MAIN_ACTIVITY);
        } else {
            startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
        }
        finish();
    }

    public class ProxyClick {
        public void login() {
            if (TextUtils.isEmpty(loginViewModel.account.get())) {
                MyToastUtils.error("请输入用户名！", 0);
                return;
            }
            if (TextUtils.isEmpty(loginViewModel.password.get())) {
                MyToastUtils.error("请输入密码！",0);
                return;
            }
            if (!Utils.doubleClick()){
                doLogin();
            }
        }

        public void openEyes() {
            if (txtPassword.getInputType() == 128) {
                txtPassword.setInputType(129);
                imageView.setBackgroundResource(R.mipmap.icon_pwd_show);
            } else {
                txtPassword.setInputType(128);
                imageView.setBackgroundResource(R.mipmap.icon_pwd_hide);
            }
            txtPassword.setSelection(Objects.requireNonNull(loginViewModel.password.get()).length());
        }

        public void setHost(){
            if (dialog != null){
                dialog.show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus2.getInstance().unRegister(this);
        EventBus.getDefault().unregister(this);
    }

}
