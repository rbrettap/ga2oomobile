package com.ga2oo.palendar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.ga2oo.palendar.businesslayer.FriendsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.DialogUtility;
import com.ga2oo.palendar.objects.EventsDetails;
import com.ga2oo.palendar.objects.Notifications;
import com.ga2oo.palendar.objects.UserRecommendationObject;
import com.ga2oo.jsonparsers.EventAddDeleteWrapper;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;
import com.ga2oo.jsonparsers.NotificationsWrapper;
import com.ga2oo.jsonparsers.RecommendationUseraccountWrapper;
import com.ga2oo.parsing.net.JsonHttpHelper;

public class UpcomingEvents  extends Activity 
{
	private static final String CHECKED = "checked";

	private static final String CHECK = "check";

	private static final String ALL = "all";

	public static final String LOGTAG = "UpcomingEventsScreen";
	
	private Button btnAll, btnRecommendations, btnNotifications,btnDelete, cancel;
	private LinearLayout llRecommendationEvents,llAllEvents, llNotifiction,llDeleteClearall;
	private boolean isEditClick=false;
	private TextView tvTitle,tvNoanyNotification,tvRecommendationTitle,tvAllNoanyNotification,tvNoanyRecommendation;
	public  ListView lvFriendsList;
	public PullToRefreshListView lvNotifications,lvNotificationAll,lvRecommendation;
	private ScrollView svEvents;
	public List<EventsDetails> vctEventsDetails,vctAllEventsDetails;
	private List<Notifications> vctUserNotificatios;
	private List<UserRecommendationObject> vctUserrecommendation;
	private int stateno=0;
	public boolean isAllEvents=true,isRecommendations=false;
	private DrawableManager objDrawableManager;
	private UserAccountBusinessLayer userAccBL;
	private FriendsBusinessLayer friendBL;
	private CustomNotificationAdapter objCustomNotificationAdapter;
	private int status;
	private JsonElement element;
	private JsonHttpHelper jsonHelper;
	private EditText searchEdit;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		 
		super.onCreate(savedInstanceState);
    }

	 @Override
	    public void onResume()
	    {
	    	super.onResume();
	    	setContentView(R.layout.upcoming_events_view);
			objDrawableManager	= new DrawableManager();
			userAccBL			= new UserAccountBusinessLayer();
			friendBL			= new FriendsBusinessLayer();
			vctUserNotificatios	= new ArrayList<Notifications>();
			vctUserrecommendation=new ArrayList<UserRecommendationObject>();
			
			lvNotifications		=  new PullToRefreshListView(this);
			lvNotificationAll	=  new PullToRefreshListView(this);
			lvRecommendation	=  new PullToRefreshListView(this);
			tvRecommendationTitle= new TextView(this);
			tvAllNoanyNotification=new TextView(this);
			svEvents			=(ScrollView)findViewById(R.id.svEvents);
			btnAll				= (Button)findViewById(R.id.btnAll);
			btnRecommendations 	= (Button)findViewById(R.id.btnRecommendations);
			btnNotifications	= (Button)findViewById(R.id.btnNotifications);
			btnDelete 			= (Button)findViewById(R.id.btnDelete);
		    tvTitle				= (TextView)findViewById(R.id.tvtitle);
		    tvNoanyNotification	= (TextView)findViewById(R.id.tvNoanyNotification);
		    tvNoanyRecommendation=(TextView)findViewById(R.id.tvNoanyRecommendation);
			llRecommendationEvents = (LinearLayout)findViewById(R.id.llRecommendationEvents);
			llNotifiction 		= (LinearLayout)findViewById(R.id.llNotification);
			llAllEvents			=(LinearLayout)findViewById(R.id.llAllEvents);
			llDeleteClearall 	= (LinearLayout)findViewById(R.id.llDeleteClearall);
			searchEdit 			= (EditText) findViewById(R.id.etSearchUpcoming);
			cancel 				= (Button) findViewById(R.id.btn_cancel);
			
			lvNotifications.setCacheColorHint(0);
			lvNotifications.setScrollbarFadingEnabled(true);
			
			lvNotificationAll.setCacheColorHint(0);
			lvNotificationAll.setScrollbarFadingEnabled(true);
			
			tvRecommendationTitle.setTextSize(16);
			tvRecommendationTitle.setTextColor(R.color.heading_color_dark);
			tvRecommendationTitle.setTypeface(null, Typeface.BOLD);
			tvRecommendationTitle.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,30));
			tvRecommendationTitle.setText(getResources().getString(R.string.recommendations));
			tvRecommendationTitle.setPadding(10, 0, 0,0);
			
			tvAllNoanyNotification.setTextSize(16);
			tvAllNoanyNotification.setTextColor(R.color.heading_color_dark);
			tvAllNoanyNotification.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,30));
			tvAllNoanyNotification.setText(getResources().getString(R.string.no_any_notification));
			tvAllNoanyNotification.setGravity(Gravity.CENTER);
			tvAllNoanyNotification.setPadding(10, 0, 0,0);
			
			lvRecommendation.setCacheColorHint(0);
			lvRecommendation.setScrollbarFadingEnabled(true);
			
			vctUserNotificatios	=	userAccBL.getUserNotifications();
			vctUserrecommendation=  userAccBL.getUserInboxMessage();
			
			jsonHelper = JsonHttpHelper.getInstance();
			
			btnDelete.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					final AlertDialog alertDialog = new AlertDialog.Builder(getParent()).create();
					
	                if(stateno == 0)
	                	alertDialog.setMessage(getResources().getString(R.string.are_you_sure_you_want_to_remove_all_events));
	                else if(stateno == 1)
	                	alertDialog.setMessage(getResources().getString(R.string.are_you_sure_you_want_to_remove_all_recommendations));
	                else
	                	alertDialog.setMessage(getResources().getString(R.string.are_you_sure_you_want_to_remove_all_notifications));
		 
	                alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() 
	                {
	                	public void onClick(DialogInterface dialog, int which) 
	                	{
	                		if(stateno==0)
	                			llAllEvents.removeAllViews();
	                		else if(stateno==1)
	                			llRecommendationEvents.removeAllViews();
	                		else 
	                			llNotifiction.removeAllViews();
	                		alertDialog.dismiss();
	                	}
	                });
					
	               alertDialog.show();
				}
			});
			
			svEvents.setOnTouchListener(new OnTouchListener() 
			{
				@Override
				public boolean onTouch(View v, MotionEvent event) 
				{
					if(stateno==0)
					{
						View view = (View) llAllEvents.getChildAt(2);
						int diff = (view.getTop()-(svEvents.getScrollY()));
						if(diff<0)
						{
							tvRecommendationTitle.setVisibility(View.GONE);
							tvTitle.setText(getResources().getString(R.string.recommendations));
							tvAllNoanyNotification.setVisibility(View.GONE);
						}
						else
						{
							tvAllNoanyNotification.setVisibility(View.VISIBLE);
							tvRecommendationTitle.setVisibility(View.VISIBLE);
							tvTitle.setText(getResources().getString(R.string.notifications));
						}
					}
					return false;
				}
			});
			btnAll.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					stateno=0;
					tvRecommendationTitle.setVisibility(View.VISIBLE);
					allEvents();
				}
			});
			
			btnRecommendations.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					stateno=1;
					recommendationsUpcomingE("");
				}
			});
			
			btnNotifications.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					stateno=2;
					notificationUpcomingE();
				}
			});
			allEvents();
	    	
	        TabsActivity.btnBack.setVisibility(View.VISIBLE);
	        TabsActivity.btnLogout.setVisibility(View.GONE);
	        TabsActivity.rlCommonTitleBar.invalidate();

	        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
	        {
				@Override
				public void onClick(View v) 
				{
					TabGroupActivity pActivity = (TabGroupActivity)UpcomingEvents.this.getParent();
					pActivity.finishFromChild(null);
				}
			});
	        
	        TabsActivity.btnLogout.setOnClickListener(new OnClickListener()
	        {
				@Override
				public void onClick(View v) 
				{
					if(isAllEvents)
					{
						int looplimit=1;
						looplimit=vctAllEventsDetails.size();		
						if(!isEditClick)
						{
							isEditClick=true;
							llDeleteClearall.setVisibility(View.VISIBLE);
							TabsActivity.btnLogout.setBackgroundResource(R.drawable.cancelbtn);
							for(int i=0; i<looplimit; i++)
							{
							   ImageView ivEditCheck  = new ImageView(UpcomingEvents.this);
							   ivEditCheck=  (ImageView)findViewById(1100+i);
							   ivEditCheck.setVisibility(View.VISIBLE);
							}
						}
						else
						{
							isEditClick=false;
							llDeleteClearall.setVisibility(View.GONE);
							TabsActivity.btnLogout.setBackgroundResource(R.drawable.edit);
							for(int i=0; i<looplimit; i++)
							{
							   ImageView ivEditCheck  = new ImageView(UpcomingEvents.this);
							   ivEditCheck  =(ImageView)findViewById(1100+i);
							   ivEditCheck.setVisibility(View.GONE);
							  
							}
						}
					}
					else if(isRecommendations)
					{
						int looplimit=1;
						
						looplimit=vctEventsDetails.size();
						if(!isEditClick)
						{
							isEditClick=true;
							llDeleteClearall.setVisibility(View.VISIBLE);
							
							TabsActivity.btnLogout.setBackgroundResource(R.drawable.cancelbtn);
							for(int i=0; i<looplimit; i++)
							{
							   ImageView ivEditCheck  = new ImageView(UpcomingEvents.this);
							   ivEditCheck=  (ImageView)findViewById(1100+i);
							   ivEditCheck.setVisibility(View.VISIBLE);
							}
						}
						else
						{
							isEditClick=false;
							llDeleteClearall.setVisibility(View.GONE);
							TabsActivity.btnLogout.setBackgroundResource(R.drawable.edit);
							for(int i=0; i<looplimit; i++)
							{
							   ImageView ivEditCheck  = new ImageView(UpcomingEvents.this);
							   ivEditCheck  =(ImageView)findViewById(1100+i);
							   ivEditCheck.setVisibility(View.GONE);
							  
							}
						}
					}
				}
	        });
	        
	        lvNotificationAll.setOnRefreshListener(new OnRefreshListener(){

				@Override
				public void onRefresh() {
					searchEdit.setText("");
					searchEdit.removeTextChangedListener(lvNotificationAll);
					new GetAllNotifications().execute();			
				}
	        	
	        });
			
			lvRecommendation.setOnRefreshListener(new OnRefreshListener(){

				@Override
				public void onRefresh() {
					searchEdit.setText("");
					searchEdit.removeTextChangedListener(lvRecommendation);
					new GetAllRecomendations().execute();
				}
				
			});
	        
	        searchEdit.addTextChangedListener(new TextWatcher(){

				@Override
				public void afterTextChanged(Editable arg0) {
					
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
			
				}

				@Override
				public void onTextChanged(CharSequence filter, int start,
						int before, int count) {
					switch(stateno){
					case(0):
						tvRecommendationTitle.setVisibility(View.VISIBLE);
						vctUserNotificatios = userAccBL.getUserFilteredNotifications(filter.toString());
						vctUserrecommendation = userAccBL.getFilteredUserInboxMessage(filter.toString());
						allEvents();
						break;
					case(1):
						vctUserrecommendation = userAccBL.getFilteredUserInboxMessage(filter.toString());
						recommendationsUpcomingE("");
						break;
					case(2):
						vctUserNotificatios = userAccBL.getUserFilteredNotifications(filter.toString());
						notificationUpcomingE();
						break;
					}
				}
	        	
	        });
	        
	        cancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					searchEdit.setText("");
					switch(stateno){
					case(0):
						tvRecommendationTitle.setVisibility(View.VISIBLE);
						searchEdit.removeTextChangedListener(lvNotificationAll);
						vctUserNotificatios = userAccBL.getUserNotifications();
						vctUserrecommendation = userAccBL.getUserInboxMessage();
						allEvents();
						break;
					case(1):
						searchEdit.removeTextChangedListener(lvNotifications);
						vctUserrecommendation = userAccBL.getUserInboxMessage();
						recommendationsUpcomingE("");
						break;
					case(2):
						searchEdit.removeTextChangedListener(lvRecommendation);
						vctUserNotificatios = userAccBL.getUserNotifications();
						notificationUpcomingE();
						break;
					}
				}
	        	
	        });
	       
	    }

	public void allEvents()
	{
		tvTitle.setText(getResources().getString(R.string.notifications));
		llAllEvents.setVisibility(View.VISIBLE);
		llAllEvents.removeAllViews();
		if(vctUserNotificatios.size()!=0)
		{
			lvNotificationAll.setAdapter(objCustomNotificationAdapter=new CustomNotificationAdapter(vctUserNotificatios));
			llAllEvents.addView(lvNotificationAll, new LayoutParams(LayoutParams.FILL_PARENT,107*vctUserNotificatios.size()+30));
		}
		else
		{
			llAllEvents.addView(tvAllNoanyNotification);
		}
		if(vctUserrecommendation!=null)
		{
			View view = new View(UpcomingEvents.this);
			view.setBackgroundResource(R.drawable.seperator);
			llAllEvents.addView(tvRecommendationTitle);
			llAllEvents.addView(view);
			lvRecommendation.setAdapter(new CustomRecommendationAdapter(vctUserrecommendation));
			llAllEvents.addView(lvRecommendation, new LayoutParams(LayoutParams.FILL_PARENT,60*vctUserrecommendation.size()+50));
		}
		isRecommendations=false;
		isAllEvents=true;
		isEditClick=false;
		TabsActivity.btnLogout.setBackgroundResource(R.drawable.edit);
		llDeleteClearall.setVisibility(View.GONE);
		
		btnAll.setBackgroundResource(R.drawable.all_btn_hover);
		btnRecommendations.setBackgroundResource(R.drawable.recommen_btn);
		btnNotifications.setBackgroundResource(R.drawable.notifications_btn);
		
		llRecommendationEvents.setVisibility(View.GONE);	
		llRecommendationEvents.removeAllViews();
		
		llNotifiction.setVisibility(View.GONE);	
		llNotifiction.removeAllViews();
	}

	public void recommendationsUpcomingE(String from)
	{
		llRecommendationEvents.removeAllViews();
		if(!from.equals(ALL))
		{
		isRecommendations=true;
		isAllEvents=false;
		isEditClick=false;
		TabsActivity.btnLogout.setBackgroundResource(R.drawable.edit);
		llDeleteClearall.setVisibility(View.GONE);
		
		btnAll.setBackgroundResource(R.drawable.all_btn);
		btnRecommendations.setBackgroundResource(R.drawable.recommen_btn_hover);
		btnNotifications.setBackgroundResource(R.drawable.notifications_btn);
		tvTitle.setText(getResources().getString(R.string.recommendations));
		
		llAllEvents.setVisibility(View.GONE);
		llAllEvents.removeAllViews();
		
		llNotifiction.setVisibility(View.GONE);
		llNotifiction.removeAllViews();
		
		llRecommendationEvents.setVisibility(View.VISIBLE);	
		
		llNotifiction.setVisibility(View.GONE);
		}
        if(vctUserrecommendation.size()!=0)
        {
			for( int i=0; i<vctUserrecommendation.size(); i++)
			{
				LinearLayout llEvents = (LinearLayout) getLayoutInflater().inflate(R.layout.upcoming_event_cell, null);
				llRecommendationEvents.addView(llEvents, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
				
				final ImageView ivEditCheck = (ImageView)llEvents.findViewById(R.id.ivEditCheck);
				ivEditCheck.setId(1100+i);
				ivEditCheck.setTag(CHECK);
	
				TextView tvEventName=(TextView)llEvents.findViewById(R.id.tvEventName);
				TextView tvStartDate=(TextView)llEvents.findViewById(R.id.tvEventStartDate);
				ImageView ivDelete=(ImageView)llEvents.findViewById(R.id.btnDelete);
				RelativeLayout rlEventImage=(RelativeLayout)llEvents.findViewById(R.id.rlEventImage);
				rlEventImage.setVisibility(View.GONE);
				ivDelete.setVisibility(View.GONE);
				
				tvEventName.setText(getResources().getString(R.string.from)+vctUserrecommendation.get(i).fromname);
				tvStartDate.setText(vctUserrecommendation.get(i).subject);
				
				ivEditCheck.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						String isCheck=v.getTag().toString();
						if(isCheck.equalsIgnoreCase(CHECK))
						{
							ivEditCheck.setBackgroundResource(R.drawable.checked);
							ivEditCheck.setTag(CHECKED);
						}
						else
						{
							ivEditCheck.setBackgroundResource(R.drawable.check);
							ivEditCheck.setTag(CHECK);
						}
					}
				});
				
				View view = new View(UpcomingEvents.this);
				view.setBackgroundResource(R.drawable.seperator);
				view.setPadding(10, 0, 0, 10);
				llRecommendationEvents.addView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			}
        }
        else
        {
        	tvNoanyRecommendation.setVisibility(View.VISIBLE);
        }
	}
	
	public void notificationUpcomingE()
	{
		llNotifiction.removeAllViews();
		if(vctUserNotificatios.size()!=0)
		{
			lvNotifications.setAdapter(objCustomNotificationAdapter=new CustomNotificationAdapter(vctUserNotificatios));
			tvTitle.setText(getResources().getString(R.string.notifications));
			llNotifiction.addView(lvNotifications, new LayoutParams(LayoutParams.FILL_PARENT,107*vctUserNotificatios.size()+30));
		}
		else
		{
			tvTitle.setText(getResources().getString(R.string.notifications));
			tvNoanyNotification.setVisibility(View.VISIBLE);
			tvNoanyNotification.setGravity(Gravity.CENTER);
			llNotifiction.addView(tvNoanyNotification,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		}
		btnAll.setBackgroundResource(R.drawable.all_btn);
		btnRecommendations.setBackgroundResource(R.drawable.recommen_btn);
		btnNotifications.setBackgroundResource(R.drawable.notifications_btn_hover);
		
		llRecommendationEvents.setVisibility(View.GONE);	
		llAllEvents.setVisibility(View.GONE);
		llNotifiction.setVisibility(View.VISIBLE);
		
		lvNotifications.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
					new GetlNotifications().execute();			
			}
			
		});
	}
	
	
	public class CustomNotificationAdapter extends BaseAdapter 
	{
		private static final String TRUE2 = "True";
		private static final String TRUE = "true";
		private List<Notifications> vctUserNotificatios;
		public CustomNotificationAdapter(List<Notifications> vctUserNotificatios)
		{
			this.vctUserNotificatios = vctUserNotificatios;
		}
		
		@Override
		public int getCount()
		{
			if(vctUserNotificatios != null)
				return vctUserNotificatios.size();
			return 0;
		}
		
		private void refresh(List<Notifications> vctUserNotificatios)
		{
			this.vctUserNotificatios = vctUserNotificatios;
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
			final Notifications objNotifications = vctUserNotificatios.get(position);
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.upcoming_event_cell, null);
			convertView.setId(objNotifications.notificationId);
			
			final ImageView ivEditCheck = (ImageView)convertView.findViewById(R.id.ivEditCheck);
			ivEditCheck.setTag(CHECK);

			TextView tvNotificationName=(TextView)convertView.findViewById(R.id.tvEventName);
			TextView tvSenderName=(TextView)convertView.findViewById(R.id.tvEventStartDate);
			ImageView ivEventImage=(ImageView)convertView.findViewById(R.id.ivEventImage);
			ImageView ivDelete=(ImageView)convertView.findViewById(R.id.btnDelete);
			Button btnAcceptRequest=(Button)convertView.findViewById(R.id.btnAcceptRequest);
			Button btnRejectRequest=(Button)convertView.findViewById(R.id.btnRejectRequest);
			RelativeLayout rlEventImage=(RelativeLayout)convertView.findViewById(R.id.rlEventImage);
			ProgressBar upcomingeventProgressBar=(ProgressBar)convertView.findViewById(R.id.upcomingeventProgressbar);
			RelativeLayout rlFriendRequestResponce=(RelativeLayout)convertView.findViewById(R.id.rlFriendRequestResponce);
			rlFriendRequestResponce.setVisibility(View.VISIBLE);
			upcomingeventProgressBar.setVisibility(View.GONE);
			ivEventImage.setVisibility(View.GONE);
			rlEventImage.setVisibility(View.GONE);
			ivDelete.setVisibility(View.GONE);
			tvNotificationName.setSingleLine(false);
			tvNotificationName.setText(objNotifications.strNotificationType);
			tvSenderName.setText(objNotifications.strText+"\n"+" "+getResources().getString(R.string.by)+":"+objNotifications.strSenderName+
					" "+getResources().getString(R.string.on)+":"+objNotifications.strDateCreated.substring(0,10));
			if(objNotifications.strIsRead.equals(TRUE))
			{
				btnAcceptRequest.setEnabled(false);
				btnRejectRequest.setEnabled(false);
			}
			btnAcceptRequest.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
//					if(objGa2ooParsers==null)
//						objGa2ooParsers=new Ga2ooParsers();
					//TODO: no such metod in Ga2ooJsonPasers
//					status=objGa2ooParsers.friendRequestResponce(AppConstants.USER_ID, objNotifications.friendRequestId, 1);
					status = Ga2ooJsonParsers.friendRequestResponce(AppConstants.USER_ID, objNotifications.friendRequestId, 1);
					if(status<=0)
					{
						Toast.makeText(UpcomingEvents.this, getResources().getString(R.string.some_problem), Toast.LENGTH_SHORT).show();
					}
					else
					{
						Log.i(LOGTAG, "Mark read notification...");
						try {
							element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.USER_NOTIFICATIONS+AppConstants.USER_ID+AppConstants.MARKREAD+objNotifications.notificationId);
						} catch (Exception e) {
							Log.e(LOGTAG, "Error in mark read notification!");
							e.printStackTrace();
							DialogUtility.showConnectionErrorDialog(UpcomingEvents.this);
						}
						Object markedResultObject = jsonHelper.parse(element, EventAddDeleteWrapper.class);
						if(markedResultObject!=null){
							if(((EventAddDeleteWrapper)markedResultObject).getUseraccount().result.get(0).code<0){
								Toast.makeText(UpcomingEvents.this, getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
							}else{
								objNotifications.strIsRead=TRUE;
								vctUserNotificatios.set(position, objNotifications);
								if(objCustomNotificationAdapter!=null)
									objCustomNotificationAdapter.notifyDataSetChanged();
								if(userAccBL!=null)
									userAccBL.updateUserNotificationTable(objNotifications.notificationId);
								
								if(friendBL!=null)
									friendBL.updateFriendsTable(1);
								Toast.makeText(UpcomingEvents.this, getResources().getString(R.string.friend_added_to_your_friend_list), Toast.LENGTH_SHORT).show();	
							}
							Log.i(LOGTAG, "Mark read notification successfully completed.");
						}else{
//							DialogUtility.showConnectionErrorDialog(UpcomingEvents.this.getParent());
						}
						
					}
					
				}
			});
			btnRejectRequest.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					status = Ga2ooJsonParsers.friendRequestResponce(AppConstants.USER_ID, objNotifications.friendRequestId, -1);
					if(status<=0)
					{
						Toast.makeText(UpcomingEvents.this, getResources().getString(R.string.some_problem), Toast.LENGTH_SHORT).show();
					}
					else
					{
						Log.i(LOGTAG, "Mark read notification...");
						try {
							element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.USER_NOTIFICATIONS+AppConstants.USER_ID+AppConstants.MARKREAD+objNotifications.notificationId);
						} catch (Exception e) {
							Log.e(LOGTAG, "Error in mark read notification!");
							e.printStackTrace();
							DialogUtility.showConnectionErrorDialog(UpcomingEvents.this);
						}
						Object markedResultObject = jsonHelper.parse(element, EventAddDeleteWrapper.class);
						if(markedResultObject!=null){
							if(((EventAddDeleteWrapper)markedResultObject).getUseraccount().result.get(0).code<0){
								Toast.makeText(UpcomingEvents.this, getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
							}else{
								objNotifications.strIsRead=TRUE2;
								vctUserNotificatios.set(position, objNotifications);
								if(objCustomNotificationAdapter!=null)
									objCustomNotificationAdapter.notifyDataSetChanged();
								
								if(userAccBL!=null)
									userAccBL.updateUserNotificationTable(objNotifications.notificationId);
								
								if(friendBL!=null)
									friendBL.updateFriendsTable(-1);
								Toast.makeText(UpcomingEvents.this, getResources().getString(R.string.friend_request_rejected), Toast.LENGTH_SHORT).show();	
						}
						}else{
							DialogUtility.showConnectionErrorDialog(UpcomingEvents.this.getParent());
						}
						Log.i(LOGTAG, "Mark read notification successfully completed.");
					}
				}
			});
			return convertView;
		}
	}

	public class CustomRecommendationAdapter extends BaseAdapter 
	{
		private List<UserRecommendationObject> vctUserRecommendations;
		public CustomRecommendationAdapter(List<UserRecommendationObject> vctUserNotificatios)
		{
			this.vctUserRecommendations = vctUserNotificatios;
		}
		
		@Override
		public int getCount()
		{
			if(vctUserRecommendations != null)
				return vctUserRecommendations.size();
			return 0;
		}
		
		private void refresh(List<UserRecommendationObject> vctUserRecommendations)
		{
			this.vctUserRecommendations = vctUserRecommendations;
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
			final UserRecommendationObject objRecommendation= vctUserRecommendations.get(position);
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.upcoming_event_cell, null);
			final ImageView ivEditCheck = (ImageView)convertView.findViewById(R.id.ivEditCheck);

			TextView tvEventName=(TextView)convertView.findViewById(R.id.tvEventName);
			TextView tvStartDate=(TextView)convertView.findViewById(R.id.tvEventStartDate);
			ImageView ivDelete=(ImageView)convertView.findViewById(R.id.btnDelete);
			RelativeLayout rlEventImage=(RelativeLayout)convertView.findViewById(R.id.rlEventImage);
			rlEventImage.setVisibility(View.GONE);
			ivDelete.setVisibility(View.GONE);
			
			tvEventName.setText(getResources().getString(R.string.from)+" : "+objRecommendation.fromname);
			tvStartDate.setText(objRecommendation.subject);
			return convertView;
		}
	}


		@Override
		protected void onPause()
		{
			super.onPause();
			llAllEvents.removeAllViews();
			llRecommendationEvents.removeAllViews();
			llNotifiction.removeAllViews();
			if(objDrawableManager != null)
				objDrawableManager.clear();
		}

		private class GetAllNotifications extends AsyncTask<Void,  Void, Boolean>{
			
			JsonElement element;
			JsonHttpHelper jsonHelper = JsonHttpHelper.getInstance();
			
			@Override
			protected Boolean doInBackground(Void... arg0) {
				AppConstants.vctUserNotifications= new ArrayList<Notifications>();
				Log.i(LOGTAG, "Loading user notifications...");
				try {
					element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.USER_NOTIFICATIONS+AppConstants.USER_ID);
				} catch (Exception e) {
					Log.e(LOGTAG, "Error in loading user notifications!");
					e.printStackTrace();
					return false;
				}
				Object notification = jsonHelper.parse(element, NotificationsWrapper.class);
				if(notification!=null){
					AppConstants.vctUserNotifications.addAll(((NotificationsWrapper)notification).getUseraccount().notifications);
					for(int i=0;i<((NotificationsWrapper)notification).getUseraccount().notifications.size();i++){
						userAccBL.insertUserNotifications(((NotificationsWrapper)notification).getUseraccount().notifications.get(i));
					}
					Log.i(LOGTAG, "Loading user notifications successfully completed.");
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if(!result){
					DialogUtility.showConnectionErrorDialog(UpcomingEvents.this.getParent());
				}
				View view = new View(UpcomingEvents.this);
				view.setBackgroundResource(R.drawable.seperator);
				llAllEvents.removeAllViews();
				vctUserNotificatios = userAccBL.getUserNotifications();
				lvNotificationAll.setAdapter(objCustomNotificationAdapter=new CustomNotificationAdapter(vctUserNotificatios));
				objCustomNotificationAdapter.notifyDataSetChanged();
				lvNotificationAll.onRefreshComplete();
				llAllEvents.addView(lvNotificationAll, new LayoutParams(LayoutParams.FILL_PARENT,107*vctUserNotificatios.size()+30));	
				llAllEvents.addView(tvRecommendationTitle);
				llAllEvents.addView(view);
				lvRecommendation.setAdapter(new CustomRecommendationAdapter(vctUserrecommendation));
				llAllEvents.addView(lvRecommendation, new LayoutParams(LayoutParams.FILL_PARENT,60*vctUserrecommendation.size()+50));
				super.onPostExecute(result);
			}
			
		}	
		
		private class GetlNotifications extends AsyncTask<Void,  Void,  Boolean>{
			
			JsonElement element;
			JsonHttpHelper jsonHelper = JsonHttpHelper.getInstance();
			
			@Override
			protected Boolean doInBackground(Void... arg0) {
				Log.i(LOGTAG, "Loading user notifications...");
				AppConstants.vctUserNotifications= new ArrayList<Notifications>();
				try {
					element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.USER_NOTIFICATIONS+AppConstants.USER_ID);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(LOGTAG, "Error in loading user notifications!");
					return false;
				}
				Object notification = jsonHelper.parse(element, NotificationsWrapper.class);
				if(notification!=null){
					AppConstants.vctUserNotifications.addAll(((NotificationsWrapper)notification).getUseraccount().notifications);
					for(int i=0;i<((NotificationsWrapper)notification).getUseraccount().notifications.size();i++){
						userAccBL.insertUserNotifications(((NotificationsWrapper)notification).getUseraccount().notifications.get(i));
					}
					Log.i(LOGTAG, "Loading user notifications successfully completed.");
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if(!result){
					DialogUtility.showConnectionErrorDialog(UpcomingEvents.this.getParent());
				}
				llNotifiction.removeAllViews();
				vctUserNotificatios = userAccBL.getUserNotifications();
				lvNotifications.setAdapter(objCustomNotificationAdapter=new CustomNotificationAdapter(vctUserNotificatios));
				objCustomNotificationAdapter.notifyDataSetChanged();
				lvNotifications.onRefreshComplete();
				llNotifiction.addView(lvNotifications, new LayoutParams(LayoutParams.FILL_PARENT,107*vctUserNotificatios.size()+30));	
				super.onPostExecute(result);
			}
			
		}	

		private class GetAllRecomendations extends AsyncTask<Void, Void, Boolean>{

			@Override
			protected Boolean doInBackground(Void... params) {
				AppConstants.vctUserRecommendations = new ArrayList<UserRecommendationObject>();
    			UserAccountBusinessLayer userAccBL=new UserAccountBusinessLayer();
				userAccBL.deleteInboxValues();
				Log.i(LOGTAG, "Loading user recomendations...");
    			try {
					element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.USER_INBOX+AppConstants.USER_ID);
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(LOGTAG, "Error in loading user recomendations!");
					return false;
				}
    			Object recommendationUseraccountObject = jsonHelper.parse(element, RecommendationUseraccountWrapper.class);
    			if(recommendationUseraccountObject!=null){
	    			for(int i=0;i<((RecommendationUseraccountWrapper)recommendationUseraccountObject).getUseraccount().messages.size();i++){
	    				UserRecommendationObject tmpObject = ((RecommendationUseraccountWrapper)recommendationUseraccountObject).getUseraccount().messages.get(i);
	    				AppConstants.vctUserRecommendations.add(tmpObject);
	    				userAccBL.InsertInUserInbox(tmpObject);
	    			}
	    			Log.i(LOGTAG, "Loading user recomendations successfully completed.");
    			}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if(!result){
					DialogUtility.showConnectionErrorDialog(UpcomingEvents.this.getParent());
				}
				View view = new View(UpcomingEvents.this);
				view.setBackgroundResource(R.drawable.seperator);
				llAllEvents.removeAllViews();
				vctUserNotificatios = userAccBL.getUserNotifications();
				lvNotificationAll.setAdapter(objCustomNotificationAdapter=new CustomNotificationAdapter(vctUserNotificatios));
				objCustomNotificationAdapter.notifyDataSetChanged();
				llAllEvents.addView(lvNotificationAll, new LayoutParams(LayoutParams.FILL_PARENT,107*vctUserNotificatios.size()+30));	
				llAllEvents.addView(tvRecommendationTitle);
				llAllEvents.addView(view);
				lvRecommendation.setAdapter(new CustomRecommendationAdapter(vctUserrecommendation));
				llAllEvents.addView(lvRecommendation, new LayoutParams(LayoutParams.FILL_PARENT,60*vctUserrecommendation.size()+50));
				lvRecommendation.onRefreshComplete();
				super.onPostExecute(result);
			}
			
		}

}

