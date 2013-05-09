package com.winit.ga2oo.xmlparsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.winit.ga2oo.businesslayer.EventsBusinessLayer;
import com.winit.ga2oo.businesslayer.UserAccountBusinessLayer;
import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.objects.Business;
import com.winit.ga2oo.objects.FavoriteEvent;

public class FavoriteCompanyParser extends BaseGa2ooParser
{
	private Boolean currentElement = false;
	private String currentValue = null;
	private Business objBusiness;
	
	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if (localName.equals("fav_businesses"))
		{
			if(AppConstants.vctUserFavoritesBusiness !=null)
				AppConstants.vctUserFavoritesBusiness .clear();
			AppConstants.vctUserFavoritesBusiness = new Vector<Business>();
		}	
		else if (localName.equals("business"))
		{
			objBusiness	=	new Business();
		}	
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("useraddedbusinessid"))
		{
			try
			{
				objBusiness.useraddedbusinessid=Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - EventId ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("businessid"))
		{
			try
			{
				objBusiness.businessid=Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - EventId ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("businessname"))
		{
			try
			{
				objBusiness.businessname=currentValue;
				Log.i("objBusiness.BusinessName",""+objBusiness.businessname);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - EventId ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("date_updated"))
		{
			try
			{
				objBusiness.date_updated=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - EventId ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("business"))
		{
			try
			{
				AppConstants.vctUserFavoritesBusiness.add(objBusiness);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - EventId ", e.toString());
			}
		}
		else if (localName.equals("fav_businesses"))
		{
			UserAccountBusinessLayer userAccBL=new UserAccountBusinessLayer();
			for(int i=0;i<AppConstants.vctUserFavoritesBusiness.size();i++)
			{
				Business objBusiness=AppConstants.vctUserFavoritesBusiness.get(i);
				userAccBL.InsertUserBusiness(objBusiness);
			}
		}	
	}
	
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
