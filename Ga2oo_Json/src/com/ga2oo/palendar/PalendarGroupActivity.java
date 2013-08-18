package com.ga2oo.palendar;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.DialogUtility;
import com.ga2oo.palendar.controls.PopUpDailog;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;

public class PalendarGroupActivity extends TabGroupActivity
{
	
	private static final String PALENDAR = "Palendar";
	private static final String BUSINESSPALENDER ="BusinessPalender";
	private static final String FRIENDSPROFILE = "FriendsProfile";
	private static final String FRIENDSPALENDER = "FriendsPalender";
	private static final String EVENTDETAILSVIEWPAGE = "EventDetailsViewPager";
	
	private	PopUpDailog customDialog = null;
	private int status = 0;
	
	public static PalendarGroupActivity palendarStack;
	String strAttendingEvents;
	
	static Map<String,Integer> searchValues;
	
	static{
		searchValues = new HashMap<String, Integer>();
		searchValues.put(PALENDAR, 1);
        searchValues.put(EVENTDETAILSVIEWPAGE, 2);
        searchValues.put(BUSINESSPALENDER, 3);
        searchValues.put(FRIENDSPROFILE, 4);
        searchValues.put(FRIENDSPALENDER, 5);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        palendarStack = PalendarGroupActivity.this;
        startChildActivity(PALENDAR, new Intent(this, Palendar.class));
    }
    
    @Override
   	public boolean onKeyDown(int keyCode, KeyEvent event) {
   		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
   			Log.v(Palendar.LOGTAG, " home group search current activity = "+PalendarGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
   			switch(searchValues.get(PalendarGroupActivity.this.getCurrentActivity().getClass().getSimpleName())){
   			case(1):
   				PalendarGroupActivity.this.getCurrentActivity().findViewById(R.id.ivSearch).performClick();
   				break;
   			case(2):
   				//TODO: search function
   				break;
   			case(3):
   				PalendarGroupActivity.this.getCurrentActivity().findViewById(R.id.ivSearch).performClick();
   				break;
   			case(4):
   				//TODO: search function
   				break;
   			case(5):
   				PalendarGroupActivity.this.getCurrentActivity().findViewById(R.id.ivSearch).performClick();
   				break;
   			}
   		}
   		return super.onKeyDown(keyCode, event);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	Log.v(Events.LOGTAG, "create menu events group search current activity = "+PalendarGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
    	if(EVENTDETAILSVIEWPAGE.equals((PalendarGroupActivity.this.getCurrentActivity().getClass().getSimpleName()))){
		 MenuInflater inflater = getMenuInflater();
		 menu.clear();
		 inflater.inflate(R.menu.palendar_event_details, menu);
		 
		    return true;
    	}else{
    		return false;
    	}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.add_to_favorite:
			((EventDetailsViewPager)PalendarGroupActivity.this.getCurrentActivity()).getCurrentView().findViewById(R.id.llStar).performClick();
			return true;
		case R.id.share_event:
			((EventDetailsViewPager)PalendarGroupActivity.this.getCurrentActivity()).getCurrentView().findViewById(R.id.llShare).performClick();
			return true;
		case R.id.show_previous:
			((EventDetailsViewPager)PalendarGroupActivity.this.getCurrentActivity()).goToPrevious();
			return true;
		case R.id.show_next:
			((EventDetailsViewPager)PalendarGroupActivity.this.getCurrentActivity()).goToNext();
			return true;
		case R.id.delete_event:
			deleteEvent();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	private void deleteEvent(){
		final EventsBusinessLayer eventsBL = new EventsBusinessLayer();
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.confirmation_popup, null);
		Button btnYes = (Button)popup.findViewById(R.id.btnYes);
		Button btnNo = (Button)popup.findViewById(R.id.btnNo);
		TextView tvMessage=(TextView)popup.findViewById(R.id.tvMessage);
		tvMessage.setText(getResources().getString(R.string.sure_to_delete_event_from_palender));
		customDialog = new PopUpDailog(getParent(), popup, 220, LayoutParams.WRAP_CONTENT);
		customDialog.show();
		btnYes.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				UserAccountBusinessLayer userAccBL=new UserAccountBusinessLayer();
				String currentEventId = userAccBL.getUserAddedEventId((Integer)((EventDetailsViewPager)PalendarGroupActivity.this.getCurrentActivity()).getCurrentView().getTag());
				if(currentEventId!=null){
					status = Ga2ooJsonParsers.deleteUserEvent(AppConstants.USER_ID, Integer.parseInt(currentEventId));
					if(status>0){
						userAccBL.deleteUserFavorites(AppConstants.USER_ID, (Integer)((EventDetailsViewPager)PalendarGroupActivity.this.getCurrentActivity()).getCurrentView().getTag());
						strAttendingEvents	= eventsBL.getLogedInUserAttendingEvents(AppConstants.USER_ID);
						customDialog.dismiss();
						((EventDetailsViewPager)PalendarGroupActivity.this.getCurrentActivity()).finish();
					}else{
						customDialog.dismiss();
						DialogUtility.showConnectionErrorDialog(PalendarGroupActivity.this);
					}
				}else{
					customDialog.dismiss();
					DialogUtility.showConnectionErrorDialog(PalendarGroupActivity.this);
				}
			
				
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
		
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		Log.v(Events.LOGTAG, "open menu events group search current activity = "+PalendarGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
		if(EVENTDETAILSVIEWPAGE.equals((PalendarGroupActivity.this.getCurrentActivity().getClass().getSimpleName()))){
		MenuInflater inflater = getMenuInflater();
		menu.clear();
	    inflater.inflate(R.menu.palendar_event_details, menu);
		return true;
		}else{
			return false;
		}
	}
}