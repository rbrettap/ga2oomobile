package com.ga2oo.palendar.xmlparsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.objects.Business;
import com.ga2oo.palendar.objects.FavoriteEvent;
import com.ga2oo.palendar.objects.UserLocationObject;

public class UserEventParser extends BaseGa2ooParser
{
	private Boolean currentElement = false;
	private String currentValue = null;
	private FavoriteEvent objFavoriteEvent;
	
	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if (localName.equals("events"))
		{
			AppConstants.vctUserFavorites = new Vector<FavoriteEvent>();
		}	
		else if (localName.equals("event"))
		{
			objFavoriteEvent	=	new FavoriteEvent();
		}	
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("useraddedeventid"))
		{
			try
			{
				objFavoriteEvent.userAddedEventId=Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in User Event Parsing - objFavoriteEvent.userAddedEventId ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("eventid"))
		{
			try
			{
				objFavoriteEvent.eventId=Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in User Event Parsing - objFavoriteEvent.eventId ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("likecount"))
		{
			try
			{
				if(!currentValue.equals("None"))
					objFavoriteEvent.likeCount=Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in User Event Parsing - objFavoriteEvent.likeCount ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("ga2oometer"))
		{
			try
			{
				objFavoriteEvent.ga2ooMeter=Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in User Event Parsing - objFavoriteEvent.ga2ooMeter ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("event"))
		{
			try
			{
				AppConstants.vctUserFavorites.add(objFavoriteEvent);
			}
			catch(Exception e)
			{
				Log.e("Error in User Event Parsing - AppConstants.vctUserFavorites.add(objFavoriteEvent) ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("events"))
		{
			try
			{
				EventsBusinessLayer eventBL	=	new EventsBusinessLayer();
				for(int i=0;i<AppConstants.vctUserFavorites.size();i++)
				{
					FavoriteEvent objFavoriteEvent=AppConstants.vctUserFavorites.get(i);
					eventBL.insertUserFavorites(objFavoriteEvent);
				}
			}
			catch(Exception e)
			{
				Log.e("Error in User Event Parsing - inserting ", e.toString());
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
