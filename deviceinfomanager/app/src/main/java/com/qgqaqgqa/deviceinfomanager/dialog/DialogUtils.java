package com.qgqaqgqa.deviceinfomanager.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.StringUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.TimeUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * User: Created by 钱昱凯
 * Date: 2019/11/6
 * Time: 17:46
 * EMail: 342744291@qq.com
 */
public class DialogUtils {

    /**
     * 弹出单选框
     * @param mContext
     * @param strArray
     * @param listener
     * @return
     */
    public static Dialog getListDialog(Context mContext, String [] strArray, DialogInterface.OnClickListener listener){
        return new AlertDialog.Builder(mContext)
//                .setTitle("请选择").setIcon(R.mipmap.icon)
                .setItems(strArray, listener)
                .setNegativeButton("取消", null)
                .create();
    }
    /**
     * 弹出单选框
     * @param mContext
     * @param adapter
     * @param listener1
     * @param listener2
     * @return
     */
    public static Dialog getMultiListDialog(Context mContext, ListAdapter adapter, int position,
                                            DialogInterface.OnClickListener listener1,
                                            DialogInterface.OnClickListener listener2){
        return new AlertDialog.Builder(mContext)
//                .setTitle("请选择").setIcon(R.mipmap.icon)
                .setSingleChoiceItems(adapter,position, listener1)
                .setPositiveButton("确定", listener2)
                .setNegativeButton("取消", null)
                .create();
    }
    /**
     * 弹出单选框
     * @param mContext
     * @param adapter
     * @param listener
     * @return
     */
    public static Dialog getSingleListDialog(Context mContext, ListAdapter adapter, int position, DialogInterface.OnClickListener listener){
        return new AlertDialog.Builder(mContext)
//                .setTitle("请选择").setIcon(R.mipmap.icon)
                .setSingleChoiceItems(adapter,position, listener)
//                .setNegativeButton("取消", null)
                .create();
    }
    /**
     * 弹出单选框
     * @param mContext
     * @param strArray
     * @param listener
     * @return
     */
    public static Dialog getSingleListDialog(Context mContext, String [] strArray,int position, DialogInterface.OnClickListener listener){
//        Arrays.sort(strArray);
        return new AlertDialog.Builder(mContext)
//                .setTitle("请选择").setIcon(R.mipmap.icon)
                .setSingleChoiceItems(strArray,position, listener)
//                .setNegativeButton("取消", null)
                .create();
    }

    /**
     * 弹出多选框
     * @param mContext
     * @param strArray
     * @param listener
     * @return
     */
    public static Dialog getMultiListDialog(Context mContext, String[] strArray, boolean[] bs,
                                            DialogInterface.OnClickListener listener){
//        return new AlertDialog.Builder(mContext)
////                .setTitle("请选择").setIcon(R.mipmap.icon)
//                .setMultiChoiceItems(strArray, bs, new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                        bs[which]=isChecked;
//                    }
//                })
//                .setPositiveButton("确定", listener)
//                .setNegativeButton("取消", null)
//                .create();
        return getMultiListDialog(mContext,strArray,bs,listener,false);
    }
    /**
     * 弹出多选框
     * @param mContext
     * @param strArray
     * @param listener
     * @return
     */
    public static Dialog getMultiListDialog(Context mContext, String[] strArray, boolean[] bs,
                                            DialogInterface.OnClickListener listener,boolean hasAll){
        int i1 = hasAll ? strArray.length + 1 : strArray.length;
        String[] newStrArray = new String[i1];
        boolean[] newBsArray = new boolean[i1];
        if(newStrArray.length>0){
            newStrArray[0]="全选";
            newBsArray[0]=false;
        }
            for(int i=0;i<strArray.length;i++){
                newStrArray[hasAll?i+1:i]=strArray[i];
                newBsArray[hasAll?i+1:i]=bs[i];
            }
        return new AlertDialog.Builder(mContext)
//                .setTitle("请选择").setIcon(R.mipmap.icon)
                .setMultiChoiceItems(newStrArray, newBsArray, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(which==0){
                            if(hasAll) {
                                for(int i=0;i<i1-1;i++){
                                    newBsArray[i+1]=isChecked;
                                    bs[i] = isChecked;
                                }
                            }else{
                                bs[which] = isChecked;
                            }
                        }else{
                            bs[hasAll?which-1:which] = isChecked;
                        }
                        newBsArray[which]=isChecked;
                        ((BaseAdapter)((AlertDialog)dialog).getListView().getAdapter()).notifyDataSetChanged();
                    }
                })
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null)
                .create();
    }

    public static Dialog getDateAndTimeDialog(Context mContext, TextView tv) {
        Calendar calendar= Calendar.getInstance();
        Date date= TimeUtils.millis2Date(System.currentTimeMillis());

        return new DatePickerDialog(mContext, (view, year, month, dayOfMonth) -> new TimePickerDialog(mContext, (view1, hourOfDay, minute) -> tv.setText(year+"-"+((month+1)>9?"":"0")+(month+1)+"-"+
                (dayOfMonth>9?"":"0")+dayOfMonth+" "+
                (hourOfDay>9?"":"0")+hourOfDay+":"+
                (minute>9?"":"0")+minute+":00"), calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE),true).show(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
    }

    public static Dialog getEditDialog(Context mContext,String title,EditText et,
                                       DialogInterface.OnClickListener listener){
        if(StringUtils.isBlank(title))
            title="请输入消息";
        return new AlertDialog.Builder(mContext).setTitle(title)
                .setView(et)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null)
                .create();
    }


    public static Dialog getDialog(Context mContext,String title,String message, DialogInterface.OnClickListener listener){
        return new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null)
                .create();
    }
}
