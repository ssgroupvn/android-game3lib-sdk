package com.ssgroup.demogame3;

import com.ssgroup.game3sdk.Game3;
import com.ssgroup.game3sdk.R;
import com.ssgroup.libgame3.ExchangeCallback;
import com.ssgroup.libgame3.InstallerGame3;
import com.ssgroup.libgame3.LoginCallback;
import com.ssgroup.libgame3.LogoutCallback;
import com.ssgroup.libgame3.PaymentCallback;

//import com.ssgroup.libgame3.R;
//import com.ssgroup.libgame3.*;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DemoActivity  extends Activity{
	
	String user = "";
	String username = "";
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub		
		Intent intent = new Intent(this, InstallerGame3.class);			
		startActivity(intent);		
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Game3.StartHUD(this);		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Game3.StopHUD(this);
		
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Game3.StopBanner(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);				 
	    	
		String appId = "sm08";
		String appKey = "dHRG8IW2naPFsLM7dxWKenWnF";
		String gameId = "1";
		
		String android_id = Game3.SetKey(this, appId, appKey, gameId);
		
		System.out.println("android_id: " + android_id);
		
		//Intent intent = new Intent(this, Login_Activity.class);
		//startActivity(intent);
		//Game3.StartLogin(this);											
		
		final Handler handle = new Handler();													
			
		Button btn_login = (Button)findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				Game3.Login(DemoActivity.this, new LoginCallback() {
					
					@Override
					public void onSuccess(String user_id, String user_name) {
						// TODO Auto-generated method stub						
						user = user_id;
						username = user_name;						
						handle.post(new Runnable() {	    	    
				    	    public void run() {	    	    	
				    	        // code here will run on UI thread	
				    	    	ShowGame();
				    	    	findViewById(R.id.btn_login).setVisibility(View.GONE);
								findViewById(R.id.btn_logout).setVisibility(View.VISIBLE);
				    	    }
				    	});
					}

					@Override
					public void onError(int errorCode, String message) {
						// TODO Auto-generated method stub
						
					}
				});
				//findViewById(R.id.btn_login).setVisibility(View.GONE);
				//findViewById(R.id.btn_logout).setVisibility(View.VISIBLE);
			}
		});
		
		Button btn_banner = (Button) findViewById(R.id.btn_banner);
		btn_banner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Game3.StartBanner(DemoActivity.this);				
			}
		});
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && Game3.imgBanner != null)
		{
			Game3.StopBanner(DemoActivity.this);
			return false;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	void ShowGame()
	{
		final TextView text = (TextView)findViewById(R.id.text);
		text.setText("User id: " + user + " Username: " + username + "\nFB AccessToken: " + Game3.getFBAccessToken());
		text.setVisibility(View.VISIBLE);
		Button btn_payment = (Button)findViewById(R.id.btn_payment);
		btn_payment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				String exchangeURL = "http://www.example.com"; // url you must send to exchange SSRoll to gold ingame
				String exchangeExt = "171001:6000520140110141956861:android"; // param you pass to exchange function
				final int amount_exchange = 100; //amount of gold that user want to exchange from ssrole
				Game3.Exchange(DemoActivity.this, amount_exchange, exchangeURL, exchangeExt, new ExchangeCallback() {
					
					@Override
					public void onSuccess(String order_id, String user_id, int amount, String ext) {
						// TODO Auto-generated method stub												
					}
					
					@Override
					public void onError(int error_code, String ext) {
						// TODO Auto-generated method stub
						//show card payment site								
						int gameGold = Game3.GetBalance(Game3.id, true); //amount of gameGold in account
						Toast.makeText(DemoActivity.this, "Not enough ssroll to exchange!", Toast.LENGTH_SHORT).show(); //this message is just an example, you need show a dialog to inform user
						
						String paymentURL = "http://www.example.com"; // url you must send to payment card for ssroll
						String paymentExt = "171001:6000520140110141956861:android"; // extension param you pass to payment function						
						int amount_payment = amount_exchange - gameGold; // amount of gameGold that be exchange after payment card
						Game3.Payment(DemoActivity.this, amount_payment, paymentURL, paymentExt, new PaymentCallback() {
							
							@Override
							public void onFinish() {
								// TODO Auto-generated method stub
								System.out.println("payment finish");
							}
						});												
					}
				});
			}
		});
		final Handler handle1 = new Handler();
		Button btn_logout = (Button)findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				Game3.Logout(DemoActivity.this, new LogoutCallback() {					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						findViewById(R.id.btn_logout).setVisibility(View.GONE);
						findViewById(R.id.btn_login).setVisibility(View.VISIBLE);
						text.setText("log-outed");
					}
					
					@Override
					public void onError() {
						// TODO Auto-generated method stub
						
					}
				});						
			}
		});
		
	}	
}
