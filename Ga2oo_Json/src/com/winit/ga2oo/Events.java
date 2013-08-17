package com.winit.ga2oo;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.winit.ga2oo.businesslayer.EventsBusinessLayer;
import com.winit.ga2oo.businesslayer.FriendsBusinessLayer;
import com.winit.ga2oo.businesslayer.UserAccountBusinessLayer;
import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.common.DialogUtility;
import com.winit.ga2oo.common.Tools;
import com.winit.ga2oo.controls.CalendarListener;
import com.winit.ga2oo.controls.CalendarView;
import com.winit.ga2oo.controls.PopUpDailog;
import com.winit.ga2oo.objects.Category;
import com.winit.ga2oo.objects.EventsDetails;
import com.winit.ga2oo.objects.UserFriend;
import com.winit.ga2oo.xmlparsers.EventFilterParser;
import com.winit.go2oo.jsonparsers.EventsWrapper;
import com.winit.parsing.net.HttpHelper;
import com.winit.parsing.net.JsonHttpHelper;

public class Events extends ShareEvent implements CalendarListener
{
	private static final String CURRENT_EVENT_CATEGORY = "CurrentEventCategory";
	public static final String LOGTAG ="EventsScreen";
	public static final String ISCALBTNCLICKED = "isCalBtnClicked";
	public static final String EVENTS = "Events";
	public static final String TRUE = "true";
	public static final String DATEFORMAT1 = "MMM dd, yyyy";
	public static final String DATEFORMAT2 = "yyy-MM-dd";
	public static final String EVENTID = "EventID";
	public static final String EVENTLIST = "EventsList";
	public static final String POSITION = "Position";
	public static final String EVENTDETAILSVIEWPAGER = "EventDetailsViewPager";
	public static final String USERFRIENDID = "userFriendId";
	public static final String FRIENDSPROFILE = "FriendsProfile";
	
	public static Boolean showCalendar = false;
	private LinearLayout llCalView, llListView, llMonthAndWeekDays, llPrevious, llNext, llDaysOfMonth, llCatg;
	private Button btnPrevious, btnNext;
	private TextView tvMonth,tvDistance, tvType, tvDateInList, tvAll;
	public  ListView lvFriendsList;
	public  PullToRefreshListView lvEventsList;
//	public static ListView lvEvents;
	private ImageView ivCalIcon,ivSearch;
	private ProgressDialog progressDialog;
	private EditText etQuicksearchByLocation,etQuicksearchBykey;
	
	private int downX, moveX,day, month, year,selDistanceId = 0, selTypeId = 0,categoryId=0;
	public static int selectedCategoryId;
	public static boolean isCalBtnClicked = true;
	public static boolean isFirstTime = true;
	private String friendImageUrl,strEventsInCategory="",strSelectedDate="";
	public static String strSearchKeyWordForCalView="";
	
	private Calendar currentCal;
	private PopUpDailog customDialog;
	private PopupWindow mPopupWindow =null;
	private CustomEventsAdapter adapterList;
	private EventsBusinessLayer eventsBL;
	public  EventsDetails objUpEvent;
	private FriendsBusinessLayer friendBL;
	private Context context;
	private UserAccountBusinessLayer userActBL;
	private DrawableManager drawManager;
	private HttpHelper helper;
	public static List<EventsDetails> vctAllEvenetsList,vctEventList;
	public List<UserFriend> userFriends,vecFriends;
	public List<Category> vctCategoryList;
	private List<String> vctEmails;
	private String selecteDate[];
	private String to[],toAllFriend[];
	private String strEventName;
	private String strEventDetail="";
	private String strEventImage="";
	
	private JsonElement element;
	private JsonHttpHelper jsonHelper;
	private Calendar cal;
	private SharedPreferences sharedPreferences;
	
	private static final String kLogTag = Events.class.getSimpleName();
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
    }
	/*
    *end of onCreate method
    **/
	
	@Override
	public void onResume()
	{
		super.onResume();
		setContentView(R.layout.events);
		sharedPreferences = getPreferences(MODE_PRIVATE);
		categoryId = sharedPreferences.getInt(CURRENT_EVENT_CATEGORY, 0);
		context=this;
		isCalBtnClicked = this.getIntent().getBooleanExtra(ISCALBTNCLICKED, true);
		jsonHelper = JsonHttpHelper.getInstance();
		AppConstants.vctFilteredEvents = new ArrayList<EventsDetails>();
		
		if(progressDialog!=null && progressDialog.isShowing())
	    	progressDialog.dismiss();
		tvAll = new TextView(Events.this);
		tvAll.setText(getResources().getString(R.string.all));
		tvAll.setGravity(Gravity.CENTER_HORIZONTAL);
		tvAll.setPadding(7, 0, 7, 0);
		tvAll.setTextColor(Color.WHITE);
		
		progressDialog 	= new ProgressDialog(getParent());
		progressDialog.setMessage(getResources().getString(R.string.loading));
//		progressDialog.show();
		vctEmails		=  new ArrayList<String>();
		userActBL		= new UserAccountBusinessLayer();
		drawManager 	= new DrawableManager();
		friendBL		= new FriendsBusinessLayer();
		
		llCalView 		= (LinearLayout) findViewById(R.id.llCalView);
		llListView 		= (LinearLayout) findViewById(R.id.llListView);
		
	    llMonthAndWeekDays = (LinearLayout) findViewById(R.id.llMonthAndWeekDays);
		llDaysOfMonth 	= (LinearLayout) findViewById(R.id.llDaysOfMonth);
//		lvEvents 		= (ListView) findViewById(R.id.lvEvents);
		
		lvEventsList 	= (PullToRefreshListView) findViewById(R.id.lvEventsList);
		tvDateInList 	= (TextView) findViewById(R.id.tvDateInList);
		llCatg 			= (LinearLayout) findViewById(R.id.llCatg);
		ivSearch		= (ImageView)findViewById(R.id.ivSearch);
		
		lvEventsList.setCacheColorHint(0);
		lvEventsList.setFadingEdgeLength(0);
		
		vctAllEvenetsList	= new ArrayList<EventsDetails>();
		eventsBL			= new EventsBusinessLayer();
		vctAllEvenetsList	= eventsBL.getAllEventsList();
		
		eventsBL			= new EventsBusinessLayer();
		vctCategoryList		= new ArrayList<Category>();
		vctCategoryList		= eventsBL.getAllCategories();
//		progressDialog.show();
		llCatg.addView(tvAll,new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
						
		tvAll.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				categoryId=0;
				progressDialog.show();
				new Thread(new  Runnable()
				{
					@Override
					public void run() 
					{
						System.gc();
						AppConstants.vctFilteredEvents = eventsBL.getAllEventsInCategoryOnDate(strEventsInCategory,strSelectedDate);
						runOnUiThread(new Runnable()
						{
							public void run() 
							{
//								strSearchKeyWordForCalView="";
//								selectedCategoryId=0;
//								if(adapterList!=null)
//									if(AppConstants.vctFilteredEvents!=null)
//										if(AppConstants.vctFilteredEvents.size()!=0)
//											adapterList.refresh(AppConstants.vctFilteredEvents);
//												
//								if(CalendarView.vctEventList!=null){
//									CalendarView.vctEventList.clear();
//								}else{
//									CalendarView.vctEventList=new ArrayList<EventsDetails>();
//								}					
//								CalendarView.vctEventList=eventsBL.getAllEventsList();
//								if(isCalBtnClicked){
//									buildFilteredCalView();
//								}
								strSearchKeyWordForCalView="";
								selectedCategoryId=0;
								if(adapterList!=null)
									if(AppConstants.vctFilteredEvents!=null)
										if(AppConstants.vctFilteredEvents.size()!=0)
										{
											lvEventsList.setVisibility(View.VISIBLE);
											adapterList.refresh(AppConstants.vctFilteredEvents);
										}
										else
										{
											lvEventsList.setVisibility(View.GONE);
										}
												
								if(CalendarView.vctEventList!=null)
									CalendarView.vctEventList.clear();
								else
									CalendarView.vctEventList=new Vector<EventsDetails>();
													
								CalendarView.vctEventList=eventsBL.getAllEventsList();
								if(isCalBtnClicked)
									buildFilteredCalView();
							}
						});
						progressDialog.dismiss();
					}
				}).start();
				
				for(int i=0; i < llCatg.getChildCount(); i++)
				{
					llCatg.getChildAt(i).setBackgroundResource(0);
				}
				v.setBackgroundResource(R.drawable.catg_hover);
			}
		});
			
		if(vctCategoryList.size()!=0)
		{
			for(int i=0; i<vctCategoryList.size(); i++)
			{
				TextView tvCatg = new TextView(Events.this);
				tvCatg.setText(vctCategoryList.get(i).maincategoryname);
				tvCatg.setId(vctCategoryList.get(i).maincategory);
				tvCatg.setGravity(Gravity.CENTER_HORIZONTAL);
				tvCatg.setPadding(7, 0, 7, 0);
				tvCatg.setTextColor(Color.WHITE);
				
				llCatg.addView(tvCatg, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				
				tvCatg.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View v) 
					{
						strSearchKeyWordForCalView="";
						vctAllEvenetsList.clear();
						categoryId=v.getId();
						selectedCategoryId=categoryId;
						new LoadEventsListByCategory().execute();
				
						for(int i=0; i < llCatg.getChildCount(); i++)
						{
							llCatg.getChildAt(i).setBackgroundResource(0);
						}
						v.setBackgroundResource(R.drawable.catg_hover);
					}
				});
			}
		}
//		llCatg.getChildAt(0).setBackgroundResource(R.drawable.catg_hover);
						
		ivCalIcon = (ImageView) findViewById(R.id.ivCalIcon);
		ivCalIcon.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				selecteDate=strSelectedDate.split("-");
				DatePickerDialog dailog = new DatePickerDialog(Events.this.getParent(), new OnDateSetListener()
				{											
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
					{
						cal= Calendar.getInstance();
						cal.set(year, monthOfYear, dayOfMonth);
						new LoadEventsListByDate().execute();
					}
				},  Integer.parseInt(selecteDate[0]), Integer.parseInt(selecteDate[1])-1, Integer.parseInt(selecteDate[2]));
				dailog.setTitle("");
				dailog.show();
			}
		});
		
		
		tvDistance = (TextView) findViewById(R.id.tvDistance);
		tvDistance.setTag(""+selDistanceId);
		final String[] strArrDistances = {getResources().getString(R.string.walking),
				getResources().getString(R.string.drinking),getResources().getString(R.string.flying)};
		tvDistance.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
				builder.setTitle("");
				builder.setSingleChoiceItems(strArrDistances,selDistanceId, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						tvDistance.setText(strArrDistances[which]);
						tvDistance.setTag(""+which);
						selDistanceId = which;
						dialog.dismiss();
					}
				});
				AlertDialog dialog = builder.create();
								dialog.show();
			}
		});
				
		tvType = (TextView) findViewById(R.id.tvType);
		tvType.setTag(""+selTypeId);
		final String[] strArrTypes = {getResources().getString(R.string.top_hits),getResources().getString(R.string.hits)};
		
		tvType.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(getParent());
				builder.setTitle("");
				builder.setSingleChoiceItems(strArrTypes,selTypeId, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						tvType.setText(strArrTypes[which]);
						tvType.setTag(""+which);
						selTypeId = which;
						dialog.dismiss();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
				if(!dialog.isShowing())
				{
					new LoadEventsByType().execute();
				}
			}
		});
		
		if(isFirstTime)
		{
			isFirstTime = false;
			buildCalView();
			buildListView();
		}
		
//		if(isCalBtnClicked)
//		{
////			TabsActivity.btnCalView.performClick();
//			llCalView.setVisibility(View.VISIBLE);
//			llListView.setVisibility(View.GONE);	
//		}
//		else
//		{
//			isCalBtnClicked=false;
//			llCalView.setVisibility(View.GONE);
//			llListView.setVisibility(View.VISIBLE);	
//		}
		
		ivSearch.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showQuickSearchPopup();
			}
		});
		
						
		TabsActivity.btnBack.setVisibility(View.GONE);
		TabsActivity.llEventButtons.setVisibility(View.VISIBLE);
		
		if(isCalBtnClicked)
		{
			TabsActivity.btnCalView.setBackgroundResource(R.drawable.btn_calview_hover);
			TabsActivity.btnListView.setBackgroundResource(R.drawable.btn_listview);
			isCalBtnClicked=true;
			llDaysOfMonth.removeAllViews();
			buildCalendarViewWithSavedCategory();
			llCalView.setVisibility(View.VISIBLE);
			llListView.setVisibility(View.GONE);
		}
		else
		{
			isCalBtnClicked=false;
			TabsActivity.btnCalView.setBackgroundResource(R.drawable.btn_calview);
			TabsActivity.btnListView.setBackgroundResource(R.drawable.btn_listview_hover);
			
			llCalView.setVisibility(View.GONE);
			llListView.setVisibility(View.VISIBLE);	
			progressDialog.show(); 
			buildListViewWithSavedCategory();
			if(adapterList != null)
				adapterList.refresh(vctAllEvenetsList);
					//			adapterList.notifyDataSetChanged();
		}
						
		TabsActivity.btnCalView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				TabsActivity.btnCalView.setBackgroundResource(R.drawable.btn_calview_hover);
				TabsActivity.btnListView.setBackgroundResource(R.drawable.btn_listview);
				isCalBtnClicked=true;
				llDaysOfMonth.removeAllViews();
				buildCalendarViewWithSavedCategory();
				llCalView.setVisibility(View.VISIBLE);
				llListView.setVisibility(View.GONE);	
			}
		});
		
		TabsActivity.btnListView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
//				TabsActivity.btnCalView.setBackgroundResource(R.drawable.btn_calview);
//				TabsActivity.btnListView.setBackgroundResource(R.drawable.btn_listview_hover);
//				isCalBtnClicked=false;
//				buildListView();
//				tvAll.performClick();
//				adapterList.notifyDataSetChanged();
//				
//				llCalView.setVisibility(View.GONE);
//				llListView.setVisibility(View.VISIBLE);	
				//TODO
				isCalBtnClicked= true;
				Intent inttentEventDetails = new Intent(Events.this, Events.class);
				inttentEventDetails.putExtra(ISCALBTNCLICKED, false);
				TabGroupActivity pActivity = (TabGroupActivity)Events.this.getParent();
				pActivity.startChildActivity(EVENTS, inttentEventDetails);
			}
		});
		TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar);
		TabsActivity.tvTitle.setVisibility(View.GONE);
		TabsActivity.btnLogout.setVisibility(View.VISIBLE);
		TabsActivity.btnLogout.setBackgroundResource(R.drawable.logout_btn);
		
		TabsActivity.btnLogout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				progressDialog.setMessage(getResources().getString(R.string.logout));
				progressDialog.show();
				new Thread(new  Runnable()
				{
					@Override
					public void run() 
					{
						//userActBL.clearData();
						runOnUiThread(new Runnable()
						{
							public void run() 
							{
								Intent intent = new Intent(Events.this, Login.class);
								startActivity(intent);
								TabsActivity.tabActivity.finish();
							}
						});
						progressDialog.dismiss();
					}
				}).start();
			}
		});
	}
	protected void buildListViewWithSavedCategory() {
		if(categoryId!=0){
			new LoadEventsListByCategory().execute();
			llCatg.getChildAt(0).setBackgroundResource(0);
			for(int i=1; i < llCatg.getChildCount(); i++)
			{
				if(llCatg.getChildAt(i).getId()!=categoryId){
					llCatg.getChildAt(i).setBackgroundResource(0);
				}else{

					llCatg.getChildAt(i).setBackgroundResource(R.drawable.catg_hover);
				}
			}
		}else{
			llCatg.getChildAt(0).setBackgroundResource(R.drawable.catg_hover);
			buildListView();
		}
	}

	protected void buildCalendarViewWithSavedCategory() {
		if(categoryId!=0){
			vctAllEvenetsList.clear();
			llCatg.getChildAt(0).setBackgroundResource(0);
			for(int i=1; i < llCatg.getChildCount(); i++)
			{
				if(llCatg.getChildAt(i).getId()!=categoryId){
					llCatg.getChildAt(i).setBackgroundResource(0);
				}else{
					
					llCatg.getChildAt(i).setBackgroundResource(R.drawable.catg_hover);
					new LoadEventsListByCategory().execute();
				}
			}
		}
		else{
			buildCalView();
			llCatg.getChildAt(0).setBackgroundResource(R.drawable.catg_hover);
		}

	}
	
	
	private class LoadEventsListByCategory extends AsyncTask<Void,Void,Boolean>{
		
		@Override
		protected void onPreExecute() {
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			EventFilterParser objEventFilterParser=new EventFilterParser();
			helper=new HttpHelper();

				if(!isCalBtnClicked)
				{
					Log.i(LOGTAG, "Loading events list by category and selected date...");
					try {
						element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_FILTER+"category="+categoryId+"&startdate="+strSelectedDate.replace("-", ""));
					} catch (Exception e) {
						Log.e(LOGTAG, "Error in Loading events list by category and selected date!");
						e.printStackTrace();
						return false;
					}
					AppConstants.vctFilteredEvents.clear();
					Object eventsObjects = jsonHelper.parse(element, EventsWrapper.class);
					if(eventsObjects!=null){
						for(int i=0;i<((EventsWrapper) eventsObjects).getEvents().size();i++){
							AppConstants.vctFilteredEvents.add(((EventsWrapper)eventsObjects).getEvents().get(i));
						}
					}
					Log.i(LOGTAG, "Loading events list by category and selected date successfully completed.");
				}
				else
				{
					Log.i(LOGTAG, "Loading events list by category...");
					try {
						element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_FILTER+"category="+categoryId);
					} catch (Exception e) {
						Log.e(LOGTAG, "Error in Loading events list by category!");
						e.printStackTrace();
						return false;
					}
					AppConstants.vctFilteredEvents.clear();
					Object eventsObjects = jsonHelper.parse(element, EventsWrapper.class);
					if(eventsObjects!=null){
						for(int i=0;i<((EventsWrapper) eventsObjects).getEvents().size();i++){
							if(((EventsWrapper) eventsObjects).getEvents().get(i).images!=null){
								for(int j=0;j<((EventsWrapper)eventsObjects).getEvents().get(i).images.size();j++){
									if(TRUE.equals(((EventsWrapper)eventsObjects).getEvents().get(i).images.get(j).mainimage)){
										((EventsWrapper)eventsObjects).getEvents().get(i).image = ((EventsWrapper)eventsObjects).getEvents().get(i).images.get(j).imagesrc;
									}
								}
							}else{
								((EventsWrapper)eventsObjects).getEvents().get(i).image=AppConstants.NOIMAGE;
							}
							AppConstants.vctFilteredEvents.add(((EventsWrapper)eventsObjects).getEvents().get(i));
						}
						Log.i(LOGTAG, "Loading events list by category successfully completed.");
					}
				}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			if(!result){
				
				DialogUtility.showConnectionErrorDialog(Events.this.getParent());
			}

			if(AppConstants.vctFilteredEvents!=null && AppConstants.vctFilteredEvents.size()!=0)
			{
				if(adapterList!=null)
					adapterList.refresh(AppConstants.vctFilteredEvents);
//				tvNoEventInList.setVisibility(View.GONE);
				lvEventsList.setVisibility(View.VISIBLE);
				lvEventsList.onRefreshComplete();
				if(CalendarView.vctEventList!=null)
					CalendarView.vctEventList.clear();
				CalendarView.vctEventList=(ArrayList<EventsDetails>) AppConstants.vctFilteredEvents.clone();
			}
			else
			{
				lvEventsList.setVisibility(View.GONE);
//				tvNoEventInList.setVisibility(View.VISIBLE);
				if(CalendarView.vctEventList!=null)
					CalendarView.vctEventList.clear();	
			}
			if(isCalBtnClicked)
				buildFilteredCalView();
			super.onPostExecute(result);
		}
		
	}
	
	private class LoadEventsListByDate extends AsyncTask<Void,Void,Boolean>{

		String dateFormat;
		String dateFormat1;
		
		@Override
		protected void onPreExecute() {
			progressDialog.show();
			dateFormat=(String) DateFormat.format(DATEFORMAT1, cal);
			dateFormat1=(String) DateFormat.format(DATEFORMAT2, cal);
			strSelectedDate=dateFormat1;
			CalendarView.strDate=dateFormat1;
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			EventFilterParser objEventFilterParser=new EventFilterParser();
			helper=new HttpHelper();
				if(selectedCategoryId!=0){
					Log.i(LOGTAG, "Loading events list by category and start date...");
					try {
						element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_FILTER+"category="+categoryId+"&startdate="+dateFormat1.replace("-", ""));
					} catch (Exception e) {
						Log.e(LOGTAG, "Error in Loading events list by category and start date!");
						e.printStackTrace();
						return false;
					}
					AppConstants.vctFilteredEvents.clear();
					Object eventsObjects = jsonHelper.parse(element, EventsWrapper.class);
					if(eventsObjects!=null){
						for(int i=0;i<((EventsWrapper) eventsObjects).getEvents().size();i++){
							AppConstants.vctFilteredEvents.add(((EventsWrapper)eventsObjects).getEvents().get(i));
						}
					}
					Log.i(LOGTAG, "Loading events list by category and start date successfully completed.");
				}
				else{
					Log.i(LOGTAG, "Loading events list by start date...");
					try {
						element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_FILTER+"startdate="+dateFormat1.replace("-", ""));
					} catch (Exception e) {
						Log.e(LOGTAG, "Error in Loading events list by start date!");
						e.printStackTrace();
						return false;
					}
					AppConstants.vctFilteredEvents.clear();
					Object eventsObjects = jsonHelper.parse(element, EventsWrapper.class);
					if(eventsObjects!=null){
						for(int i=0;i<((EventsWrapper) eventsObjects).getEvents().size();i++){
							AppConstants.vctFilteredEvents.add(((EventsWrapper)eventsObjects).getEvents().get(i));
						}
					}
					Log.i(LOGTAG, "Loading events list by start date successfully completed.");		
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				progressDialog.dismiss();
				DialogUtility.showConnectionErrorDialog(Events.this.getParent());
			}
				tvDateInList.setText(dateFormat.substring(0, dateFormat.indexOf(",")));	
				if(AppConstants.vctFilteredEvents!=null)
					adapterList.refresh(AppConstants.vctFilteredEvents);
				strSelectedDate=dateFormat1;
				progressDialog.dismiss();
			super.onPostExecute(result);
		}		
	}

	private class LoadEventsByType extends AsyncTask<Void,Void,Boolean>{
		
		@Override
		protected void onPreExecute() {
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			EventFilterParser objEventFilterParser=new EventFilterParser();
			helper=new HttpHelper();

				if(selectedCategoryId!=0 && tvType.getText().equals(getResources().getString(R.string.top_hits))){
					Log.i(LOGTAG, "Loading events list by category and start date with sorting...");
					try {
						element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_FILTER+"category="+categoryId+"&startdate="+strSelectedDate.replace("-", "")+"&sortby="+tvType.getText().toString().toLowerCase().replace(" ", ""));
					} catch (Exception e) {
						Log.e(LOGTAG, "Error in loading events list by category and start date with sorting!");
						e.printStackTrace();
						return false;
					}
					AppConstants.vctFilteredEvents.clear();
					Object eventsObjects = jsonHelper.parse(element, EventsWrapper.class);
					if(eventsObjects!=null){
						for(int i=0;i<((EventsWrapper) eventsObjects).getEvents().size();i++){
							AppConstants.vctFilteredEvents.add(((EventsWrapper)eventsObjects).getEvents().get(i));
						}
						Log.i(LOGTAG, "Loading events list by category and start date with sorting successfully completed.");
					}
				}	
				else{
					Log.i(LOGTAG, "Loading events list by start date.");
					try {
						element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_FILTER+"startdate="+strSelectedDate.replace("-", ""));
					} catch (Exception e) {
						Log.e(LOGTAG, "Error in loading events list by start date successfully completed.");
						e.printStackTrace();
						return false;
					}
					AppConstants.vctFilteredEvents.clear();
					Object eventsObjects = jsonHelper.parse(element, EventsWrapper.class);
					if(eventsObjects!=null){
						for(int i=0;i<((EventsWrapper) eventsObjects).getEvents().size();i++){
							AppConstants.vctFilteredEvents.add(((EventsWrapper)eventsObjects).getEvents().get(i));
						}
						Log.i(LOGTAG, "Loading events list by start date successfully completed.");
					}
				}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			
			if(result){
				if(AppConstants.vctFilteredEvents!=null){
					adapterList.refresh(AppConstants.vctFilteredEvents);
					progressDialog.dismiss();
				}
			}else{
				progressDialog.dismiss();
				DialogUtility.showConnectionErrorDialog(Events.this.getParent());
			}

			super.onPostExecute(result);
		}
		
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if(KeyEvent.KEYCODE_SEARCH == keyCode){
//			showQuickSearchPopup();
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	public void showQuickSearchPopup()
	{
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.quick_search_popup, null);
		popup.setBackgroundColor(Color.TRANSPARENT);
		etQuicksearchBykey	 		 		 = (EditText)popup.findViewById(R.id.etQuicksearchBykey);
		etQuicksearchByLocation	 			 = (EditText)popup.findViewById(R.id.etQuicksearchByLocation);
		Button btnClose				 		 = (Button)popup.findViewById(R.id.btnClose);
		Button btnSearchByKeyword 	 		 = (Button)popup.findViewById(R.id.btnSearchByKeyword);
		Button btnSearchByLocation			 = (Button)popup.findViewById(R.id.btnSearchByLocation);
		RelativeLayout rlCross				 = (RelativeLayout)popup.findViewById(R.id.rlCross);
		btnClose.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
			}
		});
		rlCross.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
			}
		});
		
		btnSearchByKeyword.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				String searchKeyword;
				searchKeyword=etQuicksearchBykey.getText().toString();
				if(!isCalBtnClicked)
					vctAllEvenetsList=eventsBL.searchEvent(searchKeyword,strSelectedDate,selectedCategoryId);
				else if(isCalBtnClicked)
				{
					strSearchKeyWordForCalView=etQuicksearchBykey.getText().toString();
					if(CalendarView.vctEventList!=null)
						CalendarView.vctEventList.clear();
					else
						CalendarView.vctEventList=new ArrayList<EventsDetails>();
					CalendarView.vctEventList=eventsBL.searchEventforCalView(searchKeyword, selectedCategoryId);
					buildFilteredCalView();
				}
				adapterList.refresh(vctAllEvenetsList);
				customDialog.dismiss();
			}
		});

		btnSearchByLocation.setOnClickListener(new OnClickListener()//currently same as search by keyword
		{					
			@Override
			public void onClick(View v) 
			{
				String searchKeyword;
				searchKeyword=etQuicksearchByLocation.getText().toString();
				if(!isCalBtnClicked)
					vctAllEvenetsList=eventsBL.searchEventByLocation(searchKeyword,strSelectedDate,selectedCategoryId);
				else if(isCalBtnClicked)
				{
					strSearchKeyWordForCalView=etQuicksearchBykey.getText().toString();
					if(CalendarView.vctEventList!=null)
						CalendarView.vctEventList.clear();
					else
						CalendarView.vctEventList=new ArrayList<EventsDetails>();
					CalendarView.vctEventList=eventsBL.searchEventByLocation(searchKeyword,"", selectedCategoryId);
					buildFilteredCalView();
				}
				adapterList.refresh(vctAllEvenetsList);
				customDialog.dismiss();
			}
		});
		customDialog = new PopUpDailog(getParent(), popup, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		customDialog.show();
		
		if(showCalendar){
			TabsActivity.btnCalView.performClick();
			showCalendar=false;
		}
	}

	
	/*
	 * this method buildCalView() is used to display events in calendar view
	 */
	public void buildCalView()
	{
		CalendarView.vctEventList=new ArrayList<EventsDetails>();
		//strSearchKeyWordForCalView="";
		if(categoryId==0){
			CalendarView.vctEventList=eventsBL.searchEventforCalView("", selectedCategoryId);
		}
		if(llMonthAndWeekDays != null && llMonthAndWeekDays.getChildCount() > 0)
			llMonthAndWeekDays.removeAllViews();
		LinearLayout llCalHead = (LinearLayout) getLayoutInflater().inflate(R.layout.calendar_top_days, null);
		llMonthAndWeekDays.addView(llCalHead, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
    	btnPrevious 	= (Button) llCalHead.findViewById(R.id.btnPrevious);
        llPrevious 		= (LinearLayout)llCalHead.findViewById(R.id.llPrevious);
        btnNext 		= (Button) llCalHead.findViewById(R.id.btnNext);
        llNext 			= (LinearLayout)llCalHead.findViewById(R.id.llNext);
        tvMonth 		= (TextView) llCalHead.findViewById(R.id.tvMonth);   
        
        tvMonth.setOnTouchListener(new OnTouchListener()
        {	
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{ 
					downX =(int) event.getX();
				}
				else if(event.getAction() == MotionEvent.ACTION_UP)
				{
					moveX =(int) event.getX();
					
					if(downX-moveX > 10)
					{
						btnNext.dispatchTouchEvent(event);
					}
					else if(downX-moveX < -10)
					{
						btnPrevious.dispatchTouchEvent(event);
					}
				}
				return true;
			}
		});
        
    	llPrevious.setOnTouchListener(new OnTouchListener()
        {	
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				if(event.getAction() == 1)
					btnPrevious.dispatchTouchEvent(event);
				return true;
			}
		});
        
        btnPrevious.setOnTouchListener(new OnTouchListener()
        {
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				progressDialog.show();
				if(event.getAction() == 1)
				{
					month -= 1;
					if(month < 0)
					{
						month = 11;
						year -= 1;
					}
					
					if(llDaysOfMonth.getChildCount() > 0)
						llDaysOfMonth.removeAllViews();
					
					tvMonth.setText(Tools.getMonthFromNumber(month+1,Events.this)+" "+year);
					new Thread(new  Runnable()
					{
						@Override
						public void run() 
						{
							final CalendarView calView = new CalendarView(Events.this, month, year,Events.this);
							runOnUiThread(new Runnable()
							{
								public void run() 
								{
									CalendarView.strDate=year+"-"+(month+1)+"-"+"1";
									llDaysOfMonth.addView(calView.makeCalendar());
								}
							});
							progressDialog.dismiss();
						}
					}).start();
					
//					lvEvents.setVisibility(View.GONE);
				}
				return false;
			}
        });
        
        llNext.setOnTouchListener(new OnTouchListener()
        {	
        	@Override
        	public boolean onTouch(View v, MotionEvent event) 
			{
        		if(event.getAction() == 1)
        			btnNext.dispatchTouchEvent(event);
				return true;
			}
		});
        
        btnNext.setOnTouchListener(new OnTouchListener()
        {	
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{			
				progressDialog.show();
				if(event.getAction() == 1)
				{
					month += 1;
					if(month >= 12)
					{
						month = 0;
						year += 1;
					}
					
					if(llDaysOfMonth.getChildCount() > 0)
						llDaysOfMonth.removeAllViews();
					
					tvMonth.setText(Tools.getMonthFromNumber(month+1,Events.this)+" "+year);
					tvMonth.invalidate();
					new Thread(new  Runnable()
			    	{
						@Override
						public void run() 
						{
							final CalendarView calView = new CalendarView(Events.this, month, year,Events.this);
							runOnUiThread(new Runnable()
							{
								public void run() 
								{
									CalendarView.strDate=year+"-"+(month+1)+"-"+"1";
									llDaysOfMonth.addView(calView.makeCalendar());
								}
							});
							progressDialog.dismiss();
						}
					}).start();
//					lvEvents.setVisibility(View.GONE);
				}
				return false;
			}
		});
        
        currentCal 	= Calendar.getInstance();
        if(CalendarView.strDate.equals(""))
        {
        	day = currentCal.get(Calendar.DAY_OF_MONTH);
        	month = currentCal.get(Calendar.MONTH);
        	year = currentCal.get(Calendar.YEAR);
        }
        else
        {
        	selecteDate=CalendarView.strDate.split("-");
        	if(selecteDate.length==3)
        	{
        		day = Integer.parseInt(selecteDate[2]);
            	month = Integer.parseInt(selecteDate[1])-1;
            	year = Integer.parseInt(selecteDate[0]);
        	}
        }

        tvMonth.setText(Tools.getMonthFromNumber(month+1,Events.this)+" "+year);       
        new Thread(new  Runnable()
   	 	{
        	@Override
			public void run() 
			{
        		final CalendarView calView = new CalendarView(Events.this, month, year,Events.this);
				
				runOnUiThread(new Runnable()
				{
					public void run() 
					{
						llDaysOfMonth.addView(calView.makeCalendar());
					}
				});

			}
   	 	}).start();
	}
	/*
	 * end of method buildCalView()
	 **/


	public void buildFilteredCalView()
	{
//		progressDialog.show();
	    
	    
		if(llDaysOfMonth.getChildCount() > 0)
			llDaysOfMonth.removeAllViews();
			
		tvMonth.setText(Tools.getMonthFromNumber(month+1,Events.this)+" "+year);
		new Thread(new  Runnable()
		{
			@Override
			public void run() 
			{
				final CalendarView calView = new CalendarView(Events.this, month, year, Events.this);
				runOnUiThread(new Runnable()
				{
					public void run() 
					{
							llDaysOfMonth.addView(calView.makeCalendar());
//							calView.makeCalenderList();
					}
				});
	//				progressDialog.dismiss();
			}
		}).start();
			
//		lvEvents.setVisibility(View.GONE);
	}
	
	/* 
	 * method buildListView is used to display events in ListView when we click on ListView icon
	 */
	public void buildListView()
	{
		strSelectedDate=CalendarView.getSelectedDate();
		if(!strSelectedDate.equals(""))
		{
			selecteDate	=strSelectedDate.split("-");
			String date	=	new DateFormatSymbols().getMonths()[Integer.parseInt(selecteDate[1])-1];
			tvDateInList.setText(date.substring(0, 3)+" "+selecteDate[2]);
		}
		for(int i=1; i < llCatg.getChildCount(); i++)
		{
			llCatg.getChildAt(i).setBackgroundResource(0);
		}
		llCatg.getChildAt(0).setBackgroundResource(R.drawable.catg_hover);
		vctAllEvenetsList.clear();
		selectedCategoryId=categoryId;
		Log.v(LOGTAG, "build list view categoryId = "+categoryId);
		strEventsInCategory=""; 
		vctAllEvenetsList=eventsBL.getAllEventsInCategoryOnDate(strEventsInCategory,strSelectedDate);
		lvEventsList.setAdapter(adapterList = new CustomEventsAdapter(vctAllEvenetsList));
//		adapterList.refresh(vctAllEvenetsList);
		
		lvEventsList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				if(categoryId!=0){
				new LoadEventsListByCategory().execute();
				}else{
					//TODO: load all list
					new RefreshEvents().execute();		
				}
					
			}
					
		});
	}
	/* 
	 * end of method buildListView
	 **/
	
	private class RefreshEvents extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			Log.v(LOGTAG, "eventn selected date = "+strSelectedDate);
			EventsBusinessLayer evbl=new EventsBusinessLayer();
			
//			evbl.deleteEventByDate(strSelectedDate);
			List<EventsDetails> listEvents=new ArrayList<EventsDetails>();
//			evbl.deleteFromAttending();
			String tempDate = strSelectedDate.replace("-", "");
			Log.i(LOGTAG, "Loading events list filtered by start date.");
			try {
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENTFILTER+tempDate);
			} catch (Exception e) {
				Log.e(LOGTAG, "Error in loading events list filtered by start date!");
				e.printStackTrace();
				return false;
			}
			Object eventsObjects = jsonHelper.parse(element, EventsWrapper.class);
			if(eventsObjects!=null){
				for(int i=0;i<((EventsWrapper) eventsObjects).getEvents().size();i++){
					EventsDetails tmpObject = ((EventsWrapper)eventsObjects).getEvents().get(i);
					listEvents.add(tmpObject);
					evbl.InsertEventList(((EventsWrapper)eventsObjects).getEvents().get(i));
				}
				Log.i(LOGTAG, "Loading events list filtered by start date successfully completed.");
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(!result){
				DialogUtility.showConnectionErrorDialog(Events.this.getParent());
			}
				vctAllEvenetsList.clear();
				vctAllEvenetsList=eventsBL.getAllEventsInCategoryOnDate(strEventsInCategory,strSelectedDate);
				lvEventsList.setAdapter(adapterList = new CustomEventsAdapter(vctAllEvenetsList));
				adapterList.refresh(vctAllEvenetsList);
				lvEventsList.onRefreshComplete();
			
			super.onPostExecute(result);
		}
	}
	
	/* 
	 * start of inner class CustomEventsAdapter used by buildListView method
	 */
	public class CustomEventsAdapter extends BaseAdapter 
	{
		private List<EventsDetails> vecUpcomingEvent = null;
		
		public CustomEventsAdapter(List<EventsDetails> vecUpcomingEvent2) 
		{

			this.vecUpcomingEvent = vecUpcomingEvent2;
		}
		
		@Override
		public int getCount()
		{
			return (vecUpcomingEvent != null?vecUpcomingEvent.size():0);
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

		public void refresh(List<EventsDetails> vecUpcomingEvent)
		{
			this.vecUpcomingEvent = vecUpcomingEvent;
			this.notifyDataSetChanged();
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			objUpEvent = vecUpcomingEvent.get(position);
			final int eventId=objUpEvent.eventid;//this event id is used to id of views;
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.list_events_cell, null);
			convertView.setId(eventId);
			
			ImageView ivEventIcon 				= (ImageView) convertView.findViewById(R.id.ivEventIcon);
			ProgressBar eventicon_progress_bar	= (ProgressBar)convertView.findViewById(R.id.eventicon_progress_bar);
			TextView tvUpName 					= (TextView) convertView.findViewById(R.id.tvUpName);
			TextView tvStartDate 				= (TextView) convertView.findViewById(R.id.tvStartdate);
			final LinearLayout llFavourite 			= (LinearLayout) convertView.findViewById(R.id.llStar);
			LinearLayout llShare 				= (LinearLayout) convertView.findViewById(R.id.llShare);
			ImageView ivFriendOne 				= (ImageView) convertView.findViewById(R.id.ivFriendOne);
        	ProgressBar progressBar				= (ProgressBar)convertView.findViewById(R.id.event_progress_bar);
			TextView tvNoOfFriends 				= (TextView) convertView.findViewById(R.id.tvNoOfFriends);
			llShare.setId(eventId);
			
			userFriends		= new ArrayList<UserFriend>();
		    userFriends		= friendBL.getUsrFrendAttendingEvents(Integer.toString(objUpEvent.eventid));
		    
		    if(objUpEvent.image!=null){
				if(!objUpEvent.image.equals(AppConstants.NOIMAGE))
				{
					Log.v(LOGTAG, "image id = "+objUpEvent.imageId);
//					if(!drawManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objUpEvent.image, ivEventIcon,70,70)){
						if(!drawManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objUpEvent.image, ivEventIcon,50,50,objUpEvent.imageId,objUpEvent.isImageUpdated)){
						eventicon_progress_bar.setVisibility(View.GONE);
						ivEventIcon.setBackgroundResource(R.drawable.no_image_event);
					}else{
						eventicon_progress_bar.setVisibility(View.GONE);
						strEventImage=AppConstants.IMAGE_HOST_URL+objUpEvent.image;
					}
				}
				else
				{
					eventicon_progress_bar.setVisibility(View.GONE);
					ivEventIcon.setBackgroundResource(R.drawable.no_image_event);
				}
		    }
		    else{
					eventicon_progress_bar.setVisibility(View.GONE);
					ivEventIcon.setBackgroundResource(R.drawable.no_image_event);
		    }
			if(userFriends.size()!=0)
			{

				friendImageUrl=AppConstants.IMAGE_HOST_URL+userFriends.get(0).Image1;
				progressBar.setVisibility(View.GONE);
				drawManager.fetchDrawableOnThread(friendImageUrl, ivFriendOne,20,20,userFriends.get(0).image_id,userFriends.get(0).isImageUpdated);	
			    if(userFriends.get(0).noOfFriends==1)
			    	tvNoOfFriends.setText(userFriends.get(0).noOfFriends+" "+getResources().getString(R.string.friend));
	            else if(userFriends.get(0).noOfFriends>1)
	            	tvNoOfFriends.setText(userFriends.get(0).noOfFriends+" "+getResources().getString(R.string.friends));
			    tvNoOfFriends.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						showFrindsPopup(eventId);
					}
				});
			}	
			else
			{
				progressBar.setVisibility(View.GONE);
				tvNoOfFriends.setText(getResources().getString(R.string.no_friend_yet));
				ivFriendOne.setVisibility(View.GONE);
			}
			
			tvUpName.setText(objUpEvent.eventname);
			tvStartDate.setText(objUpEvent.event_start_date+"  "+objUpEvent.event_start_time.toString().substring(0, 5)+"  "+objUpEvent.price);
			strEventName=objUpEvent.eventname.toString();
			strEventDetail=getResources().getString(R.string.start_date)+objUpEvent.event_start_date.toString()+
					getResources().getString(R.string.start_time)+objUpEvent.event_start_time.toString()+
					getResources().getString(R.string.event_price)+objUpEvent.price;
			llFavourite.setTag(objUpEvent.EventBusiness);
			llFavourite.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					showFavouritePopup(eventId,(Integer) llFavourite.getTag());
				}
			});
			
			llShare.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					showSharePopup(getApplicationContext(),v.getId(),strEventName,strEventDetail);
				}
			});
			
			convertView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
//					Intent inttentEventDetails = new Intent(Events.this, EventDetails.class);
					Intent inttentEventDetails = new Intent(Events.this, EventDetailsViewPager.class);
					inttentEventDetails.putExtra(EVENTID,v.getId());
					AppConstants.vctFilteredEvents = eventsBL.getAllEventsInCategoryOnDate(strEventsInCategory,strSelectedDate);
					inttentEventDetails.putExtra(EVENTLIST, AppConstants.vctFilteredEvents);
					inttentEventDetails.putExtra(POSITION, position);
					TabGroupActivity pActivity = (TabGroupActivity)Events.this.getParent();
//					pActivity.startChildActivity("EventDetails", inttentEventDetails);
					pActivity.startChildActivity(EVENTDETAILSVIEWPAGER, inttentEventDetails);
				}
			});
			
			return convertView;
		}
	}/* 
	 * end of inner class CustomEventsAdapter
	 **/
	
	
	public void showFrindsPopup(int eventId)
	{
		LayoutInflater inflater = getLayoutInflater();
		RelativeLayout popup=(RelativeLayout) inflater.inflate(R.layout.friends_popup, null);
		
		ImageView ivClose = (ImageView)popup.findViewById(R.id.ivClose);
		LinearLayout llFriendsList = (LinearLayout)popup.findViewById(R.id.llFriendsList);
		
		ivClose.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
			}
		});
		vecFriends = new ArrayList<UserFriend>();
		vecFriends=friendBL.getUsrFrendAttendingEvents(Integer.toString(eventId));
		if(vecFriends.size()!=0)
		{
			for(int i=0; i<vecFriends.size(); i++)
			{
				UserFriend friend = vecFriends.get(i);
				final int friendId=friend.ID1;
				LinearLayout llFriendItem = (LinearLayout) inflater.inflate(R.layout.friends_list_cell, null);
				llFriendsList.addView(llFriendItem);
				llFriendItem.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						Intent intent = new Intent(Events.this,FriendsProfile.class);
						intent.putExtra(USERFRIENDID,friendId);
						
						TabGroupActivity pActivity = (TabGroupActivity)Events.this.getParent();
						pActivity.startChildActivity(FRIENDSPROFILE, intent);
						
						customDialog.dismiss();
					}
				});
				
				ImageView ivFriendsListIcon = (ImageView) llFriendItem.findViewById(R.id.ivFriendsListIcon);
				TextView tvFriendsListName = (TextView) llFriendItem.findViewById(R.id.tvFriendsListName);
				ProgressBar progressBar=(ProgressBar)llFriendItem.findViewById(R.id.friend_list_progress_bar);
				String friendImageUrl=AppConstants.IMAGE_HOST_URL+friend.Image1;
				if(!friend.Image1.equals(AppConstants.NOIMAGE))
				{
					if(!drawManager.fetchDrawableOnThread(friendImageUrl, ivFriendsListIcon,50,50,friend.image_id,friend.isImageUpdated)){
						ivFriendsListIcon.setImageResource(R.drawable.no_image_smal_event);
					}
					progressBar.setVisibility(View.GONE);
				}
				else
				{
					progressBar.setVisibility(View.GONE);
					ivFriendsListIcon.setImageResource(R.drawable.no_image_smal_event);
				}
				tvFriendsListName.setText(friend.FirstName1+" "+friend.LastName1);
			}
		}
		customDialog = new PopUpDailog(getParent(), popup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}
	
	@Override
	protected void onPause()
	{
		saveCurrentCategory();
		TabsActivity.llEventButtons.setVisibility(View.GONE);
		llCalView.removeAllViews();
		llListView.removeAllViews();
		isFirstTime=true;
		if(adapterList != null)
			adapterList.refresh(new ArrayList<EventsDetails>());
		if(drawManager != null)
			drawManager.clear();
		super.onPause();
	}

	@Override
	protected void onStop() 
	{
		saveCurrentCategory();
		if(adapterList != null)
			adapterList.refresh(new ArrayList<EventsDetails>());
		if(drawManager != null)
			drawManager.clear();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		saveCurrentCategory();
		super.onDestroy();
	}
	protected void saveCurrentCategory() {
	    SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.putInt(CURRENT_EVENT_CATEGORY, categoryId);
	    editor.commit();
	}
	
	@Override
	public void daySelected(int day, int month, int year)
	{
		
	}

	@Override
	public void showList() {
		isCalBtnClicked= true;
		Intent inttentEventDetails = new Intent(Events.this, Events.class);
		inttentEventDetails.putExtra(ISCALBTNCLICKED, false);
		TabGroupActivity pActivity = (TabGroupActivity)Events.this.getParent();
		pActivity.startChildActivity(EVENTS, inttentEventDetails);
	}

	
}
