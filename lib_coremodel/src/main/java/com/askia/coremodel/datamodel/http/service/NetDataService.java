package com.askia.coremodel.datamodel.http.service;


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
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface NetDataService {
    // 查询人脸包下载地址
    @GET("/canteen/appControllor.do?getZipList")
    Observable<QueryFaceZipsUrlsData> queryFaceZipsUrls(@Query("timeStamp") String timeStamp);

    @POST("/api/sys/mLogin")
    Observable<LoginData> mLogin(@Body RequestBody body);//@Field("username") String username, @Field("password") String password);

    @GET("/api/pad/checkVersion")
    Observable<CheckVersionData> checkVersion(@Query("version") String version);

    @GET("/api/pad/getpackurl")
    Observable<GetZipData> getpackurl(@Query("examCode") String examCode, @Query("siteCode") String siteCode, @Query("dataVersion") String dataVersion);

    //查询考试列表
    @GET("/api/pad/selectpalnbysitecode")
    Observable<SelectpalnbysitecodeData> selectpalnbysitecode(@Query("orgCode") String orgCode);

    //实时数据上传
    @Multipart
    @POST("/api/pad/importverifypack")
    Observable<UpLoadResult> postVerifyData(@HeaderMap Map<String,String> map, @Part MultipartBody.Part body);
}
