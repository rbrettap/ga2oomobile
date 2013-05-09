package com.winit.ga2oo.xmlparsers;

import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

public class BaseGa2ooParser extends DefaultHandler
{
	Boolean currentElement = false;
	String currentValue = null;
	public final static String apostrophe = "'";
	public final static String sep = ",";
	public Context context;
}
