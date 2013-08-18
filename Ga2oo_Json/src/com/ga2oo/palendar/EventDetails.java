package com.ga2oo.palendar;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.businesslayer.FriendsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.DialogUtility;
import com.ga2oo.palendar.common.Tools;
import com.ga2oo.palendar.controls.PopUpDailog;
import com.ga2oo.palendar.objects.Attending;
import com.ga2oo.palendar.objects.Business;
import com.ga2oo.palendar.objects.Event;
import com.ga2oo.palendar.objects.EventImages;
import com.ga2oo.palendar.objects.EventsDetails;
import com.ga2oo.palendar.objects.EventsDetailsData;
import com.ga2oo.palendar.objects.UserFriend;
import com.ga2oo.jsonparsers.EventsDetailsWrapper;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;
import com.ga2oo.parsing.net.JsonHttpHelper;

public class EventDetails extends ShareEvent
{
	
	private static final String LOGTAG = "EventDetailsScreen";
	private static final String EVENTID = "EventID";
	private static final String FROMACTIVITY = "fromActivity";
	private static final String HOME = "Home";
	private static final String GEOPOINT = "GeoPoint";
	private static final String STRTO = "strTo";
	private static final String DISPLAYLOCATION = "displayLocation";
	private static final String MYMAPACTIVITY = "MyMapActivity";
	private static final String BUSINESSID = "businessId";
	private static final String SEARCHEDEVENT = "SearchedEvent";
	private static final String EVENTTICKET = "event_ticket";
	private static final String TEXTPLAIN = "text/plain";
	private static final String IMAGE= "image/*";
	private static final String SMSBODY = "sms_body";
	private static final String ADRESS = "address";
	private static final String MMSSMS = "vnd.android-dir/mms-sms";
	private static final String FACEBOOKSESSION = "facebook-session";
	private static final String ACESSTOKEN = "access_token";
	private static final String UNSELECTED = "unselected";
	private static final String SELECTED = "selected";
	private static final String USERFINDID = "userFriendId";
	private static final String FRIENDSPROFILE = "FriendsProfile";	
	
	private ImageView ivShare, ivStar,ivPhoto,ivBuyTicket,ivMap;
	private ProgressBar eventDetailsProgressBar;
	private TextView tvEventDetails,tvDate,tvTime,tvDetails,tvbusinessname,tvAttendingTitle,tvSeeMoreEvents,tvEventLocation;
	private ListView lvUserAttendingEvent,lvFriendsList;
	private LinearLayout llMapBtn,llStar,llShare,llFriend;
	private RelativeLayout rlEventImage;
	
	public  String friendImageUrl;
	public int eventId,businessId,duration = Toast.LENGTH_SHORT;
	private String to[],toAllFriend[];
	private String strEventDetail="";
	private String strEventImage="";
	private String strEventName;

	public List<Event> vctEvents;
	public List<EventsDetails> vctEventDesc,vctEventsDetails;
	public List<UserFriend> userFriends;
	public List<EventImages> vctEventImages,vctEventImageAdpter;
	private List<String> vctEmails;
	private List<UserFriend> vecFriends;
	
	private DrawableManager objDrawableManager;
	private EventsBusinessLayer eventsBL;
	private FriendsBusinessLayer friendBL;
	private UserAccountBusinessLayer userActBL;
	private CustomFriendsAdapter friendAdapter;
	private PopUpDailog customDialog;
	private ProgressDialog progressDialog;
	private PopupWindow mPopupWindow =null;
	private Context context;
	private JsonElement element;
	private JsonHttpHelper jsonHelper;
	private ImageAdapter objImageAdapter;
	private CustomFriendAdapter objCustomFriendAdapter;
	private AlertDialog.Builder internetPopup;
	private AlertDialog alert;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
    }

	@Override
    public void onResume()
    {
    	super.onResume();
		setContentView(R.layout.eventdetails);
		
		context=this;
		objDrawableManager=new DrawableManager();
		//Reciving the Layout id with eventID from Home
		vctEmails				=  new ArrayList<String>();
	    eventId 				= getIntent().getExtras().getInt(EVENTID);
	    Log.v(LOGTAG, EVENTID+" = "+eventId);
	    userActBL				= new UserAccountBusinessLayer();
		tvEventDetails			=(TextView)findViewById(R.id.tvEventDetails);
		tvDate					=(TextView)findViewById(R.id.tvDate);
		tvSeeMoreEvents			=(TextView)findViewById(R.id.tvSeeMoreEvents);
		tvTime					=(TextView)findViewById(R.id.tvTime);
		llMapBtn				=(LinearLayout)findViewById(R.id.llMapBtn);
		tvEventLocation			=(TextView)findViewById(R.id.tvEventLocation);
		tvDetails				=(TextView)findViewById(R.id.tvDetailsEvent);
		tvbusinessname			=(TextView)findViewById(R.id.tvBusinessName);
		rlEventImage			=(RelativeLayout)findViewById(R.id.rlEventImage);
		ivPhoto					=(ImageView)findViewById(R.id.ivPhoto);
		llStar					= (LinearLayout)findViewById(R.id.llStar);
		llShare					= (LinearLayout)findViewById(R.id.llShare);
		ivStar					= (ImageButton)findViewById(R.id.ivStar);
		ivShare					= (ImageButton)findViewById(R.id.ivShare);
		ivBuyTicket				=(ImageButton)findViewById(R.id.ivBuyTicket);
		eventDetailsProgressBar	=(ProgressBar)findViewById(R.id.eventdetails_progress_bar);
    	
		eventsBL 				= new EventsBusinessLayer();
		jsonHelper = JsonHttpHelper.getInstance();
		
		vctEventsDetails=new ArrayList<EventsDetails>();
    	if(progressDialog!=null && progressDialog.isShowing())
    		progressDialog.dismiss();
    	
    	progressDialog = new ProgressDialog(getParent());
		progressDialog.setMessage(getResources().getString(R.string.loading));
    	AppConstants.vctEventsDetails = new ArrayList<EventsDetailsData>();
    	new LoadEventDetails().execute();
    	 
    	 //when user click on event image then it shows gallery of images
    	 ivPhoto.setOnClickListener(new OnClickListener()
    	 {
			@Override
			public void onClick(View v)
			{
				eventDetailsProgressBar.setVisibility(View.GONE);
				showImageGallery(v.getId());
			}
		});
    	 
    	 
    	//TODO:_____________________________________________________________________________   	 
        /*this code for list view which contains list of
        all the friends which attends any particular event*/
    	tvAttendingTitle	= (TextView)findViewById(R.id.tvAttendingTitle);
    	friendBL			= new FriendsBusinessLayer();
        userFriends			= new ArrayList<UserFriend>();
        userFriends			= friendBL.getUsrFrendAttendingEvents(Integer.toString(eventId));
        llFriend			= (LinearLayout)findViewById(R.id.llFriend);
        lvUserAttendingEvent=(ListView)findViewById(R.id.lvuserAttendingEvents);
        lvUserAttendingEvent.setFadingEdgeLength(0);
        lvUserAttendingEvent.setCacheColorHint(0);
        lvUserAttendingEvent.setDivider(getResources().getDrawable(R.drawable.line));
        lvUserAttendingEvent.setVerticalScrollBarEnabled(false);
        lvUserAttendingEvent.setAdapter(new CustomFriendsAdapter(userFriends));
		
        if(userFriends.size()==0)
        {
			lvUserAttendingEvent.setVisibility(View.GONE);
			tvAttendingTitle.setVisibility(View.GONE);
        }
    	
        TabsActivity.btnBack.setVisibility(View.VISIBLE);
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar_empty);
        TabsActivity.tvTitle.setText(getResources().getString(R.string.event_details));
        TabsActivity.tvTitle.setVisibility(View.VISIBLE);
        TabsActivity.btnLogout.setVisibility(View.VISIBLE);
        TabsActivity.btnLogout.setBackgroundResource(R.drawable.logout_btn);
        TabsActivity.rlCommonTitleBar.invalidate();
        if(friendAdapter != null)
        	friendAdapter.refresh(userFriends);
        
        if(objImageAdapter!=null)
        	objImageAdapter.refresh(vctEventImageAdpter);
        
        if(objCustomFriendAdapter!=null)
        	objCustomFriendAdapter.refresh(vecFriends);
        
        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				TabGroupActivity pActivity = (TabGroupActivity)EventDetails.this.getParent();
				pActivity.finishFromChild(EventDetails.this);
			}
		});
        
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
		    					Intent intent = new Intent(EventDetails.this, Login.class);
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

	
	
	private class LoadEventDetails extends AsyncTask<Void,Void,Boolean>{

		@Override
		protected void onPreExecute() {
			progressDialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			EventsBusinessLayer evbl = new EventsBusinessLayer();
			AppConstants.vctEventsDetails = new ArrayList();
			Log.i(LOGTAG, "Loading event details...");
			try {
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_DETAILS+eventId);
			} catch (Exception e) {
				Log.e(LOGTAG, "Error in Loading event details.");
				e.printStackTrace();
				return false;
			}
			Object eventsDetailsObjects = jsonHelper.parse(element, EventsDetailsWrapper.class);
			if(eventsDetailsObjects!=null){
			EventsDetailsData tmpDetails = ((EventsDetailsWrapper)eventsDetailsObjects).getEvent();
				AppConstants.vctEventsDetails.add(tmpDetails);
				evbl.InsertIntoEventDetails(tmpDetails);
					for(int i=0;i<((EventsDetailsWrapper)eventsDetailsObjects).getEvent().attending.size();i++)
					{
						Attending tmpAttending = new Attending();
						tmpAttending = ((EventsDetailsWrapper)eventsDetailsObjects).getEvent().attending.get(i);
						tmpAttending.eventID = tmpDetails.id;
						evbl.InsertAttending(tmpAttending);
					}
					Log.i(LOGTAG, "Loading event details successfully completed.");
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			if(!result){
				 DialogUtility.showConnectionErrorDialog(EventDetails.this.getParent());
			}
				llMapBtn.setVisibility(View.VISIBLE);
				vctEventDesc = eventsBL.geteventDetails(eventId);
				for(int i = 0; i<vctEventDesc.size(); i++)
			        {   
						businessId=vctEventDesc.get(i).business;
//						final String strBusinessName=vctEventDesc.get(i).business.businessname;
						final String event_ticket_url=vctEventDesc.get(i).tickets.toString();
						ivPhoto.setId(vctEventDesc.get(i).eventid);
				    	tvEventDetails.setText(vctEventDesc.get(i).eventname.toString());
				    	tvDetails.setText(vctEventDesc.get(i).description.toString().replace("\r", ""));
				    	tvDate.setText(vctEventDesc.get(i).event_start_date.toString());
				    	tvbusinessname.setText(getResources().getString(R.string.by)+vctEventDesc.get(i).BusinessName.toString());
				    	if(vctEventDesc.get(i).event_start_time.toString().length()>5){
				    		tvTime.setText(vctEventDesc.get(i).event_start_time.toString().subSequence(0, 5)+"  "+vctEventDesc.get(i).price);
				    	}else{
				    		tvTime.setText(vctEventDesc.get(i).event_start_time.toString()+"  "+vctEventDesc.get(i).price);
				    	}
				    	if(!(vctEventDesc.get(i).EventAddress+vctEventDesc.get(i).EventCity+vctEventDesc.get(i).EventState+vctEventDesc.get(i).EventCountry).equals("")){
				    		tvEventLocation.setText(vctEventDesc.get(i).EventAddress+","+vctEventDesc.get(i).EventCity+","+vctEventDesc.get(i).EventState+","+vctEventDesc.get(i).EventLocationZip+","+vctEventDesc.get(i).EventCountry);
				    	}else{
				    		tvEventLocation.setVisibility(View.GONE);
				    	}
				    	tvSeeMoreEvents.setId(businessId);
				    	strEventName=vctEventDesc.get(i).eventname.toString();
				    	strEventDetail=getResources().getString(R.string.start_date)+vctEventDesc.get(i).event_start_date.toString()+
				    			getResources().getString(R.string.start_time)+vctEventDesc.get(i).event_start_time.toString()+
				    			getResources().getString(R.string.event_price)+vctEventDesc.get(i).price;
				    	if(!vctEventDesc.get(i).image.equals(AppConstants.NOIMAGE))
				    	{
				    		if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+vctEventDesc.get(i).image, ivPhoto,100,100,vctEventDesc.get(i).imageId,vctEventDesc.get(i).imageDateUpdated)){
				    			eventDetailsProgressBar.setVisibility(View.GONE);
					    		ivPhoto.setBackgroundResource(R.drawable.no_image_event);
				    		}else{
					    		strEventImage=AppConstants.IMAGE_HOST_URL+vctEventDesc.get(i).image;
					    		eventDetailsProgressBar.setVisibility(View.GONE);
				    		}
				    	}
				    	else
				    	{
				    		eventDetailsProgressBar.setVisibility(View.GONE);
				    		ivPhoto.setBackgroundResource(R.drawable.no_image_event);
				    	}
				    
				    	llMapBtn.setOnClickListener(new OnClickListener()
				    	{
							@Override
							public void onClick(View v) 
							{
								ConnectivityManager connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
								boolean connected = (connec.getActiveNetworkInfo() != null &&
													 connec.getActiveNetworkInfo().isAvailable() &&
													 connec.getActiveNetworkInfo().isConnected());
								if(!connected)
								{
									internetPopup =  new Builder(EventDetails.this.getParent());
									internetPopup.setMessage(EventDetails.this.getParent().getResources().getString(R.string.network_unavailable_for_map));
									alert=internetPopup.create();
									internetPopup.setNegativeButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() 
									{
							            public void onClick(DialogInterface dialog, int which) 
							            {             	
							            	alert.dismiss();
							            }
									});	
									internetPopup.show();
								}else{   	  	
								Intent intent = new Intent(EventDetails.this, EventLocationOnMap.class);
					            intent.putExtra(FROMACTIVITY, HOME);
					            intent.putExtra(GEOPOINT, vctEventDesc.get(0).EventGeoCode);
					            intent.putExtra(STRTO, DISPLAYLOCATION);
					            TabGroupActivity pActivity = (TabGroupActivity)EventDetails.this.getParent();
					            pActivity.startChildActivity(MYMAPACTIVITY, intent);	
								}
							}
						});
				    	
				    	tvSeeMoreEvents.setOnClickListener(new OnClickListener()
				    	{
							@Override
							public void onClick(View v)
							{
								Intent inttentMoreEvent = new Intent(EventDetails.this, BusinessPalender.class);
//								inttentMoreEvent.putExtra("friendname", strBusinessName);
								inttentMoreEvent.putExtra(BUSINESSID, v.getId());
								overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
								TabGroupActivity pActivity = (TabGroupActivity)EventDetails.this.getParent();
								pActivity.startChildActivity(SEARCHEDEVENT, inttentMoreEvent);
							}
						});
				    	
				    	ivBuyTicket.setOnClickListener(new OnClickListener()
				        {
							@Override
							public void onClick(View v)
							{
								Intent inttentMoreEvent = new Intent(EventDetails.this, BuyTicket.class);
								inttentMoreEvent.putExtra(EVENTTICKET, event_ticket_url);
								overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
								TabGroupActivity pActivity = (TabGroupActivity)EventDetails.this.getParent();
								pActivity.startChildActivity(SEARCHEDEVENT, inttentMoreEvent);
							}
						});
				    	
				        llStar.setOnClickListener(new OnClickListener()
						{			
							@Override
							public void onClick(View v) 
							{
								LayoutInflater inflater = getLayoutInflater();
								LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.category_popup, null);
								popup.setBackgroundColor(Color.TRANSPARENT);
								Button btnCompany 	= (Button)popup.findViewById(R.id.btnCompany);
								Button btnEvent 	= (Button)popup.findViewById(R.id.btnEvent);
								EventsBusinessLayer ebl = new EventsBusinessLayer();
								final UserAccountBusinessLayer uabl = new UserAccountBusinessLayer();
								TextView dialogText = (TextView) popup.findViewById(R.id.add_favorite_text);
								
								if(ebl.isEventAttendedByUser(eventId, AppConstants.USER_ID) && uabl.isBusinessIsInUserFavorites(businessId, AppConstants.USER_ID)){
									dialogText.setText(R.string.all_added_to_favorite);
								}else{
									dialogText.setText(R.string.add_to_favorite);
								}
								if(ebl.isEventAttendedByUser(eventId, AppConstants.USER_ID)){
									btnEvent.setVisibility(View.GONE);
								}
								
								if(uabl.isBusinessIsInUserFavorites(businessId, AppConstants.USER_ID)){
									btnCompany.setVisibility(View.GONE);
								}
								
								btnCompany.setOnClickListener(new OnClickListener()
								{					
									@Override
									public void onClick(View v) 
									{
										int duration = Toast.LENGTH_SHORT;
										CharSequence text="";
										int status = Ga2ooJsonParsers.addBusinessToUser(AppConstants.USER_ID,businessId);

										if(status>0)
										{
											Business tmpBusiness = new Business();
											tmpBusiness.businessid = businessId;
											tmpBusiness.businessname= " ";
											tmpBusiness.useraddedbusinessid = 0;
											tmpBusiness.date_updated = "0";
											uabl.InsertUserBusiness(tmpBusiness);
											text = getResources().getString(R.string.company_added_successfully); 
											Toast toast = Toast.makeText(context, text, duration);
											toast.show();
										}
										else if(status==0)
										{
											 text = getResources().getString(R.string.compamy_already_in_your_favorite);    
											 Toast toast = Toast.makeText(context, text, duration);
												toast.show();
										}else if(status==-1){
											DialogUtility.showConnectionErrorDialog(EventDetails.this.getParent());
										}
										customDialog.dismiss();
									}
								});
								
								btnEvent.setOnClickListener(new OnClickListener()
								{					
									@Override
									public void onClick(View v) 
									{
										int duration = Toast.LENGTH_SHORT;
										CharSequence text="";
										customDialog.dismiss();
										int status = Ga2ooJsonParsers.addEventToUser(AppConstants.USER_ID,eventId);
										if(status>0)
										{
											text = getResources().getString(R.string.event_successfully_added);    
											eventsBL=new EventsBusinessLayer();
											eventsBL.addToAttending(eventId, AppConstants.USER_ID);
											userActBL.insertUserFavorites(AppConstants.USER_ID, eventId,status);
											Toast toast = Toast.makeText(context, text, duration);
											toast.show();
										}
										else if(status==0)
										{
											 text = getResources().getString(R.string.event_already_in_palendar);   
											 Toast toast = Toast.makeText(context, text, duration);
											 toast.show();
										}else if(status==-1){
											DialogUtility.showConnectionErrorDialog(EventDetails.this.getParent());
										}
									 }
								});
								customDialog = new PopUpDailog(getParent(), popup, 220, LayoutParams.WRAP_CONTENT);
								customDialog.show();
							}
						});
				        
				        ivStar.setOnClickListener(new OnClickListener()
						{			
							@Override
							public void onClick(View v) 
							{
								LayoutInflater inflater = getLayoutInflater();
								LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.category_popup, null);
								popup.setBackgroundColor(Color.TRANSPARENT);
								Button btnCompany 	= (Button)popup.findViewById(R.id.btnCompany);
								Button btnEvent 	= (Button)popup.findViewById(R.id.btnEvent);
								
								btnCompany.setOnClickListener(new OnClickListener()
								{					
									@Override
									public void onClick(View v) 
									{
										int duration = Toast.LENGTH_SHORT;
										CharSequence text="";
										int status = Ga2ooJsonParsers.addBusinessToUser(AppConstants.USER_ID,businessId);
										if(status>0)
										{
											text = getResources().getString(R.string.company_added_successfully);   
											Toast toast = Toast.makeText(context, text, duration);
											toast.show();
										}
										else if(status==0)
										{
											 text = getResources().getString(R.string.compamy_already_in_your_favorite);    
											 Toast toast = Toast.makeText(context, text, duration);
												toast.show();
										}else if(status==-1){
											DialogUtility.showConnectionErrorDialog(EventDetails.this.getParent());
										}
										
										customDialog.dismiss();
									}
								});
								
								btnEvent.setOnClickListener(new OnClickListener()
								{					
									@Override
									public void onClick(View v) 
									{
										int duration = Toast.LENGTH_SHORT;
										CharSequence text="";
										customDialog.dismiss();
										int status = Ga2ooJsonParsers.addEventToUser(AppConstants.USER_ID,eventId);
										if(status>0)
										{
											text = getResources().getString(R.string.event_successfully_added);     
											eventsBL=new EventsBusinessLayer();
											eventsBL.addToAttending(eventId, AppConstants.USER_ID);
											userActBL.insertUserFavorites(AppConstants.USER_ID, eventId,status);
											Toast toast = Toast.makeText(context, text, duration);
											toast.show();
										}
										else if(status==0)
										{
											 text = getResources().getString(R.string.event_already_in_palendar);  
											 Toast toast = Toast.makeText(context, text, duration);
												toast.show();
										}else if(status == -1){
											DialogUtility.showConnectionErrorDialog(EventDetails.this.getParent());
										}
										
									 }
								});
								customDialog = new PopUpDailog(getParent(), popup, 220, LayoutParams.WRAP_CONTENT);
								customDialog.show();
							}
						});

						llShare.setOnClickListener(new OnClickListener() 
						{
							@Override
							public void onClick(View v) 
							{
								//showPopup(eventId);
								showSharePopup(getApplicationContext(), eventId, strEventName, strEventDetail);
							}
						});
						ivShare.setOnClickListener(new OnClickListener() 
						{
							@Override
							public void onClick(View v) 
							{
								//showPopup(eventId);
								showSharePopup(getApplicationContext(), eventId, strEventName, strEventDetail);
							}
						});

			        }
			
			
			super.onPostExecute(result);
		}
		
	}
	/*
	 * start of Method showImageGallery for showing image gallery.
	 */
	public void showImageGallery(int eventId)
	{
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout galleryPopup=(LinearLayout) inflater.inflate(R.layout.gallery, null);
		Gallery gallery=(Gallery)galleryPopup.findViewById(R.id.gallery);
		ImageView ivClose=(ImageView)galleryPopup.findViewById(R.id.ivClose);
		vctEventImages		= new ArrayList<EventImages>();
		vctEventImages		= eventsBL.getAllEventImages(eventId);
		vctEventImageAdpter=new ArrayList<EventImages>();
		if(vctEventImages.size()!=0)
		{
			if(!vctEventImages.get(0).strEventImage1.equals(AppConstants.NOIMAGE))
			{
			EventImages objEventImageAdapter	= new EventImages();
			objEventImageAdapter.Image			= vctEventImages.get(0).strEventImage1;
			vctEventImageAdpter.add(objEventImageAdapter);
			}
			
			if(!vctEventImages.get(0).strEventImage2.equals(AppConstants.NOIMAGE))
			{
			EventImages objEventImageAdapter	= new EventImages();
			objEventImageAdapter.Image			= vctEventImages.get(0).strEventImage2;
			vctEventImageAdpter.add(objEventImageAdapter);
			}
			
			if(!vctEventImages.get(0).strEventImage3.equals(AppConstants.NOIMAGE))
			{
			EventImages objEventImageAdapter	= new EventImages();
			objEventImageAdapter.Image			= vctEventImages.get(0).strEventImage3;
			vctEventImageAdpter.add(objEventImageAdapter);
			}
			
			if(!vctEventImages.get(0).strEventImage4.equals(AppConstants.NOIMAGE))
			{
			EventImages objEventImageAdapter	= new EventImages();
			objEventImageAdapter.Image			= vctEventImages.get(0).strEventImage4;
			vctEventImageAdpter.add(objEventImageAdapter);
			}
		}
		ivClose.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				customDialog.dismiss();
			}
		});
		
		gallery.setAdapter(new ImageAdapter(vctEventImageAdpter));
		gallery.setSpacing(10);
		customDialog = new PopUpDailog(getParent(), galleryPopup, 280, LayoutParams.FILL_PARENT);
		if(vctEventImageAdpter.size()!=0)
		{
		customDialog.show();
		}
	}
	/*
	 * End of Method showImageGallery.
	 */
	
	
	
	/*
	 * Show Delete popup
	 */
	public void showPopup(final int eventID) 
	{
		LayoutInflater inflater = this.getLayoutInflater();
		View mView = inflater.inflate(R.layout.event_popup,(ViewGroup) findViewById(R.id.dontRecordPopup));
		mPopupWindow = new PopupWindow(mView,android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, false);
		mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		mPopupWindow.showAtLocation(mView, Gravity.BOTTOM, 10, 0);
		

		Button btnSpecificFri = (Button) mView.findViewById(R.id.btnSpecificFri);
		btnSpecificFri.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v)
			{
				showAllGa2ooFriendsPopup();
				mPopupWindow.dismiss();
			}
		});
		
		Button btnGoFri = (Button) mView.findViewById(R.id.btnGoFri);
		btnGoFri.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v)
			{
				vecFriends = new ArrayList<UserFriend>();
				vecFriends.clear();
				vecFriends=friendBL.getAllFriends();
				if(vecFriends!=null && vecFriends.size()!=0)
				{
					toAllFriend=new String[vecFriends.size()];
					for(int i=0;i<vecFriends.size();i++)
					{
						toAllFriend[i]=vecFriends.get(i).email;
					}
				}
				if(toAllFriend!=null)
				{
					Intent int_email = new Intent(Intent.ACTION_SEND);
			    	int_email.setType(TEXTPLAIN); 
			    	int_email.setType(IMAGE); 
			    	int_email.putExtra(Intent.EXTRA_STREAM, Uri.parse(strEventImage));
					int_email.putExtra(Intent.EXTRA_SUBJECT, "");
					int_email.putExtra(Intent.EXTRA_TEXT,strEventDetail);
					int_email.putExtra(Intent.EXTRA_EMAIL, toAllFriend);
					
					List<ResolveInfo> list = EventDetails.this.getPackageManager().queryIntentActivities(int_email, PackageManager.MATCH_DEFAULT_ONLY);					
					int index = Tools.getMailIntentIndex(list);
					if(index != -1)
					{
						ResolveInfo ri = (ResolveInfo) list.get(index);
						int_email.setClassName(ri.activityInfo.packageName, ri.activityInfo.name);
					}
					
					startActivity(int_email);
				}
				mPopupWindow.dismiss();
			}
		});
		Button btnEmail = (Button) mView.findViewById(R.id.btnEmail);
		btnEmail.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v)
			{
				Intent int_email = new Intent(Intent.ACTION_SEND);
		    	int_email.setType(TEXTPLAIN); 
		    	int_email.setType(IMAGE); 
		    	int_email.putExtra(Intent.EXTRA_STREAM, Uri.parse(strEventImage));
				String[] to = {"milean@gmail.com"};
				int_email.putExtra(Intent.EXTRA_SUBJECT, "");
				int_email.putExtra(Intent.EXTRA_TEXT,strEventDetail);
				int_email.putExtra(Intent.EXTRA_EMAIL, to);
				
				List<ResolveInfo> list = EventDetails.this.getPackageManager().queryIntentActivities(int_email, PackageManager.MATCH_DEFAULT_ONLY);					
				int index = Tools.getMailIntentIndex(list);
				if(index != -1)
				{
					ResolveInfo ri = (ResolveInfo) list.get(index);
					int_email.setClassName(ri.activityInfo.packageName, ri.activityInfo.name);
				}
				
				startActivity(int_email);
				mPopupWindow.dismiss();
			}
		});
		Button btnSMS = (Button) mView.findViewById(R.id.btnSMS);
		btnSMS.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v)
			{
				Intent sendIntent= new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra(SMSBODY, "smsBody");
				sendIntent.putExtra(ADRESS, "139");
				sendIntent.setType(MMSSMS);
				startActivity(sendIntent); 
				mPopupWindow.dismiss();
			}
		});
		
		Button btnCancel = (Button) mView.findViewById(R.id.btnCancel);
		//Button btnFacebook = (Button) mView.findViewById(R.id.btnFacebook);
		btnCancel.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				SharedPreferences savedSession = context.getSharedPreferences(FACEBOOKSESSION, Context.MODE_PRIVATE);
				String strAccessToken = savedSession.getString(ACESSTOKEN, null);
				
				//Log.i("yes",""+eventID);
				//vctEventsDetails=eventsBL.getPostEventDetails(eventID);
				//String strFacebookWallPost = "Event Name:"+vctEventsDetails.get(0).EventName;
				
//				if(strAccessToken == null)
//				{
//					Intent facebook=new Intent(EventDetails.this,Example.class);
//					facebook.putExtra("Wall", strFacebookWallPost);
//					startActivity(facebook);
//					mPopupWindow.dismiss();
//				}
//				else
//				{				
//					Facebook mFacebook = new Facebook("136463256439740");
//					Log.i("FaceBook", "FACEBOOkkkkkkkkkkkkkkkkkkkkkkkk");
//			       	AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(mFacebook);
//			       	Log.i("FaceBook", "FACEBOOKKKKKKKKKKKKKKKK");
//			       	mFacebook.setAccessToken(savedSession.getString("access_token", null));
//			       	mFacebook.setAccessExpires(savedSession.getLong("expires_in", 0));
//			       
//			       	Bundle params = new Bundle();
//	                params.putString("method", "stream.publish");
//	            	params.putString("message", strFacebookWallPost);
//	                
//	            	mAsyncRunner.request(null, params, "POST",new SampleRequestListenerNew());
	            	mPopupWindow.dismiss();
//				}
			}
		});

	}
	
	@Override
	public void onBackPressed() 
	{
		if(mPopupWindow!=null &&mPopupWindow.isShowing())
		{
			mPopupWindow.dismiss();
		}
		else
			finish();
	}

	public void showAllGa2ooFriendsPopup()
	{
		LayoutInflater inflater = getLayoutInflater();
		RelativeLayout popup=(RelativeLayout) inflater.inflate(R.layout.all_friends_popup, null);
		
		ImageView ivClose = (ImageView)popup.findViewById(R.id.ivClose);
		Button btnDone	  = (Button)popup.findViewById(R.id.btnDone);
		lvFriendsList = (ListView)popup.findViewById(R.id.lvFriendsList);
		lvFriendsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lvFriendsList.setCacheColorHint(0);
		lvFriendsList.setFadingEdgeLength(0);
		ivClose.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
			}
		});
		vecFriends = new ArrayList<UserFriend>();
		vecFriends.clear();
		vecFriends=friendBL.getAllFriends();
		if(vecFriends.size()!=0)
		{
			lvFriendsList.setAdapter(new CustomFriendAdapter(vecFriends));
		}
		vctEmails.clear();
		btnDone.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(vctEmails.size()!=0&&vctEmails!=null)
				{
					to=new String[vctEmails.size()];
					for (int i = 0; i <vctEmails.size(); i++)
					{
						to[i]=vctEmails.get(i);
					}
					if(to.length!=0&&to!=null)
					{
						Intent int_email = new Intent(Intent.ACTION_SEND);
				    	int_email.setType(TEXTPLAIN); 
				    	int_email.setType(IMAGE); 
				    	int_email.putExtra(Intent.EXTRA_STREAM, Uri.parse(strEventImage));
						int_email.putExtra(Intent.EXTRA_SUBJECT, "");
						int_email.putExtra(Intent.EXTRA_TEXT,strEventDetail);
						int_email.putExtra(Intent.EXTRA_EMAIL, to);
						List<ResolveInfo> list = EventDetails.this.getPackageManager().queryIntentActivities(int_email, PackageManager.MATCH_DEFAULT_ONLY);					
						int index = Tools.getMailIntentIndex(list);
						if(index != -1)
						{
							ResolveInfo ri = (ResolveInfo) list.get(index);
							int_email.setClassName(ri.activityInfo.packageName, ri.activityInfo.name);
						}
						
						startActivity(int_email);
					}
				}
				customDialog.dismiss();
			}
		});
		customDialog = new PopUpDailog(getParent(), popup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}
	
	public class CustomFriendAdapter extends BaseAdapter 
	{
		private List<UserFriend> vecFreiend;
		public CustomFriendAdapter(List<UserFriend> vecFreiend)
		{
			this.vecFreiend = vecFreiend;
		}
		
		@Override
		public int getCount()
		{
			if(vecFreiend != null)
				return vecFreiend.size();
			return 0;
		}
		
		public void refresh(List<UserFriend> vecFreiend)
		{
			this.vecFreiend = vecFreiend;
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
			final UserFriend objFriend = vecFreiend.get(position);
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.friends_list_cell, null);
			final ImageView ivCheckBox=(ImageView)convertView.findViewById(R.id.ivCheckBox);
			ivCheckBox.setVisibility(ImageView.VISIBLE);
			ImageView ivFriendsListIcon = (ImageView) convertView.findViewById(R.id.ivFriendsListIcon);
			ProgressBar friend_list_progress_bar=(ProgressBar)convertView.findViewById(R.id.friend_list_progress_bar);
			TextView tvFriendsListName = (TextView) convertView.findViewById(R.id.tvFriendsListName);
			ivCheckBox.setId(position);
			ivCheckBox.setTag(UNSELECTED);

			if(!objFriend.imagesrc.equals(AppConstants.NOIMAGE))
			{
				String friendImageUrl=AppConstants.IMAGE_HOST_URL+objFriend.imagesrc;
				if(!objDrawableManager.fetchDrawableOnThread(friendImageUrl, ivFriendsListIcon,70,50,objFriend.image_id,objFriend.isImageUpdated)){
					friend_list_progress_bar.setVisibility(View.GONE);
					ivFriendsListIcon.setImageResource(R.drawable.no_image_smal_event);
				}
			}
			else
			{
				friend_list_progress_bar.setVisibility(View.GONE);
				ivFriendsListIcon.setImageResource(R.drawable.no_image_smal_event);
			}
			
			convertView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					if(UNSELECTED.equals(ivCheckBox.getTag()))
					{
						String email=objFriend.email;
						vctEmails.add(email);
						ivCheckBox.setTag(SELECTED);
						ivCheckBox.setImageResource(R.drawable.btn_check_on);
					}
					
					else if(SELECTED.equals(ivCheckBox.getTag()))
					{
						String email=objFriend.email;
						ivCheckBox.setTag(UNSELECTED);
						ivCheckBox.setImageResource(R.drawable.btn_check_off);
						vctEmails.remove(email);
					}
				}
			});
			tvFriendsListName.setText(objFriend.firstname+" "+objFriend.lastname);
			return convertView;
		}
	}

	/*
	 * Adapter class for ListView which shows list of friends 
	 */
	public class CustomFriendsAdapter extends BaseAdapter 
	{
		private List<UserFriend> vecAdapterFriends;
		
		public CustomFriendsAdapter(List<UserFriend> vecAdapterFriends)
		{
			this.vecAdapterFriends = vecAdapterFriends;
		}
		
		@Override
		public int getCount()
		{
			if(vecAdapterFriends != null)
				return vecAdapterFriends.size();
			return 0;
		}

		@Override
		public Object getItem(int position)
		{
			return position;
		}

	   public void refresh(List<UserFriend> vecAdapterFriends)
		{
			this.vecAdapterFriends = vecAdapterFriends;
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
			
			final  UserFriend friend = vecAdapterFriends.get(position);
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.userfriendattendingevent, null);
			ImageView ivFriendsIcon = (ImageView) convertView.findViewById(R.id.userImage);
			TextView tvFriendsName = (TextView) convertView.findViewById(R.id.txtUserName);
			ProgressBar eventDetails_friend_ProgressBar=(ProgressBar)convertView.findViewById(R.id.eventdetail_frieds_progress_bar);
			if(friend.FirstName1!="")
				{
				  if(!friend.Image1.equals(AppConstants.NOIMAGE))
				  {
					 eventDetails_friend_ProgressBar.setVisibility(View.GONE);
					 friendImageUrl=AppConstants.IMAGE_HOST_URL+friend.Image1;
					 if(!objDrawableManager.fetchDrawableOnThread(friendImageUrl, ivFriendsIcon,40,35,friend.image_id,friend.isImageUpdated)){
						 eventDetails_friend_ProgressBar.setVisibility(View.GONE);
						  ivFriendsIcon.setImageResource(R.drawable.no_image_smal_event); 
					 }
				  }
				  else
				  {
					  eventDetails_friend_ProgressBar.setVisibility(View.GONE);
					  ivFriendsIcon.setImageResource(R.drawable.no_image_smal_event);
				  }
					tvFriendsName.setText(friend.FirstName1+" "+friend.LastName1);
				}
		
			convertView.setOnClickListener(new OnClickListener()
			{			
				@Override
				public void onClick(View v) 
				{
					Intent intent = new Intent(EventDetails.this,FriendsProfile.class);
					intent.putExtra(USERFINDID,friend.ID1);
					
					TabGroupActivity pActivity = (TabGroupActivity)EventDetails.this.getParent();
					pActivity.startChildActivity(FRIENDSPROFILE, intent);
				}
			});
		
			return convertView;
		}
	}
	
	/*
	 * adapter class for image gallery
	 */
	
	public class ImageAdapter extends BaseAdapter 
	{
		private List<EventImages> vctEventImagesAdapter;
		
	    public ImageAdapter(List<EventImages> vctEventImages)
	    {
	    	this.vctEventImagesAdapter=vctEventImages;
	    }

	    public int getCount()
	    {
	    	if(vctEventImagesAdapter != null)
				return vctEventImagesAdapter.size();
			return 0;
	    }

	    public Object getItem(int position)
	    {
	        return position;
	    }

	 
	    
	    public long getItemId(int position)
	    {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent)
	    {
	    	final EventImages images=vctEventImagesAdapter.get(position);
	    	convertView=(LinearLayout)getLayoutInflater().inflate(R.layout.galleryview, null);
	        ImageView imageview=(ImageView)convertView.findViewById(R.id.imageview);
	        ProgressBar eventgalleryProgressBar=(ProgressBar)convertView.findViewById(R.id.galleryprogressbar);
	        if(!images.Image.equals(AppConstants.NOIMAGE))
	        {
	        	if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+images.Image, imageview, 220,210)){
	        		eventgalleryProgressBar.setVisibility(View.GONE);
		        	imageview.setImageResource(R.drawable.no_image_event);
	        	}
	        }
	        else
	        {
	        	eventgalleryProgressBar.setVisibility(View.GONE);
	        	imageview.setImageResource(R.drawable.no_image_event);
	        }
	        convertView.setLayoutParams(new Gallery.LayoutParams(220, 210));
	        imageview.setScaleType(ImageView.ScaleType.FIT_XY);

	    	return convertView;
	    }
	    public void refresh(List<EventImages> vctEventImages)
		{
			this.vctEventImagesAdapter = vctEventImages;
			this.notifyDataSetChanged();
		}
	}
	/*
	 * end of image adapter class
	 */
	
	
	
	@Override
	protected void onPause()
	{
		super.onPause();
		rlEventImage.removeAllViews();
		llFriend.removeAllViews();
		
		if(friendAdapter != null)
			friendAdapter.refresh(new ArrayList<UserFriend>());
		if(objImageAdapter!=null)
        	objImageAdapter.refresh(new ArrayList<EventImages>());
		if(objCustomFriendAdapter!=null)
        	objCustomFriendAdapter.refresh(new ArrayList<UserFriend>());
		if(objDrawableManager != null)
			objDrawableManager.clear();
	}

	@Override
	protected void onStop() 
	{
		if(friendAdapter != null)
			friendAdapter.refresh(new ArrayList<UserFriend>());
		if(objImageAdapter!=null)
        	objImageAdapter.refresh(new ArrayList<EventImages>());
		if(objCustomFriendAdapter!=null)
        	objCustomFriendAdapter.refresh(new ArrayList<UserFriend>());
		if(objDrawableManager != null)
			objDrawableManager.clear();
		super.onStop();
	}
	
}
