package com.ga2oo.palendar.xmlparsers;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.objects.Attending;
import com.ga2oo.palendar.objects.Categories;
import com.ga2oo.palendar.objects.EventLocation;
import com.ga2oo.palendar.objects.EventsDetails;
import com.ga2oo.palendar.objects.EventsDetailsData;

public class ParseEventDetails extends BaseGa2ooParser
{
	private boolean currentElement = false;
	private String currentValue = null;	
	private EventLocation eventlocation;	
	private EventsDetailsData event;
	private Categories objCategories;
	private EventsBusinessLayer eventListBL;
	private Attending attending;
	public int eventImageCount=1;
	public int businessCount=1;
	public int state=1;
	private boolean isEventImage=false,isBusiness=false,isEventLocation;
	private String isEventmainImage="";

	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;
		 
		if(localName.equalsIgnoreCase("event"))
		{
			AppConstants.vctEventsDetails = new ArrayList<EventsDetailsData>();
			event = new EventsDetailsData();	
			state=1;
		}
		else if(localName.equalsIgnoreCase("categories"))
		{
	//		event.vctEventCategories = new Vector<Categories>();
		}
		else if(localName.equalsIgnoreCase("category"))
		{
			objCategories=new Categories();
		}
		else if(localName.equalsIgnoreCase("images"))
		{
			isEventImage=true;
		}
		else if(localName.equalsIgnoreCase("image")&& isEventImage)
		{
			isEventmainImage=attributes.getValue("main");
		}
		else if(localName.equalsIgnoreCase("Img") && isEventImage && isEventmainImage.equals("True") )
		{
//			event.EventsImage1 = attributes.getValue("src");
		}
		else if(localName.equalsIgnoreCase("Img") && isEventImage && eventImageCount==1)
		{
//			event.EventsImage2 = attributes.getValue("src");
		}
		else if(localName.equalsIgnoreCase("Img") && isEventImage && eventImageCount==2)
		{
//			event.EventsImage3 = attributes.getValue("src");
		}
		else if(localName.equalsIgnoreCase("Img") && isEventImage && eventImageCount==3)
		{
//			event.EventsImage4 = attributes.getValue("src");
		}
	
		else if(localName.equalsIgnoreCase("business"))
		{
			isBusiness=true;
		}
		else if(localName.equalsIgnoreCase("location") && !isBusiness)
		{
			state=3;
			isEventLocation=true;
			AppConstants.vctEventLocation=new ArrayList<EventLocation>();
			eventlocation=new EventLocation();
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		if(localName.equalsIgnoreCase("id") && state==1)
		{			
			try
			{
			event.eventid = Integer.parseInt(currentValue);
			state=2;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventId ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("name"))
		{
			
			try
			{
				event.eventname = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventName ",e.toString());
			}

		}
		else if(localName.equalsIgnoreCase("description"))
		{
			try
			{
				event.description = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventsDetail ",e.toString());
			}

		}
		else if(localName.equalsIgnoreCase("tickets"))
		{
			try
			{
				event.tickets = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventsTickets ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("supportemail"))
		{
			try
			{
				event.supportemail = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing supportemail ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("supportphone"))
		{
			
			try
			{
				event.supportphone = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventsSupportPhone ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("event_start_date"))
		{
			event.event_start_date = currentValue;
			try
			{
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventId ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("event_end_date"))
		{
			try
			{
				event.event_end_date = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventsEndDate ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("event_start_time"))
		{
			try
			{
				event.event_start_time = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventId ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("event_end_time"))
		{
			try
			{
				event.event_end_time = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventsEndTime ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("event_expiration"))
		{
			try
			{
				event.event_expiration = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventExpiration ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("is_featured"))
		{
			try
			{
				event.is_featured = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventIsFeatured ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("price"))
		{
			try
			{
				event.price = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventPrice ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("likecount"))
		{
			try
			{
				event.likecount = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventLikeCount ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("date_updated"))
		{
			try
			{
				event.date_updated = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventDate_Updated ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("business"))
		{
			try
			{
				isBusiness=false;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventDate_Updated ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("date_created"))
		{
			try
			{
				event.date_created = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventsDate_Created ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("maincategory"))
		{
			try
			{
				objCategories.maincategory = currentValue;
				objCategories.eventId=event.eventid;
			}
			catch (Exception e)
			{
				Log.e("error in parsing MainCategory ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("category"))
		{
			try
			{
	//			event.vctEventCategories.add(objCategories);
			}
			catch (Exception e)
			{
				Log.e("error in parsing vctEventCategories ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("image") && isEventmainImage.equals("False"))
		{
			try
			{
				eventImageCount++;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventId ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("images"))
		{
			try
			{
				isEventImage=false;
				eventImageCount=1;
			}
			catch (Exception e)
			{
				Log.e("error in parsing isEventImage ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("id") && state==3 && !isBusiness)
		{
			try
			{
				if(currentValue!=null)
					eventlocation.LOCATIONID = Integer.parseInt(currentValue);				
				eventlocation.EventId=event.eventid;
			}
			catch (Exception e)
			{
				Log.e("error in parsing LOCATIONID ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("geocode")&& state==3 && !isBusiness)
		{
			try
			{
				eventlocation.geocode = currentValue;	
			}
			catch (Exception e)
			{
				Log.e("error in parsing GeoCode ",e.toString());
			}
			
		}
		else if(localName.equalsIgnoreCase("address")&& state==3 && !isBusiness)
		{
			try
			{
				eventlocation.Address = currentValue;	
			}
			catch (Exception e)
			{
				Log.e("error in parsing Address ",e.toString());
			}
			
		}
		else if(localName.equalsIgnoreCase("city")&& state==3 && !isBusiness)
		{
			try
			{
				eventlocation.city =currentValue;	
			}
			catch (Exception e)
			{
				Log.e("error in parsing City ",e.toString());
			}
			
		}
		else if(localName.equalsIgnoreCase("state")&& state==3 && !isBusiness)
		{
			try
			{
				eventlocation.state = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing State ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("zipcode")&& state==3 && !isBusiness)
		{
			try
			{
				eventlocation.geocode = currentValue;	
			}
			catch (Exception e)
			{
				Log.e("error in parsing Zip ",e.toString());
			}
			
		}
		else if(localName.equalsIgnoreCase("location")&& state==3 && !isBusiness)
		{
			state=4;
		}
		else if(localName.equalsIgnoreCase("status"))
		{
			try
			{
				event.status = currentValue;	
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventStatus ",e.toString());
			}
		}
		
		else if(localName.equalsIgnoreCase("id") && isBusiness)
		{			
//			event.business = Integer.parseInt(currentValue);	
		}
//		else if(localName.equalsIgnoreCase("id") && state==4)
//		{
//			//attending.userID = Integer.parseInt(currentValue);				
//			//attending.eventID=event.EventId;
//		}
//		else if(localName.equalsIgnoreCase("user"))
//		{			
//			event.vctEventAttending.add(attending);
//		}
//		else if(localName.equalsIgnoreCase("attending"))
//		{
//			state=5;
//			eventListBL = new EventsBusinessLayer();
//			for(int i=0;i<event.vctEventAttending.size();i++)
//			{
//				Attending objAttending = event.vctEventAttending.get(i);
//				//eventListBL.Insert(objAttending);
//			}
//			
//		}
		else if(localName.equalsIgnoreCase("price"))
		{
			try
			{
				event.price = currentValue;
			}
			catch (Exception e)
			{
				Log.e("error in parsing EventPrice ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("event"))
		{
			AppConstants.vctEventsDetails.add(event);			
			eventListBL = new EventsBusinessLayer();
			for(int i=0;i<AppConstants.vctEventsDetails.size();i++)
			{
				EventsDetailsData obj = AppConstants.vctEventsDetails.get(i);
				eventListBL.InsertIntoEventDetails(obj);
			}
			if(eventlocation!=null)
				AppConstants.vctEventLocation.add(eventlocation);			
			if(isEventLocation)
			for(int i=0;i<AppConstants.vctEventLocation.size();i++)
			{
				EventLocation eventLocation = AppConstants.vctEventLocation.get(i);					
				eventListBL.insertIntoEventLocation(eventLocation);
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