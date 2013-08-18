package com.ga2oo.parsing.net;

import java.util.Timer;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationUtility 
{
	    Timer timer1;
	    LocationManager lm;
	    LocationResult locationResult;
	    boolean gps_enabled=false;
	    boolean network_enabled=false;

	    public  boolean getLocation(Context context, LocationResult result)
	    {
	        //I use LocationResult callback class to pass location value from MyLocation to user code.
	        locationResult=result;
	        if(lm==null)
	            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

	        //exceptions will be thrown if provider is not permitted.
	        try{gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
	        try{network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}

	        //don't start listeners if no provider is enabled
	        if(!gps_enabled && !network_enabled)
	            return false;

	        if(gps_enabled)
	            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, locationListenerGps);
	        if(network_enabled)
	            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locationListenerNetwork);
	        
	        new GetLastLocation().start();
	        
//	        timer1=new Timer();
////	        timer1.schedule(new GetLastLocation(), 2000);
//	        timer1.schedule(new GetLastLocation(), 2000, 2000);
	        return true;
	    }

	    LocationListener locationListenerGps = new LocationListener() {
	        public void onLocationChanged(Location loc) {
//	            timer1.cancel();
	            locationResult.gotLocation(loc);
	            
//	            lm.removeUpdates(this);
//	            lm.removeUpdates(locationListenerNetwork);
	        }
	        public void onProviderDisabled(String provider) {}
	        public void onProviderEnabled(String provider) {}
	        public void onStatusChanged(String provider, int status, Bundle extras) {}
	    };

	    LocationListener locationListenerNetwork = new LocationListener() {
	        public void onLocationChanged(Location loc) {
//	            timer1.cancel();
	            locationResult.gotLocation(loc);
//	            lm.removeUpdates(this);
//	            lm.removeUpdates(locationListenerGps);
	        }
	        public void onProviderDisabled(String provider) {}
	        public void onProviderEnabled(String provider) {}
	        public void onStatusChanged(String provider, int status, Bundle extras) {}
	    };

	    class GetLastLocation extends Thread
	    {
	        @Override
	        public void run() 
	        {
//	             lm.removeUpdates(locationListenerGps);
//	             lm.removeUpdates(locationListenerNetwork);

	             Location net_loc=null, gps_loc=null;
	             if(gps_enabled)
	                 gps_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	             if(network_enabled)
	                 net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

	             //if there are both values use the latest one
	             if(gps_loc!=null && net_loc!=null)
	             {
	                 if(gps_loc.getTime()>net_loc.getTime())
	                 {
	                     locationResult.gotLocation(gps_loc);
//	                     if(gps_loc!=null)
//	     	    		 {
//	                    	 CaptcureDataActivity.laitiude= gps_loc.getLatitude();
//	                    	 CaptcureDataActivity.longitude=gps_loc.getLongitude();
//	     	    			
//	                    	 DecimalFormat df = new DecimalFormat("#.######");
//	     	    			
//	                    	 CaptcureDataActivity.laitiude = Double.parseDouble(df.format(CaptcureDataActivity.laitiude));
//	                    	 CaptcureDataActivity.longitude = Double.parseDouble(df.format(CaptcureDataActivity.longitude));
//	     	    		    
//	                    	 final String latLongString = "lat= "+ df.format(CaptcureDataActivity.laitiude)+" long= "+ df.format(CaptcureDataActivity.longitude);
//	     	    			
//	                    	 Log.e("location", ""+latLongString);
//	     	    		}
	                 }
	                 else
	                 {
	                	 locationResult.gotLocation(net_loc);
//	                     if(net_loc!=null)
//	     	    		 {
//	                    	 CaptcureDataActivity.laitiude= net_loc.getLatitude();
//	                    	 CaptcureDataActivity.longitude=net_loc.getLongitude();
//		     	    		    
//	                    	 DecimalFormat df = new DecimalFormat("#.######");
//		     	    			
//	                    	 CaptcureDataActivity.laitiude = Double.parseDouble(df.format(CaptcureDataActivity.laitiude));
//	                    	 CaptcureDataActivity.longitude = Double.parseDouble(df.format(CaptcureDataActivity.longitude));
//		     	    		    
//	                    	 final String latLongString = "lat= "+ df.format(CaptcureDataActivity.laitiude)+" long= "+ df.format(CaptcureDataActivity.longitude);
//		     	    			
//	                    	 Log.e("location", ""+latLongString);
//	     	    		 }
	                 }
	                 return;
	             }

	             if(net_loc!=null)
	             {
	                 locationResult.gotLocation(net_loc);
	                 return;
	             }
	             
	             if(gps_loc!=null)
	             {
	                 locationResult.gotLocation(gps_loc);
	                 return;
	             }
	             
	             locationResult.gotLocation(null);
	        }
	}
	    
    public void stopGpsLocUpdation() 
    {
//    	if(timer1 != null && lm != null)
//    	{
//    		timer1.cancel();
//	    	timer1.purge();
//	    	lm.removeUpdates(locationListenerGps);
//            lm.removeUpdates(locationListenerNetwork);
//    	}
    	
    	if(lm != null)
    	{
    		lm.removeUpdates(locationListenerGps);
            lm.removeUpdates(locationListenerNetwork);
    	}
    }
    public interface LocationResult
    {
    	public void gotLocation(Location loc);
    }

}
