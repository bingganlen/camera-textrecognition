package com.qgqaqgqa.deviceinfomanager.finals;
import android.os.Environment;

import com.qgqaqgqa.deviceinfomanager.MyApplication;
import com.qgqaqgqa.deviceinfomanager.R;

import java.io.File;

/**
 * ClassName:AppConfig Function:系统配置文件目录，配置系统的默认固定参数.
 * Description: 系统配置文件目录，配置系统的默认固定参数.
 * User: Created by 钱昱凯
 * Date: 2018/2/8 0008
 * Time: 14:48
 * Email:342744291@qq.com
 */
public class AppConfig {
    public static final int REQUEST_LOGIN = 999;
    //系统缓存字段
    public static final String SP_IS_SAVE_USERNAME = "sp_is_save_username";
    public static final String SP_IS_SAVE_PASSWORD = "sp_is_save_password";
    public static final String SP_USERNAME = "sp_username";
    public static final String SP_USERPWD = "sp_userpwd";
    public static final String SP_SESSIONID = "sp_sessionid";


    public static final String SP_UID ="sp_uid";//视频用户uID
    public static final String SP_USER_ID ="sp_user_id";//用户ID
    public static final String SP_USER_NICKNAME ="sp_user_nickname";//用户名
    public static final String SP_USER_USERNAME ="sp_user_username";//用户名
    public static final String SP_USER_AVATARURL ="sp_user_avatarurl";//用户头像
    public static final String SP_USER_OFFICE ="sp_user_office";//用户机构
    public static final String SP_USER_OFFICE_CODE ="sp_user_office_code";//用户机构编码
    public static final String SP_USER_COMPANY ="sp_user_company";//用户公司
    public static final String SP_USER_AUTHINFO ="sp_user_authinfo";//用户权限
    public static final String SP_USER_MENUTREE ="sp_user_menutree";//用户菜单
    public static final String SP_USER_PARENT_OFFICE_CODE ="sp_user_parent_office_code";//父级机构编码
    public static final String SP_USER_PARENT_OFFICE_CODES ="sp_user_parent_office_codes";//父级机构编码列表
    public static final String SP_USER_OFFICE_TYPE ="sp_user_office_type";//用户机构类型：1）省级公司；2）市级公司；3）部门；4）县级公司；5）抢修班组；6）监理公司；7）施工单位

    public static final String SP_USER_POST ="sp_user_post";//当前用户选择员工岗位
    public static final String SP_USER_POST_LIST ="sp_user_post_list";//当前用户员工岗位列表


    public static final String SP_PROJECT_JOB_TYPE ="sp_project_job_type";//项目作业类型
    public static final String SP_PROJECT_MBID ="sp_project_mbid";//项目步骤模板id
    public static final String SP_PROJECT_TITLE ="sp_project_title";//项目名称
    public static final String SP_PROJECT_DTGCMC ="sp_project_dtgcmc";//项目名称
    public static final String SP_PROJECT_REPAIR_STAFF ="sp_project_repair_staff";//项目作业人员
    public static final String SP_PROJECT_QXBH ="sp_project_qxbh";//项目抢修编号
    public static final String SP_PROJECT_CONTENT ="sp_project_content";//项目作业内容
    public static final String SP_PROJECT_PALACE ="sp_project_palace";//项目作业地点
    public static final String SP_PROJECT_MAKE_PERSON ="sp_project_make_person";//项目作业监督人员
    public static final String SP_PROJECT_YJRYCODE ="sp_project_yjrycode";//项目作业监督人员编码
    public static final String SP_PROJECT_MAKE_PERSON1 ="sp_project_make_person1";//项目作业管理人员
    public static final String SP_PROJECT_DDRYCODE ="sp_project_ddrycode";//项目作业管理人员编码


//    个推
    public static final String GETUI_APP_ID ="CqbdyBsrU490VQ6PKhIWs1";
    public static final String GETUI_APP_KEY ="woWTcSkzCi84JAqkMhqwK1";
    public static final String GETUI_APP_SECRET ="4FIsFIWmGj8gud5kx38sl7";

    //服务器基本信息
    public static final String SECRETKEY = "dlqx,wskj,online";

    //当前连接服务器模式，测试模式还是产线模式,正式版的时候需要改成false.
    public static final boolean TEST_MODE = true;

    ///////////////////////////////////////////////////////////////////////////
    // 测试的API头地址.
    ///////////////////////////////////////////////////////////////////////////
    public static final String TEST_ZAPI_URL = "http://192.168.0.111";

    ///////////////////////////////////////////////////////////////////////////
    // 产线的API头地址.
    ///////////////////////////////////////////////////////////////////////////
    public static final String ONLINE_ZAPI_URL = "http://154.8.138.119";

    ///////////////////////////////////////////////////////////////////////////
    // App使用的API头地址.
    ///////////////////////////////////////////////////////////////////////////
    public static final String HOST_URL = (TEST_MODE?TEST_ZAPI_URL:ONLINE_ZAPI_URL)+":8080/zngk_nw/";
    public static final String ZAPI_URL = HOST_URL +"a/";

    //应用缓存文件基本信息，程序在手机SDK中的主缓存目录.
    public static final String APP_PATH = Environment.getExternalStorageDirectory().getPath()+
            File.separator+ MyApplication.getInstance().getApplicationContext().getResources()
            .getString(R.string.app_name);

    //程序在手机SDK中的缓存目录
    public static final String DIR_CACHE = APP_PATH+File.separator+"cache";

    //程序在手机SDK中的音频缓存目录.
    public static final String DIR_SOUND = APP_PATH + File.separator + "sound";

    //程序在手机SDK中的图片缓存目录.
    public static final String DIR_IMG = APP_PATH + File.separator + "image";

    //程序在手机SDK中的视频缓存目录.
    public static final String DIR_VIDEO = APP_PATH + File.separator + "video";

    //程序在手机SDK中的压缩图片缓存目录
    public static final String DIR_CPIMG = DIR_IMG + File.separator + "cpimg";
}