package com.askia.coremodel.datamodel.http.service;


import com.askia.coremodel.datamodel.http.HttpUnionPayBean;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.CheckVersionData;
import com.askia.coremodel.datamodel.http.entities.GetZipData;
import com.askia.coremodel.datamodel.http.entities.LoginData;
import com.askia.coremodel.datamodel.http.entities.QueryFaceZipsUrlsData;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface NetDataService {
    // 查询人脸包下载地址
    @GET("/canteen/appControllor.do?getZipList")
    Observable<QueryFaceZipsUrlsData> queryFaceZipsUrls(@Query("timeStamp") String timeStamp);

    @FormUrlEncoded
    @POST("/sys/mLogin")
    Observable<LoginData> mLogin(@Field("username") String username, @Field("password") String password);

    @GET("/jeecg-boot/pad/checkVersion")
    Observable<CheckVersionData> checkVersion(@Query("version") String version);

    @GET("/authentication/datapreparation/layoutpack/getpackurl")
    Observable<GetZipData> getpackurl(@Query("getpackurl") String getpackurl, @Query("siteCode") String siteCode, @Query("dataVersion") String dataVersion);

    @GET("/authentication/homepage/selectpalnbysitecode")
    Observable<BaseResponseData> selectpalnbysitecode(@Query("siteCode") String siteCode);

    @POST("/authentication/verifystatistics/verifyfacecontrast")
    Observable<BaseResponseData> verifyfacecontrast(@Body RequestBody body);
}
