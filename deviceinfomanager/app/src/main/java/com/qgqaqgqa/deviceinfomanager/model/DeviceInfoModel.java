package com.qgqaqgqa.deviceinfomanager.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.LogUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.TimeUtils;

import java.io.Serializable;

/**
 * User: Created by 钱昱凯
 * Date: 2019/12/22
 * Time: 15:53
 * EMail: 342744291@qq.com
 */
public class DeviceInfoModel implements Serializable {
    private String id;
    private String name;
    private String allname;

    public String getAllname() {
        return allname;
    }

    public void setAllname(String allname) {
        this.allname = allname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("name",name);
        contentValues.put("allname",allname);
        return contentValues;
    }
    public DeviceInfoModel() {
    }

    public DeviceInfoModel(Cursor cursor) {
        id=cursor.getString(cursor.getColumnIndex("id"));
        name=cursor.getString(cursor.getColumnIndex("name"));
        allname=cursor.getString(cursor.getColumnIndex("allname"));
//        LogUtils.e(id,name,allname);
    }

    public String getAllValue(){
        String[] names=allname.split("  ");
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<names.length/2;i++){
            if(sb.length()>0)
                sb.append("\n");
            sb.append("【");
            sb.append(names[i*2]);
            sb.append("】：");
            sb.append(names[i*2+1]);
        }
        return sb.toString();
    }
}
