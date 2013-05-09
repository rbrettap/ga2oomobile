package com.winit.ga2oo.xmlparsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.objects.EventCategory;

public class ParserEventCategory extends BaseGa2ooParser
{
	private Boolean currentElement = false;
	private String currentValue = null;
	private EventCategory eventCategory;

	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if (localName.equals("tblEventCategory"))
		{
			/** Start */ 
		}		
		else if(localName.equalsIgnoreCase("EventCategory"))
		{
			eventCategory = new EventCategory();
		}
	}

	/** Called when tag closing ( ex:- <event>AndroidPeople</event> 
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("Id"))
		{
			try
			{
				eventCategory.Id = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in ParserEventCategory - Id ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("EventId"))
		{
			try
			{
				eventCategory.EventId = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in ParserEventCategory - EventId  ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("CategoryId"))
		{
			try
			{
				eventCategory.CategoryId = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in ParserEventCategory - categoryId  ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("CreatedOn"))
		{
			eventCategory.CreatedOn = currentValue;
		}
		else if(localName.equalsIgnoreCase("CreatedBy"))
		{
			eventCategory.CreatedBy = currentValue;
		}
		else if(localName.equalsIgnoreCase("ModifiedOn"))
		{
			eventCategory.ModifiedOn= currentValue;
		}
		else if(localName.equalsIgnoreCase("ModifiedBy"))
		{
			eventCategory.ModifiedBy = currentValue;
		}
		else if(localName.equalsIgnoreCase("EventCategory"))
		{
			AppConstants.vctEventCategory.add(eventCategory);
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
