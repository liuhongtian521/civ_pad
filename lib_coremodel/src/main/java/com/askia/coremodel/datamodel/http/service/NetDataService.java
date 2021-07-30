package com.askia.coremodel.datamodel.http.service;


import com.askia.coremodel.datamodel.http.HttpUnionPayBean;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.QueryFaceZipsUrlsData;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface NetDataService
{
    // 查询人脸包下载地址
    @GET("/canteen/appControllor.do?getZipList")
    Observable<QueryFaceZipsUrlsData> queryFaceZipsUrls(@Query("timeStamp") String timeStamp);


}
