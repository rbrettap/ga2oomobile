package com.ga2oo.palendar;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.JsonElement;
import com.ga2oo.palendar.businesslayer.BusinessBusinessLayer;
import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.databaseaccess.DatabaseHelper;
import com.ga2oo.jsonparsers.CategoryWrapper;
import com.ga2oo.jsonparsers.BusinessTypeWrapper;
import com.ga2oo.parsing.net.HttpHelper;
import com.ga2oo.parsing.net.JsonHttpHelper;
import com.ga2oo.parsing.net.LocationUtility;
import com.ga2oo.parsing.net.LocationUtility.LocationResult;

import com.flurry.android.*;

public class SplashScreen extends Activity implements LocationResult
{
	
	private static final String DATEFORMAT = "#.#######";

	public static final String LOGTAG = "SplashScreen";
	private static final String kLogTag = FlurryAgent.class.getSimpleName();
	
	HttpHelper helper;
	private AlertDialog.Builder InternetPopup;
	private AlertDialog alert;
	private UserAccountBusinessLayer userAccBL;
	LocationUtility locationUtility  = new LocationUtility();
	public static double lattitude,longitude;
	private JsonElement element;
	private JsonHttpHelper jsonHelper;
	
	private String fApiKey;
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        fApiKey = AppConstants.Flurry_Api_Key.toString();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashscreen);
        userAccBL	=	new UserAccountBusinessLayer();
        jsonHelper = JsonHttpHelper.getInstance();
        
        createDataBase();
        ConnectivityManager connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean connected = (connec.getActiveNetworkInfo() != null &&
							 connec.getActiveNetworkInfo().isAvailable() &&
							 connec.getActiveNetworkInfo().isConnected());
		if(!connected)
		{
			InternetPopup =  new Builder(SplashScreen.this);
			InternetPopup.setMessage(getResources().getString(R.string.network_unavailable_for_login));  
			alert=InternetPopup.create();
			InternetPopup.setNegativeButton(getResources().getString(R.string.ok) , new DialogInterface.OnClickListener() 
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
			//TODO: romove this
//			userAccBL.clearGa2ooUserList();
//			userAccBL.clearData();
			
			new GetCategoriesAndBusinessTypes().execute();					
		}
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, fApiKey);
        Log.d(kLogTag, "onStart");
    }
    
    
    @Override
    protected void onStop() {
        super.onStop();

        FlurryAgent.onEndSession(this);
        Log.d(kLogTag, "onStop");
    }

    //Creating DB and opening DB
    public void createDataBase()
    {
    	DatabaseHelper dbHelper = new DatabaseHelper(this);
		try
		{
			dbHelper.createDataBase();
			//DatabaseHelper.openDataBase();
		}
		catch (Exception e)
		{							
			e.printStackTrace();
		}
    }

	@Override
	public void gotLocation(Location loc) {
		// TODO Auto-generated method stub
		if(loc!=null)
		{
			locationUtility.stopGpsLocUpdation();
			DecimalFormat df = new DecimalFormat(DATEFORMAT);
			lattitude=Double.parseDouble(df.format(loc.getLatitude()));
			longitude=Double.parseDouble(df.format(loc.getLongitude()));
		}
		
	}
	
	private class GetCategoriesAndBusinessTypes extends AsyncTask<Void,Void,Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			Log.i(LOGTAG, "Loading categories list and business types.");
			try{
				EventsBusinessLayer eventBL=new EventsBusinessLayer();
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.CATEGORY_LIST);
				Object categoriesObjects = jsonHelper.parse(element, CategoryWrapper.class);
				if(categoriesObjects!=null){
					if(((CategoryWrapper) categoriesObjects).getCategories()!=null){
						for(int i=0;i<((CategoryWrapper)categoriesObjects).getCategories().size();i++){
							eventBL.insertCategories(((CategoryWrapper)categoriesObjects).getCategories().get(i));
						}
					}
				}
				BusinessBusinessLayer businessBL = new BusinessBusinessLayer();
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.BUSINES_TYPES);
				Object btypesObjects = jsonHelper.parse(element, BusinessTypeWrapper.class);
				if(btypesObjects!=null){
					if(((BusinessTypeWrapper)btypesObjects).businesstypes!=null){
						for(int i=0;i<((BusinessTypeWrapper)btypesObjects).businesstypes.size();i++){
							businessBL.insertBusinesstype(((BusinessTypeWrapper)btypesObjects).businesstypes.get(i));
						}
					}
				}
			}catch(Exception e){
				Log.e(LOGTAG, "Error in loading categories list and business types");
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(!result){
	//			DialogUtility.showConnectionErrorDialog(SplashScreen.this);		
			}else{
				Log.i(LOGTAG, "Loading categories list and business types successfully completed.");
			}
			finish();
//			if(UserAccountBusinessLayer.isUserDataNotEmpty()){
//			Intent intent = new Intent(SplashScreen.this, TabsActivity.class);
//			intent.putExtra(Login.SELECTEDID, 0);
//			startActivity(intent);
//		}else{
			Intent intent = new Intent(SplashScreen.this, Login.class);
			intent.putExtra(Login.CURRENTSCREEN, Login.LOGIN);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
//		}
			super.onPostExecute(result);
		}
		
	}
}