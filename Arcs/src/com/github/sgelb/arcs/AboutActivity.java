package com.github.sgelb.arcs;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		WebView aboutWebView;
		aboutWebView = (WebView) findViewById(R.id.aboutWebView);
		aboutWebView.loadUrl("file:///android_asset/about.html");
	}
}
