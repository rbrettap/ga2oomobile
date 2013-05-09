package com.winit.ga2oo.xmlparsers;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.objects.Globalization;

public class ParserGlobalization extends BaseGa2ooParser
{
	private Boolean currentElement = false;
	private String currentValue = null;
	private Globalization globalization;

	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if (localName.equals("tblBusinessType"))
		{
			/** Start */ 
		}		
		else if(localName.equalsIgnoreCase("Globalization"))
		{
			globalization = new Globalization();
		}

	}

	/** Called when tag closing ( ex:- <event>AndroidPeople</event> 
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("ID"))
		{
			try
			{
				globalization.ID = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in ParsingGlobalization - Id ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("BusinessId"))
		{
			try
			{
				globalization.BusinessId = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in ParsingGlobalization - BusinessId ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("Longitude"))
		{
			try
			{
				globalization.Longitude = Double.parseDouble(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in ParsingGlobalization - Longitude ", e.toString());
			}
		}	
		else if(localName.equalsIgnoreCase("Latitude"))
		{
			try
			{
				globalization.Latitude = Double.parseDouble(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in ParsingGlobalization - Latitude ", e.toString());
			}
		}		
		else if(localName.equalsIgnoreCase("GeoPoint"))
		{
			try
			{
				globalization.GeoPoint = Long.parseLong(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in ParsingGlobalization - Latitude ", e.toString());
			}
		}	
		else if(localName.equalsIgnoreCase("Address"))
		{
			globalization.Address = currentValue;
		}	
		else if(localName.equalsIgnoreCase("Address1"))
		{
			globalization.Address1 = currentValue;
		}
		else if(localName.equalsIgnoreCase("Address2"))
		{
			globalization.Address2 = currentValue;
		}
		else if(localName.equalsIgnoreCase("City"))
		{
			globalization.City = currentValue;
		}	
		else if(localName.equalsIgnoreCase("State"))
		{
			globalization.State = currentValue;
		}	
		else if(localName.equalsIgnoreCase("ZipCode"))
		{
			globalization.ZipCode = currentValue;
		}	
		else if(localName.equalsIgnoreCase("Country"))
		{
			globalization.Country = currentValue;
		}
		else if(localName.equalsIgnoreCase("CreatedOn"))
		{
			globalization.CreatedOn = currentValue;
		}
		else if(localName.equalsIgnoreCase("CreatedBy"))
		{
			globalization.CreatedBy = currentValue;
		}
		else if(localName.equalsIgnoreCase("ModifiedOn"))
		{
			globalization.ModifiedOn= currentValue;
		}
		else if(localName.equalsIgnoreCase("ModifiedBy"))
		{
			globalization.ModifiedBy = currentValue;
		}
		else if(localName.equalsIgnoreCase("Globalization"))
		{
			AppConstants.vctGlobalization.add(globalization);
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
