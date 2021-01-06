package com.qgqaqgqa.deviceinfomanager.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import com.qgqaqgqa.deviceinfomanager.R;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.ScreenUtils;


/**
 * User: Created by 钱昱凯
 * Date: 2018/2/8 0008
 * Time: 15:52
 * Email:342744291@qq.com
 */
public class MyProgressDialog {
    /**
     * 进度条对话框
     *
     * @param context
     * @param msg     对话框上显示的内容
     * @return 进度条
     */
    public static ProgressDialog getDialog(Context context, String msg) {
        // 显示进度条
        return getDialog(context, msg, false);
    }

    public static ProgressDialog getDialog(Context context, String msg, boolean isCancelable) {
        // 显示进度条
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);

        //任务条是否不定
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(isCancelable);
        return progressDialog;
    }

    /**
     * 弹出视图的提示框
     *
     * @param context
     * @param view
     * @return
     */
    public static Dialog getDialog(Context context, View view, int i, boolean isCancelable) {
        final Dialog dialog = new Dialog(context, R.style.MyDialogStyleBottom);
        dialog.setCancelable(isCancelable);
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setGravity(i);
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        //(int) (ApplicationEx.getInstance().getDeviceWidth() * 0.8);

        p.width = ScreenUtils.getScreenWidth();
        p.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(p);
        return dialog;
    }
}