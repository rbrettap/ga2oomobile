package com.winit.ga2oo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.winit.ga2oo.businesslayer.EventsBusinessLayer;
import com.winit.ga2oo.businesslayer.FriendsBusinessLayer;
import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.common.Tools;
import com.winit.ga2oo.objects.EventsDetails;
import com.winit.ga2oo.objects.UserFriend;

public class FriendsProfile extends Activity
{
	
	private static final String LOGTAG = "FriendsProfiltScreen";
	private static final String USERFRIENDID = "userFriendId";
	private static final String TEXTPLAIN = "text/plain";
	private static final String FRIENDNAME = "friendname";
	private static final String EVENTDETAILS = "EventDetails";
	private static final String DATEFORMAT = "yyyy-MM-dd";
	private static final String EVENTID = "EventID";
	
	private TextView tvFName,tvLName,tvLocation;
	private ImageView ivUserImage;
	private ProgressBar progressBar;
	private LinearLayout llUpcoming;
	private Button btnSendMessage, btnSeeCalendar;
	private RelativeLayout rlFriendImage;
	
	private FriendsBusinessLayer friendBL;
	private DrawableManager objDrawableManager;
	public  Friends objFriends; 
	private EventsBusinessLayer eventsBL;
	private Toast toast;
	private Date eventDate;
	
	private static int userFriendId;
	public String frendImgUrl,strFriendName;
	
	public List<UserFriend> userFriendInfo;
	public static List<EventsDetails> vctEvents;
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
    }
	
	@Override
	public void onResume()
	{
		super.onResume();		
		setContentView(R.layout.friends_profile);
		
		objDrawableManager	= new DrawableManager();
		userFriendId		= getIntent().getExtras().getInt(USERFRIENDID);
		friendBL			= new FriendsBusinessLayer();
		objFriends			= new Friends();
		
		userFriendInfo		= friendBL.getUserFriendInformation(Integer.toString(userFriendId));
		rlFriendImage		=(RelativeLayout)findViewById(R.id.rlFriendImage);
		ivUserImage 		= (ImageView) findViewById(R.id.ivUserImage);
		progressBar			= (ProgressBar)findViewById(R.id.friend_profile_progress_bar);
		tvFName  			= (TextView) findViewById(R.id.tvFName);
		tvLName  			= (TextView) findViewById(R.id.tvLName);
		tvLocation			= (TextView) findViewById(R.id.tvLocation);
		llUpcoming  		= (LinearLayout) findViewById(R.id.llUpcoming);
		btnSendMessage 		= (Button) findViewById(R.id.btnSendMessage);
		btnSeeCalendar 		= (Button) findViewById(R.id.btnSeeCalendar);
		eventsBL 			=  new EventsBusinessLayer();
		vctEvents 			= eventsBL.getUpcomingEvent(userFriendId);
		if(userFriendInfo!=null)
		{
			if(userFriendInfo.size()!=0)
			{
				tvLocation.setText(userFriendInfo.get(0).Address+","+userFriendInfo.get(0).City+","+userFriendInfo.get(0).State+","+userFriendInfo.get(0).zipcode+","+userFriendInfo.get(0).Country);
			}
		}
//		if(!userFriendInfo.get(0).imagesrc.equals("no image"))
//		{
//			if(AppConstants.vctFriend.size()!=0)
//			{
//				for(int i=0;i<AppConstants.vctFriend.size();i++)
//				{
//					if(Integer.toString(AppConstants.vctFriend.get(i).friendid).equals(Integer.toString(userFriendId)))
//					{
//						tvLocation.setText(AppConstants.vctFriend.get(i).address+","+AppConstants.vctFriend.get(i).city+","+AppConstants.vctFriend.get(i).country);
//					}
//				}
//			}
//		}
		if(!userFriendInfo.get(0).imagesrc.equals(AppConstants.NOIMAGE))
		{
			frendImgUrl=AppConstants.IMAGE_HOST_URL+userFriendInfo.get(0).imagesrc;
			objDrawableManager.fetchDrawableOnThread(frendImgUrl, ivUserImage,100,75,userFriendInfo.get(0).image_id,userFriendInfo.get(0).isImageUpdated);
			progressBar.setVisibility(View.GONE);
		}
		else
		{
			progressBar.setVisibility(View.GONE);
			ivUserImage.setImageResource(R.drawable.no_image_smal_event);
		}
		tvFName.setText(userFriendInfo.get(0).firstname);
		tvLName.setText(userFriendInfo.get(0).lastname);
		strFriendName=userFriendInfo.get(0).firstname+" "+userFriendInfo.get(0).lastname;
		
		btnSendMessage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent int_email = new Intent(Intent.ACTION_SEND);
		    	int_email.setType(TEXTPLAIN); 
				String[] to = {userFriendInfo.get(0).email};
				int_email.putExtra(Intent.EXTRA_EMAIL, to);
				
				List<ResolveInfo> list = FriendsProfile.this.getPackageManager().queryIntentActivities(int_email, PackageManager.MATCH_DEFAULT_ONLY);					
				int index = Tools.getMailIntentIndex(list);
				if(index != -1)
				{
					ResolveInfo ri = (ResolveInfo) list.get(index);
					int_email.setClassName(ri.activityInfo.packageName, ri.activityInfo.name);
				}
				
				startActivity(int_email);
			}
		});
		
		btnSeeCalendar.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(userFriendInfo.get(0).is_calendar_shared.equals("True") || userFriendInfo.get(0).is_calendar_shared.equals("true"))
				{
					Intent intent = new Intent(FriendsProfile.this, FriendsPalender.class);
					intent.putExtra(USERFRIENDID, userFriendId);
					intent.putExtra(FRIENDNAME, strFriendName);
					TabGroupActivity pActivity = (TabGroupActivity)FriendsProfile.this.getParent();
					pActivity.startChildActivity(EVENTDETAILS, intent);	
				}
				else
				{
					toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.calendar_is_not_shared), Toast.LENGTH_SHORT);
					toast.show();
				}
				
			}
		});
		
		upcomingEvents();//method is called
    }
	
//	@Override
//	public void onResume()
//	{
//		super.onResume();
//		Log.e("FriendsProfile","onResume");
//		
//		upcomingEvents();//method is called
//
//		
//		TabsActivity.btnBack.setVisibility(View.VISIBLE);
//        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar_empty);
//        TabsActivity.tvTitle.setVisibility(View.VISIBLE);
//        TabsActivity.tvTitle.setText(strFriendName);
//        TabsActivity.btnLogout.setVisibility(View.GONE);
//        
//        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
//        {
//        	@Override
//			public void onClick(View v) 
//			{
//				TabGroupActivity pActivity = (TabGroupActivity)FriendsProfile.this.getParent();
//				pActivity.finishFromChild(FriendsProfile.this);
//			}
//		});
//	}
//	
	public void upcomingEvents()
	{
		for( int i1 = 0; i1<vctEvents.size(); i1++)
		{ 
			Date currentDate	= 	new Date();
			String dtStart = vctEvents.get(i1).event_start_date.toString(); 
			SimpleDateFormat  format = new SimpleDateFormat(DATEFORMAT);  
			try 
			{ 
				currentDate=format.parse(format.format(currentDate));
				eventDate = format.parse(dtStart);  
			}
			catch (ParseException e) 
			{  
			    e.printStackTrace();  
			}
			if(eventDate.after(currentDate)||eventDate.equals(currentDate))
			{

				LinearLayout llEvents 				= (LinearLayout) getLayoutInflater().inflate(R.layout.profile_events, null);
				llUpcoming.addView(llEvents, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				TextView tvUpName 					= (TextView) llEvents.findViewById(R.id.tvUpName);
				TextView tvstartDate 				= (TextView) llEvents.findViewById(R.id.tvStartdate);
				ImageView ivEventImage				= (ImageView)llEvents.findViewById(R.id.ivEventImage);
				ProgressBar upcomingeventProgressbar= (ProgressBar)llEvents.findViewById(R.id.upcomingeventProgressbar);
					
				try
				{
					int eventId = vctEvents.get(i1).eventid;
					llEvents.setId(eventId);
					tvUpName.setText(vctEvents.get(i1).eventname.toString());
					if(vctEvents.get(i1).event_start_time.toString().length()>5)
						tvstartDate.setText(vctEvents.get(i1).event_start_date.toString()+"  "+vctEvents.get(i1).event_start_time.toString().substring(0,5)+"  "+vctEvents.get(i1).price.toString());
					else
						tvstartDate.setText(vctEvents.get(i1).event_start_date.toString()+"  "+vctEvents.get(i1).event_start_time.toString()+"  "+vctEvents.get(i1).price.toString());
					if(!vctEvents.get(i1).image.equals(AppConstants.NOIMAGE))
					{
						objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+vctEvents.get(i1).image, ivEventImage, 30, 30,vctEvents.get(i1).imageId,vctEvents.get(i1).isImageUpdated);
					}
					else
					{
						upcomingeventProgressbar.setVisibility(View.GONE);
						ivEventImage.setImageResource(R.drawable.no_image_smal_event);
					}
				}
				catch(Exception ee)
				{
				}
				llEvents.setOnClickListener(new OnClickListener()
				{				
					@Override
					public void onClick(View v) 
					{
						Intent inttentEventDetails = new Intent(FriendsProfile.this, EventDetails.class);
						overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
						inttentEventDetails.putExtra(EVENTID, v.getId());
						TabGroupActivity pActivity = (TabGroupActivity)FriendsProfile.this.getParent();
						pActivity.startChildActivity(EVENTDETAILS, inttentEventDetails);	
					}
				});
			}
		}	
	}
	@Override
	protected void onPause()
	{
		super.onPause();
		rlFriendImage.removeAllViews();
		llUpcoming.removeAllViews();
		if(objDrawableManager != null)
			objDrawableManager.clear();
	}

}
