package com.example.a003.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private WebView mWv;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getIntentData();
        initView();
    }

    private void getIntentData() {
        mUrl = getIntent().getStringExtra("url");
    }

    private void initView() {
        mToolBar = (Toolbar) findViewById(R.id.toolBar);
        mWv = (WebView) findViewById(R.id.wv);
        mToolBar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(mToolBar);

        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取webview的设置对象
        WebSettings settings = mWv.getSettings();
        //网页是拿js写的,需要webview支持js代码
        settings.setJavaScriptEnabled(true);

        mWv.loadUrl(mUrl);
        //获取网页的标题
        mWv.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                mToolBar.setTitle(title);
            }
        });

    }
}
