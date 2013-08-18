package com.ga2oo.palendar;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class EventsGroupActivity extends TabGroupActivity
{
	public static EventsGroupActivity eventsStack;
	public static String query="";
	
	private static final String EVENTS = "Events";
	private static final String EVENTDETAILSVIEWPAGE = "EventDetailsViewPager";
	private static final String BUSINESSPALENDER ="BusinessPalender";
	private static final String FRIENDSPALENDER = "FriendsPalender";
	
	static Map<String,Integer> searchValues;
	
	static{
		searchValues = new HashMap<String, Integer>();
		searchValues.put(EVENTS, 1);
        searchValues.put(EVENTDETAILSVIEWPAGE, 2);
        searchValues.put(BUSINESSPALENDER, 3);
        searchValues.put(FRIENDSPALENDER, 4);
	}
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        query="";
        eventsStack = EventsGroupActivity.this;
        startChildActivity(EVENTS, new Intent(this, Events.class));
    }
    

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			Log.v(Events.LOGTAG, "events group search current activity = "+EventsGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
			switch(searchValues.get(EventsGroupActivity.this.getCurrentActivity().getClass().getSimpleName())){
			case(1):
				EventsGroupActivity.this.getCurrentActivity().findViewById(R.id.ivSearch).performClick();
				break;
			case(2):
				//TODO: search function
				break;
			case(3):
				EventsGroupActivity.this.getCurrentActivity().findViewById(R.id.ivSearch).performClick();
				break;
			case(4):
				EventsGroupActivity.this.getCurrentActivity().findViewById(R.id.ivSearch).performClick();
				break;
			}
			return true;
//		}else{
//			if(keyCode == KeyEvent.KEYCODE_BACK) {
//				Log.v(EVENTS, "isCalBtnClicked = "+Events.isCalBtnClicked);
//				Log.v(EVENTS, ""+EventsGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
//				if("Events".equals(EventsGroupActivity.this.getCurrentActivity().getClass().getSimpleName())){
//	//				((Events)EventsGroupActivity.this.getCurrentActivity()).buildCalView();
//					Log.v(EVENTS, "events back");
//					return true;
//				}else{
//					return false;
//				}
			}
		else{
			return super.onKeyDown(keyCode, event);
//		}
		}
	}
	
//	 @Override
//		public void onBackPressed() {
//		 Log.v(EVENTS, ""+EventsGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
//		 if("Events".equals(EventsGroupActivity.this.getCurrentActivity().getClass().getSimpleName()) && !Events.isCalBtnClicked){
//				Log.v(EVENTS, "events back isCalBtnClicked = "+Events.isCalBtnClicked);
//			 TabsActivity.btnCalView.performClick();				
//		 }else{
//			super.onBackPressed();
//		 }
//		}


    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	Log.v(Events.LOGTAG, "create menu events group search current activity = "+EventsGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
    	if(EVENTDETAILSVIEWPAGE.equals((EventsGroupActivity.this.getCurrentActivity().getClass().getSimpleName()))){
		 MenuInflater inflater = getMenuInflater();
		 menu.clear();
		 inflater.inflate(R.menu.event_details, menu);
		 
		    return true;
    	}else{
    		return false;
    	}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.add_to_favorite:
			((EventDetailsViewPager)EventsGroupActivity.this.getCurrentActivity()).getCurrentView().findViewById(R.id.llStar).performClick();
			return true;
		case R.id.share_event:
			((EventDetailsViewPager)EventsGroupActivity.this.getCurrentActivity()).getCurrentView().findViewById(R.id.llShare).performClick();
			return true;
		case R.id.show_previous:
			((EventDetailsViewPager)EventsGroupActivity.this.getCurrentActivity()).goToPrevious();
			return true;
		case R.id.show_next:
			((EventDetailsViewPager)EventsGroupActivity.this.getCurrentActivity()).goToNext();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		Log.v(Events.LOGTAG, "open menu events group search current activity = "+EventsGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
		if(EVENTDETAILSVIEWPAGE.equals((EventsGroupActivity.this.getCurrentActivity().getClass().getSimpleName()))){
		MenuInflater inflater = getMenuInflater();
		menu.clear();
	    inflater.inflate(R.menu.event_details, menu);
		return true;
		}else{
			return false;
		}
	}
}