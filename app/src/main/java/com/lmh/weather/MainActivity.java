package com.lmh.weather;

import static android.view.KeyEvent.KEYCODE_BACK;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * https://developer.android.google.cn/develop/ui/views/layout/webapps/webview
 * <p>
 * https://www.msn.cn/zh-cn/weather
 * <p>
 * adb shell
 * t30al:/ $ input keyevent KEYCODE_BACK
 *
 * app签名:
 *  lmh
 *  lmh123456
 */
public class MainActivity extends AppCompatActivity {

    // Used to load the 'weather' library on application startup.
    static {
        System.loadLibrary("weather");
    }

    /**
     * A native method that is implemented by the 'weather' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    //private ActivityMainBinding binding;
    //
    private Context context;
    private WindowManager windowManager;
    private View floatingView;
    private WindowManager.LayoutParams layoutParams;
    //
    private WebView webView;
    private ProgressBar pb_progress;

    private void initNavigationBar(Context context) {
        try {
            floatingView = LayoutInflater.from(context).inflate(R.layout.pop_title, null, false);
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            layoutParams = new WindowManager.LayoutParams();
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            layoutParams.format = PixelFormat.RGBA_8888;
            windowManager.addView(floatingView, layoutParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        //// Example of a call to a native method
        //TextView tv = binding.sampleText;
        //tv.setText(stringFromJNI());
        //
        context = this;
        setContentView(R.layout.activity_main);
        initNavigationBar(context);
        webView = (WebView) findViewById(R.id.wv_webview);
        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                hideView();
                return false;//true,不可点击; false,可以点击;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                hideView();
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                hideView();
                super.onPageCommitVisible(view, url);
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                hideView();
                super.onScaleChanged(view, oldScale, newScale);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                hideView();
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                hideView();
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                hideView();
                super.onLoadResource(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

        });
        webView.loadUrl("https://www.msn.cn/zh-cn/weather");
    }

    private void hideView() {
        try {
            pb_progress.setVisibility(View.INVISIBLE);
            //
            // class:
            // unitSwitchContainer-DS-EntryPoint1-1
            // unitSwitchContainer_mobile-DS-EntryPoint1-1
//            webView.loadUrl(
//                    "javascript:(function() { "
//                            + "document.getElementsByClassName('unitSwitchContainer-DS-EntryPoint1-1')[0].style.position='absolute';"
//                            + " })()"
//            );
//            webView.loadUrl(
//                    "javascript:(function() { "
//                            + "document.getElementsByClassName('unitSwitchContainer_mobile-DS-EntryPoint1-1')[0].style.position='absolute';"
//                            + " })()"
//            );
            webView.loadUrl(
                    "javascript:(function() { "
                            + "document.getElementsByClassName('unitSwitchContainer-DS-EntryPoint1-1')[0].style.margin-right='300px';"
                            + " })()"
            );
            webView.loadUrl(
                    "javascript:(function() { "
                            + "document.getElementsByClassName('unitSwitchContainer_mobile-DS-EntryPoint1-1')[0].style.margin-right='300px';"
                            + " })()"
            );
            //class: newsContent-DS-EntryPoint1-1
            webView.loadUrl(
                    "javascript:(function() { "
                            + "document.getElementsByClassName('newsContent-DS-EntryPoint1-1')[0].style.display='none';"
                            + " })()"
            );
            //class: mobileHeaderLogo-DS-EntryPoint1-1
            webView.loadUrl(
                    "javascript:(function() { "
                            + "document.getElementsByClassName('mobileHeaderLogo-DS-EntryPoint1-1')[0].style.display='none';"
                            + " })()"
            );
            //class:  inline-DS-EntryPoint1-1
            webView.loadUrl(
                    "javascript:(function() { "
                            + "document.getElementsByClassName('inline-DS-EntryPoint1-1')[0].style.display='none';"
                            + " })()"
            );
            //class: button-DS-EntryPoint1-1 feedback_link_default-DS-EntryPoint1-1
            webView.loadUrl(
                    "javascript:(function() { "
                            + "document.getElementsByClassName('button-DS-EntryPoint1-1')[0].style.display='none';"
                            + " })()"
            );
            webView.loadUrl(
                    "javascript:(function() { "
                            + "document.getElementsByClassName('feedback_link_default-DS-EntryPoint1-1')[0].style.display='none';"
                            + " })()"
            );
            webView.loadUrl(
                    "javascript:(function() { "
                            + "document.getElementsByClassName('newsContent-DS-EntryPoint1-1')[0].style.display='none';"
                            + " })()"
            );
            //id: mobileHeaderLogo-DS-EntryPoint1-1
            webView.loadUrl("javascript:(function() { "
                    + "document.getElementById('your_id').style.display='none';"
                    + "})()"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }else if (!webView.canGoBack()){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}