package com.winit.ga2oo;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.winit.ga2oo.objects.EventDates;
import com.winit.ga2oo.objects.EventsDetails;
import com.winit.ga2oo.objects.FavoriteEvent;
import com.winit.ga2oo.objects.UserFriend;
import com.winit.ga2oo.xmlparsers.EventFilterParser;
import com.winit.go2oo.jsonparsers.EventsWrapper;
import com.winit.go2oo.jsonparsers.Ga2ooJsonParsers;
import com.winit.go2oo.jsonparsers.UserAccountWrapper;
import com.winit.parsing.net.HttpHelper;
import com.winit.parsing.net.JsonHttpHelper;

public class Palendar extends ShareEvent implements CalendarListener
{

	private static final String FRIENDSPROFILE = "FriendsProfile";
	private static final String USERFRIENDID = "userFriendId";
	private static final String PALENDER = "palender";
	private static final String PALENDAR = "Palendar";
	private static final String DATEFORMAT2 = "yyy-MM-dd";
	private static final String DATEFORMAT1 = "MMM dd, yyyy";
	private static final String IS_CAL_BTN_CLICKED = "isCalBtnClicked";
	public final static String LOGTAG = "PalendarScreen";
	private static final String CURREN_PALENDAR_CATEGORY = "CurrenPalendarCategory";
	
	public LinearLayout llCalView, llListView, llMonthAndWeekDays, llPrevious, llNext, llDaysOfMonth, llCatg;
	private Button btnPrevious, btnNext;
	private TextView tvDistance, tvType, tvDateInList,tvMonth;
//	public static ListView lvEvents;
	private PullToRefreshListView  lvEventsList;
	private ListView lvFriendsList;
	private ImageView ivCalIcon,ivSearch;
	private ProgressDialog progressDialog;
	private EditText etQuicksearchByLocation,etQuicksearchBykey;
	
	
	private static boolean isCalBtnClicked = true;
	private static boolean isFirstTime = true;
	private int downX, moveX, day, month, year,selDistanceId = 0, selTypeId = 0;
	private String strAttendingEvents="",strEventsInCategory="", friendImageUrl;
	public static int categoryId;
	public static String strSearchKeyWord="";
	
	private PopUpDailog customDialog;
	private PopupWindow mPopupWindow =null;
	private Calendar currentCal;
	private EventDates objEvent;
	private CustomEventsAdapter adapterList;
	private CustomCalEventsAdapter calEventAdapter;
	private EventsBusinessLayer eventsBL;
	private DrawableManager objDrawableManager;
	public  Drawable friendImageDraw;
	private FriendsBusinessLayer friendBL;
	private UserAccountBusinessLayer userActBL;
	private Context context;
	
	static public ArrayList<EventDates> vctEventdates,vctEventList;
	public List<Category> vctCategoryList;
	public static List<EventsDetails> vecUpcomingEvent,vctAllEvenetsList;
	public List<UserFriend> userFriends;
	public List<UserFriend> vecFriends;
	private List<String> vctEmails;
	private String selecteDate[];
	private String to[],toAllFriend[];
	private String strEventName;
	private String strEventDetail="";
	private String strEventImage="";
	public static Button btnDelete;
	private SharedPreferences sharedPreferences;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
    }/*
    *end of onCreate method
    **/
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		setContentView(R.layout.events);
		
		
		context=this;
		isCalBtnClicked = this.getIntent().getBooleanExtra(IS_CAL_BTN_CLICKED, true);
		sharedPreferences = getPreferences(MODE_PRIVATE);
		categoryId = sharedPreferences.getInt(CURREN_PALENDAR_CATEGORY, 0);
		if(progressDialog!=null && progressDialog.isShowing())
			progressDialog.dismiss();
		btnDelete=new Button(Palendar.this);
		TextView tvAll = new TextView(Palendar.this);
		tvAll.setText(getResources().getString(R.string.all));
		tvAll.setGravity(Gravity.CENTER_HORIZONTAL);
		tvAll.setPadding(7, 0, 7, 0);
		tvAll.setTextColor(Color.WHITE);
		vctEmails		=  new ArrayList<String>();
		progressDialog = new ProgressDialog(getParent());
		progressDialog.setMessage(getResources().getString(R.string.loading));
		
		selecteDate			= new String[3];
		llCalView 			= (LinearLayout) findViewById(R.id.llCalView);
		llListView 			= (LinearLayout) findViewById(R.id.llListView);
		
		llMonthAndWeekDays 	= (LinearLayout) findViewById(R.id.llMonthAndWeekDays);
		llDaysOfMonth 		= (LinearLayout) findViewById(R.id.llDaysOfMonth);
//		lvEvents 			= (ListView) findViewById(R.id.lvEvents);
		
		lvEventsList 		= (PullToRefreshListView) findViewById(R.id.lvEventsList);
		tvDateInList 		= (TextView) findViewById(R.id.tvDateInList);
		llCatg 				= (LinearLayout) findViewById(R.id.llCatg);
		ivSearch			= (ImageView)findViewById(R.id.ivSearch);
		
		eventsBL			= new EventsBusinessLayer();
		vctEventdates		= new ArrayList<EventDates>();
		vctEventList		= new ArrayList<EventDates>();	
		objDrawableManager	= new DrawableManager();
		vctCategoryList		= new ArrayList<Category>();
		lvEventsList.setCacheColorHint(0);
		lvEventsList.setFadingEdgeLength(0);
		
		strAttendingEvents	= eventsBL.getLogedInUserAttendingEvents(AppConstants.USER_ID);
		
		llCatg.addView(tvAll,new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		tvAll.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
//				progressDialog.show();
				vctEventdates.clear();
				categoryId=0;
				strSearchKeyWord="";
							strEventsInCategory="";
							new LoadFavoriteEvents().execute();
							vctEventdates		= eventsBL.getdatesOfUserAttendingEvents(strAttendingEvents,CalendarView.strPalenderSelecteddate);
								if(adapterList!=null)
									adapterList.refresh(vctEventdates);
								if(isCalBtnClicked)
									buildFilteredCalView();
									
				for(int i=0; i < llCatg.getChildCount(); i++)
				{
					llCatg.getChildAt(i).setBackgroundResource(0);
				}
				v.setBackgroundResource(R.drawable.catg_hover);
			}
		});
		
		vctCategoryList=eventsBL.getAllCategories();
		for(int i=0; i<vctCategoryList.size(); i++)
		{
			TextView tvCatg = new TextView(Palendar.this);
			tvCatg.setText(vctCategoryList.get(i).maincategoryname);
			tvCatg.setId(vctCategoryList.get(i).maincategory);
			tvCatg.setGravity(Gravity.CENTER_HORIZONTAL);
			tvCatg.setPadding(7, 0, 7, 0);
			tvCatg.setTextColor(Color.WHITE);
			
			llCatg.addView(tvCatg, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			tvCatg.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					progressDialog.show();
					vctEventdates.clear();
					categoryId=v.getId();
					  new Thread(new  Runnable()
				    	{
							@Override
							public void run() 
							{
								strEventsInCategory=eventsBL.getAllEventsInCategory(categoryId,strAttendingEvents);
								if(!strEventsInCategory.equals(""))
								{
									System.gc();
									vctEventdates=eventsBL.getdatesOfUserAttendingEvents(strEventsInCategory,CalendarView.strPalenderSelecteddate);
								}
								runOnUiThread(new Runnable()
								{
									public void run() 
									{
										if(adapterList!=null)
											adapterList.refresh(vctEventdates);
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
		}
		
		ivCalIcon = (ImageView) findViewById(R.id.ivCalIcon);
		ivCalIcon.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				selecteDate=CalendarView.strPalenderSelecteddate.split("-");
				DatePickerDialog dailog = new DatePickerDialog(Palendar.this.getParent(), new OnDateSetListener()
				{											
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
					{
						Calendar cal= Calendar.getInstance();
						cal.set(year, monthOfYear, dayOfMonth);
		        		String dateFormat=(String) DateFormat.format(DATEFORMAT1, cal);
		        		String dateFormat1=(String) DateFormat.format(DATEFORMAT2, cal);
		        		CalendarView.strPalenderSelecteddate=dateFormat1;
		        		tvDateInList.setText(dateFormat.substring(0, dateFormat.indexOf(",")));	
		        		vctEventdates.clear();
						vctEventdates=eventsBL.getdatesOfUserAttendingEventsCalView(strAttendingEvents,dateFormat1,Palendar.categoryId,Palendar.strSearchKeyWord);
						adapterList.refresh(vctEventdates);
					}
					
				}, Integer.parseInt(selecteDate[0]), Integer.parseInt(selecteDate[1])-1, Integer.parseInt(selecteDate[2]));
				dailog.setTitle("");
				dailog.show();
				
			}
		});
		
		tvDistance = (TextView) findViewById(R.id.tvDistance);
		tvDistance.setTag(""+selDistanceId);
		final String[] strArrDistances = {getResources().getString(R.string.walking),
				getResources().getString(R.string.driwing),getResources().getString(R.string.flying)};
		
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
			}
		});
		
		btnDelete.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				LayoutInflater inflater = getLayoutInflater();
				LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.confirmation_popup, null);
				Button btnYes = (Button)popup.findViewById(R.id.btnYes);
				Button btnNo = (Button)popup.findViewById(R.id.btnNo);
				TextView tvMessage=(TextView)popup.findViewById(R.id.tvMessage);
				tvMessage.setText(getResources().getString(R.string.sure_to_delete_event_from_palender));
				btnYes.setOnClickListener(new OnClickListener()
				{					
					@Override
					public void onClick(View v) 
					{
//						objGa2ooParsers=new Ga2ooParsers();
						UserAccountBusinessLayer userAccBL=new UserAccountBusinessLayer();
//						objGa2ooParsers.deleteUserEvent(AppConstants.USER_ID, CalendarView.userAddedEventId);
						Ga2ooJsonParsers.deleteUserEvent(AppConstants.USER_ID, CalendarView.userAddedEventId);
						userAccBL.deleteUserFavorites(AppConstants.USER_ID, CalendarView.eventId);
						strAttendingEvents	= eventsBL.getLogedInUserAttendingEvents(AppConstants.USER_ID);
						buildFilteredCalView();
						customDialog.dismiss();
					}
				});
					
				btnNo.setOnClickListener(new OnClickListener()
				{					
					@Override
					public void onClick(View v) 
					{
						customDialog.dismiss();
					}
				});
				customDialog = new PopUpDailog(getParent(), popup, 220, LayoutParams.WRAP_CONTENT);
				customDialog.show();
			}
		});
		
//		lvEvents.setOnItemClickListener(new OnItemClickListener()
//		{
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View v, int arg2,long arg3)
//			{
//				Log.i("id of clicked",""+v.getId());
//				Intent inttentEventDetails = new Intent(Palendar.this, EventDetails.class);
//				inttentEventDetails.putExtra("EventID",v.getId());
//				TabGroupActivity pActivity = (TabGroupActivity)Palendar.this.getParent();
//				pActivity.startChildActivity("EventDetails", inttentEventDetails);
//			}
//		});
		
		if(isFirstTime)
		{
			//Load favorites 
			isFirstTime = false;
			buildCalView();
			buildListView();
		}
		
//		if(isCalBtnClicked)
//		{
//			llCalView.setVisibility(View.VISIBLE);
//			llListView.setVisibility(View.GONE);	
//		}
//		else
//		{
//			llCalView.setVisibility(View.GONE);
//			llListView.setVisibility(View.VISIBLE);		
//		}
//		
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
//			buildCalView();
			buildCalendarViewWithSavedCategory();
			llCalView.setVisibility(View.VISIBLE);
			llListView.setVisibility(View.GONE);	
			if(calEventAdapter != null)
				calEventAdapter.refresh(vctEventdates);
		}
		else
		{
			TabsActivity.btnCalView.setBackgroundResource(R.drawable.btn_calview);
			TabsActivity.btnListView.setBackgroundResource(R.drawable.btn_listview_hover);
			
			llCalView.setVisibility(View.GONE);
			llListView.setVisibility(View.VISIBLE);	
			buildListViewWithSavedCategory();
//			buildListView();
			if(adapterList != null)
				adapterList.refresh(vctEventdates);
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
				buildCalView();
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
//				
//				buildListView();
//				adapterList.notifyDataSetChanged();
//				isCalBtnClicked=false;
//				llCalView.setVisibility(View.GONE);
//				llListView.setVisibility(View.VISIBLE);	
				isCalBtnClicked= true;
				Intent inttentEventDetails = new Intent(Palendar.this, Palendar.class);
				inttentEventDetails.putExtra(IS_CAL_BTN_CLICKED, false);
				TabGroupActivity pActivity = (TabGroupActivity)Palendar.this.getParent();
				pActivity.startChildActivity(PALENDAR, inttentEventDetails);
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
		    					Intent intent = new Intent(Palendar.this, Login.class);
		    		            startActivity(intent);
		    		            TabsActivity.tabActivity.finish();
							}
						});
						progressDialog.dismiss();
					}
				}).start();
			}
		});
		
		lvEventsList.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				new RefreshEvents().execute();
				
			}
			
		});
	}
	
	protected void buildListViewWithSavedCategory() {
		buildListView();
		if(categoryId!=0){
			progressDialog.show();
			new LoadEventsListByCategory().execute();
			strEventsInCategory=eventsBL.getAllEventsInCategory(categoryId,strAttendingEvents);
			vctEventdates=eventsBL.getdatesOfUserAttendingEvents(strEventsInCategory,CalendarView.strPalenderSelecteddate);
			llCatg.getChildAt(0).setBackgroundResource(0);
			if(adapterList!=null)
				adapterList.refresh(vctEventdates);
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
			
		}
	}

	protected void buildCalendarViewWithSavedCategory() {
		if(categoryId!=0){
			new LoadEventsListByCategory().execute();
			vctEventdates.clear();
			llCatg.getChildAt(0).setBackgroundResource(0);
			for(int i=1; i < llCatg.getChildCount(); i++)
			{
				if(llCatg.getChildAt(i).getId()!=categoryId){
					llCatg.getChildAt(i).setBackgroundResource(0);
				}else{

					llCatg.getChildAt(i).setBackgroundResource(R.drawable.catg_hover);
//					new LoadFavoriteEvents().execute();
				}
			}
		}
		else{
			buildCalView();
			llCatg.getChildAt(0).setBackgroundResource(R.drawable.catg_hover);
		}

	}
	
	
	private class LoadFavoriteEvents extends AsyncTask<Void,Void,Boolean>{
		
		JsonElement element;
		JsonHttpHelper jsonHelper = JsonHttpHelper.getInstance();
		EventsBusinessLayer eventBL = new EventsBusinessLayer();
		
		@Override
		protected void onPreExecute() {
			progressDialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			AppConstants.vctUserFavorites = new ArrayList<FavoriteEvent>();
//			eventBL.deleteUserFavorites();
			Log.i(LOGTAG, "Loading user events list");
			try{
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.ADD_EVENT+"id/"+AppConstants.USER_ID);
			}catch(Exception e){
				Log.e(LOGTAG, "Error in loading user events list");
				e.printStackTrace();
				return false;
			}
			Object favoriteEventsObjects = jsonHelper.parse(element, UserAccountWrapper.class);
			if(favoriteEventsObjects!=null){
				if(((UserAccountWrapper)favoriteEventsObjects).getUserAccount().events!=null){
					AppConstants.vctUserFavorites.addAll(((UserAccountWrapper)favoriteEventsObjects).getUserAccount().events);
					
					for(int i=0;i<((UserAccountWrapper)favoriteEventsObjects).getUserAccount().events.size();i++){
						eventBL.insertUserFavorites(((UserAccountWrapper)favoriteEventsObjects).getUserAccount().events.get(i));
					}
				}
			}
			Log.i(LOGTAG, "Loading user events list successfully completed.");
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			if(!result){
				DialogUtility.showConnectionErrorDialog(Palendar.this.getParent());
			}
			super.onPostExecute(result);
		}
		
	}
	
	private class RefreshEvents extends AsyncTask<Void, Void, Boolean> {

		JsonElement element;
		JsonHttpHelper jsonHelper = JsonHttpHelper.getInstance();
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			Log.v(LOGTAG, "eventn selected date = "+CalendarView.strPalenderSelecteddate);
			List<EventsDetails> listEvents=new ArrayList<EventsDetails>();
			String tempDate = CalendarView.strPalenderSelecteddate.replace("-", "");
			Log.i(LOGTAG, "Loading events list filtered by start date.");
			try {
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_FILTER+tempDate);
			} catch (Exception e) {
				Log.e(LOGTAG, "Error in oading events list filtered by start date!");
				e.printStackTrace();
				return false;
			}
			Object eventsObjects = jsonHelper.parse(element, EventsWrapper.class);
			if(eventsObjects!=null){
				for(int i=0;i<((EventsWrapper) eventsObjects).getEvents().size();i++){
					EventsDetails tmpObject = ((EventsWrapper)eventsObjects).getEvents().get(i);
					listEvents.add(tmpObject);
					for(int j=0;j<tmpObject.attending.size();j++){
						tmpObject.attending.get(j).eventID =  tmpObject.eventid;
					}
					for(int k=0;k<tmpObject.locations.size();k++){
						tmpObject.locations.get(k).EventId = tmpObject.eventid;
					}
				}
			}
			Log.i(LOGTAG, "Loading events list filtered by start date successfully completed.");
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(!result){
				DialogUtility.showConnectionErrorDialog(Palendar.this.getParent());
			}
			vctEventdates.clear();
			vctEventdates		= eventsBL.getdatesOfUserAttendingEvents(strAttendingEvents,CalendarView.strPalenderSelecteddate);
			lvEventsList.setAdapter(adapterList = new CustomEventsAdapter(vctEventdates));
			adapterList.refresh(vctEventdates);
			lvEventsList.onRefreshComplete();
			super.onPostExecute(result);
		}
	}
		
	public void showQuickSearchPopup()
	{
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.quick_search_popup, null);
		
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
				String searchKeyword="";
				searchKeyword=etQuicksearchBykey.getText().toString();
				Log.i("searchKeyword",""+searchKeyword);
				vctEventdates.clear();
				if(isCalBtnClicked)
				{
					strSearchKeyWord=etQuicksearchBykey.getText().toString();
					buildFilteredCalView();
				}
				else
				{
					vctEventdates=eventsBL.searchEventForPalender(strAttendingEvents,searchKeyword,CalendarView.strPalenderSelecteddate,categoryId);
				}
				adapterList.refresh(vctEventdates);
				customDialog.dismiss();
			}
		});

		btnSearchByLocation.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				String searchKeyword;
				searchKeyword=etQuicksearchByLocation.getText().toString();
				vctEventdates.clear();
				if(isCalBtnClicked)
				{
					strSearchKeyWord=etQuicksearchBykey.getText().toString();
					buildFilteredCalView();
				}
				else
				{
					vctEventdates=eventsBL.searchEventForPalenderByLocation(strAttendingEvents,searchKeyword,CalendarView.strPalenderSelecteddate,categoryId);
				}
				adapterList.refresh(vctEventdates);
				customDialog.dismiss();
			}
		});
		customDialog = new PopUpDailog(getParent(), popup, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		customDialog.show();
	}

	
	/*
	 * this method buildCalView() is used to display events in calendar view
	 */
	public void buildCalView()
	{
		Log.i(LOGTAG,"date selected =="+CalendarView.strPalenderSelecteddate);
		strSearchKeyWord="";
		if(llMonthAndWeekDays != null && llMonthAndWeekDays.getChildCount() > 0)
			llMonthAndWeekDays.removeAllViews();
      
		if(vctEventdates!=null)
			vctEventdates.clear();
		
        vctEventdates			= eventsBL.getdatesOfUserAttendingEvents(strAttendingEvents,"");
		LinearLayout llCalHead 	= (LinearLayout) getLayoutInflater().inflate(R.layout.calendar_top_days, null);
		llMonthAndWeekDays.addView(llCalHead, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		
    	btnPrevious 		= (Button) llCalHead.findViewById(R.id.btnPrevious);
        llPrevious 			= (LinearLayout)llCalHead.findViewById(R.id.llPrevious);
        btnNext 			= (Button) llCalHead.findViewById(R.id.btnNext);
        llNext 				= (LinearLayout)llCalHead.findViewById(R.id.llNext);
        tvMonth 			= (TextView) llCalHead.findViewById(R.id.tvMonth);   
        
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
					
					tvMonth.setText(Tools.getMonthFromNumber(month+1,Palendar.this)+" "+year);
					CalendarView.strPalenderSelecteddate=year+"-"+(month+1)+"-"+"1";
					CalendarView calView = new CalendarView(Palendar.this, month, year,Palendar.this);
					llDaysOfMonth.addView(calView.makePalendar());
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
					CalendarView.strPalenderSelecteddate=year+"-"+(month+1)+"-"+"1";
					Log.i("CalendarView.strPalenderSelecteddate",CalendarView.strPalenderSelecteddate);
					tvMonth.setText(Tools.getMonthFromNumber(month+1,Palendar.this)+" "+year);
					tvMonth.invalidate();
					CalendarView calView = new CalendarView(Palendar.this, month, year,Palendar.this);
					llDaysOfMonth.addView(calView.makePalendar());
//					lvEvents.setVisibility(View.GONE);
				}
				return false;
			}
		});
        
        currentCal = Calendar.getInstance();
        currentCal.get(Calendar.DAY_OF_MONTH);
        if(PALENDER.equals(CalendarView.strPalenderSelecteddate))
        {
        	day = currentCal.get(Calendar.DAY_OF_MONTH);
        	month = currentCal.get(Calendar.MONTH);
        	year = currentCal.get(Calendar.YEAR);
        }
        else
        {
        	selecteDate=CalendarView.strPalenderSelecteddate.split("-");
        	if(selecteDate.length==3)
        	{
        		day = Integer.parseInt(selecteDate[2]);
            	month = Integer.parseInt(selecteDate[1])-1;
            	year = Integer.parseInt(selecteDate[0]);
        	}
        }
        tvMonth.setText(Tools.getMonthFromNumber(month+1,Palendar.this)+" "+year);       
		
        CalendarView calView = new CalendarView(Palendar.this, month, year,Palendar.this);
		llDaysOfMonth.addView(calView.makePalendar());
		
	}/*
	 * end of method buildCalView()
	 **/
	
	public void buildFilteredCalView()
	{
		Log.i("Month",""+month);
		progressDialog.show();
		if(llDaysOfMonth.getChildCount() > 0)
			llDaysOfMonth.removeAllViews();
			
		tvMonth.setText(Tools.getMonthFromNumber(month+1,Palendar.this)+" "+year);
		new Thread(new  Runnable()
		{
			@Override
			public void run() 
			{
				final CalendarView calView = new CalendarView(Palendar.this, month, year,Palendar.this);
				runOnUiThread(new Runnable()
				{
					public void run() 
					{
							llDaysOfMonth.addView(calView.makePalendar());
					}
				});
					progressDialog.dismiss();
			}
		}).start();
			
//		lvEvents.setVisibility(View.GONE);
	}
	
private class LoadEventsListByCategory extends AsyncTask<Void,Void,Boolean>{
		
		private static final String TRUE = "true";

		@Override
		protected void onPreExecute() {
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			EventFilterParser objEventFilterParser=new EventFilterParser();
			HttpHelper helper=new HttpHelper();
			JsonElement element; 
			JsonHttpHelper jsonHelper = JsonHttpHelper.getInstance();
					Log.i(LOGTAG, "Loading events list by category...");
					try {
						element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_FILTER+"category="+categoryId);
					} catch (Exception e) {
						Log.e(LOGTAG, "Error in Loading events list by category!");
						e.printStackTrace();
						return false;
					}
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
							eventsBL.InsertEventList(((EventsWrapper)eventsObjects).getEvents().get(i));
						}
						Log.i(LOGTAG, "Loading events list by category successfully completed.");
					}
				
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			if(!result){
				
				DialogUtility.showConnectionErrorDialog(Palendar.this.getParent());
			}

//			if(AppConstants.vctFilteredEvents!=null && AppConstants.vctFilteredEvents.size()!=0)
//			{
//				if(adapterList!=null)
//					adapterList.refresh(AppConstants.vctFilteredEvents);
////				tvNoEventInList.setVisibility(View.GONE);
//				lvEventsList.setVisibility(View.VISIBLE);
//				lvEventsList.onRefreshComplete();
//				if(CalendarView.vctEventList!=null)
//					CalendarView.vctEventList.clear();
//				CalendarView.vctEventList=(ArrayList<EventsDetails>) AppConstants.vctFilteredEvents.clone();
//			}
//			else
//			{
//				lvEventsList.setVisibility(View.GONE);
////				tvNoEventInList.setVisibility(View.VISIBLE);
//				if(CalendarView.vctEventList!=null)
//					CalendarView.vctEventList.clear();	
//			}
//			if(isCalBtnClicked)
//				buildFilteredCalView();
//			progressDialog.dismiss();
			super.onPostExecute(result);
		}
		
	}
	
	/* 
	 * inner class CustomCalEventsAdapter used by ListView
	 */
	public class CustomCalEventsAdapter extends BaseAdapter 
	{
		private static final String EVENT_DETAILS = "EventDetails";
		private static final String EVENT_ID = "EventID";
		private List<EventDates> vecMyAdapterEvent;
		
		public CustomCalEventsAdapter(List<EventDates> vecMyAdapterEvent)
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

		@Override
		public Object getItem(int position)
		{
			return position;
		}
		private void refresh(List<EventDates> vecMyAdapterEvent)
		{
			this.vecMyAdapterEvent = vecMyAdapterEvent;
			this.notifyDataSetChanged();
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
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.event_cal_cell, null);
			ImageView ivCalEventIcon = (ImageView) convertView.findViewById(R.id.ivCalEventIcon);
			TextView tvCalEventName = (TextView) convertView.findViewById(R.id.tvCalEventName);
			ProgressBar calEventProgressBar=(ProgressBar)convertView.findViewById(R.id.calEventProgressBar);
			final int eventId=objEvent.eventId;
			convertView.setId(eventId);
			if(!objEvent.eventImage.equals(AppConstants.NOIMAGE))
			{
				if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEvent.eventImage, ivCalEventIcon, 30,30,objEvent.imageId,objEvent.isImageUpdated)){
					calEventProgressBar.setVisibility(View.GONE);
					ivCalEventIcon.setImageResource(R.drawable.no_image_smal_event);
				}
			}
			else
			{
				calEventProgressBar.setVisibility(View.GONE);
				ivCalEventIcon.setImageResource(R.drawable.no_image_smal_event);
			}
			tvCalEventName.setText(objEvent.eventName);
		   //  }
		     
			convertView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					Intent inttentEventDetails = new Intent(Palendar.this, EventDetails.class);
					inttentEventDetails.putExtra(EVENT_ID, v.getId());
					TabGroupActivity pActivity = (TabGroupActivity)Palendar.this.getParent();
					pActivity.startChildActivity(EVENT_DETAILS, inttentEventDetails);
				}
			});
			
			return convertView;
		}
	}/* 
	 * end of inner class CustomCalEventsAdapter
	 **/
	
	
	/* 
	 * method buildListView is used to display events in ListView when we click on ListView icon
	 */
	public void buildListView()
	{
		if(!CalendarView.strPalenderSelecteddate.equals(""))
		{
			Log.i("CalendarView.strPalenderSelecteddate",CalendarView.strPalenderSelecteddate);
			String dateArr[]=CalendarView.strPalenderSelecteddate.split("-");
			String date=	new DateFormatSymbols().getMonths()[Integer.parseInt(dateArr[1])-1];
			tvDateInList.setText(date.substring(0, 3)+" "+dateArr[2]);
		}
		for(int i=1; i < llCatg.getChildCount(); i++)
		{
			llCatg.getChildAt(i).setBackgroundResource(0);
		}
		llCatg.getChildAt(0).setBackgroundResource(R.drawable.catg_hover);
		vctEventdates.clear();
		vctEventdates		= eventsBL.getdatesOfUserAttendingEvents(strAttendingEvents,CalendarView.strPalenderSelecteddate);
		lvEventsList.setAdapter(adapterList = new CustomEventsAdapter(vctEventdates));
	}/* 
	 * end of method buildListView
	 **/
	
	
	
	/* 
	 * start of inner class CustomEventsAdapter used by buildListView method
	 */
	public class CustomEventsAdapter extends BaseAdapter 
	{
		private static final String EVENT_DETAILS_VIEW_PAGER = "EventDetailsViewPager";
		private static final String POSITION = "Position";
		private static final String EVENTS_LIST = "EventsList";
		private static final String EVENT_ID = "EventID";
		private List<EventDates> vecUpcomingEvent = null;
		
		public CustomEventsAdapter(List<EventDates> vecUpcomingEvent2) 
		{
			this.vecUpcomingEvent = vecUpcomingEvent2;
		}
		
		@Override
		public int getCount()
		{
			if(vecUpcomingEvent != null)
				return vecUpcomingEvent.size();
			return 0;
		}

		private void refresh(List<EventDates> vecUpcomingEvent)
		{
			this.vecUpcomingEvent = vecUpcomingEvent;
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
			objEvent = vecUpcomingEvent.get(position);
			final int eventId=objEvent.eventId;//this event id is used to id of views;
				convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.list_events_cell, null);

			convertView.setId(eventId);
			ImageView ivEventIcon = (ImageView) convertView.findViewById(R.id.ivEventIcon);
			TextView tvUpName = (TextView) convertView.findViewById(R.id.tvUpName);
			TextView tvStartDate = (TextView) convertView.findViewById(R.id.tvStartdate);
			TextView tvStartTime = (TextView) convertView.findViewById(R.id.tvStartTime);
			TextView tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
        	ImageView ivFriendOne = (ImageView) convertView.findViewById(R.id.ivFriendOne);
        	ProgressBar eventicon_progress_bar=(ProgressBar)convertView.findViewById(R.id.eventicon_progress_bar);
			ProgressBar progressBar=(ProgressBar)convertView.findViewById(R.id.event_progress_bar);
        	friendBL=new FriendsBusinessLayer();
			userFriends=new ArrayList<UserFriend>();
		    userFriends=friendBL.getUsrFrendAttendingEvents(Integer.toString(objEvent.eventId));
			final LinearLayout llFavourite 			= (LinearLayout) convertView.findViewById(R.id.llStar);
			LinearLayout llShare 				= (LinearLayout) convertView.findViewById(R.id.llShare);
			llShare.setId(eventId);
			TextView tvNoOfFriends = (TextView) convertView.findViewById(R.id.tvNoOfFriends);
			if(!objEvent.eventImage.equals(AppConstants.NOIMAGE))
			{
				if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEvent.eventImage, ivEventIcon,65,65,objEvent.imageId,objEvent.isImageUpdated)){
					eventicon_progress_bar.setVisibility(View.GONE);
					ivEventIcon.setBackgroundResource(R.drawable.no_image_event);
				}else{
					eventicon_progress_bar.setVisibility(View.GONE);
					strEventImage=AppConstants.IMAGE_HOST_URL+objEvent.eventImage;
				}
			}
			else
			{
				eventicon_progress_bar.setVisibility(View.GONE);
				ivEventIcon.setBackgroundResource(R.drawable.no_image_event);
			}
			if(userFriends.size()!=0)
			{
				friendImageUrl=AppConstants.IMAGE_HOST_URL+userFriends.get(0).Image1;
				objDrawableManager.fetchDrawableOnThread(friendImageUrl, ivFriendOne,50,50);	
				if(userFriends.get(0).noOfFriends==1){
					tvNoOfFriends.setText(userFriends.get(0).noOfFriends+" "+getResources().getString(R.string.friend));
				}
				else{
					tvNoOfFriends.setText(userFriends.get(0).noOfFriends+" "+getResources().getString(R.string.friends));
				}
				tvNoOfFriends.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
//						if(userFriends.get(0).noOfFriends!=0)
							showFrindsPopup(eventId);
					}
				});
			}
			else
			{
				progressBar.setVisibility(View.GONE);
				tvNoOfFriends.setText(" "+getResources().getString(R.string.no_friend_yet));
				ivFriendOne.setVisibility(View.GONE);
			}
			tvUpName.setText(objEvent.eventName);
			tvStartDate.setText(objEvent.strEventStartdate);
			
			//if(objEvent.strEventStartTime.toString().length()>5)
			tvStartTime.setText(objEvent.strEventStartTime+"  "+objEvent.strEventPrice);
			//else
			//	tvStartTime.setText(objEvent.strEventStartTime.toString()+"  "+objEvent.strEventPrice);
			//tvPrice.setText(objEvent.strEventPrice);
			strEventName=objEvent.eventName.toString();	
			strEventDetail=getResources().getString(R.string.start_date)+objEvent.strEventStartdate.toString()+
					getResources().getString(R.string.start_time)+objEvent.strEventStartTime.toString()+
					getResources().getString(R.string.event_price)+objEvent.strEventPrice;
			
			llFavourite.setTag(objEvent.businessId);
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
//					Intent inttentEventDetails = new Intent(Palendar.this, EventDetails.class);
//					inttentEventDetails.putExtra("EventID",v.getId());
//					TabGroupActivity pActivity = (TabGroupActivity)Palendar.this.getParent();
//					pActivity.startChildActivity("EventDetails", inttentEventDetails);
					Intent inttentEventDetails = new Intent(Palendar.this, EventDetailsViewPager.class);
					inttentEventDetails.putExtra(EVENT_ID,v.getId());
					vctEventdates		= eventsBL.getdatesOfUserAttendingEvents(strAttendingEvents,CalendarView.strPalenderSelecteddate);
					Log.v(LOGTAG, "vctEventdates size = "+vctEventdates.size());
					inttentEventDetails.putExtra(EVENTS_LIST,convertListEventDatesToEventDetails(vctEventdates));
					inttentEventDetails.putExtra(POSITION, position);
					TabGroupActivity pActivity = (TabGroupActivity)Palendar.this.getParent();
//					pActivity.startChildActivity("EventDetails", inttentEventDetails);
					pActivity.startChildActivity(EVENT_DETAILS_VIEW_PAGER, inttentEventDetails);
				}
			});
			
			return convertView;
		}
	}/* 
	 * end of inner class CustomEventsAdapter
	 **/
	
	private ArrayList<EventsDetails> convertListEventDatesToEventDetails(List<EventDates> data){
		
		ArrayList<EventsDetails> result = new ArrayList<EventsDetails>();
		for(int i=0; i<data.size();i++){
			EventsDetails tmp = new EventsDetails();
			tmp.eventid = data.get(i).eventId;
			result.add(tmp);
		}
		
		return result;
	}
	
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
						Intent intent = new Intent(Palendar.this,FriendsProfile.class);
						intent.putExtra(USERFRIENDID,friendId);
						
						TabGroupActivity pActivity = (TabGroupActivity)Palendar.this.getParent();
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
					if(!objDrawableManager.fetchDrawableOnThread(friendImageUrl, ivFriendsListIcon,50,50,friend.image_id,friend.isImageUpdated)){
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
		if(calEventAdapter != null)
			calEventAdapter.refresh(new ArrayList<EventDates>());
		if(adapterList != null)
			adapterList.refresh(new ArrayList<EventDates>());
		if(objDrawableManager != null)
			objDrawableManager.clear();
		super.onPause();
	}

	@Override
	protected void onStop() 
	{
		saveCurrentCategory();
		if(calEventAdapter != null)
			calEventAdapter.refresh(new ArrayList<EventDates>());
		if(adapterList != null)
			adapterList.refresh(new ArrayList<EventDates>());
		if(objDrawableManager != null)
			objDrawableManager.clear();
		super.onStop();
	}
	
	
	
	@Override
	protected void onDestroy() {
		saveCurrentCategory();
		super.onDestroy();
	}

	protected void saveCurrentCategory() {
	    SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.putInt(CURREN_PALENDAR_CATEGORY, categoryId);
	    editor.commit();
	}
	

	@Override
	public void daySelected(int day, int month, int year)
	{
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_SEARCH == keyCode){
			showQuickSearchPopup();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void showList() {
		isCalBtnClicked= true;
		Intent inttentEventDetails = new Intent(Palendar.this, Palendar.class);
		inttentEventDetails.putExtra(IS_CAL_BTN_CLICKED, false);
		TabGroupActivity pActivity = (TabGroupActivity)Palendar.this.getParent();
		pActivity.startChildActivity(PALENDAR, inttentEventDetails);
	}

}
