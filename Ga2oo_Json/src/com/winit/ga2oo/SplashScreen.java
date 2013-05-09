package com.winit.ga2oo;

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
import com.winit.ga2oo.businesslayer.BusinessBusinessLayer;
import com.winit.ga2oo.businesslayer.EventsBusinessLayer;
import com.winit.ga2oo.businesslayer.UserAccountBusinessLayer;
import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.databaseaccess.DatabaseHelper;
import com.winit.ga2oo.xmlparsers.CategoryWrapper;
import com.winit.go2oo.jsonparsers.BusinessTypeWrapper;
import com.winit.parsing.net.HttpHelper;
import com.winit.parsing.net.JsonHttpHelper;
import com.winit.parsing.net.LocationUtility;
import com.winit.parsing.net.LocationUtility.LocationResult;

public class SplashScreen extends Activity implements LocationResult
{
	
	private static final String DATEFORMAT = "#.#######";

	public static final String LOGTAG = "SplashScreen";
	
	HttpHelper helper;
	private AlertDialog.Builder InternetPopup;
	private AlertDialog alert;
	private UserAccountBusinessLayer userAccBL;
	LocationUtility locationUtility  = new LocationUtility();
	public static double lattitude,longitude;
	private JsonElement element;
	private JsonHttpHelper jsonHelper;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
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
			
			new GetCategoryesAndBusinessTypes().execute();					
		}
    }
    
    //Creating DB and opening DB
    public void createDataBase()
    {
    	DatabaseHelper dbHelper = new DatabaseHelper(this);
		try
		{
			dbHelper.createDataBase();
//			DatabaseHelper.openDataBase();
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
	
	private class GetCategoryesAndBusinessTypes extends AsyncTask<Void,Void,Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			Log.i(LOGTAG, "Loading categories list and business types.");
			try{
				EventsBusinessLayer eventBL=new EventsBusinessLayer();
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.CATEGORY_LIST);
				Object categoriesObjects = jsonHelper.parse(element, CategoryWrapper.class);
				if(categoriesObjects!=null){
					if(((CategoryWrapper) categoriesObjects).getCaregories()!=null){
						for(int i=0;i<((CategoryWrapper)categoriesObjects).getCaregories().size();i++){
							eventBL.insertCategories(((CategoryWrapper)categoriesObjects).getCaregories().get(i));
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