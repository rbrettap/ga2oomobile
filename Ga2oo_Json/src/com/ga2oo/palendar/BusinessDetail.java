package com.ga2oo.palendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.DialogUtility;
import com.ga2oo.palendar.common.Tools;
import com.ga2oo.palendar.objects.Business;
import com.ga2oo.palendar.objects.EventsDetails;
import com.ga2oo.jsonparsers.BusinessAccountWrapper;
import com.ga2oo.parsing.net.JsonHttpHelper;

public class BusinessDetail extends Activity
{
	
	private static final String LOGTAG = "BusinessDetail";
	private static final String BUSINESSID = "BusinessId";
	private static final String TEXTPLAIN = "text/plain";
	private static final String BUSINESSIDS = "businessId";
	private static final String FRIENDNAME = "friendname";
	private static final String BUSINESSDETAIL = "Business Detail";
	private static final String DATEFORMAT ="yyyy-MM-dd";
	private static final String EVENTID = "EventID";
	private static final String EVENTDETAILS = "EventDetails";
	
	private TextView tvFName,tvLName,tvLocation;
	private ImageView ivUserImage;
	private ProgressBar progressBar;
	private LinearLayout llUpcoming;
	private Button btnSendMessage, btnSeeCalendar;
	private RelativeLayout rlFriendImage;
	
	private EventsBusinessLayer eventBL;
	private DrawableManager objDrawableManager;
	public  Friends objFriends; 
	private EventsBusinessLayer eventsBL;
	private Date eventDate;
	private ProgressDialog progressDialog;
	private JsonElement element;
	private JsonHttpHelper jsonHelper;
	private static int BusinessId;
	public String frendImgUrl,strFriendName;
	
	public List<Business> businessInfo;
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
		BusinessId			= getIntent().getExtras().getInt(BUSINESSID);
		businessInfo		= new ArrayList<Business>();
		eventBL				= new EventsBusinessLayer();
		objFriends			= new Friends();
		
		businessInfo		= eventBL.getBusinessInformation(Integer.toString(BusinessId));
		rlFriendImage		=(RelativeLayout)findViewById(R.id.rlFriendImage);
		ivUserImage 		= (ImageView) findViewById(R.id.ivUserImage);
		progressBar			= (ProgressBar)findViewById(R.id.friend_profile_progress_bar);
		tvFName  			= (TextView) findViewById(R.id.tvFName);
		tvLName  			= (TextView) findViewById(R.id.tvLName);
		tvLocation			= (TextView) findViewById(R.id.tvLocation);
		llUpcoming  		= (LinearLayout) findViewById(R.id.llUpcoming);
		btnSendMessage 		= (Button) findViewById(R.id.btnSendMessage);
		btnSeeCalendar 		= (Button) findViewById(R.id.btnSeeCalendar);
		btnSeeCalendar.setBackgroundResource(R.drawable.see_cal);
		eventsBL 			=  new EventsBusinessLayer();
		jsonHelper = JsonHttpHelper.getInstance();
		AppConstants.vctBusinessDetail = new ArrayList<Business>();
		vctEvents 			= eventsBL.getUpcomingEventByBusiness(BusinessId);
		if(progressDialog!=null && progressDialog.isShowing())
    		progressDialog.dismiss();
    	
    	progressDialog = new ProgressDialog(getParent());
		progressDialog.setMessage(getResources().getString(R.string.loading));
		
		new LaderBusinessDetails().execute();
	}
	
	private class LaderBusinessDetails extends AsyncTask<Void,Void,Boolean>{
		
		@Override
		protected void onPreExecute() {
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			Log.i(LOGTAG, "Loading business details...");
			try {
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.BUSINESS_DETAIL+BusinessId);
			} catch (Exception e) {
				DialogUtility.showConnectionErrorDialog(BusinessDetail.this);
				Log.e(LOGTAG, "Error in Loading business details...");
				e.printStackTrace();
				return false;
			}
				Object businessObject =  jsonHelper.parse(element, BusinessAccountWrapper.class);
				if(businessObject!=null){
					AppConstants.vctBusinessDetail.add(((BusinessAccountWrapper)businessObject).getBusinessaccount());
				}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			if(result){
				AppConstants.vctBusinessDetail = EventsBusinessLayer.getBusinessInformation(BusinessId+"");
			}else{
				DialogUtility.showConnectionErrorDialog(BusinessDetail.this.getParent());
			}
			if(AppConstants.vctBusinessDetail!=null)
			{
				if(AppConstants.vctBusinessDetail.size()!=0)
				{
					if(AppConstants.vctBusinessDetail.get(0).imagesrc!=null && !AppConstants.vctBusinessDetail.get(0).imagesrc.equals("") 
							&& !AppConstants.vctBusinessDetail.get(0).imagesrc.equals(AppConstants.NOIMAGE))
					{
						frendImgUrl=AppConstants.IMAGE_HOST_URL+AppConstants.vctBusinessDetail.get(0).imagesrc;
						if(!objDrawableManager.fetchDrawableOnThread(frendImgUrl, ivUserImage,100,75)){
							ivUserImage.setImageResource(R.drawable.no_image_smal_event);
						}
						progressBar.setVisibility(View.GONE);
					}
					else
					{
						progressBar.setVisibility(View.GONE);
						ivUserImage.setImageResource(R.drawable.no_image_smal_event);
					}
				}
				else
				{
					progressBar.setVisibility(View.GONE);
					ivUserImage.setImageResource(R.drawable.no_image_smal_event);
				}
			}
			tvFName.setTextSize(20);
			tvFName.setText(businessInfo.get(0).businessname);
			tvLName.setTextSize(18);
			tvLName.setTextColor(R.color.text_color_light);
			tvLName.setText(businessInfo.get(0).businesstype);
			tvLocation.setTextSize(14);
			tvLocation.setTextColor(R.color.text_color_light);
			tvLocation.setText(businessInfo.get(0).contactname+"\n"+businessInfo.get(0).contactemail);
			strFriendName=businessInfo.get(0).businessname;
			
			btnSendMessage.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					Intent int_email = new Intent(Intent.ACTION_SEND);
			    	int_email.setType(TEXTPLAIN); 
					String[] to = {businessInfo.get(0).contactemail};
					int_email.putExtra(Intent.EXTRA_EMAIL, to);
					
					List<ResolveInfo> list = BusinessDetail.this.getPackageManager().queryIntentActivities(int_email, PackageManager.MATCH_DEFAULT_ONLY);					
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
						Intent intent = new Intent(BusinessDetail.this, BusinessPalender.class);
						intent.putExtra(BUSINESSIDS, BusinessId);
						intent.putExtra(FRIENDNAME, strFriendName);
						TabGroupActivity pActivity = (TabGroupActivity)BusinessDetail.this.getParent();
						pActivity.startChildActivity(BUSINESSDETAIL, intent);	
				}
			});
			
			upcomingEvents();//method is called
			TabsActivity.btnBack.setVisibility(View.VISIBLE);
	        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar_empty);
	        TabsActivity.tvTitle.setVisibility(View.VISIBLE);
	        TabsActivity.tvTitle.setText(strFriendName);
	        TabsActivity.btnLogout.setVisibility(View.GONE);
	        
	        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
	        {
	        	@Override
				public void onClick(View v) 
				{
					TabGroupActivity pActivity = (TabGroupActivity)BusinessDetail.this.getParent();
					pActivity.finishFromChild(BusinessDetail.this);
				}
			});
			super.onPostExecute(result);
		}
	}
	
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
					if(!vctEvents.get(i1).images.get(0).imagesrc.equals(AppConstants.NOIMAGE))
					{
						if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+vctEvents.get(i1).images.get(0).imagesrc, ivEventImage, 30, 30)){
							upcomingeventProgressbar.setVisibility(View.GONE);
							ivEventImage.setImageResource(R.drawable.no_image_smal_event);
						}
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
						Intent inttentEventDetails = new Intent(BusinessDetail.this, EventDetails.class);
						overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
						inttentEventDetails.putExtra(EVENTID, v.getId());
						TabGroupActivity pActivity = (TabGroupActivity)BusinessDetail.this.getParent();
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
