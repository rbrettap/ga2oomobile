package com.ga2oo.palendar.controls;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rjb.android.impl.core.log.Flog;
import com.ga2oo.palendar.BusinessPalender;
import com.ga2oo.palendar.DrawableManager;
import com.ga2oo.palendar.EventDetails;
import com.ga2oo.palendar.Events;
import com.ga2oo.palendar.FriendsPalender;
import com.ga2oo.palendar.Palendar;
import com.ga2oo.palendar.R;
import com.ga2oo.palendar.TabGroupActivity;
import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.objects.EventDates;
import com.ga2oo.palendar.objects.EventsDetails;

public class CalendarView extends LinearLayout
{	
	
	private static final String EVENT = "event";

	private static final String NO_EVENT = "no event";

	private PopUpDailog customDialog;
	private int month, year;
	private Context context;
	private int days,preDays;
	private int selDay = 0,userFriendId,businessId;
	private CalendarListener listener;
	private View prevView = null;
	private Drawable prevBackground;
	private Toast toast;
	private EventsBusinessLayer eventBL;
	private DrawableManager objDrawableManager;
	private String strAttendingEvents;
	public static String strDate="",strPalenderSelecteddate="palender",strSelectedDate="friends palender";
	private EventsDetails objEvent;
	private EventDates objEventDates;
	static public List<EventDates> vctEventdates;
	public static List<EventsDetails> vctEventList;
	public static List<EventsDetails> vctEventsOnDate;
	private int eventCount=0;
	public static int userAddedEventId,eventId;
	private CustomPalEventsAdapter objCustomPalEventsAdapter;
	private CustomCalEventsAdapter objCustomCalEventsAdapter;
	private CalendarListener currenntListener;
	
	private static final String kLogTag = CalendarView.class.getSimpleName();
	
	public CalendarView(Context context, int month, int year, CalendarListener cl)
	{
		
		super(context);
		this.context = context;		
		this.month = month;
		this.year = year;
		this.listener = (CalendarListener)context;
		this.currenntListener = cl;
		objDrawableManager=new DrawableManager();
		eventBL=new EventsBusinessLayer();
		vctEventdates=new ArrayList<EventDates>();
		strAttendingEvents=eventBL.getLogedInUserAttendingEvents(AppConstants.USER_ID);
		vctEventList=new ArrayList<EventsDetails>();
//		vctEventList=eventBL.getAllEventsList();
	}

	public LinearLayout makeCalendar()
	{
		Calendar cal = new GregorianCalendar(year, month, 1);
		days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		Calendar cal1 = new GregorianCalendar(year, month-1, 1);
		preDays = cal1.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		this.setOrientation(1);
		for(int date = 1;date <= days;)
		{
			int count = 0,j=1; 
			LinearLayout llHorizontal = new LinearLayout(context);
			llHorizontal.setPadding(0, 0, 0, 0);
			for(int i=1; i < dayOfWeek; i++)
			{
				count++;
				llHorizontal.addView(addButtonWithOutDate((preDays - (dayOfWeek-1)+i)), new LayoutParams(46,44));
			}
			for(int i = count; i < 7; i++, count++,eventCount=0)
			{				
				if(date > days)
					break;
				llHorizontal.addView(addButtonWithDate(date++), new LayoutParams(46,44));
			}
			for(int i = count; i < 7; i++)
			{
				
				llHorizontal.addView(addButtonWithOutDate(j++), new LayoutParams(46,44));
			}
			dayOfWeek = 0;
			this.addView(llHorizontal);
		}
		return this;
	}
	
	public RelativeLayout addButtonWithDate(int date)
	{
	    Flog.p(Flog.DEBUG,  kLogTag, "addButtonWithDate");
	    
		final RelativeLayout rlEachDate=new RelativeLayout(context);
		TextView tvNoOfEvents=new TextView(context);
		tvNoOfEvents.setText("");
		tvNoOfEvents.setTextColor(Color.WHITE);
		tvNoOfEvents.setTextSize(10);
		tvNoOfEvents.setGravity(Gravity.CENTER);
		tvNoOfEvents.setBackgroundResource(R.drawable.alert_bg);
		tvNoOfEvents.setPadding(0, 0, 0, 0);
		rlEachDate.setPadding(2, 2, 2, 2);
		rlEachDate.setBackgroundResource(R.drawable.calender_cell_bg);
		Button btnDate = new Button(context);
		
		btnDate.setId(date);
		btnDate.setText(date+"");  //For prototype I commented this
		btnDate.setTextSize(16);
		btnDate.setGravity(Gravity.TOP|Gravity.RIGHT);
		btnDate.setPadding(0, 0, 7, 0);
		btnDate.setTypeface(Typeface.DEFAULT_BOLD);
		btnDate.setBackgroundColor(Color.TRANSPARENT);
		btnDate.setTag(getResources().getString(R.string.no_event));
		rlEachDate.setTag(NO_EVENT);
		rlEachDate.setId(date);
		for(int i=0;i<vctEventList.size();i++)
		{
			String str2array=vctEventList.get(i).event_start_date;
			String[] arr=str2array.split("-");
			if(arr.length==3)
			if(date == Integer.parseInt(arr[2]) && month+1 == Integer.parseInt(arr[1]) && year == Integer.parseInt(arr[0]))	
			{
				btnDate.setTag(EVENT);
				rlEachDate.setTag(EVENT);
				if(vctEventList.get(i).image!=null && !vctEventList.get(i).image.equals(AppConstants.NOIMAGE))
				{
					if(!objDrawableManager.fetchDrawableOnThreadButton(AppConstants.IMAGE_HOST_URL+vctEventList.get(i).image, btnDate, 44, 44)){
						btnDate.setBackgroundResource(R.drawable.no_image_smal_event);
					}
				}
				{
					btnDate.setBackgroundResource(R.drawable.no_image_smal_event);
				}
				eventCount++;
			}
				  
			else
			{
				btnDate.setText(date+"");
			}
		}

		if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == date && Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.YEAR) == year)
		{
			btnDate.setTextColor(Color.rgb(0,136,194));
			if(!btnDate.getTag().toString().equals(EVENT))
				rlEachDate.setBackgroundResource(R.drawable.bluebg);
			if(!strDate.equals(""))
			{
				String selectedDate[]=strDate.split("-");
				if(selectedDate.length==3)
				{
					if(Integer.parseInt(selectedDate[2]) == date && Integer.parseInt(selectedDate[1])-1 == month && Integer.parseInt(selectedDate[0]) == year)
					{
						if(!btnDate.getTag().toString().equals(EVENT))
						{
							rlEachDate.setBackgroundResource(R.drawable.calender_cell_bg_hoveroutline);
						}
						else if(btnDate.getTag().toString().equals(EVENT))
						{
							rlEachDate.setBackgroundResource(R.drawable.datebg);
						}
					}
				}
			}
			else
			{
				int monthLocal=month+1;
				if(month<9  && date<10)
				{
					strDate=year+"-0"+monthLocal+"-0"+date;
				}
				else if(month<9)
				{
					strDate=year+"-0"+monthLocal+"-"+date;
				}
				else if(date<10)
				{
					strDate=year+"-"+monthLocal+"-0"+date;
				}
				else
				{
					strDate=year+"-"+monthLocal+"-"+date;
				}
			}
		}
		else if(!strDate.equals(""))
		{
			String selectedDate[]=strDate.split("-");
			if(selectedDate.length==3)
			{
				if(Integer.parseInt(selectedDate[2]) == date && Integer.parseInt(selectedDate[1])-1 == month && Integer.parseInt(selectedDate[0]) == year)
				{
					if(!btnDate.getTag().toString().equals(EVENT))
					{
						rlEachDate.setBackgroundResource(R.drawable.calender_cell_bg_hoveroutline);
//						if(vctEventsOnDate==null)
//							vctEventsOnDate=new Vector<EventsDetails>();
//						if(vctEventsOnDate!=null)
//							vctEventsOnDate.clear();
//				
//						vctEventsOnDate=eventBL.getEventOnDate(strDate,Events.selectedCategoryId,Events.strSearchKeyWordForCalView);
//						if(vctEventsOnDate!=null)
//						{
//							Events.lvEvents.setVisibility(View.VISIBLE);
//							if(objCustomCalEventsAdapter==null)
//								Events.lvEvents.setAdapter(objCustomCalEventsAdapter=new CustomCalEventsAdapter(vctEventsOnDate));
//							else
//								objCustomCalEventsAdapter.refresh(vctEventsOnDate);
//						}
					}
					else if(btnDate.getTag().toString().equals(EVENT))
					{
						rlEachDate.setBackgroundResource(R.drawable.datebg);
					}
				}
			}
		}
		else
		{
			btnDate.setTextColor(Color.DKGRAY);
		}
		
		btnDate.setOnClickListener(new OnClickListener()
		{
		
			public void onClick(View btnDate) 
			{
				toast = Toast.makeText(context, getResources().getString(R.string.no_any_event_on)+strDate+".", Toast.LENGTH_SHORT);
				if(prevView == null)
				{
					prevBackground = btnDate.getBackground();
					prevView = btnDate;
				}
				prevView.setBackgroundDrawable(prevBackground);
				
				prevView = btnDate;
				prevBackground = btnDate.getBackground();
				if(!btnDate.getTag().toString().equals(EVENT))
				{
					for(int j=0;j<=days;j++)
					{
						RelativeLayout rl=new RelativeLayout(context);
						rl=(RelativeLayout)findViewById(j);
						if(rl!=null && j==btnDate.getId())
							rl.setBackgroundResource(R.drawable.calender_cell_bg_hoveroutline);
						else if(rl!=null && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == j && Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.YEAR) == year && !rl.getTag().toString().equals(EVENT))
							rl.setBackgroundResource(R.drawable.bluebg);
						else if(rl!=null)
							rl.setBackgroundResource(R.drawable.calender_cell_bg);
					}
				}
				else
				{
					for(int j=0;j<=days;j++)
					{
						RelativeLayout rl=new RelativeLayout(context);
						rl=(RelativeLayout)findViewById(j);
						if(rl!=null)
						{
							if(j==btnDate.getId())
								rl.setBackgroundResource(R.drawable.datebg);
							else if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == j && Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.YEAR) == year && !rl.getTag().toString().equals(EVENT))
								rl.setBackgroundResource(R.drawable.bluebg);
							else
								rl.setBackgroundResource(R.drawable.calender_cell_bg);
						}
					}
				}
				selDay = btnDate.getId();	
				
				int monthLocal=month+1;
				if(month<9  && selDay<10)
				{
					strDate=year+"-0"+monthLocal+"-0"+selDay;
				}
				else if(month<9)
				{
					strDate=year+"-0"+monthLocal+"-"+selDay;
				}
				else if(selDay<10)
				{
					strDate=year+"-"+monthLocal+"-0"+selDay;
				}
				else
				{
					strDate=year+"-"+monthLocal+"-"+selDay;
				}
				vctEventsOnDate=new ArrayList<EventsDetails>();
				vctEventsOnDate.clear();
				vctEventsOnDate=eventBL.getEventOnDate(strDate,Events.selectedCategoryId,Events.strSearchKeyWordForCalView);
				if(vctEventsOnDate!=null)
				{
					if(objCustomCalEventsAdapter==null){
						currenntListener.showList();
					}else{
						objCustomCalEventsAdapter.refresh(vctEventsOnDate);
					}
				}
				else
				{
					toast.show();
				}
				listener.daySelected(selDay, month, year);						
			}
		});
		rlEachDate.addView(btnDate,new LayoutParams(46,44));
		if(eventCount!=0)
		{
			tvNoOfEvents.setText(""+eventCount);
			rlEachDate.addView(tvNoOfEvents,new LayoutParams(15,15));
		}
		return rlEachDate;
	}
	
	public LinearLayout makeFriendPalendar(int userFriendId)
	{
		this.userFriendId=userFriendId;
		Calendar cal = new GregorianCalendar(year, month, 1);
		days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		Calendar cal1 = new GregorianCalendar(year, month-1, 1);
		preDays = cal1.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		this.setOrientation(1);
		
		for(int date = 1;date <= days;)
		{
			int count = 0,j=1; 
			LinearLayout llHorizontal = new LinearLayout(context);
			for(int i=1; i < dayOfWeek; i++)
			{
				count++;
				llHorizontal.addView(addButtonWithOutDate((preDays - (dayOfWeek-1)+i)), new LayoutParams(46,44));
			}
			for(int i = count; i < 7; i++, count++,eventCount=0)
			{				
				if(date > days)
					break;
				llHorizontal.addView(addButtonWithDateInFriendPalendar(date++), new LayoutParams(46,44));
			}
			for(int i = count; i < 7; i++)
			{
				
				llHorizontal.addView(addButtonWithOutDate(j++), new LayoutParams(46,44));
			}
			dayOfWeek = 0;
			this.addView(llHorizontal);
		}
		return this;
	}
	
	public RelativeLayout addButtonWithDateInFriendPalendar(int date)
	{
		RelativeLayout rlEachDate=new RelativeLayout(context);
		TextView tvNoOfEvents=new TextView(context);
		tvNoOfEvents.setText("");
		tvNoOfEvents.setTextColor(Color.WHITE);
		tvNoOfEvents.setTextSize(10);
		tvNoOfEvents.setGravity(Gravity.CENTER);
		tvNoOfEvents.setBackgroundResource(R.drawable.alert_bg);
		tvNoOfEvents.setPadding(3, 0, 0, 0);
		rlEachDate.setId(date);
		rlEachDate.setBackgroundResource(R.drawable.calender_cell_bg);
		rlEachDate.setPadding(2, 2, 2, 2);
		Button btnDate = new Button(context);
		
		btnDate.setId(date);
		btnDate.setText(date+"");  //For prototype I commented this
		btnDate.setTextSize(16);
		btnDate.setGravity(Gravity.TOP|Gravity.RIGHT);
		btnDate.setPadding(0, 0, 7, 0);
		btnDate.setTypeface(Typeface.DEFAULT_BOLD);
		btnDate.setBackgroundColor(Color.TRANSPARENT);
		btnDate.setTag(NO_EVENT);
		rlEachDate.setTag(NO_EVENT);
		
		vctEventdates.clear();
		strAttendingEvents="";
		strAttendingEvents=eventBL.getLogedInUserAttendingEvents(userFriendId);
		vctEventdates=eventBL.getdatesOfUserAttendingEventsCalView(strAttendingEvents,FriendsPalender.categoryId,FriendsPalender.strSearchKeyWord);
		if(vctEventdates.size()!=0)
		{
		 for(int i=0;i<vctEventdates.size();i++)
			  {
				  String str2array=vctEventdates.get(i).strEventStartdate;
			      String[] arr=str2array.split("-");
			      if(arr.length==3)
				  if(date == Integer.parseInt(arr[2]) && month+1 == Integer.parseInt(arr[1]) && year == Integer.parseInt(arr[0]))	
				  {
					  btnDate.setTag(EVENT);
					  rlEachDate.setTag(EVENT);
					  if(!vctEventList.get(i).image.equals("no image"))
					  {
						  if(!objDrawableManager.fetchDrawableOnThreadButton(AppConstants.IMAGE_HOST_URL+vctEventdates.get(i).eventImage, btnDate, 44, 44)){
							  btnDate.setBackgroundResource(R.drawable.no_image_smal_event);
						  }
					  }
					  {
						  btnDate.setBackgroundResource(R.drawable.no_image_smal_event);
					  }
					  eventCount++;
				  }
				  
				else
				{
					
					btnDate.setText(date+"");
				}
			  }
		}
		else
		{
			btnDate.setText(date+"");
		}
		if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == date && Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.YEAR) == year)
		{
			btnDate.setTextColor(Color.rgb(0,136,194));
			if(!btnDate.getTag().toString().equals(EVENT))
				rlEachDate.setBackgroundResource(R.drawable.bluebg);
			if(!strSelectedDate.equals("friends palender"))
			{
				String selectedDate[]=strSelectedDate.split("-");
				if(selectedDate.length==3)
				{
					if(Integer.parseInt(selectedDate[2]) == date && Integer.parseInt(selectedDate[1])-1 == month && Integer.parseInt(selectedDate[0]) == year)
					{
						if(!btnDate.getTag().toString().equals(EVENT))
						{
							rlEachDate.setBackgroundResource(R.drawable.calender_cell_bg_hoveroutline);
						}
						else if(btnDate.getTag().toString().equals(EVENT))
						{
							rlEachDate.setBackgroundResource(R.drawable.datebg);
						}
					}
				}
			}
			else
			{
				int monthLocal=month+1;
				if(month<9  && date<10)
				{
					strSelectedDate=year+"-0"+monthLocal+"-0"+date;
				}
				else if(month<9)
				{
					strSelectedDate=year+"-0"+monthLocal+"-"+date;
				}
				else if(date<10)
				{
					strSelectedDate=year+"-"+monthLocal+"-0"+date;
				}
				else
				{
					strSelectedDate=year+"-"+monthLocal+"-"+date;
				}
			}
		}
		else if(!strSelectedDate.equals("friends palender"))
		{
			String selectedDate[]=strSelectedDate.split("-");
			if(selectedDate.length==3)
			{
				if(Integer.parseInt(selectedDate[2]) == date && Integer.parseInt(selectedDate[1])-1 == month && Integer.parseInt(selectedDate[0]) == year)
				{
					if(!btnDate.getTag().toString().equals(EVENT))
					{
						rlEachDate.setBackgroundResource(R.drawable.calender_cell_bg_hoveroutline);
					}
					else if(btnDate.getTag().toString().equals(EVENT))
					{
						rlEachDate.setBackgroundResource(R.drawable.datebg);
					}
				}
			}
		}
		else
		{
			btnDate.setTextColor(Color.DKGRAY);
		}
		
		btnDate.setOnClickListener(new OnClickListener()
		{
		
			public void onClick(View btnDate) 
			{
				toast = Toast.makeText(context, "No any event on "+strDate+".", Toast.LENGTH_SHORT);
				if(prevView == null)
				{
					prevBackground = btnDate.getBackground();
					prevView = btnDate;
				}
				prevView.setBackgroundDrawable(prevBackground);
				
				prevView = btnDate;
				prevBackground = btnDate.getBackground();
				
				if(!btnDate.getTag().toString().equals(EVENT))
				{
					for(int j=0;j<=days;j++)
					{
						RelativeLayout rl=new RelativeLayout(context);
						rl=(RelativeLayout)findViewById(j);
						if(rl!=null && j==btnDate.getId())
							rl.setBackgroundResource(R.drawable.calender_cell_bg_hoveroutline);
						else if(rl!=null && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == j && Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.YEAR) == year && !rl.getTag().toString().equals(EVENT))
							rl.setBackgroundResource(R.drawable.bluebg);
						else if(rl!=null)
							rl.setBackgroundResource(R.drawable.calender_cell_bg);
					}
				}
				else
				{
					for(int j=0;j<=days;j++)
					{
						RelativeLayout rl=new RelativeLayout(context);
						rl=(RelativeLayout)findViewById(j);
						if(rl!=null)
						{
							if(j==btnDate.getId())
								rl.setBackgroundResource(R.drawable.datebg);
							else if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == j && Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.YEAR) == year && !rl.getTag().toString().equals(EVENT))
								rl.setBackgroundResource(R.drawable.bluebg);
							else
								rl.setBackgroundResource(R.drawable.calender_cell_bg);
						}
					}
				}
				selDay = btnDate.getId();	
				int monthLocal=month+1;
				if(month<9  && selDay<10)
				{
					strSelectedDate=year+"-0"+monthLocal+"-0"+selDay;
				}
				else if(month<9)
				{
					strSelectedDate=year+"-0"+monthLocal+"-"+selDay;
				}
				else if(selDay<10)
				{
					strSelectedDate=year+"-"+monthLocal+"-0"+selDay;
				}
				else
				{
					strSelectedDate=year+"-"+monthLocal+"-"+selDay;
				}
				vctEventdates=new ArrayList<EventDates>();
				vctEventdates.clear();
				vctEventdates			= eventBL.getdatesFriendsPalenderCalView(FriendsPalender.strAttendingEvents,strSelectedDate,FriendsPalender.categoryId,FriendsPalender.strSearchKeyWord);
				if(vctEventdates!=null)
				{
					//TODO:go to events list
//					FriendsPalender.lvEvents.setVisibility(View.VISIBLE);
//					FriendsPalender.lvEvents.setAdapter(new CustomFriendPalEventsAdapter(vctEventdates));
					currenntListener.showList();
				}
				else
				{
					toast.show();
				}
				listener.daySelected(selDay, month, year);						
			}
		});
		rlEachDate.addView(btnDate,new LayoutParams(46,44));
		if(eventCount!=0)
		{
			tvNoOfEvents.setText(""+eventCount);
			rlEachDate.addView(tvNoOfEvents,new LayoutParams(15,15));
		}
		return rlEachDate;
	}

	
	public LinearLayout makePalendar()
	{
		Calendar cal = new GregorianCalendar(year, month, 1);
		days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		Calendar cal1 = new GregorianCalendar(year, month-1, 1);
		preDays = cal1.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		this.setOrientation(1);
		
		for(int date = 1;date <= days;)
		{
			int count = 0,j=1; 
			LinearLayout llHorizontal = new LinearLayout(context);
			for(int i=1; i < dayOfWeek; i++)
			{
				count++;
				llHorizontal.addView(addButtonWithOutDate((preDays - (dayOfWeek-1)+i)), new LayoutParams(46,44));
			}
			for(int i = count; i < 7; i++, count++,eventCount=0)
			{				
				if(date > days)
					break;
				llHorizontal.addView(addButtonWithDateInPalendar(date++), new LayoutParams(46,44));
			}
			for(int i = count; i < 7; i++)
			{
				
				llHorizontal.addView(addButtonWithOutDate(j++), new LayoutParams(46,44));
			}
			dayOfWeek = 0;
			this.addView(llHorizontal);
		}
		return this;
	}
	
	public RelativeLayout addButtonWithDateInPalendar(int date)
	{
		
		RelativeLayout rlEachDate=new RelativeLayout(context);
		TextView tvNoOfEvents=new TextView(context);
		tvNoOfEvents.setText("");
		tvNoOfEvents.setTextColor(Color.WHITE);
		tvNoOfEvents.setTextSize(10);
		tvNoOfEvents.setGravity(Gravity.CENTER);
		tvNoOfEvents.setBackgroundResource(R.drawable.alert_bg);
		tvNoOfEvents.setPadding(3, 0, 0, 0);
		rlEachDate.setPadding(2, 2, 2, 2);
		Button btnDate = new Button(context);
		rlEachDate.setId(date);
		rlEachDate.setBackgroundResource(R.drawable.calender_cell_bg);
		btnDate.setId(date);
		btnDate.setText(date+"");  //For prototype I commented this
		btnDate.setTextSize(16);
		btnDate.setGravity(Gravity.TOP|Gravity.RIGHT);
		btnDate.setPadding(0, 0, 7, 0);
		btnDate.setTypeface(Typeface.DEFAULT_BOLD);
		btnDate.setBackgroundColor(Color.TRANSPARENT);
		btnDate.setTag(NO_EVENT);
		rlEachDate.setTag(NO_EVENT);
		vctEventdates.clear();
		vctEventdates=eventBL.getdatesOfUserAttendingEventsCalView(strAttendingEvents,Palendar.categoryId,Palendar.strSearchKeyWord);
		if(vctEventdates.size()!=0)
		{
		 for(int i=0;i<vctEventdates.size();i++)
			  {
				  String str2array=vctEventdates.get(i).strEventStartdate;
			      String[] arr=str2array.split("-");
			      if(arr.length==3)
				  if(date == Integer.parseInt(arr[2]) && month+1 == Integer.parseInt(arr[1]) && year == Integer.parseInt(arr[0]))	
				  {
					  btnDate.setTag(EVENT);
					  rlEachDate.setTag(EVENT);
					  if(!vctEventdates.get(i).eventImage.equals("no image"))
					  {
						 if(!objDrawableManager.fetchDrawableOnThreadButton(AppConstants.IMAGE_HOST_URL+vctEventdates.get(i).eventImage, btnDate, 44, 44)){
							 btnDate.setBackgroundResource(R.drawable.no_image_smal_event); 
						 }
					  }
					  else{
						  btnDate.setBackgroundResource(R.drawable.no_image_smal_event);
					  }
					  eventCount++;
				  }
				  
				else
				{
					
					btnDate.setText(date+"");
				}
			  }
		}
		else
		{
			btnDate.setText(date+"");
		}
		if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == date && Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.YEAR) == year)
		{
			btnDate.setTextColor(Color.rgb(0,136,194));
			if(!btnDate.getTag().toString().equals(EVENT))
				rlEachDate.setBackgroundResource(R.drawable.bluebg);
			if(!strPalenderSelecteddate.equals("palender"))
			{
				String selectedDate[]=strPalenderSelecteddate.split("-");
				if(selectedDate.length==3)
				{
					if(Integer.parseInt(selectedDate[2]) == date && Integer.parseInt(selectedDate[1])-1 == month && Integer.parseInt(selectedDate[0]) == year)
					{
						if(!btnDate.getTag().toString().equals(EVENT))
						{
							rlEachDate.setBackgroundResource(R.drawable.calender_cell_bg_hoveroutline);
						}
						else if(btnDate.getTag().toString().equals(EVENT))
						{
							rlEachDate.setBackgroundResource(R.drawable.datebg);
						}
					}
				}
			}
			else
			{
				int monthLocal=month+1;
				if(month<9  && date<10)
				{
					strPalenderSelecteddate=year+"-0"+monthLocal+"-0"+date;
				}
				else if(month<9)
				{
					strPalenderSelecteddate=year+"-0"+monthLocal+"-"+date;
				}
				else if(date<10)
				{
					strPalenderSelecteddate=year+"-"+monthLocal+"-0"+date;
				}
				else
				{
					strPalenderSelecteddate=year+"-"+monthLocal+"-"+date;
				}
			}

		}
		else if(!strPalenderSelecteddate.equals("palender"))
		{
			String selectedDate[]=strPalenderSelecteddate.split("-");
			if(selectedDate.length==3)
			{
				if(Integer.parseInt(selectedDate[2]) == date && Integer.parseInt(selectedDate[1])-1 == month && Integer.parseInt(selectedDate[0]) == year)
				{
					if(!btnDate.getTag().toString().equals(EVENT))
					{
						rlEachDate.setBackgroundResource(R.drawable.calender_cell_bg_hoveroutline);
					}
					else if(btnDate.getTag().toString().equals(EVENT))
					{
						rlEachDate.setBackgroundResource(R.drawable.datebg);
					}
				}
			}
		}
		else
		{
			btnDate.setTextColor(Color.DKGRAY);
		}
		
		btnDate.setOnClickListener(new OnClickListener()
		{
		
			public void onClick(View btnDate) 
			{
				if(prevView == null)
				{
					prevBackground = btnDate.getBackground();
					prevView = btnDate;
				}
				prevView.setBackgroundDrawable(prevBackground);
				
				prevView = btnDate;
				prevBackground = btnDate.getBackground();
				if(!btnDate.getTag().toString().equals(EVENT))
				{
					for(int j=0;j<=days;j++)
					{
						RelativeLayout rl=new RelativeLayout(context);
						rl=(RelativeLayout)findViewById(j);
						if(rl!=null && j==btnDate.getId())
							rl.setBackgroundResource(R.drawable.calender_cell_bg_hoveroutline);
						else if(rl!=null && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == j && Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.YEAR) == year && !rl.getTag().toString().equals(EVENT))
							rl.setBackgroundResource(R.drawable.bluebg);
						else if(rl!=null)
							rl.setBackgroundResource(R.drawable.calender_cell_bg);
					}
				}
				else
				{
					for(int j=0;j<=days;j++)
					{
						RelativeLayout rl=new RelativeLayout(context);
						rl=(RelativeLayout)findViewById(j);
						if(rl!=null)
						{
							if(j==btnDate.getId())
								rl.setBackgroundResource(R.drawable.datebg);
							else if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == j && Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.YEAR) == year && !rl.getTag().toString().equals(EVENT))
								rl.setBackgroundResource(R.drawable.bluebg);
							else
								rl.setBackgroundResource(R.drawable.calender_cell_bg);
						}
					}
				}
				
				selDay = btnDate.getId();	
				int monthLocal=month+1;
				if(month<9  && selDay<10)
				{
					strPalenderSelecteddate=year+"-0"+monthLocal+"-0"+selDay;
				}
				else if(month<9)
				{
					strPalenderSelecteddate=year+"-0"+monthLocal+"-"+selDay;
				}
				else if(selDay<10)
				{
					strPalenderSelecteddate=year+"-"+monthLocal+"-0"+selDay;
				}
				else
				{
					strPalenderSelecteddate=year+"-"+monthLocal+"-"+selDay;
				}
				vctEventdates=new ArrayList<EventDates>();
				vctEventdates.clear();
				vctEventdates			= eventBL.getdatesOfUserAttendingEventsCalView(strAttendingEvents,strPalenderSelecteddate,Palendar.categoryId,Palendar.strSearchKeyWord);
				if(vctEventdates!=null)
				{
					//TODO: show list view
//					Log.v(LOGTAG, "set adapter in palendar list size = "+vctEventdates.size());
//					Palendar.lvEvents.setVisibility(View.VISIBLE);
//					Palendar.lvEvents.setAdapter(objCustomPalEventsAdapter=new CustomPalEventsAdapter(vctEventdates));
					currenntListener.showList();
				}
				else
				{
					toast.show();
				}
				listener.daySelected(selDay, month, year);						
			}
		});
		rlEachDate.addView(btnDate,new LayoutParams(46,44));
		if(eventCount!=0)
		{
			tvNoOfEvents.setText(""+eventCount);
			rlEachDate.addView(tvNoOfEvents,new LayoutParams(15,15));
		}
		return rlEachDate;
	}
	
	public LinearLayout makeBusinessPalendar(int businessId)
	{
		this.businessId=businessId;
		Calendar cal = new GregorianCalendar(year, month, 1);
		days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		Calendar cal1 = new GregorianCalendar(year, month-1, 1);
		preDays = cal1.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		this.setOrientation(1);
		
		for(int date = 1;date <= days;)
		{
			int count = 0,j=1; 
			LinearLayout llHorizontal = new LinearLayout(context);
			for(int i=1; i < dayOfWeek; i++)
			{
				count++;
				llHorizontal.addView(addButtonWithOutDate((preDays - (dayOfWeek-1)+i)), new LayoutParams(46,44));
			}
			for(int i = count; i < 7; i++, count++,eventCount=0)
			{				
				if(date > days)
					break;
				llHorizontal.addView(addButtonWithDateInBusinessPalendar(date++), new LayoutParams(46,44));
			}
			for(int i = count; i < 7; i++)
			{
				
				llHorizontal.addView(addButtonWithOutDate(j++), new LayoutParams(46,44));
			}
			dayOfWeek = 0;
			this.addView(llHorizontal);
		}
		return this;
	}
	
	public RelativeLayout addButtonWithDateInBusinessPalendar(int date)
	{
		RelativeLayout rlEachDate=new RelativeLayout(context);
		TextView tvNoOfEvents=new TextView(context);
		tvNoOfEvents.setText("");
		tvNoOfEvents.setTextColor(Color.WHITE);
		tvNoOfEvents.setTextSize(10);
		tvNoOfEvents.setGravity(Gravity.CENTER);
		tvNoOfEvents.setBackgroundResource(R.drawable.alert_bg);
		tvNoOfEvents.setPadding(3, 0, 0, 0);
		rlEachDate.setId(date);
		rlEachDate.setBackgroundResource(R.drawable.calender_cell_bg);
		rlEachDate.setPadding(2, 2, 2, 2);
		Button btnDate = new Button(context);
		
		btnDate.setId(date);
		btnDate.setText(date+"");  //For prototype I commented this
		btnDate.setTextSize(16);
		btnDate.setGravity(Gravity.TOP|Gravity.RIGHT);
		btnDate.setPadding(0, 0, 7, 0);
		btnDate.setTypeface(Typeface.DEFAULT_BOLD);
		btnDate.setBackgroundColor(Color.TRANSPARENT);
		btnDate.setTag(NO_EVENT);
		rlEachDate.setTag(NO_EVENT);
		vctEventdates.clear();
		strAttendingEvents="";
		vctEventdates=eventBL.getEventsByBusiness(businessId,BusinessPalender.strSearchKeyWord,"",BusinessPalender.categoryId);
		if(vctEventdates.size()!=0)
		{
		 for(int i=0;i<vctEventdates.size();i++)
			  {
				  String str2array=vctEventdates.get(i).strEventStartdate;
			      String[] arr=str2array.split("-");
			      if(arr.length==3)
				  if(date == Integer.parseInt(arr[2]) && month+1 == Integer.parseInt(arr[1]) && year == Integer.parseInt(arr[0]))	
				  {
					  btnDate.setTag(EVENT);
					  rlEachDate.setTag(EVENT);
					  if(!vctEventdates.get(i).eventImage.equals(""))
					  {
						  if(!objDrawableManager.fetchDrawableOnThreadButton(AppConstants.IMAGE_HOST_URL+vctEventdates.get(i).eventImage, btnDate, 44, 44)){
							  btnDate.setBackgroundResource(R.drawable.no_image_smal_event);
						  }
					  }
					  else{
						  btnDate.setBackgroundResource(R.drawable.no_image_smal_event);
					  }
					  eventCount++;
				  }
				  
				else
				{
					
					btnDate.setText(date+"");
				}
			  }
		}
		else
		{
			btnDate.setText(date+"");
		}
		if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == date && Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.YEAR) == year)
		{
			btnDate.setTextColor(Color.rgb(0,136,194));
			if(!btnDate.getTag().toString().equals(EVENT))
				rlEachDate.setBackgroundResource(R.drawable.bluebg);
			if(!strSelectedDate.equals("friends palender"))
			{
				String selectedDate[]=strSelectedDate.split("-");
				if(selectedDate.length==3)
				{
					if(Integer.parseInt(selectedDate[2]) == date && Integer.parseInt(selectedDate[1])-1 == month && Integer.parseInt(selectedDate[0]) == year)
					{
						if(!btnDate.getTag().toString().equals(EVENT))
						{
							rlEachDate.setBackgroundResource(R.drawable.calender_cell_bg_hoveroutline);
						}
						else if(btnDate.getTag().toString().equals(EVENT))
						{
							rlEachDate.setBackgroundResource(R.drawable.datebg);
						}
					}
				}
			}
			else
			{
				int monthLocal=month+1;
				if(month<9  && date<10)
				{
					strSelectedDate=year+"-0"+monthLocal+"-0"+date;
				}
				else if(month<9)
				{
					strSelectedDate=year+"-0"+monthLocal+"-"+date;
				}
				else if(date<10)
				{
					strSelectedDate=year+"-"+monthLocal+"-0"+date;
				}
				else
				{
					strSelectedDate=year+"-"+monthLocal+"-"+date;
				}
			}
		}
		else if(!strSelectedDate.equals("friends palender"))
		{
			String selectedDate[]=strSelectedDate.split("-");
			if(selectedDate.length==3)
			{
				if(Integer.parseInt(selectedDate[2]) == date && Integer.parseInt(selectedDate[1])-1 == month && Integer.parseInt(selectedDate[0]) == year)
				{
					if(!btnDate.getTag().toString().equals(EVENT))
					{
						rlEachDate.setBackgroundResource(R.drawable.calender_cell_bg_hoveroutline);
					}
					else if(btnDate.getTag().toString().equals(EVENT))
					{
						rlEachDate.setBackgroundResource(R.drawable.datebg);
					}
				}
			}
		}
		else
		{
			btnDate.setTextColor(Color.DKGRAY);
		}
		
		btnDate.setOnClickListener(new OnClickListener()
		{
		
			public void onClick(View btnDate) 
			{
				if(prevView == null)
				{
					prevBackground = btnDate.getBackground();
					prevView = btnDate;
				}
				prevView.setBackgroundDrawable(prevBackground);
				
				prevView = btnDate;
				prevBackground = btnDate.getBackground();
				
				if(!btnDate.getTag().toString().equals(EVENT))
				{
					for(int j=0;j<=days;j++)
					{
						RelativeLayout rl=new RelativeLayout(context);
						rl=(RelativeLayout)findViewById(j);
						if(rl!=null && j==btnDate.getId())
							rl.setBackgroundResource(R.drawable.calender_cell_bg_hoveroutline);
						else if(rl!=null && Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == j && Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.YEAR) == year && !rl.getTag().toString().equals(EVENT))
							rl.setBackgroundResource(R.drawable.bluebg);
						else if(rl!=null)
							rl.setBackgroundResource(R.drawable.calender_cell_bg);
					}
				}
				else
				{
					for(int j=0;j<=days;j++)
					{
						RelativeLayout rl=new RelativeLayout(context);
						rl=(RelativeLayout)findViewById(j);
						if(rl!=null)
						{
							if(j==btnDate.getId())
								rl.setBackgroundResource(R.drawable.datebg);
							else if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == j && Calendar.getInstance().get(Calendar.MONTH) == month && Calendar.getInstance().get(Calendar.YEAR) == year && !rl.getTag().toString().equals(EVENT))
								rl.setBackgroundResource(R.drawable.bluebg);
							else
								rl.setBackgroundResource(R.drawable.calender_cell_bg);
						}
					}
				}
				selDay = btnDate.getId();	
				int monthLocal=month+1;
				if(month<9  && selDay<10)
				{
					strSelectedDate=year+"-0"+monthLocal+"-0"+selDay;
				}
				else if(month<9)
				{
					strSelectedDate=year+"-0"+monthLocal+"-"+selDay;
				}
				else if(selDay<10)
				{
					strSelectedDate=year+"-"+monthLocal+"-0"+selDay;
				}
				else
				{
					strSelectedDate=year+"-"+monthLocal+"-"+selDay;
				}
				vctEventdates=new ArrayList<EventDates>();
				vctEventdates.clear();
				vctEventdates			= eventBL.getEventsByBusiness(businessId,BusinessPalender.strSearchKeyWord,strSelectedDate,BusinessPalender.categoryId);
				if(vctEventdates!=null)
				{
					//TODO: show list of events
//					BusinessPalender.lvEvents.setVisibility(View.VISIBLE);
//					BusinessPalender.lvEvents.setAdapter(new CustomFriendPalEventsAdapter(vctEventdates));
					currenntListener.showList();
				}
				else
				{
					toast.show();
				}
				listener.daySelected(selDay, month, year);						
			}
		});
		rlEachDate.addView(btnDate,new LayoutParams(46,44));
		if(eventCount!=0)
		{
			tvNoOfEvents.setText(""+eventCount);
			rlEachDate.addView(tvNoOfEvents,new LayoutParams(15,15));
		}
		return rlEachDate;
	}

	public Button addButtonWithOutDate(int prevDate)
	{
		Button btnEmptyDate = new Button(context);
		btnEmptyDate.setTextSize(16);
		btnEmptyDate.setPadding(0, 0, 7, 0);
		btnEmptyDate.setTextColor(Color.GRAY);
		btnEmptyDate.setText(prevDate+"");
		btnEmptyDate.setGravity(Gravity.RIGHT | Gravity.TOP);
		btnEmptyDate.setTypeface(Typeface.DEFAULT_BOLD);
		btnEmptyDate.setBackgroundResource(R.drawable.calender_cell_bg);
		return btnEmptyDate;
	}
	
	public static String getSelectedDate()
	{
		return strDate; 
	}	
	
	public class CustomCalEventsAdapter extends BaseAdapter 
	{
		private List<EventsDetails> vecMyAdapterEvent;
		public CustomCalEventsAdapter(List<EventsDetails> vecMyAdapterEvent)
		{
			this.vecMyAdapterEvent = vecMyAdapterEvent;
		}
		
		@Override
		public int getCount()
		{
			if(vecMyAdapterEvent != null)
				return vecMyAdapterEvent.size();
			
			return 0;
		}
		
		private void refresh(List<EventsDetails> vecMyAdapterEvent)
		{
			this.vecMyAdapterEvent = vecMyAdapterEvent;
			this.notifyDataSetChanged();
		}
		
		@Override
		public Object getItem(int position)
		{
			return position;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			objEvent = vecMyAdapterEvent.get(position);
			LinearLayout llEventsOnDate=new LinearLayout(getContext());
			TextView eventName=new TextView(getContext());
			eventName.setTextColor(Color.BLACK);
			eventName.setTextSize(18);
			llEventsOnDate.setId(objEvent.eventid);
			ImageView eventImage=new ImageView(getContext());
			if(!objEvent.image.equals("no image"))
			{
				if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEvent.image, eventImage, 90, 95,objEvent.imageId,objEvent.isImageUpdated)){
					eventImage.setImageResource(R.drawable.no_image_smal_event);
				}
			}
			else
			{
				eventImage.setImageResource(R.drawable.no_image_smal_event);
			}
			
			eventName.setText(objEvent.eventname);
			llEventsOnDate.addView(eventImage, new LayoutParams(40,35));
			llEventsOnDate.addView(eventName, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			//tvCalEventName.setText(objEvent.EventName);
			return llEventsOnDate;
		}
	}
	
	public class CustomPalEventsAdapter extends BaseAdapter 
	{
		private List<EventDates> vecMyAdapterEvent;
		public CustomPalEventsAdapter(List<EventDates> vecMyAdapterEvent)
		{
			this.vecMyAdapterEvent = vecMyAdapterEvent;
		}
		
		@Override
		public int getCount()
		{
			if(vecMyAdapterEvent != null)
				return vecMyAdapterEvent.size();
			
			return 0;
		}
		
//		private void refresh(Vector<EventDates> vecMyAdapterEvent)
//		{
//			this.vecMyAdapterEvent = vecMyAdapterEvent;
//			this.notifyDataSetChanged();
//		}
		
		@Override
		public Object getItem(int position)
		{
			return position;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			objEventDates = vecMyAdapterEvent.get(position);
			RelativeLayout llEventsOnDate=new RelativeLayout(getContext());
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
			        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
			        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

			TextView eventName=new TextView(getContext());
			eventName.setTextColor(Color.BLACK);
			eventName.setTextSize(18);
			llEventsOnDate.setId(objEventDates.eventId);
			ImageView eventImage=new ImageView(getContext());
			eventImage.setId(objEventDates.businessId);
			ImageView btnDelete=new ImageView(getContext());
			btnDelete.setBackgroundResource(R.drawable.delete);
			if(!objEventDates.eventImage.equals(""))
			{
				if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEventDates.eventImage, eventImage, 90, 95,objEventDates.imageId,objEventDates.isImageUpdated)){
					eventImage.setImageResource(R.drawable.no_image_smal_event);
				}
			}
			else
			{
				eventImage.setImageResource(R.drawable.no_image_smal_event);
			}
			
			eventName.setText(objEventDates.eventName);
			lp.addRule(RelativeLayout.RIGHT_OF, eventImage.getId());
			llEventsOnDate.addView(eventImage, new LayoutParams(40,35));
			llEventsOnDate.addView(eventName,lp);
			lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp2.addRule(RelativeLayout.CENTER_VERTICAL);
			llEventsOnDate.addView(btnDelete,lp2);
			btnDelete.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					Palendar.btnDelete.performClick();
					userAddedEventId=objEventDates.userAddedEventId;
					eventId= objEventDates.eventId;
				}
			});
			return llEventsOnDate;
		}
	}
	public class CustomFriendPalEventsAdapter extends BaseAdapter 
	{
		private List<EventDates> vecMyAdapterEvent;
		public CustomFriendPalEventsAdapter(List<EventDates> vecMyAdapterEvent)
		{
			this.vecMyAdapterEvent = vecMyAdapterEvent;
		}
		
		@Override
		public int getCount()
		{
			if(vecMyAdapterEvent != null)
				return vecMyAdapterEvent.size();
			
			return 0;
		}
		
//		private void refresh(Vector<EventDates> vecMyAdapterEvent)
//		{
//			this.vecMyAdapterEvent = vecMyAdapterEvent;
//			this.notifyDataSetChanged();
//		}
		
		@Override
		public Object getItem(int position)
		{
			return position;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			objEventDates = vecMyAdapterEvent.get(position);
			RelativeLayout llEventsOnDate=new RelativeLayout(getContext());
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
			        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
			        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

			TextView eventName=new TextView(getContext());
			eventName.setTextColor(Color.BLACK);
			eventName.setTextSize(18);
			llEventsOnDate.setId(objEventDates.eventId);
			ImageView eventImage=new ImageView(getContext());
			eventImage.setId(objEventDates.businessId);
			if(!objEventDates.eventImage.equals("no image"))
			{
				if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEventDates.eventImage, eventImage, 90, 95,objEventDates.imageId,objEventDates.isImageUpdated)){
					eventImage.setImageResource(R.drawable.no_image_smal_event);
				}
			}
			else
			{
				eventImage.setImageResource(R.drawable.no_image_smal_event);
			}
			
			eventName.setText(objEventDates.eventName);
			lp.addRule(RelativeLayout.RIGHT_OF, eventImage.getId());
			llEventsOnDate.addView(eventImage, new LayoutParams(40,35));
			llEventsOnDate.addView(eventName,lp);
			lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp2.addRule(RelativeLayout.CENTER_VERTICAL);
			return llEventsOnDate;
		}
	}
	
}
