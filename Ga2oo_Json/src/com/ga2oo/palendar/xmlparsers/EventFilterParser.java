package com.ga2oo.palendar.xmlparsers;

import java.util.ArrayList;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.objects.EventsDetails;
import com.ga2oo.palendar.objects.FavoriteEvent;

public class EventFilterParser extends BaseGa2ooParser
{
	private Boolean currentElement = false;
	private String currentValue = null;
	private EventsDetails objEventsDetails;
	private boolean isEventImage=true;
	
	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if (localName.equals("events"))
		{
			AppConstants.vctFilteredEvents = new ArrayList<EventsDetails>();
			if(AppConstants.vctFilteredEvents!=null)
				AppConstants.vctFilteredEvents.clear();
		}	
		else if(localName.equalsIgnoreCase("images"))
		{
			isEventImage=true;
		}
		else if (localName.equals("event"))
		{
			objEventsDetails	=	new EventsDetails();
		}	
		else if(localName.equalsIgnoreCase("Img")&&isEventImage)
		{
//			objEventsDetails.images. = attributes.getValue("src");
			isEventImage=false;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("eventid"))
		{
			try
			{
				objEventsDetails.eventid=Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - EventId ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("name"))
		{
			try
			{
				objEventsDetails.eventname=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - name ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("event_start_date"))
		{
			try
			{
				objEventsDetails.event_start_date=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - event_start_date ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("event_start_time"))
		{
			try
			{
				objEventsDetails.event_start_time=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - event_start_time ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("price"))
		{
			try
			{
				objEventsDetails.price=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - price ", e.toString());
			}
		}

		else  if(localName.equalsIgnoreCase("business"))
		{
			try
			{
	//			objEventsDetails.business=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - business ", e.toString());
			}
		}
		
		else  if(localName.equalsIgnoreCase("date_updated"))
		{
			try
			{
				objEventsDetails.date_updated=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - date_updated ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("images"))
		{
			isEventImage=false;
		}
		else  if(localName.equalsIgnoreCase("event"))
		{
			try
			{
				AppConstants.vctFilteredEvents.add(objEventsDetails);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - EventId ", e.toString());
			}
		}
//		else  if(localName.equalsIgnoreCase("events"))
//		{
//			try
//			{
//				EventsBusinessLayer eventBL	=	new EventsBusinessLayer();
//				for(int i=0;i<AppConstants.vctUserFavorites.size();i++)
//				{
//					FavoriteEvent objEventsDetails=AppConstants.vctUserFavorites.get(i);
//					eventBL.insertUserFavorites(objEventsDetails);
//				}
//				AppConstants.vctUserFavorites.add(objEventsDetails);
//			}
//			catch(Exception e)
//			{
//				Log.e("Error in Event Parsing - EventId ", e.toString());
//			}
//		}
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
