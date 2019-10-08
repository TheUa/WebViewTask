package the.ua.webviewtask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.Map;

public class ContentPlayerWebView extends WebView {
    public class JavascriptInterface {
        @android.webkit.JavascriptInterface
        public void notifyVideoEnd()
        {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (videoEnabledWebChromeClient != null) {
                        videoEnabledWebChromeClient.onHideCustomView();
                    }
                }
            });
        }
    }

    private ContentPlayerWebChromeClient videoEnabledWebChromeClient;
    private boolean addedJavascriptInterface;

    public ContentPlayerWebView(Context context) {
        super(context);
        addedJavascriptInterface = false;
    }

    @SuppressWarnings("unused")
    public ContentPlayerWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addedJavascriptInterface = false;
    }

    @SuppressWarnings("unused")
    public ContentPlayerWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addedJavascriptInterface = false;
    }

    public boolean isVideoFullscreen() {
        return videoEnabledWebChromeClient != null && videoEnabledWebChromeClient.isVideoFullscreen();
    }

    @Override
    @SuppressLint("SetJavaScriptEnabled")
    public void setWebChromeClient(WebChromeClient client) {
        getSettings().setJavaScriptEnabled(true);

        if (client instanceof ContentPlayerWebChromeClient) {
            this.videoEnabledWebChromeClient = (ContentPlayerWebChromeClient) client;
        }

        super.setWebChromeClient(client);
    }

    @Override
    public void loadData(String data, String mimeType, String encoding) {
        addJavascriptInterface();
        super.loadData(data, mimeType, encoding);
    }

    @Override
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        addJavascriptInterface();
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    @Override
    public void loadUrl(String url) {
        addJavascriptInterface();
        super.loadUrl(url);
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        addJavascriptInterface();
        super.loadUrl(url, additionalHttpHeaders);
    }

    private void addJavascriptInterface() {
        if (!addedJavascriptInterface) {
            addJavascriptInterface(new JavascriptInterface(), "_VideoEnabledWebView");
            addedJavascriptInterface = true;
        }
    }

}