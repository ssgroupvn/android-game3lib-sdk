package com.ssgroup.game3sdk;

import java.util.ArrayList;
import java.util.List;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import com.ssgroup.libgame3.Banner;
import com.ssgroup.libgame3.ExchangeCallback;
import com.ssgroup.libgame3.Game3Lib;
import com.ssgroup.libgame3.LoginCallback;
import com.ssgroup.libgame3.LogoutCallback;
import com.ssgroup.libgame3.PaymentCallback;
import com.ssgroup.libgame3.Payment_Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class Game3 {	
	
	static public String id = "";
	static public String username = "";
	static public String token = "";				
	
	static public String SetKey(final Context c, String appID, String appKey, String gameID)
	{
		return Game3Lib.SetKey(c, appID, appKey, gameID);
	}										
	
	static public void Login(Context c,LoginCallback callback)
	{		
		Game3Lib.loginCallback = callback;
		if(Game3Lib.DeviceAuth())
		{			
			callback.onSuccess(Game3Lib.id, Game3Lib.username);
		}
		else
		{			
			//Intent i = new Intent(c, Login_Web.class);
			Intent i = new Intent(c, Login_Activity.class);
			c.startActivity(i);
		}
	}
	
	static public String getFBAccessToken()
	{
		return Game3Lib.getFBAccessToken();
	}
	static public void Logout(Context c, LogoutCallback callback)
	{
		Game3Lib.Logout(c, callback);
				
	}	
	
	static public void Payment(Context c, int amount, String url, String ext, PaymentCallback callback)
	{						
		Game3Lib.paymentCallback = callback;
		Bundle sendBundle = new Bundle();
		sendBundle.putInt("amount", amount);
		sendBundle.putString("URL", url);
		sendBundle.putString("EXT", ext);
		Intent i = new Intent(c, Payment_Activity.class);
		i.putExtras(sendBundle);
		c.startActivity(i);
	}
	
	static public void Exchange(Context c, int amount, String url, String ext, ExchangeCallback callback )
	{
		Game3Lib.Exchange(c, amount, url, ext, callback);				
	}
	
	static public int GetBalance(String userId, Boolean GameGold)
	{
		return Game3Lib.GetBalance(userId, GameGold);
	}
	
	static public boolean ProccessLoginViaGame3(final Context context, String packageName)
	{			 
		Game3Lib.dlMg = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);	  
	    
		boolean Game3installed = false;
		List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);        	
    	for(int i=0;i<packs.size();i++)         	 
    	{
 	        if(packs.get(i).packageName.equals("com.ssgroup.game3app"))
 	        {
 	        	Game3installed = true;
 	        	break;
 	        }     	       
 	    }
    	if(Game3installed)
    	{
			/*String packageName = context.getPackageName();
			Bundle sendBundle = new Bundle();
			sendBundle.putString("packageName", packageName);
			
			Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.ssgroup.game3app");
	    	intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	    	intent.putExtras(sendBundle);		   
	    	context.startActivity(intent);		   
	    	((Activity) context).finish();*/
    	}        	
    	else	    
    	{  	       
    		//check online
    		if(Game3Lib.isOnline(context))
    		{    			    		
	    		//show dialog to warn user install Game3App
    			if(Game3Lib.CheckDownloadGame3(packageName))
    			{
		    		new AlertDialog.Builder(context)		    		
		    	    .setTitle("Game3 - Cổng game mobile số 1 VN")
		    	    .setMessage("Bạn chưa cài Game3, cài Game3 ngay nhé!")    	    
		    	    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
		    	        public void onClick(DialogInterface dialog, int which) {
		    	            //Download Game3App
		    	        	String phone_model = Build.MANUFACTURER + " " + Build.MODEL;    	        	
		    	            //String IMEI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		    	        	//String IMEI = "354400051222069";    
		    	            //System.out.println("http://game3.vn/download/game3?os=android&type=log_install_app&data={\"IMEI\":\"" + IMEI + "\",\"model\":\"" + phone_model + "\",\"game\":\"" + context.getPackageName() + "\"}");
		    	        	Game3Lib.downloadId = Game3Lib.Download(context, "http://game3.vn/download/game3?os=android&type=log_install_app&data={\"IMEI\":\"" + Game3Lib.android_id + "\",\"model\":\"" + phone_model + "\",\"game\":\"" + context.getPackageName() + "\"}");
		    	        	//context.startActivity(m_intent);
		    	        	//((Activity)context).finish();
		    	        }    	      
		    	     })
		    	     .setNegativeButton("Để sau", new DialogInterface.OnClickListener() {
		    	        public void onClick(DialogInterface dialog, int which) {
		    	            //Download Game3App
		    	        	String phone_model = Build.MANUFACTURER + " " + Build.MODEL;    	        	
		    	            //String IMEI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		    	        	//String IMEI = "354400051222069";        	        	
		    	        	Game3Lib.downloadId = Game3Lib.Download(context, "http://game3.vn/download/game3?os=android&type=log_install_app&data={\"IMEI\":\"" + Game3Lib.android_id + "\",\"model\":\"" + phone_model + "\",\"game\":\"" + context.getPackageName() + "\"}");
		    	        	//context.startActivity(m_intent);
		    	        	//((Activity)context).finish();    	        
		    	        }    	      
		    	     })
		    	     .setCancelable(false)				
		    	     .show();	
		    		return true;
    			}
    			else
    			{
    				return false;
    			}
    		}
    		else
    		{
    			new AlertDialog.Builder(context)
	    	    .setTitle("Chưa kết nối internet")
	    	    .setMessage("Bạn chưa kết nối internet. Hãy kết nối internet trước khi vào game!")    	    
	    	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        public void onClick(DialogInterface dialog, int which) {
	    	            //Close app to connect internet	    	        	
	    	        	((Activity)context).finish();
	    	        }    	      
	    	     })	    	     
	    	     .show();
    		}
    	} 
    	return false;
	}
	
	static float X;
	static float Y;
	
	static float globalX;
	static float globalY;		
	
	static boolean isClick = false;
	
	static public int SCREEN_WIDTH;
	static public int SCREEN_HEIGHT;
	
	static HUDView mHUDView;
	static public ImageView imgBanner;// = new ImageView(context);
	
	static boolean isIdle = false;
	
	static public boolean HUDAppear;
	static public ArrayList<String> HUDLinks = new ArrayList<String>();
	static public Banner m_Banner = new Banner();
	
	@SuppressLint("NewApi")
	static public void StartHUD(Context context)
	{
		if(context == null)
			return;
		//Intent Servintent = new Intent(context, HUD.class);
		//context.startService(Servintent);
		if(!Game3Lib.isOnline(context))
		{
			return;					
		}
		try
		{
			mHUDView = new HUDView(context);
		}
		catch(NullPointerException e)
		{
			return;
		}
		final WindowManager.LayoutParams params;
		
		//LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		//mView = (LinearLayout) li.inflate(R.layout.hud_layout, null);
		
		params = new WindowManager.LayoutParams(
        		100, 100, 
        		0, 100,               
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.TOP;       
        params.setTitle("Load Average");
        
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.addView(mHUDView, params);
        
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        SCREEN_WIDTH = metrics.widthPixels;
        SCREEN_HEIGHT = metrics.heightPixels;             
                
        mHUDView.BtnExpand.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub					
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					//System.out.println("KKKKKKK touch event down raw y: " + event.getRawY());
					//System.out.println("KKKKKKK touch event down y: " + event.getY());
					//System.out.println("KKKKKKK touch event down params y: " + params.y);
					X = event.getX();
					Y = event.getRawY() - params.y;
					
					globalX = event.getRawX();
					globalY = event.getRawY();		
					
					//isIdle = false;
					mHUDView.btnExpandIdle = false;
					//mView.BtnExpand.setAlpha(1);
				}
				else if(event.getAction() == MotionEvent.ACTION_MOVE)
				{													
					params.x = (int)(event.getRawX() - X);
					params.y = (int)(event.getRawY() - Y);					
					//wm.updateViewLayout(ray, params1);
			        wm.updateViewLayout(mHUDView, params);			        			        			        			        
				}
				else if(event.getAction() == MotionEvent.ACTION_UP)
				{												
					if((Math.abs(event.getRawX() - globalX) < 3) || (Math.abs(event.getRawY()- globalY) < 3))
					{								
						//System.out.println("KKKKKKK button press");
						isClick = true;
						mHUDView.switchState(true);																							
					}		
					else
						isClick = false;
					
					if(params.x <= SCREEN_WIDTH/2)	
					{
						params.x = 0;	
						mHUDView.isLeft = true;
					}
					else
					{
						params.x = SCREEN_WIDTH;	
						mHUDView.isLeft = false;
						//System.out.println("KKK x: " + params.x);
					}
						//params.x = wm.getDefaultDisplay().getWidth() - params.width;
					if(!isClick)
					{						
						//wm.updateViewLayout(ray, params1);
					}					
					wm.updateViewLayout(mHUDView, params);
											
					//isIdle = true;
					mHUDView.btnExpandIdle = true;																					
				}
				return true;
			}
		});
	}		
	
	static public void StopHUD(Context context)
	{
		if(mHUDView != null)
        {
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).removeView(mHUDView);
            mHUDView = null;            
        }
	}
	
	static void GetHUDInfo(String gameId)
	{							
		Game3Lib.GetHUDInfo(gameId);
	}
	
	static public void StartBanner(Context context)
	{
		final WindowManager.LayoutParams params;
		
		//LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		//mView = (LinearLayout) li.inflate(R.layout.hud_layout, null);
		
		params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, 
        		0, 0,               
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;       
        params.setTitle("Load Average");
        
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        
        imgBanner = new ImageView(context);
        imgBanner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//StopBanner(v.getContext());
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(m_Banner.link));
                v.getContext().startActivity(intent);
                StopBanner(v.getContext());
			}
		});
        UrlImageViewHelper.setUrlDrawable(imgBanner, m_Banner.img_url);
        //imgView.setImageResource(R.drawable.icon_inapp);
        //imgView.setScaleType(ScaleType.FIT_CENTER);
        wm.addView(imgBanner, params);                   
	}	
	
	static public void StopBanner(Context context)
	{
		if(imgBanner != null)
        {
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).removeView(imgBanner);
            imgBanner = null;            
        }
	}
}
