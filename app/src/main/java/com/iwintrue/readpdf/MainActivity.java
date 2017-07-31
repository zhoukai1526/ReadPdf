package com.iwintrue.readpdf;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private String fileUrl = "http://cdn.mozilla.net/pdfjs/tracemonkey.pdf";
    private String  pdfHtml = "file:///android_asset/pdf.html";
    private ProgressBar pro;
    public static final int LOAD_JAVASCRIPT = 0X01;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String javaScript = "javascript: getpdf2('"+ fileUrl +"')";
            mWebView.loadUrl(javaScript);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {

        mWebView = (WebView) findViewById(R.id.web);
        pro = (ProgressBar) findViewById(R.id.pro);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDomStorageEnabled(true);
        mWebView.addJavascriptInterface(new JsObject(this, fileUrl), "client");



        mWebView.loadUrl(pdfHtml);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pro.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                handler.sendEmptyMessage(LOAD_JAVASCRIPT);
                Toast.makeText(MainActivity.this, "开始请求pdf文件", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void pre(View view) {
        String javaScript = "javascript: getpdf2('"+ fileUrl +"')";
        mWebView.loadUrl(javaScript);

    }

    public void next(View view) {
        mWebView.loadUrl("javascript: goNext()");

    }

    public void before(View view) {
        mWebView.loadUrl("javascript: goPrevious()");
    }


    class JsObject {
        Activity mActivity;
        String url ;
        public JsObject(Activity activity,String url) {
            mActivity = activity;
            this.url= url;
        }

        //    测试方法
        @JavascriptInterface
        public String dismissProgress() {
            pro.setVisibility(View.GONE);
            return this.url;
        }
    }
}
