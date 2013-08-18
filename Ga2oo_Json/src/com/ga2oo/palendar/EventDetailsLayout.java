package com.ga2oo.palendar;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import com.ga2oo.palendar.EventDetails.ImageAdapter;
import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.businesslayer.FriendsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.controls.PopUpDailog;
import com.ga2oo.palendar.objects.Event;
import com.ga2oo.palendar.objects.EventImages;
import com.ga2oo.palendar.objects.EventsDetails;
import com.ga2oo.palendar.objects.EventsDetailsData;
import com.ga2oo.palendar.objects.UserFriend;
import com.ga2oo.parsing.net.JsonHttpHelper;

public class EventDetailsLayout extends LinearLayout {

	private static final String LOGTAG = "EventDetailsLayout";
	private static final String HOMEGROUPACTIVITY = "HomeGroupActivity";
	private static final String UNSELECTED = "unselected";
	private static final String SELECTED = "selected";
	
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
	private PopupWindow mPopupWindow =null;
	private Context context;
	private JsonElement element;
	private JsonHttpHelper jsonHelper;
	private ImageAdapter objImageAdapter;
	private CustomFriendAdapter objCustomFriendAdapter;
	
	public EventDetailsLayout(Context context, int id) {
		super(context);
		this.context = context;
		this.eventId = id;
		build(context);
	}

	private void build(final Context context) {
		LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.event_details_layout, this);
		
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
		AppConstants.vctEventsDetails = new ArrayList<EventsDetailsData>();
		jsonHelper = JsonHttpHelper.getInstance();
		eventsBL 				= new EventsBusinessLayer();
		objDrawableManager=new DrawableManager();
//		new LoadEventDetails().execute();
		setDataToView();
		ivPhoto.setOnClickListener(new OnClickListener()
   	 	{
			@Override
			public void onClick(View v)
			{
				eventDetailsProgressBar.setVisibility(View.GONE);
				((EventDetailsViewPager)context).showImageGallery(eventId);
			}
   	 	});
		
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
    	
//        TabsActivity.btnBack.setVisibility(View.VISIBLE);
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar_empty);

        TabsActivity.btnCalView.setBackgroundResource(R.drawable.btn_calview);
		TabsActivity.btnListView.setBackgroundResource(R.drawable.btn_listview);
        TabsActivity.tvTitle.setText(getResources().getString(R.string.event_details));
        TabsActivity.tvTitle.setVisibility(View.VISIBLE);
        TabsActivity.btnLogout.setVisibility(View.VISIBLE);
        TabsActivity.btnLogout.setBackgroundResource(R.drawable.logout_btn);
        TabsActivity.rlCommonTitleBar.invalidate();
        
        if(HOMEGROUPACTIVITY.equals(((EventDetailsViewPager) context).getParentName())){
        	TabsActivity.btnBack.setVisibility(View.VISIBLE);
            TabsActivity.llEventButtons.setVisibility(View.GONE);
    		TabsActivity.btnCalView.setVisibility(View.GONE);
    		TabsActivity.btnListView.setVisibility(View.GONE);
        }else{
        	TabsActivity.btnBack.setVisibility(View.GONE);
            TabsActivity.llEventButtons.setVisibility(View.VISIBLE);
    		TabsActivity.btnCalView.setVisibility(View.VISIBLE);
    		TabsActivity.btnListView.setVisibility(View.VISIBLE);
        }
        
        if(friendAdapter != null)
        	friendAdapter.refresh(userFriends);
        
        if(objImageAdapter!=null)
        	objImageAdapter.refresh(vctEventImageAdpter);
        
        if(objCustomFriendAdapter!=null)
        	objCustomFriendAdapter.refresh(vecFriends);
        
//        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
//        {
//			@Override
//			public void onClick(View v) 
//			{
//				TabGroupActivity pActivity = (TabGroupActivity)EventDetails.this.getParent();
//				pActivity.finishFromChild(EventDetails.this);
//			}
//		});
        TabsActivity.btnListView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				((EventDetailsViewPager)context).finish();		
			}
        	
        });
        TabsActivity.btnCalView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				((EventDetailsViewPager)context).showCalendar();
				
			}
        	
        });
        TabsActivity.btnLogout.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				((EventDetailsViewPager)context).doLogout();				
			}
		});
        
        TabsActivity.btnBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				((EventDetailsViewPager)context).finish();						
			}
        	
        });
    
	}
	
//	public void setData(){
//		new LoadEventDetails().execute();
//	}
	
private class StarClickListner implements OnClickListener{
		
		@Override
		public void onClick(View v) {
			((EventDetailsViewPager)context).addToFavoriteDialog(businessId, eventId);			
		}
	}
	
	private class ShareClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			((EventDetailsViewPager)context).showSharePopup(context, eventId, strEventName, strEventDetail);		
		}
		
	}
	
	public void setDataToView(){
		llMapBtn.setVisibility(View.VISIBLE);
		vctEventDesc = eventsBL.geteventDetails(eventId);
		for(int i = 0; i<vctEventDesc.size(); i++)
	        {   
				businessId=vctEventDesc.get(i).business;
//					final String strBusinessName=vctEventDesc.get(i).business.businessname;
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
		    		if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+vctEventDesc.get(i).image, ivPhoto,100,100,vctEventDesc.get(i).imageId,vctEventDesc.get(i).isImageUpdated)){
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
						((EventDetailsViewPager)context).startMapActivity(vctEventDesc.get(0).EventGeoCode);
					}
				});
		    	tvSeeMoreEvents.setOnClickListener(new OnClickListener()
		    	{
					@Override
					public void onClick(View v)
					{
						((EventDetailsViewPager)context).openBusinessPalendar(v.getId());
					}
		    	});
		    	ivBuyTicket.setOnClickListener(new OnClickListener()
		        {
					@Override
					public void onClick(View v)
					{
						((EventDetailsViewPager)context).openByTicket(event_ticket_url);
					}
		        });
		    	 llStar.setOnClickListener(new StarClickListner());
		    	 ivStar.setOnClickListener(new StarClickListner());
		    	 llShare.setOnClickListener(new ShareClickListener());
		    	 ivShare.setOnClickListener(new ShareClickListener());
	        }
	}
	
//	private class LoadEventDetails extends AsyncTask<Void,Void,Boolean>{
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//		
//		@Override
//		protected Boolean doInBackground(Void... arg0) {
//			EventsBusinessLayer evbl = new EventsBusinessLayer();
//			AppConstants.vctEventsDetails = new ArrayList<EventsDetailsData>();
//			Log.i(LOGTAG, "Loading event details...");
//			try {
//				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_DETAILS+eventId);
//			} catch (Exception e) {
//				Log.e(LOGTAG, "Error in Loading event details.");
//				e.printStackTrace();
//				return false;
//			}
//			Object eventsDetailsObjects = jsonHelper.parse(element, EventsDetailsWrapper.class);
//			EventsDetailsData tmpDetails = ((EventsDetailsWrapper)eventsDetailsObjects).getEvent();
//				AppConstants.vctEventsDetails.add(tmpDetails);
//				evbl.InsertIntoEventDetails(tmpDetails);
//					for(int i=0;i<((EventsDetailsWrapper)eventsDetailsObjects).getEvent().attending.size();i++)
//					{
//						Attending tmpAttending = new Attending();
//						tmpAttending = ((EventsDetailsWrapper)eventsDetailsObjects).getEvent().attending.get(i);
//						tmpAttending.eventID = tmpDetails.id;
//						evbl.InsertAttending(tmpAttending);
//					}
//					Log.i(LOGTAG, "Loading event details successfully completed.");
//			return true;
//		}
//		
//		@Override
//		protected void onPostExecute(Boolean result) {
//
//			if(result){
//				setDataToView();
//			}else{
////				DialogUtility.showConnectionErrorDialog(context);
//			}
//			super.onPostExecute(result);
//		}
//		
//		
//		
//	}


	
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
			convertView = (LinearLayout) ((LayoutInflater) EventDetailsLayout.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.friends_list_cell, null);
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
			convertView = (LinearLayout) ((LayoutInflater) EventDetailsLayout.this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.userfriendattendingevent, null);
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
					((EventDetailsViewPager)context).showFriendProfile(friend.ID1);
				}
			});
		
			return convertView;
		}
	}
}
