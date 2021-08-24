package com.askia.coremodel.viewmodel;

import android.util.Log;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.askia.coremodel.datamodel.http.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.CheckVersionData;
import com.askia.coremodel.datamodel.http.entities.LoginData;
import com.askia.coremodel.datamodel.http.entities.LoginUpData;
import com.askia.coremodel.datamodel.http.repository.NetDataRepository;

import java.net.SocketTimeoutException;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class LoginViewModel extends BaseViewModel {

    //account
    public ObservableField<String> account = new ObservableField<>("");
    //pwd
    public ObservableField<String> password = new ObservableField<>("");

    public MutableLiveData<LoginData> loginDate = new MutableLiveData<>();

    public MutableLiveData<LoginData> getLoginDate() {
        return loginDate;
    }

    public void login(String username, String psd) {
        LoginUpData loginUpData = new LoginUpData();
        loginUpData.setPassword(psd);
        loginUpData.setUsername(username);
        NetDataRepository.login(convertPostBody(loginUpData))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .safeSubscribe(new Observer<LoginData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(LoginData data) {
                        Log.e("TagSnake", data.getMessage());
                        loginDate.postValue(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TagSnake", Log.getStackTraceString(e));
                        LoginData rulesData = new LoginData();
                        if (e instanceof SocketTimeoutException) {
                            rulesData.setCode(ResponseCode.ConnectTimeOut);
                            rulesData.setMessage("连接超时，请重试");
                        } else {
                            rulesData.setCode(ResponseCode.ServerNotResponding);
                            rulesData.setMessage("服务器无响应，请重试");
                        }
                        loginDate.postValue(rulesData);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
