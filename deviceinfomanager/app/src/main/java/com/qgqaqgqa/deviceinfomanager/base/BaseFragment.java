package com.qgqaqgqa.deviceinfomanager.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;

/**
 * User: Created by 钱昱凯
 * Date: 2018/3/8 0008
 * Time: 11:29
 * Email:342744291@qq.com
 */
public abstract class BaseFragment extends Fragment {
    protected BaseActivity mBaseActivity;
    private View mRootView;

    @Override
    public void onAttach(Context context) {
        if (context instanceof BaseActivity) {
            mBaseActivity = (BaseActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ABC_Listener");
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    public abstract int getLayoutResource();

}
