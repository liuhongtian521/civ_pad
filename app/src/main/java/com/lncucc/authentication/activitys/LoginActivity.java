package com.lncucc.authentication.activitys;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.utils.TextUtils;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.datamodel.database.operation.DBOperation;
import com.askia.coremodel.datamodel.http.entities.LoginData;
import com.askia.coremodel.util.NetUtils;
import com.askia.coremodel.viewmodel.LoginViewModel;
import com.baidu.tts.tools.SharedPreferencesUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lncucc.authentication.R;
import com.lncucc.authentication.databinding.ActLoginBinding;

import java.util.Objects;

/**
 * LoginActivity
 */
@Route(path = ARouterPath.LOGIN_ACTIVITY)
public class LoginActivity extends BaseActivity {

    private ActLoginBinding loginBinding;
    private LoginViewModel loginViewModel;
    private EditText txtPassword;
    private ImageView imageView;

    @Override
    public void onInit() {
        txtPassword = findViewById(R.id.edt_pwd);
        imageView = findViewById(R.id.iv_pwd_switch);
        String defaultAccount = SharedPreferencesUtils.getString(this, "account", "");
        loginViewModel.account.set("admin");
        loginViewModel.password.set("123456");
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

    @Override
    public void onSubscribeViewModel() {

        loginViewModel.getLoginDate().observe(this, new Observer<LoginData>() {
            @Override
            public void onChanged(LoginData loginData) {
                Log.e("TagSnake", loginData.getMessage() + loginData.isSuccess());
                if (loginData.isSuccess()) {
                    Log.e("TagSnake", loginData.getResult().getUserInfo().toString());
                }

                if (loginData.isSuccess()) {
                    SharedPreferencesUtils.putString(getApplicationContext(), "account", loginViewModel.account.get());
                    Bundle _b = new Bundle();
                    _b.putString("code", loginData.getResult().getUserInfo().getOrgCode());
                    startActivityByRouter(ARouterPath.INITIALIZE_ACTIVITY, _b);
                    finish();
                }
            }
        });
    }

    private void doLogin() {

        if (DBOperation.getDBExamArrange() != null && DBOperation.getDBExamArrange().size() > 0) {
            startActivityByRouter(ARouterPath.MAIN_ACTIVITY);
        } else {
            startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
        }
        finish();


        //有网络联网登录
        if (NetUtils.isNetConnected()) {
            loginViewModel.login(loginViewModel.account.get(), loginViewModel.password.get());
//            SharedPreferencesUtils.putString(this, "account", loginViewModel.account.get());
//            startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
//            finish();
        } else {
            //本地账号密码登录
            if ("admin".equals(loginViewModel.account.get()) && "123456".equals(loginViewModel.password.get())) {
                //登录成功，存本次登录账号
                SharedPreferencesUtils.putString(this, "account", loginViewModel.account.get());

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

    public class ProxyClick {
        public void login() {
            if (TextUtils.isEmpty(loginViewModel.account.get())) {
                MyToastUtils.error("请输入用户名！", 0);
                return;
            }
            if (TextUtils.isEmpty(loginViewModel.password.get())) {
                ToastUtils.showShort("请输入密码！");
                return;
            }
            doLogin();
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
    }
}
