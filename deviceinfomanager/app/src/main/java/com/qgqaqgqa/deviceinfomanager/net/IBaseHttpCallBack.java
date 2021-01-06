package com.qgqaqgqa.deviceinfomanager.net;

import com.qgqaqgqa.deviceinfomanager.model.BaseModel;

/**
 * 基础请求返回接口
 * User: Created by 钱昱凯
 * Date: 2018/2/9 0009
 * Time: 14:19
 * Email:342744291@qq.com
 */

public interface IBaseHttpCallBack {
    void onSucess(String url, BaseModel response);

    /**
     * 回调完成时结束请求等待框
     */
    void onFinish();
}
