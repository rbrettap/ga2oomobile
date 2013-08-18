package com.ga2oo.palendar.xmlparsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.objects.UserAccount;

public class ParseGa2ooUser  extends BaseGa2ooParser
{
	private UserAccount userAccountObj;
	private UserAccountBusinessLayer userActBL;
	private Vector<UserAccount> vctGa2ooUser;
	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if(localName.equalsIgnoreCase("useraccounts"))
		{
			vctGa2ooUser = new Vector<UserAccount>();
		}
		else if(localName.equalsIgnoreCase("useraccount"))
		{
			userAccountObj = new UserAccount();
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
				userAccountObj.id = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.i("Error in UserAccountParser - id", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("username"))
		{
			userAccountObj.username = currentValue;
		}
		else if(localName.equalsIgnoreCase("email"))
		{
			userAccountObj.email= currentValue;
		}
		else if(localName.equalsIgnoreCase("firstname"))
		{
			userAccountObj.firstname = currentValue;
		}
		else if(localName.equalsIgnoreCase("lastname"))
		{
			userAccountObj.lastname= currentValue;
		}
		else if(localName.equalsIgnoreCase("is_active"))
		{
			userAccountObj.is_active = currentValue;
		}
		else if(localName.equalsIgnoreCase("is_public"))
		{
			userAccountObj.is_public= currentValue;
		}
		else if(localName.equalsIgnoreCase("date_updated"))
		{
			userAccountObj.date_updated= currentValue;
		}
		else if(localName.equalsIgnoreCase("useraccount"))
		{
			vctGa2ooUser.add(userAccountObj);
		}
		else if(localName.equalsIgnoreCase("useraccounts"))
		{
			userActBL = new UserAccountBusinessLayer();
			for(int i=0;i<vctGa2ooUser.size();i++)
			{
				UserAccount obj = vctGa2ooUser.get(i);
				userActBL.InsertAllGa2ooUsers(obj);
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
