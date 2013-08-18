package com.ga2oo.palendar.xmlparsers;

import java.util.ArrayList;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.ga2oo.palendar.EventDetails;
import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.objects.Attending;
import com.ga2oo.palendar.objects.EventLocation;
import com.ga2oo.palendar.objects.Events;
import com.ga2oo.palendar.objects.EventsDetails;

public class ParseEventsList extends BaseGa2ooParser
{
	private Boolean currentElement = false;
	private String currentValue = null;
	private EventsDetails objEventDetails;
	private int state=1;
	private int categoryCount=1;
	private Attending attending;
	private boolean isEventImage=true,isAttendingUserId=false,isEventLocation;
	private EventLocation eventlocation;	
	private EventsBusinessLayer eventListBL;	

	/** Called when tag starts ( ex:- <objEventDetails>AndroidPeople</objEventDetails> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if (localName.equals("events"))
		{
			AppConstants.vctEvents = new ArrayList<EventsDetails>();
			AppConstants.vctEventLocation=new ArrayList<EventLocation>();
		}		
		else if(localName.equalsIgnoreCase("event"))
		{
			objEventDetails = new EventsDetails();
		}
		else if(localName.equalsIgnoreCase("images"))
		{
			isEventImage=true;
		}
		else if(localName.equalsIgnoreCase("image")&& isEventImage)
		{
//			objEventDetails.isEventImage=attributes.getValue("main");
		}
		else if(localName.equalsIgnoreCase("Img") && isEventImage)
		{
//			objEventDetails.EventListImage = attributes.getValue("src");
		}
		else if(localName.equalsIgnoreCase("location"))
		{
			isEventLocation=true;
			eventlocation=new EventLocation();
		}
		else if(localName.equalsIgnoreCase("attending"))
		{
//			objEventDetails.vctEventAttending = new Vector<Attending>();
			isAttendingUserId=true;
		}
		else if(localName.equalsIgnoreCase("user") && isAttendingUserId)
		{
			attending =new Attending();
		}
		
	}

	/** Called when tag closing ( ex:- <objEventDetails>AndroidPeople</objEventDetails> 
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		
		if(localName.equalsIgnoreCase("eventid"))
		{
			try
			{
				state=2;
				objEventDetails.eventid = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.i("Error in objEventDetails Parsing - EventId ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("name"))
		{
			objEventDetails.eventname = currentValue;
		}
		else if(localName.equalsIgnoreCase("event_start_date"))
		{
			objEventDetails.event_start_date = currentValue;
		}
		else if(localName.equalsIgnoreCase("event_start_time"))
		{
			objEventDetails.event_start_time = currentValue;
		}
		else if(localName.equalsIgnoreCase("is_featured"))
		{
			objEventDetails.is_featured = currentValue;
		}
		else if(localName.equalsIgnoreCase("price"))
		{
			objEventDetails.price = currentValue;
		}
		else if(localName.equalsIgnoreCase("status"))
		{
			objEventDetails.status = currentValue;
		}
		else if(localName.equalsIgnoreCase("business"))
		{
//			objEventDetails.business = Integer.parseInt(currentValue);
		}
		else if(localName.equalsIgnoreCase("date_updated"))
		{
			objEventDetails.date_updated = currentValue;
		}
		else if(localName.equalsIgnoreCase("maincategory") && categoryCount==1)
		{
//			objEventDetails.category1=Integer.parseInt(currentValue);
		}
		else if(localName.equalsIgnoreCase("maincategory") && categoryCount==2)
		{
//			objEventDetails.category2=Integer.parseInt(currentValue);
		}
		else if(localName.equalsIgnoreCase("category"))
		{
			categoryCount++;
		}
		else if(localName.equalsIgnoreCase("categories"))
		{
			categoryCount=1;
		}
		else if(localName.equalsIgnoreCase("images"))
		{
			isEventImage=false;
		}
		else if(localName.equalsIgnoreCase("id") && isEventLocation)
		{
			try
			{
				if(currentValue!=null)
					eventlocation.LOCATIONID = Integer.parseInt(currentValue);				
				eventlocation.EventId=objEventDetails.eventid;
			}
			catch (Exception e)
			{
				Log.e("error in parsing LOCATIONID ",e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("geocode")&& isEventLocation)
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
		else if(localName.equalsIgnoreCase("address")&& isEventLocation)
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
		else if(localName.equalsIgnoreCase("city")&& isEventLocation)
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
		else if(localName.equalsIgnoreCase("state")&& isEventLocation)
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
		else if(localName.equalsIgnoreCase("zipcode")&& isEventLocation )
		{
			try
			{
				eventlocation.zipcode = currentValue;	
			}
			catch (Exception e)
			{
				Log.e("error in parsing Zip ",e.toString());
			}
			
		}
		else if(localName.equalsIgnoreCase("country")&& isEventLocation )
		{
			try
			{
				eventlocation.country = currentValue;	
			}
			catch (Exception e)
			{
				Log.e("error in parsing Country ",e.toString());
			}
			
		}
		else if(localName.equalsIgnoreCase("location")&&isEventLocation)
		{
			isEventLocation=false;
				AppConstants.vctEventLocation.add(eventlocation);		
		}

		else if(localName.equalsIgnoreCase("event"))
		{
			state=1;
			AppConstants.vctEvents.add(objEventDetails);
		}
		
		else if(localName.equalsIgnoreCase("id") && isAttendingUserId)
		{
			attending.id = Integer.parseInt(currentValue);				
			attending.eventID=objEventDetails.eventid;
		}
		else if(localName.equalsIgnoreCase("user") && isAttendingUserId)
		{			
//			objEventDetails.vctEventAttending.add(attending);
		}
		else if(localName.equalsIgnoreCase("attending"))
		{
			isAttendingUserId=false;
			eventListBL = new EventsBusinessLayer();
//			for(int i=0;i<objEventDetails.vctEventAttending.size();i++)
//			{
//				Attending objAttending = objEventDetails.vctEventAttending.get(i);
//				eventListBL.Insert(objAttending);
//			}
		}

		else if(localName.equalsIgnoreCase("events"))
		{
			eventListBL = new EventsBusinessLayer();
			for(int i=0;i<AppConstants.vctEvents.size();i++)
			{
				EventsDetails obj = AppConstants.vctEvents.get(i);
				eventListBL.InsertEventList(obj);
			}
			for(int i=0;i<AppConstants.vctEventLocation.size();i++)
			{
				EventLocation eventLocation = AppConstants.vctEventLocation.get(i);					
				eventListBL.insertIntoEventLocation(eventLocation);
			}
		}
	}

	/** Called to get tag characters ( ex:- <objEventDetails>AndroidPeople</objEventDetails> 
	 * -- to get objEventDetails Character ) */
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
