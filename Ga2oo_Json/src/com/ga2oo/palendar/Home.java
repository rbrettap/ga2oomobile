package com.ga2oo.palendar;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.businesslayer.FriendsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.DialogUtility;
import com.ga2oo.palendar.controls.PopUpDailog;
import com.ga2oo.palendar.objects.EventsDetails;
import com.ga2oo.palendar.objects.EventsDetailsData;
import com.ga2oo.palendar.objects.FavoriteEvent;
import com.ga2oo.palendar.objects.Notifications;
import com.ga2oo.palendar.objects.UserAccount;
import com.ga2oo.palendar.objects.UserCurrentLocationBasedOnZip;
import com.ga2oo.palendar.objects.UserFriend;
import com.ga2oo.palendar.objects.UserLocation;
import com.ga2oo.palendar.objects.UserLocationObject;
import com.ga2oo.palendar.objects.UserRecommendationObject;
import com.ga2oo.palendar.xmlparsers.ParseUserCurrentLocationBasedOnZip;
import com.ga2oo.jsonparsers.EventsDetailsWrapper;
import com.ga2oo.jsonparsers.EventsWrapper;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;
import com.ga2oo.jsonparsers.NotificationsWrapper;
import com.ga2oo.jsonparsers.RecommendationUseraccountWrapper;
import com.ga2oo.jsonparsers.UserAccountWrapper;
import com.ga2oo.parsing.net.HttpHelper;
import com.ga2oo.parsing.net.JsonHttpHelper;
import com.ga2oo.parsing.net.LocationUtility;
import com.ga2oo.parsing.net.LocationUtility.LocationResult;

import com.flurry.android.*;

public class Home extends ShareEvent implements LocationResult, FlurryAdListener
{
	public static final String LOGTAG = "HomeScreen";
	public static final String NONE = "None";
	public static final String FROMACTIVITY = "fromActivity";
	public static final String HOME = "Home";
	public static final String PROFILEDETAILS = "ProfileDetails";
	public static final String UPCOMINGEVENTS = "UpcomingEvents";
	public static final String DATEFORMAT = "yyyy-MM-dd";
	public static final String EVENTID = "EventID";
	public static final String EVENTSLIST = "EventsList";
	public static final String POSITION = "Position";
	public static final String EVENTDETAILSVIEWPAGER = "EventDetailsViewPager";
	public static final String SEARCHKEYWORD = "searchKeyword";
	public static final String SEARCHBY = "searchBy";
	public static final String KEYWORD = "Keyword";
	public static final String SEARCHEDEVENT = "SearchedEvent";
	public static final String LOCATION = "Location";
	private static final String GEOPOINT = "GeoPoint";
	private static final String STRTO = "strTo";
	private static final String MYMAPACTIVITY = "MyMapActivity";
	public static final String ADDNEWLOCATION = "addNewLocation";
	public static final String GEOPOINTFORMAT = "#.#######";
	public static final int DAYSINMONTHS = 93;
	
	private TextView tvName, tvLocation,tvQuickSearch,tvFeatured,tvUpcoming;
	private Button btnAddLocation, btnNoOfEvents;
	private ImageView ivUserImage;
	private ProgressBar user_image_progressBar;
	private EditText etQuicksearchBykey,etQuicksearchByLocation;
	public static List<com.ga2oo.palendar.objects.Events> vctEvents1;
	public  ListView lvFriendsList;
	public PullToRefreshListView lvFeatured,lvUpcoming;
	public LinearLayout llHomeRoot,llFeatured,llUpcoming,llEvetsMain;
	private RelativeLayout rlUserImage;
	private ScrollView svEvents;
	private TextView noEventsFound;
	
	private PopUpDailog customDialog;
	private PopupWindow mPopupWindow =null;
	private DrawableManager objDrawableManager;
	private Context context;
	private UserAccountBusinessLayer userActBL;
	private EventsBusinessLayer eventsBL;
	private FriendsBusinessLayer friendBL;
	public  AlertDialog alertDialog;
	private CustomFriendAdapter friendAdapter;
	private Date eventDate;
	private Date currentDate;
	private SimpleDateFormat sdfCurrentDate;
	private CustomUpcomingEventAdapter objCustomUpcomingEventAdapter;
	private CustomFeaturedEventAdapter objCustomFeaturedEventAdapter;
	private ProgressDialog progressDialog;
	private ShareEvent objShareEvent;
	
	public ArrayList<EventsDetails> vctEventsList;
	public List<EventsDetails> vctEvents,vctUpcomingEvents;
	public static List<UserAccount> vctUserAccount;
	public static List<UserLocation> vctUserLocation;
	private List<UserFriend> vecFriends,userFriendInfo1;
	
	public static String userImageUrl="";
	private String strEventStartDate;
    public int i1,duration = Toast.LENGTH_SHORT,currentLocation;
    public  boolean ischecked=false;
	
	private WheelView location;
	private List<UserLocationObject> vctUserSavedLocation;
	private List<String> vctEmails;
	private String userlocation[];
	private String strEventDetail="";
	private String strEventImage="";
	private String strEventName;
	private String dateFeaturedEvents;
	private int noOfNewNotification;
	private double lattitude,longitude;
	LocationUtility locationUtility  = new LocationUtility();
	private  Geocoder geocoder;

	private JsonElement element;
	private JsonHttpHelper jsonHelper;
	UserAccountBusinessLayer userLayer;
	
	private final String kLogTag = getClass().getSimpleName();
	private FrameLayout fContainer;

	
	@Override
	public void onCreate(Bundle savedInstanceState) 
    {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);

    }
	
	@Override
	public void onStart() {
	    
	    super.onStart();
	    FlurryAgent.onStartSession(this, AppConstants.Flurry_Api_Key.toString());
	}
	
    @Override
    public boolean shouldDisplayAd(String adSpaceName, FlurryAdType type)
    {
        Log.d(kLogTag, "shouldDisplayAd("+adSpaceName+","+type.toString()+")");
        return true;
    }

    @Override
    public void spaceDidFailToReceiveAd(String adSpaceName)
    {
        Log.d(kLogTag, "spaceDidFailToReceiveAd("+adSpaceName+")");
    }

    @Override
    public void spaceDidReceiveAd(String adSpaceName)
    {
        Log.d(kLogTag, "spaceDidReceiveAd("+adSpaceName+")");
        FlurryAds.displayAd(this, "Banner_Top", fContainer);
    }
    @Override
    public void onApplicationExit(String adSpaceName)
    {
        Log.d(kLogTag, "onApplicationExit("+adSpaceName+")");
    }

    @Override
    public void onRenderFailed(String adSpaceName)
    {
        Log.d(kLogTag, "onRenderFailed("+adSpaceName+")");
    }
    
    @Override
    public void onAdClicked(String adSpaceName)
    {
        Log.d(kLogTag, "onAdClicked("+adSpaceName+")");
    }

    @Override
    public void onAdOpened(String adSpaceName)
    {
        Log.d(kLogTag, "onAdOpened("+adSpaceName+")");
    }

    @Override
    public void onAdClosed(String adSpaceName)
    {
        Log.d(kLogTag, "onAdClosed("+adSpaceName+")");
    }    
    
    @Override
    public void onVideoCompleted(String adSpace) 
    {
        Log.d(kLogTag, "onVideoCompleted "+ adSpace);
    }   
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		setContentView(R.layout.home);
		
		friendBL			=  new FriendsBusinessLayer();
		locationUtility.getLocation(this, this);
		objShareEvent			= new ShareEvent();
		vctEmails				=  new ArrayList<String>();
		objDrawableManager 		=  new DrawableManager();
		currentDate				=  new Date();
		context 				=  this;
		userFriendInfo1			=  new ArrayList<UserFriend>();
		llHomeRoot				= (LinearLayout)findViewById(R.id.llhomeroot);
		ivUserImage 			= (ImageView) findViewById(R.id.ivUserImage);
		tvName  				= (TextView) findViewById(R.id.tvName);
		tvLocation  			= (TextView) findViewById(R.id.tvLocation);
		tvFeatured				= (TextView)findViewById(R.id.tvFeatured);
		btnAddLocation  		= (Button) findViewById(R.id.btnAddLocation);
		btnNoOfEvents  			= (Button) findViewById(R.id.btnNoOfEvents);
		tvQuickSearch  			= (TextView) findViewById(R.id.tvQuickSearch);
		svEvents				= (ScrollView)findViewById(R.id.svEvents);	
		llEvetsMain				= (LinearLayout)findViewById(R.id.llEvetsMain);
		llFeatured				= (LinearLayout)findViewById(R.id.llFeatured);
		tvUpcoming				= (TextView)findViewById(R.id.tvUpcoming);
		llUpcoming				= (LinearLayout)findViewById(R.id.llUpcoming);
		rlUserImage				= (RelativeLayout)findViewById(R.id.rlUserImage);
		user_image_progressBar	= (ProgressBar) findViewById(R.id.user_image_progressBar);
		lvFeatured				= new PullToRefreshListView(this);
		lvUpcoming				= new PullToRefreshListView(this);
		noEventsFound 			= new TextView(this);
		noEventsFound.setText(R.string.no_events_found);
		lvFeatured.setCacheColorHint(0);
		lvFeatured.setFadingEdgeLength(0);
		lvFeatured.setScrollContainer(false);
		lvFeatured.setSmoothScrollbarEnabled(true);
		lvFeatured.setScrollbarFadingEnabled(true);
		
		objCustomFeaturedEventAdapter=new CustomFeaturedEventAdapter(null);
		lvFeatured.setAdapter(objCustomFeaturedEventAdapter);
		llFeatured.addView(lvFeatured,new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		llFeatured.requestFocus();
		llFeatured.addView(noEventsFound);
		noEventsFound.setVisibility(View.GONE);
		lvUpcoming.setCacheColorHint(0);
		lvUpcoming.setFadingEdgeLength(0);
		lvUpcoming.setScrollContainer(false);
		lvUpcoming.setSmoothScrollbarEnabled(true);
		lvUpcoming.setScrollbarFadingEnabled(true);
//		llUpcoming.addView(noEventsFound);
		jsonHelper = JsonHttpHelper.getInstance();
		userLayer = new UserAccountBusinessLayer();
		
		svEvents.setSmoothScrollingEnabled(true);
		svEvents.setOnTouchListener(new OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				View view = (View) llEvetsMain.getChildAt(2);
				int diff = (view.getTop()-(svEvents.getScrollY()));
				view.setVisibility(View.GONE);
				if(diff<=64)
				{
					tvUpcoming.setVisibility(View.GONE);
					tvFeatured.setText(getResources().getString(R.string.upcoming_events));
				}
				else
				{
					tvUpcoming.setVisibility(View.VISIBLE);
					tvFeatured.setText(getResources().getString(R.string.featured_events));
				}
				return false;
			}
		});
		
		if(userActBL == null)
			userActBL = new UserAccountBusinessLayer();
		if(eventsBL == null)
			eventsBL = new EventsBusinessLayer();
		
		vctUserAccount	= new ArrayList<UserAccount>();
		vctUserLocation	= new ArrayList<UserLocation>();
		vctUserAccount.clear();
		vctUserAccount  = userActBL.getUserInformation();
		vctUserLocation = userActBL.getUserLocation();
	
		if(progressDialog!=null && progressDialog.isShowing())
    		progressDialog.dismiss();
    	
    	progressDialog = new ProgressDialog(getParent());
		progressDialog.setMessage(getResources().getString(R.string.loading));
    	
		//lading actual user and notifications data
    	new GetCurrentData().execute();
    	
		llHomeRoot.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				return false;
			}
		});
		
		if(vctUserAccount.size()!=0)
		{
			if(!vctUserAccount.get(0).imagesrc.equals(AppConstants.NOIMAGE) || !vctUserAccount.get(0).imagesrc.equals(""))
			{
				userImageUrl=vctUserAccount.get(0).imagesrc;
				if(!objDrawableManager.fetchDrawableOnThread(userImageUrl, ivUserImage,65,50,vctUserAccount.get(0).imageId,vctUserAccount.get(0).isImageUpdated)){
					ivUserImage.setImageResource(R.drawable.no_image_smal_event);
				}
				user_image_progressBar.setVisibility(View.GONE);
			}
			else
			{
				user_image_progressBar.setVisibility(View.GONE);
				ivUserImage.setImageResource(R.drawable.no_image_smal_event);
			}
			//TODO: remove version info
			tvName.setText(vctUserAccount.get(0).username+" (app version 332)");
			
			if(vctUserLocation.size()!=0)
				if(!vctUserLocation.get(0).city.equals(NONE)&&!vctUserLocation.get(0).zipcode.equals(NONE))
					tvLocation.setText(vctUserLocation.get(0).city+","+vctUserLocation.get(0).zipcode);
				else if(!vctUserLocation.get(0).city.equals(NONE)&&vctUserLocation.get(0).zipcode.equals(NONE))
					tvLocation.setText(vctUserLocation.get(0).city);
				else if(vctUserLocation.get(0).city.equals(NONE)&&!vctUserLocation.get(0).zipcode.equals(NONE))
					tvLocation.setText(vctUserLocation.get(0).zipcode);
				else if(vctUserLocation.get(0).city.equals(NONE)&&vctUserLocation.get(0).zipcode.equals(NONE)&&!vctUserLocation.get(0).country.equals(NONE))
					tvLocation.setText(vctUserLocation.get(0).country);
		}
		
		tvName.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Home.this,ProfileDetails.class);
				intent.putExtra(FROMACTIVITY,HOME);
				TabGroupActivity pActivity = (TabGroupActivity)Home.this.getParent();
				pActivity.startChildActivity(PROFILEDETAILS, intent);	
			}
		});
		tvQuickSearch.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				showQuickSearchPopup();
			}
		});
		
		btnNoOfEvents.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Home.this,UpcomingEvents.class);
				intent.putExtra(FROMACTIVITY,HOME);
				
				TabGroupActivity pActivity = (TabGroupActivity)Home.this.getParent();
				pActivity.startChildActivity(UPCOMINGEVENTS, intent);	
			}
		});
		
		alertDialog = new AlertDialog.Builder(getParent()).create();
		alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				alertDialog.dismiss();
			}
		});
		
		vctEventsList= eventsBL.getFeaturedEvent();
		if(vctEventsList != null && vctEventsList.size()!=0)
		{
			lvFeatured.setVisibility(View.VISIBLE);
			noEventsFound.setVisibility(View.GONE);
			objCustomFeaturedEventAdapter.refresh(vctEventsList);
			
		}else{
			String date = getCurrentDate();
			int daysCounter=0;
			do{
				date= getNextDate(daysCounter++);
				lvFeatured.setVisibility(View.VISIBLE);
				noEventsFound.setVisibility(View.GONE);
				if(daysCounter>=DAYSINMONTHS){
					lvFeatured.setVisibility(View.GONE);
					noEventsFound.setVisibility(View.VISIBLE);
					break;
				}
				vctEventsList = (ArrayList<EventsDetails>) EventsBusinessLayer.getEventsByDate(date);
			}while(vctEventsList.size()==0);
			dateFeaturedEvents=date;
			objCustomFeaturedEventAdapter.refresh(vctEventsList);
		}
		sdfCurrentDate = new SimpleDateFormat(DATEFORMAT);
		try
		{
			currentDate=sdfCurrentDate.parse(sdfCurrentDate.format(currentDate));
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		btnAddLocation.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				addLocationOption();
			}
		});
		
		
//		if(vctEventsList!=null)
//			vctEventsList.clear();
//		vctEventsList= eventsBL.getFeaturedEvent();
//		if(objCustomFeaturedEventAdapter!=null)
//			objCustomFeaturedEventAdapter.refresh(vctEventsList);
//		
		if(objCustomUpcomingEventAdapter!=null)
			objCustomUpcomingEventAdapter.refresh(vctUpcomingEvents);
		TabsActivity.llEventButtons.setVisibility(View.GONE);
		TabsActivity.btnBack.setVisibility(View.GONE);
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
		    					Intent intent = new Intent(Home.this, Login.class);
		    		            startActivity(intent);
		    		            TabsActivity.tabActivity.finish();
							}
						});
						progressDialog.dismiss();
					}
				}).start();
			}
		});
		upcomingEvent();
		
		
		lvFeatured.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				new GetFeaturedEvents().execute();				
			}
			
		});
		
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.getAdBottomSpace);
        fContainer = new FrameLayout(this);
        linearLayout.addView(fContainer);       
        FlurryAds.setAdListener(this); 
		
	      // fetch and prepare ad for this ad space. won’t render one yet
        FlurryAds.fetchAd(this, "Banner_Top", fContainer, FlurryAdSize.BANNER_BOTTOM);


	}
		
	private class GetCurrentData extends AsyncTask<Void,Void,Boolean>{

		UserAccountBusinessLayer userAccBL;

		@Override
		protected void onPreExecute() {
			userAccBL = new UserAccountBusinessLayer();
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			if(AppConstants.isProfileUpdated)
			{
				Log.i(LOGTAG, "Loading user data.");
				AppConstants.vctUserAccount = new ArrayList<UserAccount>();
				try {
					element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.USER_ACCOUNT_URL+AppConstants.USER_ID);
				} catch (Exception e) {
					Log.i(LOGTAG, "Error in loading user data!");
					e.printStackTrace();
					return false;
				}
				Object userObjects = jsonHelper.parse(element, UserAccountWrapper.class);
				if(userObjects!=null){
					if(((UserAccountWrapper)userObjects).getUserAccount()!=null){
						userLayer.Insert(((UserAccountWrapper)userObjects).getUserAccount());
						vctUserAccount.add(((UserAccountWrapper)userObjects).getUserAccount());
						Log.i(LOGTAG, "Loading user data successfully completed.");
					}else{
						return false;
					}
				}
			}
			AppConstants.vctUserNotifications= new ArrayList<Notifications>();
			Log.i(LOGTAG, "Loading user notifications.");
			try {
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.USER_NOTIFICATIONS+AppConstants.USER_ID);
			} catch (Exception e) {
				Log.i(LOGTAG, "Error in loading user notifications");
				e.printStackTrace();
				return false;
			}
			Object notification = jsonHelper.parse(element, NotificationsWrapper.class);
			if(notification!=null){
				if(((NotificationsWrapper)notification).getUseraccount().notifications!=null){
					userLayer.deleteUserNotifications();
					for(int i=0;i<((NotificationsWrapper)notification).getUseraccount().notifications.size();i++){
						userLayer.insertUserNotifications(((NotificationsWrapper)notification).getUseraccount().notifications.get(i));
						AppConstants.vctUserNotifications.add(((NotificationsWrapper)notification).getUseraccount().notifications.get(i));
					}
				}else{
					return false;
				}
				Log.i(LOGTAG, "Loading user notifications successfully completed.");
			}
			
			AppConstants.vctUserRecommendations = new ArrayList<UserRecommendationObject>();
			Log.i(LOGTAG, "Loading user recomendations.");
			try {
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.USER_INBOX+AppConstants.USER_ID);
			} catch (Exception e) {
				Log.e(LOGTAG, "Error in loading user recomendations!");
				e.printStackTrace();
				return false;
			}
			Object recommendationUseraccountObject = jsonHelper.parse(element, RecommendationUseraccountWrapper.class);
			if(recommendationUseraccountObject!=null){
				if(((RecommendationUseraccountWrapper)recommendationUseraccountObject).getUseraccount().messages!=null){
					userAccBL.deleteInboxValues();
					for(int i=0;i<((RecommendationUseraccountWrapper)recommendationUseraccountObject).getUseraccount().messages.size();i++){
						UserRecommendationObject tmpObject = ((RecommendationUseraccountWrapper)recommendationUseraccountObject).getUseraccount().messages.get(i);
						AppConstants.vctUserRecommendations.add(tmpObject);
						userAccBL.InsertInUserInbox(tmpObject);
					}
				}else{
					return false;
				}
				Log.i(LOGTAG, "Loding user recomendations successfully completed.");
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			if(!result){
				DialogUtility.showConnectionErrorDialog((TabGroupActivity)Home.this.getParent());
			}
				noOfNewNotification=0;
				noOfNewNotification = userAccBL.getUserNotifications().size()+userAccBL.getUserInboxMessage().size();
				btnNoOfEvents.setText(noOfNewNotification!=1?(noOfNewNotification+" "+getResources().getString(R.string.events)):(noOfNewNotification+" "+getResources().getString(R.string.event)));
				btnNoOfEvents.setVisibility(View.VISIBLE);

			super.onPostExecute(result);
		}
		
	}
	
	private class GetFeaturedEvents extends AsyncTask<Void,Void,Boolean>{
		@Override
		protected Boolean doInBackground(Void... params) {
			EventsBusinessLayer evbl=new EventsBusinessLayer();
			
			AppConstants.vctEvents = new ArrayList<EventsDetails>();
			try {
				Log.i(LOGTAG, "Loading events list");
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_LIST+"?"+AppConstants.DATERANGE+Login.getPeriod());
			} catch (Exception e) {
				Log.e(LOGTAG, "Error in loading events list");			
				e.printStackTrace();
				return false;
			}
			Object eventsObjects = jsonHelper.parse(element, EventsWrapper.class);
			if(eventsObjects!=null){
				if(((EventsWrapper) eventsObjects).getEvents()!=null){
					for(int i=0;i<((EventsWrapper) eventsObjects).getEvents().size();i++){
						EventsDetails tmpObject = ((EventsWrapper) eventsObjects).getEvents().get(i);
						AppConstants.vctEvents.add(tmpObject);
						evbl.InsertEventList(tmpObject);
					}
				}else{
					return false;
				}
				Log.i(LOGTAG, "Loading events list successfully completed.");
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			
				llFeatured.removeAllViews();
				vctEventsList.clear();
				vctEventsList = eventsBL.getFeaturedEvent();
				if(vctEventsList.size()==0){
					String date = getCurrentDate();
					int daysCounter=0;
					do{
						date= getNextDate(daysCounter++);
						if(daysCounter>=DAYSINMONTHS){
							noEventsFound.setVisibility(View.VISIBLE);
							lvFeatured.setVisibility(View.GONE);
							break;
						}
						noEventsFound.setVisibility(View.GONE);
						lvFeatured.setVisibility(View.VISIBLE);
						vctEventsList = (ArrayList<EventsDetails>) EventsBusinessLayer.getEventsByDate(date);
					}while(vctEventsList.size()==0);
					dateFeaturedEvents=date;
				}
				
				lvFeatured.setAdapter(objCustomFeaturedEventAdapter=new CustomFeaturedEventAdapter(vctEventsList));
				objCustomFeaturedEventAdapter.refresh(vctEventsList);
				lvFeatured.onRefreshComplete();
				llFeatured.addView(lvFeatured,new LayoutParams(LayoutParams.FILL_PARENT,98*vctEventsList.size()+30));
					lvFeatured.setVisibility(View.VISIBLE);
					noEventsFound.setVisibility(View.GONE);
			if(!result){
				DialogUtility.showConnectionErrorDialog((TabGroupActivity)Home.this.getParent());	
			}
			super.onPostExecute(result);
		}
				
	}
	
	public void upcomingEvent()
	{
		if(llUpcoming!=null)
			llUpcoming.removeAllViews();
		vctUpcomingEvents	=	new ArrayList<EventsDetails>();
		vctEvents = eventsBL.getUpcomingEvent(AppConstants.USER_ID);
		if(vctEvents!=null && vctEvents.size()!=0)
		{
			for(int i=0;i<vctEvents.size();i++)
			{
				strEventStartDate	=	vctEvents.get(i).event_start_date;
				try
				{
					eventDate = sdfCurrentDate.parse(strEventStartDate);
				}
				catch (ParseException e) 
				{
					e.printStackTrace();
				} 
				if(eventDate.after(currentDate) || eventDate.equals(currentDate))
				{
					EventsDetails objeEventsDetails=vctEvents.get(i);
					vctUpcomingEvents.add(objeEventsDetails);
				}
			}

			lvUpcoming.setAdapter(objCustomUpcomingEventAdapter=new CustomUpcomingEventAdapter(vctUpcomingEvents));
			llUpcoming.addView(lvUpcoming, new LayoutParams(LayoutParams.FILL_PARENT,95*vctUpcomingEvents.size()+80));

		}
		
		lvUpcoming.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				new GetUpcomingEvents().execute();			
			}
			
		});
	}
	
	private class GetUpcomingEvents extends AsyncTask<Void,Void,Boolean>{

		List<FavoriteEvent> userEvents = new ArrayList<FavoriteEvent>();
		String startDate;
		Date eventDateStart;
		EventsBusinessLayer ebl = new EventsBusinessLayer();
		
		@Override
		protected Boolean doInBackground(Void... params) {
			
			try {
				Log.i(LOGTAG, "Loading user events list.");
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.GET_USER_EVENTS+AppConstants.USER_ID);
			} catch (Exception e) {
				Log.e(LOGTAG, "Error in loading user event list.");
				e.printStackTrace();
				return false;
			}
			Object userAccountObjects = jsonHelper.parse(element, UserAccountWrapper.class);
			if(userAccountObjects!=null){
			userEvents = ((UserAccountWrapper) userAccountObjects).getUserAccount().events;
			Log.i(LOGTAG, "Loading events list successfully completed.");
				if(userEvents.size()!=0)
				{
					for(int j=0;j<userEvents.size();j++)
					{
						startDate	=	userEvents.get(j).event_start_date;
						try
						{
							eventDateStart = sdfCurrentDate.parse(startDate);
						}
						catch (ParseException e) 
						{
							e.printStackTrace();
						} 
						if(eventDateStart.after(currentDate) || eventDateStart.equals(currentDate))
						{
							try {
								Log.i(LOGTAG, "Loading event details...");
								element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_DETAILS+userEvents.get(j).eventid);
							} catch (Exception e) {
								Log.e(LOGTAG, "Error in loading event details!");
								e.printStackTrace();
								return false;
							}
							Object eventsDetailsObjects = jsonHelper.parse(element, EventsDetailsWrapper.class);
							if(eventsDetailsObjects!=null){
								if(((EventsDetailsWrapper)eventsDetailsObjects).getEvent()!=null){
									EventsDetailsData eventsData = ((EventsDetailsWrapper)eventsDetailsObjects).getEvent();
									EventsDetails tmpEventsDetails = new EventsDetails();
									tmpEventsDetails.eventid =  eventsData.eventid;
									tmpEventsDetails.eventname =  eventsData.eventname;
									tmpEventsDetails.categories =  eventsData.categories;
									tmpEventsDetails.event_start_date =  eventsData.event_start_date;
									tmpEventsDetails.event_start_time =  eventsData.event_start_time;
									tmpEventsDetails.is_featured =  eventsData.is_featured;
									tmpEventsDetails.price = (eventsData.price!=null?eventsData.price:"");
									tmpEventsDetails.business =  eventsData.business.businessid;
									tmpEventsDetails.date_updated =  eventsData.date_updated;
									tmpEventsDetails.images =  eventsData.images;
									tmpEventsDetails.image =  (eventsData.images!=null?eventsData.images.get(0).imagesrc:AppConstants.NOIMAGE);
		//							tmpEventsDetails.eventmainimage =  eventsData.eventmainimage;
									tmpEventsDetails.status =  eventsData.status;
									ebl.InsertEventList(tmpEventsDetails);
									ebl.InsertIntoEventDetails(((EventsDetailsWrapper)eventsDetailsObjects).getEvent());
									vctUpcomingEvents.add(tmpEventsDetails);
									Log.i(LOGTAG, "Loading event details successfully completed.");
									return true;
								}else{
									return false;
								}
							}
						}
					}
				}
			}
	
		return true;
		}
	
		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				llUpcoming.removeAllViews();
					lvUpcoming.setAdapter(objCustomUpcomingEventAdapter=new CustomUpcomingEventAdapter(vctUpcomingEvents));
					objCustomUpcomingEventAdapter.refresh(vctUpcomingEvents);
					lvUpcoming.onRefreshComplete();
					llUpcoming.addView(lvUpcoming, new LayoutParams(LayoutParams.FILL_PARENT,95*vctUpcomingEvents.size()+80));
					
			}else{
				lvUpcoming.onRefreshComplete();
				DialogUtility.showConnectionErrorDialog((TabGroupActivity)Home.this.getParent());	
			}
			super.onPostExecute(result);
		}
		
	}
	
	public class CustomFeaturedEventAdapter extends BaseAdapter 
	{
		private List<EventsDetails> vctEventsDetails;
		public CustomFeaturedEventAdapter(List<EventsDetails> vctEventsDetails)
		{
			this.vctEventsDetails = vctEventsDetails;
		}
		
		@Override
		public int getCount()
		{
			if(vctEventsDetails != null)
				return vctEventsDetails.size();
			return 0;
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
			final EventsDetails objEventsDetails1 	= vctEventsDetails.get(position);
			convertView 							= (LinearLayout) getLayoutInflater().inflate(R.layout.event_cell, null);
			final int eventId						= objEventsDetails1.eventid;;
			LinearLayout llFavourite 				= (LinearLayout) convertView.findViewById(R.id.llStar);
			LinearLayout llShare 					= (LinearLayout) convertView.findViewById(R.id.llShare);
			ImageView ivEventImage 					= (ImageView) convertView.findViewById(R.id.ivEventImage);
			ProgressBar eventImageProgressBar		= (ProgressBar)convertView.findViewById(R.id.eventimage_progress_bar);
			TextView tvEventTitle 					= (TextView) convertView.findViewById(R.id.tvEventTitle);
			TextView tvEventDesc 					= (TextView) convertView.findViewById(R.id.tvEventDesc);
			convertView.setId(eventId);
			llFavourite.setId(objEventsDetails1.EventBusiness);
			llShare.setId(eventId);
			tvEventTitle.setText(objEventsDetails1.eventname.toString());
			tvEventDesc.setSingleLine();
			tvEventDesc.setText(objEventsDetails1.event_start_date.toString()+"  "+objEventsDetails1.event_start_time.toString().subSequence(0, 5)+"  "+objEventsDetails1.price);
			//TODO: fixed this
			Log.v(LOGTAG, "image src = "+objEventsDetails1.image + "isImageUpdated = "+objEventsDetails1.isImageUpdated);
			if(!objEventsDetails1.image.equals(AppConstants.NOIMAGE) && !objEventsDetails1.image.equals(""))
			{
//if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEventsDetails1.image, ivEventImage,50,50)){
				Log.v(LOGTAG, "image id  ="+objEventsDetails1.imageId);
					if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEventsDetails1.image, ivEventImage,50,50,objEventsDetails1.imageId,objEventsDetails1.isImageUpdated)){
						eventImageProgressBar.setVisibility(View.GONE);
						ivEventImage.setBackgroundResource(R.drawable.no_image_smal_event);
					}else{
						strEventImage=AppConstants.IMAGE_HOST_URL+objEventsDetails1.image;
						eventImageProgressBar.setVisibility(View.GONE);
						eventsBL.setImageStatusLoaded(eventId);
					}
	
			} 
			else
			{
				eventImageProgressBar.setVisibility(View.GONE);
				ivEventImage.setBackgroundResource(R.drawable.no_image_smal_event);
			}
			
			llFavourite.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					showFavouritePopup(eventId ,v.getId());
					upcomingEvent();
				}
			});
			
			llShare.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					strEventName=objEventsDetails1.eventname.toString();
					strEventDetail=getResources().getString(R.string.start_date)+objEventsDetails1.event_start_date.toString()+
							getResources().getString(R.string.start_time)+objEventsDetails1.event_start_time.toString()+
							getResources().getString(R.string.event_price)+objEventsDetails1.price;
					//showSharePopup(eventId);
					showSharePopup(getApplicationContext(), eventId,strEventName,strEventDetail);
				}
			});
				

			convertView.setOnClickListener(new OnClickListener()
			{				
				@Override
				public void onClick(View v) 
				{
					Intent inttentEventDetails = new Intent(Home.this, EventDetailsViewPager.class);
					inttentEventDetails.putExtra(EVENTID,v.getId());
					vctEventsList = eventsBL.getFeaturedEvent();
					if(vctEventsList.size()==0){
						vctEventsList = EventsBusinessLayer.getEventsByDate(dateFeaturedEvents);
					}
					inttentEventDetails.putExtra(EVENTSLIST, vctEventsList);
					inttentEventDetails.putExtra(POSITION, position);
					TabGroupActivity pActivity = (TabGroupActivity)Home.this.getParent();
					pActivity.startChildActivity(EVENTDETAILSVIEWPAGER, inttentEventDetails);
				}
			});
			return convertView;
		}
		
		private void refresh(List<EventsDetails> vctEventsDetails)
		{
//			this.vctEventsDetails = ((List<EventsDetails>) vctEventsDetails).clone();
			this.vctEventsDetails = vctEventsDetails;
			if(this.vctEventsDetails != null)
				lvFeatured.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 98 * vctEventsDetails.size()+30));
			this.notifyDataSetChanged();
		}
	}
	
	public class CustomUpcomingEventAdapter extends BaseAdapter 
	{
		private List<EventsDetails> vctUpcomingEvents;
		public CustomUpcomingEventAdapter(List<EventsDetails> vctUpcomingEvents)
		{
			this.vctUpcomingEvents = vctUpcomingEvents;
		}
		
		@Override
		public int getCount()
		{
			if(vctUpcomingEvents != null)
				return vctUpcomingEvents.size();
			return 0;
		}
		
		private void refresh(List<EventsDetails> vctUpcomingEvents)
		{
			this.vctUpcomingEvents = vctUpcomingEvents;
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
			final EventsDetails objEventsDetails = vctUpcomingEvents.get(position);
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.list_events_cell, null);
			TextView tvUpName = (TextView) convertView.findViewById(R.id.tvUpName);
			TextView tvstartDate = (TextView) convertView.findViewById(R.id.tvStartdate);
			TextView tvstartTime = (TextView) convertView.findViewById(R.id.tvStartTime);
			TextView tvNoOfFriends = (TextView) convertView.findViewById(R.id.tvNoOfFriends);
			
			ImageView ivEventIcon = (ImageView) convertView.findViewById(R.id.ivEventIcon);
			LinearLayout llFavourite = (LinearLayout) convertView.findViewById(R.id.llStar);
			LinearLayout llShare = (LinearLayout) convertView.findViewById(R.id.llShare);
			ImageView ivFriendOne = (ImageView) convertView.findViewById(R.id.ivFriendOne);
						
			ProgressBar progressBar=(ProgressBar)convertView.findViewById(R.id.event_progress_bar);
			ProgressBar eventIconProgressBar=(ProgressBar)convertView.findViewById(R.id.eventicon_progress_bar);
			
			try
			{
				userFriendInfo1=friendBL.getUsrFrendAttendingEvents(Integer.toString(objEventsDetails.eventid));
				convertView.setId(objEventsDetails.eventid);
				llFavourite.setId(objEventsDetails.eventid);
				llShare.setId(objEventsDetails.eventid);
				tvNoOfFriends.setId(objEventsDetails.eventid);
				tvUpName.setText(objEventsDetails.eventname.toString());
				tvstartDate.setText(objEventsDetails.event_start_date.toString());
				if(objEventsDetails.event_start_time.toString().length()>5)
					tvstartTime.setText(objEventsDetails.event_start_time.toString().subSequence(0, 5)+"  "+objEventsDetails.price.toString());
				else
					tvstartTime.setText(objEventsDetails.event_start_time.toString()+"  "+objEventsDetails.price.toString());
				strEventName=objEventsDetails.eventname.toString();
				strEventDetail=getResources().getString(R.string.start_date)+objEventsDetails.event_start_date.toString()+
						getResources().getString(R.string.start_time)+objEventsDetails.event_start_time.toString()+
						getResources().getString(R.string.event_price)+objEventsDetails.price;
				//TODO: fixed this
				Log.v(LOGTAG, "image in adapter = "+objEventsDetails.image);
				if(!objEventsDetails.image.equals(AppConstants.NOIMAGE))
				{
					if(!"0".equals(objEventsDetails.imageId)){
//						if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEventsDetails.image, ivEventIcon, 50,50)){
						if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEventsDetails.image, ivEventIcon,50,50,objEventsDetails.imageId,objEventsDetails.isImageUpdated)){
							eventIconProgressBar.setVisibility(View.GONE);
							ivEventIcon.setBackgroundResource(R.drawable.no_image_event);
						}else{
							strEventImage=AppConstants.IMAGE_HOST_URL+objEventsDetails.image;
							eventIconProgressBar.setVisibility(View.GONE);
							eventsBL.setImageStatusLoaded(objEventsDetails.eventid);
						}
					}
				}
				else
				{
					eventIconProgressBar.setVisibility(View.GONE);
					ivEventIcon.setBackgroundResource(R.drawable.no_image_event);
				}
				Log.v(LOGTAG, "friends count"+userFriendInfo1.size());
				if(userFriendInfo1!=null&&userFriendInfo1.size()!=0)
				{
					ivFriendOne.setVisibility(View.GONE);
					progressBar.setVisibility(View.GONE);
					if(userFriendInfo1.get(0).noOfFriends==1)
						tvNoOfFriends.setText(userFriendInfo1.get(0).noOfFriends+" "+getResources().getString(R.string.friend));
					else
						tvNoOfFriends.setText(userFriendInfo1.get(0).noOfFriends+" "+getResources().getString(R.string.friends));
					tvNoOfFriends.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							showFrindsPopup(v.getId());
						}
					});
				}
				else
				{
					tvNoOfFriends.setText(getResources().getString(R.string.no_friend_yet));
					progressBar.setVisibility(View.GONE);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
                	
			llFavourite.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					showFavouritePopup(v.getId(),objEventsDetails.EventBusiness);
					upcomingEvent();
				}
			});
                	
			llShare.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					showSharePopup(getApplicationContext(),objEventsDetails.eventid,strEventName,strEventDetail);
					
				}
			});
                	
		
			convertView.setOnClickListener(new OnClickListener()
			{				
				@Override
				public void onClick(View v) 
				{
					Intent inttentEventDetails = new Intent(Home.this, EventDetails.class);
					inttentEventDetails.putExtra(EVENTID, v.getId());//sending the layout id to Details
					overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
					
					TabGroupActivity pActivity = (TabGroupActivity)Home.this.getParent();
					pActivity.startChildActivity(UPCOMINGEVENTS, inttentEventDetails);	
				}
			});
				
			return convertView;
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
	
		rlCross.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
			}
		});
		btnClose.setOnClickListener(new OnClickListener()
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
				Intent inttentSearchedEvent = new Intent(Home.this, SearchedEvent.class);
				inttentSearchedEvent.putExtra(SEARCHKEYWORD, searchKeyword);
				inttentSearchedEvent.putExtra(SEARCHBY, KEYWORD);
				overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
				TabGroupActivity pActivity = (TabGroupActivity)Home.this.getParent();
				pActivity.startChildActivity(SEARCHEDEVENT, inttentSearchedEvent);
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
				Intent inttentSearchedEvent = new Intent(Home.this, SearchedEvent.class);
				inttentSearchedEvent.putExtra(SEARCHKEYWORD, searchKeyword);
				inttentSearchedEvent.putExtra(SEARCHBY, LOCATION);
				overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
				TabGroupActivity pActivity = (TabGroupActivity)Home.this.getParent();
				pActivity.startChildActivity(SEARCHEDEVENT, inttentSearchedEvent);
				customDialog.dismiss();
			}
		});
		customDialog = new PopUpDailog(getParent(), popup, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}
	
	public void addLocationOption() 
	{
		LayoutInflater inflater = this.getLayoutInflater();
		View mView = inflater.inflate(R.layout.addlocationoption,(ViewGroup) findViewById(R.id.addLocationOptions));
		mPopupWindow = new PopupWindow(mView,android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		mPopupWindow.showAtLocation(mView, Gravity.BOTTOM, 10, 0);

		Button btnExistingLocation = (Button) mView.findViewById(R.id.btnExistingLocation);
		btnExistingLocation.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v)
			{
				mPopupWindow.dismiss();
				View mView = getLayoutInflater().inflate(R.layout.locationpicker,(ViewGroup) findViewById(R.id.locationpicker));
				mPopupWindow = new PopupWindow(mView,android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
				mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
				mPopupWindow.showAtLocation(mView, Gravity.BOTTOM, 10, 0);
				location	=	(WheelView)mView.findViewById(R.id.location);
				vctUserSavedLocation	=	new ArrayList<UserLocationObject>();
			    userActBL		=	new UserAccountBusinessLayer();
			    vctUserSavedLocation.clear();
			    vctUserSavedLocation	=	userActBL.getUserSavedLocation(AppConstants.USER_ID);
			    userlocation			=	new String[vctUserSavedLocation.size()];
			    for(int i=0;i<vctUserSavedLocation.size();i++)
			    {
			    	if(vctUserSavedLocation.get(i).Is_Primary.equals("True"))
			    	{
			    		currentLocation=i;
			    	}
			    	if(!NONE.equals(vctUserSavedLocation.get(i).Zip))
			    		userlocation[i]=vctUserSavedLocation.get(i).City+","+vctUserSavedLocation.get(i).Zip;
			    	else
			    		userlocation[i]=vctUserSavedLocation.get(i).City;
			    }
			    UserLocationAdapter objUserLocationAdapter=new UserLocationAdapter(getApplicationContext(),userlocation,currentLocation);
			    location.setViewAdapter(objUserLocationAdapter);
			    location.setCurrentItem(currentLocation);
			    Button btnDone=(Button)mView.findViewById(R.id.btnDone);
			    btnDone.setOnClickListener(new OnClickListener()
			    {
					@Override
					public void onClick(View v) 
					{
						tvLocation.setText(userlocation[location.getCurrentItem()]);
						userActBL.updatePrimaryLocation(vctUserSavedLocation.get(location.getCurrentItem()).locationId);
//						objGa2ooParsers	=	new Ga2ooParsers();
//						objGa2ooParsers.changeUserPrimaryLocation(vctUserSavedLocation.get(location.getCurrentItem()).locationId);
						Ga2ooJsonParsers.changeUserPrimaryLocation(vctUserSavedLocation.get(location.getCurrentItem()).locationId);
						mPopupWindow.dismiss();
					}
				});
			    
			    Button btnCancel=(Button)mView.findViewById(R.id.btnCancel);
			    btnCancel.setOnClickListener(new OnClickListener()
			    {
					@Override
					public void onClick(View v) 
					{
						mPopupWindow.dismiss();
					}
				});
			}
		});
		
		Button btnAddLocation = (Button) mView.findViewById(R.id.btnAddLocation);
		btnAddLocation.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v)
			{
				mPopupWindow.dismiss();
				showLocation();
			}
		});
		
		
		Button btnCancel = (Button) mView.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				mPopupWindow.dismiss();
			}
		});

	}
	
	public void showLocation()
	{
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.location, null);
		Button btnYes = (Button)popup.findViewById(R.id.btnYes);
		Button btnNo = (Button)popup.findViewById(R.id.btnNo);
		TextView tvMessage=(TextView)popup.findViewById(R.id.tvMessage);
		tvMessage.setText(getResources().getString(R.string.selected_location));
		
		btnYes.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				Log.i("lattitude home",""+lattitude);
				getCurrentLoacationAddress(lattitude,longitude);
				if(AppConstants.vctUserCurrentLocation!=null && AppConstants.vctUserCurrentLocation.size()!=0)
					tvLocation.setText(AppConstants.vctUserCurrentLocation.get(0).strAddress);
				Intent intent = new Intent(Home.this, MyMapActivity.class);
	            intent.putExtra(FROMACTIVITY, HOME);
	            intent.putExtra(GEOPOINT, longitude+","+lattitude);
	            intent.putExtra(STRTO, ADDNEWLOCATION);
	            TabGroupActivity pActivity = (TabGroupActivity)Home.this.getParent();
	            pActivity.startChildActivity(MYMAPACTIVITY, intent);
				customDialog.dismiss();
			}
		});
			
		btnNo.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
				showPoptoEnterZip();
			}
		});
		customDialog = new PopUpDailog(getParent(), popup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}	
	
	public void showPoptoEnterZip()
	{
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.enterzippopup, null);
		Button btnOk = (Button)popup.findViewById(R.id.btnOk);
		Button btnCancel = (Button)popup.findViewById(R.id.btnCancel);
		final EditText etZip	=	(EditText)popup.findViewById(R.id.etZip);
		TextView tvMessage=(TextView)popup.findViewById(R.id.tvMessage);
		tvMessage.setText(getResources().getString(R.string.enter_zip));
		
		btnOk.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				if(etZip.getText().toString().equals("")||etZip.getText().toString()==null)
				{
					alertDialog.setTitle(getResources().getString(R.string.enter_zip));
					alertDialog.setMessage(getResources().getString(R.string.please_enter_zip));
					alertDialog.show();
				}
				else
				{
					getLocationFromZipCode(etZip.getText().toString());
					if(AppConstants.vctUserCurrentLocation!=null&& AppConstants.vctUserCurrentLocation.size()!=0)
						Log.i("geocode",""+AppConstants.vctUserCurrentLocation.get(0).strGeoCode);
					Intent intent = new Intent(Home.this, MyMapActivity.class);
		            intent.putExtra(FROMACTIVITY, HOME);
		            if(AppConstants.vctUserCurrentLocation!=null&& AppConstants.vctUserCurrentLocation.size()!=0)
		            	intent.putExtra(GEOPOINT,AppConstants.vctUserCurrentLocation.get(0).strGeoCode);
		            else
		            	intent.putExtra(GEOPOINT,"");
		            intent.putExtra(STRTO, ADDNEWLOCATION);
		            TabGroupActivity pActivity = (TabGroupActivity)Home.this.getParent();
		            pActivity.startChildActivity(MYMAPACTIVITY, intent);
					customDialog.dismiss();
				}
			}
		});
			
		btnCancel.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
			}
		});
		customDialog = new PopUpDailog(getParent(), popup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}	

	public void getLocationFromZipCode(String zipcode)
	{
		ParseUserCurrentLocationBasedOnZip objUserCurrentLocation	=	new ParseUserCurrentLocationBasedOnZip();
		HttpHelper helper=new HttpHelper();
		try
		{
			helper.settingUrlConnection(new URL(AppConstants.strAPIURL+zipcode), objUserCurrentLocation);
			Log.i("URL",""+AppConstants.strAPIURL+zipcode);
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@Override
	public void gotLocation(Location loc) 
	{
		// TODO Auto-generated method stub
		if(loc!=null)
		{
			locationUtility.stopGpsLocUpdation();
			DecimalFormat df = new DecimalFormat(GEOPOINTFORMAT);
			lattitude=Double.parseDouble(df.format(loc.getLatitude()));
			longitude=Double.parseDouble(df.format(loc.getLongitude()));
		}
		
	}
	
	public void getCurrentLoacationAddress( double LATITUDE, double LONGITUDE)
    {
		geocoder = new Geocoder(this, Locale.ENGLISH);
		try 
		{
			List<Address> addresses = geocoder.getFromLocation(LATITUDE,LONGITUDE,1);
			UserCurrentLocationBasedOnZip objCurrentLocation=new UserCurrentLocationBasedOnZip();
			if (addresses.size()> 0)
			{
				if(AppConstants.vctUserCurrentLocation==null)
					AppConstants.vctUserCurrentLocation=new ArrayList<UserCurrentLocationBasedOnZip>();
				else if(AppConstants.vctUserCurrentLocation!=null)
					AppConstants.vctUserCurrentLocation.clear();
				objCurrentLocation.strCountryName = addresses.get(0).getCountryName();
				objCurrentLocation.strAddress=addresses.get(0).getAddressLine(0);
				objCurrentLocation.strCity=addresses.get(0).getLocality();
				if(addresses.get(0).getPostalCode()!=null)
					objCurrentLocation.zipCode=addresses.get(0).getPostalCode();
				else
					objCurrentLocation.zipCode=NONE;
				objCurrentLocation.strState=addresses.get(0).getAdminArea();
				objCurrentLocation.strGeoCode=""+LATITUDE+","+LONGITUDE;
				AppConstants.vctUserCurrentLocation.add(objCurrentLocation);
			}
			else
			{
				Log.i("","no adress returned");
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}             
    }
	
	private class UserLocationAdapter extends ArrayWheelAdapter<String> 
	{
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;
		
		/**
		 * Constructor
		 */
		public UserLocationAdapter(Context context, String[] items, int current) 
		{
			super(context, items);
			this.currentValue = current;
			setTextSize(16);
		}
		
		@Override
		protected void configureTextView(TextView view) 
		{
			super.configureTextView(view);
			if (currentItem == currentValue) 
			{
				view.setTextColor(0xFF347235);
			}
		}
		@Override
		public View getItem(int index, View cachedView) 
		{
			currentItem = index;
			return super.getItem(index, cachedView);
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		rlUserImage.removeAllViews();
		llFeatured.removeAllViews();
		llUpcoming.removeAllViews();
		if(objDrawableManager != null)
			objDrawableManager.clear();
	}

	@Override
	protected void onStop() 
	{
		super.onStop();
		if(objDrawableManager != null)
			objDrawableManager.clear();
		
		FlurryAgent.onEndSession(this);
	}
	 
	@Override
	 protected void onDestroy()
	 {
	    super.onDestroy();
	    System.gc();
	 }

	@Override
	public boolean onSearchRequested() 
	{
	    return false ; 
	}

	
	private String getCurrentDate(){
		DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	private String getNextDate(int addedDays){
		StringBuilder result =new StringBuilder();
		Calendar c = Calendar.getInstance();
		int date = c.get(Calendar.DATE);
		int month = c.get(Calendar.MONTH)+1;
		int year = c.get(Calendar.YEAR);
		result.append(year+(date/372));
		result.append("-");
		result.append((month+(date/31))<10?"0"+month+(date/31):month+(date/31));
		result.append("-");
		result.append(((date+addedDays)%31)<10?"0"+(date+addedDays)%31:(date+addedDays)%31);
		Log.i(LOGTAG, "searchedDate = "+result);
		return result.toString();
	}

}
