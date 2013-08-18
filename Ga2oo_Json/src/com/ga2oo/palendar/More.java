package com.ga2oo.palendar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class More extends Activity
{
	
	public static final String LOGTAG = "MoreScreen"; 
	private static final String FROMACTIVITY= "fromActivity";
	private static final String MORE = "More";
	private static final String PROFILESETTINGS= "ProfileSettings";
	private static final String MYCIRCLE = "MyCircle";
	private static final String FAVORITES = "Favorites";
	private static final String ACCOUNT = "Account";
	private static final String ABOUT = "About";
	private static final String FAQ = "FAQ";
	private static final String CONTACT = "Contact";
	
	private RelativeLayout rellayProfile,rellayFavorites,rellayAccount, rellayAbout, rellayFAQ, rellayContact,rellayMyCircle;
	private ProgressDialog progressDialog;
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_view);
		
		rellayProfile 	= (RelativeLayout)findViewById(R.id.rellayProfile);
		rellayFavorites = (RelativeLayout)findViewById(R.id.rellayFavorites);
		rellayMyCircle	= (RelativeLayout)findViewById(R.id.rellayMyCircle);
		rellayAccount 	= (RelativeLayout)findViewById(R.id.rellayAccount);
		rellayAbout 	= (RelativeLayout)findViewById(R.id.rellayAbout);
		rellayFAQ 		= (RelativeLayout)findViewById(R.id.rellayFaq);
		rellayContact 	= (RelativeLayout)findViewById(R.id.rellayContact);
		
		if(progressDialog!=null && progressDialog.isShowing())
    		progressDialog.dismiss();
    	progressDialog = new ProgressDialog(getParent());
		
		rellayProfile.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(More.this,ProfileSettings.class);
				intent.putExtra(FROMACTIVITY,MORE);
				TabGroupActivity pActivity = (TabGroupActivity)More.this.getParent();
				pActivity.startChildActivity(PROFILESETTINGS, intent);	
			}
		});
		rellayMyCircle.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent inttentSearchedEvent = new Intent(More.this, MyCircle.class);
				overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
				TabGroupActivity pActivity = (TabGroupActivity)More.this.getParent();
				pActivity.startChildActivity(MYCIRCLE, inttentSearchedEvent);
			}
		});
		rellayFavorites.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(More.this,Favorites.class);
				intent.putExtra(FROMACTIVITY,MORE);
				TabGroupActivity pActivity = (TabGroupActivity)More.this.getParent();
				pActivity.startChildActivity(FAVORITES, intent);	
			}
		});
		
		rellayAccount.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(More.this,Account.class);
				intent.putExtra(FROMACTIVITY,MORE);
				TabGroupActivity pActivity = (TabGroupActivity)More.this.getParent();
				pActivity.startChildActivity(ACCOUNT, intent);
			}
		});
		rellayAbout.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(More.this,About.class);
				intent.putExtra(FROMACTIVITY,MORE);
				TabGroupActivity pActivity = (TabGroupActivity)More.this.getParent();
				pActivity.startChildActivity(ABOUT, intent);
			}
		});
		rellayFAQ.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(More.this,FAQ.class);
				intent.putExtra(FROMACTIVITY,MORE);
				TabGroupActivity pActivity = (TabGroupActivity)More.this.getParent();
				pActivity.startChildActivity(FAQ, intent);
			}
		});
		rellayContact.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(More.this,Contact.class);
				intent.putExtra(FROMACTIVITY,MORE);
				TabGroupActivity pActivity = (TabGroupActivity)More.this.getParent();
				pActivity.startChildActivity(CONTACT, intent);
			}
		});
    }
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		TabsActivity.btnBack.setVisibility(View.GONE);
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar);
        TabsActivity.tvTitle.setVisibility(View.GONE);
        TabsActivity.btnLogout.setVisibility(View.VISIBLE);
        TabsActivity.btnLogout.setBackgroundResource(R.drawable.logout_btn);
		
        TabsActivity.btnLogout.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View v) 
        	{
        		progressDialog.setMessage(getResources().getString(R.string.logout));
        		progressDialog.show();
        		new Thread(new  Runnable()
        		{
        			@Override
        			public void run() 
        			{
        				//userActBL.clearData();
        				runOnUiThread(new Runnable()
        				{
        					public void run() 
        					{
        						Intent intent = new Intent(More.this, Login.class);
        						startActivity(intent);
        						TabsActivity.tabActivity.finish();
        					}
        				});
        				progressDialog.dismiss();
        			}
        		}).start();
        	}
        });
	}
}
