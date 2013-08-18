package com.ga2oo.palendar.xmlparsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.objects.UserLocationObject;

public class UserLocationParser extends BaseGa2ooParser
{
	private UserLocationObject userLocationObj;
	private UserAccountBusinessLayer userActBL;
	private boolean checkId=true;

	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if (localName.equals("useraccount"))
		{
			checkId=false;
		}
		else if (localName.equals("savedlocations"))
		{
			AppConstants.vctUserLocation = new Vector<UserLocationObject>();
			checkId=true;
		}		
		else if(localName.equalsIgnoreCase("location"))
		{
			userLocationObj = new UserLocationObject();
		}
	}

	/** Called when tag closing ( ex:- <event>AndroidPeople</event> 
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("id")&&checkId)
		{
			userLocationObj.locationId = Integer.parseInt(currentValue);
			Log.i("userLocationObj.locationId",""+userLocationObj.locationId);
		}
		else if(localName.equalsIgnoreCase("is_primary"))
		{
			userLocationObj.Is_Primary = currentValue;
			Log.i("userLocationObj.Is_Primary",""+userLocationObj.Is_Primary);
		}
		else if(localName.equalsIgnoreCase("geocode"))
		{
			userLocationObj.GeoCode = currentValue;
		}
		else if(localName.equalsIgnoreCase("address"))
		{
			userLocationObj.Address= currentValue;
		}
		else if(localName.equalsIgnoreCase("city"))
		{
			userLocationObj.City = currentValue;
		}
		else if(localName.equalsIgnoreCase("state"))
		{
			userLocationObj.State= currentValue;
		}
		else if(localName.equalsIgnoreCase("zipcode"))
		{
			userLocationObj.Zip = currentValue;
		}
		else if(localName.equalsIgnoreCase("country"))
		{
			userLocationObj.Country= currentValue;
		}
		else if(localName.equalsIgnoreCase("date_updated"))
		{
			userLocationObj.Date_Updated= currentValue;
		}
		else if(localName.equalsIgnoreCase("location"))
		{
			AppConstants.vctUserLocation.add(userLocationObj);
		}
		else if(localName.equalsIgnoreCase("savedlocations"))
		{
			userActBL = new UserAccountBusinessLayer();
			for(int i=0;i<AppConstants.vctUserLocation.size();i++)
			{
				UserLocationObject obj = AppConstants.vctUserLocation.get(i);
				userActBL.InsertSavedLocation(obj);
			}
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
