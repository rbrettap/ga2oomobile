package com.winit.ga2oo.xmlparsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.winit.ga2oo.businesslayer.UserAccountBusinessLayer;
import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.objects.Business;
import com.winit.ga2oo.objects.UserLocationObject;

public class ParserBusiness extends BaseGa2ooParser
{
	private Boolean currentElement = false;
	private String currentValue = null;
	private Business business;
	private UserLocationObject businessLocObj;
	private UserAccountBusinessLayer userActBL;
	
	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if (localName.equals("businesses"))
		{
			AppConstants.vctBusiness = new Vector<Business>();
		}		
		else if(localName.equalsIgnoreCase("account"))
		{
			business = new Business();
			AppConstants.vctUserLocation = new Vector<UserLocationObject>();
		}
		else if(localName.equalsIgnoreCase("location"))
		{
			businessLocObj = new UserLocationObject();
		}
		
	}

	/** Called when tag closing ( ex:- <event>AndroidPeople</event> 
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("id"))
		{
			try
			{
			business.businessid = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - EventId ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("bizname"))
		{
			business.businessname = currentValue;
		}
		else if(localName.equalsIgnoreCase("businesstype"))
		{
			business.businesstype = currentValue;
		}	
		else if(localName.equalsIgnoreCase("contactemail"))
		{
			business.contactemail = currentValue;
		}	
		else if(localName.equalsIgnoreCase("contactname"))
		{
			business.contactname = currentValue;
		}
		else if(localName.equalsIgnoreCase("url"))
		{
			business.url = currentValue;
		}	
		else if(localName.equalsIgnoreCase("ein"))
		{
			business.Ein = currentValue;
		}	
		else if(localName.equalsIgnoreCase("supportemail"))
		{
			business.supportemail = currentValue;
		}	
		else if(localName.equalsIgnoreCase("phone"))
		{
			business.phonenumber = currentValue;
		}	
		else if(localName.equalsIgnoreCase("fax"))
		{
			business.faxnumber = currentValue;
		}	
		else if(localName.equalsIgnoreCase("is_active"))
		{
			business.is_active = currentValue;
		}	
		else if(localName.equalsIgnoreCase("date_updated"))
		{
			business.date_updated = currentValue;
		}	
		
		else if(localName.equalsIgnoreCase("geocode"))
		{
			businessLocObj.GeoCode = currentValue;
		}
		else if(localName.equalsIgnoreCase("address"))
		{
			businessLocObj.Address = currentValue;
		}
		else if(localName.equalsIgnoreCase("city"))
		{
			businessLocObj.City = currentValue;
		}
		else if(localName.equalsIgnoreCase("state"))
		{
			businessLocObj.State = currentValue;
		}
		else if(localName.equalsIgnoreCase("zip"))
		{
			businessLocObj.Zip = currentValue;
		}
		else if(localName.equalsIgnoreCase("country"))
		{
			businessLocObj.Country = currentValue;
		}
		else if(localName.equalsIgnoreCase("location"))
		{
			AppConstants.vctUserLocation.add(businessLocObj);
		}
		else if(localName.equalsIgnoreCase("account"))
		{
			AppConstants.vctBusiness.add(business);
		}
		else if(localName.equalsIgnoreCase("businesses"))
		{
			userActBL = new UserAccountBusinessLayer();
			for(int i=0;i<AppConstants.vctBusiness.size();i++)
			{
				Business obj = AppConstants.vctBusiness.get(i);
				userActBL.InsertBusinessList(obj);
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
