package com.winit.ga2oo.xmlparsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.winit.ga2oo.businesslayer.UserAccountBusinessLayer;
import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.objects.Friend;

public class FriendParser extends BaseGa2ooParser
{
	private Friend friendObj;
	private UserAccountBusinessLayer userActBL;
	private int state=1;
	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;
       if (localName.equals("friendships"))
		{
			AppConstants.vctFriend = new Vector<Friend>();
		}		
		else if(localName.equalsIgnoreCase("friend"))
		{
			friendObj = new Friend();
		}
		else if(localName.equalsIgnoreCase("Img"))
		{
			friendObj.imagesrc = attributes.getValue("src");
		}
		else if(localName.equalsIgnoreCase("location"))
		{
		}
	}

	/** Called when tag closing ( ex:- <event>AndroidPeople</event> 
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("useraddedfriendid"))
		{
			try
			{
				friendObj.useraddedfriendid = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in Friend Parser ", e.toString());
			}
		}
		if(localName.equalsIgnoreCase("status"))
		{
			try
			{
//				friendObj.status = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in Friend Parser status", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("friendid"))
		{
			try
			{
				friendObj.friendid = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in Friend Parser ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("firstname"))
		{
			friendObj.firstname = currentValue;
		}
		else if(localName.equalsIgnoreCase("lastname"))
		{
			friendObj.lastname = currentValue;
		}
		else if(localName.equalsIgnoreCase("is_calendar_shared"))
		{
//			friendObj.strIsCalenderShared = currentValue;
		}
		else if(localName.equalsIgnoreCase("email"))
		{
			friendObj.email = currentValue;
		}
		else if(localName.equalsIgnoreCase("date_updated"))
		{
			friendObj.date_updated = currentValue;
		}
		else if(localName.equalsIgnoreCase("id"))
		{
			try
			{
//	/			if(!currentValue.equals("None"))
	//				friendObj.FriendLocationId = Integer.parseInt(currentValue);
			}
			catch (Exception e)
			{
			}
		}
		else if(localName.equalsIgnoreCase("is_primary"))
		{
			friendObj.is_primary = currentValue;
		}
		else if(localName.equalsIgnoreCase("geocode"))
		{
			friendObj.geocode = currentValue;
		}
		else if(localName.equalsIgnoreCase("address"))
		{
			friendObj.address = currentValue;
		}
		else if(localName.equalsIgnoreCase("city"))
		{
			friendObj.city = currentValue;
		}
		else if(localName.equalsIgnoreCase("state"))
		{
			friendObj.state = currentValue;
		}
		else if(localName.equalsIgnoreCase("zipcode"))
		{
			friendObj.zipcode = currentValue;
		}
		else if(localName.equalsIgnoreCase("country"))
		{
			friendObj.country = currentValue;
		}
		else if(localName.equalsIgnoreCase("friend"))
		{
			AppConstants.vctFriend.add(friendObj);
		}
		else if(localName.equalsIgnoreCase("friendships"))
		{
			userActBL = new UserAccountBusinessLayer();
			for(int i=0;i<AppConstants.vctFriend.size();i++)
			{
				Friend obj = AppConstants.vctFriend.get(i);
				userActBL.InsertFriend(obj);
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
