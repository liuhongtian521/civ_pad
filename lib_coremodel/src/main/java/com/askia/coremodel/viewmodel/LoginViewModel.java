package com.askia.coremodel.viewmodel;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;


public class LoginViewModel extends BaseViewModel{

    //account
    public ObservableField<String> account = new ObservableField<>("");
    //pwd
    public ObservableField<String> password = new ObservableField<>("");

}
