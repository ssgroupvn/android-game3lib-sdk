package com.ssgroup.game3sdk;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import com.ssgroup.game3sdk.R;
import com.ssgroup.libgame3.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login_Activity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener
{
	/* Request code used to invoke sign in user interactions. */	  
	private static final int RC_SIGN_IN = 0;

	  
	/* Client used to interact with Google APIs. */	  
	private GoogleApiClient mGoogleApiClient;
	  
	/* A flag indicating that a PendingIntent is in progress and prevents
	   
	   * us from starting further intents.
	   */	  
	private boolean mIntentInProgress;
	
	/* Track whether the sign-in button has been clicked so that we know to resolve
	 * all issues preventing sign-in without waiting.
	 */
	private boolean mSignInClicked;

	/* Store the connection result from onConnectionFailed callbacks so that we can
	 * resolve them when the user clicks sign-in.
	 */
	private ConnectionResult mConnectionResult;

	/* A helper method to resolve the current ConnectionResult error. */
	private void resolveSignInError() 
	{	  
		if (mConnectionResult.hasResolution()) 
		{	    
			try 
			{	      
				mIntentInProgress = true;	      
				startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);	    
			} 
			catch (SendIntentException e) 
			{	      
				// The intent was canceled before it was sent.  Return to the default	      
				// state and attempt to connect to get an updated ConnectionResult.	      
				mIntentInProgress = false;	      
				mGoogleApiClient.connect();	    
			}	  
		}	
	}


	
	EditText edtxt_Username;
	EditText edtxt_Password;
	
	Button btn_Login;
	Button btn_LoginFB;
	Button btn_LoginGG;
	
	TextView btn_ForgetPass;
	TextView btn_Register;
	
	ProgressDialog mProgress;
	
	int loginSocial;
	String Social[] = {"game3", "facebook", "google"};
	
	static String FacebookID;
	int LoginFB_Code = 8748;		
	
	/*private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i("FB", "Logged in...");
        } else if (state.isClosed()) {
            Log.i("FB", "Logged out...");
        }
    }
    
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) 
        {
            onSessionStateChange(session, state, exception);
        }
    };
    
    private UiLifecycleHelper uiHelper;*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/*uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);*/
	    
		setContentView(R.layout.login);
		//getWindow().setLayout(dpToPx(280), dpToPx(320));						
		
		mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(Plus.API)
        .addScope(Plus.SCOPE_PLUS_LOGIN)
        .build();

		
		edtxt_Username = (EditText) findViewById(R.id.login_edtxtUsername);
		edtxt_Password = (EditText) findViewById(R.id.login_edtxtPassword);
		
		btn_Login = (Button) findViewById(R.id.login_btnLogin);
		btn_LoginFB = (Button) findViewById(R.id.login_btnLoginFB);
		btn_LoginGG = (Button) findViewById(R.id.login_btnLoginGG);
		
		/*LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
		  authButton.setBackgroundResource(R.drawable.but_face);
		  authButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		  authButton.setReadPermissions(Arrays.asList("public_profile", "email",
		    "user_birthday"));*/		  
		
		btn_ForgetPass = (TextView) findViewById(R.id.login_btnFogetPass);
		btn_Register = (TextView) findViewById(R.id.login_btnRegister);
		
		btn_Login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String username = edtxt_Username.getText().toString();
				String password = edtxt_Password.getText().toString();
				
				loginSocial = 0;
				TaskLogin task = new TaskLogin();
				task.execute(username, password);
				/*if(Login.LoginGame3(username, password, Login_Activity.this))
				{										
					finish();
				}*/
			}
		});
		
		btn_Register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Login_Activity.this, Register_Activity.class);
				startActivity(intent);
			}
		});
		
		btn_LoginFB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Login_Activity.this, LoginUsingLoginFragmentActivity.class);
				startActivityForResult(intent, LoginFB_Code);
			}
		});
		
		btn_LoginGG.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mGoogleApiClient.isConnected()) 
				{				      
					// Prior to disconnecting, run clearDefaultAccount().
					Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
					Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
					    .setResultCallback(new ResultCallback<Status>() {
					   

					@Override
					public void onResult(Status result) {
						// TODO Auto-generated method stub
						// mGoogleApiClient is now disconnected and access has been revoked.
					    // Trigger app logic to comply with the developer policies
					}

					});

				    
				}

			}
		});
			
		SignInButton btn = (SignInButton) findViewById(R.id.sign_in_button);	
		//btn.setSize(SignInButton.SIZE_ICON_ONLY);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				 
				if (v.getId() == R.id.sign_in_button && !mGoogleApiClient.isConnecting()) 			  
				{			    
					mSignInClicked = true;			    
					resolveSignInError();			  
				}
			}
		});
	}				
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		//uiHelper.onActivityResult(requestCode, resultCode, data);
		if(requestCode == LoginFB_Code && resultCode == RESULT_OK)
		{
			loginSocial = 1;
			TaskLogin task = new TaskLogin();
			task.execute(data.getStringExtra("fbID"));
			//if(Login.LoginSocial("facebook", data.getStringExtra("fbID"), Login_Activity.this))
				//finish();
		}
		
		/*if (requestCode == RC_SIGN_IN) 
		{
		    mIntentInProgress = false;
		    if (!mGoogleApiClient.isConnecting()) 
		    {		      
		    	mGoogleApiClient.connect();
		    }		  
		}*/
		
		if (requestCode == RC_SIGN_IN) 
		{
		    if (resultCode != RESULT_OK) 
		    {		      
		    	mSignInClicked = false;
		    }

		    mIntentInProgress = false;
		    if (!mGoogleApiClient.isConnecting()) 
		    {		      
		    	mGoogleApiClient.connect();
		    }		  
		}


	}	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mGoogleApiClient.connect();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    if(Game3Lib.id.compareTo("") != 0)
	    {
	    	finish();
	    }
	    	
	    /*Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    uiHelper.onResume();*/
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    //uiHelper.onPause();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mGoogleApiClient.isConnected())		      
			mGoogleApiClient.disconnect();

	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    //uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    //uiHelper.onSaveInstanceState(outState);
	}		
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		/*if (!mIntentInProgress && result.hasResolution()) 
		{		    
			try 
			{		      
				mIntentInProgress = true;		      
				startIntentSenderForResult(result.getResolution().getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
		    } catch (SendIntentException e) {		      
		    	// The intent was canceled before it was sent.  Return to the default		      
		    	// state and attempt to connect to get an updated ConnectionResult.		      
		    	mIntentInProgress = false;		      
		    	mGoogleApiClient.connect();
		    }		  
		}*/
		
		if (!mIntentInProgress) {
		    // Store the ConnectionResult so that we can use it later when the user clicks 'sign-in'.		   
		    mConnectionResult = result;

		    if (mSignInClicked) {
		      // The user has already clicked 'sign-in' so we attempt to resolve all
		      // errors until the user is signed in, or they cancel.
		      resolveSignInError();
		    }
		  }
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		// We've resolved any connection errors.  mGoogleApiClient can be used to
		// access Google APIs on behalf of the user.	
		mSignInClicked = false;		  
		//Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		/*Plus.PeopleApi.loadVisible(mGoogleApiClient, null)
			.setResultCallback(this);*/

		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) 
		{
		    Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		    //String personName = currentPerson.getDisplayName();
		    String ggID = currentPerson.getId();
		    //String personPhoto = currentPerson.getImage();
		    //String personGooglePlusProfile = currentPerson.getUrl();
		    
		    loginSocial = 2;
		    TaskLogin task = new TaskLogin();
		    task.execute(ggID);
		    //if(Login.LoginSocial("google", ggID, Login_Activity.this))
				//finish();
		}
		
		if (mGoogleApiClient.isConnected()) 
		{				      
			//Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);				      
			//mGoogleApiClient.disconnect();				
			//mGoogleApiClient.connect();
			// Prior to disconnecting, run clearDefaultAccount().
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
			    .setResultCallback(new ResultCallback<Status>() {
			   

			@Override
			public void onResult(Status result) {
				// TODO Auto-generated method stub
				// mGoogleApiClient is now disconnected and access has been revoked.
			    // Trigger app logic to comply with the developer policies
			}

			});
		}
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		mGoogleApiClient.connect();
	}
	
	/*@Override
	public void onResult(LoadPeopleResult peopleData) {
	  if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
	    PersonBuffer personBuffer = peopleData.getPersonBuffer();
	    try {
	      int count = personBuffer.getCount();
	      for (int i = 0; i < count; i++) {
	        Log.d("", "Display name: " + personBuffer.get(i).getDisplayName());
	      }
	    } finally {
	      personBuffer.close();
	    }
	  } else {
	    Log.e("", "Error requesting visible circles: " + peopleData.getStatus());
	  }
	}*/

	class TaskLogin extends AsyncTask<String, Void, Integer>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();			
			mProgress = ProgressDialog.show(Login_Activity.this, "", "Đang xử lý...");
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub	
			if(loginSocial > 0)
			{
				return Login.LoginSocial(Login_Activity.this, Social[loginSocial], params[0]);
			}
			return Login.LoginGame3(Login_Activity.this, params[0], params[1]);			
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(mProgress.isShowing())
				mProgress.dismiss();
			
			if(result == Login.LOGIN_SUCCESS)
			{				
				Login_Activity.this.finish();
			}
			else if(result == Login.LOGIN_WRONG_PASSWORD)
			{
				Toast toast = Toast.makeText(Login_Activity.this, "Tên đăng nhập không tồn tại hoặc sai mật khẩu!", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();				
				//UrlImageViewHelper.setUrlDrawable(imgCaptcha, captcha_url);
			}
			else
			{
				Toast toast = Toast.makeText(Login_Activity.this, "Đăng nhập thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();				
			}
		}
	}
}
