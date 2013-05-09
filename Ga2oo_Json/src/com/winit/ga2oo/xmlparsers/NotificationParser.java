package com.winit.ga2oo.xmlparsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.winit.ga2oo.businesslayer.EventsBusinessLayer;
import com.winit.ga2oo.businesslayer.UserAccountBusinessLayer;
import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.objects.FavoriteEvent;
import com.winit.ga2oo.objects.Notifications;

public class NotificationParser extends BaseGa2ooParser
{
	private Boolean currentElement = false;
	private String currentValue = null;
	private Notifications objNotifications;
	public static int noOfNewNotifications=0;
	
	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if (localName.equals("notifications"))
		{
			noOfNewNotifications=0;
			if(AppConstants.vctUserNotifications ==null)
				AppConstants.vctUserNotifications = new Vector<Notifications>();
			if(AppConstants.vctUserNotifications !=null)
				AppConstants.vctUserNotifications.clear();
			
		}	
		else if (localName.equals("notification"))
		{
			objNotifications	=	new Notifications();
		}	
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("notificationid"))
		{
			try
			{
				objNotifications.notificationId=Integer.parseInt(currentValue);
				Log.i("objNotifications.notificationId",""+objNotifications.notificationId);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - notificationid ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("notificationtype"))
		{
			try
			{
				objNotifications.strNotificationType=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - notificationtype ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("sender"))
		{
			try
			{
				objNotifications.senderId=Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - sender ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("sendername"))
		{
			try
			{
				objNotifications.strSenderName=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - sendername ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("useraddedfriendid"))
		{
			try
			{
				if(!currentValue.equals("None"))
					objNotifications.friendRequestId=Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - friendrequestid ", e.toString());
			}
		}

		else  if(localName.equalsIgnoreCase("eventid"))
		{
			try
			{
				if(!currentValue.equals("None"))
					objNotifications.eventId=Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - eventid ", e.toString());
			}
		}

		else  if(localName.equalsIgnoreCase("eventname"))
		{
			try
			{
				objNotifications.strEventName=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - eventname ", e.toString());
			}
		}

		else  if(localName.equalsIgnoreCase("text"))
		{
			try
			{
				objNotifications.strText=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - text ", e.toString());
			}
		}

		else  if(localName.equalsIgnoreCase("is_read"))
		{
			try
			{
				if(!currentValue.equals("True"))
					noOfNewNotifications++;
				objNotifications.strIsRead=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - is_read ", e.toString());
			}
		}

		else  if(localName.equalsIgnoreCase("date_created"))
		{
			try
			{
				objNotifications.strDateCreated=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - date_created ", e.toString());
			}
		}

		else  if(localName.equalsIgnoreCase("notification"))
		{
			try
			{
				AppConstants.vctUserNotifications.add(objNotifications);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - notification ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("notifications"))
		{
			try
			{
				UserAccountBusinessLayer userAccBL	=	new UserAccountBusinessLayer();
				for(int i=0;i<AppConstants.vctUserNotifications.size();i++)
				{
					Notifications objNotifications=AppConstants.vctUserNotifications.get(i);
					userAccBL.insertUserNotifications(objNotifications);
				}
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - EventId ", e.toString());
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
