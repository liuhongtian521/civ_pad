package com.askia.coremodel.viewmodel;


import androidx.lifecycle.ViewModel;

import com.askia.coremodel.datamodel.http.params.BaseRequestParams;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BaseViewModel extends ViewModel
{
    public final CompositeDisposable mDisposable = new CompositeDisposable();
    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }

    public RequestBody convertPostBody(BaseRequestParams param)
    {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        //  param.setRequestUrl("/TestUsers/restwebservice/app/webservicevalidate/gettransactionpublickey");
        String route= gson.toJson(param);
        try {
            route = route.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            route = route.replaceAll("\\+", "%2B");
            route = URLDecoder.decode(route, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json"),route);
        return body;
    }

    public RequestBody convertFormPostBody(BaseRequestParams param)
    {

        String route = "";
        Class cls = param.getClass();
        Field[] fields = cls.getDeclaredFields();
        for(int i=0; i<fields.length; i++){
            Field f = fields[i];
            f.setAccessible(true);
            try {
                route = route + f.getName() + "=" + f.get(param) + "&";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        try {
            route = route.substring(0,route.length() - 1);
            route = route.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            route = route.replaceAll("\\+", "%2B");
            route = URLDecoder.decode(route, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/x-www-form-urlencoded"),route);
        LogUtils.d("netInfo"," post body is :" + route);
        return body;
    }

    public RequestBody arrayConvertPostBody(List<String> array)
    {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        //  param.setRequestUrl("/TestUsers/restwebservice/app/webservicevalidate/gettransactionpublickey");
        String route= gson.toJson(array);
        try {
            route = route.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
            route = route.replaceAll("\\+", "%2B");
            route = URLDecoder.decode(route, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json"),route);
        return body;
    }

    public RequestBody toRequestBodyOfText(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    public RequestBody toRequestBodyOfImage(File pFile) {

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), pFile);
        return fileBody;
    }

}
