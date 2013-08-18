package com.ga2oo.palendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ga2oo.palendar.businesslayer.BusinessBusinessLayer;
import com.ga2oo.palendar.objects.BusinessRegistration;
import com.ga2oo.palendar.objects.BusinessType;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;

public class RegistrationforBussiness extends Activity 
{
	private RelativeLayout rlBusinessType;
	private TextView tvBusinessType;
	private CharSequence csBusinessType;
	private Button btnBack,btnSubmit;
	private AlertDialog alertDialog;
	private EditText etCompany,etName,etEmail,etPhNo,etPassword,etAddress,etCity,etState,etZip,etCountry;
	
	public String strCompanyName="",strName="",strEmail="",strUserName,strPassword="",strAddress="",strCity="",strState="",strCountry="", strGeocode="", strIsPrimary="";
	private Boolean isEmailAddressValid=false,isDataCorrect=false;
	private Vector<BusinessRegistration> vctBusinessRegistration;
	private String strPhNo,strZip;
	private String[] strBusinessType = {};
	private int selectType =0,intBusinessTypeCode=1;
	double longitude, latitude;
	private List<BusinessType>  types;
	private List<String> names;
	private List<Integer> ids;
	
	
  public void onCreate(Bundle savedInstanceState)
  {
	  super.onCreate(savedInstanceState);
	  requestWindowFeature(Window.FEATURE_NO_TITLE);
	  setContentView(R.layout.registrationbussiness);
	  btnBack 				= (Button)findViewById(R.id.btnBack);
	  tvBusinessType		=(TextView)findViewById(R.id.tvBusinessType);
	  rlBusinessType		=(RelativeLayout)findViewById(R.id.rlBusinessType);
	  etCompany				=(EditText)findViewById(R.id.etCompany);
	  etName				=(EditText)findViewById(R.id.etName);
	  etEmail				=(EditText)findViewById(R.id.etEmail);
	  etPhNo				=(EditText)findViewById(R.id.etPhNo);
	  etPassword			=(EditText)findViewById(R.id.etPassword);
	  etAddress				=(EditText)findViewById(R.id.etAddress);
	  etCity				=(EditText)findViewById(R.id.etCity);
	  etState				=(EditText)findViewById(R.id.etState);
	  etZip					=(EditText)findViewById(R.id.etZip);
	  etCountry				=(EditText)findViewById(R.id.etCountry);
	  btnSubmit			 	= (Button)findViewById(R.id.btnSubmit);
	  vctBusinessRegistration=new Vector<BusinessRegistration>();
	  
	//get location
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
		
		types = BusinessBusinessLayer.getBusinessTypes(); 
		names = new ArrayList<String>();
		ids = new ArrayList<Integer>();
		for(int i=0; i<types.size();i++){
			names.add(types.get(i).businesstypename);
			ids.add(types.get(i).businesstypeid);
		}
		strBusinessType = names.toArray(new String[names.size()]);
		intBusinessTypeCode = ids.get(0);
		tvBusinessType.setText(names.get(0));
	  
	alertDialog = new AlertDialog.Builder(RegistrationforBussiness.this).create();
		alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
				{
					 alertDialog.dismiss();
				}
		});
	  
		btnBack.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		
		tvBusinessType.setTag(""+selectType);
		rlBusinessType.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationforBussiness.this);
				builder.setTitle("");
				builder.setSingleChoiceItems(strBusinessType,Integer.parseInt((String)tvBusinessType.getTag()), new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						tvBusinessType.setText(strBusinessType[which]);
						tvBusinessType.setTag(""+which);
						selectType = which;
						dialog.dismiss();
					}
				});
				
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		
		
		btnSubmit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				int regStatus;
				isDataCorrect=getAndValidateRegistrationData();
				if(isDataCorrect)
				{
					BusinessRegistration objBusinessRegistration=new BusinessRegistration();
					intBusinessTypeCode = ids.get(selectType);
					objBusinessRegistration.intBusinessType=intBusinessTypeCode;
					objBusinessRegistration.strCompanyName=strCompanyName;
					objBusinessRegistration.strName=strName;
					objBusinessRegistration.strEmail=strEmail;
					objBusinessRegistration.strPhNo=strPhNo;
					objBusinessRegistration.strPassword=strPassword;
					objBusinessRegistration.strAddress=strAddress;
					objBusinessRegistration.strCity=strCity;
					objBusinessRegistration.strState=strState;
					objBusinessRegistration.strZip=strZip;
					objBusinessRegistration.strCountry=strCountry;
					objBusinessRegistration.strGeocode=strGeocode;
					//set default values
					objBusinessRegistration.strSuppEmail="empty@test.com";
					objBusinessRegistration.strFaxNo="0";
					objBusinessRegistration.strImage="";
					objBusinessRegistration.strDescription="no description";
					objBusinessRegistration.strUrl="http://www.nosite.com";
					objBusinessRegistration.isPrimary="True";
					 
					
					vctBusinessRegistration.add(objBusinessRegistration);
//					Ga2ooParsers register=new Ga2ooParsers();
//					regStatus=register.registerNewBusiness(vctBusinessRegistration);
					regStatus = Ga2ooJsonParsers.registerNewBusiness(vctBusinessRegistration);
					if(regStatus==-1)
					{
						 alertDialog.setTitle(getResources().getString(R.string.failure));
						 alertDialog.setMessage(getResources().getString(R.string.registration_is_failed));
						 alertDialog.show();
					}
					else if(regStatus==-2)
					{
						 alertDialog.setTitle(getResources().getString(R.string.already));
						 alertDialog.setMessage(getResources().getString(R.string.username_already_exist));
						 alertDialog.show();
						 etName.requestFocus();
					}
					else if(regStatus>0)
					{
						 alertDialog.setTitle(getResources().getString(R.string.congrates));
						 alertDialog.setMessage(getResources().getString(R.string.registered_successfully));
						 alertDialog.show();
							alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int which) 
									{
									Intent intent = new Intent(RegistrationforBussiness.this, Login.class);
									 intent.putExtra(Login.CURRENTSCREEN, Login.LOGIN);
									startActivity(intent);
									overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
									alertDialog.dismiss();
										
									}
							});
						 
					}
				
				}
			}
		});
  }
  
  
  public boolean getAndValidateRegistrationData()
  {
	  csBusinessType		=tvBusinessType.getText();
	  strCompanyName		=etCompany.getText().toString();
	  strName				=etName.getText().toString();
	  strEmail				=etEmail.getText().toString();
	  strPhNo				=etPhNo.getText().toString();
	  strPassword			=etPassword.getText().toString();
	  strAddress			=etAddress.getText().toString();
	  strCity				=etCity.getText().toString();
	  strState				=etState.getText().toString();
	  strZip				=etZip.getText().toString();
	  strCountry			=etCountry.getText().toString();
	  if(strCompanyName.equals("")||strName.equals("")||strEmail.equals("")||strPassword.equals("")||strAddress.equals("")||strCity.equals("")||strCountry.equals("")||strState.equals(""))
	  {
		  alertDialog.setTitle(getResources().getString(R.string.blank_fields));
		  alertDialog.setMessage(getResources().getString(R.string.all_friends_are_mandatory));
		  etCompany.requestFocus();
		  alertDialog.show();
		  return false;
	  }
	  else if(!strEmail.equals(""))
	  {
				  isEmailAddressValid=emailAddressValidation(strEmail);
				  if(!isEmailAddressValid)
				  {
					alertDialog.setMessage(getResources().getString(R.string.email_is_not_valid));
					etEmail.requestFocus();
					alertDialog.show();
					return false;
				  }
				  else
				  {
					  return true;
				  }
				  
	  }
	  return false;
  }
  /*
   * Method for email validation
   */
  public boolean emailAddressValidation(String strEmail)
  {
	    Matcher matcher;
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
  		CharSequence inputStr = etEmail.getText(); //Make the comparison case-insensitive.  
  		Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);  
  		matcher = pattern.matcher(inputStr); 
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
  
  
  public class MyLocationListener implements LocationListener

  {

	  private static final String FALSE = "false";

	@Override
	  public void onLocationChanged(Location loc)  {
		  latitude =  loc.getLatitude();
		  longitude = loc.getLongitude();
		  Geocoder gc = new Geocoder(RegistrationforBussiness.this, Locale.getDefault());
	        try {
				List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
				StringBuilder sb = new StringBuilder();
				if(addresses.get(0).getMaxAddressLineIndex()>0){
					strAddress=(addresses.get(0).getAddressLine(0)!=null?addresses.get(0).getAddressLine(0):"");
					strCity = (addresses.get(0).getAddressLine(1)!=null?addresses.get(0).getAddressLine(1):"");
					strState = (addresses.get(0).getAddressLine(2)!=null?addresses.get(0).getAddressLine(2):"");
				}
				if("".equals(strState)){
					strState = addresses.get(0).getAdminArea();
				}
				strZip=(addresses.get(0).getPostalCode()!=null?addresses.get(0).getPostalCode():"0000");
				strCountry=(addresses.get(0).getCountryName()!=null?addresses.get(0).getCountryName():"");
				strGeocode=""+roundDouble(latitude)+", "+roundDouble(longitude);
				strIsPrimary=FALSE;
				setLocationFields();
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	  }
	  
	  private double roundDouble(double num) {
		  double result = num * 10;
		  result = Math.round(result);
		  result = result / 10;
		  return result;
		  }
	
	  
	  private void setLocationFields(){
		  etAddress.setText(strAddress);
		  etCity.setText(strCity);
		  etState.setText(strState);
		  etZip.setText(strZip);
		  etCountry.setText(strCountry);
	  }
	  
	  @Override
	  public void onProviderDisabled(String provider){
	
		  Toast.makeText( getApplicationContext(),
		  getResources().getString(R.string.gps_disabled),
		  Toast.LENGTH_SHORT ).show();
	
	  }
	
	  @Override
	  public void onProviderEnabled(String provider){
	
		  Toast.makeText( getApplicationContext(),
		  getResources().getString(R.string.gps_enabled),
		  Toast.LENGTH_SHORT).show();
		
	  }

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		
	}
	
	
  }
}
