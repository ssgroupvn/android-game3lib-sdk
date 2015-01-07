package com.ssgroup.game3sdk;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.ssgroup.libgame3.Game3Lib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class Page_Activity extends Activity 
{	
	static String link = "http://www.google.com";	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//Unregister(this);
		super.onDestroy();
	}
		
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
		super.onCreate(savedInstanceState);		
						
		setContentView(R.layout.page);		
		getWindow().setLayout(-1, -1);
	    	
		if(android.os.Build.VERSION.SDK_INT > 8 ){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy); 
        }								
		
		final LinearLayout prg = (LinearLayout) findViewById(R.id.prg_loadpage);		
		
		final WebView webviewPage = (WebView) findViewById(R.id.webpage);
				
		if(android.os.Build.VERSION.SDK_INT > 10){
			webviewPage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	    }
		webviewPage.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView wView, String url)
            {                     	            	
                return false;                
            }		
            
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            	// TODO Auto-generated method stub
            	super.onPageStarted(view, url, favicon);
            	prg.setVisibility(View.VISIBLE);
            	webviewPage.setVisibility(View.GONE);
            }
            
            @Override
            public void onPageFinished(WebView view, String url) {
            	// TODO Auto-generated method stub
            	super.onPageFinished(view, url);
            	prg.setVisibility(View.GONE);
            	webviewPage.setVisibility(View.VISIBLE);
            }
            
            @Override
            public void onReceivedError(WebView view, int errorCode,
            		String description, String failingUrl) {
            	// TODO Auto-generated method stub
            	super.onReceivedError(view, errorCode, description, failingUrl);
            }
            
        });
		
		webviewPage.loadUrl(link);	
				
		ImageView back = (ImageView) findViewById(R.id.btn_back);
		ImageView forward = (ImageView) findViewById(R.id.btn_forward);
		
		ImageView refresh = (ImageView) findViewById(R.id.btn_refresh_web);
		ImageView close = (ImageView) findViewById(R.id.btn_close_web);			
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(webviewPage.canGoBack())
					webviewPage.goBack();
			}
		});
		
		forward.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(webviewPage.canGoForward())
					webviewPage.goForward();
					//webviewPage.goBackOrForward(1);
			}
		});
		
		refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				webviewPage.reload();
			}
		});
		
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return false;
	}		
}
