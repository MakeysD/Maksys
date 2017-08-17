package com.example.duan.chao.DCZ_util;


import com.example.duan.chao.DCZ_bean.EquipmentBean;
import com.example.duan.chao.DCZ_bean.ExitBean;
import com.example.duan.chao.DCZ_bean.Footprints2Bean;
import com.example.duan.chao.DCZ_bean.FootprintsBean;
import com.example.duan.chao.DCZ_bean.LoginBean;
import com.example.duan.chao.DCZ_bean.LoginOkBean;
import com.example.duan.chao.DCZ_bean.NewsBean;
import com.example.duan.chao.DCZ_bean.OperationRecordBean;
import com.example.duan.chao.DCZ_bean.SecurityBean;
import com.example.duan.chao.DCZ_bean.StatusBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface InterfaceService {
    @GET("https://api.bincrea.com/app/free/getMultimediaList.do")
    Call<StatusBean> getpicpage();

/*    @Headers({"Content-Type: application/json","Accept: application/json,"})//json数据需要添加头
    @POST("https://api.bincrea.com/app/entrust/addEntrustInfo.do")
    Call<StatusBean<>> set_json(@Body RequestBody body);*/

    @FormUrlEncoded
    @POST("sms/code")
    Call<StatusBean> jiekou(@Field("mobile") String string);

    /**
     * 资讯列表
     * */
    @FormUrlEncoded
    @POST("https://api.bincrea.com/app/info/getInfoList.do")
    Call<NewsBean> getNews(@Field("searchKey") String key, @Field("pageNumber") int number, @Field("pageSize") int size);


    @FormUrlEncoded
   // @Headers({"Accept-Language:en"})
    @POST("http://192.168.2.189:8081/api/login")
    Call<StatusBean> login2(@Header("Accept-Language") String en, @Field("username") String string, @Field("password") String string2, @Field("code") String string3);

    /**
     *  登录
     * */
    @FormUrlEncoded
    @POST("login")
    Call<LoginOkBean> login(@Field("username") String username,
                            @Field("password") String password,
                            @Field("code") String code,
                            @Field("publicKey")String publicKey,
                            @Field("deviceUUID") String deviceUUID,
                            @Field("deviceName") String deviceName,
                            @Field("registrationId")String registrationId);

    /**
     *  退出登录
     */
    @FormUrlEncoded
    @POST("logout")
    Call<LoginBean> exit_login(@Field("deviceUUID") String deviceUUID);

    /**
     *  修改登录密码
     */
    @FormUrlEncoded
    @POST("user/changePwd ")
    Call<LoginOkBean>login_password(@Field("code") String code,
                                    @Field("oldPwd") String oldPwd,
                                    @Field("newPwd")String newPwd,
                                    @Field("confirmNewPwd")String confirmNewPwd);

    /**
     *  修改安全密码
     */
    @FormUrlEncoded
    @POST("user/updateSecPwd")
    Call<LoginOkBean>anquan_password(@Field("oldSecPwd") String oldSecPwd,
                                   @Field("newSecPwd") String newSecPwd,
                                   @Field("confirmNewSecPwd")String confirmNewSecPwd,
                                   @Field("code")String code);

    /**
     *  校验登录密码
     */
    @FormUrlEncoded
    @POST("user/checkLoginPwd")
    Call<LoginOkBean> checklogin(@Field("username") String username,
                                      @Field("password")String password);

    /**
     *  发送短信
     */
    @FormUrlEncoded
    @POST("user/sendSMS")
    Call<EquipmentBean> sendsms(@Field("mobile") String mobile,
                                   @Field("areaCode")String areaCode);

    /**
     *  设备列表
     */
    @FormUrlEncoded
    @POST("device/getAuthorizedList")
    Call<EquipmentBean> getEquipent(@Field("deviceUUID") String deviceUUID);

    /**
     *  删除设备
     */
    @FormUrlEncoded
    @POST("device/delete")
    Call<EquipmentBean> deleteEquipent(@Field("id") Long id);

    /**
     *  刷新Token
     */
    @FormUrlEncoded
    @POST("loginByRefreshToken")
    Call<EquipmentBean> getToken(@Field("username") String id,
                                 @Field("refreshToken") Long refreshToken,
                                 @Field("deviceUUID") Long deviceUUID);

    /**
     *  账户足迹
     */
    @FormUrlEncoded
    @POST("getLatestLoginLogList")
    Call<FootprintsBean> getFoot(@Field("pageNumber") int pageNumber,
                                 @Field("pageSize") int pageSize,
                                 @Field("orderBy") String orderBy,
                                 @Field("startTime") String startTime,
                                 @Field("endTime") String endTime);

    /**
     *  获取用户在各子系统的在线情况
     */
    @FormUrlEncoded
    @POST("getUserOnLineList ")
    Call<Footprints2Bean> getOnline(@Field("username") String username,
                                    @Field("nonce") String nonce);

    /**
     *  踢出用户在某个系统的登录情况
     */
    @FormUrlEncoded
    @POST("kickout ")
    Call<FootprintsBean> kickout(@Field("systemId") String systemId,
                                 @Field("username")String username,
                                 @Field("sessionid")String sessionid,
                                @Field("nonce") String nonce);

    /**
     *  操作记录
     */
    @FormUrlEncoded
    @POST("getOperationList")
    Call<OperationRecordBean> getOperation(@Field("startTime") String startTime,
                                           @Field("endTime")String endTime);

    /**
     *  安全保护列表
     */
    @FormUrlEncoded
    @POST("safe/getProtectedSystemList")
    Call<SecurityBean> protect(@Field("deviceUUID") String deviceUUID);

    /**
     *  开启或关闭保护（1开启，2关闭）
     */
    @FormUrlEncoded
    @POST("safe/updateProtectedEnable")
    Call<EquipmentBean> updateProtect(@Field("id") Long id,
                                     @Field("enable")String enable);

    /**
     *  授权
     */
    @FormUrlEncoded
    @POST("safe/authorize")
    Call<LoginOkBean> have(@Field("username") String username,
                            @Field("reqSysId") String reqSysId,
                           @Field("srcReqSysId")String srcReqSysId,
                            @Field("reqFlowId") String reqFlowId,
                            @Field("agreement") String agreement,
                           @Field("sign") String sign);

}
