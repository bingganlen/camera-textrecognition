package com.qgqaqgqa.deviceinfomanager.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qgqaqgqa.deviceinfomanager.R;
import com.qgqaqgqa.deviceinfomanager.base.BaseActivity;
import com.qgqaqgqa.deviceinfomanager.finals.AppConfig;


public class FramentWebView {
    private BaseActivity mActivity;
    private MyWebViewClient webClient;
    private WebView web;
    private ProgressBar web_process;
    private TextView tv_refreshing;

    public FramentWebView(BaseActivity activity, WebViewCallBack callback) {
        mActivity = activity;
        web_process = (ProgressBar) activity.findViewById(R.id.web_process);
        web = (WebView) activity.findViewById(R.id.web);
        tv_refreshing = (TextView) activity.findViewById(R.id.tv_refreshing);
        webClient = new MyWebViewClient(web_process, tv_refreshing, callback);
    }

    public FramentWebView(View view, WebViewCallBack callback) {
        web_process = (ProgressBar) view.findViewById(R.id.web_process);
        web = (WebView) view.findViewById(R.id.web);
        tv_refreshing = (TextView) view.findViewById(R.id.tv_refreshing);
        webClient = new MyWebViewClient(web_process, tv_refreshing, callback);
    }

    /**
     * 加载网页链接
     *
     * @param url
     */
    public void loadUrl(String url) {
        initWebView(false);
        web.loadUrl(url);
    }

    /**
     * 加载富文本
     *
     * @param data
     */
    public void loadData(String data) {
        initWebView(true);
        web.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
    }

    public WebView getWebView() {
        return web;
    }

    private void initWebView(boolean isdata) {
        CookieSyncManager.createInstance(mActivity);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(AppConfig.HOST_URL, "clientType=app");//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();

        // 如果访问的页面中有Javascript，则webview必须设置支持Javascript
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //android与js交互
        web.getSettings().setSupportMultipleWindows(true);
//        web.getSettings().setBlockNetworkImage(false);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //设置网页提示框
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(getWebView().getContext());
                b.setTitle("提示");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });

//        // 设置可以支持缩放
//        web.getSettings().setSupportZoom(true);
//        //设置手势缩放
//        web.getSettings().setBuiltInZoomControls(true);
//        //不显示缩放按钮
//        web.getSettings().setDisplayZoomControls(false);
//        //扩大比例的缩放
//        web.getSettings().setUseWideViewPort(true);
//        //自适应屏幕
//        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        web.getSettings().setLoadWithOverviewMode(true);

        // 设置背景色
        web.setBackgroundColor(0);
        // 设置填充透明度 范围：0-255
        web.getBackground().setAlpha(0);

        web.setWebViewClient(webClient);
        // 设置缓存模式
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 DOM storage API 功能
        web.getSettings().setDomStorageEnabled(false);
        // 设置是否使用HTML5缓存
        web.getSettings().setAppCacheEnabled(false);
        // 数据缓存
        web.getSettings().setDatabaseEnabled(false);
        // 不保存表单数据
        web.getSettings().setSaveFormData(false);
        // 不保存密码
        web.getSettings().setSavePassword(false);
        // 获取焦点
        web.requestFocus();
        // 清除缓存
        web.clearCache(true);
        web.clearHistory();
        web.clearFormData();
        // Clear the view so that onDraw() will draw nothing but white
        // background, and onMeasure() will return 0 if MeasureSpec is not
        // MeasureSpec.EXACTLY
        web.clearView();
        web.clearAnimation();
        web.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        if (isdata) {
            if (webClient.getmRefreshView() != null) {
                webClient.getmRefreshView().setVisibility(View.GONE);
            }
            if (webClient.getmLoadingView() != null) {
                webClient.getmLoadingView().setVisibility(View.GONE);
            }
            web.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @author BaoHang baohang2011@gmail.com
     * @ClassName: MyWebViewClient
     * @Description: 网页跳转情况监听器
     * @date 2014年4月17日 下午1:34:20
     */
    class MyWebViewClient extends WebViewClient {
        private View mLoadingView;
        private View mRefreshView;
        private WebViewCallBack callBack;

        public MyWebViewClient(View loadingView, View refreshView) {
            this.mLoadingView = loadingView;
            this.mRefreshView = refreshView;
        }

        public View getmLoadingView() {
            return mLoadingView;
        }

        public View getmRefreshView() {
            return mRefreshView;
        }

        public MyWebViewClient(View loadingView, View refreshView,
                               WebViewCallBack callback) {
            this(loadingView, refreshView);
            callBack = callback;
        }

        // 在页面加载开始时调用
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (null != mLoadingView && url != null) {
                mLoadingView.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
            }
        }

        // 在页面加载结束时调用
        @Override
        public void onPageFinished(WebView view, String url) {

            if (null != mLoadingView && url != null) {
                mLoadingView.setVisibility(View.GONE);
                view.setVisibility(View.VISIBLE);
            }
        }

        @SuppressWarnings("deprecation")
        private void showRefreshView(final WebView view, final String url) {
            if (url != null) {
                view.stopLoading();
                view.clearView();
                view.setVisibility(View.GONE);
                if (null != mRefreshView) {
                    mRefreshView.setVisibility(View.VISIBLE);
                    mRefreshView.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mRefreshView.setVisibility(View.GONE);
                            view.loadUrl(url);
                        }
                    });
                }
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            showRefreshView(view, failingUrl);
        }

        // 只有在调用webview.loadURL的时候才会调用
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if(url.contains("?")){
//                url+="?";
//            }else {
//                url+="&";
//            }
//            if(!url.contains("clientType=app")){
//                url+="clientType=app";
//            }
            if (callBack != null) {
                if (callBack.urlCallBack(view, url)) {
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            } else
                return super.shouldOverrideUrlLoading(view, url);
        }
    }

    /**
     * @author BaoHang baohang2011@gmail.com
     * @ClassName: WebViewCallBack
     * @Description: 网络请求回调
     * 只有在调用webview.loadURL的时候才会调用
     * @date 2014年4月17日 下午1:26:48
     */
    public interface WebViewCallBack {
        public boolean urlCallBack(WebView web, String url);
    }

}
