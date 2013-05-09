package com.winit.ga2oo;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.winit.ga2oo.businesslayer.EventsBusinessLayer;
import com.winit.ga2oo.businesslayer.UserAccountBusinessLayer;
import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.common.DialogUtility;
import com.winit.ga2oo.controls.PopUpDailog;
import com.winit.ga2oo.objects.Attending;
import com.winit.ga2oo.objects.Business;
import com.winit.ga2oo.objects.EventImages;
import com.winit.ga2oo.objects.EventsDetails;
import com.winit.ga2oo.objects.EventsDetailsData;
import com.winit.go2oo.jsonparsers.EventsDetailsWrapper;
import com.winit.go2oo.jsonparsers.Ga2ooJsonParsers;
import com.winit.parsing.net.JsonHttpHelper;

public class EventDetailsViewPager extends ShareEvent {
	
	private static final String LOGTAG = "EventDetailsViewPager";
	private static final String FROMACTIVITY = "fromActivity";
	private static final String HOME = "Home";
	private static final String GEOPOINT = "GeoPoint";
	private static final String STRTO = "strTo";
	private static final String DISPLAYLOCATION = "displayLocation";
	private static final String MYMAPACTIVITY = "MyMapActivity";
	private static final String BUSINESSID = "businessId";
	private static final String SEARCHEDEVENT = "SearchedEvent";
	private static final String EVENTTICKET = "event_ticket";
	private static final String USERFINDID = "userFriendId";
	private static final String FRIENDSPROFILE = "FriendsProfile";
	private static final String EVENTSLIST = "EventsList";
	private static final String POSITION = "Position";
	
	public static ProgressBar progressDialog;
	private PopUpDailog customDialog;
	private EventsBusinessLayer eventsBL;
	private UserAccountBusinessLayer userActBL;
	public List<EventImages> vctEventImages,vctEventImageAdpter;
	private DrawableManager objDrawableManager;
	ArrayList<EventsDetails> eventsList;
	List<Boolean> loadedDetails;
	private ViewPager viewPager;
	MyPagerAdapter adapter;
	View currentView;
	private AlertDialog.Builder internetPopup;
	private AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		objDrawableManager=new DrawableManager();
		setContentView(R.layout.event_details_view_pager);
		loadedDetails = new ArrayList<Boolean>();
		loadedDetails.clear();

		progressDialog = (ProgressBar)findViewById(R.id.viewPagerProgress);
		
		eventsBL 				= new EventsBusinessLayer();
		userActBL				= new UserAccountBusinessLayer();
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		eventsList = (ArrayList<EventsDetails>) EventDetailsViewPager.this.getIntent().getExtras().getSerializable(EVENTSLIST);
		int position = EventDetailsViewPager.this.getIntent().getIntExtra(POSITION, 0);
		adapter = new MyPagerAdapter(this);
		viewPager.setAdapter(adapter);
		
		viewPager.setCurrentItem(this.getIntent().getIntExtra(POSITION, 0));
		
		View current = ((EventDetailsLayout)adapter.views.get(position));
		currentView = current;
		currentView.setTag(eventsList.get(position).eventid);
		viewPager.setOnPageChangeListener(new OnPageChangeListener(){
			
			@Override
			public void onPageScrollStateChanged(int page) {	
			}

			@Override
			public void onPageScrolled(int page, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int page) {
				if(!loadedDetails.get(page)){
					viewPager.setVisibility(View.GONE);
					progressDialog.setVisibility(View.VISIBLE);
					new LoadEventDetails().execute(page);
					
				}
				View current = ((EventDetailsLayout)adapter.views.get(page));
				currentView = current;
				currentView.setTag(eventsList.get(page).eventid);
			}
			
		});
		
	}
	
	public View getCurrentView(){
		return currentView;
	}
	
	public class LoadEventDetails extends AsyncTask<Integer, Void, Boolean>{
		
		int currentPage=0;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Integer... params) {
			currentPage = params[0];
			return loadEventDetails(eventsList.get(params[0]).eventid);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
			loadedDetails.set(currentPage, true);
			}else{
				loadedDetails.set(currentPage, false);
				DialogUtility.showConnectionErrorDialog(EventDetailsViewPager.this.getParent());
			}
			((EventDetailsLayout)adapter.views.get(currentPage)).setDataToView();
			viewPager.setVisibility(View.VISIBLE);
			progressDialog.setVisibility(View.GONE);
			super.onPostExecute(result);
		}
		
	}

	private class MyPagerAdapter extends PagerAdapter {

		public ArrayList<LinearLayout> views;

		public MyPagerAdapter(Context context) {
			views = new ArrayList<LinearLayout>();
			int position = EventDetailsViewPager.this.getIntent().getIntExtra(POSITION, 0);
			if(!loadEventDetails(eventsList.get(position).eventid)){
				DialogUtility.showConnectionErrorDialog(EventDetailsViewPager.this.getParent());
			}
			for(int i=0;i<eventsList.size();i++){
				loadedDetails.add(false);
				views.add(new EventDetailsLayout(context,eventsList.get(i).eventid));
			}
			loadedDetails.add(position, true);
		}

		@Override
		public void destroyItem(View view, int arg1, Object object) {
			((ViewPager) view).removeView((LinearLayout) object);
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public Object instantiateItem(View view, int position) {
			View myView = views.get(position);
			((ViewPager) view).addView(myView);
			return myView;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View view) {
		}
		
	}
	
	//methods from views
	public void startMapActivity(String geocode){
		ConnectivityManager connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean connected = (connec.getActiveNetworkInfo() != null &&
							 connec.getActiveNetworkInfo().isAvailable() &&
							 connec.getActiveNetworkInfo().isConnected());
		if(!connected)
		{
			internetPopup =  new Builder(EventDetailsViewPager.this.getParent());
			internetPopup.setMessage(EventDetailsViewPager.this.getParent().getResources().getString(R.string.network_unavailable_for_map));
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
			Intent intent = new Intent(EventDetailsViewPager.this, EventLocationOnMap.class);
	        intent.putExtra(FROMACTIVITY, HOME);
	        intent.putExtra(GEOPOINT, geocode);
	        intent.putExtra(STRTO, DISPLAYLOCATION);
	        TabGroupActivity pActivity = (TabGroupActivity)EventDetailsViewPager.this.getParent();
	        pActivity.startChildActivity(MYMAPACTIVITY, intent);
		}
	}
	
	public void openBusinessPalendar(int businessId){
		Intent inttentMoreEvent = new Intent(EventDetailsViewPager.this, BusinessPalender.class);
//		inttentMoreEvent.putExtra("friendname", strBusinessName);
		inttentMoreEvent.putExtra(BUSINESSID,businessId );
		overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
		TabGroupActivity pActivity = (TabGroupActivity)EventDetailsViewPager.this.getParent();
		pActivity.startChildActivity(SEARCHEDEVENT, inttentMoreEvent);
	}
	
	public void openByTicket(String event_ticket_url){
		Intent inttentMoreEvent = new Intent(EventDetailsViewPager.this, BuyTicket.class);
		inttentMoreEvent.putExtra(EVENTTICKET, event_ticket_url);
		overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
		TabGroupActivity pActivity = (TabGroupActivity)EventDetailsViewPager.this.getParent();
		pActivity.startChildActivity(SEARCHEDEVENT, inttentMoreEvent);
	}
	
	public void addToFavoriteDialog(final int businessId, final int eventId){
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
					Toast toast = Toast.makeText(EventDetailsViewPager.this, text, duration);
					toast.show();
				}
				else if(status==0)
				{
					 text = getResources().getString(R.string.compamy_already_in_your_favorite);   
					 Toast toast = Toast.makeText(EventDetailsViewPager.this, text, duration);
						toast.show();
				}else if(status==-1){
					DialogUtility.showConnectionErrorDialog(EventDetailsViewPager.this.getParent());
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
					Toast toast = Toast.makeText(EventDetailsViewPager.this, text, duration);
					toast.show();
				}
				else if(status==0)
				{
					 text = getResources().getString(R.string.event_already_in_palendar);   
					 Toast toast = Toast.makeText(EventDetailsViewPager.this, text, duration);
						toast.show();
				}else if(status==-1){
					DialogUtility.showConnectionErrorDialog(EventDetailsViewPager.this.getParent());
				}
				
			 }
		});
		customDialog = new PopUpDailog(getParent(), popup, 220, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}
		
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
	    private void refresh(List<EventImages> vctEventImages)
		{
			this.vctEventImagesAdapter = vctEventImages;
			this.notifyDataSetChanged();
		}
	}
	
	public boolean loadEventDetails(int eventid){
		Log.i(LOGTAG, "loading event id = "+eventid);
		JsonElement element = null;
		JsonHttpHelper jsonHelper = JsonHttpHelper.getInstance();
		EventsBusinessLayer evbl = new EventsBusinessLayer();
		AppConstants.vctEventsDetails = new ArrayList();
		Log.i(LOGTAG, "Loading event details...");
		try {
			element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.EVENT_DETAILS+eventid);
		} catch (Exception e) {
			Log.e(LOGTAG, "Error in Loading event details.");
			e.printStackTrace();
			return false;
		}
		
		Object eventsDetailsObjects = jsonHelper.parse(element, EventsDetailsWrapper.class);
		if(eventsDetailsObjects!=null){
		EventsDetailsData tmpDetails = ((EventsDetailsWrapper)eventsDetailsObjects).getEvent();
		if(tmpDetails!=null){
			AppConstants.vctEventsDetails.add(tmpDetails);
			evbl.InsertIntoEventDetails(tmpDetails);
				for(int i=0;i<((EventsDetailsWrapper)eventsDetailsObjects).getEvent().attending.size();i++)
				{
					Attending tmpAttending = new Attending();
					tmpAttending = ((EventsDetailsWrapper)eventsDetailsObjects).getEvent().attending.get(i);
					tmpAttending.eventID = tmpDetails.id;
					evbl.InsertAttending(tmpAttending);
				}
		}
				Log.i(LOGTAG, "Loading event details successfully completed.");
		}
		return true;
	}

	public void doLogout(){
		Intent intent = new Intent(EventDetailsViewPager.this, Login.class);
        startActivity(intent);
        TabsActivity.tabActivity.finish();
	
	}

	public void showFriendProfile(int friendId){
		Intent intent = new Intent(EventDetailsViewPager.this,FriendsProfile.class);
		intent.putExtra(USERFINDID,friendId);
		TabGroupActivity pActivity = (TabGroupActivity)EventDetailsViewPager.this.getParent();
		pActivity.startChildActivity(FRIENDSPROFILE, intent);
	}

	public void goToNext(){
		int number = viewPager.getCurrentItem();
		number++;
		if(number<eventsList.size()){
			viewPager.setCurrentItem(number);
		}
	}
	
	public void goToPrevious(){
		int number = viewPager.getCurrentItem();
		number--;
		if(number>=0){
			viewPager.setCurrentItem(number);
		}
	}

	public void showCalendar(){
		TabsActivity.btnCalView.setBackgroundResource(R.drawable.btn_calview_hover);
		TabsActivity.btnListView.setBackgroundResource(R.drawable.btn_listview);
//		if("EventsGroupActivity".equals(this.getParent().getClass().getSimpleName())){
//		Log.v(LOGTAG, "parent name = "+ this.getParent().getClass().getSimpleName());
//			Events.isFirstTime=true;
//		Events.showCalendar=true;
			this.finish();	
			TabsActivity.btnCalView.performClick();
//		}
		//TODO: show current calendar view
//		Intent intent = new Intent(EventDetailsViewPager.this,Events.class);
//		TabGroupActivity pActivity = (TabGroupActivity)EventDetailsViewPager.this.getParent();
//		pActivity.startChildActivity("FriendsProfile", intent);
	}
	
	public String getParentName(){
		return this.getParent().getClass().getSimpleName();		
	}
}
