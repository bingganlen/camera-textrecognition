package com.qgqaqgqa.deviceinfomanager.net;

import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.NetworkUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.internal.http.RealResponseBody;

/**
 * 处理网络异常情况
 * User: Created by 钱昱凯
 * Date: 2018/2/8 0008
 * Time: 14:23
 * Email:342744291@qq.com
 */
public class NetErrorInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (NetworkUtils.isConnected())
            return chain.proceed(chain.request());
        else {
            String res = "{\"erron\":\"-1200\",\"msg\":\"网络连接失败\",\"nowPage\":\"0\",\"totalPage\":\"0\"}";
            Response response = new Response.Builder()
            .code(200)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .addHeader("Cache-Control", "no-cache")
            .addHeader("Content-Type", "text/html; charset=utf-8")
            .body(RealResponseBody.create(MediaType.parse("text"), res.getBytes("utf-8")))
            .build();
            return response;
        }
    }
}