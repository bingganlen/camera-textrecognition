package com.qgqaqgqa.deviceinfomanager.base;
import android.app.Dialog;
import android.text.TextUtils;


import com.google.gson.Gson;
import com.qgqaqgqa.deviceinfomanager.dialog.MyProgressDialog;
import com.qgqaqgqa.deviceinfomanager.model.BaseModel;
import com.qgqaqgqa.deviceinfomanager.net.IBaseHttpCallBack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础请求Activity
 * User: Created by 钱昱凯
 * Date: 2018/1/18 0018
 * Time: 16:14
 */
public abstract class BaseProtocolActivity extends BaseActivity implements IBaseHttpCallBack {
    protected Dialog loadingDialog;
    public List<Boolean> isControl = new ArrayList<Boolean>();
    private int type = 0;
    private Gson mGson;

    /**
     * @param layoutResID 界面资源文件id
     */
    public BaseProtocolActivity(int layoutResID) {
        super(layoutResID);
    }

    public void isNeedFinished() {
        for (boolean b : isControl) {
            if (!b)
                return;
        }
        dissDialog();
    }

    /**
     * @param msg
     */
    public void showDialog(String msg) {
        if (TextUtils.isEmpty(msg))
            showDialog();
        else {
            if (null == loadingDialog) {
                loadingDialog = MyProgressDialog.getDialog(this, msg);
                loadingDialog.show();
            }
        }
    }

    public void showDialog() {
        if (null == loadingDialog) {
            loadingDialog = MyProgressDialog.getDialog(this, "正在加载...");
            loadingDialog.show();
        }
    }

    public void dissDialog() {
        isControl.clear();
        type = 0;
        if (null != loadingDialog) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dissDialog();
    }

    @Override
    public void onSucess(String url, BaseModel response) {
//        if (UrlConstants.ucenter.login.UCENTER_LOGIN_GETLOGINMEMBERINFO.equals(url)) {
//            UcenterLoginMembersSerializable um = (UcenterLoginMembersSerializable) response.getData();
//            initSaveLoginMembmerInfo(um);
//        }
    }

    @Override
    public void onFinish() {
        if (isControl.size() == 0) {
            dissDialog();
            return;
        }
        if (type < isControl.size())
            isControl.set(type, true);

        if (isControl.size() > (type - 1)) {
            type++;
        }
        isNeedFinished();
    }

    /**
     * 更新用户信息
     */
    public void updateLoginMembmer() {
//        ApiManager.getInstance().post(UcenterLoginMembersSerializable.class, UrlConstants.ucenter.login.UCENTER_LOGIN_GETLOGINMEMBERINFO, this);
    }
    private Gson getGson() {
        if (mGson == null) {
            mGson = new Gson();
        }
        return mGson;
    }

    public <T> T fromJson(String json, Type typeOfT) {
        return getGson().fromJson(json, typeOfT);
    }

    public <T> T fromListJson(String json, Class<?> element) {
        return getGson().fromJson(json, com.google.gson.internal
                .$Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, element));
    }
}