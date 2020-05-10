package com.example.bypassnsc;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        webView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://www.google.com");
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {

    }
    public void clickFunc(View view){
        webView.loadUrl("https://www.google.com");
    }

    public void executeHttpsUrlConnectRequest(View view){

        InputStream in = null;
        try {

            URL url = new URL("https://example.org/");
            HttpsURLConnection urlConnection =
                    (HttpsURLConnection)url.openConnection();
            in = urlConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //copyInputStreamToOutputStream(in, System.out);
    }
}
