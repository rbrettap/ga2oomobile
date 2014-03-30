package com.ga2oo.palendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.LoginButton;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;
import com.flurry.android.FlurryAgent;
import com.google.gson.JsonElement;
import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.DialogUtility;
import com.ga2oo.palendar.objects.Business;
import com.ga2oo.palendar.objects.EventsDetails;
import com.ga2oo.palendar.objects.FavoriteEvent;
import com.ga2oo.jsonparsers.BusinessAccountsWrapper;
import com.ga2oo.jsonparsers.BusinessToFavoriteWrapper;
import com.ga2oo.jsonparsers.EventsWrapper;
import com.ga2oo.jsonparsers.FriendsWrapper;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;
import com.ga2oo.jsonparsers.RecoverPasswordWrapper;
import com.ga2oo.jsonparsers.UserAccountWrapper;
import com.ga2oo.jsonparsers.UserLocationWrapper;
import com.ga2oo.parsing.net.JsonHttpHelper;

public class Login extends Activity 
{
	
	public static final String CURRENTSCREEN = "CurrentScreen";
	public static final String LOGIN = "Login";
	public static final String RECOVERPASS = "Recover";
	public static final String LOGTAG = "LoginScreen";
	public static final String FACEBOOK = "Facebook";
	public static final String USERABOUTME = "user_about_me";
	public static final String USERBIRTHDAY = "user_birthday";
	public String ACCESSTOKEN = "access_token";
	
	private static final String ID ="id";
	private static final String FIRASTNAME = "first_name";
	private static final String LASTNAME= "last_name";
	private static final String USERNAME2 ="username";
	private static final String BIRTHDAY = "birthday";
	private static final String GENDER = "gender";
	private static final String SELECTEDID= "selectedId";
	
	public static String USERNAME = "";
	
	private Button btnLogin, btnRegister;
	private LoginButton btnLoginFacebook;
	private EditText etEmail, etPassword;
	private ImageView ivClearusername,ivClearPassword;
	private LinearLayout llLoginMain;
	private TextView forgotPassword;
	
	private String password,username; 
	
	public static List<com.ga2oo.palendar.objects.Events> vctEvents;
	private AlertDialog.Builder InternetPopup, dialog;
	private AlertDialog alert;
	JsonHttpHelper jsonHelper;
	JsonElement element;
	UserAccountBusinessLayer userLayer;
	
	private Facebook mFacebook;
    private AsyncFacebookRunner mAsyncRunner; 
    
    private AlertDialog alertDialog;
    public int code=-2;
    
    private static final String kLogTag = FlurryAgent.class.getSimpleName();
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(this.getIntent().getExtras()==null || LOGIN.equals(this.getIntent().getExtras().getString(CURRENTSCREEN))){
		setContentView(R.layout.login);
		llLoginMain		= (LinearLayout) findViewById(R.id.llLoginMain);
		llLoginMain		= (LinearLayout) findViewById(R.id.llLoginMain);
		etEmail 		= (EditText) findViewById(R.id.loginUsername);
		etPassword 		= (EditText) findViewById(R.id.etPassword);
		ivClearusername	=(ImageView)findViewById(R.id.ivClearUsername);
		ivClearPassword	=(ImageView)findViewById(R.id.ivClearPassword);
		btnRegister 	= (Button) findViewById(R.id.btnRegister);
		btnLogin 		= (Button) findViewById(R.id.btnLogin);
		btnLoginFacebook= (LoginButton) findViewById(R.id.facebook_login_button);
		forgotPassword = (TextView) findViewById(R.id.frogot_password);

		jsonHelper = JsonHttpHelper.getInstance();
		userLayer = new UserAccountBusinessLayer();
		
		llLoginMain.setOnClickListener(new OnClickListener(){

			
			@Override
			public void onClick(View v){
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
			}
		});
		
		btnRegister.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Login.this,Registration.class);
				startActivity(intent);				
			}
			
		});
		
		//facebook
		if (AppConstants.APP_ID == null) 
        {
   //         Util.showAlert(Login.this, "Warning", "Facebook Applicaton ID must be specified before running this example: see Example.java");
        }else{
        	mFacebook = new Facebook(AppConstants.APP_ID);
           	mAsyncRunner = new AsyncFacebookRunner(mFacebook);
            SessionStore.restore(mFacebook, Login.this);
            SessionEvents.addAuthListener(new SampleAuthListener());
 //           SessionEvents.addLogoutListener(new SampleLogoutListener());
            btnLoginFacebook.init(Login.this, mFacebook, new String[]{USERABOUTME,USERBIRTHDAY});
        }
		
		dialog			= new AlertDialog.Builder(this);
	    dialog.setCancelable(true);
	    dialog.setPositiveButton(getResources().getString(R.string.ok),new DialogInterface.OnClickListener() 
	    {		
	    	public void onClick(DialogInterface dialog, int which)
	    	{
	    		dialog.dismiss();				
	    	}
	    });

	    etEmail.addTextChangedListener(new TextWatcher()
	    {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if(!etEmail.getText().toString().equals("") && etEmail.isFocused())
					ivClearusername.setVisibility(View.VISIBLE);
				else
					ivClearusername.setVisibility(View.GONE);		
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after)
			{
				if(!etEmail.getText().toString().equals("") && etEmail.isFocused())
					ivClearusername.setVisibility(View.VISIBLE);
				else
					ivClearusername.setVisibility(View.GONE);	
			}
			@Override
			public void afterTextChanged(Editable s)
			{
			}
		});
	    //TODO: delete this
	    etEmail.setText("rbrett2010");
	    etPassword.setText("1r303b");
//	    etEmail.setText(USERNAME);
	    
	    etEmail.setOnFocusChangeListener(new OnFocusChangeListener()
	    {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if(etEmail.isFocused()&& !etEmail.getText().toString().equals(""))
					ivClearusername.setVisibility(View.VISIBLE);	
				else if(!etEmail.isFocused())
					ivClearusername.setVisibility(View.GONE);	

			}
		});
	    etPassword.addTextChangedListener(new TextWatcher()
	    {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if(!etPassword.getText().toString().equals("") && etPassword.isFocused())
					ivClearPassword.setVisibility(View.VISIBLE);
				else
	    			ivClearPassword.setVisibility(View.GONE);		
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after)
			{
				if(!etPassword.getText().toString().equals("") && etPassword.isFocused())
					ivClearPassword.setVisibility(View.VISIBLE);
				else
	    			ivClearPassword.setVisibility(View.GONE);	
			}
			@Override
			public void afterTextChanged(Editable s)
			{
			}
		});
	       
	    etPassword.setOnFocusChangeListener(new OnFocusChangeListener()
	    {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if(!etPassword.getText().toString().equals("") && etPassword.isFocused())
					ivClearPassword.setVisibility(View.VISIBLE);
				else if(!etPassword.isFocused())
					ivClearPassword.setVisibility(View.GONE);	
			}
		});
	    ivClearusername.setOnClickListener(new OnClickListener()
	    {
			@Override
			public void onClick(View v)
			{
				etEmail.setText("");
			}
	    });
	    ivClearPassword.setOnClickListener(new OnClickListener()
	    {
	    	@Override
	    	public void onClick(View v)
	    	{
	    		etPassword.setText("");
	    	}
	    });
	    etPassword.setOnEditorActionListener(new OnEditorActionListener() 
	    {
	        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
	        {
	            if (actionId == EditorInfo.IME_ACTION_GO) 
	            {
	            	btnLogin.performClick();
	            }
	            return true;
	        }
	    });
	    if (getResources().getConfiguration().keyboardHidden != Configuration.KEYBOARDHIDDEN_YES)
		{
			Log.i("inside","inside");
		}
		btnLogin.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				ivClearusername.setVisibility(View.GONE);	
				ivClearPassword.setVisibility(View.GONE);	
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
				
				username = etEmail.getText().toString();
				password = etPassword.getText().toString();

				if(username.equals("")|| password.equals(""))
			    {
					if(username.equals(""))
					{
						dialog.setMessage(getResources().getString(R.string.enter_username_again));
			    		dialog.show();
			    	}
			    	else
			    	{
			    		dialog.setMessage(getResources().getString(R.string.enter_password_again));
			    		dialog.show();
			    	}
			    }
				else
				{
						ConnectivityManager connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
						boolean connected = (connec.getActiveNetworkInfo() != null &&
											 connec.getActiveNetworkInfo().isAvailable() &&
											 connec.getActiveNetworkInfo().isConnected());
						if(!connected)
						{
							InternetPopup =  new Builder(Login.this);
							InternetPopup.setMessage(getResources().getString(R.string.network_unavailable_for_login));
							alert=InternetPopup.create();
							InternetPopup.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() 
							{
					            public void onClick(DialogInterface dialog, int which) 
					            {             	
					            	alert.dismiss();
					            	finish();
					            }
							});	
							InternetPopup.show();
						}
						else
						{
							new Load().execute();
						}
				}
			}
		});		
		
		forgotPassword.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Login.this, Login.class);
				intent.putExtra(CURRENTSCREEN, RECOVERPASS);
				startActivity(intent);
			}
			
		});
		}else{
			if(RECOVERPASS.equals(this.getIntent().getExtras().getString(CURRENTSCREEN))){
				setContentView(R.layout.forgot_password);
				Button recover = (Button) findViewById(R.id.btnSendPass);
				recover.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						EditText email = (EditText)Login.this.findViewById(R.id.etEmail);
						String strEmail = email.getText().toString();
					if(emailAddressValidation(strEmail)){
							new SendReciverPass().execute();	
						}else{
							alertDialog.setMessage(getResources().getString(R.string.email_is_not_valid));
							  alertDialog.show();
						}
					}
					
				});
				
				Button back = (Button)findViewById(R.id.btnBack);
				back.setOnClickListener(new OnClickListener(){
	
					@Override
					public void onClick(View arg0) {
						Login.this.finish();
						
					}
					
				});
			}
		}
		
		alertDialog = new AlertDialog.Builder(Login.this).create();
		alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				alertDialog.dismiss();
				if(code==0){
					Login.this.finish();
				}
			}
		});
	}
	
    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, AppConstants.Flurry_Api_Key.toString());
        Log.d(kLogTag, "onStart");
    }
    
    
    @Override
    protected void onStop() {
        super.onStop();

        FlurryAgent.onEndSession(this);
        Log.d(kLogTag, "onStop");
    }

	
	public boolean emailAddressValidation(String strEmail)
	  {
		  Matcher matcher;
		  String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
		  Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);  
		  matcher = pattern.matcher(strEmail); 
		  if(strEmail.length()>0)
		  {
			  if(!matcher.matches()) 
				  return false;
			  else
				  return true;
		  }
		  else
			  return false;
	  }
	
	public class SendReciverPass extends AsyncTask<Void,Void,Boolean>{
		
		ProgressDialog dialog = new ProgressDialog(Login.this);
		JsonHttpHelper jsonHelper;
		JsonElement element;
		EditText email = (EditText)Login.this.findViewById(R.id.etEmail);
		
		String message = null;
		@Override
		protected void onPreExecute() {
			jsonHelper = JsonHttpHelper.getInstance();
			dialog.setMessage(getResources().getString(R.string.sending));
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.RECOVER_PASSWORD+email.getText());
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
			Object usersObjects = jsonHelper.parse(element, RecoverPasswordWrapper.class);
			if(usersObjects!=null){
				code = ((RecoverPasswordWrapper)usersObjects).getUseraccount().code;
				message = ((RecoverPasswordWrapper)usersObjects).getUseraccount().message;
				return true;
			}else{
				return false;
			}
			
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dialog.dismiss();
			if(result){
				alertDialog.setMessage(message);
				alertDialog.show();
			}else{
				DialogUtility.showConnectionErrorDialog(Login.this);
			}
			
			
			super.onPostExecute(result);
		}

		
	}
	
	public class SampleAuthListener implements AuthListener 
    {
        public void onAuthSucceed() 
        {
        	Log.v(FACEBOOK, "auth success");
        	//From facebook
    			Bundle parameters = new Bundle();
    			parameters.remove(ACCESSTOKEN);
    			parameters.putString(ACCESSTOKEN, mFacebook.getAccessToken());
    			mAsyncRunner.request("me", parameters, "GET", new GetUserDataListener());
        }

        public void onAuthFail(String error) 
        {
          
        }
    }
	
	public class GetUserDataListener extends BaseRequestListener
	{
		
		@Override
		public void onComplete(String response) {
			Log.v(FACEBOOK, "response = "+response);
			JSONObject json = null;
			try {
				json = Util.parseJson(response);
		
			}catch(Exception e){
				e.printStackTrace();
			} catch (FacebookError e) {
				e.printStackTrace();
			}
				Intent intent = new Intent(Login.this, RegistrationforPersonal.class);
				try {
					intent.putExtra(ID, (json.getString(ID)!=null?json.getString(ID):"0"));
					intent.putExtra(FIRASTNAME, (json.getString(FIRASTNAME)!=null?json.getString(FIRASTNAME):""));
					intent.putExtra(LASTNAME, (json.getString(LASTNAME)!=null?json.getString(LASTNAME):""));
					intent.putExtra(USERNAME2, (json.getString(USERNAME2)!=null?json.getString(USERNAME2):""));
					intent.putExtra(BIRTHDAY, (json.get(BIRTHDAY)!=null?json.get(BIRTHDAY):"").toString());
					intent.putExtra(GENDER, (json.get(GENDER)!=null?json.get(GENDER):"").toString());
				
				}catch(Exception e){
					e.printStackTrace();
				}
	        	startActivity(intent);
			
        	
		}

	}

    public class SampleLogoutListener implements LogoutListener 
    {
        public void onLogoutBegin() 
        {
        	//pdLoading = ProgressDialog.show(Example.this, "", "Loading...");
        	//mText.setText("Logging out...");
        }

        public void onLogoutFinish() 
        {
            Log.e("Facebook","I am in LogoutListener :"+ "Line NO :187");
 //       	mPostButton.setVisibility(View.INVISIBLE);
        }
    }

	public class Load extends AsyncTask<Void,Void,Boolean>{

		int status;
		ProgressDialog progDialog = new ProgressDialog(Login.this);
		@Override
		protected void onPreExecute() {
			status=Ga2ooJsonParsers.loginStatus(username, password);
			// for some reason the status = 0 unless this is done twice....
			progDialog.setMessage(getResources().getString(R.string.authenticating_please_wait));
			progDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Log.i(LOGTAG, "status = "+status);
			if(status > 0)
			{
				USERNAME = username;
				AppConstants.USER_ID = status;
					//get user account data
					Log.i(LOGTAG, "Loading user account data...");
					try {
						element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.USER_ACCOUNT_URL+AppConstants.USER_ID);
					} catch (Exception e) {
						Log.e(LOGTAG, "Error in Loading user account data!");
						e.printStackTrace();
						return false;
					}
					
					Object userObjects = jsonHelper.parse(element, UserAccountWrapper.class);
					if(userObjects!=null){
						if(((UserAccountWrapper)userObjects).getUserAccount()!=null){
							userLayer.Insert(((UserAccountWrapper)userObjects).getUserAccount());
						}else{
							return false;
						}
						Log.i(LOGTAG, "Loading user account data successfully completed.");
					}
					// get all friends data
					Log.i(LOGTAG, "Loading user friends data...");
					try {
						element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.FRIEND_URL+AppConstants.USER_ID);
					} catch (Exception e) {
						Log.e(LOGTAG, "Error in Loading user friends data!");
						e.printStackTrace();
						return false;
					}
					Object friendObjects = jsonHelper.parse(element, FriendsWrapper.class);
					if(friendObjects!=null){
						if(((FriendsWrapper)friendObjects).getUseraccount()!=null){
							for(int i=0;i<((FriendsWrapper)friendObjects).getUseraccount().friendships.size();i++){
								userLayer.InsertFriend(((FriendsWrapper)friendObjects).getUseraccount().friendships.get(i));
							}
						}else{
							return false;
						}
						Log.i(LOGTAG, "Loading user account data successfully completed.");
					}
						//get location data
						Log.i(LOGTAG, "Loading user locations data...");
						try {
							element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.GET_USER_LOCATION_URL+AppConstants.USER_ID);
						} catch (Exception e) {
							Log.e(LOGTAG, "error in Loading user location data!");
							e.printStackTrace();
							return false;
						}
						Object userLicationObjects = jsonHelper.parse(element, UserLocationWrapper.class);
						if(userLicationObjects!=null){
							if(((UserLocationWrapper)userLicationObjects).getUseraccount().savedlocations!=null){
								for(int i=0;i<((UserLocationWrapper)userLicationObjects).getUseraccount().savedlocations.size();i++){
									userLayer.InsertSavedLocation(((UserLocationWrapper)userLicationObjects).getUseraccount().savedlocations.get(i));
								}
							}else{
								return false;
							}
							Log.i(LOGTAG, "Loading user location data successfully completed.");
						}
						
					//get all event data
					Log.i(LOGTAG, "Loading all events data...");
					EventsBusinessLayer evbl=new EventsBusinessLayer();
					AppConstants.vctEvents = new ArrayList<EventsDetails>();
					try {
						element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_LIST+"?"+AppConstants.DATERANGE+getPeriod());
					} catch (Exception e) {
						Log.e(LOGTAG, "Error in Loading all events data!");
						e.printStackTrace();
						return false;
					}
					
					Object eventsObjects = null;  // RBRB 5-28 this causes a runtime exception
					//Object eventsObjects = jsonHelper.parse(element, EventsWrapper.class);
					if(eventsObjects!=null){
						if(((EventsWrapper) eventsObjects).getEvents()!=null){
							for(int i=0;i<((EventsWrapper) eventsObjects).getEvents().size();i++){
								EventsDetails tmpObject = ((EventsWrapper) eventsObjects).getEvents().get(i);
								AppConstants.vctEvents.add(tmpObject);
								evbl.InsertEventList(((EventsWrapper)eventsObjects).getEvents().get(i));
							}
						}else{
							return false;
						}
						Log.i(LOGTAG, "Loading all events data successfully completed.");
					}
					
					//get all business accounts
					Log.i(LOGTAG, "Loading all business accounts data...");
					UserAccountBusinessLayer userActBL = new UserAccountBusinessLayer();
						try {
							element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.BUSINESS_LIST_URL);
						} catch (Exception e) {
							Log.e(LOGTAG, "Error in Loading business accounts data");
							e.printStackTrace();
							return false;
						} 
						Object businessObjects = jsonHelper.parse(element, BusinessAccountsWrapper.class);
						if(businessObjects!=null){
							if(((BusinessAccountsWrapper)businessObjects).getBusinessaccounts()!=null){
								for(int i=0;i<((BusinessAccountsWrapper)businessObjects).getBusinessaccounts().size();i++)
								{
									userActBL.InsertBusinessList(((BusinessAccountsWrapper)businessObjects).getBusinessaccounts().get(i));
								}
							}else{
								return false;
							}
							Log.i(LOGTAG, "Loading business accounts data successfully completed");
						}
						
						
						AppConstants.vctUserFavoritesBusiness = new ArrayList<Business>();
						AppConstants.vctUserFavorites = new ArrayList<FavoriteEvent>();
						Log.i(LOGTAG, "Loading user favorite businesses.");
						try {
							element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.ADD_BUSINESS+"id/"+AppConstants.USER_ID);
						}
						 catch (Exception e) {
							 Log.e(LOGTAG, "Loading user favorite businesses.");
							e.printStackTrace();
							return false;
						}
						Object favoriteObjects = jsonHelper.parse(element, BusinessToFavoriteWrapper.class);
						if(favoriteObjects!=null){
							if(((BusinessToFavoriteWrapper) favoriteObjects).getUseraccount().fav_businesses!=null){
								AppConstants.vctUserFavoritesBusiness.addAll(((BusinessToFavoriteWrapper) favoriteObjects).getUseraccount().fav_businesses);
								for(int i=0;i<((BusinessToFavoriteWrapper) favoriteObjects).getUseraccount().fav_businesses.size();i++){
									userActBL.InsertUserBusiness(((BusinessToFavoriteWrapper) favoriteObjects).getUseraccount().fav_businesses.get(i));
								}
							}
							Log.i(LOGTAG, "Loading user favorite businesses successfully completed.");
						}
					return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progDialog.dismiss();
			if(result){
				if(status>0){
					finish();
					
					Intent intent = new Intent(Login.this, TabsActivity.class);
					intent.putExtra(SELECTEDID, 0);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
				}
				if(status == -1 || status == -2)
				{
					 dialog.setMessage(getResources().getString(R.string.incorrect_username_or_password));
		    		 dialog.show();
				}
				if(status==0){
					 dialog.setMessage(getResources().getString(R.string.server_is_unavailable));
		    		 dialog.show();
				}
			}else{
				
				DialogUtility.showConnectionErrorDialog(Login.this);
			}
			super.onPostExecute(result);
		}


	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) 
    {
        mFacebook.authorizeCallback(requestCode, resultCode, data);
    }

	public static String getPeriod(){
		StringBuilder result =new StringBuilder();
		Calendar c = Calendar.getInstance();
		int date = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH)+1;
		int year = c.get(Calendar.YEAR);
		Log.i(LOGTAG, "date = "+date);
		Log.i(LOGTAG, "month = "+month);
		Log.i(LOGTAG, "year = "+year);
		result.append((month-1)<1?year-1:year);
		result.append((month-1)<1?"12":((month-1)<10?("0"+(month-1)):month-1));
		result.append(date<10?"0"+date:date);
		result.append((month+1)>12?year+1:year);
		result.append((month+1)>12?"01":((month+1)<10?("0"+(month+1)):month+1));
		result.append(date<10?"0"+date:date);
		Log.i(LOGTAG, "period = "+result);
		return result.toString();				
	}
}

	


