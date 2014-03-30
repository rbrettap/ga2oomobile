package com.ga2oo.palendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.objects.UserLocationObject;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;
public class EventLocationOnMap extends MapActivity 
{
	
	private static final String GEOPOINT = "GeoPoint";
	private static final String STRTO = "strTo";
	private static final String NONE = "None";
	private static final String TRUE = "True";
	private static final String ADDEDNEWLOCATION = "addNewLocation";
	
	private MapView mapview; 
	private MapController mc;
	private static GeoPoint p;
	private EditText etSearch;
	public String strCountry,strAdressLine,strSubAdminArea,strCity,strFeature,strZipcode;
	public List<UserLocationObject> userSavedLoaction;
	private  UserLocationObject objUserLocation;
	private double lattitude2=40.759255,longitude2=-73.923181;
	private String geoCode="";
	private  Geocoder geocoder;
	private String strTo;
	private FindUsSelectedHelloItemizedOverlay itemizedOverlay;
	private List<Overlay> mapOverlays;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		mapview=(MapView)findViewById(R.id.mapView);
		mapview.setBuiltInZoomControls(true);
		mc= mapview.getController();       
		mc.setZoom(10);
		geoCode	=getIntent().getExtras().getString(GEOPOINT);
		strTo	=getIntent().getExtras().getString(STRTO);
		
		if(!geoCode.equals(""))
		{
			String arr[]=geoCode.split(",");
			lattitude2=Double.parseDouble(arr[0]);
			longitude2=Double.parseDouble(arr[1]);
			Log.i("lattitude2",""+lattitude2+"longitude2"+longitude2);
		}
		
		setMapPoints();
		etSearch=(EditText)findViewById(R.id.etSearch);
		etSearch.setVisibility(View.GONE);
		etSearch.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after)
			{
			}
			@Override
			public void afterTextChanged(Editable s)
			{
				Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault()); 
				String address= etSearch.getText().toString();
				try
				{
					List<Address> addresses = geoCoder.getFromLocationName(address, 5);
					if (addresses.size() > 0)
					{
						lattitude2=addresses.get(0).getLatitude();
						longitude2=addresses.get(0).getLongitude();
						setMapPoints();
		                	}
				}	
				catch(Exception ee)
				{
				}
			}
		});
	       
		final MapOverlay mapOverlay = new MapOverlay();	      
		Log.i("mapOverlay",""+mapOverlay.LATITUDE);
		List<Overlay> listOfOverlays = mapview.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(mapOverlay);        
		TabsActivity.btnLogout.setVisibility(View.GONE);
		TabsActivity.btnLogout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				getLoacationAddress(mapOverlay.LATITUDE, mapOverlay.LONGITUDE);
//				Ga2ooParsers objGa2ooParsers=new Ga2ooParsers();
//				int locationId = objGa2ooParsers.addUserSavedLocation(userSavedLoaction);
				int locationId = Ga2ooJsonParsers.addUserSavedLocation(userSavedLoaction);
				
				if(locationId==0 || locationId==-1)
				{
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.some_error), 5).show();
				} 
				else
				{					
					alertbox();
					UserAccountBusinessLayer userActBL = new UserAccountBusinessLayer();
					for(int i=0;i<userSavedLoaction.size();i++)
					{
						objUserLocation.locationId = locationId;
						UserLocationObject obj = userSavedLoaction.get(i);
						userActBL.updatePrimaryLocation(0);
						userActBL.InsertSavedLocation(obj);
					}
				}
			}
		});
		       
	    }
	@SuppressWarnings("deprecation")
	private void setMapPoints()
	{
		mapview.invalidate();
		getLoacationAddress(lattitude2, longitude2);
		MapController mc = mapview.getController();
		mapOverlays = mapview.getOverlays();
		Drawable marker1 = getResources().getDrawable(R.drawable.purple);
		GeoPoint point1 = getPoint(lattitude2, longitude2);
		
		final CustomView customView = new CustomView(mapview.getContext());
		mapview.addView(customView);	
		customView.setBackgroundResource(R.drawable.balloon_overlay_bg_selector);
		customView.removeAllViews();
		customView.bringToFront();
		customView.setVisibility(View.VISIBLE);
		TextView tv1=new TextView(mapview.getContext());
		tv1.setText("");
		tv1.setTextColor(Color.WHITE);
		tv1.setTextSize(12);
		tv1.setTypeface(Typeface.DEFAULT_BOLD);
		customView.addView(tv1, new AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,10,20));
		
		TextView tv2=new TextView(mapview.getContext());
		tv2.setText("");
		//locationName[index]);
		tv2.setTextColor(Color.WHITE);
		tv2.setTextSize(12);
		tv2.setTypeface(Typeface.DEFAULT_BOLD);
		if(userSavedLoaction!=null&&userSavedLoaction.size()!=0)
		{
			tv1.setText(userSavedLoaction.get(0).City);//locationName[index]);
			tv2.setText(userSavedLoaction.get(0).Country);
		}
		customView.addView(tv2, new AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,10,50));
		customView.setLayoutParams(new MapView.LayoutParams(220, 110, point1,-110,-135, MapView.LayoutParams.MODE_MAP|MapView.LayoutParams.TOP_LEFT));
		mapOverlays.clear();
		itemizedOverlay = new FindUsSelectedHelloItemizedOverlay(marker1,mapview.getContext(),mapview,mc,customView);
		if(userSavedLoaction!=null&&userSavedLoaction.size()!=0)
		{
		OverlayItem item = new OverlayItem(point1,userSavedLoaction.get(0).City,userSavedLoaction.get(0).Country);
		itemizedOverlay.addOverlay(item);
		}
		else
		{
			OverlayItem item = new OverlayItem(point1,"","");
			itemizedOverlay.addOverlay(item);
		}
		mapOverlays.add(itemizedOverlay);
		mc.animateTo(point1);
		mapview.getController().setCenter(point1); 
	}

	private GeoPoint getPoint(double lat, double lon)
	{
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}
	public void getLoacationAddress( double LATITUDE, double LONGITUDE)
	{
		geocoder = new Geocoder(this, Locale.ENGLISH);
		Log.i("LATITUDE",""+LATITUDE);
		try 
		{
			Log.i("LATITUDE",""+LATITUDE);
			List<Address> addresses = geocoder.getFromLocation(LATITUDE,LONGITUDE,1);
			userSavedLoaction=new ArrayList<UserLocationObject>();
			objUserLocation=new UserLocationObject();
			Log.i("addresses.size()",""+addresses.size());
			if (addresses.size()> 0)
			{
				if(addresses.get(0).getCountryName()!=null)
					objUserLocation.Country = addresses.get(0).getCountryName();
				else
					objUserLocation.Country=NONE;
						
				if(addresses.get(0).getAddressLine(0)!=null)
					objUserLocation.Address=addresses.get(0).getAddressLine(0);
				else
					objUserLocation.Address=NONE;
					
				if(addresses.get(0).getLocality()!=null)
					objUserLocation.City=addresses.get(0).getLocality();
				else
					objUserLocation.City=NONE;
				
				if(addresses.get(0).getPostalCode()!=null)
					objUserLocation.Zip= addresses.get(0).getPostalCode();
				else
					objUserLocation.Zip=NONE;
					
				if(addresses.get(0).getAdminArea()!=null)
					objUserLocation.State=addresses.get(0).getAdminArea();
				else
					objUserLocation.State=NONE;
					
				objUserLocation.GeoCode=""+LATITUDE+","+LONGITUDE;
				objUserLocation.Is_Primary=TRUE;
				if(userSavedLoaction!=null)
					userSavedLoaction.clear();
				userSavedLoaction.add(objUserLocation);
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
		
	public void alertbox() 
	{
		   
		AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
		builder.setMessage(getResources().getString(R.string.location_added_want_add_more));
		builder.setCancelable(false);
			
		builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		})
		.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				EventLocationOnMap.this.finishFromChild(EventLocationOnMap.this);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	
		@Override
		protected boolean isRouteDisplayed()
		{
			return false;
		}
		@Override
		public void onResume()
		{
			super.onResume();
			Log.e("MyMapActivity","onResume");
			
			TabsActivity.btnBack.setVisibility(View.VISIBLE);
			TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar_empty);
			TabsActivity.tvTitle.setText(getResources().getString(R.string.map));
			TabsActivity.tvTitle.setVisibility(View.VISIBLE);
			TabsActivity.tvTitle.setGravity(Gravity.CENTER);
			if(ADDEDNEWLOCATION.equals(strTo))
				TabsActivity.btnLogout.setBackgroundResource(R.drawable.save);
			else
				TabsActivity.btnLogout.setVisibility(View.GONE);
			TabsActivity.rlCommonTitleBar.invalidate();
			
			TabsActivity.btnBack.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					EventLocationOnMap.this.finishFromChild(EventLocationOnMap.this);
				}
			});
			
		           
		}
		 class MapOverlay extends Overlay
		 {
			 double LATITUDE;
			 double LONGITUDE;
			 Geocoder geoCoder = null;
			 GeoPoint p1;
			 @Override
			 public boolean onTouchEvent(MotionEvent event, MapView mapView) 
			 {   
				 //---when user lifts his finger---
				 if (event.getAction() == 1) 
				 {                
					 p1= mapView.getProjection().fromPixels((int) event.getX(),(int) event.getY());
					 LATITUDE =   p1.getLatitudeE6()/ 1E6;
					 LONGITUDE=   p1.getLongitudeE6()/ 1E6;         
					 Log.i("LATITUDE",""+LATITUDE+"LONGITUDE "+LONGITUDE);
				 } 
				 return false;
			 }
		       
			 @Override
			 public boolean draw(Canvas canvas, MapView mapView,boolean shadow, long when) 
			 {
				 super.draw(canvas, mapView, shadow);                   
		             //---translate the GeoPoint to screen pixels---
				 Point screenPts = new Point();
				 mapView.getProjection().toPixels(getPoint(lattitude2, longitude2), screenPts);
				 //---adding the marker---
				 Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ico_carte_pin);            
				 canvas.drawBitmap(bmp, screenPts.x-10, screenPts.y-bmp.getHeight(), null);  
				 return true;
			 }
		 }
}
