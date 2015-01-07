package com.ssgroup.game3sdk;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError.Category;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.ssgroup.game3sdk.R;
import com.ssgroup.libgame3.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Register_Activity extends Activity 
{
	EditText edtxt_Username;
	EditText edtxt_Password;
	EditText edtxt_Password_Confirm;
	EditText edtxt_Captcha;
	
	ImageView imgCaptcha;
	
	Button btn_Register;
	ImageView btn_Captcha_refresh;
	
	ProgressDialog mProgress;
	
	/*static public String captcha_url;
	static public String session_id;*/		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		//getWindow().setLayout(dpToPx(280), dpToPx(320));
		
		edtxt_Username 			= (EditText) findViewById(R.id.reg_edtxtUsername);
		edtxt_Password 			= (EditText) findViewById(R.id.reg_edtxtPassword);		
		edtxt_Password_Confirm 	= (EditText) findViewById(R.id.reg_edtxtPassword_confirm);
		edtxt_Captcha 			= (EditText) findViewById(R.id.reg_edtxtCaptcha);
						
		imgCaptcha = (ImageView) findViewById(R.id.reg_imgCaptcha);
		btn_Captcha_refresh = (ImageView) findViewById(R.id.reg_btnCaptcha_refresh);
		
		btn_Register = (Button) findViewById(R.id.reg_btnRegister);
		
		btn_Register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String ClientID = "SSG";
				try {
					ClientID = readTxt();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("KKKKK register");
				String username = edtxt_Username.getText().toString();
				String password = edtxt_Password.getText().toString();	
				String password_confirm = edtxt_Password_Confirm.getText().toString();								
				String captcha = edtxt_Captcha.getText().toString();
				
				if(username.equals(""))
				{
					Toast toast = Toast.makeText(v.getContext(), "Chưa nhập tên đăng nhập!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
					return;
				}
				
				if(password.equals(""))
				{
					Toast toast = Toast.makeText(v.getContext(), "Chưa nhập mật khẩu!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
					return;
				}
				
				if(!password.equals(password_confirm))
				{
					Toast toast = Toast.makeText(v.getContext(), "Mật khẩu không chính xác!", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();
					return;
				}
				
				TaskRegis task = new TaskRegis();
				task.execute(ClientID, username, password, captcha);								
			}
		});
		
		if(Login.GetCaptcha())
			UrlImageViewHelper.setUrlDrawable(imgCaptcha, Login.captcha_url);
		
		btn_Captcha_refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Login.GetCaptcha())
					UrlImageViewHelper.setUrlDrawable(imgCaptcha, Login.captcha_url);
			}
		});
	}		
	
	String readTxt() throws Exception
    {
        InputStream inputStream = null;
        try {
            inputStream = this.getAssets().open("ClientId.txt");
            System.out.println("inputStream ClientId.txt from assets " +  inputStream);
            //Log.d("readTxt", "inputStream ClientId.txt from assets " +  inputStream);
        } catch (FileNotFoundException e) {
            inputStream = this.getClass().getClassLoader().getResourceAsStream("META-INF/ClientId.txt");
            System.out.println("inputStream ClientId.txt from META-INF " +  inputStream);
            //Log.d("readTxt", "inputStream ClientId.txt from META-INF " +  inputStream);
        }
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try
        {
            for(int i = inputStream.read(); i != -1; i = inputStream.read())
                byteArrayOutputStream.write(i);

            inputStream.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
        String txt = byteArrayOutputStream.toString();
        System.out.println("txt " + txt);
        //Log.d("readTxt", "txt " +  txt);
        return txt;
    }
	
	/*String readTxt() throws IOException{	     
		//InputStream inputStream;					
		//inputStream = getAssets().open("ClientId.txt");	
		
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("/META-INF/ClientId.txt");
		System.out.println(inputStream);	     
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();	     
		int i;	  
		try {	  
			i = inputStream.read();
			while (i != -1)	      
			{	       
				byteArrayOutputStream.write(i);	      
				i = inputStream.read();	      
			}	      
			inputStream.close();	  
		} catch (IOException e) {	   
			// TODO Auto-generated catch block	   
			e.printStackTrace();	  
		}	     
		System.out.println(byteArrayOutputStream.toString());
				
		return byteArrayOutputStream.toString();			
	}*/
	
	class TaskRegis extends AsyncTask<String, Void, Integer>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();			
			mProgress = ProgressDialog.show(Register_Activity.this, "", "Đang xử lý...");
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub								
			int result = Login.Register(params[0], params[1], params[2], params[3], Register_Activity.this);
			return result;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(mProgress.isShowing())
				mProgress.dismiss();
			
			if(result == Login.REGISTER_SUCCESS)
			{
				Game3Lib.loginCallback.onSuccess(Game3Lib.id, Game3Lib.username);
				Register_Activity.this.finish();
			}
			else if(result == Login.WRONG_CAPTCHA)
			{
				Toast toast = Toast.makeText(Register_Activity.this, "Sai mã xác thực!", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
				
				UrlImageViewHelper.setUrlDrawable(imgCaptcha, Login.captcha_url);
			}
			else if(result == Login.WRONG_USERNAME_FORMAT)
			{
				Toast toast = Toast.makeText(Register_Activity.this, "Tên đăng nhập từ 6-24 kí tự và chỉ có a-z,0-9", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
				
				UrlImageViewHelper.setUrlDrawable(imgCaptcha, Login.captcha_url);
			}
			else if(result == Login.EXISTED_USERNAME)
			{
				Toast toast = Toast.makeText(Register_Activity.this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
				
				UrlImageViewHelper.setUrlDrawable(imgCaptcha, Login.captcha_url);
			}
		}
	}		
}
