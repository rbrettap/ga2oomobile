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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.ga2oo.palendar.businesslayer.FriendsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.DialogUtility;
import com.ga2oo.palendar.controls.PopUpDailog;
import com.ga2oo.palendar.objects.Friend;
import com.ga2oo.palendar.objects.UserAccount;
import com.ga2oo.palendar.objects.UserFriend;
import com.ga2oo.palendar.xmlparsers.FriendParser;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;
import com.ga2oo.jsonparsers.Ga2ooUsersWrapper;
import com.ga2oo.jsonparsers.UserFriendWrapper;
import com.ga2oo.parsing.net.JsonHttpHelper;

public class MyCircle extends Activity
{
	
	private static final String LOGTAG="MyCircleScreen";
	
	private List<UserFriend> vecFreindsInfo;
	private List<UserAccount> vctGa2ooUsers;
	
	private Button btnCancelButton;
	private EditText etSearchFriend;
	private ProgressDialog progressDialog;
	private ListView lvFriends,lvsearchedFriend;
	private TextView tvnoResult;
	
	private PopUpDailog customDialog;
	private UserAccountBusinessLayer objUserAccountBL;
	private FriendsBusinessLayer friendBL;
	private DrawableManager drawManager;
	private Friend objFriend;
	private CustomFindFriendsAdapter customFindFriendsAdapter;
	private LinearLayout llFriends;
	private CustomFriendsAdapter friendAdapter;
	private InputMethodManager imm;
	private FriendParser friendParser;
	private boolean isAnyUser=true;
	private boolean isDeleted=false,isEditClicked=false;
	private String searchKeyword="",friendImageUrl; 
	private int status;
	
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
		setContentView(R.layout.friends);
		
		imm 				= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		drawManager 		= 	new DrawableManager();
		vecFreindsInfo 		= 	new ArrayList<UserFriend>();
		friendBL			=	new FriendsBusinessLayer();
		
		lvFriends 			=	(ListView)findViewById(R.id.lvFriends);
		llFriends			= (LinearLayout)findViewById(R.id.llFriends);
		etSearchFriend		=	(EditText)findViewById(R.id.etSearchFriend);
		btnCancelButton		=	(Button)findViewById(R.id.btnSearchcancel);
		tvnoResult			= (TextView)findViewById(R.id.tvnoResult);
		lvsearchedFriend	=	(ListView)findViewById(R.id.lvsearchedFriend);
		vctGa2ooUsers		=	new ArrayList<UserAccount>();
		objUserAccountBL	=	new UserAccountBusinessLayer();
		objFriend			=	new Friend();
		
		jsonHelper = JsonHttpHelper.getInstance();
		
		if(progressDialog!=null && progressDialog.isShowing())
			progressDialog.dismiss();
				
		progressDialog = new ProgressDialog(getParent());
		progressDialog.setMessage(getResources().getString(R.string.loading));
				
		lvsearchedFriend.setFadingEdgeLength(0);
		lvsearchedFriend.setCacheColorHint(0);
		lvsearchedFriend.setDivider(getResources().getDrawable(R.drawable.line));
		lvsearchedFriend.setVerticalScrollBarEnabled(false);
		
		lvFriends.setFadingEdgeLength(0);
		lvFriends.setCacheColorHint(0);
		lvFriends.setDivider(getResources().getDrawable(R.drawable.line));
		lvFriends.setVerticalScrollBarEnabled(false);

		friendParser 		= new FriendParser();
		AppConstants.vctFriend = new ArrayList<Friend>();
		new LoadUserFriends().execute();

		etSearchFriend.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				
				new SearchGa2ooUser().execute();
			
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after)
			{
			}
			@Override
			public void afterTextChanged(Editable s)
			{
				tvnoResult.setVisibility(View.GONE);
			}
		});
			
		btnCancelButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				tvnoResult.setVisibility(View.GONE);
				etSearchFriend.setText("");
				etSearchFriend.removeTextChangedListener(lvFriends);
				lvFriends.setVisibility(View.VISIBLE);
				if(vecFreindsInfo.size()!=0)
					friendAdapter.refresh(vecFreindsInfo);
					
				if(vctGa2ooUsers.size()!=0)
				{
					lvsearchedFriend.setVisibility(View.GONE);
					vctGa2ooUsers.clear();
				}
			}
		});
		
		TabsActivity.btnBack.setVisibility(View.VISIBLE);
		TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar);
		TabsActivity.tvTitle.setVisibility(View.GONE);
		TabsActivity.btnLogout.setVisibility(View.VISIBLE);
		TabsActivity.btnLogout.setBackgroundResource(R.drawable.edit);
		TabsActivity.btnLogout.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(!isEditClicked)
				{
					isEditClicked=true;
					friendAdapter.refresh();
					TabsActivity.btnLogout.setBackgroundResource(R.drawable.cancelbtn);
				}
				else if(isEditClicked)
				{
					isEditClicked=false;
					friendAdapter.refresh();
					TabsActivity.btnLogout.setBackgroundResource(R.drawable.edit);
				}
			}
		});
		TabsActivity.btnBack.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				TabGroupActivity pActivity = (TabGroupActivity)MyCircle.this.getParent();
				pActivity.finishFromChild(MyCircle.this);
			}
		});
	}
	
	private class LoadUserFriends extends AsyncTask<Void,Void,Boolean>{
		
		@Override
		protected void onPreExecute() {
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			Log.i(LOGTAG, "Loading user friends");
			try {
				element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.FRIEND_URL+AppConstants.USER_ID);
			} catch (Exception e) {
				Log.e(LOGTAG, "Error in loading euser friends.");
				e.printStackTrace();
				return false;
			}
			Object userFriendObjects =  jsonHelper.parse(element, UserFriendWrapper.class);
			if(userFriendObjects!=null){
				if(((UserFriendWrapper)userFriendObjects).getUseraccount()!=null){
					if(((UserFriendWrapper)userFriendObjects).getUseraccount().friendships!=null){
						AppConstants.vctFriend.addAll(((UserFriendWrapper)userFriendObjects).getUseraccount().friendships);
					}
				}
				Log.i(LOGTAG, "Loading user friends successfully completed.");
				friendBL.Insert(((UserFriendWrapper)userFriendObjects).getUseraccount());
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			if(!result){
				DialogUtility.showConnectionErrorDialog(MyCircle.this.getParent());
			}
			vecFreindsInfo=friendBL.getUserFriendInformation("");
			lvFriends.setAdapter(friendAdapter=new CustomFriendsAdapter(vecFreindsInfo));
			friendAdapter.refresh(vecFreindsInfo);
			super.onPostExecute(result);
		}
		
	}
	
	private class SearchGa2ooUser extends AsyncTask<Void,Void,Boolean>{
		
		@Override
		protected void onPreExecute() {
			lvsearchedFriend.setVisibility(View.VISIBLE);
			lvFriends.setVisibility(View.GONE);
			//progressDialog.show();
			searchKeyword=etSearchFriend.getText().toString();
			tvnoResult.setVisibility(View.GONE);
			vctGa2ooUsers.clear();
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {			
			if(!searchKeyword.equals(""))
			{
				isAnyUser=true;
				Log.i(LOGTAG, "Search ga2oo users...");
				try {
					element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.FINDGATOOUSERS+searchKeyword);
				} catch (Exception e) {
					Log.e(LOGTAG, "Error in search ga2oo users!");
					e.printStackTrace();
					return false;
				}
				Object usersObjects = jsonHelper.parse(element, Ga2ooUsersWrapper.class);
				if(usersObjects!=null){
					for(int i=0;i<((Ga2ooUsersWrapper)usersObjects).getUseraccount().getUsers().size();i++){
						vctGa2ooUsers.add(((Ga2ooUsersWrapper)usersObjects).getUseraccount().getUsers().get(i));
					}
					Log.i(LOGTAG, "Search ga2oo users successfully completed.");
				}
			}
			else
			{
				tvnoResult.setVisibility(View.GONE);
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			if(!result){
				DialogUtility.showConnectionErrorDialog(MyCircle.this.getParent());
			}
			lvsearchedFriend.setAdapter(customFindFriendsAdapter=new CustomFindFriendsAdapter(vctGa2ooUsers));
			customFindFriendsAdapter.refresh(vctGa2ooUsers);
			if(vctGa2ooUsers.size()==0&&!searchKeyword.equals(""))
				{
					tvnoResult.setText(getResources().getString(R.string.no_result_found));
					tvnoResult.setVisibility(View.VISIBLE);
				}
			else {
				if(vctGa2ooUsers.size()!=0){
					tvnoResult.setVisibility(View.GONE);
				}
			}
			super.onPostExecute(result);
		}
		
	}
	
	public class CustomFriendsAdapter extends BaseAdapter 
	{
		private static final String FRIENDS_PROFILE = "FriendsProfile";
		private static final String USER_FRIEND_ID = "userFriendId";
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
		private void refresh()
		{
			this.notifyDataSetChanged();
		}

		private void refreshAfterDelete(int position)
		{
			this.vecAdapterFriends.remove(position);
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
			convertView.setId(friend.userFriendAddedId);
			ivDeleteFriend.setId(friend.userFriendAddedId);
			if(isEditClicked)
			{
				ivDeleteFriend.setVisibility(View.VISIBLE);
			}
			else if(!isEditClicked)
			{
				ivDeleteFriend.setVisibility(View.GONE);
			}
			
			if(!friend.imagesrc.equals(AppConstants.NOIMAGE))
			{
				friendImageUrl=AppConstants.IMAGE_HOST_URL+friend.imagesrc;
				drawManager.fetchDrawableOnThread(friendImageUrl, ivFriendsIcon,70,70,friend.image_id,friend.isImageUpdated);
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
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etSearchFriend.getWindowToken(), 0);
					Intent intent = new Intent(MyCircle.this,FriendsProfile.class);
					intent.putExtra(USER_FRIEND_ID,friend.id);
						
					TabGroupActivity pActivity = (TabGroupActivity)MyCircle.this.getParent();
					pActivity.startChildActivity(FRIENDS_PROFILE, intent);
				}
			});
			
			ivDeleteFriend.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					int friendAddedId=v.getId();
					isDeleted=showConfirmationPopup(friendAddedId,position);
				}
			});
			return convertView;
		}
		
		public boolean showConfirmationPopup(final int userFriendAddedId,final int position)
		{
			LayoutInflater inflater = getLayoutInflater();
			LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.confirmation_popup, null);
			Button btnYes = (Button)popup.findViewById(R.id.btnYes);
			Button btnNo = (Button)popup.findViewById(R.id.btnNo);
			TextView tvMessage=(TextView)popup.findViewById(R.id.tvMessage);
			tvMessage.setText(getResources().getString(R.string.are_you_shure_to_delete_friend));
			
			btnYes.setOnClickListener(new OnClickListener()
			{					
				@Override
				public void onClick(View v) 
				{
//					status=objGa2ooParsers.deleteUserFriend(userFriendAddedId);
					status = Ga2ooJsonParsers.deleteUserFriend(userFriendAddedId);
					Log.i("status",""+status);
					if(status>0)
					{
						Toast.makeText(getParent(),getResources().getString(R.string.friend_delete_successfully), Toast.LENGTH_SHORT).show();
						isDeleted=true;
						friendBL.deleteFriend(userFriendAddedId);
						if(friendAdapter!=null)
							friendAdapter.refreshAfterDelete(position);
					}
					else
					{
						Toast.makeText(getParent(),getResources().getString(R.string.try_again), Toast.LENGTH_SHORT).show();
						isDeleted=false;
					}
					customDialog.dismiss();
				}
			});
				
			btnNo.setOnClickListener(new OnClickListener()
			{					
				@Override
				public void onClick(View v) 
				{
					isDeleted=false;
					customDialog.dismiss();
				}
			});
			customDialog = new PopUpDailog(getParent(), popup, 220, LayoutParams.WRAP_CONTENT);
			customDialog.show();
			return isDeleted;
		}	
	}
		
	public class CustomFindFriendsAdapter extends BaseAdapter 
	{
		private List<UserAccount> vecAdapterGa2ooUsers;
		
		public CustomFindFriendsAdapter(List<UserAccount> vecAdapterGa2ooUsers)
		{
			this.vecAdapterGa2ooUsers = vecAdapterGa2ooUsers;
		}
			
		@Override
		public int getCount()
		{
			if(vecAdapterGa2ooUsers != null)
				return vecAdapterGa2ooUsers.size();
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
		private void refresh(List<UserAccount> vecAdapterGa2ooUsers)
		{
			this.vecAdapterGa2ooUsers = vecAdapterGa2ooUsers;
			this.notifyDataSetChanged();
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			final  UserAccount objUserAccount = vecAdapterGa2ooUsers.get(position);
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.friends_cell, null);
			ImageView ivFriendsIcon = (ImageView) convertView.findViewById(R.id.ivFriendsIcon);
			ProgressBar progressbar=(ProgressBar)convertView.findViewById(R.id.friend_cell_progress_bar);
			TextView tvFriendsName = (TextView) convertView.findViewById(R.id.tvFriendsName);
			ImageView ivAddFriend=(ImageView)convertView.findViewById(R.id.ivDeleteFriend);
			ivAddFriend.setImageResource(R.drawable.addfriend);
			convertView.setId(objUserAccount.id);
			ivAddFriend.setId(objUserAccount.id);
				
			progressbar.setVisibility(View.GONE);
			ivFriendsIcon.setImageResource(R.drawable.no_image_smal_event);
			convertView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				}
			});
			tvFriendsName.setText(objUserAccount.firstname);
			ivAddFriend.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					int friendId=v.getId();
					try
					{
						status = Ga2ooJsonParsers.addFriend(AppConstants.USER_ID,friendId);
						if(status>0)
						{
							vecAdapterGa2ooUsers.remove(position);
							customFindFriendsAdapter.notifyDataSetChanged();
							objFriend.friendid=friendId;
							objFriend.useraddedfriendid=status;
							objFriend.date_updated="";
							//objUserAccountBL.InsertFriend(objFriend);
							Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.friend_request_sent_successfully), Toast.LENGTH_SHORT);
							toast.show();
						}
						else
						{
							Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.error_in_sending_friend_request), Toast.LENGTH_SHORT);
							toast.show();
						}
					}
					catch(Exception ee)
					{
						ee.printStackTrace();
					}
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
		
		if(friendAdapter != null){
			friendAdapter.refresh(new ArrayList<UserFriend>());
		}
		if(drawManager != null){
			drawManager.clear();
		}
	}

	@Override
	protected void onStop() 
	{
		super.onStop();
		if(friendAdapter != null)
			friendAdapter.refresh(new ArrayList<UserFriend>());
		if(drawManager != null)
			drawManager.clear();
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
	
}
