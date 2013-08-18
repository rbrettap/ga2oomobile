package com.ga2oo.parsing.net;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class HttpHelper
{
	
	public void settingUrlConnection(URL url,DefaultHandler handler) 
	{
		try
		{
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();
			xr.setContentHandler(handler);
			xr.parse(new InputSource(url.openStream()));
		}
		catch (Exception e) 
		{
			Log.e("Error in HttpHelper", e.toString());
		}
	}
}