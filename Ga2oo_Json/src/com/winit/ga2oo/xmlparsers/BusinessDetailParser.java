package com.winit.ga2oo.xmlparsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.objects.Business;

public class BusinessDetailParser extends BaseGa2ooParser
{
	private boolean currentElement = false;
	private String currentValue = null;	
	private Business objBusiness;
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;
		if(localName.equalsIgnoreCase("businessaccount"))
		{
			if(AppConstants.vctBusinessDetail==null)
				AppConstants.vctBusinessDetail = new Vector<Business>();
			else
				AppConstants.vctBusinessDetail.clear();
			objBusiness = new Business();	
		}
		else if(localName.equalsIgnoreCase("img"))
		{
			objBusiness.imagesrc=attributes.getValue("src");
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		if(localName.equalsIgnoreCase("image"))
		{			
			try
			{
				AppConstants.vctBusinessDetail.add(objBusiness);
			}
			catch (Exception e)
			{
				Log.e("error in parsing Business Image ",e.toString());
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
