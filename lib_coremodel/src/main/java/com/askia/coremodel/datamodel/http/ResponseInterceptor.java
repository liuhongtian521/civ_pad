package com.askia.coremodel.datamodel.http;


import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.event.AuthenticationEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.ttsea.jrxbus2.RxBus2;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if ( originalResponse.code() == 401) {
           RxBus2.getInstance().post(new AuthenticationEvent());

        }
        return originalResponse;
    }
}
