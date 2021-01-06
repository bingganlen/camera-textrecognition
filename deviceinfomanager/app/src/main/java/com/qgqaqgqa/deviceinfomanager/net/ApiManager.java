package com.qgqaqgqa.deviceinfomanager.net;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.qgqaqgqa.deviceinfomanager.finals.AppConfig;
import com.qgqaqgqa.deviceinfomanager.model.BaseModel;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.ActivityUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.AppUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.DeviceUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.EncryptUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.LogUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.NetworkUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.SPUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.StringUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.ToastUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

///////////////////////////////////////////////////////////////////////////
// Retrofit的Url组合规则
//
//    BaseUrl 和URL有关的注解中提供的值
//    最后结果
//    http://localhost:4567/path/to/other/	/post	http://localhost:4567/post
//    http://localhost:4567/path/to/other/	post	http://localhost:4567/path/to/other/post
//    http://localhost:4567/path/to/other/	https://github.com/ikidou	https://github.com/ikidou
//    从上面不能难看出以下规则：
//
//    如果你在注解中提供的url是完整的url，则url将作为请求的url。
//    如果你在注解中提供的url是不完整的url，且不以 /开头，则请求的url为baseUrl+注解中提供的值
//    如果你在注解中提供的url是不完整的url，且以 /开头，则请求的url为baseUrl的主机部分+注解中提供的值
//
///////////////////////////////////////////////////////////////////////////

/**
 * 接口管理
 * User: Created by 钱昱凯
 * Date: 2018/2/4/0004
 * Time: 18:46
 * EMail: 342744291@qq.com
 */
public class ApiManager {
    private Gson mGson;
    private ApiService service;
    private static ApiManager apiManager;

    private ApiManager() {
        mGson = new Gson();
    }

    /**
     * 单利模式
     *
     * @return
     */
    public synchronized static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    /**
     * 创建一个apiservice
     *
     * @return
     */
    public ApiService createApiService() {
        if (service == null) {
            OkHttpClient okClient = null;
            if (AppConfig.TEST_MODE) {
                okClient = new OkHttpClient.Builder()
                        //.addInterceptor(new TokenInterceptord())
                        .addInterceptor(new LoggerInterceptor(this.getClass().getSimpleName()))
                        .addNetworkInterceptor(new CacheInterceptor())
                        .build();
            } else {
                okClient = new OkHttpClient.Builder()
//                .addInterceptor(new TokenInterceptord())
//                .addInterceptor(new LoggerInterceptor(this.getClass().getSimpleName()))
//                .addNetworkInterceptor(new CacheInterceptor())
                        .build();
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConfig.ZAPI_URL)
                    .client(okClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(ApiService.class);
        }
        return service;
    }

    public void get(final Class<?> tClass, final String url, final IBaseHttpCallBack callBack) {
        get(tClass, null, url, new HashMap<String, Object>(), callBack);
    }

    public void get(final Class<?> tClass, final String url, Map<String, Object> maps, final IBaseHttpCallBack callBack) {
        get(tClass, null, url, maps, callBack);
    }

    public void getList(final Class<?> tClass, final String url, final IBaseHttpCallBack callBack) {
        get(null, tClass, url, new HashMap<String, Object>(), callBack);
    }

    public void getList(final Class<?> tClass, final String url, Map<String, Object> maps, final IBaseHttpCallBack callBack) {
        get(null, tClass, url, maps, callBack);
    }

    private void get(final Class<?> cls, final Class<?> element, final String url, Map<String, Object> maps, final IBaseHttpCallBack callBack) {
        initFlowable(createApiService().get(url, initMap(url, maps)), cls, element, url, callBack);
    }

    public void post(final String url, final IBaseHttpCallBack callBack) {
        post(url, new HashMap<String, Object>(), callBack);
    }
    public void post(final String url, Map<String, Object> maps ,final IBaseHttpCallBack callBack) {
        post(null, null, url, maps, null, callBack);
    }

    public void post(final Class<?> tClass, final String url, final IBaseHttpCallBack callBack) {
        post(tClass, null, url, new HashMap<String, Object>(), null, callBack);
    }

    public void post(final Class<?> tClass, final String url, String file, final IBaseHttpCallBack callBack) {
        post(tClass, null, url, new HashMap<String, Object>(), file, callBack);
    }

    public void post(final Class<?> tClass, final String url, Map<String, Object> maps, final IBaseHttpCallBack callBack) {
        post(tClass, null, url, maps, null, callBack);
    }

    public void post(final Class<?> tClass, final String url, Map<String, Object> maps, String file, final IBaseHttpCallBack callBack) {
        post(tClass, null, url, maps, file, callBack);
    }

    public void postList(final Class<?> tClass, final String url, final IBaseHttpCallBack callBack) {
        post(null, tClass, url, new HashMap<String, Object>(), null, callBack);
    }

    public void postList(final Class<?> tClass, final String url, String file, final IBaseHttpCallBack callBack) {
        post(null, tClass, url, new HashMap<String, Object>(), file, callBack);
    }

    public void postList(final Class<?> tClass, final String url, Map<String, Object> maps, final IBaseHttpCallBack callBack) {
        post(null, tClass, url, maps, null, callBack);
    }

    public void postList(final Class<?> tClass, final String url, Map<String, Object> maps, String file, final IBaseHttpCallBack callBack) {
        post(null, tClass, url, maps, file, callBack);
    }

    private void post(final Class<?> cls, final Class<?> element, final String url,
                      Map<String, Object> maps, final IBaseHttpCallBack callBack) {
        post(cls, element, url, maps, null, callBack);
    }

    private void post(final Class<?> cls, final Class<?> element, final String url,
                      Map<String, Object> maps, String file, final IBaseHttpCallBack callBack) {
        maps = initMap(url, maps);
        if (file != null) {
            LogUtils.e(file);
            Map<String, RequestBody> fileUpload2Args = new HashMap<>();
            for (String key : maps.keySet()) {
                fileUpload2Args.put(key, convertToRequestBody((String) maps.get(key)));
            }
            String[] files = file.split(",");
            if(files.length==1){
                initFlowable(createApiService().post(url, fileUpload2Args,
                        filesToMultipartBodyParts("file", new File(files[0]))), cls, element, url, callBack);
//                initFlowable(createApiService().post(url, fileUpload2Args,
//                        filesToMultipartBodyParts("fileBase64", new File(file))), cls, element, url, callBack);
            }else{
                List<File> fileList =new ArrayList<>();
                for(String f:files)
                    if(!f.isEmpty())
                    fileList.add(new File(f));
                initFlowable(createApiService().post(url, fileUpload2Args,
                        filesToMultipartBodyParts(fileList)), cls, element, url, callBack);

            }
        } else {
            initFlowable(createApiService().post(url, maps), cls, element, url, callBack);
        }
    }


    private Map<String, Object> initMap(String url, Map<String, Object> maps) {
        if (maps == null) {
            maps = new HashMap<>();
        }
////        maps.put("__login", "true");
//        maps.put("__ajax", "json");
//        maps.put("__sid", SPUtils.getInstance().getString(AppConfig.SP_SESSIONID,""));
//
////        maps.put(UcenterLoginMembersSerializable.MEMBER_TOKEN,
////                SPUtils.getInstance().getString(UcenterLoginMembersSerializable.MEMBER_TOKEN));
////        if(NetworkUtils.isConnected()){
////            NetworkUtils.getIPAddress(true);
////            DeviceUtils.getMacAddress();
////            DeviceUtils.getSDKVersionName();
////            DeviceUtils.getSDKVersionCode();
////            AppUtils.getAppPackageName();
////            AppUtils.getAppName();
////            AppUtils.getAppVersionName();
//
////        }
//        String deviceStartTime = SPUtils.getInstance().getString("deviceStartTime");
//        if (StringUtils.isEmpty(deviceStartTime)) {
//            deviceStartTime = System.currentTimeMillis() + "";
//            SPUtils.getInstance().put("deviceStartTime", deviceStartTime);
//        }
//        maps.put("clientDeviceNum", EncryptUtils.encryptMD5ToString(
//                DeviceUtils.getAndroidID() + Build.SERIAL + deviceStartTime));
//        maps.put("andrdoidId", DeviceUtils.getAndroidID());
//        maps.put("clientOs", "android");
//        maps.put("clientType", "android");
//        maps.put("clientAppVerison", AppUtils.getAppVersionName());
//        maps.put("clientIp", NetworkUtils.getIPAddress(true));
//
//        maps.put("param_deviceType", "mobileApp");
        LogUtils.e(url, maps);
        return maps;
    }

    private void initFlowable(Flowable<ResponseBody> flowable, final Class<?> cls, final Class<?> element, final String url, final IBaseHttpCallBack callBack) {
        flowable.subscribeOn(Schedulers.newThread())
                .map(new Function<ResponseBody, BaseModel>() {
                    @Override
                    public BaseModel apply(ResponseBody body) throws Exception {
                        BaseModel bm = null;
                        try {
                            String data = body.string();
                            LogUtils.e(data);
//                            if(cls==null&&element==null){
//                                bm=new BaseModel();
//                                bm.setData(data);
////                                bm.setResult("true");
//                            }else{
                                bm = parseData(data, cls, element);
//                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return bm;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiSubscriberCallBack<BaseModel>() {
                    @Override
                    public void onSuccess(BaseModel bm) {
//                        if (bm.isSuccess()) {//获取数据成功
//                            if (callBack != null) {
//                                if (element != null) {
//                                    if (bm.getData() == null) {
//                                        bm.setData(new ArrayList<>());
//                                    }
//                                }
//                                callBack.onSucess(url, bm);
//                            }
////                        }else if(bm.getErrcode()==500){
//                        } else
//                            if ("login".equals(bm.getResult())) {//账号异常请重新登录
//                                if(SPUtils.getInstance().getString(AppConfig.SP_SESSIONID,"").isEmpty())
//                                    return;
//                            SPUtils.getInstance().remove(AppConfig.SP_SESSIONID);
//                            ToastUtils.showShort(bm.getMessage());
//                            ActivityUtils.startActivityForResult(ActivityUtils.getTopActivity(),LoginActivity.class,AppConfig.REQUEST_LOGIN);
////                        } else if (!bm.isSuccess()) {//弹出消息
////                            ToastUtils.showShort(bm.getMessage());
//                        } else {
                            if (callBack != null) {
                                if (element != null) {
                                    if (bm.getData() == null) {
                                        bm.setData(new ArrayList<>());
                                    }
                                }
                                callBack.onSucess(url, bm);
                            }
//                        }
                        if (callBack != null) {
                            callBack.onFinish();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.e(t);
                        if (callBack != null) {
                            callBack.onFinish();
                        }
                        ToastUtils.showLong("网络连接失败");
                    }
                });
    }

    /**
     * 数据解析，如果需要解析，则重写这个方法
     *
     * @return
     */
    public BaseModel parseData(String data, Class<?> cls, Class<?> element) throws JsonSyntaxException {
        BaseModel base;
        if('['==data.charAt(0)){

            base=new BaseModel();
            base.setData(data);
        }else{

            //fromJson()方法用于将JSON数据转换为相应的Java对象
            //或者把json格式的data转化成BaseModel类型
            //基本信息（即basemodel中信息，只附带成功和失败）
            base = mGson.fromJson(data, BaseModel.class);
//        if (base.isSuccess()) {
            Type t = null;
            if (cls != null) {
                t = com.google.gson.internal
                        .$Gson$Types
                        .newParameterizedTypeWithOwner(null, BaseModel.class, cls);
                return mGson.fromJson(data, t);
            } else if (element != null) {
                t = com.google.gson.internal
                        .$Gson$Types
                        .newParameterizedTypeWithOwner(
                                null,
                                BaseModel.class,
                                com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, element)
                        );
                return mGson.fromJson(data, t);
            }else {
                base.setData(data);
            }
        }
        return base;
    }
    public <T> T fromJson(String json, Type typeOfT) {
        return mGson.fromJson(json, typeOfT);
    }
    public <T> T fromListJson(String json, Class<?> element) {
        Type t = com.google.gson.internal
                .$Gson$Types
                .newParameterizedTypeWithOwner(
                        null,
                        ArrayList.class, element
                );
        return mGson.fromJson(json, t);
    }

    public static RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }

    public static MultipartBody.Part filesToMultipartBodyParts(File file) {
        return filesToMultipartBodyParts("file", file);
//        return filesToMultipartBodyParts("multipartFiles", file);
    }

    public static MultipartBody.Part filesToMultipartBodyParts(String name, File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        return MultipartBody.Part.createFormData(name, file.getName(), requestBody);
    }

    public static List<MultipartBody.Part> filesToMultipartBodyParts(List<File> files) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            parts.add(filesToMultipartBodyParts(file));
        }
        return parts;
    }

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public static String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG, 40, bos);//第二个入参表示图片压缩率，如果是100就表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

}