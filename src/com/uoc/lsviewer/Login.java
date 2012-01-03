package com.uoc.lsviewer;


import greendroid.app.GDActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
//http://danlaho.wordpress.com/2011/10/31/login-usuario-simple-para-android/
public class Login extends GDActivity {
	private String session;
	private String user = "";
	private String pass = "";
	private EditText etUser;
	private EditText etPass;
	private Button btnLogin;
	private CheckBox cbRemember;
	private static String REMEMBERED = "remembered";
	private static String strUSER = "User";
	private static String strPASS = "Password";
	private SharedPreferences sp;
	private boolean saveData  = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Comprovem si estan desades les dades d'acces
		sp = getApplicationContext().getSharedPreferences("settings", 0);
		boolean remembered = sp.getBoolean(REMEMBERED, false);
		// Si no estan desades les recuperem de la pantalla
		if(remembered == false){
			setActionBarContentView(R.layout.login);
			etUser = (EditText)this.findViewById(R.id.etUser);
			etPass = (EditText)this.findViewById(R.id.etpassword);
			cbRemember = (CheckBox) findViewById(R.id.cbRemember);
			btnLogin = (Button)this.findViewById(R.id.btnLogin);
			btnLogin.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){                	
					user = etUser.getText().toString();
					pass = etPass.getText().toString();
					saveData = cbRemember.isChecked();
					validar(user, pass);
				}
			}); 

		}else { // Si estan desades les recuperem de les preferencies
			user = sp.getString(strUSER, user);
			pass = sp.getString(strPASS, pass);
			validar(user, pass);
		} 
	}

	private boolean validar(String username, String pass){

		String Servidor= this.getResources().getString(R.string.Servidor);
		/* Comprobamos que no venga alguno en blanco. */
		if (!username.equals("") && !pass.equals("")){
			/* Creamos el objeto cliente que realiza la petición al servidor */
			HttpClient cliente = new DefaultHttpClient();
			/* Definimos la ruta al servidor. En mi caso, es un servlet. */
			String server = Servidor + getResources().getString(R.string.login);
			HttpPost post = new HttpPost(server);

			try{
				/* Defino los parámetros que enviaré. Primero el nombre del parámetro, seguido por el valor.*/
				List<NameValuePair> nvp = new ArrayList<NameValuePair>(2);
				nvp.add(new BasicNameValuePair("user", username));
				/* Encripto la contraseña en MD5. Definición más abajo */
				nvp.add(new BasicNameValuePair("pass", toMd5(pass)));
				/* Agrego los parámetros a la petición */
				post.setEntity(new UrlEncodedFormEntity(nvp));
				/* Ejecuto la petición, y guardo la respuesta */
				HttpResponse respuesta = cliente.execute(post);
				try{   
					/* Traspaso la respuesta del servidor (que viene como JSON) a la instancia de JSONObject en job.
		                /* Definición de inputStreamToString() más abajo. */
					JSONObject job = new JSONObject(this.inputStreamToString(respuesta.getEntity().getContent()).toString());
					/* Muestro la respuesta */
					session=job.getString("session");
					if(session.equals("0")){
						Toast.makeText(Login.this, "L'usuari o la contrasenya són incorrectes!", Toast.LENGTH_LONG).show();
					}else{
						// Si no estan desades però marquem ‘Remember password'
						if(saveData == true){
							//Obtenim l'editor de preferencies per desar-ho
							SharedPreferences.Editor editor = sp.edit();
							editor.putString(strUSER, user);
							editor.putString(strPASS, pass);
							editor.putBoolean(REMEMBERED, true);
							editor.commit();
						}	
						Intent intent = new Intent(Login.this, Home.class);							
						Bundle bundle = new Bundle();
						bundle.putString("session", session);
						intent.putExtras(bundle);
						startActivity(intent);
						finish();
					}
					/* Abajo todas los exceptions que pueden ocurrir. Se imprimen en el log */
					return true;
				}catch(JSONException ex){
					Log.w("Aviso", ex.toString());
					return false;
				}
			}catch(ClientProtocolException ex){
				Log.w("ClientProtocolException", ex.toString());
				return false;
			}catch(UnsupportedEncodingException ex){
				Log.w("UnsupportedEncodingException", ex.toString());
				return false;
			}catch(IOException ex){
				Log.w("IOException", ex.toString());
				return false;
			}
			catch(IllegalStateException ex){
				Log.w("IOException", ex.toString());
				return false;
			}
		}else{
			Toast.makeText(Login.this, "Has d'escriure un usuari i la seva contrasenya!",Toast.LENGTH_LONG).show();
			return false;
		}
	}
	private StringBuilder inputStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();
		//Guardamos la dirección en un buffer de lectura
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		//Y la leemos toda hasta el final
		try{
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		}catch(IOException ex){
			Log.w("Aviso", ex.toString());
		}

		// Devolvemos todo lo leido
		return total;
	}

	public static final String toMd5(final String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
			.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
}
