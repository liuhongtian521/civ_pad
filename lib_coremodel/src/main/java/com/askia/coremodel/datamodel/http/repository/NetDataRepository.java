package com.askia.coremodel.datamodel.http.repository;



import com.askia.coremodel.datamodel.http.ApiClient;
import com.askia.coremodel.datamodel.http.HttpUnionPayBean;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.CheckVersionData;
import com.askia.coremodel.datamodel.http.entities.LoginData;
import com.askia.coremodel.datamodel.http.entities.QueryFaceZipsUrlsData;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class NetDataRepository
{

    // 人脸包下载
    public static Observable<QueryFaceZipsUrlsData> queryFaceZipsUrls(String TimeStamp) {
        Observable<QueryFaceZipsUrlsData> responseData = ApiClient.getNetDataService()
                .queryFaceZipsUrls(TimeStamp);
        return responseData;
    }

    //登录
    public static  Observable<LoginData> login(String user,String psd){
        Observable<LoginData> responseData = ApiClient.getNetDataService()
                .mLogin(user, psd);
        return responseData;
    }

    //更新包
    public static Observable<CheckVersionData> checkVersion(String version){
        Observable<CheckVersionData> responseData = ApiClient.getNetDataService()
                .checkVersion(version);
        return responseData;
    }

}
