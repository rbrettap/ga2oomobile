package com.ga2oo.palendar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.businesslayer.FriendsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.DialogUtility;
import com.ga2oo.palendar.common.Tools;
import com.ga2oo.palendar.controls.PopUpDailog;
import com.ga2oo.palendar.objects.Business;
import com.ga2oo.palendar.objects.EventsDetails;
import com.ga2oo.palendar.objects.UserFriend;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;

public class MoreEventsByBusiness extends Activity
{

	private static final String VND_ANDROID_DIR_MMS_SMS = "vnd.android-dir/mms-sms";
	private static final String _139 = "139";
	private static final String ADDRESS = "address";
	private static final String BUSINESSNAME = "Business_name";
	private static final String BUSINESSID= "businessId";
	private static final String EVENTID = "EventID";
	private static final String SEARCHEDEVENT  = "SearchedEvent";
	private static final String TEXTPLAIN = "text/plain";
	private static final String SMSBODY = "sms_body";
	
	public String strBusinessName,strFacebookWallPost;
	public int businessId,duration = Toast.LENGTH_SHORT;
	private PopUpDailog customDialog;
	private PopupWindow mPopupWindow =null;
	private Context context;
	private EventsBusinessLayer objEventBL,eventsBL;
	private UserAccountBusinessLayer userActBL;
	private DrawableManager objDrawableManager;
	private CustomMoreEventsAdapter objCustomMoreEventsAdapter;
	private FriendsBusinessLayer friendBL;
	
	private List<EventsDetails> vctEventDetails;
	private List<String> vctEmails;
	private List<UserFriend> vecFriends;
	
	private ListView lvMoreEvents,lvFriendsList;
	private LinearLayout llMoreEvents;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
    public void onResume()
    {
    	super.onResume();
    	setContentView(R.layout.moreventsbybusiness);
		
		vctEmails			=  new ArrayList<String>();
		userActBL			=new UserAccountBusinessLayer();
		friendBL			=new FriendsBusinessLayer();
		context				=this;
		objDrawableManager	=new DrawableManager();
		strBusinessName		=getIntent().getExtras().getString(BUSINESSNAME);
		businessId			=getIntent().getExtras().getInt(BUSINESSID);
		objEventBL			=new EventsBusinessLayer();
		vctEventDetails		=new ArrayList<EventsDetails>();
		
		llMoreEvents		=(LinearLayout)findViewById(R.id.llMoreEvents);
		lvMoreEvents 		=(ListView)findViewById(R.id.lvMoreEvents);
		lvMoreEvents.setCacheColorHint(0);
		lvMoreEvents.setScrollbarFadingEnabled(true);
		vctEventDetails		   = objEventBL.getEventsByBusiness(businessId);
		if(vctEventDetails!=null)
			if(vctEventDetails.size()!=0)
				lvMoreEvents.setAdapter(objCustomMoreEventsAdapter=new CustomMoreEventsAdapter(vctEventDetails));
		
    	if(objCustomMoreEventsAdapter!=null)
    		objCustomMoreEventsAdapter.refresh(vctEventDetails);
    	
        TabsActivity.btnBack.setVisibility(View.VISIBLE);
        TabsActivity.btnLogout.setVisibility(View.INVISIBLE);
        TabsActivity.tvTitle.setText(getResources().getString(R.string.events_by)+strBusinessName);
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar_empty);
        TabsActivity.btnLogout.setVisibility(View.VISIBLE);
        TabsActivity.rlCommonTitleBar.invalidate();

        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				TabGroupActivity pActivity = (TabGroupActivity)MoreEventsByBusiness.this.getParent();
				pActivity.finishFromChild(MoreEventsByBusiness.this);
			}
		});

		TabsActivity.btnLogout.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(MoreEventsByBusiness.this, Login.class);
	            startActivity(intent);
	            TabsActivity.tabActivity.finish();
			}
		});
      
    }
	public class CustomMoreEventsAdapter extends BaseAdapter 
	{
		private List<EventsDetails> vctUpcomingEvents;
		public CustomMoreEventsAdapter(List<EventsDetails> vctUpcomingEvents)
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
			
			TextView tvUpName 		= (TextView)  convertView.findViewById(R.id.tvUpName);
			TextView tvstartDate 	= (TextView)  convertView.findViewById(R.id.tvStartdate);
			TextView tvPrice 		= (TextView)  convertView.findViewById(R.id.tvPrice);
			ImageView ivEventIcon 	= (ImageView) convertView.findViewById(R.id.ivEventIcon);
			LinearLayout llFavourite = (LinearLayout) convertView.findViewById(R.id.llStar);
			LinearLayout llShare = (LinearLayout) convertView.findViewById(R.id.llShare);	
			ProgressBar eventicon_progress_bar	=	(ProgressBar) convertView.findViewById(R.id.eventicon_progress_bar);
			ProgressBar event_progress_bar		=	(ProgressBar) convertView.findViewById(R.id.event_progress_bar);
			event_progress_bar.setVisibility(View.GONE);
			
			final int eventId=objEventsDetails.eventid;
			llShare.setId(eventId);
						
			try
			{
				convertView.setId(eventId);
				tvUpName.setText(objEventsDetails.eventname);
				tvstartDate.setText(objEventsDetails.event_start_date+" "+objEventsDetails.event_start_time);
				tvPrice.setText(objEventsDetails.price);
				if(!objEventsDetails.images.get(0).imagesrc.equals(AppConstants.NOIMAGE))
				{
					if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEventsDetails.images.get(0).imagesrc, ivEventIcon, 65, 65,objEventsDetails.imageId,objEventsDetails.isImageUpdated)){
						eventicon_progress_bar.setVisibility(View.GONE);
						ivEventIcon.setImageResource(R.drawable.no_image_event);
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
					showFavouritePopup(eventId);
				}
			});
						
			llShare.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					showSharePopup(v.getId());
				}
			});
					
			convertView.setOnClickListener(new OnClickListener()
			{				
				@Override
				public void onClick(View v) 
				{
					Intent inttentEventDetails = new Intent(MoreEventsByBusiness.this, EventDetails.class);
					inttentEventDetails.putExtra(EVENTID, v.getId());
					overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
					TabGroupActivity pActivity = (TabGroupActivity)MoreEventsByBusiness.this.getParent();
					pActivity.startChildActivity(SEARCHEDEVENT, inttentEventDetails);	
				}
			});
			return convertView;
		}
	}

	public void showFavouritePopup(final int  eventId)
	{
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.category_popup, null);
			
		Button btnCompany = (Button)popup.findViewById(R.id.btnCompany);
		Button btnEvent = (Button)popup.findViewById(R.id.btnEvent);
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
					text = getResources().getString(R.string.company_added_successfully);  
					Business tmpBusiness = new Business();
					tmpBusiness.businessid = businessId;
					tmpBusiness.businessname= " ";
					tmpBusiness.useraddedbusinessid = 0;
					tmpBusiness.date_updated = "0";
					uabl.InsertUserBusiness(tmpBusiness);  
					Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				}
				else if(status==0)
				{
					 text = getResources().getString(R.string.compamy_already_in_your_favorite);   
					 Toast toast = Toast.makeText(context, text, duration);
					toast.show(); 
				}else if(status==-1){
					DialogUtility.showConnectionErrorDialog(MoreEventsByBusiness.this.getParent());
				}
				customDialog.dismiss();
			}
		});
		
		btnEvent.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
				int duration = Toast.LENGTH_SHORT;
				CharSequence text="";
//				Ga2ooParsers objGa2ooParsers=new Ga2ooParsers();
//				int status=objGa2ooParsers.addEventToUser(AppConstants.USER_ID,eventId);
				int status = Ga2ooJsonParsers.addEventToUser(AppConstants.USER_ID,eventId);
				if(status>0)
				{
					text = getResources().getString(R.string.event_successfully_added)  ;
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
					DialogUtility.showConnectionErrorDialog(MoreEventsByBusiness.this.getParent());
				}
				
			 }
		});
		customDialog = new PopUpDailog(getParent(), popup, 220, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}
			
		
	public void showSharePopup(final int eventID) 
	{
		LayoutInflater inflater = this.getLayoutInflater();
		View mView = inflater.inflate(R.layout.event_popup,(ViewGroup) findViewById(R.id.dontRecordPopup));
		mPopupWindow = new PopupWindow(mView,android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
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
				String[] to = {"milean@gmail.com"};
				int_email.putExtra(Intent.EXTRA_EMAIL, to);
//				int_email.putExtra(Intent.EXTRA_SUBJECT, "");
				
				List<ResolveInfo> list = MoreEventsByBusiness.this.getPackageManager().queryIntentActivities(int_email, PackageManager.MATCH_DEFAULT_ONLY);					
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
				sendIntent.putExtra(SMSBODY, SMSBODY);
				sendIntent.putExtra(ADDRESS, _139);
				sendIntent.setType(VND_ANDROID_DIR_MMS_SMS);
				startActivity(sendIntent); 
				mPopupWindow.dismiss();
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
				
				for (int i = 0; i <vctEmails.size(); i++)
				{
					Log.i("Email",""+vctEmails.get(i));
				}     
				customDialog.dismiss();
			}
		});
		customDialog = new PopUpDailog(getParent(), popup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}
	
	public class CustomFriendAdapter extends BaseAdapter 
	{
		private static final String SELECTED = "selected";
		private static final String UNSELECTED = "unselected";
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
		
//		private void refresh(Vector<UserFriend> vecFreiend)
//		{
//			this.vecFreiend = vecFreiend;
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
					if(ivCheckBox.getTag().equals(UNSELECTED))
					{
						String email=objFriend.email;
						vctEmails.add(email);
						ivCheckBox.setTag(SELECTED);
						ivCheckBox.setImageResource(R.drawable.btn_check_on);
					}
					
					else if(ivCheckBox.getTag().equals(SELECTED))
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


	
	@Override
	protected void onPause()
	{
		llMoreEvents.removeAllViews();
		if(objCustomMoreEventsAdapter!=null)
    		objCustomMoreEventsAdapter.refresh(new ArrayList<EventsDetails>());
		if(objDrawableManager != null)
			objDrawableManager.clear();
		super.onPause();
	}

	@Override
	protected void onStop() 
	{
		if(objCustomMoreEventsAdapter!=null)
    		objCustomMoreEventsAdapter.refresh(new ArrayList<EventsDetails>());
		if(objDrawableManager != null)
			objDrawableManager.clear();
		super.onStop();
	}
	
}
