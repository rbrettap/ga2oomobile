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

public class FriendsGroupActivity extends TabGroupActivity
{
	
	private static final String FRIENDS ="Friends";
	private static final String FRIENDSPROFILE = "FriendsProfile";
	private static final String FRIENDSPALENDER = "FriendsPalender";
	private static final String EVENTDETAILSVIEWPAGE = "EventDetailsViewPager";
	
	public static FriendsGroupActivity friendsStack;
	
	static Map<String,Integer> searchValues;
	
	static{
		searchValues = new HashMap<String, Integer>();
		searchValues.put(FRIENDS, 1);
        searchValues.put(FRIENDSPROFILE, 2);
        searchValues.put(FRIENDSPALENDER, 3);
        searchValues.put(EVENTDETAILSVIEWPAGE, 4);
	}
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
       
        friendsStack = FriendsGroupActivity.this;
        
      startChildActivity(FRIENDS, new Intent(this, Friends.class));
    }
    
    @Override
   	public boolean onKeyDown(int keyCode, KeyEvent event) {
   		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
   			Log.v(Friends.LOGTAG, " home group search current activity = "+FriendsGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
   			switch(searchValues.get(FriendsGroupActivity.this.getCurrentActivity().getClass().getSimpleName())){
   			case(1):
   				FriendsGroupActivity.this.getCurrentActivity().findViewById(R.id.etSearchFriend).requestFocus();
   				break;
   			case(2):
   				//TODO: search function
   				break;
   			case(3):
   				FriendsGroupActivity.this.getCurrentActivity().findViewById(R.id.ivSearch).performClick();
   				break;
   			case(4):
   				//TODO: search function
   				break;
   			}
   		}
   		return super.onKeyDown(keyCode, event);
   	
    }
    
    @Override
   	public boolean onCreateOptionsMenu(Menu menu) {
       	Log.v(Events.LOGTAG, "create menu events group search current activity = "+FriendsGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
       	if(EVENTDETAILSVIEWPAGE.equals((FriendsGroupActivity.this.getCurrentActivity().getClass().getSimpleName()))){
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
   			((EventDetailsViewPager)FriendsGroupActivity.this.getCurrentActivity()).getCurrentView().findViewById(R.id.llStar).performClick();
   			return true;
   		case R.id.share_event:
   			((EventDetailsViewPager)FriendsGroupActivity.this.getCurrentActivity()).getCurrentView().findViewById(R.id.llShare).performClick();
   			return true;
   		case R.id.show_previous:
   			((EventDetailsViewPager)FriendsGroupActivity.this.getCurrentActivity()).goToPrevious();
   			return true;
   		case R.id.show_next:
   			((EventDetailsViewPager)FriendsGroupActivity.this.getCurrentActivity()).goToNext();
   			return true;
   		default:
   			return super.onOptionsItemSelected(item);
   		}
   	}

   	@Override
   	public boolean onMenuOpened(int featureId, Menu menu) {
   		Log.v(Events.LOGTAG, "open menu events group search current activity = "+FriendsGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
   		if(EVENTDETAILSVIEWPAGE.equals((FriendsGroupActivity.this.getCurrentActivity().getClass().getSimpleName()))){
   		MenuInflater inflater = getMenuInflater();
   		menu.clear();
   	    inflater.inflate(R.menu.event_details, menu);
   		return true;
   		}else{
   			return false;
   		}
   	}
}