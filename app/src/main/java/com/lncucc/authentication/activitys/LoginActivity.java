package com.lncucc.authentication.activitys;

import android.widget.EditText;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.utils.TextUtils;
import com.askia.common.base.ARouterPath;
import com.askia.common.base.BaseActivity;
import com.askia.common.util.MyToastUtils;
import com.askia.coremodel.viewmodel.LoginViewModel;
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
    }

    @Override
    public void onInitViewModel() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    @Override
    public void onInitDataBinding() {
        loginBinding = DataBindingUtil.setContentView(this, R.layout.act_login);
        loginBinding.setClick(new ProxyClick());
        loginBinding.setViewmodel(loginViewModel);
    }

    @Override
    public void onSubscribeViewModel() {
    }

    private void doLogin(){
        LogUtils.e("do login!");
        startActivityByRouter(ARouterPath.MANAGER_SETTING_ACTIVITY);
    }

    public class ProxyClick {
        public void login(){
            if (TextUtils.isEmpty(loginViewModel.account.get())){
                MyToastUtils.error("请输入用户名！",0);
                return;
            }
            if (TextUtils.isEmpty(loginViewModel.password.get())){
                ToastUtils.showShort("请输入密码！");
                return;
            }
            doLogin();
        }

        public void openEyes(){
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
