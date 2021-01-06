package com.qgqaqgqa.deviceinfomanager.activity;
import android.webkit.WebView;
import java.util.HashMap;
import com.qgqaqgqa.deviceinfomanager.R;
import com.qgqaqgqa.deviceinfomanager.base.BaseActivity;
import com.qgqaqgqa.deviceinfomanager.view.CommonTitleBar;
import com.qgqaqgqa.deviceinfomanager.view.FramentWebView;

/**
 * app内部显示网页，webView
 * User: Created by 钱昱凯
 * Date: 2018/3/8 0008
 * Time: 16:01
 * Email:342744291@qq.com
 */
public class MyWebActivity extends BaseActivity {
    public String TAG = this.getClass().getName();
    private FramentWebView webView;
    private String url;
    private String data;
    private String title;

    public MyWebActivity() {
        super(R.layout.act_my_web);
    }

    @Override
    public void getIntentData() {
        HashMap<String, String> map = (HashMap<String, String>) getIntent().getSerializableExtra("data");
        url = map.get("url");
        data = map.get("data");
        title = map.get("title");
    }

    @Override
    public void initViews() {
        mTitle = new CommonTitleBar(this);
        mTitle.setTitle(title == null ? "详情" : title);
        mTitle.showLeftIcon();
        webView = new FramentWebView(this, new FramentWebView.WebViewCallBack() {
            @Override
            public boolean urlCallBack(WebView web, String url) {
                //只有在调用webview.loadURL的时候才会调用
                //重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                //return false 跳浏览器去

//                if(url.contains("?")){
//                    url+="?";
//                }else {
//                    url+="&";
//                }
//                if(!url.contains("clientType=app")){
//                    url+="clientType=app";
//                }
//                webView.loadUrl(url);
//                return true;
                return false;
            }
        });
        if (data != null && !data.isEmpty()) {
            if (data.contains("http")) {
                webView.loadUrl(data);
            } else {
                webView.loadData(data);
            }
        } else {
            webView.loadUrl(url);
        }
    }
}
