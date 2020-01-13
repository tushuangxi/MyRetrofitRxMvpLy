package com.lding.pad.myseial.libding.http;

import com.lding.pad.myseial.libding.entity.GetListRsp;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;


/*
        使用OkHttp3在Android P 出现的错误：CLEARTEXT communication to host not permitted by network

        使用OkHttp3做网络请求框架时，如果是http请求而非https请求，会导致请求失败，因为Android P之后系统限制了明文的网络请求，非加密请求会被系统禁止掉。
        同样如果您使用了WebView加载http协议下的页面，也会出现加载失败，https则不受影响。
 */

public interface ApiService {

    String APP_HOST = "http://api.zhuishushenqi.com";//正式接口
    String FUNCTION_INDEX = "/cats/lv2/statistics/";
    String FUNCTION_LOGIN =  "/cats/lv2/statistics/";

    /*登录  GetListRsp  */
    @GET(FUNCTION_LOGIN)
    Observable<GetListRsp> getGetListRsp(@QueryMap Map<String, String> params);
}
