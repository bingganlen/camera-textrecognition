package com.qgqaqgqa.deviceinfomanager.net;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * 所有接口的入口
 * User: Created by 钱昱凯
 * Date: 2018/2/4/0004
 * Time: 18:40
 * EMail: 342744291@qq.com
 */
public interface ApiService {
    ///////////////////////////////////////////////////////////////////////////
    // Retrofit的Url组合规则
    // BaseUrl 和URL有关的注解中提供的值
    // 最后结果
    // http://localhost:4567/path/to/other/	/post	http://localhost:4567/post
    // http://localhost:4567/path/to/other/	post	http://localhost:4567/path/to/other/post
    // http://localhost:4567/path/to/other/	https://github.com/ikidou	https://github.com/ikidou
    // 从上面不能难看出以下规则：
    //
    // 如果你在注解中提供的url是完整的url，则url将作为请求的url。
    // 如果你在注解中提供的url是不完整的url，且不以 /开头，则请求的url为baseUrl+注解中提供的值
    // 如果你在注解中提供的url是不完整的url，且以 /开头，则请求的url为baseUrl的主机部分+注解中提供的值
    ///////////////////////////////////////////////////////////////////////////

    @GET()
    Flowable<ResponseBody> get(@Url String url, @QueryMap Map<String, Object> maps);

    /**
     * 注意:
     * 1.如果方法的泛型指定的类不是ResonseBody,retrofit会将返回的string成用json转换器自动转换该类的一个对象,转换不成功就报错.
     *  如果不需要gson转换,那么就指定泛型为ResponseBody,
     *  只能是ResponseBody,子类都不行,同理,下载上传时,也必须指定泛型为ResponseBody
     * 2. map不能为null,否则该请求不会执行,但可以size为空.
     * 3.使用@url,而不是@Path注解,后者放到方法体上,会强制先urlencode,然后与baseurl拼接,请求无法成功.
     * @param url
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST()
    Flowable<ResponseBody> post(@Url String url, @FieldMap Map<String, Object> map);

    @POST()
    @Multipart
    Flowable<ResponseBody> post(@Url String url, @PartMap Map<String, RequestBody> map,
                                @Part MultipartBody.Part file);

    @POST()
    @Multipart
    Flowable<ResponseBody> post(@Url String url, @PartMap Map<String, RequestBody> map,
                                @Part List<MultipartBody.Part> fileList);
}