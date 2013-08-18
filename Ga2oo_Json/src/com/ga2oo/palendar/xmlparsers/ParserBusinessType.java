package com.ga2oo.palendar.xmlparsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.objects.BusinessType;

public class ParserBusinessType extends BaseGa2ooParser
{
	private Boolean currentElement = false;
	private String currentValue = null;
	private BusinessType businessType;

	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if (localName.equals("tblBusinessType"))
		{
			/** Start */ 
		}		
		else if(localName.equalsIgnoreCase("Business"))
		{
			businessType = new BusinessType();
		}

	}

	/** Called when tag closing ( ex:- <event>AndroidPeople</event> 
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		
		if(localName.equalsIgnoreCase("BusinessTypeId"))
		{
			try
			{
			businessType.BusinessTypeId = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in Event Parsing - EventId ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("BusinessType"))
		{
			businessType.BusinessType = currentValue;
		}		
		else if(localName.equalsIgnoreCase("CreatedOn"))
		{
			businessType.CreatedOn = currentValue;
		}
		else if(localName.equalsIgnoreCase("CreatedBy"))
		{
			businessType.CreatedBy = currentValue;
		}
		else if(localName.equalsIgnoreCase("ModifiedOn"))
		{
			businessType.ModifiedOn= currentValue;
		}
		else if(localName.equalsIgnoreCase("ModifiedBy"))
		{
			businessType.ModifiedBy = currentValue;
		}
		else if(localName.equalsIgnoreCase("Business"))
		{
			AppConstants.vctBusinessType.add(businessType);
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
