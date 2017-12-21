package com.example.wpx.framework.http.ApiService;

import java.util.Map;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * <h3>Description</h3>
 * TODO
 * <h3>Author</h3> （王培学）
 * <h3>Date</h3> 2017/4/28 12:44
 * <h3>Copyright</h3> Copyright (c)2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */

public interface BaseApiService {

    /******************************************************************Get请求*****************************************************/
    /**
     * Map表单方式get请求必须加泛型String 无参数get请求的时候Map传null
     *
     * @param method
     * @param pargrams
     * @return
     */
    @GET("{method}")
    Observable<ResponseBody> executeGet(@Path("method") String method, @QueryMap Map<String,String> pargrams);

    /**
     * 下载文件(例如下载APK文件)
     *
     * @param fileUrl
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    /******************************************************************Post请求*****************************************************/
    /**
     * Map表单方式post请求 无参数post请求的时候Map传null
     *
     * @param method
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST("{method}")
    Observable<ResponseBody> executePost(@Path("method") String method, @FieldMap Map<String, String> fields);

    /**
     * 实体Json方式post请求
     *
     * @param method
     * @param body
     * @return
     */
    @POST("{method}")
    Observable<ResponseBody> executeJson(@Path("method") String method, @Body RequestBody body);

    /**
     * 单文件上传post请求
     *
     * @param description
     * @param file
     * @return
     */
    @Multipart
    @POST("{method}")
    Observable<ResponseBody> upLoadFile(@Path("method") String method, @Part("description") RequestBody description, @Part MultipartBody.Part file);

    /**
     * 不定个数多文件上传post请求
     *
     * @param url
     * @param headers
     * @param description
     * @param maps
     * @return
     */
    @POST("{method}")
    Observable<ResponseBody> uploadFiles(
            @Path("method") String url,
            @Path("headers") Map<String, String> headers,
            @Part("description") String description,
            @PartMap() Map<String, RequestBody> maps);

}
