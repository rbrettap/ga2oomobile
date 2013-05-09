package com.winit.ga2oo.xmlparsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.winit.ga2oo.businesslayer.UserAccountBusinessLayer;
import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.objects.UserAccount;
import com.winit.ga2oo.objects.UserCurrentLocationBasedOnZip;

public class ParseUserCurrentLocationBasedOnZip extends BaseGa2ooParser
{

	private UserCurrentLocationBasedOnZip objUserCurrentLocation;
	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if(localName.equalsIgnoreCase("kml"))
		{
			if(AppConstants.vctUserCurrentLocation==null)
				AppConstants.vctUserCurrentLocation=new Vector<UserCurrentLocationBasedOnZip>();
			else if(AppConstants.vctUserCurrentLocation!=null)
				AppConstants.vctUserCurrentLocation.clear();
		}
		else if(localName.equalsIgnoreCase("Placemark"))
		{
			 objUserCurrentLocation=new UserCurrentLocationBasedOnZip();
		}
		
		
		
	}

	/** Called when tag closing ( ex:- <event>AndroidPeople</event> 
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("address"))
		{
			try
			{
				Log.i("Address",""+currentValue);
				objUserCurrentLocation.strAddress = currentValue;
			}
			catch(Exception e)
			{
				Log.i("Error in UserAccountParser - id", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("CountryName"))
		{
			objUserCurrentLocation.strCountryName = currentValue;
			Log.i("objUserCurrentLocation.strCountryName",""+objUserCurrentLocation.strCountryName);
		}
		else if(localName.equalsIgnoreCase("coordinates"))
		{
			objUserCurrentLocation.strGeoCode= currentValue;
			Log.i("strGeoCode",""+objUserCurrentLocation.strGeoCode);
		}
		else if(localName.equalsIgnoreCase("LocalityName"))
		{
			objUserCurrentLocation.strCity = currentValue;
		}
		else if(localName.equalsIgnoreCase("AdministrativeAreaName"))
		{
			objUserCurrentLocation.strState= currentValue;
		}
		else if(localName.equalsIgnoreCase("PostalCodeNumber"))
		{
			if(currentValue!=null)
				objUserCurrentLocation.zipCode = currentValue;
			else
				objUserCurrentLocation.zipCode = "None";
		}
		else if(localName.equalsIgnoreCase("Placemark"))
		{
			AppConstants.vctUserCurrentLocation.add(objUserCurrentLocation);
		}
	}

	/** Called to get tag characters ( ex:- <event>AndroidPeople</event> 
	 * -- to get event Character ) */
	@Override
	public void characters(char[] ch, int start, int length)throws SAXException 
	{
		if (currentElement) 
		{
			currentValue = new String(ch, start, length);
			currentElement = false;
		}
	}
}
