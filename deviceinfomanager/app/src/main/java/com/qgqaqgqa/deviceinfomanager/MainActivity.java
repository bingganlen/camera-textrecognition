package com.qgqaqgqa.deviceinfomanager;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.google.gson.reflect.TypeToken;
import com.qgqaqgqa.deviceinfomanager.adapter.DeviceInfoAdapter;
import com.qgqaqgqa.deviceinfomanager.baidu.ocr.ui.camera.CameraActivity;
import com.qgqaqgqa.deviceinfomanager.base.BaseRefreshActivity;
import com.qgqaqgqa.deviceinfomanager.dialog.DialogUtils;
import com.qgqaqgqa.deviceinfomanager.model.DeviceInfoModel;
import com.qgqaqgqa.deviceinfomanager.model.SearchModel;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.constant.PermissionConstants;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.PermissionUtils;
import com.qgqaqgqa.deviceinfomanager.util.utilcode.util.SizeUtils;
import com.qgqaqgqa.deviceinfomanager.view.CommonTitleBar;
import com.qgqaqgqa.deviceinfomanager.view.LeftSpaceItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseRefreshActivity {
    private static final int REQUEST_CODE_INPUT_EXCEL = 1;

    private static final int REQUEST_CODE_GENERAL = 105;
    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
    private static final int REQUEST_CODE_ACCURATE_BASIC = 107;
    private static final int REQUEST_CODE_ACCURATE = 108;
    private static final int REQUEST_CODE_GENERAL_ENHANCED = 109;
    private static final int REQUEST_CODE_GENERAL_WEBIMAGE = 110;
    private static final int REQUEST_CODE_BANKCARD = 111;
    private static final int REQUEST_CODE_VEHICLE_LICENSE = 120;
    private static final int REQUEST_CODE_DRIVING_LICENSE = 121;
    private static final int REQUEST_CODE_LICENSE_PLATE = 122;
    private static final int REQUEST_CODE_BUSINESS_LICENSE = 123;
    private static final int REQUEST_CODE_RECEIPT = 124;

    private static final int REQUEST_CODE_PASSPORT = 125;
    private static final int REQUEST_CODE_NUMBERS = 126;
    private static final int REQUEST_CODE_QRCODE = 127;
    private static final int REQUEST_CODE_BUSINESSCARD = 128;
    private static final int REQUEST_CODE_HANDWRITING = 129;
    private static final int REQUEST_CODE_LOTTERY = 130;
    private static final int REQUEST_CODE_VATINVOICE = 131;
    private static final int REQUEST_CODE_CUSTOM = 132;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.tv_no_data)
    View tvNoData;

    @BindView(R.id.tv_device_count)
    TextView tvCount;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;

    private List<DeviceInfoModel> list=new ArrayList<>();
    private DeviceInfoAdapter adapter;
    private int page=0;

    private boolean hasGotToken = false;

    private AlertDialog.Builder alertDialog;
    private Dialog selSearchDialog;
    private List<HashMap<String, String>> searchList = new ArrayList<>();

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    public void initViews() {
        mTitle=new CommonTitleBar(this);
        mTitle.showSearch();
        mTitle.setRight(R.drawable.ic_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkTokenStatus()) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
            }
        });
        mTitle.getSearch().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mRefreshLayout.autoRefresh();
                    return true;
                }
                return false;
            }
        });

        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceInfoAdapter(this, list);
        mRecyclerview.addItemDecoration(new LeftSpaceItemDecoration(this, SizeUtils.dp2px(1)));
        mRecyclerview.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener((view, position) -> {

            new AlertDialog.Builder(this)
                    .setTitle(list.get(position).getId())
                    .setMessage(list.get(position).getAllValue())
                    .setCancelable(false)
                    .setPositiveButton("关闭", null)
                    .create().show();
        });
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setOnLoadmoreListener(refreshlayout -> search());

        initPermission();
    }
    private void initPermission() {
        if (!PermissionUtils.isGranted(android.Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.STORAGE).callback(new PermissionUtils.FullCallback() {
                @Override
                public void onGranted(List<String> permissionsGranted) {
                    initAccessTokenWithAkSk();
                }

                @Override
                public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                    finish();
                }
            }).request();
        }else{
            initAccessTokenWithAkSk();
        }
    }
    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }

    private void search() {
        if(page==0)
            list.clear();
        tvCount.setVisibility(list.size()>0?View.VISIBLE:View.GONE);
        page++;
        onFinish();
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page=0;
        search();
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }
    /**
     * 以license文件方式初始化
     */
    private void initAccessToken() {
        OCR.getInstance(this).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("licence方式获取token失败", error.getMessage());
            }
        }, getApplicationContext());
    }

    /**
     * 用明文ak，sk初始化
     */
    private void initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(),  "Zmgl00YZuG4amoa9ixZIP0y2", "EWdIbQcGLdYufBYGhGQgYL2CuYiXlXoI");
    }



    private void alertText(final String title, final String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(alertDialog==null)
                    alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    private void infoPopText(final String result) {

        if(result!=null){
            SearchModel sm=fromJson(result,new TypeToken<SearchModel>(){}.getType());
            if(sm!=null){
                if(sm.getWords_result_num()>1){
                    searchList.clear();
                    int kvCount=0;
                    String kvSearch="";
                    for(SearchModel.WordsResultBean b:sm.getWords_result()){
                        if(b.getWords().toUpperCase().contains("KV")) {
                            kvSearch=b.getWords();
                            kvCount++;
                        }
                        HashMap<String,String> map=new HashMap<>();
                        map.put("title",b.getWords());
                        searchList.add(map);
                    }
                    if(kvCount==1){
                        mTitle.getSearch().setText(kvSearch);
                        mRefreshLayout.autoRefresh();
                    }else{
                        showSelSearchSelect();
                    }
                }else if(sm.getWords_result_num()==1){
                    mTitle.getSearch().setText(sm.getWords_result().get(0).getWords());
                    mRefreshLayout.autoRefresh();
                }
            }
        }
        dissDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }

    //然后进入系统的文件管理,选择文件后
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK)
            return;
        if(requestCode!=REQUEST_CODE_INPUT_EXCEL)
        showDialog();
        switch (requestCode){
            case REQUEST_CODE_GENERAL:// 识别成功回调，通用文字识别（含位置信息）
                RecognizeService.recGeneral(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_ACCURATE:// 识别成功回调，通用文字识别（含位置信息高精度版）
                RecognizeService.recAccurate(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_GENERAL_BASIC:// 识别成功回调，通用文字识别
                RecognizeService.recGeneralBasic(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_ACCURATE_BASIC:// 识别成功回调，通用文字识别（高精度版）
                RecognizeService.recAccurateBasic(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_GENERAL_ENHANCED:// 识别成功回调，通用文字识别（含生僻字版）
                RecognizeService.recGeneralEnhanced(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_GENERAL_WEBIMAGE:// 识别成功回调，网络图片文字识别
                RecognizeService.recWebimage(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_BANKCARD:// 识别成功回调，银行卡识别
                RecognizeService.recBankCard(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_VEHICLE_LICENSE:// 识别成功回调，行驶证识别
                RecognizeService.recVehicleLicense(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_DRIVING_LICENSE:// 识别成功回调，驾驶证识别
                RecognizeService.recDrivingLicense(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_LICENSE_PLATE:// 识别成功回调，车牌识别
                RecognizeService.recLicensePlate(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_BUSINESS_LICENSE:// 识别成功回调，营业执照识别
                RecognizeService.recBusinessLicense(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_RECEIPT:// 识别成功回调，通用票据识别
                RecognizeService.recReceipt(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_PASSPORT:// 识别成功回调，护照
                RecognizeService.recPassport(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_QRCODE:// 识别成功回调，二维码
                RecognizeService.recQrcode(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_LOTTERY:// 识别成功回调，营业执照识别
                RecognizeService.recLottery(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_VATINVOICE:// 识别成功回调，增值税发票
                RecognizeService.recVatInvoice(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_NUMBERS:// 识别成功回调，数字
                RecognizeService.recNumbers(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_HANDWRITING:// 识别成功回调，手写
                RecognizeService.recHandwriting(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_BUSINESSCARD:// 识别成功回调，名片
                RecognizeService.recBusinessCard(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
            case REQUEST_CODE_CUSTOM:// 识别成功回调，自定义模板
                RecognizeService.recCustom(this, getSaveFile(getApplicationContext()).getAbsolutePath(),
                        new RecognizeService.ServiceListener() {
                            @Override
                            public void onResult(String result) {
                                infoPopText(result);
                            }
                        });
                break;
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance(this).release();
    }



    private void showSelSearchSelect() {
        if (selSearchDialog == null) {
            SimpleAdapter adapter = new SimpleAdapter(this, searchList, android.R.layout.simple_list_item_1, new String[]{"title"}, new int[]{android.R.id.text1});
            selSearchDialog = DialogUtils.getSingleListDialog(this, adapter, -1, (dialog, which) -> {
                mTitle.getSearch().setText(searchList.get(which).get("title"));
                mRefreshLayout.autoRefresh();
                dialog.dismiss();
            });
            selSearchDialog.setOnShowListener(dialog -> adapter.notifyDataSetChanged());
        }
        selSearchDialog.show();
    }
}