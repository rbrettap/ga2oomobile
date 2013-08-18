package com.ga2oo.palendar.common;

import java.util.List;

import com.ga2oo.palendar.R;

import android.content.Context;
import android.content.pm.ResolveInfo;

public class Tools 
{
	private static final String GMAIL = ".gmail.";
	private static final String MAIL = ".mail.";
	private static final String EMAIL = ".email.";
	private static final String COM_HTC_ANDROID_MAIL = "com.htc.android.mail.";
	private static final String COM_ANDROID_EMAIL = "com.android.email.";
	public static String selectedImg="";
	public static String getMonthFromNumber(int intMonth, Context context)
	{
		String strMonth = "";
		
		switch(intMonth)
		{
			case 1:
					strMonth = context.getResources().getString(R.string.january);break;
			case 2:
					strMonth = context.getResources().getString(R.string.february);break;
			case 3:
					strMonth = context.getResources().getString(R.string.march);break;
			case 4:
					strMonth = context.getResources().getString(R.string.april);break;
			case 5:
					strMonth = context.getResources().getString(R.string.may);break;
			case 6:
					strMonth = context.getResources().getString(R.string.june);break;
			case 7:
					strMonth = context.getResources().getString(R.string.july);break;
			case 8:
					strMonth = context.getResources().getString(R.string.august);break;
			case 9:
					strMonth = context.getResources().getString(R.string.september);break;
			case 10:
					strMonth = context.getResources().getString(R.string.october);break;
			case 11:
					strMonth = context.getResources().getString(R.string.november);break;
			case 12:
					strMonth = context.getResources().getString(R.string.december);break;
		}
		
		return strMonth;
	}
	
	public static int getMailIntentIndex(List<ResolveInfo> list)
    {
    	boolean isFound = false;
		int index = -1;	
		
		if(!isFound)
		{
			for(int i=0; i<list.size(); i++)
			{
				ResolveInfo ri = list.get(i);
				if(ri.isDefault)
				{
					index = i;
					isFound = true;
					break;
				}
			}
		}
		
		if(!isFound)
		{
			for(int i=0; i<list.size(); i++)
			{
				ResolveInfo ri = list.get(i);
				if(ri.activityInfo.name.contains(COM_ANDROID_EMAIL))
				{
					index = i;
					isFound = true;
					break;
				}
			}
		}
		
		if(!isFound)
		{
			for(int i=0; i<list.size(); i++)
			{
				ResolveInfo ri = list.get(i);
				if(ri.activityInfo.name.contains(COM_HTC_ANDROID_MAIL))
				{
					index = i;
					isFound = true;
					break;
				}
			}
		}
		
		if(!isFound)
		{
			for(int i=0; i<list.size(); i++)
			{
				ResolveInfo ri = list.get(i);
				if(ri.activityInfo.name.contains(EMAIL))
				{
					index = i;
					isFound = true;
					break;
				}
			}
		}
		
		if(!isFound)
		{
			for(int i=0; i<list.size(); i++)
			{
				ResolveInfo ri = list.get(i);
				if(ri.activityInfo.name.contains(MAIL))
				{
					index = i;
					isFound = true;
					break;
				}
			}
		}
		
		if(!isFound)
		{
			for(int i=0; i<list.size(); i++)
			{
				ResolveInfo ri = list.get(i);
				if(ri.activityInfo.name.contains(GMAIL))
				{
					index = i;
					isFound = true;
					break;
				}
			}
		}
		
		return index;
    }
}
