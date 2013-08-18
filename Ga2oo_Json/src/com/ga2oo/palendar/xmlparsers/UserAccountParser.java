package com.ga2oo.palendar.xmlparsers;

import java.util.ArrayList;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.objects.Association;
import com.ga2oo.palendar.objects.UserAccount;
import com.ga2oo.palendar.objects.UserLocation;

public class UserAccountParser extends BaseGa2ooParser
{
	private UserAccount userAccountObj;
	private Association associationObj;
	private UserLocation userLocationObj;
	private UserAccountBusinessLayer userActBL;
	private int check=0;
	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if(localName.equalsIgnoreCase("useraccount"))
		{
			check=1;
			userAccountObj = new UserAccount();
			AppConstants.vctUserAccount = new ArrayList<UserAccount>();
		}
		else if(localName.equalsIgnoreCase("Img"))
		{
			userAccountObj.imagesrc = attributes.getValue("src");
		}
		else if(localName.equalsIgnoreCase("associations"))
		{
			check=2;
//			userAccountObj.vctAssociations = new Vector<Association>();
		}
		else if(localName.equalsIgnoreCase("association"))
		{
			associationObj = new Association();
		}
		else if(localName.equalsIgnoreCase("savedlocations"))
		{
			check=3;
//			userAccountObj.vctSavedlocations = new Vector<UserLocation>();
		}
		else if(localName.equalsIgnoreCase("location"))
		{
			userLocationObj = new UserLocation();
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
		else if(localName.equalsIgnoreCase("currentzip"))
		{
			userAccountObj.zipcode = currentValue;
		}
		else if(localName.equalsIgnoreCase("gender"))
		{
			userAccountObj.gender= currentValue;
		}
		else if(localName.equalsIgnoreCase("birthday"))
		{
			userAccountObj.birthday= currentValue;
		}
		else if(localName.equalsIgnoreCase("password"))
		{
			userAccountObj.password= currentValue;
		}
		else if(localName.equalsIgnoreCase("is_active"))
		{
			userAccountObj.is_active = currentValue;
		}
		else if(localName.equalsIgnoreCase("is_public"))
		{
			userAccountObj.is_public= currentValue;
		}
		else if(localName.equalsIgnoreCase("is_calendar_shared"))
		{
			userAccountObj.is_calendar_shared = currentValue;
		}
		else if(localName.equalsIgnoreCase("deviceid"))
		{
			userAccountObj.deviceid= currentValue;
		}
		else if(localName.equalsIgnoreCase("date_updated"))
		{
			userAccountObj.date_updated= currentValue;
		}
		else if(localName.equalsIgnoreCase("id")&&check==2)
		{
			try
			{
				associationObj.ID = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.i("Error in UserAccountParser - associationObj.ID", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("associationtype"))
		{
			associationObj.Associationtype = currentValue;
		}
		else if(localName.equalsIgnoreCase("associationid"))
		{
			associationObj.Associationid = currentValue;
		}
		else if(localName.equalsIgnoreCase("association") && check==2)
		{
//			userAccountObj.vctAssociations.add(associationObj);
		}
		else if(localName.equalsIgnoreCase("associations"))
		{
			//check=3;
		}
		else if(localName.equalsIgnoreCase("id")&&check==3)
		{
			try
			{
				userLocationObj.id = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.i("Error in UserAccountParser - userLocationObj.ID", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("is_primary"))
		{
			userLocationObj.is_primary = Boolean.parseBoolean(currentValue);
		}
		else if(localName.equalsIgnoreCase("geocode"))
		{
			userLocationObj.geocode = currentValue;
		}
		
		else if(localName.equalsIgnoreCase("address"))
		{
			userLocationObj.address = currentValue;
		}
		else if(localName.equalsIgnoreCase("city"))
		{
			userLocationObj.city = currentValue;
		}
		else if(localName.equalsIgnoreCase("state"))
		{
			userLocationObj.state = currentValue;
		}
		else if(localName.equalsIgnoreCase("zip"))
		{
			userLocationObj.zipcode = currentValue;
		}
		else if(localName.equalsIgnoreCase("country"))
		{
			userLocationObj.country = currentValue;
		}
		
		else if(localName.equalsIgnoreCase("location"))
		{
//			userAccountObj.vctSavedlocations.add(userLocationObj);
		}
		
		else if(localName.equalsIgnoreCase("useraccount"))
		{
			AppConstants.vctUserAccount.add(userAccountObj);
			
			userActBL = new UserAccountBusinessLayer();
			for(int i=0;i<AppConstants.vctUserAccount.size();i++)
			{
				UserAccount obj = AppConstants.vctUserAccount.get(i);
				userActBL.Insert(obj);
			}
//			for(int i=0;i<userAccountObj.vctAssociations.size();i++)
			{
//				Association objAssociation=userAccountObj.vctAssociations.get(i);
//				userActBL.InsertUserAssociation(objAssociation);
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
