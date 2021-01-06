package com.qgqaqgqa.deviceinfomanager;
import android.app.Application;
import android.content.Context;
import com.qgqaqgqa.deviceinfomanager.finals.AppConfig;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.Utils;

import java.io.File;

/**
 * 全局的app
 *
 * @author 钱昱凯
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    public static Context mContext;

    /**
     * 构造方法
     *
     * @author 粽子
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getContext();
        instance = this;
        Utils.init(this);

        //缓存文件目录
        initFileDir();

    }

    /**
     * 获取context
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * initFileDir:初始化系统SDK缓存文件目录. <br/>
     * 注意在使用时需要针对于不同的项目修改APP_PATH中的工程名称.<br/>
     * 初始化SD卡下的AppConfig指定的缓存目录.<br/>
     * 注意尽量不要进行删减.<br/>
     *
     * @author 钱昱凯
     */
    public void initFileDir() {
        File appFile = new File(AppConfig.APP_PATH);
        if (!appFile.exists()) {
            appFile.mkdir();
        }
        File cacheFile = new File(AppConfig.DIR_CACHE);// 其他下载缓存数据目录
        if (!cacheFile.exists()) {
            cacheFile.mkdir();
        }
        File imgFile = new File(AppConfig.DIR_IMG);// 其他下载缓存数据目录
        if (!imgFile.exists()) {
            imgFile.mkdir();
        }
        File cpimgFile = new File(AppConfig.DIR_CPIMG);// 其他下载缓存数据目录
        if (!cpimgFile.exists()) {
            cpimgFile.mkdir();
        }
    }
}