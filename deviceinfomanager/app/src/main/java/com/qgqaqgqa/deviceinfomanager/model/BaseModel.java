package com.qgqaqgqa.deviceinfomanager.model;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * User: Created by 钱昱凯
 * Date: 2018/2/4/0004
 * Time: 19:16
 * EMail: 342744291@qq.com
 */
public class BaseModel<T> implements Serializable {

//    "result": "true",
//            "message": "获取信息成功！",
//            "sessionid": "20deffbbd0054d07bf180af2c767fe0c",
//            "__url": "/zngk_nw/a/index"


    //接口返回的数据模型，我们实际需要的数据值
    private T user;
    private T data;
    //接口错误代码
    private String result;
    //接口错误信息
    private String message;
    //接口访问系统的凭证
    private String sessionid;
    private String __url;
    private int pageNo;//页数
    private int count;//数量
    private int pageSize;//每页多少个
    private T list;//列表数据

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    public T getData() {
        return list==null?data:list;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public String get__url() {
        return __url;
    }

    public void set__url(String __url) {
        this.__url = __url;
    }

    /**
     * 创建一个Class的对象来获取泛型的class
     */
    private Class<T> clz;

    @SuppressWarnings("unchecked")
    public Class<T> getClz() {
        if (clz == null) {
            clz = (Class<T>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        }
        return clz;
    }

    public boolean isSuccess() {
        return "true".equals(result);
    }


}
