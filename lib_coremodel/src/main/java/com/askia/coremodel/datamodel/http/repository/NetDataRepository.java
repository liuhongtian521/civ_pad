package com.askia.coremodel.datamodel.http.repository;


import com.askia.coremodel.datamodel.http.ApiClient;
import com.askia.coremodel.datamodel.http.HttpUnionPayBean;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.CheckVersionData;
import com.askia.coremodel.datamodel.http.entities.GetZipData;
import com.askia.coremodel.datamodel.http.entities.LoginData;
import com.askia.coremodel.datamodel.http.entities.QueryFaceZipsUrlsData;
import com.askia.coremodel.datamodel.http.entities.SelectpalnbysitecodeData;
import com.askia.coremodel.datamodel.http.entities.UpLoadResult;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Query;


public class NetDataRepository {

    // 人脸包下载
    public static Observable<QueryFaceZipsUrlsData> queryFaceZipsUrls(String TimeStamp) {
        Observable<QueryFaceZipsUrlsData> responseData = ApiClient.getNetDataService()
                .queryFaceZipsUrls(TimeStamp);
        return responseData;
    }

    //登录
    public static Observable<LoginData> login(RequestBody body) {
        Observable<LoginData> responseData = ApiClient.getNetDataService()
                .mLogin(body);
        return responseData;
    }

    //更新包
    public static Observable<CheckVersionData> checkVersion(String version) {
        Observable<CheckVersionData> responseData = ApiClient.getNetDataService()
                .checkVersion(version);
        return responseData;
    }

    //数据包获取
    public static Observable<GetZipData> getpackurl(String examCode, String siteCode, String dataVersion) {
        Observable<GetZipData> responseData = ApiClient.getNetDataService()
                .getpackurl(examCode, siteCode, dataVersion);
        return responseData;
    }

    //考场数据获取
    public static Observable<SelectpalnbysitecodeData> selectpalnbysitecode(String siteCode) {
        Observable<SelectpalnbysitecodeData> responseData = ApiClient.getNetDataService()
                .selectpalnbysitecode(siteCode);
        return responseData;
    }

    //数据上传
    public static Observable<UpLoadResult> verifyfacecontrast(Map<String,String> map, MultipartBody.Part body){
        Observable<UpLoadResult> responseData = ApiClient.getNetDataService()
                .postVerifyData(map,body);
        return responseData;
    }

    public static Observable<BaseResponseData> uploadverifydetail(RequestBody body,String orgCode){
        Observable<BaseResponseData> responseData = ApiClient.getNetDataService()
                .uploadverifydetail(body,orgCode);
        return responseData;
    }

}
