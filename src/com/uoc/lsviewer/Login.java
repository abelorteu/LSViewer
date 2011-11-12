package com.uoc.lsviewer;


import greendroid.app.GDActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends GDActivity {
    
	private EditText etUser;
	private Button btnLogin;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.login);
        
        etUser = (EditText)findViewById(R.id.etUser);
        
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Login.this, Home.class);
					
				Bundle bundle = new Bundle();
				bundle.putString("user", etUser.getText().toString());
				intent.putExtras(bundle);
					
				startActivity(intent);				
			}
		});
    }
}