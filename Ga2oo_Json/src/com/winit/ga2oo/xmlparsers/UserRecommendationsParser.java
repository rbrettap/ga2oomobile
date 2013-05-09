package com.winit.ga2oo.xmlparsers;

import java.util.ArrayList;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.winit.ga2oo.businesslayer.UserAccountBusinessLayer;
import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.objects.Business;
import com.winit.ga2oo.objects.UserRecommendationObject;

public class UserRecommendationsParser extends BaseGa2ooParser
{
	private Boolean currentElement = false;
	private String currentValue = null;
	private UserRecommendationObject objUserRecommendationObject;
	private int messageTagCount=0;
	public static int noOfNewRecommendations=0;
	
	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if (localName.equals("messages"))
		{
			noOfNewRecommendations=0;
			if(AppConstants.vctUserRecommendations ==null)
				AppConstants.vctUserRecommendations = new ArrayList<UserRecommendationObject>();
			if(AppConstants.vctUserRecommendations !=null)
				AppConstants.vctUserRecommendations .clear();
		}	
		else if (localName.equals("message"))
		{
			if(messageTagCount==0)
				objUserRecommendationObject	=	new UserRecommendationObject();
			messageTagCount++;
		}
	}	
	
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("fromid"))
		{
			try
			{
				objUserRecommendationObject.fromid=Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing objUserRecommendationObject- senderId ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("fromname"))
		{
			try
			{
				objUserRecommendationObject.fromname=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - objUserRecommendationObject.strSenderName ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("thread_id"))
		{
			try
			{
				objUserRecommendationObject.thread_id=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - 	objUserRecommendationObject.strThreadId=currentValue; ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("subject"))
		{
			try
			{
				objUserRecommendationObject.subject=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - objUserRecommendationObject.strSubject ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("message")&&messageTagCount==2)
		{
			try
			{
				objUserRecommendationObject.message=currentValue;
				messageTagCount--;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - objUserRecommendationObject.strMessage ", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("is_read"))
		{
			try
			{
				if(!currentValue.equals("True"))
					noOfNewRecommendations++;
				objUserRecommendationObject.is_read=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - objUserRecommendationObject.strIsRead", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("gaeventid"))
		{
			try
			{
				objUserRecommendationObject.gaeventid=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - objUserRecommendationObject.strGaEventId", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("gaeventname"))
		{
			try
			{
				objUserRecommendationObject.gaeventname=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - objUserRecommendationObject.strGaEventName", e.toString());
			}
		}
		else  if(localName.equalsIgnoreCase("date_created"))
		{
			try
			{
				objUserRecommendationObject.date_created=currentValue;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - objUserRecommendationObject.strDateCreated", e.toString());
			}
			
		}
		else  if(localName.equalsIgnoreCase("message")&& messageTagCount==1)
		{
			try
			{
				AppConstants.vctUserRecommendations.add(objUserRecommendationObject);
				Log.i("raeched message","here"+AppConstants.vctUserRecommendations.size());
				messageTagCount=0;
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - AppConstants.vctUserRecommendations ", e.toString());
			}
		}
		else if (localName.equals("messages"))
		{
			Log.i("raeched","here"+AppConstants.vctUserRecommendations.size());
			UserAccountBusinessLayer userAccBL=new UserAccountBusinessLayer();
			userAccBL.deleteInboxValues();
			for(int i=0;i<AppConstants.vctUserRecommendations.size();i++)
			{
				UserRecommendationObject objUserRecommendationObject=AppConstants.vctUserRecommendations.get(i);
				userAccBL.InsertInUserInbox(objUserRecommendationObject);
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
