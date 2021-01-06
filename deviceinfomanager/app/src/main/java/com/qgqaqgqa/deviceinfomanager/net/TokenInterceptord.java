package com.qgqaqgqa.deviceinfomanager.net;

import android.text.TextUtils;

import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.SPUtils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 处理apptoken等公共参数
 * User: Created by 钱昱凯
 * Date: 2018/2/8 0008
 * Time: 14:22
 * Email:342744291@qq.com
 */
public class TokenInterceptord implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        if (canInjectIntoBody(request)) {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            formBodyBuilder.add("apptoken", SPUtils.getInstance().getString("apptoken"));
            RequestBody formBody = formBodyBuilder.build();
            String bodyString = bodyToString(request.body());
            bodyString += ((bodyString.length() > 0) ? "&" : "") + bodyToString(formBody);
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8");
            requestBuilder.method(request.method(), RequestBody.create(mediaType, bodyString));
        }
        request = requestBuilder.build();
        return chain.proceed(request);
    }

    private boolean canInjectIntoBody(Request request) {
        if (request == null) {
            return false;
        }

        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }

        RequestBody body = request.body();
        if (body == null) {
            return false;
        }

        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return false;
        }

        if (!TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded")) {
            return false;
        }
        return true;
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}