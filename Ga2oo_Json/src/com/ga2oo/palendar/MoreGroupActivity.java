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

public class MoreGroupActivity extends TabGroupActivity
{
	
	private static final String MORE = "More";
	private static final String PROFILESETTINGS ="ProfileSettings";
	private static final String MYCIRCCLE = "MyCircle";
	private static final String FRIENDSPROFILE = "FriendsProfile";
	private static final String FRIENDSPALENDER = "FriendsPalender";
	private static final String FAVORITES = "Favorites";
	private static final String BUSINESSDETAIL = "BusinessDetail";
	private static final String BUSINESSPALENDER ="BusinessPalender";
	private static final String ACCOUNT = "Account";
	private static final String ABOUT = "About";
	private static final String FAQ = "FAQ";
	private static final String CONTACT = "Contact";
	private static final String EVENTDETAILSVIEWPAGE = "EventDetailsViewPager";
	
	public static MoreGroupActivity moreStack;
	
	static Map<String,Integer> searchValues;
	
	static{
		searchValues = new HashMap<String, Integer>();
		searchValues.put(MORE, 1);
        searchValues.put(PROFILESETTINGS, 2);
        searchValues.put(MYCIRCCLE, 3);
        searchValues.put(FRIENDSPROFILE, 4);
        searchValues.put(FRIENDSPALENDER, 5);
        searchValues.put(FAVORITES, 6);
        searchValues.put(BUSINESSDETAIL, 7);
        searchValues.put(BUSINESSPALENDER, 8);
        searchValues.put(ACCOUNT, 9);
        searchValues.put(ABOUT, 10);
        searchValues.put(FAQ, 11);
        searchValues.put(CONTACT, 12);
        searchValues.put(EVENTDETAILSVIEWPAGE, 13);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        moreStack = MoreGroupActivity.this;
        
        startChildActivity(MORE, new Intent(this, More.class));
    }
    
    @Override
   	public boolean onKeyDown(int keyCode, KeyEvent event) {
   		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
   			Log.v(More.LOGTAG, " home group search current activity = "+MoreGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
   			switch(searchValues.get(MoreGroupActivity.this.getCurrentActivity().getClass().getSimpleName())){
   			case(1):
   				//TODO: search function
   				break;
   			case(2):
   				//TODO: search function
   				break;
   			case(3):
   				MoreGroupActivity.this.getCurrentActivity().findViewById(R.id.etSearchFriend).requestFocus();
   				break;
   			case(4):
   				//TODO: search function
   				break;
   			case(5):
   				MoreGroupActivity.this.getCurrentActivity().findViewById(R.id.ivSearch).performClick();
   				break;
   			case(6):
   				MoreGroupActivity.this.getCurrentActivity().findViewById(R.id.etSearchList).requestFocus();
   				break;
   			case(7):
   				//TODO: search function
   				break;
   			case(8):
   				MoreGroupActivity.this.getCurrentActivity().findViewById(R.id.ivSearch).performClick();
   				break;
   			case(9):
   				//TODO: search function
   				break;
   			case(10):
   				//TODO: search function
   				break;
   			case(11):
   				//TODO: search function
   				break;
   			case(12):
   				//TODO: search function
   				break;
   			case(13):
   				//TODO: search function
   				break;
   			}
   		}
   		return super.onKeyDown(keyCode, event);
   	
    }
    
    @Override
   	public boolean onCreateOptionsMenu(Menu menu) {
       	Log.v(Events.LOGTAG, "create menu events group search current activity = "+MoreGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
       	if(EVENTDETAILSVIEWPAGE.equals((MoreGroupActivity.this.getCurrentActivity().getClass().getSimpleName()))){
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
   			((EventDetailsViewPager)MoreGroupActivity.this.getCurrentActivity()).getCurrentView().findViewById(R.id.llStar).performClick();
   			return true;
   		case R.id.share_event:
   			((EventDetailsViewPager)MoreGroupActivity.this.getCurrentActivity()).getCurrentView().findViewById(R.id.llShare).performClick();
   			return true;
   		case R.id.show_previous:
   			((EventDetailsViewPager)MoreGroupActivity.this.getCurrentActivity()).goToPrevious();
   			return true;
   		case R.id.show_next:
   			((EventDetailsViewPager)MoreGroupActivity.this.getCurrentActivity()).goToNext();
   			return true;
   		default:
   			return super.onOptionsItemSelected(item);
   		}
   	}

   	@Override
   	public boolean onMenuOpened(int featureId, Menu menu) {
   		Log.v(Events.LOGTAG, "open menu events group search current activity = "+MoreGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
   		if(EVENTDETAILSVIEWPAGE.equals((MoreGroupActivity.this.getCurrentActivity().getClass().getSimpleName()))){
   		MenuInflater inflater = getMenuInflater();
   		menu.clear();
   	    inflater.inflate(R.menu.event_details, menu);
   		return true;
   		}else{
   			return false;
   		}
   	}
}