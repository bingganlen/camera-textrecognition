package com.qgqaqgqa.deviceinfomanager.base;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.ActivityUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.BarUtils;
import com.qgqaqgqa.deviceinfomanager.view.CommonTitleBar;

import java.util.HashMap;
import butterknife.ButterKnife;
import com.qgqaqgqa.deviceinfomanager.R;
import com.qgqaqgqa.deviceinfomanager.activity.MyWebActivity;

/**
 * 基础
 * 负责控制管理所有Activity
 * 统一Activity初始化信息
 * 统一登录注销方法
 *
 * @author 粽子
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 标题栏
     */
    public CommonTitleBar mTitle;

    /**
     * 加载界面的资源id
     */
    protected int mLayoutResID = -1;

    /**
     * @param layoutResID 界面资源文件id
     */
    public BaseActivity(int layoutResID) {
        this.mLayoutResID = layoutResID;
    }

    /**
     * 构造了上级<br/>
     * <p>
     * 自定义生命流程<br/>
     * getIntentData();初始化数据：intent传入...<br/>
     * findIds();//初始化控件
     * initViews();//初始化页面绑定数据和监听器
     * refreshView();//刷新页面获取数据
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        setContentView(mLayoutResID);
        ButterKnife.bind(this);
        getIntentData();
        findIds();
        initViews();
        refreshView();

        /**
         * 判断是否有标题
         * @author 粽子
         */
        if(mTitle!=null){
            //给状态栏主色调
            BarUtils.setStatusBarColor(this, getResources().getColor(R.color.colorPrimary),false);
            BarUtils.addMarginTopEqualStatusBarHeight(mTitle.getTitleView());
        }
    }

    /**
     * 父类方法的重写，增加了友盟统计的代码.
     * @see Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        // 集成了友盟统计代码
//        MobclickAgent.onResume(this);
    }

    /**
     * 父类方法的重写，增加了友盟统计的代码.
     * @see Activity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
        // 集成了友盟统计代码
//         MobclickAgent.onPause(this);
    }

    /**
     * getData:获得上层界面传过来的数据. <br/>
     * 一般情况下是不需要做处理的，只有在上层界面有数据传过来时才需要在这边进行数据获取操作.<br/>
     * 获取传递过来数据的方法 getIntent().getExtras().getSerializable("data")
     * @author 钱昱凯
     */
    public void getIntentData() {

    }

    /**
     * findIds:初始化控件操作，一般用于从XML中实例化控件. <br/>
     * 只是用于控件的实例化操作，不要在里面进行空间的数据填写操作，除非调用封装了一定数据操作的方法，如TitleBar等控件封装.<br/>
     * @author 钱昱凯
     */
    public void findIds() {

    }

    /**
     * initViews:初始化界面，对界面进行赋值等操作. <br/>
     * 主要用于界面的数据填充操作，在操作之前，确保已经在findIds方法中对控件进行过实例化操作了.<br/>
     * @author 钱昱凯
     */
    public abstract void initViews();


    /**
     * 页面数据更新，在开始的时候，需要判断对象是否为空，更安全；在页面刷新时，只需要调这个方法，减少冗余代码
     */
    public void refreshView() {

    }

    /**
     * 判断是否已经登录
     * @return false：未登录直接打开登录页；true：已登录
     * @example if(baseActivity.hasLoginMember()){}
     */
    public boolean hasLoginMember(boolean login) {
//        if (!SPUtils.getInstance().contains(UcenterLoginMembersSerializable.MEMBER_TOKEN)) {
//            if (login)
//                startActivityForResult(new Intent(this,
//                        LoginActivity.class), REQUEST_LOGIN);
//            return false;
//        }
        return true;
    }

    /**
     * 判断是否已经登录
     * @return false：未登录直接打开登录页；true：已登录
     * @example if(baseActivity.hasLoginMember()){}
     */
    public boolean hasLoginMember() {
        return hasLoginMember(true);
    }

//    /**
//     * 初始化用户信息，保存token
//     * @param um 用户模型
//     */
//    public void initSaveLoginMembmerInfo(UcenterLoginMembersSerializable um) {
//        // TODO: 2018/3/6 0006 初始化用户信息，保存token
//        if (um != null) {
//            if (!StringUtils.isEmpty(um.getMemberToken()))
//                SPUtils.getInstance().put(UcenterLoginMembersSerializable.MEMBER_TOKEN, um.getMemberToken());
//            if (!StringUtils.isEmpty(um.getNickname()))
//                SPUtils.getInstance().put(UcenterLoginMembersSerializable.MEMBER_NICKNAME, um.getNickname());
//            if (!StringUtils.isEmpty(um.getMobile()))
//                SPUtils.getInstance().put(UcenterLoginMembersSerializable.MEMBER_MOBILE, um.getMobile());
//            if (!StringUtils.isEmpty(um.getMemberid()))
//                SPUtils.getInstance().put(UcenterLoginMembersSerializable.MEMBER_ID, um.getMemberid());
//            if (!StringUtils.isEmpty(um.getGold()))
//                SPUtils.getInstance().put(UcenterLoginMembersSerializable.MEMBER_GOLD, um.getGold());
//            if (!StringUtils.isEmpty(um.getIntegral()))
//                SPUtils.getInstance().put(UcenterLoginMembersSerializable.MEMBER_INTEGRAL, um.getIntegral());
//            if (!StringUtils.isEmpty(um.getEverdaySigninNum()))
//                SPUtils.getInstance().put(UcenterLoginMembersSerializable.MEMBER_EVERDAY_SIGNIN_NUM, um.getEverdaySigninNum());
//            if (!StringUtils.isEmpty(um.getAvatar()))
//                SPUtils.getInstance().put(UcenterLoginMembersSerializable.MEMBER_AVATAR, um.getAvatar());
//        }
//    }
//
//    /**
//     * 注销用户信息
//     */
//    public void logout() {
//        // TODO: 2018/3/6 0006 注销用户信息
//        SPUtils.getInstance().remove(UcenterLoginMembersSerializable.MEMBER_TOKEN);
//        SPUtils.getInstance().remove(UcenterLoginMembersSerializable.MEMBER_NICKNAME);
//        SPUtils.getInstance().remove(UcenterLoginMembersSerializable.MEMBER_MOBILE);
//        SPUtils.getInstance().remove(UcenterLoginMembersSerializable.MEMBER_ID);
//        SPUtils.getInstance().remove(UcenterLoginMembersSerializable.MEMBER_GOLD);
//        SPUtils.getInstance().remove(UcenterLoginMembersSerializable.MEMBER_INTEGRAL);
//        SPUtils.getInstance().remove(UcenterLoginMembersSerializable.MEMBER_EVERDAY_SIGNIN_NUM);
//        SPUtils.getInstance().remove(UcenterLoginMembersSerializable.MEMBER_AVATAR);
//    }

    public void openWeb(String title, String data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("data", data);
        Intent intent = new Intent(this, MyWebActivity.class);
        intent.putExtra("data", map);
        ActivityUtils.startActivity(intent,R.anim.push_right_out,R.anim.push_right_in);
    }
}