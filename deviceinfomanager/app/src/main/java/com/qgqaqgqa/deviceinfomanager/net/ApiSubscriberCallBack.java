package com.qgqaqgqa.deviceinfomanager.net;
import com.qgqaqgqa.deviceinfomanager.model.BaseModel;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * 接口回调基类
 * User: Created by 钱昱凯
 * Date: 2018/2/4/0004
 * Time: 18:51
 * EMail: 342744291@qq.com
 */
public abstract class ApiSubscriberCallBack<T extends BaseModel> implements Subscriber<T> {
    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
        onFailure(t);
    }


    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(Throwable t);
}
