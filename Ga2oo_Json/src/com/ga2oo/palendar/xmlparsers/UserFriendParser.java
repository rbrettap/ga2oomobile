package com.ga2oo.palendar.xmlparsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.ga2oo.palendar.businesslayer.FriendsBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.objects.UserFriend;

public class UserFriendParser extends BaseGa2ooParser
{
	private UserFriend userFriendObj;
	private FriendsBusinessLayer userFriendBL;
	private int check=0;
	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if(localName.equalsIgnoreCase("useraccount"))
		{
			userFriendObj = new UserFriend();
			AppConstants.vctUserFriend = new Vector<UserFriend>();
			check=1;
		}
		else if(localName.equalsIgnoreCase("Img"))
		{
			userFriendObj.imagesrc = attributes.getValue("src");
		}
		
	}

	/** Called when tag closing ( ex:- <event>AndroidPeople</event> 
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("id")&&check==1)
		{
			try
			{
				userFriendObj.id = Integer.parseInt(currentValue);
				check=2;
			}
			catch(Exception e)
			{
				Log.i("Error in UserAccountParser - id", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("username"))
		{
			userFriendObj.username = currentValue;
		}
		else if(localName.equalsIgnoreCase("email"))
		{
			userFriendObj.email= currentValue;
		}
		else if(localName.equalsIgnoreCase("firstname"))
		{
			userFriendObj.firstname = currentValue;
		}
		else if(localName.equalsIgnoreCase("lastname"))
		{
			userFriendObj.lastname= currentValue;
		}
		else if(localName.equalsIgnoreCase("currentzip"))
		{
//			userFriendObj.zipcode = currentValue;
		}
		else if(localName.equalsIgnoreCase("gender"))
		{
			userFriendObj.gender= currentValue;
		}
		else if(localName.equalsIgnoreCase("birthday"))
		{
			userFriendObj.birthday= currentValue;
		}
		else if(localName.equalsIgnoreCase("password"))
		{
			userFriendObj.password= currentValue;
		}
		else if(localName.equalsIgnoreCase("is_active"))
		{
			userFriendObj.is_active = currentValue;
		}
		else if(localName.equalsIgnoreCase("is_public"))
		{
			userFriendObj.is_public= currentValue;
		}
		else if(localName.equalsIgnoreCase("is_calendar_shared"))
		{
			userFriendObj.is_calendar_shared = currentValue;
		}
		else if(localName.equalsIgnoreCase("deviceid"))
		{
			userFriendObj.deviceid= currentValue;
		}
	
		else if(localName.equalsIgnoreCase("date_updated"))
		{
			userFriendObj.date_updated= currentValue;
		}
		
		else if(localName.equalsIgnoreCase("useraccount"))
		{
			AppConstants.vctUserFriend.add(userFriendObj);
			
			userFriendBL = new FriendsBusinessLayer();
			for(int i=0;i<AppConstants.vctUserFriend.size();i++)
			{
				UserFriend obj = AppConstants.vctUserFriend.get(i);
//				userFriendBL.Insert(obj);
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
