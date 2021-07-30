package com.askia.coremodel.datamodel.http;

import android.text.TextUtils;

import com.askia.coremodel.datamodel.database.repository.SharedPreUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wujiajun on 17/4/11.
 */

public class CookieInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String token = "";
      /*  if(SharedPreUtil.getInstance().getUser() != null && SharedPreUtil.getInstance().getUser()!= null)
        {
            token = SharedPreUtil.getInstance().getUser().getToken();
        }*/

        Request request = null;
        if(TextUtils.isEmpty(token))
        {
            request = original.newBuilder()
                    .header("deviceType", "android")
                    //     .header("version", ""+BuildConfig.VERSION_CODE)
                    //   .header("token", LoginManager.instance().getAccessToken())
                    .method(original.method(), original.body())
                    .build();
        }
        else
        {
            request = original.newBuilder()
                    .header("deviceType", "android")
                    .addHeader("X-Access-Token",token)
                    //     .header("version", ""+BuildConfig.VERSION_CODE)
                    //   .header("token", LoginManager.instance().getAccessToken())
                    .method(original.method(), original.body())
                    .build();
        }

        Response response = chain.proceed(request);

    /*    Response newResponse = response.networkResponse();
        if(  newResponse.body() == null)
        {
         //   newResponse.
            int id = 0;
        }*/

        return response;
    }
}
