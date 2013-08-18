package com.ga2oo.palendar;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.SessionEvents;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.Base64;
import com.ga2oo.palendar.controls.PopUpDailog;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;
import com.ga2oo.parsing.net.LocationUtility;
import com.ga2oo.parsing.net.LocationUtility.LocationResult;

public class RegistrationforPersonal extends Activity implements LocationResult
{
	private static final String DECIMALFORMAT = "#.#######";
	private static final String FACEBOOKPICTURE = "/picture";
	private static final String HTTPFACEBOOK = "http://graph.facebook.com/";
	private static final String MALE = "male";
	private static final String FEMALE = "female";
	private static final String GENDER = "gender";
	private static final String BIRTHDAY = "birthday";
	private static final String USERNAME = "username";
	private static final String LASTNAME = "last_name";
	private static final String FIRSTNAME = "first_name";
	private static final String ID = "id";
	private Button btnBack;
	private RelativeLayout rlGender,rlBirthDate;
	private EditText etFName,etLName,etFacebook,etEmail,etUserName,etPassword,etConfirmPassword,etTwitter,etYoutube,etAddress,etCity,etState,etZip,etCountry;
	private ImageView chkFacebook,chkTwitter,chkYoutube;
	private CharSequence csGender,csBirthdate;
	private Boolean chkedFacebook=false,chkedTwitter=false,chkedYoutube=false;
	private TextView tvGender,tvBirthDate,tvLocation;
	private AlertDialog alertDialog;
	private Boolean isEmailAddressValid=false,isDataCorrect=false;
	private AlertDialog.Builder InternetPopup;
	private AlertDialog alert;
	private PopUpDailog customDialog;
	public String strFirstName="",strLastName="",strEmail="",strUserName,strPassword="",strConfirmPassword="",strLocation="",strFbAssociationId="",strTwitterAssociationId="",strYoutubeAssociationId="";
	private String[] strGender; int selectType =0,day,month,year;
	private double lattitude,longitude;
	LocationUtility locationUtility  = new LocationUtility();
	private String arrDate[];
	private Vector<com.ga2oo.palendar.objects.Registration> vctRegistrationData=null;
	private String strAdress="", strCity="", strState="", strZipcode="", strCountry="", strGeocode="", strIsPrimary="";
	LocationManager mlocManager;
	LocationListener mlocListener;
	
  public void onCreate(Bundle savedInstanceState)
  {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.registrationpersonel);
		strGender = new String []{getResources().getString(R.string.male),getResources().getString(R.string.female)};  
		btnBack     = (Button)findViewById(R.id.btnBack);
		etFName		= (EditText)findViewById(R.id.etFName);
		etLName		= (EditText)findViewById(R.id.etLName);
		etEmail		= (EditText)findViewById(R.id.etEmail);
		etUserName	= (EditText)findViewById(R.id.etUserName);
		etPassword	= (EditText)findViewById(R.id.etPassword);
        etConfirmPassword	= (EditText)findViewById(R.id.etConfirmPassword);
//		etLocation	= (EditText)findViewById(R.id.etLocation);
		rlGender    = (RelativeLayout)findViewById(R.id.rlMale);
		rlBirthDate = (RelativeLayout)findViewById(R.id.rlBirthDate);
		tvGender    = (TextView)findViewById(R.id.etGender);
		tvBirthDate = (TextView)findViewById(R.id.etBirthDate);
		etFacebook  = (EditText)findViewById(R.id.etFacebook);
		etTwitter   = (EditText)findViewById(R.id.etTwitter);
		etYoutube	= (EditText)findViewById(R.id.etYoutube);
		chkFacebook	= (ImageView)findViewById(R.id.chkFacebook);
		chkTwitter	= (ImageView)findViewById(R.id.chkTwitter);
		chkYoutube	= (ImageView)findViewById(R.id.chkYoutube);
		
		  etAddress				=(EditText)findViewById(R.id.etAddress);
		  etCity				=(EditText)findViewById(R.id.etCity);
		  etState				=(EditText)findViewById(R.id.etState);
		  etZip					=(EditText)findViewById(R.id.etZip);
		  etCountry				=(EditText)findViewById(R.id.etCountry);
		
		vctRegistrationData=new Vector<com.ga2oo.palendar.objects.Registration>();
		
		if(getIntent().getStringExtra(ID)!=null){
			etFacebook.setText(getIntent().getStringExtra(ID));
			chkFacebook.setImageResource(R.drawable.btn_check_on);
			etFName.setText(getIntent().getStringExtra(FIRSTNAME));
			etLName.setText(getIntent().getStringExtra(LASTNAME));
			etUserName.setText(getIntent().getStringExtra(USERNAME));
			
			
//			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/DD/yyyy");
//			SimpleDateFormat stringFormat = new SimpleDateFormat("yyyy-MM-DD");
//			Date convertedDate = null;
//				try {
//					convertedDate = dateFormat.parse(getIntent().getStringExtra("birthday"));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
			//	tvBirthDate.setText(dateFormat.format(convertedDate));
			String date = getIntent().getStringExtra(BIRTHDAY);
				tvBirthDate.setText(date.substring(6)+"-"+date.substring(0, 2)+"-"+date.substring(3, 5));
			if(getIntent().getStringExtra(GENDER)==FEMALE){
				tvGender.setText(getResources().getString(R.string.female));
			}else if(getIntent().getStringExtra(GENDER)==MALE){
				tvGender.setText(getResources().getString(R.string.male));
			}
		}
				//get location
				mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
				mlocListener = new MyLocationListener();
				mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
//				
		Facebook mFb = new Facebook(AppConstants.APP_ID);
		 
      if (mFb.isSessionValid()) {
	      SessionEvents.onLogoutBegin();
	      AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
	      asyncRunner.logout(this, new LogoutRequestListener());
		 }
		
		alertDialog = new AlertDialog.Builder(RegistrationforPersonal.this).create();
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

//		rellayLocation.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				showLocation();
//			}
//		});
		tvGender.setTag(""+selectType);
		rlGender.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationforPersonal.this);
				builder.setTitle("");
				builder.setSingleChoiceItems(strGender,Integer.parseInt((String)tvGender.getTag()), new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						tvGender.setText(strGender[which]);
						tvGender.setTag(""+which);
						selectType = which;
						dialog.dismiss();
					}
				});
				
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		Calendar currentCal = Calendar.getInstance();
		day = currentCal.get(Calendar.DAY_OF_MONTH);
        month = currentCal.get(Calendar.MONTH);
        year = currentCal.get(Calendar.YEAR);
        if(month<9)
        	tvBirthDate.setText(year+"-0"+(month+1)+"-"+day);
        else
        	tvBirthDate.setText(year+"-"+(month+1)+"-"+day);
        arrDate=new String[3];
		rlBirthDate.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				arrDate=tvBirthDate.getText().toString().split("-");
				DatePickerDialog dailog = new DatePickerDialog(RegistrationforPersonal.this, new OnDateSetListener()
				{											
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
					{
						Log.i("all",tvBirthDate.getText().toString()+"year"+year+"monthOfYear"+"dayOfMonth"+dayOfMonth);
						Calendar cal= Calendar.getInstance();
						cal.set(year,monthOfYear ,dayOfMonth);
						if(cal.get(Calendar.MONTH)<9)
							tvBirthDate.setText(cal.get(Calendar.YEAR)+"-0"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE));
						else
							tvBirthDate.setText(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE));
					}
				},Integer.parseInt(arrDate[0]),Integer.parseInt(arrDate[1])-1, Integer.parseInt(arrDate[2]));
				dailog.setTitle("");
				dailog.show();
			}
		});
		
		chkFacebook.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if(!chkedFacebook)
				{
					chkFacebook.setImageResource(R.drawable.btn_check_on);
					etFacebook.setEnabled(true);
					etFacebook.requestFocus();
					chkedFacebook=true;
				}
				else if(chkedFacebook)
				{
					etFacebook.setEnabled(false);
					chkFacebook.setImageResource(R.drawable.btn_check_off);
					//etFacebook.setText("");
					chkedFacebook=false;
				}
			}
		});
		
		chkTwitter.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				if(!chkedTwitter)
				{
					chkTwitter.setImageResource(R.drawable.btn_check_on);
					etTwitter.setEnabled(true);
					etTwitter.requestFocus();
					chkedTwitter=true;
				}
				else if(chkedTwitter)
				{
					chkTwitter.setImageResource(R.drawable.btn_check_off);
					etTwitter.setEnabled(false);
					etTwitter.setText("");
					chkedTwitter=false;
				}
				
			}
		});
		
		chkYoutube.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if(!chkedYoutube)
				{
					chkYoutube.setImageResource(R.drawable.btn_check_on);
					etYoutube.setEnabled(true);
					etYoutube.requestFocus();
					chkedYoutube=true;
				}
				else if(chkedYoutube)
				{
					chkYoutube.setImageResource(R.drawable.btn_check_off);
					etYoutube.setEnabled(false);
					etYoutube.setText("");
					chkedYoutube=false;
				}
				
			}
		});

		Button btnSubmit = (Button)findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				int regStatus;
				isDataCorrect=getAndValidateRegistrationData();
				ConnectivityManager connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				boolean connected = (connec.getActiveNetworkInfo() != null &&connec.getActiveNetworkInfo().isAvailable() && connec.getActiveNetworkInfo().isConnected());
				if(!connected)
				{
					InternetPopup =  new Builder(RegistrationforPersonal.this);
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
				
				else if(isDataCorrect)
				{
					vctRegistrationData.clear();
					com.ga2oo.palendar.objects.Registration objRegistration=new com.ga2oo.palendar.objects.Registration();
					
					objRegistration.strFName=strFirstName;
					objRegistration.strLName=strLastName;
					objRegistration.strEmail=strEmail;
					objRegistration.strUserName=strUserName;
					objRegistration.strPassword=strPassword;
					objRegistration.strGender=csGender.toString();
					objRegistration.strDOB=csBirthdate.toString();
					objRegistration.strFbAssociationId=(strFbAssociationId.equals("")?"0":strFbAssociationId);
					objRegistration.strTwitterAssociationId=(strTwitterAssociationId.equals("")?"0":strTwitterAssociationId);
					objRegistration.strYoutubeAssociationId=(strYoutubeAssociationId.equals("")?"0":strYoutubeAssociationId);
					objRegistration.strAddress=strAdress;
					objRegistration.strCity=strCity;
					objRegistration.strCountry=strCountry;
					objRegistration.strZipcode=strZipcode;
					objRegistration.strState=strState;
					objRegistration.strGeocode=strGeocode;
					objRegistration.strIsPrimary=strIsPrimary;
					vctRegistrationData.add(objRegistration);
					regStatus = Ga2ooJsonParsers.registerNewUser(vctRegistrationData);
					//TODO: upload image
//					if(getIntent().getStringExtra(ID)!=null){
//						String url = HTTPFACEBOOK+getIntent().getStringExtra(ID)+FACEBOOKPICTURE;
//						Log.v("Facebook", "picture url = "+url);
//	//		           ProfileSettings.profileSettings.ivImage.setImageBitmap(loadBitmap(url));
//	            		String strImageAsciiCode = imageToAscii(url);
//	            		Log.i("strImageAsciiCode",strImageAsciiCode);
////	            		ga2ooParsersUserImageUpload.uploadUserImage(AppConstants.USER_ID, strImageAsciiCode);
//	            		Ga2ooJsonParsers.uploadUserImage(AppConstants.USER_ID, strImageAsciiCode);
//					}
					if(regStatus==-1)
					{
						 alertDialog.setTitle(getResources().getString(R.string.failure));
						 alertDialog.setMessage(Ga2ooJsonParsers.regMessage);
						 alertDialog.show();
					}
					else if(regStatus==-2)
					{
						 alertDialog.setTitle(getResources().getString(R.string.already));
						 alertDialog.setMessage(Ga2ooJsonParsers.regMessage);
						 alertDialog.show();
						 etEmail.requestFocus();
					}
					else if(regStatus==-3)
					{
						 alertDialog.setTitle(getResources().getString(R.string.already));
						 alertDialog.setMessage(Ga2ooJsonParsers.regMessage);
						 alertDialog.show();
						 etUserName.requestFocus();
					}
					else if(regStatus>0)
					{
						 alertDialog.setTitle(getResources().getString(R.string.congrates));
						 alertDialog.setMessage(Ga2ooJsonParsers.regMessage);
						 alertDialog.show();
						 alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
						 {
							 public void onClick(DialogInterface dialog, int which) 
							 {
								 RegistrationforPersonal.this.finish();
//								 Intent intent = new Intent(RegistrationforPersonal.this, Login.class);
//								 intent.putExtra(Login.CURRENTSCREEN, Login.LOGIN);
//								 intent.putExtra("from", "register");
//								 intent.putExtra("username", strUserName);
//								 intent.putExtra("password", strPassword);
//								 startActivity(intent);
								 overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
								 alertDialog.dismiss();
							 }
						});
					}
				}
			}
		});
		
		
  }
  
  public static Bitmap loadBitmap(String url) {
	  Bitmap bitmap = null;
	  try {
		  bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
		} catch (MalformedURLException e) {
		  e.printStackTrace();
		} catch (IOException e) {
		  e.printStackTrace();
		}


	    return bitmap;
	}

  public String imageToAscii(String urlString)
	{
	  URL url;
	  String strAscii = "";
		
	try {
		url = new URL(urlString);
	
	  URLConnection ucon = url.openConnection();
//	  File myImageFile = new File("tmpImage");
      /*
       * Define InputStreams to read from the URLConnection.
       */
      InputStream is = ucon.getInputStream();
//      BufferedInputStream bis = new BufferedInputStream(is);

//      /*
//       * Read bytes to the Buffer until there is nothing more to read(-1).
//       */
//      ByteArrayBuffer baf = new ByteArrayBuffer(50);
//      int current = 0;
//      while ((current = bis.read()) != -1) {
//              baf.append((byte) current);
//      fos.write(baf.toByteArray());
//      fos.close();
//	 
//		FileInputStream fis;
//
//			fis = new FileInputStream(myImageFile);      }
//
//      FileOutputStream fos = new FileOutputStream(myImageFile);

			byte[] data = new byte[1024];
			byte[] tmp = new byte[0];
			byte[] myArrayImage = new byte[0];
			int len = 0 ;
			int total = 0;
			while( (len = is.read(data)) != -1 )
			{
				total += len;
				tmp = myArrayImage;
				myArrayImage = new byte[total];
				System.arraycopy(tmp,0,myArrayImage,0,tmp.length);
				System.arraycopy(data,0,myArrayImage,tmp.length,len);
				strAscii= Base64.encodeBytes(myArrayImage);
			}
			is.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		  //new String(myArrayImage, "US-ASCII");
		
		return strAscii;
	}
  
  /*
   * Method getRegistrationData
   */
 
  public boolean  getAndValidateRegistrationData()
  {
	  strFirstName		=  etFName.getText().toString();
	  strLastName		=  etLName.getText().toString();
	  strEmail			=  etEmail.getText().toString();
	  strUserName		=  etUserName.getText().toString();
	  strPassword		=  etPassword.getText().toString();
	  strConfirmPassword=  etConfirmPassword.getText().toString();
//	  strLocation		=  etLocation.getText().toString();
	  strLocation      = "";
	  csGender			=  tvGender.getText();
	  csBirthdate		=  tvBirthDate.getText();
	  strAdress			= etAddress.getText().toString();
	  strCity			= etCity.getText().toString();
	  strCountry		= etCountry.getText().toString();
	  strZipcode		= etZip.getText().toString();
	  strState			= etState.getText().toString();
	  
	  
	  if(chkedFacebook)
		  strFbAssociationId			=	etFacebook.getText().toString();
	  else
		  strFbAssociationId			=	"";
	  
	  if(chkedTwitter)
		  strTwitterAssociationId		=	etTwitter.getText().toString();
	  else
		  strTwitterAssociationId		=	"";
	  
	  if(chkedYoutube)
		  strYoutubeAssociationId		=	etYoutube.getText().toString();
	  else
		  strYoutubeAssociationId		=	"";
		  
		  
//	  if(strFirstName.equals("")||strLastName.equals("")||strEmail.equals("")||strUserName.equals("")||strPassword.equals("")||strConfirmPassword.equals("")||strLocation.equals(""))
	  if(strFirstName.equals("")||strLastName.equals("")||strEmail.equals("")||strUserName.equals("")||strPassword.equals("")||strConfirmPassword.equals(""))
	  {
		  alertDialog.setTitle(getResources().getString(R.string.blank_fields));
		  alertDialog.setMessage(getResources().getString(R.string.all_friends_are_mandatory));
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
		  else if(!strPassword.equals(strConfirmPassword))
		  {
			  alertDialog.setMessage(getResources().getString(R.string.please_enter_same_password));
			  etConfirmPassword.setText("");
			  etConfirmPassword.requestFocus();
			  alertDialog.show();
			  return false;
		  }
		  else if(chkedFacebook && strFbAssociationId.equals(""))
		  {
			  alertDialog.setMessage(getResources().getString(R.string.please_enter_facebook_associationId));
			  etFacebook.requestFocus();
			  alertDialog.show();
			  return false;
		  }
		  else if(chkedTwitter && strTwitterAssociationId.equals(""))
		  {
			  alertDialog.setMessage(getResources().getString(R.string.please_enter_twitter_associationId));
			  etTwitter.requestFocus();
			  alertDialog.show();
			  return false;
		  }
		  else if(chkedYoutube && strYoutubeAssociationId.equals(""))
		  {
			  alertDialog.setMessage(getResources().getString(R.string.please_enter_youtube_associationId));
			  etYoutube.requestFocus();
			  alertDialog.show();
			  return false;
		  }
		  else
		  {
			  return true;
		  }
		  
	  }
	  else
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

	  @Override
	  public void onLocationChanged(Location loc)  {
		  lattitude =  loc.getLatitude();
		  longitude = loc.getLongitude();
//		  Toast.makeText( getApplicationContext(),
//				  latitude+", "+longitude,
//				  Toast.LENGTH_SHORT ).show();
	//	  etLocation.setText(longitude+", "+latitude);
		  Log.v("location", "location = "+loc);
		  Geocoder gc = new Geocoder(RegistrationforPersonal.this, Locale.getDefault());
	        try {
				List<Address> addresses = gc.getFromLocation(lattitude, longitude, 1);
				StringBuilder sb = new StringBuilder();
//				for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++){
//				 Log.d("location","address line = "+addresses.get(0).getAddressLine(i));
//				}
//				 Log.d("location","admin area = "+addresses.get(0).getAdminArea());
//				 Log.d("location","country = "+addresses.get(0).getCountryName());
//				 Log.d("location","postal code = "+addresses.get(0).getPostalCode());
				if(addresses.get(0).getMaxAddressLineIndex()>0){
					strAdress=(addresses.get(0).getAddressLine(0)!=null?addresses.get(0).getAddressLine(0):"");
					strCity = (addresses.get(0).getAddressLine(1)!=null?addresses.get(0).getAddressLine(1):"");
					strState = (addresses.get(0).getAddressLine(2)!=null?addresses.get(0).getAddressLine(2):"");
				}
				if("".equals(strState)){
					strState = addresses.get(0).getAdminArea();
				}
				strZipcode=(addresses.get(0).getPostalCode()!=null?addresses.get(0).getPostalCode():"0000");
				strCountry=(addresses.get(0).getCountryName()!=null?addresses.get(0).getCountryName():"");
				strGeocode=""+roundDouble(lattitude)+", "+roundDouble(longitude);
				strIsPrimary="True";
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
			  etAddress.setText(etAddress.getText().length()==0?strAdress:etAddress.getText());
			  etCity.setText(etCity.getText().length()==0?strCity:etCity.getText());
			  etState.setText(etState.getText().length()==0?strState:etState.getText());
			  etZip.setText(etZip.getText().length()==0?strZipcode:etZip.getText());
			  etCountry.setText(etCountry.getText().length()==0?strCountry:etCountry.getText());	
			  if(strAdress.length()>0 || strCity.length()>0||strState.length()>0 || strZipcode.length()>0 ||strCountry.length()>0){
				  mlocManager.removeUpdates(mlocListener);
			  }
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
  
  
  private class LogoutRequestListener extends BaseRequestListener {
      public void onComplete(String response) {
          // callback should be run in the original thread, 
          // not the background thread
         new Handler().post(new Runnable() {
              public void run() {
                  SessionEvents.onLogoutFinish();
              }
          });
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

	@Override
	protected void onDestroy() {
		mlocManager.removeUpdates(mlocListener);
		super.onDestroy();
	}
  
  
}

