package com.sp.loginregisterfirebases;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class WebViewFragment2 extends Fragment {

    private WebView webView;
    private CardView back;
    private CardView forward;
    private CardView refresh;
    private CardView openbrowser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.webview, container, false);

        // Get the WebView from the layout
        webView = rootView.findViewById(R.id.webvie);
        back = rootView.findViewById(R.id.back);
        forward = rootView.findViewById(R.id.forward);
        refresh = rootView.findViewById(R.id.refresh);
        openbrowser = rootView.findViewById(R.id.openbrowser);
        webView.setWebContentsDebuggingEnabled(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goForward();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshPage();
            }
        });
        openbrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInBrowser();
            }
        });

        // Enable JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Set a WebViewClient to handle page navigation within the WebView
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // Show the refresh, back, and forward cards when a new page starts loading
                refresh.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                forward.setVisibility(View.VISIBLE);
                openbrowser.setVisibility(View.VISIBLE);


            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Hide the refresh, back, and forward cards when the page finishes loading
                refresh.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                forward.setVisibility(View.VISIBLE);
                openbrowser.setVisibility(View.VISIBLE);


            }
        });
        // Load the desired website URL
        webView.loadUrl("https://www.greenplan.gov.sg/");


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Hide the system navigation bar again if it reappears
        getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


    }


    private void goBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    private void goForward() {
        if (webView.canGoForward()) {
            webView.goForward();
        }
    }

    private void refreshPage() {
        webView.reload();
    }

    private void openInBrowser() {
        // Get the current URL from the WebView
        String currentUrl = webView.getUrl();

        // Create an Intent to open the URL in the default browser
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl));
        startActivity(intent);
    }
}
