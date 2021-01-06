package com.qgqaqgqa.deviceinfomanager.view;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qgqaqgqa.deviceinfomanager.R;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.LogUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 标题栏
 * User: Created by 钱昱凯
 * Date: 2018/3/8 0008
 * Time: 11:29
 * Email:342744291@qq.com
 */
public class CommonTitleBar implements View.OnClickListener {
    private Activity mActivity;
    @BindView(R.id.header_back_btn)
    ImageView mHeaderBackBtn;
    @BindView(R.id.header_logo)
    ImageView mHeaderLogo;
    @BindView(R.id.header_title)
    TextView mHeaderTitle;
    @BindView(R.id.header_search)
    EditText mHeaderSearch;
    @BindView(R.id.tv_header_right)
    TextView mTvHeaderRight;
    @BindView(R.id.header_home_btn)
    ImageView mHeaderHomeBtn;

    public CommonTitleBar(Activity activity) {
        this.mActivity = activity;
        ButterKnife.bind(this, activity);

//        mHeaderBackBtn.setOnClickListener(this);
//        mHeaderHomeBtn.setOnClickListener(this);
//        mHeaderSearch.setOnClickListener(this);
        hideLeftIcon();
        hideRightIcon();
//        mHeaderSearch.setFocusable(false);
        mHeaderSearch.setFocusableInTouchMode(false);
    }

    /**
     * 如果设置了内容是显示标题，而不显示内容
     *
     * @param text
     */
    public void setTitle(String text) {
        mHeaderSearch.setVisibility(View.GONE);
        if (StringUtils.isEmpty(text)) {
            mHeaderLogo.setVisibility(View.VISIBLE);
            mHeaderTitle.setVisibility(View.GONE);
        } else {
            mHeaderLogo.setVisibility(View.GONE);
            mHeaderTitle.setVisibility(View.VISIBLE);
            mHeaderTitle.setText(text);
        }
    }

    public void showSearch() {
        mHeaderLogo.setVisibility(View.GONE);
        mHeaderTitle.setVisibility(View.GONE);
        mHeaderSearch.setVisibility(View.VISIBLE);
    }
    public void hideSearch() {
        mHeaderLogo.setVisibility(View.GONE);
        mHeaderTitle.setVisibility(View.GONE);
        mHeaderSearch.setVisibility(View.GONE);
    }
    public EditText getSearch() {
        return mHeaderSearch;
    }

    public void showLeftIcon() {//显示返回键
        mHeaderBackBtn.setVisibility(View.VISIBLE);
    }

    public void hideLeftIcon() {//隐藏返回键
        mHeaderBackBtn.setVisibility(View.GONE);
    }

    public void showRightIcon() {//显示返回主页键
        mHeaderHomeBtn.setVisibility(View.VISIBLE);
    }

    public void hideRightIcon() {//隐藏返回主页键
        mHeaderHomeBtn.setVisibility(View.GONE);
    }

    public void hideRightText() {
        mTvHeaderRight.setVisibility(View.GONE);
    }
    public void showRightText() {
        mTvHeaderRight.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化返回键
     *
     * @param resid    图片资源id
     * @param listener 点击事件监听器
     */
    public void setLeftIcon(int resid, View.OnClickListener listener) {
        mHeaderBackBtn.setVisibility(View.VISIBLE);
        mHeaderBackBtn.setImageResource(resid);
        if (listener != null)
            mHeaderBackBtn.setOnClickListener(listener);
    }

    /**
     * 初始化返回主页键
     *
     * @param resid    图片资源id
     * @param listener 点击事件监听器
     */
    public void setRight(int resid, View.OnClickListener listener) {
        mHeaderHomeBtn.setVisibility(View.VISIBLE);
        mHeaderHomeBtn.setImageResource(resid);
        mHeaderHomeBtn.setOnClickListener(listener);
    }

    /**
     * 初始化右侧文本按钮
     *
     * @param text     字符串
     * @param listener 点击事件监听器
     */
    public void setRight(String text, View.OnClickListener listener) {
        mTvHeaderRight.setVisibility(View.VISIBLE);
        mTvHeaderRight.setText(text);
        mTvHeaderRight.setOnClickListener(listener);
    }

    public ImageView getRightIcon() {
        return mHeaderHomeBtn;
    }


    /**
     * 获取标题控件
     *
     * @return
     */
    public View getTitleView() {
        return (View) mHeaderBackBtn.getParent();
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.header_back_btn,R.id.header_home_btn,R.id.header_search})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_home_btn:
//                ActivityUtils.finishAllActivities();
//                ActivityUtils.startActivity(MainActivity.class);
//                mActivity.finish();
                break;
            case R.id.header_back_btn:
                mActivity.onBackPressed();
                break;
            case R.id.header_search://打开搜索
                LogUtils.e("打开搜索");
//                ActivityUtils.startActivity(SoSearchActivity.class);
//                mHeaderSearch.requestFocus();
//                mHeaderSearch.setFocusable(true);
//                mHeaderSearch.requestFocus();
                mHeaderSearch.setFocusableInTouchMode(true);
                mHeaderSearch.requestFocus();
                break;
        }
    }
}