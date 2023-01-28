package com.lncucc.authentication.activitys;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.utils.TextUtils;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.event.UniAuthInfoEvent;
import com.askia.coremodel.util.NetUtils;
import com.askia.coremodel.util.Utils;
import com.askia.coremodel.viewmodel.LoginViewModel;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActLoginBinding;
import com.askia.coremodel.util.SignUtils;
import com.lncucc.authentication.widgets.DialogClickBackListener;
import com.lncucc.authentication.widgets.IPSettingDialog;
import com.ttsea.jrxbus2.RxBus2;
import com.ttsea.jrxbus2.Subscribe;

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
    }

    @Override
    public void onInitViewModel() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
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
//            loginBinding.btnLogin.setEnabled(true);
        });
    }

    private void doLogin() {
        //添加内置管理员账号
        if ("SFYZadmin".equals(loginViewModel.account.get()) && "SFYA@sfyz".equals(loginViewModel.password.get())){
            if (DBOperation.getDBExamArrange() != null && DBOperation.getDBExamArrange().size() > 0) {
                startActivityByRouter(ARouterPath.MAIN_ACTIVITY);
            } else {
                startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
            }
            finish();
            return;
        }
        //有网络联网登录
        if (NetUtils.isNetConnected()) {
            String pwd = SignUtils.encryptByPublic(loginViewModel.password.get());
            loginViewModel.login(loginViewModel.account.get(), pwd);
        } else {
            String account = SharedPreferencesUtils.getString(this, "account", loginViewModel.account.get());
            String password = SharedPreferencesUtils.getString(this, "password", loginViewModel.password.get());
            //本地账号密码登录
            if (account.equals(loginViewModel.account.get()) && password.equals(loginViewModel.password.get())) {
                if (DBOperation.getDBExamArrange() != null && DBOperation.getDBExamArrange().size() > 0) {
                    startActivityByRouter(ARouterPath.MAIN_ACTIVITY);
                } else {
                    startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
                }
                finish();
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
    }
}
