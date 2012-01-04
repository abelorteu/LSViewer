package com.uoc.lsviewer;

import greendroid.app.GDActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class Help extends GDActivity {
	WebView webHelp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.help);
		webHelp=(WebView)findViewById(R.id.Help);
		webHelp.loadUrl(getResources().getString(R.string.Servidor) + getResources().getString(R.string.Help));
	}
}
