package com.uoc.lsviewer;

import greendroid.app.GDActivity;
import greendroid.graphics.drawable.ActionBarDrawable;
import greendroid.widget.ActionBarItem;
import greendroid.widget.NormalActionBarItem;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;

public class Help extends GDActivity {
	
	private	WebView webHelp;
	private String session;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBarContentView(R.layout.help);
		initActionBar();
		
		Bundle bundle = getIntent().getExtras();
		session = bundle.getString("session");
		
		webHelp=(WebView)findViewById(R.id.Help);
		webHelp.loadUrl(getResources().getString(R.string.Servidor) + getResources().getString(R.string.Help));
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Log.d(this.getClass().getName(), "back button pressed");
	    	Intent intent = new Intent(Help.this, Home.class);							
			Bundle bundle = new Bundle();
			bundle.putString("session", session);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private void initActionBar() {
		addActionBarItem(getActionBar()
                .newActionBarItem(NormalActionBarItem.class)
				.setDrawable(new ActionBarDrawable(this, R.drawable.ic_menu_home)), R.id.action_bar_view_home);		
	}
	
	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		switch (item.getItemId()) {
			case R.id.action_bar_view_home:
				Intent intent = new Intent(Help.this, Home.class);							
				Bundle bundle = new Bundle();
				bundle.putString("session", session);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();				
			break;				
			default:
				return super.onHandleActionBarItemClick(item, position);
		}
		return true;
	}
}
