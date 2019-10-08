package the.ua.webviewtask;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ContentPlayerWebView webView;
    ContentPlayerWebChromeClient webChromeClient;
    String location;
    String getLink;
    private static final String KEY_ENTER = "ENTER";
    private static final String KEY_LOCATION = "LOCATION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);

        View nonVideoLayout = findViewById(R.id.nonVideoLayout);
        ViewGroup videoLayout = findViewById(R.id.videoLayout);
        @SuppressLint("InflateParams") View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null);
        getLink = Objects.requireNonNull(getIntent().getExtras()).getString(KEY_ENTER);
        location = getIntent().getExtras().getString(KEY_LOCATION);
        assert location != null;
        if (savedInstanceState == null)
            webChromeClient = new ContentPlayerWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) {
                @Override
                public void onProgressChanged(WebView view, int progress) {
                }
            };
        webChromeClient.setOnToggledFullscreen(new ContentPlayerWebChromeClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);

                } else {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);

                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                }

            }
        });
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new InsideWebViewClient());

        if (location.equals("Ukraine") || location.equals("Страна:") || location.equals("Ошибка")) {

            webView.loadUrl(getLink);
//        webView.loadUrl("http://m.youtube.com");
//        webView.loadUrl("http://www.game-game.com.ua");
        } else webView.loadUrl("file:///android_asset/error_page.html");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    private class InsideWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if ((!webChromeClient.onBackPressed()) || (webView != null))
            if (webView.canGoBack())
                webView.goBack();
            else
                super.onBackPressed();
    }
}

