package com.ga2oo.palendar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.ga2oo.palendar.businesslayer.FriendsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.objects.Friend;
import com.ga2oo.palendar.objects.UserFriend;
import com.ga2oo.jsonparsers.UserFriendWrapper;
import com.ga2oo.parsing.net.JsonHttpHelper;

public class Friends extends Activity
{
	
	public static final String LOGTAG = "FriendsScreen";
	public static final String USERFRIENDID = "userFriendId";
	public static final String FRIENDSPROFILE = "FriendsProfile";
	
	private List<UserFriend> vecFreindsInfo;
	
	private FriendsBusinessLayer friendBL;
	private DrawableManager drawManager;
	private CustomFriendsAdapter friendAdapter;
	
	private ProgressDialog progressDialog;
	private ListView lvFriends;
	private RelativeLayout rlFindfriend;
	private LinearLayout llFriends;
	private TextView tvnoResult;
	private EditText searchFriend;
	private Button cancelSearch;
	
	private String friendImageUrl;
	private JsonElement element;
	private JsonHttpHelper jsonHelper;
	
	UserAccountBusinessLayer uabl;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
    }
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		setContentView(R.layout.friends);
	    progressDialog 		= new ProgressDialog(this.getParent());
		progressDialog.setMessage(getResources().getString(R.string.loading));
		drawManager 		= new DrawableManager();
		vecFreindsInfo 		= new ArrayList<UserFriend>();
		friendBL			= new FriendsBusinessLayer();
		rlFindfriend		= (RelativeLayout)findViewById(R.id.rlFindFriend);
		llFriends			= (LinearLayout)findViewById(R.id.llFriends);
		tvnoResult			= (TextView)findViewById(R.id.tvnoResult);
		lvFriends 			=	(ListView)findViewById(R.id.lvFriends);
		searchFriend 		= (EditText)findViewById(R.id.etSearchFriend);
		cancelSearch		= (Button)findViewById(R.id.btnSearchcancel);
		lvFriends.setFadingEdgeLength(0);
		lvFriends.setCacheColorHint(0);
		lvFriends.setDivider(getResources().getDrawable(R.drawable.line));
		lvFriends.setVerticalScrollBarEnabled(false);

		uabl = new UserAccountBusinessLayer();
		
		jsonHelper = JsonHttpHelper.getInstance();
		
//		progressDialog.show();
		AppConstants.vctFriend =new ArrayList<Friend>();
		new LoadFriendsList().execute();
		new Thread(new  Runnable()
		{
			@Override
			public void run() 
			{
				friendBL.deleteAlFriend();
				Log.i(LOGTAG, "Loading friends list...");
					try {
						element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.FRIEND_URL+AppConstants.USER_ID);
					} catch (Exception e) {
						Log.e(LOGTAG, "Error in loading friends list.");
						e.printStackTrace();
					}
					Object userFriendObjects =  jsonHelper.parse(element, UserFriendWrapper.class);
					AppConstants.vctFriend.addAll(((UserFriendWrapper)userFriendObjects).getUseraccount().friendships);
					for(int i=0;i<((UserFriendWrapper)userFriendObjects).getUseraccount().friendships.size();i++){
						uabl.InsertFriend(((UserFriendWrapper)userFriendObjects).getUseraccount().friendships.get(i));
					}
					Log.i(LOGTAG, "Loading friends list successfully completed.");

				runOnUiThread(new Runnable()
				{
					public void run() 
					{
						vecFreindsInfo=friendBL.getUserFriendInformation("");
						if(vecFreindsInfo!=null && vecFreindsInfo.size()!=0)
						{
							tvnoResult.setVisibility(View.GONE);
							lvFriends.setAdapter(friendAdapter=new CustomFriendsAdapter(vecFreindsInfo));
//							friendAdapter.refresh(vecFreindsInfo);
						}
						else
						{
							tvnoResult.setVisibility(View.VISIBLE);
							tvnoResult.setText("There are no friend");
						}
					}
				});
				progressDialog.dismiss();
			}
		}).start();

		
		TabsActivity.btnBack.setVisibility(View.GONE);
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar);
        TabsActivity.tvTitle.setVisibility(View.GONE);
        TabsActivity.btnLogout.setBackgroundResource(R.drawable.logout_btn);
        TabsActivity.btnLogout.setVisibility(View.VISIBLE);
		drawManager=new DrawableManager();
        if(friendAdapter != null){
        	friendAdapter.refresh(vecFreindsInfo);
        }
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
		    			runOnUiThread(new Runnable()
						{
		    				public void run() 
							{
		    					Intent intent = new Intent(Friends.this, Login.class);
		    		            startActivity(intent);
		    		            TabsActivity.tabActivity.finish();
							}
						});
						progressDialog.dismiss();
					}
				}).start();			}
		});
		
		
		searchFriend.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
					
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				vecFreindsInfo = friendBL.getFilteredUserFriendInformation(s.toString());
				friendAdapter.refresh(vecFreindsInfo);
			}
			
		});
		cancelSearch.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				searchFriend.setText("");
				searchFriend.removeTextChangedListener(lvFriends);
			}
			
		});
		
	}
	
	private class LoadFriendsList extends AsyncTask<Void,Void,Boolean>{
		
		@Override
		protected void onPreExecute() {
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Log.i(LOGTAG, "Loading friends list...");
			try {
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.FRIEND_URL+AppConstants.USER_ID);
			} catch (Exception e) {
				Log.e(LOGTAG, "Error in loading friends list.");
				e.printStackTrace();
			}
			Object userFriendObjects =  jsonHelper.parse(element, UserFriendWrapper.class);
			if(userFriendObjects!=null){
				AppConstants.vctFriend.addAll(((UserFriendWrapper)userFriendObjects).getUseraccount().friendships);
				for(int i=0;i<((UserFriendWrapper)userFriendObjects).getUseraccount().friendships.size();i++){
					uabl.InsertFriend(((UserFriendWrapper)userFriendObjects).getUseraccount().friendships.get(i));
				}
				Log.i(LOGTAG, "Loading friends list successfully completed.");
			}


			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			if(!result){
//				DialogUtility.showConnectionErrorDialog(Friends.this.getParent());
			}
				vecFreindsInfo=friendBL.getUserFriendInformation("");
				if(vecFreindsInfo!=null && vecFreindsInfo.size()!=0)
				{
					tvnoResult.setVisibility(View.GONE);
					lvFriends.setAdapter(friendAdapter=new CustomFriendsAdapter(vecFreindsInfo));
//					friendAdapter.refresh(vecFreindsInfo);
				}
				else
				{
					tvnoResult.setVisibility(View.VISIBLE);
					tvnoResult.setText(getResources().getString(R.string.there_are_no_friend));
				}
			
			super.onPostExecute(result);
		}
		
	}
	
	public class CustomFriendsAdapter extends BaseAdapter 
	{
		private List<UserFriend> vecAdapterFriends;
		
		public CustomFriendsAdapter(List<UserFriend> vecAdapterFriends)
		{
			this.vecAdapterFriends = vecAdapterFriends;
		}
		
		@Override
		public int getCount()
		{
			if(vecAdapterFriends != null)
				return vecAdapterFriends.size();
			return 0;
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
		private void refresh(List<UserFriend> vecAdapterFriends)
		{
			this.vecAdapterFriends = vecAdapterFriends;
			this.notifyDataSetChanged();
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			final  UserFriend friend = vecAdapterFriends.get(position);
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.friends_cell, null);
			ImageView ivFriendsIcon = (ImageView) convertView.findViewById(R.id.ivFriendsIcon);
			ProgressBar progressbar=(ProgressBar)convertView.findViewById(R.id.friend_cell_progress_bar);
			TextView tvFriendsName = (TextView) convertView.findViewById(R.id.tvFriendsName);
			ImageView ivDeleteFriend=(ImageView)convertView.findViewById(R.id.ivDeleteFriend);
			convertView.setId(friend.id);
			ivDeleteFriend.setVisibility(View.GONE);
			
			if(!friend.imagesrc.equals(AppConstants.NOIMAGE))
			{
				friendImageUrl=AppConstants.IMAGE_HOST_URL+friend.imagesrc;
				drawManager.fetchDrawableOnThread(friendImageUrl, ivFriendsIcon,70,70,friend.image_id,friend.isImageUpdated);
				progressbar.setVisibility(View.GONE);
			}
			else
			{
				progressbar.setVisibility(View.GONE);
				ivFriendsIcon.setImageResource(R.drawable.no_image_smal_event);
			}
			tvFriendsName.setText(friend.firstname+" "+friend.lastname);
			
			convertView.setOnClickListener(new OnClickListener()
			{			
				@Override
				public void onClick(View v) 
				{
					
					Intent intent = new Intent(Friends.this,FriendsProfile.class);
					intent.putExtra(USERFRIENDID,v.getId());
						
					TabGroupActivity pActivity = (TabGroupActivity)Friends.this.getParent();
					pActivity.startChildActivity(FRIENDSPROFILE, intent);
				}
			});
			return convertView;
		}
		
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		llFriends.removeAllViews();
		
		if(friendAdapter != null)
			friendAdapter.refresh(new ArrayList<UserFriend>());
		if(drawManager != null)
			drawManager.clear();
	}

	@Override
	protected void onStop() 
	{
		super.onPause();
		if(friendAdapter != null){
			friendAdapter.refresh(new ArrayList<UserFriend>());
		}
		if(drawManager != null){
			drawManager.clear();
		}
	}
	
	//inner class
	class MyFriend 
	{
		public int resImgId;
		public String name;
		
		MyFriend(int resImgId, String name)
		{
			this.resImgId = resImgId;
			this.name = name;
		}
	}
	
	@Override
	public boolean onSearchRequested() 
	{
//		LocalActivityManager mLocalActivityManager = new LocalActivityManager(getParent(), false);
	    return false ; //mLocalActivityManager.getCurrentActivity().onSearchRequested();
	}
}
