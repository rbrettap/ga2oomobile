package com.ga2oo.palendar;

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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.businesslayer.FriendsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.Tools;
import com.ga2oo.palendar.controls.CalendarListener;
import com.ga2oo.palendar.controls.CalendarView;
import com.ga2oo.palendar.controls.PopUpDailog;
import com.ga2oo.palendar.objects.Category;
import com.ga2oo.palendar.objects.EventDates;
import com.ga2oo.palendar.objects.EventsDetails;
import com.ga2oo.palendar.objects.UserFriend;

public class BusinessPalender extends ShareEvent implements CalendarListener
{
	private static final String LOGTAG ="BusinessPalender";
	private static final String ISCALBUTTONCLICKED = "isCalBtnClicked";
	private static final String BUSINESSID = "businessId";
	private static final String FRIENDNAME = "friendname";
	private static final String DATEFORMAT1 ="MMM dd, yyyy";
	private static final String DATEFORMAT2 = "yyy-MM-dd";
	private static final String BUSINESSPALENDER = "BusinessPalender";
	private static final String FRIENDSPALENDER = "friends palender";
	private static final String EVENTID = "EventID";
	private static final String EVENTDETAILS = "EventDetails";
	private static final String EVENTSLIST = "EventsList";
	private static final String POSITION = "Position";
	private static final String EVENTDETAILSVIEWPAGER = "EventDetailsViewPager";
	private static final String USERFRIENDID = "userFriendId";
	private static final String FRIENDSPROFILE ="FriendsProfile";
	
	private LinearLayout llCalView, llListView, llMonthAndWeekDays, llPrevious, llNext, llDaysOfMonth, llCatg;
	private Button btnPrevious, btnNext;
	private TextView tvDistance, tvType, tvDateInList,tvMonth,tvFriendName;
	private ListView  lvEventsList,lvFriendsList;
//	public static ListView lvEvents;
	private ImageView ivCalIcon,ivSearch;
	private ProgressDialog progressDialog;
	private EditText etQuicksearchByLocation,etQuicksearchBykey;
	
	
	public static boolean isCalBtnClicked = true;
	private static boolean isFirstTime = true;
	private int downX, moveX, day, month, year,selDistanceId = 0, selTypeId = 0;
	private String  friendImageUrl,strFriendName;
	public static String strAttendingEvents="";
	public String strFacebookWallPost;
	public int businessId;
	public static String strSearchKeyWord="";
	public static int categoryId;
	
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
	
	static public ArrayList<EventDates> vctEventdates;
	static public List<EventDates> vctEventList;
	public List<Category> vctCategoryList;
	public static List<EventsDetails> vecUpcomingEvent,vctAllEvenetsList;
	public List<UserFriend> userFriends;
	public List<UserFriend> vecFriends;
	private List<String> vctEmails;
	private String selecteDate[];
	private String to[],toAllFriend[];
	private String strEventDetail="";
	private String strEventImage="";
	private String strEventName;
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
		isCalBtnClicked = this.getIntent().getBooleanExtra(ISCALBUTTONCLICKED, true);
		businessId		= getIntent().getExtras().getInt(BUSINESSID);
		strFriendName		= getIntent().getExtras().getString(FRIENDNAME);
		if(progressDialog!=null && progressDialog.isShowing())
			progressDialog.dismiss();
		
		TextView tvAll = new TextView(BusinessPalender.this);
		tvAll.setText(getResources().getString(R.string.all));
		tvAll.setGravity(Gravity.CENTER_HORIZONTAL);
		tvAll.setPadding(7, 0, 7, 0);
		tvAll.setTextColor(Color.WHITE);
		
		progressDialog = new ProgressDialog(getParent());
		progressDialog.setMessage(getResources().getString(R.string.loading));
		
		tvFriendName		= (TextView) findViewById(R.id.tvFriendName);
		llCalView 			= (LinearLayout) findViewById(R.id.llCalView);
		llListView 			= (LinearLayout) findViewById(R.id.llListView);
		
		llMonthAndWeekDays 	= (LinearLayout) findViewById(R.id.llMonthAndWeekDays);
		llDaysOfMonth 		= (LinearLayout) findViewById(R.id.llDaysOfMonth);
//		lvEvents 			= (ListView) findViewById(R.id.lvEvents);
		
		lvEventsList 		= (ListView) findViewById(R.id.lvEventsList);
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
		
		strAttendingEvents	= eventsBL.getLogedInUserAttendingEvents(businessId);
		llCatg.addView(tvAll,new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		tvAll.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				progressDialog.show();
				if(vctEventdates!=null)
					vctEventdates.clear();
				else
					vctEventdates=new ArrayList<EventDates>();
				categoryId=0;
				new Thread(new  Runnable()
				{
					@Override
					public void run() 
					{
							System.gc();
							strSearchKeyWord="";
							vctEventdates		= eventsBL.getEventsByBusiness(businessId,"",CalendarView.strSelectedDate,categoryId);
							runOnUiThread(new Runnable()
							{
								public void run() 
								{
									if(vctEventdates.size()!=0&&vctEventdates!=null)
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
		
		vctCategoryList=eventsBL.getAllCategories();
		for(int i=0; i<vctCategoryList.size(); i++)
		{
			TextView tvCatg = new TextView(BusinessPalender.this);
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
					if(vctEventdates!=null)
						vctEventdates.clear();
					else
						vctEventdates=new ArrayList<EventDates>();
					categoryId=v.getId();
					  new Thread(new  Runnable()
				    	{
							@Override
							public void run() 
							{
								System.gc();
								vctEventdates=eventsBL.getEventsByBusiness(businessId,"", CalendarView.strSelectedDate,categoryId);
								runOnUiThread(new Runnable()
								{
									public void run() 
									{
										if(vctEventdates.size()!=0)
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
		
		llCatg.getChildAt(0).setBackgroundResource(R.drawable.catg_hover);
		tvAll.performClick();
		ivCalIcon = (ImageView) findViewById(R.id.ivCalIcon);
		ivCalIcon.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				selecteDate=CalendarView.strSelectedDate.split("-");    
				DatePickerDialog dailog = new DatePickerDialog(BusinessPalender.this.getParent(), new OnDateSetListener()
				{											
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) 
					{
						Calendar cal= Calendar.getInstance();
						cal.set(year, monthOfYear, dayOfMonth);
		        		String dateFormat=(String) DateFormat.format(DATEFORMAT1, cal);
		        		String dateFormat1=(String) DateFormat.format(DATEFORMAT2, cal);
		        		CalendarView.strSelectedDate=dateFormat1;
		        		tvDateInList.setText(dateFormat.substring(0, dateFormat.indexOf(",")));	
		        		vctEventdates.clear();
						vctEventdates=eventsBL.getEventsByBusiness(businessId,strSearchKeyWord,dateFormat1,categoryId);
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
				getResources().getString(R.string.drinking),getResources().getString(R.string.playing)};
		
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
		
//		lvEvents.setOnItemClickListener(new OnItemClickListener()
//		{
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View v, int arg2,long arg3)
//			{
//				Log.i("id of clicked",""+v.getId());
//				Intent inttentEventDetails = new Intent(BusinessPalender.this, EventDetails.class);
//				inttentEventDetails.putExtra("EventID",v.getId());
//				TabGroupActivity pActivity = (TabGroupActivity)BusinessPalender.this.getParent();
//				pActivity.startChildActivity("EventDetails", inttentEventDetails);
//			}
//		});
		
		if(isFirstTime)
		{
			isFirstTime = false;
			buildCalView();
			buildListView();
		}
		
		if(isCalBtnClicked)
		{
			llCalView.setVisibility(View.VISIBLE);
			llListView.setVisibility(View.GONE);	
		}
		else
		{
			llCalView.setVisibility(View.GONE);
			llListView.setVisibility(View.VISIBLE);		
		}
		
		ivSearch.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				showQuickSearchPopup();
			}
		});
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar_empty);
        TabsActivity.tvTitle.setVisibility(View.VISIBLE);
        TabsActivity.tvTitle.setText(strFriendName);
        TabsActivity.tvTitle.setPadding(10, 0, 0, 0);
		TabsActivity.btnBack.setVisibility(View.GONE);
		TabsActivity.llEventButtons.setVisibility(View.VISIBLE);
		if(isCalBtnClicked)
		{
			TabsActivity.btnCalView.setBackgroundResource(R.drawable.btn_calview_hover);
			TabsActivity.btnListView.setBackgroundResource(R.drawable.btn_listview);
			buildCalView();
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
			
			buildListView();
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
				Intent inttentEventDetails = new Intent(BusinessPalender.this, BusinessPalender.class);
				inttentEventDetails.putExtra(ISCALBUTTONCLICKED, false);
				TabGroupActivity pActivity = (TabGroupActivity)BusinessPalender.this.getParent();
				pActivity.startChildActivity(BUSINESSPALENDER, inttentEventDetails);
			}
		});
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
		    					Intent intent = new Intent(BusinessPalender.this, Login.class);
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

	
	public void showQuickSearchPopup()
	{
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.quick_search_popup, null);
		
		etQuicksearchBykey	 		 		 = (EditText)popup.findViewById(R.id.etQuicksearchBykey);
		etQuicksearchByLocation	 			 = (EditText)popup.findViewById(R.id.etQuicksearchByLocation);
		Button btnSearchByKeyword 	 		 = (Button)popup.findViewById(R.id.btnSearchByKeyword);
		Button btnSearchByLocation			 = (Button)popup.findViewById(R.id.btnSearchByLocation);
	
		btnSearchByKeyword.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				String searchKeyword="";
				searchKeyword=etQuicksearchBykey.getText().toString();
				vctEventdates.clear();
				if(isCalBtnClicked)
				{
					strSearchKeyWord=etQuicksearchBykey.getText().toString();
					buildFilteredCalView();
				}
				else
				vctEventdates=eventsBL.getEventsByBusiness(businessId,searchKeyword,CalendarView.strSelectedDate,categoryId);
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
					vctEventdates=eventsBL.getEventsByBusinessLocation(businessId,searchKeyword,CalendarView.strSelectedDate,categoryId);
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
		strSearchKeyWord="";
		if(llMonthAndWeekDays != null && llMonthAndWeekDays.getChildCount() > 0)
			llMonthAndWeekDays.removeAllViews();
      
		vctEventdates.clear();
		tvFriendName.setVisibility(View.GONE);
		tvFriendName.setText(strFriendName);
        vctEventdates			= eventsBL.getEventsByBusiness(businessId,"","",0);
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
					
					tvMonth.setText(Tools.getMonthFromNumber(month+1,BusinessPalender.this)+" "+year);
					CalendarView.strSelectedDate=year+"-"+(month+1)+"-"+"1";
					CalendarView calView = new CalendarView(BusinessPalender.this, month, year,BusinessPalender.this);
					llDaysOfMonth.addView(calView.makeBusinessPalendar(businessId));
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
					
					tvMonth.setText(Tools.getMonthFromNumber(month+1,BusinessPalender.this)+" "+year);
					CalendarView.strSelectedDate=year+"-"+(month+1)+"-"+"1";
					tvMonth.invalidate();
					CalendarView calView = new CalendarView(BusinessPalender.this, month, year,BusinessPalender.this);
					llDaysOfMonth.addView(calView.makeBusinessPalendar(businessId));
//					lvEvents.setVisibility(View.GONE);
				}
				return false;
			}
		});
        
        currentCal = Calendar.getInstance();
        currentCal.get(Calendar.DAY_OF_MONTH);
        if(CalendarView.strSelectedDate.equals(FRIENDSPALENDER))
        {
        	day = currentCal.get(Calendar.DAY_OF_MONTH);
        	month = currentCal.get(Calendar.MONTH);
        	year = currentCal.get(Calendar.YEAR);
        }
        else
        {
        	selecteDate=CalendarView.strSelectedDate.split("-");
        	if(selecteDate.length==3)
        	{
        		day = Integer.parseInt(selecteDate[2]);
            	month = Integer.parseInt(selecteDate[1])-1;
            	year = Integer.parseInt(selecteDate[0]);
        	}
        }

        tvMonth.setText(Tools.getMonthFromNumber(month+1,BusinessPalender.this)+" "+year);       
		
        CalendarView calView = new CalendarView(BusinessPalender.this, month, year,BusinessPalender.this);
		llDaysOfMonth.addView(calView.makeBusinessPalendar(businessId));
	}/*
	 * end of method buildCalView()
	 **/
	
	public void buildFilteredCalView()
	{
		progressDialog.show();
		if(llDaysOfMonth.getChildCount() > 0)
			llDaysOfMonth.removeAllViews();
			
		if(tvMonth!=null)
		tvMonth.setText(Tools.getMonthFromNumber(month+1,BusinessPalender.this)+" "+year);
		new Thread(new  Runnable()
		{
			@Override
			public void run() 
			{
				final CalendarView calView = new CalendarView(BusinessPalender.this, month, year,BusinessPalender.this);
				runOnUiThread(new Runnable()
				{
					public void run() 
					{
							llDaysOfMonth.addView(calView.makeBusinessPalendar(businessId));
					}
				});
					progressDialog.dismiss();
			}
		}).start();
			
//		lvEvents.setVisibility(View.GONE);
	}
	
	/* 
	 * inner class CustomCalEventsAdapter used by ListView
	 */
	public class CustomCalEventsAdapter extends BaseAdapter 
	{
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
					Intent inttentEventDetails = new Intent(BusinessPalender.this, EventDetails.class);
					inttentEventDetails.putExtra(EVENTID, v.getId());
					TabGroupActivity pActivity = (TabGroupActivity)BusinessPalender.this.getParent();
					pActivity.startChildActivity(EVENTDETAILS, inttentEventDetails);
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
		if(!CalendarView.strSelectedDate.equals(""))
		{
			String dateArr[]=CalendarView.strSelectedDate.split("-");
			String date=	new DateFormatSymbols().getMonths()[Integer.parseInt(dateArr[1])-1];
			tvDateInList.setText(date.substring(0, 3)+" "+dateArr[2]);
		}
		for(int i=1; i < llCatg.getChildCount(); i++)
		{
			llCatg.getChildAt(i).setBackgroundResource(0);
		}
		llCatg.getChildAt(0).setBackgroundResource(R.drawable.catg_hover);
		vctEventdates.clear();
		vctEventdates		= eventsBL.getEventsByBusiness(businessId,"",CalendarView.strSelectedDate,0);
		lvEventsList.setAdapter(adapterList = new CustomEventsAdapter(vctEventdates));
	}/* 
	 * end of method buildListView
	 **/
	/* 
	 * start of inner class CustomEventsAdapter used by buildListView method
	 */
	public class CustomEventsAdapter extends BaseAdapter 
	{
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
        	ImageView ivFriendOne = (ImageView) convertView.findViewById(R.id.ivFriendOne);
        	ProgressBar eventicon_progress_bar=(ProgressBar)convertView.findViewById(R.id.eventicon_progress_bar);
			ProgressBar progressBar=(ProgressBar)convertView.findViewById(R.id.event_progress_bar);
        	friendBL=new FriendsBusinessLayer();
			userFriends=new ArrayList<UserFriend>();
		    userFriends=friendBL.getUsrFrendAttendingEvents(Integer.toString(objEvent.eventId));
		    final ImageView ivFavourite = (ImageView) convertView.findViewById(R.id.ivFavourite);
			ImageView ivShare = (ImageView) convertView.findViewById(R.id.ivShare);
			ivShare.setId(eventId);
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
				if(!userFriends.get(0).Image1.equals(AppConstants.NOIMAGE)){
					if(!objDrawableManager.fetchDrawableOnThread(friendImageUrl, ivFriendOne,50,50,userFriends.get(0).image_id,userFriends.get(0).isImageUpdated)){
						progressBar.setVisibility(View.GONE);
						tvNoOfFriends.setText(getResources().getString(R.string.no_friend_yet));
						ivFriendOne.setVisibility(View.GONE);
					}
				}else{
					progressBar.setVisibility(View.GONE);
					tvNoOfFriends.setText(getResources().getString(R.string.no_friend_yet));
					ivFriendOne.setVisibility(View.GONE);
				}
				if(userFriends.get(0).noOfFriends==1)
					tvNoOfFriends.setText(userFriends.get(0).noOfFriends+" "+getResources().getString(R.string.friend));
				else
					tvNoOfFriends.setText(userFriends.get(0).noOfFriends+" "+getResources().getString(R.string.friends));
				tvNoOfFriends.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						if(userFriends.get(0).noOfFriends!=0)
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
			tvUpName.setText(objEvent.eventName);
			tvStartDate.setText(objEvent.strEventStartdate);
			tvStartTime.setText(objEvent.strEventStartTime.toString().substring(0, 5)+"  "+objEvent.strEventPrice);
			strEventName=objEvent.eventName.toString();
			strEventDetail=getResources().getString(R.string.start_date)+objEvent.strEventStartdate.toString()+
					getResources().getString(R.string.start_time)+objEvent.strEventStartTime.toString()+
					getResources().getString(R.string.event_price)+objEvent.strEventPrice;
			ivFavourite.setTag(objEvent.businessId);
			ivFavourite.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					showFavouritePopup(eventId,(Integer) ivFavourite.getTag());
				}
			});
			
			ivShare.setOnClickListener(new OnClickListener()
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
					Intent inttentEventDetails = new Intent(BusinessPalender.this, EventDetailsViewPager.class);
					inttentEventDetails.putExtra(EVENTID,v.getId());
					vctEventdates		= eventsBL.getEventsByBusiness(businessId,"",CalendarView.strSelectedDate,categoryId);
					Log.v(LOGTAG, "vctEventdates size = "+vctEventdates.size());
					inttentEventDetails.putExtra(EVENTSLIST,convertListEventDatesToEventDetails(vctEventdates));
					inttentEventDetails.putExtra(POSITION, position);
					TabGroupActivity pActivity = (TabGroupActivity)BusinessPalender.this.getParent();
//					pActivity.startChildActivity("EventDetails", inttentEventDetails);
					pActivity.startChildActivity(EVENTDETAILSVIEWPAGER, inttentEventDetails);
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
						Intent intent = new Intent(BusinessPalender.this,FriendsProfile.class);
						intent.putExtra(USERFRIENDID,friendId);
						
						TabGroupActivity pActivity = (TabGroupActivity)BusinessPalender.this.getParent();
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
		TabsActivity.llEventButtons.setVisibility(View.GONE);
		llCalView.removeAllViews();
		llListView.removeAllViews();
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
	
		if(calEventAdapter != null)
			calEventAdapter.refresh(new ArrayList<EventDates>());
		if(adapterList != null)
			adapterList.refresh(new ArrayList<EventDates>());
		if(objDrawableManager != null)
			objDrawableManager.clear();
		super.onStop();
	}

	@Override
	public void daySelected(int day, int month, int year)
	{
		
	}
	@Override
	public void showList() {
//		TabsActivity.btnCalView.setBackgroundResource(R.drawable.btn_calview);
//		TabsActivity.btnListView.setBackgroundResource(R.drawable.btn_listview_hover);
//		
//		buildListView();
//		adapterList.notifyDataSetChanged();
//		isCalBtnClicked=false;
//		llCalView.setVisibility(View.GONE);
//		llListView.setVisibility(View.VISIBLE);	
		isCalBtnClicked= true;
		Intent inttentEventDetails = new Intent(BusinessPalender.this, BusinessPalender.class);
		inttentEventDetails.putExtra(ISCALBUTTONCLICKED, false);
		TabGroupActivity pActivity = (TabGroupActivity)BusinessPalender.this.getParent();
		pActivity.startChildActivity(BUSINESSPALENDER, inttentEventDetails);
	}

	
}