package com.ga2oo.palendar;

import com.ga2oo.palendar.services.FriendsService;

import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

public class TabsActivity extends TabActivity implements OnTabChangeListener
{	
	private static final String TABSACTIVITY = "TabsActivity";
	private static final String MORE = "More";
	private static final String FRIENDS = "Friends";
	private static final String PALENDAR = "Palendar";
	private static final String EVENTS = "Events";
	private static final String HOME = "Home";
	private static final String PARENT_PAGE = "parentPage";
	private static final String COM_GA2OO = "com.ga2oo.palendar";
	private static final String SELECTED_ID = "selectedId";
	private static final String MORE_GROUP_ACTIVITY = "MoreGroupActivity";
	private static final String FRIENDS_GROUP_ACTIVITY = "FriendsGroupActivity";
	private static final String PALENDAR_GROUP_ACTIVITY = "PalendarGroupActivity";
	private static final String EVENTS_GROUP_ACTIVITY = "EventsGroupActivity";
	private static final String HOME_GROUP_ACTIVITY = "HomeGroupActivity";
	private static final String[] TABS = {HOME_GROUP_ACTIVITY, EVENTS_GROUP_ACTIVITY, PALENDAR_GROUP_ACTIVITY, FRIENDS_GROUP_ACTIVITY, MORE_GROUP_ACTIVITY};
	private static String[] TAB_NAMES;
	TabHost tabs ;
    public static TabWidget tabWidget ;
	protected Bitmap roundedImage;
	public static Button btnBack, btnLogout, btnCalView, btnListView;
	public static RelativeLayout rlCommonTitleBar;
	public static TextView tvTitle;
	public static LinearLayout llEventButtons;
	public static TabsActivity tabActivity;
	Intent serviceIntent;
	
	@Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
              
        serviceIntent = new Intent(TabsActivity.this, FriendsService.class);
//		startService(serviceIntent);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tabs);
        TAB_NAMES = new String[]{getResources().getString(R.string.home),getResources().getString(R.string.events),
        		getResources().getString(R.string.palendar), getResources().getString(R.string.friends), getResources().getString(R.string.more)};
        tabActivity = this;
        btnBack = (Button) findViewById(R.id.btnBack);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnCalView = (Button) findViewById(R.id.btnCalView);
        btnListView = (Button) findViewById(R.id.btnListView);
        llEventButtons = (LinearLayout) findViewById(R.id.llEventsButtons);
        rlCommonTitleBar = (RelativeLayout)findViewById(R.id.rlTitle);
        tabs = getTabHost();
	   
        tabWidget = tabs.getTabWidget();
        int selectedId = getIntent().getExtras().getInt(SELECTED_ID);
        
      
         
        tabs.setOnTabChangedListener(this);
        for (int i = 0; i < TABS.length; i++)
        {
        	TabHost.TabSpec tab = tabs.newTabSpec(TABS[i]);
        	

        	ComponentName oneActivity = new ComponentName(COM_GA2OO, COM_GA2OO + TABS[i]);
        	Intent intent = new Intent().setComponent(oneActivity);
        	tab.setContent(intent);        	
        	
        	MyTabIndicator myTab =new MyTabIndicator(this, TAB_NAMES[i],(i+1),null); 
        	tab.setIndicator(myTab);
        	tabs.addTab(tab);
        }
		tabs.setCurrentTab(selectedId);
		
		setCurrentTabImage(selectedId);
		tabWidget.getChildTabViewAt(0).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(HomeGroupActivity.homeStack != null && HomeGroupActivity.homeStack.mIdList.size() > 1)
				{
				HomeGroupActivity.homeStack.mIdList.clear();
				HomeGroupActivity.homeStack.getLocalActivityManager().removeAllActivities();
				Intent intent = new Intent(HomeGroupActivity.homeStack, Home.class);
				intent.putExtra(PARENT_PAGE, HOME_GROUP_ACTIVITY);					
				HomeGroupActivity.homeStack.startChildActivity(HOME, intent);
				}
				tabWidget.setCurrentTab(0);
				tabs.setCurrentTab(0);
				setCurrentTabImage(0);
			}
		});
		tabWidget.getChildTabViewAt(1).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(EventsGroupActivity.eventsStack != null && EventsGroupActivity.eventsStack.mIdList.size() > 1)
				{
				EventsGroupActivity.eventsStack.mIdList.clear();
				EventsGroupActivity.eventsStack.getLocalActivityManager().removeAllActivities();
				Intent intent = new Intent(EventsGroupActivity.eventsStack, Events.class);
				intent.putExtra(PARENT_PAGE, EVENTS_GROUP_ACTIVITY);					
				EventsGroupActivity.eventsStack.startChildActivity(EVENTS, intent);
				}
				tabWidget.setCurrentTab(1);
				tabs.setCurrentTab(1);
				setCurrentTabImage(1);
			}
		});
		tabWidget.getChildTabViewAt(2).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(PalendarGroupActivity.palendarStack != null && PalendarGroupActivity.palendarStack.mIdList.size() > 1)
				{
				PalendarGroupActivity.palendarStack.mIdList.clear();
				PalendarGroupActivity.palendarStack.getLocalActivityManager().removeAllActivities();
				Intent intent = new Intent(PalendarGroupActivity.palendarStack, Palendar.class);
				intent.putExtra(PARENT_PAGE, PALENDAR_GROUP_ACTIVITY);					
				PalendarGroupActivity.palendarStack.startChildActivity(PALENDAR, intent);
				}
				tabWidget.setCurrentTab(2);
				tabs.setCurrentTab(2);
				setCurrentTabImage(2);
			}
		});
		tabWidget.getChildTabViewAt(3).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(FriendsGroupActivity.friendsStack != null && FriendsGroupActivity.friendsStack.mIdList.size() > 1)
				{
				FriendsGroupActivity.friendsStack.mIdList.clear();
				FriendsGroupActivity.friendsStack.getLocalActivityManager().removeAllActivities();
				Intent intent = new Intent(FriendsGroupActivity.friendsStack, Friends.class);
				intent.putExtra(PARENT_PAGE, FRIENDS_GROUP_ACTIVITY);					
				FriendsGroupActivity.friendsStack.startChildActivity(FRIENDS, intent);
				}
				tabWidget.setCurrentTab(3);
				tabs.setCurrentTab(3);
				setCurrentTabImage(3);
			}
		});
		tabWidget.getChildTabViewAt(4).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(MoreGroupActivity.moreStack != null && MoreGroupActivity.moreStack.mIdList.size() > 1)
				{
				MoreGroupActivity.moreStack.mIdList.clear();
				MoreGroupActivity.moreStack.getLocalActivityManager().removeAllActivities();
				Intent intent = new Intent(MoreGroupActivity.moreStack, More.class);
				intent.putExtra(PARENT_PAGE, MORE_GROUP_ACTIVITY);					
				MoreGroupActivity.moreStack.startChildActivity(MORE, intent);
				}
				tabWidget.setCurrentTab(4);
				tabs.setCurrentTab(4);
				setCurrentTabImage(4);
			}
		});

    }
	
	@Override
	protected void onDestroy() {
		Log.v(TABSACTIVITY, "destroyed!");
		this.stopService(serviceIntent);
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		Log.v(TABSACTIVITY, "stoped!");
		this.stopService(serviceIntent);
		super.onStop();
	}
	
	

	@Override
	protected void onPause() {
		Log.v(TABSACTIVITY, "paused!");
		this.stopService(serviceIntent);
		super.onPause();
	}

	public void setCurrentTabImage(int selID)
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    	imm.hideSoftInputFromWindow(tabs.getApplicationWindowToken(), 0);
    	
		if(tabs.getTabWidget().getChildCount() > 0)
    	if(selID == 0)
		{
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(0)).getChildAt(0)).getChildAt(0)).setBackgroundResource(R.drawable.home_icon_hover);
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(0)).getChildAt(0)).getChildAt(1)).setTextColor(Color.rgb(255, 110, 1));
		}
		else
		{
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(0)).getChildAt(0)).getChildAt(0)).setBackgroundResource(R.drawable.home_icon);
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(0)).getChildAt(0)).getChildAt(1)).setTextColor(Color.GRAY);
		}
		
		if(tabs.getTabWidget().getChildCount() > 1)
		if(selID == 1)
		{
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(1)).getChildAt(0)).getChildAt(0)).setBackgroundResource(R.drawable.events_icon_hover);
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(1)).getChildAt(0)).getChildAt(1)).setTextColor(Color.rgb(255, 110, 1));
		}
		else
		{
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(1)).getChildAt(0)).getChildAt(0)).setBackgroundResource(R.drawable.events_icon);
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(1)).getChildAt(0)).getChildAt(1)).setTextColor(Color.GRAY);
		}
		
		if(tabs.getTabWidget().getChildCount() > 2)
		if(selID == 2)
		{
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(2)).getChildAt(0)).getChildAt(0)).setBackgroundResource(R.drawable.palendar_icon_hover);
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(2)).getChildAt(0)).getChildAt(1)).setTextColor(Color.rgb(255, 110, 1));
		}
		else
		{
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(2)).getChildAt(0)).getChildAt(0)).setBackgroundResource(R.drawable.palendar_icon);
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(2)).getChildAt(0)).getChildAt(1)).setTextColor(Color.GRAY);
		}
		
		if(tabs.getTabWidget().getChildCount() > 3)
		if(selID == 3)
		{
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(3)).getChildAt(0)).getChildAt(0)).setBackgroundResource(R.drawable.friends_icon_hover);
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(3)).getChildAt(0)).getChildAt(1)).setTextColor(Color.rgb(255, 110, 1));
		}
		else
		{
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(3)).getChildAt(0)).getChildAt(0)).setBackgroundResource(R.drawable.friends_icon);
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(3)).getChildAt(0)).getChildAt(1)).setTextColor(Color.GRAY);
		}
		
		if(tabs.getTabWidget().getChildCount() > 4)
		if(selID == 4)
		{
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(4)).getChildAt(0)).getChildAt(0)).setBackgroundResource(R.drawable.more_icon_hover);
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(4)).getChildAt(0)).getChildAt(1)).setTextColor(Color.rgb(255, 110, 1));
		}
		else
		{
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(4)).getChildAt(0)).getChildAt(0)).setBackgroundResource(R.drawable.more_icon);
			((TextView)((LinearLayout)((LinearLayout)tabs.getTabWidget().getChildTabViewAt(4)).getChildAt(0)).getChildAt(1)).setTextColor(Color.GRAY);
		}
	}
	
    public static class MyTabIndicator extends LinearLayout 
    {
		public MyTabIndicator(Context context, String label,int tabId,Bitmap bgImg)
		{
			super(context);
			LinearLayout tab = null;
			this.setGravity(Gravity.CENTER);
			if(tabId == 1)
			{
				tab = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.home_tab, null);
				TextView tv = (TextView)tab.findViewById(R.id.tab_label);
				tv.setTextColor(Color.GRAY);
				tv.setText(label);
			}
			else if(tabId == 2)
			{
				tab = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.events_tab, null);
				TextView tv = (TextView)tab.findViewById(R.id.tab_label);
				tv.setTextColor(Color.GRAY);
				tv.setText(label);
			}
			else if(tabId == 3)
			{
				tab = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.palendar_tab, null);
				TextView tv = (TextView)tab.findViewById(R.id.tab_label);
				tv.setTextColor(Color.GRAY);
				tv.setText(label);
			}
			else if(tabId == 4)
			{
				tab = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.friends_tab, null);
				TextView tv = (TextView)tab.findViewById(R.id.tab_label);
				tv.setTextColor(Color.GRAY);
				tv.setText(label);
			}
			else if(tabId == 5)
			{
				tab = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.more_tab, null);
				TextView tv = (TextView)tab.findViewById(R.id.tab_label);
				tv.setTextColor(Color.GRAY);
				tv.setText(label);
			}
			else if(tabId == 6)
			{
				tab = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.more_tab, null);
				TextView tv = (TextView)tab.findViewById(R.id.tab_label);
				tv.setTextColor(Color.GRAY);
				tv.setText(label);
				tab.setVisibility(View.GONE);
			}
			this.addView(tab,new LinearLayout.LayoutParams(240/5,50));			
		}		
    }

	@Override
	public void onTabChanged(String tabId) 
	{  
        int i = 0;
        for(i=0;i<tabs.getTabWidget().getChildCount();i++)
		{
			if(tabId.equalsIgnoreCase(TABS[i]))
			{
				break;
			}
	   }	
       setCurrentTabImage(i);
	}
}