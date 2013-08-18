package com.ga2oo.palendar;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.businesslayer.FriendsBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.objects.EventDates;
import com.ga2oo.palendar.objects.EventsDetails;
import com.ga2oo.palendar.objects.UserFriend;

public class ProfileDetails extends ShareEvent
{
	private TextView tvFName,tvLName, tvLocation,tvAttending,tvFavorite;
	private ImageView ivUserImage,ivFavoriteCal,ivAttendingCal;
	private ProgressBar progressBar;
	private LinearLayout llAttending, llFavorite;
	private ListView lvFavorite,lvAttending;
	private EventsBusinessLayer eventBL;
	private CustomAttendingEvents objCustomAttendingEvents;
	private CustomFavoriteAdapter objCustomFavoriteAdapter;
	public String strUserAttendingEvents;
	private List<EventDates> vctEventdates;
	private List<EventsDetails> vctAllFavoriteEventDetails;
	private DrawableManager drawManager ;
	public String strFavoriteEventIds="";
	private List<UserFriend> vecFriends;
	private List<UserFriend> userFriendInfo1;
	private FriendsBusinessLayer friendBL;

	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
    }
	
	@Override
	public void onResume()
	{
		super.onResume();
		setContentView(R.layout.profile_details);
		if(friendBL == null)
			friendBL			=  new FriendsBusinessLayer();
		userFriendInfo1			=  new ArrayList<UserFriend>();
		drawManager 	= new DrawableManager();
		eventBL			=  new EventsBusinessLayer();
		vctEventdates	=  new ArrayList<EventDates>();
		ivUserImage 	= (ImageView) findViewById(R.id.ivUserImage);
		progressBar		= (ProgressBar)findViewById(R.id.profile_detail_progress_bar);
		tvFName  		= (TextView) findViewById(R.id.tvFName);
		tvLName  		= (TextView) findViewById(R.id.tvLName);
		tvLocation  	= (TextView) findViewById(R.id.tvLocation);
		tvAttending		= (TextView) findViewById(R.id.tvAttending);
		tvFavorite		= (TextView) findViewById(R.id.tvFavorite);
		ivFavoriteCal 	= (ImageView) findViewById(R.id.ivFavoriteCal);
		ivAttendingCal 	= (ImageView) findViewById(R.id.ivAttendingCal);
		llAttending		= (LinearLayout)findViewById(R.id.llAttending);
		llFavorite		= (LinearLayout)findViewById(R.id.llFavorite);
		lvFavorite		= new ListView(this);
		lvAttending		= new ListView(this);
		
		vctAllFavoriteEventDetails=new  ArrayList<EventsDetails>();
		llFavorite.setVisibility(View.GONE);
		tvFavorite.setVisibility(View.GONE);
		lvAttending.setCacheColorHint(0);
		lvFavorite.setCacheColorHint(0);
//		lvAttending.setFadingEdgeLength(0);
//		lvFavorite.setFadingEdgeLength(0);
		
		if(!Home.userImageUrl.equals(AppConstants.NOIMAGE))
		{
			if(!drawManager.fetchDrawableOnThread(Home.userImageUrl, ivUserImage,100,75,Home.vctUserAccount.get(0).imageId,Home.vctUserAccount.get(0).isImageUpdated)){
				ivUserImage.setImageResource(R.drawable.no_image_smal_event);
			}
			progressBar.setVisibility(View.GONE);
		}
		else
		{
			progressBar.setVisibility(View.GONE);
			ivUserImage.setImageResource(R.drawable.no_image_smal_event);
		}
		
		tvFName.setText(Home.vctUserAccount.get(0).firstname);
		tvLName.setText(Home.vctUserAccount.get(0).lastname);
		
		if(Home.vctUserLocation.size()!=0)
			tvLocation.setText(Home.vctUserLocation.get(0).city+","+Home.vctUserLocation.get(0).zipcode);
		
		
		strUserAttendingEvents	=  eventBL.getLogedInUserAttendingEvents(AppConstants.USER_ID);
		if(!strUserAttendingEvents.equals(""))
		{
			vctEventdates		=  eventBL.getdatesOfUserAttendingEvents(strUserAttendingEvents,"");
		}
		if(vctEventdates!=null && vctEventdates.size()!=0)
		{
			lvAttending.setAdapter(objCustomAttendingEvents=new CustomAttendingEvents(vctEventdates));
			llAttending.addView(lvAttending,new LayoutParams(LayoutParams.FILL_PARENT,95*vctEventdates.size()));
		}
		else
		{
			tvAttending.setGravity(Gravity.CENTER_HORIZONTAL);
			tvAttending.setText(getResources().getString(R.string.no_attending_events));
			tvAttending.setLines(2);
			ivAttendingCal.setVisibility(View.GONE);
		}
		
		vctAllFavoriteEventDetails=eventBL.getFavoriteEventDetails();
		if(vctAllFavoriteEventDetails!=null && vctAllFavoriteEventDetails.size()!=0)
		{
			lvFavorite.setAdapter(objCustomFavoriteAdapter=new CustomFavoriteAdapter(vctAllFavoriteEventDetails));
			llFavorite.addView(lvFavorite, new LayoutParams(LayoutParams.FILL_PARENT,100*vctAllFavoriteEventDetails.size()));
		}
		else
		{
			tvFavorite.setGravity(Gravity.CENTER_HORIZONTAL);
			tvFavorite.setLines(2);
			tvFavorite.setText(getResources().getString(R.string.no_favorite_events));
			ivFavoriteCal.setVisibility(View.GONE);
		}
		
		TabsActivity.btnBack.setVisibility(View.VISIBLE);
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar);
        TabsActivity.tvTitle.setVisibility(View.GONE);
        TabsActivity.btnLogout.setVisibility(View.GONE);
        if(objCustomAttendingEvents!=null)
        	objCustomAttendingEvents.refresh(vctEventdates);
        if(objCustomFavoriteAdapter!=null)
        	objCustomFavoriteAdapter.refresh(vctAllFavoriteEventDetails);
        
        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				ProfileDetails.this.finishFromChild(ProfileDetails.this);
			}
		});
	}
	public class CustomAttendingEvents extends BaseAdapter 
	{
		private static final String EVENTDETAILS = "EventDetails";
		private static final String EVENTID = "EventID";
		private List<EventDates> vctEventdates;
		public CustomAttendingEvents(List<EventDates> vctEventdates)
		{
			this.vctEventdates = vctEventdates;
		}
		
		@Override
		public int getCount()
		{
			if(vctEventdates != null)
				return vctEventdates.size();
			return 0;
		}
		
		private void refresh(List<EventDates> vctEventdates)
		{
			this.vctEventdates = vctEventdates;
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
			final EventDates objEventsDetails = vctEventdates.get(position);
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.profile_events, null);
			TextView tvAttendingEvent=(TextView)convertView.findViewById(R.id.tvUpName);
			TextView tvStartDate=(TextView)convertView.findViewById(R.id.tvStartdate);
			TextView tvNoofFriends=(TextView)convertView.findViewById(R.id.tvNoofFriends);
			tvAttendingEvent.setText(objEventsDetails.eventName);
			if(objEventsDetails.strEventStartTime.toString().length()>5)
				tvStartDate.setText(objEventsDetails.strEventStartdate+"  "+objEventsDetails.strEventStartTime.toString().substring(0, 5)+"  "+objEventsDetails.strEventPrice);
			else
				tvStartDate.setText(objEventsDetails.strEventStartdate+"  "+objEventsDetails.strEventStartTime.toString()+"  "+objEventsDetails.strEventPrice);
			convertView.setId(objEventsDetails.eventId);
			tvNoofFriends.setId(objEventsDetails.eventId);
			userFriendInfo1=friendBL.getUsrFrendAttendingEvents(Integer.toString(objEventsDetails.eventId));
			ImageView ivEventImage=(ImageView)convertView.findViewById(R.id.ivEventImage);
			ProgressBar upcomingeventProgressbar=(ProgressBar)convertView.findViewById(R.id.upcomingeventProgressbar);
			if(!objEventsDetails.eventImage.equals(AppConstants.NOIMAGE))
			{
				if(!drawManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEventsDetails.eventImage, ivEventImage, 70, 70,objEventsDetails.imageId,objEventsDetails.isImageUpdated)){
					upcomingeventProgressbar.setVisibility(View.GONE);
					ivEventImage.setImageResource(R.drawable.no_image_smal_event);
				}
			}
			else
			{
				upcomingeventProgressbar.setVisibility(View.GONE);
				ivEventImage.setImageResource(R.drawable.no_image_smal_event);
			}
			if(userFriendInfo1!=null&&userFriendInfo1.size()!=0)
			{
				if(userFriendInfo1.get(0).noOfFriends==1)
					tvNoofFriends.setText(userFriendInfo1.get(0).noOfFriends+" "+getResources().getString(R.string.friend));
				else
					tvNoofFriends.setText(userFriendInfo1.get(0).noOfFriends+" "+getResources().getString(R.string.friends));
				tvNoofFriends.setOnClickListener(new OnClickListener()
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
				tvNoofFriends.setText(getResources().getString(R.string.no_friend_yet));
			}
			
			convertView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					Intent inttentEventDetails = new Intent(ProfileDetails.this, EventDetails.class);
					inttentEventDetails.putExtra(EVENTID,v.getId());
					TabGroupActivity pActivity = (TabGroupActivity)ProfileDetails.this.getParent();
					pActivity.startChildActivity(EVENTDETAILS, inttentEventDetails);
				}
			});
			return convertView;
		}
	}

	public class CustomFavoriteAdapter extends BaseAdapter 
	{
		private static final String EVENTDETAILS = "EventDetails";
		private static final String EVENTID = "EventID";
		private List<EventsDetails> vctAllFavoriteEventDetails;
		public CustomFavoriteAdapter(List<EventsDetails> vctAllFavoriteEventDetails)
		{
			this.vctAllFavoriteEventDetails = vctAllFavoriteEventDetails;
		}
		
		@Override
		public int getCount()
		{
			if(vctAllFavoriteEventDetails != null)
				return vctAllFavoriteEventDetails.size();
			return 0;
		}
		
		private void refresh(List<EventsDetails> vctAllFavoriteEventDetails)
		{
			this.vctAllFavoriteEventDetails = vctAllFavoriteEventDetails;
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
			final EventsDetails objEventsDetails = vctAllFavoriteEventDetails.get(position);
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.profile_events, null);
			TextView tvFavoriteEvent=(TextView)convertView.findViewById(R.id.tvUpName);
			TextView tvStartDate=(TextView)convertView.findViewById(R.id.tvStartdate);
			
			ImageView ivEventImage=(ImageView)convertView.findViewById(R.id.ivEventImage);
			ProgressBar upcomingeventProgressbar=(ProgressBar)convertView.findViewById(R.id.upcomingeventProgressbar);
			tvFavoriteEvent.setText(objEventsDetails.eventname);
			tvStartDate.setText(objEventsDetails.event_start_date);
			convertView.setId(objEventsDetails.eventid);
			if(!objEventsDetails.images.get(0).imagesrc.equals(AppConstants.NOIMAGE))
			{
				if(!drawManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEventsDetails.images.get(0).imagesrc, ivEventImage, 30, 30,objEventsDetails.imageId,objEventsDetails.isImageUpdated)){
					upcomingeventProgressbar.setVisibility(View.GONE);
					ivEventImage.setImageResource(R.drawable.no_image_smal_event);
				}
			}
			else
			{
				upcomingeventProgressbar.setVisibility(View.GONE);
				ivEventImage.setImageResource(R.drawable.no_image_smal_event);
			}
			convertView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					Intent inttentEventDetails = new Intent(ProfileDetails.this, EventDetails.class);
					inttentEventDetails.putExtra(EVENTID,v.getId());
					TabGroupActivity pActivity = (TabGroupActivity)ProfileDetails.this.getParent();
					pActivity.startChildActivity(EVENTDETAILS, inttentEventDetails);
				}
			});
			return convertView;
		}
	}

	@Override
	protected void onPause()
	{
		llFavorite.removeAllViews();
		llAttending.removeAllViews();
		
		if(drawManager != null)
			drawManager.clear();
		super.onPause();
	}

	@Override
	protected void onStop() 
	{
		if(drawManager != null)
			drawManager.clear();
		super.onStop();
	}
}
