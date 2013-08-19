package com.ga2oo.palendar;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.Tools;
import com.ga2oo.palendar.controls.PopUpDailog;
import com.ga2oo.palendar.objects.UserAccount;
import com.ga2oo.palendar.objects.UserCurrentLocationBasedOnZip;
import com.ga2oo.palendar.objects.UserLocationObject;
import com.ga2oo.palendar.xmlparsers.ParseUserCurrentLocationBasedOnZip;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;
import com.ga2oo.parsing.net.HttpHelper;
import com.ga2oo.parsing.net.LocationUtility;
import com.ga2oo.parsing.net.LocationUtility.LocationResult;

public class ProfileSettings extends Activity implements LocationResult
{
	private static final String DECIMALFORMAT = "#.#######";
	private static final String HOME = "Home";
	private static final String PROFILE = "Profile";
	private static final String REMOVE = "remove";
	private static final String UPDATE = "update";
	private static final String NONE = "None";
	private static final String TRUE2 = "true";
	private static final String TRUE = "True";
	private static final String MYMAPACTIVITY = "MyMapActivity";
	private static final String ADDNEWLOCATION = "addNewLocation";
	private static final String STRTO = "strTo";
	private static final String GEOPOINT = "GeoPoint";
	private static final String PROFILESETTINGS = "ProfileSettings";
	private static final String FROMACTIVITY = "fromActivity";
	public ImageView ivImage;
	private ProgressBar progressBar;
	public static ProfileSettings profileSettings;
	private LinearLayout llSavedLocation;
	public  List<UserAccount> vctUserAccount;
	private UserAccountBusinessLayer useracountBL,useracountBL1;
	public List<UserLocationObject> vctSavedLocation;
	private PopUpDailog customDialog;
	private LinearLayout llSavedLoc;
	private DrawableManager drawManager ;
	private String userImageUrl;
	private EditText etEmail,etFirstName,etLastName;
	private TextView etUsername;
	private List<com.ga2oo.palendar.objects.UpdateProfile> vctUpdateData=null;
	private String geoCode="";
	private AlertDialog alertDialog;
	private RelativeLayout rellayAddLocation;
	private double lattitude,longitude;
	LocationUtility locationUtility  = new LocationUtility();
	private  Geocoder geocoder;
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		drawManager = new DrawableManager();
		profileSettings = this;
		setContentView(R.layout.profile_view);
		locationUtility.getLocation(this, this);
		
		alertDialog 	= new AlertDialog.Builder(getParent()).create();
		alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				alertDialog.dismiss();
			}
		});
		
		etEmail			= (EditText)findViewById(R.id.etEmail);
		etFirstName		= (EditText)findViewById(R.id.etFirstName);
		etLastName		= (EditText)findViewById(R.id.etLastName);
		etUsername		= (TextView)findViewById(R.id.etUsername);
		rellayAddLocation=(RelativeLayout)findViewById(R.id.rellayAddLocation);
		rellayAddLocation.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showLocation();
				Intent intent = new Intent(ProfileSettings.this, com.ga2oo.palendar.MyMapActivity.class);
	            intent.putExtra(FROMACTIVITY, PROFILESETTINGS);
	            intent.putExtra(GEOPOINT, geoCode);
	            intent.putExtra(STRTO, ADDNEWLOCATION);
	            TabGroupActivity pActivity = (TabGroupActivity)ProfileSettings.this.getParent();
	            pActivity.startChildActivity(MYMAPACTIVITY, intent);
			}
		});
		
		useracountBL1=new UserAccountBusinessLayer();
		vctSavedLocation=new ArrayList<UserLocationObject>();
		llSavedLocation=(LinearLayout)findViewById(R.id.llSavedLocation);
		
		ivImage = (ImageView)findViewById(R.id.ivImage);
		progressBar=(ProgressBar)findViewById(R.id.profile_view_progress_bar);
		
		vctUserAccount=new ArrayList<UserAccount>();
		vctUserAccount= useracountBL1.getUserInformation();
		if(vctUserAccount.size()!=0)
		{
			etEmail.setText(vctUserAccount.get(0).email);
			etFirstName.setText(vctUserAccount.get(0).firstname);
			etLastName.setText(vctUserAccount.get(0).lastname);
			etUsername.setText(vctUserAccount.get(0).username);
			userImageUrl=AppConstants.IMAGE_HOST_URL+vctUserAccount.get(0).imagesrc;
			if(!userImageUrl.equals(AppConstants.NOIMAGE))
			{
				if(!drawManager.fetchDrawableOnThread(userImageUrl, ivImage,90,90,vctUserAccount.get(0).imageId,vctUserAccount.get(0).isImageUpdated)){
					ivImage.setImageResource(R.drawable.no_image_smal_event);
				}
				progressBar.setVisibility(View.GONE);
			}
			else
			{
				progressBar.setVisibility(View.GONE);
				ivImage.setImageResource(R.drawable.no_image_smal_event);
			}
		}
		ivImage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Tools.selectedImg ="";
				Intent edit = new Intent(ProfileSettings.this,GallaryActivity.class);
				ProfileSettings.this.startActivity(edit);
				
			}
		});
		
		useracountBL=new UserAccountBusinessLayer();
		vctSavedLocation=useracountBL.getUserSavedLocation(AppConstants.USER_ID);
		if(vctSavedLocation.size()==0)
		{
			
		}
		else
		{
			for(int i=0;i<vctSavedLocation.size();i++)
			{
				llSavedLoc = (LinearLayout) getLayoutInflater().inflate(R.layout.list_savedlocation, null);
				llSavedLocation.addView(llSavedLoc, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				TextView tvlocationName=(TextView)llSavedLoc.findViewById(R.id.tvlocationName);
				RelativeLayout relayProfile=(RelativeLayout)llSavedLoc.findViewById(R.id.rellayProfile);
				Button btnRemoveLocation=(Button)llSavedLoc.findViewById(R.id.btnRemoveLocation);
				try
				{
					if(vctSavedLocation.get(i).Is_Primary.equals(TRUE)||vctSavedLocation.get(i).Is_Primary.equals(TRUE2))
						geoCode=vctSavedLocation.get(i).GeoCode;
					int loactionId=vctSavedLocation.get(i).locationId;
					llSavedLoc.setId(loactionId);
					btnRemoveLocation.setId(loactionId);
					tvlocationName.setId(loactionId+100);
					if(i==0)
						relayProfile.setBackgroundResource(R.drawable.list_bg_one);
					else if(i>0)
						relayProfile.setBackgroundResource(R.drawable.list_bg_two);
					
					if(!vctSavedLocation.get(i).City.equals(NONE)&&!vctSavedLocation.get(i).Zip.equals(NONE))
						tvlocationName.setText(vctSavedLocation.get(i).City+","+vctSavedLocation.get(i).Zip);
					if(!vctSavedLocation.get(i).City.equals(NONE)&&vctSavedLocation.get(i).Zip.equals(NONE))
						tvlocationName.setText(vctSavedLocation.get(i).City);
					if(vctSavedLocation.get(i).City.equals(NONE)&&!vctSavedLocation.get(i).Zip.equals(NONE))
						tvlocationName.setText(vctSavedLocation.get(i).Zip);
					if(vctSavedLocation.get(i).City.equals(NONE)&&vctSavedLocation.get(i).Zip.equals(NONE))
						tvlocationName.setText(vctSavedLocation.get(i).Country);
				}
				catch(Exception ee)
				{
					
				}
				llSavedLoc.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						int loactionId=v.getId();
						showConfirmationPopup(loactionId,UPDATE);
					}
				});
				
				btnRemoveLocation.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						int loactionId=	v.getId();
						showConfirmationPopup(loactionId,REMOVE);
					}
				});
			}
		}
    }
	
	
	
	public void showConfirmationPopup(final int locationId,final String flag)
	{
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.confirmation_popup, null);
		Button btnYes = (Button)popup.findViewById(R.id.btnYes);
		Button btnNo = (Button)popup.findViewById(R.id.btnNo);
		TextView tvMessage=(TextView)popup.findViewById(R.id.tvMessage);
		
		if(flag.equals(UPDATE))
			tvMessage.setText(getResources().getString(R.string.sure_make_location_primary));
		else
			tvMessage.setText(getResources().getString(R.string.sure_to_remove_location));
		
		btnYes.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				if(flag.equals(UPDATE))
				{
//				int status=objGa2ooParsers.changeUserPrimaryLocation(locationId);
				int status = Ga2ooJsonParsers.changeUserPrimaryLocation(locationId);
				useracountBL=new UserAccountBusinessLayer();
				useracountBL.updatePrimaryLocation(locationId);
				}
				else if(flag.equals(REMOVE))
				{
//					int status=objGa2ooParsers.removeSavedLocation(locationId);
					int status = Ga2ooJsonParsers.removeSavedLocation(locationId);
					LinearLayout llSavedLocation=(LinearLayout)findViewById(locationId);
					llSavedLocation.removeAllViews();
					useracountBL=new UserAccountBusinessLayer();
					useracountBL.removeSavedLocation(locationId);
				}
				customDialog.dismiss();
			}
		});
		
		btnNo.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
			}
		});
		customDialog = new PopUpDailog(getParent(), popup, 220, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}	
	
	@Override
    public void onResume()
    {
    	super.onResume();    	
        TabsActivity.btnBack.setVisibility(View.VISIBLE);
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar_empty);
        TabsActivity.tvTitle.setText(getResources().getString(R.string.profile));
        TabsActivity.tvTitle.setVisibility(View.VISIBLE);
        TabsActivity.tvTitle.setGravity(Gravity.CENTER);
        TabsActivity.btnLogout.setBackgroundResource(R.drawable.done);
        TabsActivity.rlCommonTitleBar.invalidate();

        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				TabGroupActivity pActivity = (TabGroupActivity)ProfileSettings.this.getParent();
				pActivity.finishFromChild(null);
			}
		});
        
        TabsActivity.btnLogout.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				vctUpdateData=new ArrayList<com.ga2oo.palendar.objects.UpdateProfile>();
				com.ga2oo.palendar.objects.UpdateProfile objupdateProfile=new com.ga2oo.palendar.objects.UpdateProfile();
				objupdateProfile.strFName=etFirstName.getText().toString();
				objupdateProfile.strLName=etLastName.getText().toString();
				objupdateProfile.strEmail=etEmail.getText().toString();
				objupdateProfile.strUserName=etUsername.getText().toString();
				vctUpdateData.add(objupdateProfile);
	//			int updateStatusStatus=register.updateUserProfile(vctUpdateData);
				int updateStatusStatus = Ga2ooJsonParsers.updateUserProfile(vctUpdateData);
                AppConstants.isProfileUpdated=true;

				if(updateStatusStatus>0)
				{
					 alertDialog.setTitle(getResources().getString(R.string.success));
					 alertDialog.setMessage(Ga2ooJsonParsers.updateUserProfileMgs);
					 alertDialog.show();
					 useracountBL.updateUser(vctUpdateData,PROFILE);
				}
				else
				{
					 alertDialog.setTitle(getResources().getString(R.string.failure));
					 alertDialog.setMessage(getResources().getString(R.string.try_again));
					 alertDialog.show();
				}
				alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						TabGroupActivity pActivity = (TabGroupActivity)ProfileSettings.this.getParent();
						pActivity.finishFromChild(null);
						alertDialog.dismiss();
					}
				});
			}
		});
    }
	public void showLocation()
	{
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.location, null);
		Button btnYes = (Button)popup.findViewById(R.id.btnYes);
		Button btnNo = (Button)popup.findViewById(R.id.btnNo);
		TextView tvMessage=(TextView)popup.findViewById(R.id.tvMessage);
		tvMessage.setText(getResources().getString(R.string.select_location));
		
		btnYes.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				Log.i("lattitude home",""+lattitude);
				getCurrentLoacationAddress(lattitude,longitude);
				Intent intent = new Intent(ProfileSettings.this, MyMapActivity.class);
	            intent.putExtra(FROMACTIVITY, HOME);
	            intent.putExtra(GEOPOINT, longitude+","+lattitude);
	            intent.putExtra(STRTO, ADDNEWLOCATION);
	            TabGroupActivity pActivity = (TabGroupActivity)ProfileSettings.this.getParent();
	            pActivity.startChildActivity(MYMAPACTIVITY, intent);
				customDialog.dismiss();
			}
		});
			
		btnNo.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
				showPoptoEnterZip();
			}
		});
		customDialog = new PopUpDailog(getParent(), popup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}	
	public void showPoptoEnterZip()
	{
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.enterzippopup, null);
		Button btnOk = (Button)popup.findViewById(R.id.btnOk);
		Button btnCancel = (Button)popup.findViewById(R.id.btnCancel);
		final EditText etZip	=	(EditText)popup.findViewById(R.id.etZip);
		TextView tvMessage=(TextView)popup.findViewById(R.id.tvMessage);
		tvMessage.setText(getResources().getString(R.string.enter_zip));
		
		btnOk.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				if(etZip.getText().toString().equals("")||etZip.getText().toString()==null)
				{
					alertDialog.setTitle(getResources().getString(R.string.enter_zip));
					alertDialog.setMessage(getResources().getString(R.string.please_enter_zip));
					alertDialog.show();
				}
				else
				{
					getLocationFromZipCode(etZip.getText().toString());
					Intent intent = new Intent(ProfileSettings.this, MyMapActivity.class);
		            intent.putExtra(FROMACTIVITY, HOME);
		            if(AppConstants.vctUserCurrentLocation!=null&& AppConstants.vctUserCurrentLocation.size()!=0)
		            	intent.putExtra(GEOPOINT,AppConstants.vctUserCurrentLocation.get(0).strGeoCode);
		            else
		            	intent.putExtra(GEOPOINT,"");
		            intent.putExtra(STRTO, ADDNEWLOCATION);
		            TabGroupActivity pActivity = (TabGroupActivity)ProfileSettings.this.getParent();
		            pActivity.startChildActivity(MYMAPACTIVITY, intent);
					customDialog.dismiss();
				}
			}
		});
			
		btnCancel.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
			}
		});
		customDialog = new PopUpDailog(getParent(), popup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}	

	public void getLocationFromZipCode(String zipcode)
	{
		ParseUserCurrentLocationBasedOnZip objUserCurrentLocation	=	new ParseUserCurrentLocationBasedOnZip();
		HttpHelper helper=new HttpHelper();
		try
		{
			helper.settingUrlConnection(new URL(AppConstants.strAPIURL+zipcode), objUserCurrentLocation);
			Log.i("URL",""+AppConstants.strAPIURL+zipcode);
		}
		catch (Exception e)
		{
			// TODO: handle exception
			Log.i("error in parsing zip code based location",""+e.toString());
		}
	}
	@Override
	public void gotLocation(Location loc) 
	{
		// TODO Auto-generated method stub
		if(loc!=null)
		{
			locationUtility.stopGpsLocUpdation();
			DecimalFormat df = new DecimalFormat(DECIMALFORMAT);
			lattitude=Double.parseDouble(df.format(loc.getLatitude()));
			longitude=Double.parseDouble(df.format(loc.getLongitude()));
		}
		
	}
	public void getCurrentLoacationAddress( double LATITUDE, double LONGITUDE)
    {
		geocoder = new Geocoder(this, Locale.ENGLISH);
		try 
		{
			Log.i("LATITUDE",""+LATITUDE);
			List<Address> addresses = geocoder.getFromLocation(LATITUDE,LONGITUDE,1);
			UserCurrentLocationBasedOnZip objCurrentLocation=new UserCurrentLocationBasedOnZip();
			if (addresses.size()> 0)
			{
				if(AppConstants.vctUserCurrentLocation==null)
					AppConstants.vctUserCurrentLocation=new ArrayList<UserCurrentLocationBasedOnZip>();
				else if(AppConstants.vctUserCurrentLocation!=null)
					AppConstants.vctUserCurrentLocation.clear();
				objCurrentLocation.strCountryName = addresses.get(0).getCountryName();
				objCurrentLocation.strAddress=addresses.get(0).getAddressLine(0);
				objCurrentLocation.strCity=addresses.get(0).getLocality();
				if(addresses.get(0).getPostalCode()!=null)
					objCurrentLocation.zipCode=addresses.get(0).getPostalCode();
				else
					objCurrentLocation.zipCode=NONE;
				objCurrentLocation.strState=addresses.get(0).getAdminArea();
				objCurrentLocation.strGeoCode=""+LATITUDE+","+LONGITUDE;
				AppConstants.vctUserCurrentLocation.add(objCurrentLocation);
			}
			else
			{
				Log.i("","no adress returned");
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}             
    }
}
