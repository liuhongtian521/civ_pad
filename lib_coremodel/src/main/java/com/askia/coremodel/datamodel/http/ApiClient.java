package com.askia.coremodel.datamodel.http;


import com.askia.coremodel.datamodel.http.service.NetDataService;
import com.blankj.utilcode.util.LogUtils;

import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


public class ApiClient {

    /**
     * 获取指定数据类型
     *
     * @return
     */
    public static NetDataService getNetDataService() {
        String host = "";
        if (host == null || host.trim().equals("")) {
            host = ApiConstants.HOST;
        }
//        String host = ApiConstants.GankHost;
        NetDataService netDataService = initService(host, NetDataService.class);
        return netDataService;
    }

      /**
     * 动态url获取数据
     *
     * @return
     */
/*    public static DynamicApiService getDynamicDataService() {

        DynamicApiService dynamicApiService = ApiClient.initService("", DynamicApiService.class);

        return dynamicApiService;
    }*/

    /**
     * 获得想要的 retrofit service
     *
     * @param baseUrl 数据请求url
     * @param clazz   想要的 retrofit service 接口，Retrofit会代理生成对应的实体类
     * @param <T>     api service
     * @return
     */
    private static <T> T initService(String baseUrl, Class<T> clazz) {
        return getRetrofitInstance(baseUrl).create(clazz);
    }

    /**
     * 单例retrofit
     */
    private static Retrofit retrofitInstance;

    private static Retrofit getRetrofitInstance(String baseUrl) {
            synchronized (ApiClient.class) {
                if (retrofitInstance == null || !baseUrl.equals(retrofitInstance.baseUrl().toString()))
                {
                    LogUtils.d("retrofit 对象为null 或检测到了baseurl变更");
                    try {
                        retrofitInstance = new Retrofit.Builder()
                                .baseUrl(baseUrl)
                                //设置数据解析器
                                .addConverterFactory(MyGsonConverterFactory.create())
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                .client(getOkHttpClientInstance())
                                .build();
                    }
                    catch (Exception e)
                    {

                    }

                }
        }
        return retrofitInstance;
    }



    /**
     * 单例OkHttpClient
     */
    private static OkHttpClient okHttpClientInstance;

    private static OkHttpClient getOkHttpClientInstance() {
        if (okHttpClientInstance == null) {
            synchronized (ApiClient.class) {
                if (okHttpClientInstance == null) {
                    //modify 适配动态切换baseUrl,使用RetrofitUrlManager进行获取
                    OkHttpClient.Builder builder = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder());
                    builder.connectTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS);
                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    builder.addInterceptor(httpLoggingInterceptor);
                    builder.addInterceptor(new ResponseInterceptor());
                    builder.addInterceptor(new CookieInterceptor());
                    builder.sslSocketFactory(HttpUtils.getSslSocketFactory(null,null,null));
                    builder.hostnameVerifier((hostname, session) -> true);
                    okHttpClientInstance = builder.build();
                }
            }
        }
        return okHttpClientInstance;
    }

}
