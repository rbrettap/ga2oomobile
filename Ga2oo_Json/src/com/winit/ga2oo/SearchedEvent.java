package com.winit.ga2oo;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.winit.ga2oo.businesslayer.EventsBusinessLayer;
import com.winit.ga2oo.businesslayer.FriendsBusinessLayer;
import com.winit.ga2oo.businesslayer.UserAccountBusinessLayer;
import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.controls.PopUpDailog;
import com.winit.ga2oo.objects.EventsDetails;
import com.winit.ga2oo.objects.UserFriend;

public class SearchedEvent extends ShareEvent
{
	private static final String LOCATION = "Location";
	private static final String KEYWORD = "Keyword";
	private static final String SEARCHBY = "searchBy";
	private static final String SEARCHKEYWORD = "searchKeyword";
	public List<EventsDetails> vctEventsDetails;
	private List<String> vctEmails;
	private EventsBusinessLayer eventsBL;
	private FriendsBusinessLayer friendBL1;
	private String searchKeyword,searchBy;
	private PopUpDailog customDialog;
	private List<UserFriend> vecFriends;
	private PopupWindow mPopupWindow =null;
	private Context context;
	public  ListView lvFriendsList,lvSearched;
	private LinearLayout llSearchedEvent;
	private TextView tvResult;
	public String strFacebookWallPost;
	private UserAccountBusinessLayer userActBL;
	private String to[],toAllFriend[];
	private String strEventName;
	private String strEventDetail="";
	private String strEventImage="";
	private ProgressDialog progressDialog;
	private DrawableManager objDrawableManager;
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
    }
	
	
	@Override
    public void onResume()
    {
    	super.onResume();
  	
    	setContentView(R.layout.searched_event);
    	if(progressDialog!=null && progressDialog.isShowing())
			progressDialog.dismiss();
    	progressDialog = new ProgressDialog(getParent());
		context=this;
		objDrawableManager	=	new DrawableManager(); 
		friendBL1=new FriendsBusinessLayer();
		vctEmails			=  new ArrayList<String>();
		
		searchKeyword	= getIntent().getExtras().getString(SEARCHKEYWORD);
		searchBy		= getIntent().getExtras().getString(SEARCHBY);
		
		llSearchedEvent	= (LinearLayout)findViewById(R.id.llSearchedEvent);
		lvSearched  = (ListView) findViewById(R.id.lvSearched);
		lvSearched.setCacheColorHint(0);
		tvResult	= (TextView)findViewById(R.id.tvResult);
		userActBL=new UserAccountBusinessLayer();
		if(eventsBL == null)
			eventsBL = new EventsBusinessLayer();
		if(vctEventsDetails==null)
			vctEventsDetails=new ArrayList<EventsDetails>();
		else
			vctEventsDetails.clear();	
		Log.i(searchBy,searchBy);
		if(searchBy.equals(KEYWORD))
		{
			vctEventsDetails = eventsBL.searchEvent(searchKeyword,"",0);
		}
		else if(searchBy.equals(LOCATION))
		{
			vctEventsDetails = eventsBL.searchEventByLocation(searchKeyword,"",0);
		}
        
		if(vctEventsDetails!=null)
		{
			if(vctEventsDetails.size()==0)
			{	
				tvResult.setVisibility(View.VISIBLE);
			}
			else
			{
				lvSearched.setAdapter(new CustomSearchedEventsAdapter(vctEventsDetails));
			}
		}

		
        TabsActivity.btnBack.setVisibility(View.VISIBLE);
        TabsActivity.btnLogout.setVisibility(View.INVISIBLE);
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar);
        TabsActivity.tvTitle.setVisibility(View.GONE);
        TabsActivity.btnLogout.setVisibility(View.VISIBLE);
        TabsActivity.rlCommonTitleBar.invalidate();

        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				TabGroupActivity pActivity = (TabGroupActivity)SearchedEvent.this.getParent();
				pActivity.finishFromChild(SearchedEvent.this);
			}
		});

		TabsActivity.btnLogout.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				progressDialog.setMessage(getResources().getString(R.string.loading));
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
		    					Intent intent = new Intent(SearchedEvent.this, Login.class);
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

	public class CustomSearchedEventsAdapter extends BaseAdapter 
	{
		private static final String SEARCHEDEVENT = "SearchedEvent";
		private static final String EVENT_ID = "EventID";
		private List<EventsDetails> vctUpcomingEvents;
		public CustomSearchedEventsAdapter(List<EventsDetails> vctUpcomingEvents)
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
		
//		private void refresh(Vector<EventsDetails> vctUpcomingEvents)
//		{
//			this.vctUpcomingEvents = vctUpcomingEvents;
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
			final EventsDetails objEventsDetails = vctUpcomingEvents.get(position);
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.list_events_cell, null);
			TextView tvUpName 		= (TextView) convertView.findViewById(R.id.tvUpName);
			TextView tvstartDate 	= (TextView) convertView.findViewById(R.id.tvStartdate);
			ImageView ivEventIcon 	= (ImageView) convertView.findViewById(R.id.ivEventIcon);
			LinearLayout llFavourite = (LinearLayout) convertView.findViewById(R.id.llStar);
			LinearLayout llShare = (LinearLayout) convertView.findViewById(R.id.llShare);       
			ProgressBar eventicon_progress_bar	=	(ProgressBar) convertView.findViewById(R.id.eventicon_progress_bar);
			ProgressBar event_progress_bar		=	(ProgressBar) convertView.findViewById(R.id.event_progress_bar);
			event_progress_bar.setVisibility(View.GONE);
			final int eventId 		=objEventsDetails.eventid;
			final int businessId	= objEventsDetails.EventBusiness;
			llShare.setId(eventId);
		 
			try
			{
				convertView.setId(eventId);
				tvUpName.setText(objEventsDetails.eventname.toString());
				if(objEventsDetails.event_start_time.toString().length()>5)
					tvstartDate.setText(objEventsDetails.event_start_date.toString()+"  "+objEventsDetails.event_start_time.toString().substring(0, 5)+"  "+objEventsDetails.price.toString());
				else
					tvstartDate.setText(objEventsDetails.event_start_date.toString()+"  "+objEventsDetails.event_start_time.toString()+"  "+objEventsDetails.price.toString());
				strEventDetail=objEventsDetails.eventname.toString()+"\n "+getResources().getString(R.string.start_date)+objEventsDetails.event_start_date.toString()+
						getResources().getString(R.string.start_time)+objEventsDetails.event_start_time.toString()+
						getResources().getString(R.string.event_price)+objEventsDetails.price;
				if(!objEventsDetails.image.equals(AppConstants.NOIMAGE))
				{
					if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEventsDetails.image, ivEventIcon, 65, 65,objEventsDetails.imageId,objEventsDetails.isImageUpdated)){
						eventicon_progress_bar.setVisibility(View.GONE);
						ivEventIcon.setImageResource(R.drawable.no_image_event);
					}else{
						eventicon_progress_bar.setVisibility(View.GONE);
	//					strEventImage=AppConstants.IMAGE_HOST_URL+objEventsDetails.EventListImage;
						strEventImage = AppConstants.IMAGE_HOST_URL+objEventsDetails.image;
					}
				}
				else
				{
					eventicon_progress_bar.setVisibility(View.GONE);
					ivEventIcon.setImageResource(R.drawable.no_image_event);
				}
		 }
		 catch(Exception EE)
		 {
			 
	     }
		 llFavourite.setOnClickListener(new OnClickListener()
		 {
			 @Override
			 public void onClick(View v) 
			 {
				 showFavouritePopup(eventId,businessId);
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
				 Intent inttentEventDetails = new Intent(SearchedEvent.this, EventDetails.class);
				 inttentEventDetails.putExtra(EVENT_ID, v.getId());
				 overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
				 TabGroupActivity pActivity = (TabGroupActivity)SearchedEvent.this.getParent();
				 pActivity.startChildActivity(SEARCHEDEVENT, inttentEventDetails);	
			 }
		 });
		 return convertView;
		}
	}

	
	@Override
	protected void onPause()
	{
		llSearchedEvent.removeAllViews();
		if(objDrawableManager != null)
			objDrawableManager.clear();
		super.onPause();
	}

	@Override
	protected void onStop() 
	{
		if(objDrawableManager != null)
			objDrawableManager.clear();
		super.onStop();
	}
	
}
