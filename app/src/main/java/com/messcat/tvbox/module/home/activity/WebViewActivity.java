package com.messcat.tvbox.module.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.messcat.tvbox.R;

import static android.view.KeyEvent.KEYCODE_BACK;


/**
 * author:Bandele
 * date:2018/5/30 0030$
 * describe:
 */
public class WebViewActivity extends Activity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_HTMLDATA = "html_data";
    private String title;
    private String url;
    private String htmlData;
    private ProgressBar mProgressBar;
    private WebView mViewView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        initView();
    }


    protected void initView() {
        mProgressBar = findViewById(R.id.mProgressBar);
        mViewView = findViewById(R.id.webView);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString(EXTRA_URL);
            url = bundle.getString(EXTRA_URL);
            htmlData = bundle.getString(EXTRA_HTMLDATA);
        }
        //        if (!TextUtils.isEmpty(title)) {
        //            initTitleBar(mToolbar, title);
        //        }
        htmlData="<p><strong>尊敬的来宾您好</strong>，欢迎参加</p><p>第十一届全国光子学学术会议，" +
                "<span style=\"color: rgb(255, 0, 0);\">感谢您多年来的共同" +
                "</span>努力和无私奉献<span style=\"font-size: 36px;\">！" +
                "祝您在会议期间，生活愉快</span>，工作顺利。</p>";
        if (!TextUtils.isEmpty(url)) {
            mViewView.loadUrl(url);
        } else {
            mViewView.loadData(htmlData, "text/html; charset=UTF-8", null);
        }
        //        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                finish();
        //            }
        //        });
        initWebSetting(mViewView.getSettings());
    }


    private void initWebSetting(WebSettings webSettings) {
        webSettings.setJavaScriptEnabled(true);//支持js

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true);  //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//支持内容重新布局
        webSettings.setAppCacheEnabled(true); //启用应用缓存
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setDomStorageEnabled(true);//启用或禁用DOM缓存
        webSettings.setBlockNetworkImage(false);
        webSettings.setSupportMultipleWindows(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //sdk>5.0
            //https协议在使用的时候需要申请一个安全证书
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等
        mViewView.setWebChromeClient(new WebChromeClient());
        //处理各种通知 & 请求事件
        mViewView.setWebViewClient(new WebViewClient());
    }

    //
    //        /**
    //         * WebChromeClient
    //         */
    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (mProgressBar == null)
                return;
            //进度条
            if (newProgress < 100) {
                mProgressBar.setProgress(newProgress);
            } else {
                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            //标题
            //                    if(TextUtils.isEmpty(title))
            //                        initTitleBar(mToolbar, title, false, -1);
        }
    }

    //
    //        /**
    //         * WebViewClient
    //         */
    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器，而是在本WebView中显示
            //不要加上下面注释掉的这句代码，会导致web界面报错
            // view.loadUrl(url);
            if (url.startsWith("http:") || url.startsWith("https:")) {
                return false;
            }
            //请务必使用try、catch 因为该处返回的url可能为无效url或者手机没有安转支付宝导致webview闪退
            try {
                //通过意图调起
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
            }
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();  //接受所有证书
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK && mViewView.canGoBack()) {
            mViewView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewView != null) {
            mViewView.removeAllViews();
            mViewView.destroy();
            //                    mBinding.webView = null;
        }
    }
    //
    //    }

}
