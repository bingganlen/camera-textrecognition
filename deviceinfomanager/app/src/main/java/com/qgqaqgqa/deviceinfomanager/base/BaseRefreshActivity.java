package com.qgqaqgqa.deviceinfomanager.base;

import com.qgqaqgqa.deviceinfomanager.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;

/**
 * User: Created by 钱昱凯
 * Date: 2019/11/20
 * Time: 15:02
 * EMail: 342744291@qq.com
 */
public abstract class BaseRefreshActivity extends BaseProtocolActivity implements OnRefreshListener {

    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout mRefreshLayout;

    public BaseRefreshActivity(int layoutResID) {
        super(layoutResID);
    }

    public void findIds() {
        mRefreshLayout.setEnableLoadmore(false);//关闭加载更多

        mRefreshLayout.setOnRefreshListener(this);
    }

    public void updateView() {
        refreshView();
    }

    public void refreshView() {
        mRefreshLayout.autoRefresh();
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (mRefreshLayout != null) {
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.finishRefresh();
            }

            if (mRefreshLayout.isLoading()) {
                mRefreshLayout.finishLoadmore();
            }
        }
    }
}
