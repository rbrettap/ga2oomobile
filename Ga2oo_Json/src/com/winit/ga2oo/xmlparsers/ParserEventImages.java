package com.winit.ga2oo.xmlparsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.objects.EventImages;

public class ParserEventImages extends BaseGa2ooParser
{
	private Boolean currentElement = false;
	private String currentValue = null;
	private EventImages eventImages;

	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if (localName.equals("tblEventImages"))
		{
			/** Start */ 
		}		
		else if(localName.equalsIgnoreCase("EventImages"))
		{
			eventImages = new EventImages();
		}
	}

	/** Called when tag closing ( ex:- <event>AndroidPeople</event> 
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("EventImageId"))
		{
			try
			{
				eventImages.EventImageId = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in EventImages Parsing - EventImageId ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("EventId"))
		{
			try
			{
				eventImages.EventId = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in EventImages Parsing - EventId ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("Image"))
		{
			eventImages.Image = currentValue;
		}
		else if(localName.equalsIgnoreCase("CreatedOn"))
		{
			eventImages.CreatedOn = currentValue;
		}
		else if(localName.equalsIgnoreCase("CreatedBy"))
		{
			eventImages.CreatedBy = currentValue;
		}
		else if(localName.equalsIgnoreCase("ModifiedOn"))
		{
			eventImages.ModifiedOn= currentValue;
		}
		else if(localName.equalsIgnoreCase("ModifiedBy"))
		{
			eventImages.ModifiedBy = currentValue;
		}
		else if(localName.equalsIgnoreCase("EventImages"))
		{
			AppConstants.vctEventImages.add(eventImages);
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
