package com.ga2oo.palendar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.DialogUtility;
import com.ga2oo.palendar.controls.PopUpDailog;
import com.ga2oo.palendar.objects.Business;
import com.ga2oo.palendar.objects.EventsDetails;
import com.ga2oo.palendar.objects.FavoriteEvent;
import com.ga2oo.jsonparsers.BusinessToFavoriteWrapper;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;
import com.ga2oo.parsing.net.JsonHttpHelper;

public class Favorites extends Activity 
{
	
	private final String LOGTAG = "FavoriteScreen";
	private static final String EVENTID = "EventID";
	private static final String EVENTDETAILS = "EventDetails";
	private static final String BUSINESSID = "BusinessId";
	private static final String BUSINESSDETAIL = "Business Detail";
	
	private EditText etSearchList;
	private Button btnCancel;
	private LinearLayout llFavoriteBusiness,layoutSettings;
	private ListView lvFavorite,lvFavoriteBusiness;
	private TextView tvFBusiness,tvNotification;
	
	private UserAccountBusinessLayer userAccBL;
	private EventsBusinessLayer eventBL;
	private DrawableManager objDrawableManager;
	private ProgressDialog progressDialog;
	private CustomFavorit objCustomFavorit;
	private CustomFavoriteBusiness objCustomFavoritBusiness;
	private List<EventsDetails> vctAllFavoriteEventDetails;
	private List<Business> vctFavoriteBusiness;
	private PopUpDailog customDialog;
	private View vSeprator;
	
	public String strFavoriteEventIds="",strSearchKeyWord="";
	public static boolean isfirst=false,isEditClicked=false;
	public static int positionFavEvents;
	
	private JsonElement element;
	private JsonHttpHelper jsonHelper;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
	
    }
	
	@Override
    public void onResume()
    {
    	super.onResume();
    	isEditClicked=false;
		setContentView(R.layout.favorite_view);	
		layoutSettings				=	(LinearLayout)findViewById(R.id.layoutSettings);
		objDrawableManager			=	new DrawableManager();
		userAccBL					=	new UserAccountBusinessLayer();
		eventBL						=	new EventsBusinessLayer();
		vctAllFavoriteEventDetails	=	new ArrayList<EventsDetails>();
		vctFavoriteBusiness			=	new ArrayList<Business>();
		etSearchList 				= 	(EditText)findViewById(R.id.etSearchList);
		btnCancel					=	(Button)findViewById(R.id.btn_cancel);
		//lvFavorite					=	(ListView)findViewById(R.id.lvFavorites);
		llFavoriteBusiness			=	(LinearLayout)findViewById(R.id.llFavoriteBusiness);
		tvFBusiness					=	(TextView)findViewById(R.id.tvFBusiness);
		vSeprator					=	(View)findViewById(R.id.vSeprator);
		tvNotification				=	(TextView)findViewById(R.id.tvNotification);
		lvFavorite					=	new ListView(this);
		lvFavorite.setScrollContainer(false);
		//lvFavoriteBusiness			=	new ListView(this);
		lvFavoriteBusiness			=	(ListView)findViewById(R.id.lvFavoriteBusiness);
		lvFavorite.setCacheColorHint(0);
		lvFavoriteBusiness.setCacheColorHint(0);
		lvFavorite.setFadingEdgeLength(0);
		lvFavorite.setScrollbarFadingEnabled(true);
		lvFavoriteBusiness.setFadingEdgeLength(0);
		lvFavoriteBusiness.setScrollbarFadingEnabled(true);
		
		jsonHelper = JsonHttpHelper.getInstance();
		
		vSeprator.setVisibility(View.GONE);
		layoutSettings.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etSearchList.getWindowToken(), 0);
			}
		});
		if(progressDialog!=null && progressDialog.isShowing())
    		progressDialog.dismiss();
    	
    	progressDialog = new ProgressDialog(getParent());
		progressDialog.setMessage(getResources().getString(R.string.loading));
//    	progressDialog.show();
    	
    	new LoadFavoriteBusinesses().execute();
    	
    	if(objCustomFavorit != null)
    		objCustomFavorit.refresh(vctAllFavoriteEventDetails);
    	if(objCustomFavoritBusiness!=null)
    		objCustomFavoritBusiness.refresh(AppConstants.vctUserFavoritesBusiness);
    	objDrawableManager=new DrawableManager();
        TabsActivity.btnBack.setVisibility(View.VISIBLE);
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar_empty);
        TabsActivity.tvTitle.setText(getResources().getString(R.string.favorites));
        TabsActivity.tvTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        TabsActivity.tvTitle.setVisibility(View.VISIBLE);
        TabsActivity.btnLogout.setBackgroundResource(R.drawable.edit);
        TabsActivity.btnLogout.setVisibility(View.VISIBLE);
        TabsActivity.rlCommonTitleBar.invalidate();
        TabsActivity.btnLogout.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				if(!isEditClicked)
				{
					if(objCustomFavoritBusiness!=null)
			    		objCustomFavoritBusiness.refresh();
					 TabsActivity.btnLogout.setBackgroundResource(R.drawable.cancelbtn);
					if(positionFavEvents==0)
					{
						ImageView ivDelete=new ImageView(Favorites.this);
						ivDelete=(ImageView)findViewById(positionFavEvents);
						if(ivDelete!=null)
							ivDelete.setVisibility(View.VISIBLE);
					}
						
					for(int i=0;i<=positionFavEvents;i++)
					{
						ImageView ivDelete=new ImageView(Favorites.this);
						ivDelete=(ImageView)findViewById(i);
						if(ivDelete!=null)
							ivDelete.setVisibility(View.VISIBLE);
					}
					isEditClicked=true;
				}
				else if(isEditClicked)
				{
					if(objCustomFavoritBusiness!=null)
			    		objCustomFavoritBusiness.refresh();
					TabsActivity.btnLogout.setBackgroundResource(R.drawable.edit);
					if(positionFavEvents==0)
					{
						ImageView ivDelete=new ImageView(Favorites.this);
						ivDelete=(ImageView)findViewById(positionFavEvents);
						if(ivDelete!=null)
							ivDelete.setVisibility(View.GONE);
					}

					for(int i=0;i<=positionFavEvents;i++)
					{
						ImageView ivDelete=new ImageView(Favorites.this);
						ivDelete=(ImageView)findViewById(i);
						if(ivDelete!=null)
							ivDelete.setVisibility(View.GONE);
					}
					isEditClicked=false;
				}
			}
		});
        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				TabGroupActivity pActivity = (TabGroupActivity)Favorites.this.getParent();
				pActivity.finishFromChild(null);
			}
		});
        
    }
	
	private class LoadFavoriteBusinesses extends AsyncTask<Void,Void,Boolean>{

		@Override
		protected void onPreExecute() {
			progressDialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			AppConstants.vctUserFavoritesBusiness = new ArrayList<Business>();
			AppConstants.vctUserFavorites = new ArrayList<FavoriteEvent>();
			Log.i(LOGTAG, "Loading user favorite businesses.");
			try {
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.ADD_BUSINESS+"id/"+AppConstants.USER_ID);
			}
			 catch (Exception e) {
				 Log.e(LOGTAG, "Loading user favorite businesses.");
				e.printStackTrace();
				return false;
			}
			Object favoriteObjects = jsonHelper.parse(element, BusinessToFavoriteWrapper.class);
			if(favoriteObjects!=null){
				if(((BusinessToFavoriteWrapper) favoriteObjects).getUseraccount().fav_businesses!=null){
					AppConstants.vctUserFavoritesBusiness.addAll(((BusinessToFavoriteWrapper) favoriteObjects).getUseraccount().fav_businesses);
					for(int i=0;i<((BusinessToFavoriteWrapper) favoriteObjects).getUseraccount().fav_businesses.size();i++){
						userAccBL.InsertUserBusiness(((BusinessToFavoriteWrapper) favoriteObjects).getUseraccount().fav_businesses.get(i));
					}
				}
				Log.i(LOGTAG, "Loading user favorite businesses successfully completed.");
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			if(!result){
				DialogUtility.showConnectionErrorDialog(Favorites.this.getParent());
			}
				isfirst=true;
				vctAllFavoriteEventDetails	=eventBL.getFavoriteEventDetails();
				vctFavoriteBusiness			=userAccBL.getUserFavouriteBusiness(AppConstants.USER_ID,strSearchKeyWord);
				
			etSearchList.setCursorVisible(false);
			etSearchList.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					etSearchList.setCursorVisible(true);
				}
			});
			
			etSearchList.setOnKeyListener(new OnKeyListener() 
			{
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event)
				{
					etSearchList.setCursorVisible(true);
					if(keyCode == KeyEvent.KEYCODE_ENTER)
					{
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(etSearchList.getWindowToken(), 0);
						return true;
					}
					return false;
				}
			});
			
			etSearchList.addTextChangedListener(new TextWatcher() 
			{
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) 
				{
					vctFavoriteBusiness.clear();
					vctFavoriteBusiness=userAccBL.getUserFavouriteBusiness(AppConstants.USER_ID,s.toString());
					if(vctFavoriteBusiness.size()!=0&&vctFavoriteBusiness!=null)
					{
						objCustomFavoritBusiness.refresh(vctFavoriteBusiness);
						tvNotification.setVisibility(View.GONE);
					}
					else
					{
						tvNotification.setVisibility(View.VISIBLE);
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,int after)
				{
				}
				@Override
				public void afterTextChanged(Editable s)
				{
				}
			});
			
			btnCancel.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					vctFavoriteBusiness.clear();
					vctFavoriteBusiness=userAccBL.getUserFavouriteBusiness(AppConstants.USER_ID,"");
					if(vctFavoriteBusiness.size()!=0)
						objCustomFavoritBusiness.refresh(vctFavoriteBusiness);
					etSearchList.setText("");
				}
			});
			
			
			if(vctAllFavoriteEventDetails.size()!=0)
			{
				lvFavorite.setAdapter(objCustomFavorit=new CustomFavorit(vctAllFavoriteEventDetails));
			}
			
			if(vctFavoriteBusiness!=null)
				if(vctFavoriteBusiness.size()!=0)
				{
					llFavoriteBusiness.removeAllViews();
					lvFavoriteBusiness.setAdapter(objCustomFavoritBusiness=new CustomFavoriteBusiness(vctFavoriteBusiness));
					llFavoriteBusiness.addView(lvFavoriteBusiness,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
					tvFBusiness.setVisibility(View.GONE);
				}
			super.onPostExecute(result);
		}
		
	}

	public class CustomFavorit extends BaseAdapter 
	{
		private List<EventsDetails> vctAllFavoriteEventDetails;
		public CustomFavorit(List<EventsDetails> vctAllFavoriteEventDetails)
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
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.upcoming_event_cell, null);
			final int eventID=objEventsDetails.eventid;
			String strEventName=objEventsDetails.eventname;
			String strStartDate=objEventsDetails.event_start_date;
			convertView.setId(eventID);
			TextView tvEventName=(TextView)convertView.findViewById(R.id.tvEventName);
		    TextView tvStartDate=(TextView)convertView.findViewById(R.id.tvEventStartDate);
		    ImageView ivEventImage=(ImageView)convertView.findViewById(R.id.ivEventImage);
		    final ImageView ivDelete=(ImageView)convertView.findViewById(R.id.btnDelete);
		    ivDelete.setBackgroundResource(R.drawable.delete);
		    ivDelete.setId(position);
		    if(!isEditClicked)
		    {
		    	ivDelete.setVisibility(View.GONE);
		    }
		    else if(isEditClicked)
		    {
		    	ivDelete.setVisibility(View.VISIBLE);
		    }
		    ProgressBar upcomingeventProgressbar=(ProgressBar)convertView.findViewById(R.id.upcomingeventProgressbar);
			tvEventName.setText(strEventName);
			tvStartDate.setText(strStartDate);
			if(!objEventsDetails.image.equals(AppConstants.NOIMAGE))
			{
				if(!objDrawableManager.fetchDrawableOnThread(AppConstants.IMAGE_HOST_URL+objEventsDetails.image, ivEventImage, 50, 50,objEventsDetails.imageId,objEventsDetails.isImageUpdated)){
					upcomingeventProgressbar.setVisibility(View.GONE);
					ivEventImage.setImageResource(R.drawable.no_image_smal_event);
				}
			}
			else
			{
				upcomingeventProgressbar.setVisibility(View.GONE);
				ivEventImage.setImageResource(R.drawable.no_image_smal_event);
			}
			ivDelete.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					LayoutInflater inflater = getLayoutInflater();
					LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.confirmation_popup, null);
					Button btnYes = (Button)popup.findViewById(R.id.btnYes);
					Button btnNo = (Button)popup.findViewById(R.id.btnNo);
					TextView tvMessage=(TextView)popup.findViewById(R.id.tvMessage);
					tvMessage.setText(getResources().getString(R.string.sure_to_delete_event)+objEventsDetails.eventname+getResources().getString(R.string.from_your_favorite));
					btnYes.setOnClickListener(new OnClickListener()
					{					
						@Override
						public void onClick(View v) 
						{
						    Ga2ooJsonParsers.getInstance().deleteUserEvent(AppConstants.USER_ID, objEventsDetails.useraddedeventid);
							vctAllFavoriteEventDetails.remove(position);
							objCustomFavorit.notifyDataSetChanged();
							userAccBL.deleteUserFavorites(AppConstants.USER_ID, eventID);
							customDialog.dismiss();
						}
					});
						
					btnNo.setOnClickListener(new OnClickListener()
					{					
						@Override
						public void onClick(View v) 
						{
							customDialog.dismiss();
						}
					});
					customDialog = new PopUpDailog(getParent(), popup, 220, LayoutParams.WRAP_CONTENT);
					customDialog.show();
					
				}
			});
			convertView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					Intent inttentEventDetails = new Intent(Favorites.this, EventDetails.class);
					inttentEventDetails.putExtra(EVENTID,v.getId());
					TabGroupActivity pActivity = (TabGroupActivity)Favorites.this.getParent();
					pActivity.startChildActivity(EVENTDETAILS, inttentEventDetails);
				}
			});
	       
			positionFavEvents=position;
	       
			return convertView;
		}
	}
	
	

	public class CustomFavoriteBusiness extends BaseAdapter 
	{
		private List<Business> vctAllFavoriteBusiness;
		public CustomFavoriteBusiness(List<Business> vctAllFavoriteBusiness)
		{
			this.vctAllFavoriteBusiness = vctAllFavoriteBusiness;
		}
		
		@Override
		public int getCount()
		{
			if(vctAllFavoriteBusiness != null)
				return vctAllFavoriteBusiness.size();
			return 0;
		}
		
		private void refresh(List<Business> vctAllFavoriteBusiness)
		{
			this.vctAllFavoriteBusiness = vctAllFavoriteBusiness;
			this.notifyDataSetChanged();
		}
		private void refresh()
		{
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
			final Business objBusiness = vctAllFavoriteBusiness.get(position);
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.upcoming_event_cell, null);
			int BusinessId=objBusiness.businessid;
			String strEventName=objBusiness.businessname;
			convertView.setId(BusinessId);
			RelativeLayout rlEventImage=(RelativeLayout)convertView.findViewById(R.id.rlEventImage);
			TextView tvEventName=(TextView)convertView.findViewById(R.id.tvEventName);
		    TextView tvStartDate=(TextView)convertView.findViewById(R.id.tvEventStartDate);
		    tvStartDate.setVisibility(View.GONE);
		    ImageView ivEventImage=(ImageView)convertView.findViewById(R.id.ivEventImage);
		    ImageView ivDeleteBusiness=(ImageView)convertView.findViewById(R.id.btnDelete);
		    ivDeleteBusiness.setId(BusinessId);
		    if(!isEditClicked)
		    {
		    	ivDeleteBusiness.setBackgroundResource(R.drawable.arrow);
		    	ivDeleteBusiness.setVisibility(View.VISIBLE);
		    }
		    else if(isEditClicked)
		    {
		    	ivDeleteBusiness.setBackgroundResource(R.drawable.deletebtn_click);
		    	ivDeleteBusiness.setVisibility(View.VISIBLE);
		    }
		    ivDeleteBusiness.setOnClickListener(new OnClickListener()
		    {
				@Override
				public void onClick(View v) 
				{
					if(isEditClicked)
				    {
					LayoutInflater inflater = getLayoutInflater();
					LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.confirmation_popup, null);
					Button btnYes = (Button)popup.findViewById(R.id.btnYes);
					Button btnNo = (Button)popup.findViewById(R.id.btnNo);
					TextView tvMessage=(TextView)popup.findViewById(R.id.tvMessage);
					tvMessage.setText(getResources().getString(R.string.sure_to_delete_favorite)+objBusiness.businessname+getResources().getString(R.string.from_your_favorite));
					btnYes.setOnClickListener(new OnClickListener()
					{					
						@Override
						public void onClick(View v) 
						{
							Log.i("objBusiness.useraddedbusinessid",""+objBusiness.useraddedbusinessid);
							Ga2ooJsonParsers.getInstance().deleteUserBusiness(AppConstants.USER_ID, objBusiness.useraddedbusinessid);
							customDialog.dismiss();
							vctAllFavoriteBusiness.remove(position);
							objCustomFavoritBusiness.notifyDataSetChanged();
							userAccBL.deleteUserFavoritesBusiness(AppConstants.USER_ID, objBusiness.businessid);
						}
					});
						
					btnNo.setOnClickListener(new OnClickListener()
					{					
						@Override
						public void onClick(View v) 
						{
							customDialog.dismiss();
						}
					});
					customDialog = new PopUpDailog(getParent(), popup, 220, LayoutParams.WRAP_CONTENT);
					customDialog.show();
				    }
				}
			});
		    convertView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					Intent inttentEventDetails = new Intent(Favorites.this, BusinessDetail.class);
					inttentEventDetails.putExtra(BUSINESSID,v.getId());
					TabGroupActivity pActivity = (TabGroupActivity)Favorites.this.getParent();
					pActivity.startChildActivity(BUSINESSDETAIL, inttentEventDetails);
				}
			});
		    ProgressBar upcomingeventProgressbar=(ProgressBar)convertView.findViewById(R.id.upcomingeventProgressbar);
			tvEventName.setText(strEventName);
			upcomingeventProgressbar.setVisibility(View.GONE);
			ivEventImage.setImageResource(R.drawable.no_image_smal_event);
			ivEventImage.setVisibility(View.GONE);
			rlEventImage.setVisibility(View.GONE);
			return convertView;
		}
	}

	@Override
	protected void onPause()
	{
		llFavoriteBusiness.removeAllViews();
		
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
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//			etSearchList.requestFocus();
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
}
