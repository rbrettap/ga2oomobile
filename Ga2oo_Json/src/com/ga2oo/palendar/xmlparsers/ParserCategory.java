package com.ga2oo.palendar.xmlparsers;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import android.util.Log;

import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.objects.Category;

public class ParserCategory extends BaseGa2ooParser
{
	private Boolean currentElement = false;
	private String currentValue = null;
	private Category category;
	private int subCategoryCount=1;

	/** Called when tag starts ( ex:- <event>AndroidPeople</event> */
	 
	@Override
	public void startElement(String uri, String localName, String qName,Attributes attributes) throws SAXException 
	{
		currentElement = true;

		if (localName.equals("categories"))
		{
			AppConstants.vctCategory=new Vector<Category>();
		}		
		else if(localName.equalsIgnoreCase("Category"))
		{
			category = new Category();
		}
	}

	/** Called when tag closing ( ex:- <event>AndroidPeople</event> 
	 * -- </name> )*/
	@Override
	public void endElement(String uri, String localName, String qName)throws SAXException 
	{
		currentElement = false;
		
		/** set value */ 
		if(localName.equalsIgnoreCase("maincategoryid"))
		{
			try
			{
//				category.MainCategoryId = Integer.parseInt(currentValue);
			}
			catch(Exception e)
			{
				Log.e("Error in ParserCategory - Id ", e.toString());
			}
		}
		else if(localName.equalsIgnoreCase("maincategoryname"))
		{
////			category.strMainCategoryName=currentValue;
		}
		else if(localName.equalsIgnoreCase("date_updated"))
		{
//			category.strDate_updated = currentValue;
		}
		else if(localName.equalsIgnoreCase("subcategoryid") && subCategoryCount==1)
		{
//			category.subcategoryid1 = Integer.parseInt(currentValue);
		}
		else if(localName.equalsIgnoreCase("subcategoryname") && subCategoryCount==1)
		{
//			category.strSubCategoryName1= currentValue;
		}
		else if(localName.equalsIgnoreCase("subcategory"))
		{
			subCategoryCount++;
		}
		else if(localName.equalsIgnoreCase("subcategoryid") && subCategoryCount==2)
		{
//			category.subcategoryid2 = Integer.parseInt(currentValue);
		}
		else if(localName.equalsIgnoreCase("subcategoryname") && subCategoryCount==2)
		{
//			category.strSubCategoryName2= currentValue;
		}
		else if(localName.equalsIgnoreCase("subcategories"))
		{
			subCategoryCount=1;
		}
		else if(localName.equalsIgnoreCase("Category"))
		{
			AppConstants.vctCategory.add(category);
		}
		else if(localName.equalsIgnoreCase("categories"))
		{
			EventsBusinessLayer eventBL=new EventsBusinessLayer();
			for(int i=0;i<AppConstants.vctCategory.size();i++)
			{
				Category objCategory=AppConstants.vctCategory.get(i);
				eventBL.insertCategories(objCategory);
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
