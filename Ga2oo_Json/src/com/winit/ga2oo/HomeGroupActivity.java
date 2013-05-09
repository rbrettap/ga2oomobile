package com.winit.ga2oo;

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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.winit.ga2oo.controls.PopUpDailog;

public class HomeGroupActivity extends TabGroupActivity
{
	
	private static final String HOME = "Home";
	private static final String BUSINESSPALENDER ="BusinessPalender";
	private static final String EVENTDETAILSVIEWPAGE = "EventDetailsViewPager";
	private static final String UPCOMINGEVENTS = "UpcomingEvents";
	private static final String FRIENDSPROFILE = "FriendsProfile";
	private static final String FRIENDSPALENDER = "FriendsPalender";
	private static final String SEARCHKEYWORD= "searchKeyword";
	private static final String SEARCHBY = "searchBy";
	private static final String KEYWORD = "Keyword";
	private static final String SEARCHEDEVENT = "SearchedEvent";
	private static final String LOCATION = "Location";
	
	public static HomeGroupActivity homeStack;
	private EditText etQuicksearchBykey,etQuicksearchByLocation;
	private PopUpDailog customDialog;
	
	static Map<String,Integer> searchValues;
	
	static{
		searchValues = new HashMap<String, Integer>();
		searchValues.put(HOME, 1);
        searchValues.put(UPCOMINGEVENTS, 2);
        searchValues.put(EVENTDETAILSVIEWPAGE, 3);
        searchValues.put(BUSINESSPALENDER, 4);
        searchValues.put(FRIENDSPROFILE, 5);
        searchValues.put(FRIENDSPALENDER, 6);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        homeStack = HomeGroupActivity.this;
        
        startChildActivity(HOME, new Intent(this, Home.class));

    }
    
    public void showHomeQuickSearchPopup()
	{
		LayoutInflater inflater = getLayoutInflater();
		LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.quick_search_popup, null);
		etQuicksearchBykey	 		 		 = (EditText)popup.findViewById(R.id.etQuicksearchBykey);
		etQuicksearchByLocation	 			 = (EditText)popup.findViewById(R.id.etQuicksearchByLocation);
		Button btnClose				 		 = (Button)popup.findViewById(R.id.btnClose);
		Button btnSearchByKeyword 	 		 = (Button)popup.findViewById(R.id.btnSearchByKeyword);
		Button btnSearchByLocation			 = (Button)popup.findViewById(R.id.btnSearchByLocation);
		RelativeLayout rlCross				 = (RelativeLayout)popup.findViewById(R.id.rlCross);
	
		rlCross.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
			}
		});
		btnClose.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
			}
		});
		
		btnSearchByKeyword.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				String searchKeyword;
				searchKeyword=etQuicksearchBykey.getText().toString();
				Intent inttentSearchedEvent = new Intent(HomeGroupActivity.this, SearchedEvent.class);
				inttentSearchedEvent.putExtra(SEARCHKEYWORD, searchKeyword);
				inttentSearchedEvent.putExtra(SEARCHBY, KEYWORD);
				overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
				TabGroupActivity pActivity = (TabGroupActivity)HomeGroupActivity.this;
				pActivity.startChildActivity(SEARCHEDEVENT, inttentSearchedEvent);
				customDialog.dismiss();
			}
		});

		btnSearchByLocation.setOnClickListener(new OnClickListener()//currently same as search by keyword
		{					
			@Override
			public void onClick(View v) 
			{
				String searchKeyword;
				searchKeyword=etQuicksearchByLocation.getText().toString();
				Intent inttentSearchedEvent = new Intent(HomeGroupActivity.this, SearchedEvent.class);
				inttentSearchedEvent.putExtra(SEARCHKEYWORD, searchKeyword);
				inttentSearchedEvent.putExtra(SEARCHBY, LOCATION);
				overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
				TabGroupActivity pActivity = (TabGroupActivity)HomeGroupActivity.this;
				pActivity.startChildActivity(SEARCHEDEVENT, inttentSearchedEvent);
				customDialog.dismiss();
			}
		});
		customDialog = new PopUpDailog(getParent(), popup, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			Log.v(Home.LOGTAG, " home group search current activity = "+HomeGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
			switch(searchValues.get(HomeGroupActivity.this.getCurrentActivity().getClass().getSimpleName())){
			case(1):
				showHomeQuickSearchPopup();
				break;
			case(2):
				HomeGroupActivity.this.getCurrentActivity().findViewById(R.id.etSearchUpcoming).requestFocus();
				break;
			case(3):
				//TODO: search function
				break;
			case(4):
				HomeGroupActivity.this.getCurrentActivity().findViewById(R.id.ivSearch).performClick();
				break;
			case(5):
				//TODO: search function
				break;
			case(6):
				HomeGroupActivity.this.getCurrentActivity().findViewById(R.id.ivSearch).performClick();
				break;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
    
    @Override
   	public boolean onCreateOptionsMenu(Menu menu) {
       	Log.v(Events.LOGTAG, "create menu events group search current activity = "+HomeGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
       	if(EVENTDETAILSVIEWPAGE.equals((HomeGroupActivity.this.getCurrentActivity().getClass().getSimpleName()))){
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
   			((EventDetailsViewPager)HomeGroupActivity.this.getCurrentActivity()).getCurrentView().findViewById(R.id.llStar).performClick();
   			return true;
   		case R.id.share_event:
   			((EventDetailsViewPager)HomeGroupActivity.this.getCurrentActivity()).getCurrentView().findViewById(R.id.llShare).performClick();
   			return true;
   		case R.id.show_previous:
   			((EventDetailsViewPager)HomeGroupActivity.this.getCurrentActivity()).goToPrevious();
   			return true;
   		case R.id.show_next:
   			((EventDetailsViewPager)HomeGroupActivity.this.getCurrentActivity()).goToNext();
   			return true;
   		default:
   			return super.onOptionsItemSelected(item);
   		}
   	}

   	@Override
   	public boolean onMenuOpened(int featureId, Menu menu) {
   		Log.v(Events.LOGTAG, "open menu events group search current activity = "+HomeGroupActivity.this.getCurrentActivity().getClass().getSimpleName());
   		if(EVENTDETAILSVIEWPAGE.equals((HomeGroupActivity.this.getCurrentActivity().getClass().getSimpleName()))){
   		MenuInflater inflater = getMenuInflater();
   		menu.clear();
   	    inflater.inflate(R.menu.event_details, menu);
   		return true;
   		}else{
   			return false;
   		}
   	}
}