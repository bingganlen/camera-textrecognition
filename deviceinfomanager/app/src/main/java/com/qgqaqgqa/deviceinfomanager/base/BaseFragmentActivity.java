package com.qgqaqgqa.deviceinfomanager.base;

import androidx.fragment.app.FragmentTransaction;

import com.qgqaqgqa.deviceinfomanager.R;
import com.qgqaqgqa.deviceinfomanager.model.BaseModel;

/**
 * 基础fragmentActivity
 * User: Created by 钱昱凯
 * Date: 2018/1/14/0014
 * Time: 18:15
 */

public abstract class BaseFragmentActivity extends BaseProtocolActivity {


    protected BaseProtocolFragment fragment;

    public BaseFragmentActivity() {
        this(R.layout.act_fragment);
    }

    public BaseFragmentActivity(int layoutResID) {
        super(layoutResID);
    }

    @Override
    public void onSucess(String url, BaseModel response) {
        if (fragment != null) {
            fragment.onSucess(url, response);
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (fragment != null) fragment.onFinish();
    }

    @Override
    public void initViews() {
        initTitle();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (fragment == null) {
            fragment = getFragment();
        }
        transaction.add(R.id.fl_content, fragment);
        transaction.commitAllowingStateLoss();
    }

    protected abstract void initTitle();

    protected abstract BaseProtocolFragment getFragment();

}
