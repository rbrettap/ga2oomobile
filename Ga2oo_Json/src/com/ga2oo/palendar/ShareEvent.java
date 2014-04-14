package com.ga2oo.palendar;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ga2oo.palendar.businesslayer.EventsBusinessLayer;
import com.ga2oo.palendar.businesslayer.FriendsBusinessLayer;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.DialogUtility;
import com.ga2oo.palendar.common.Tools;
import com.ga2oo.palendar.controls.PopUpDailog;
import com.ga2oo.palendar.objects.Business;
import com.ga2oo.palendar.objects.UserFriend;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;

public class ShareEvent extends Activity
{
	
	private static final String FRIENDSPROFILE = "FriendsProfile";

	private static final String USERFRIENDID = "userFriendId";

	private static final String VND_ANDROID_DIR_MMS_SMS = "vnd.android-dir/mms-sms";

	private static final String _139 = "139";

	private static final String ADDRESS = "address";

	private static final String SMS_BODY = "sms_body";

	private static final String IMAGE = "image/*";

	private static final String TEXTPLAIN = "text/plain";

	private static final String LOGTAG = "ShareEvent";
	
	private PopupWindow mPopupWindow =null;
	private Context context;
	public  ListView lvFriendsList;
	private List<UserFriend> vecFriends;
	private FriendsBusinessLayer friendBL;
	private int to[],toAllFriend[];
	private String strEventName;
	private String strEventDetail="";
	private String strEventImage="";
	private PopUpDailog customDialog;
	private List<Integer> vctFriendIds;
	private DrawableManager objDrawableManager;
	private UserAccountBusinessLayer userActBL;
	private EventsBusinessLayer eventsBL;
	String recommendationStatus="";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		if(friendBL == null)
			friendBL			=  new FriendsBusinessLayer();
		if(userActBL == null)
			userActBL = new UserAccountBusinessLayer();
		if(eventsBL == null)
			eventsBL = new EventsBusinessLayer();
		objDrawableManager		= new DrawableManager();
		vctFriendIds				=  new ArrayList<Integer>();
	}
	
	public void showSharePopup(Context context,final int eventID,String strEventname,String eventDetail) 
	{
		this.context=context;
		this.strEventDetail=eventDetail;
		this.strEventName=strEventname;
		LayoutInflater inflater = this.getLayoutInflater();
		View mView = inflater.inflate(R.layout.event_popup,(ViewGroup) findViewById(R.id.dontRecordPopup));
		mPopupWindow = new PopupWindow(mView,android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
		mPopupWindow.showAtLocation(mView, Gravity.BOTTOM, 10, 0);

		Button btnSpecificFri = (Button) mView.findViewById(R.id.btnSpecificFri);
		btnSpecificFri.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v)
			{
				showAllGa2ooFriendsPopup(eventID);
				mPopupWindow.dismiss();
			}
		});
		
		Button btnGoFri = (Button) mView.findViewById(R.id.btnGoFri);
		btnGoFri.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v)
			{
				vecFriends = new ArrayList<UserFriend>();
				vecFriends.clear();
				vecFriends=friendBL.getAllFriends();
				if(vecFriends!=null && vecFriends.size()!=0)
				{
					toAllFriend=new int[vecFriends.size()];
					for(int i=0;i<vecFriends.size();i++)
					{
						toAllFriend[i]=vecFriends.get(i).id;
					}
				}
				if(toAllFriend!=null)
				{
					recommendationStatus = Ga2ooJsonParsers.getInstance().sendRecommendation(toAllFriend, eventID,strEventName,strEventDetail);
					if(!recommendationStatus.equals(""))
					{
						if("0".equals(recommendationStatus)){
							DialogUtility.showConnectionErrorDialog(ShareEvent.this.getParent());
						}else{
							Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.recomendation_sent), Toast.LENGTH_SHORT);
							toast.show();
						}
					}
				}
				else
				{
					Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_friend_yet), Toast.LENGTH_SHORT);
					toast.show();
				}
				mPopupWindow.dismiss();
			}
		});
		
		Button btnEmail = (Button) mView.findViewById(R.id.btnEmail);
		btnEmail.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v)
			{
				Intent int_email = new Intent(Intent.ACTION_SEND);
		    	int_email.setType(TEXTPLAIN); 
		    	int_email.setType(IMAGE); 
		    	int_email.putExtra(Intent.EXTRA_STREAM, Uri.parse(strEventImage));
				int_email.putExtra(Intent.EXTRA_SUBJECT, "");
				int_email.putExtra(Intent.EXTRA_TEXT,strEventDetail);
				
				List<ResolveInfo> list = ShareEvent.this.getPackageManager().queryIntentActivities(int_email, PackageManager.MATCH_DEFAULT_ONLY);					
				int index = Tools.getMailIntentIndex(list);
				if(index != -1)
				{
					ResolveInfo ri = (ResolveInfo) list.get(index);
					int_email.setClassName(ri.activityInfo.packageName, ri.activityInfo.name);
				}
				
				startActivity(int_email);
				mPopupWindow.dismiss();
			}
		});
		Button btnSMS = (Button) mView.findViewById(R.id.btnSMS);
		btnSMS.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v)
			{
				Intent sendIntent= new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra(SMS_BODY, SMS_BODY);
				sendIntent.putExtra(ADDRESS, _139);
				sendIntent.setType(VND_ANDROID_DIR_MMS_SMS);
				startActivity(sendIntent); 
				mPopupWindow.dismiss();
			}
		});
		
		Button btnCancel = (Button) mView.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
				mPopupWindow.dismiss();
			}
		});

	}

	public void showAllGa2ooFriendsPopup(final int eventId)
	{
		LayoutInflater inflater = getLayoutInflater();
		RelativeLayout popup=(RelativeLayout) inflater.inflate(R.layout.all_friends_popup, null);
		
		ImageView ivClose = (ImageView)popup.findViewById(R.id.ivClose);
		Button btnDone	  = (Button)popup.findViewById(R.id.btnDone);
		TextView tvNoAnyFriend	 = (TextView)popup.findViewById(R.id.tvNoAnyFriend);	
		lvFriendsList = (ListView)popup.findViewById(R.id.lvFriendsList);
		lvFriendsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lvFriendsList.setCacheColorHint(0);
		lvFriendsList.setFadingEdgeLength(0);
		ivClose.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
			}
		});
		vecFriends = new ArrayList<UserFriend>();
		vecFriends.clear();
		vecFriends=friendBL.getAllFriends();
		if(vecFriends.size()!=0)
		{
			lvFriendsList.setAdapter(new CustomFriendAdapter(vecFriends));
		}
		else
		{
			tvNoAnyFriend.setVisibility(View.VISIBLE);
			btnDone.setVisibility(View.GONE);
		}
		vctFriendIds.clear();
		btnDone.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(vctFriendIds.size()!=0&&vctFriendIds!=null)
				{
					to=new int[vctFriendIds.size()];
					for (int i = 0; i <vctFriendIds.size(); i++)
					{
						Log.i("Email",""+vctFriendIds.get(i));
						to[i]=vctFriendIds.get(i);
					}
					if(to.length!=0&&to!=null)
					{
						//TODO: response ? Ga2ooJsonPasers
						recommendationStatus = Ga2ooJsonParsers.getInstance().sendRecommendation(to, eventId,strEventName,strEventDetail);
						Log.v(LOGTAG, "showAllGa2ooFriendsPopup recommendationStatus = "+recommendationStatus);
						if(!recommendationStatus.equals(""))
						{
							if("0".equals(recommendationStatus)){
								DialogUtility.showConnectionErrorDialog(ShareEvent.this.getParent());
							}else{
								Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.recomendation_sent), Toast.LENGTH_SHORT);
								toast.show();
							}
						}else {
							Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.friend_not_found), Toast.LENGTH_SHORT);
							toast.show();
						}
						
					}
				}
				customDialog.dismiss();
			}
		});
		customDialog = new PopUpDailog(getParent(), popup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}
	
	public class CustomFriendAdapter extends BaseAdapter 
	{
		private static final String SELECTED = "selected";
		private static final String UNSELECTED = "unselected";
		private List<UserFriend> vecFreiend;
		public CustomFriendAdapter(List<UserFriend> vecFreiend)
		{
			this.vecFreiend = vecFreiend;
		}
		
		@Override
		public int getCount()
		{
			if(vecFreiend != null)
				return vecFreiend.size();
			return 0;
		}
		
		private void refresh(List<UserFriend> vecFreiend)
		{
			this.vecFreiend = vecFreiend;
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
			final UserFriend objFriend = vecFreiend.get(position);
			convertView = (LinearLayout) getLayoutInflater().inflate(R.layout.friends_list_cell, null);
			final ImageView ivCheckBox=(ImageView)convertView.findViewById(R.id.ivCheckBox);
			ivCheckBox.setVisibility(ImageView.VISIBLE);
			ImageView ivFriendsListIcon = (ImageView) convertView.findViewById(R.id.ivFriendsListIcon);
			ProgressBar friend_list_progress_bar=(ProgressBar)convertView.findViewById(R.id.friend_list_progress_bar);
			TextView tvFriendsListName = (TextView) convertView.findViewById(R.id.tvFriendsListName);
			ivCheckBox.setId(position);
			ivCheckBox.setTag(UNSELECTED);
			if(ivCheckBox.getTag().equals(SELECTED))
			{
				ivCheckBox.setImageResource(R.drawable.btn_check_on);
			}
			
			else if(ivCheckBox.getTag().equals(UNSELECTED))
			{
				ivCheckBox.setImageResource(R.drawable.btn_check_off);
			}
			if(!objFriend.imagesrc.equals(AppConstants.NOIMAGE))
			{
				String friendImageUrl=AppConstants.IMAGE_HOST_URL+objFriend.imagesrc;
				if(!objDrawableManager.fetchDrawableOnThread(friendImageUrl, ivFriendsListIcon,70,50,objFriend.image_id,objFriend.isImageUpdated)){
					ivFriendsListIcon.setImageResource(R.drawable.no_image_smal_event);
				}
				friend_list_progress_bar.setVisibility(View.GONE);
			}
			else
			{
				friend_list_progress_bar.setVisibility(View.GONE);
				ivFriendsListIcon.setImageResource(R.drawable.no_image_smal_event);
			}
			
			convertView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					if(ivCheckBox.getTag().equals(UNSELECTED))
					{
						int id=objFriend.id;
						vctFriendIds.add(id);
						ivCheckBox.setTag(SELECTED);
						ivCheckBox.setImageResource(R.drawable.btn_check_on);
					}
					
					else if(ivCheckBox.getTag().equals(SELECTED))
					{
						int id=objFriend.id;
						ivCheckBox.setTag(UNSELECTED);
						ivCheckBox.setImageResource(R.drawable.btn_check_off);
						vctFriendIds.remove(new Integer(id));
					}
				}
			});
			tvFriendsListName.setText(objFriend.firstname+" "+objFriend.lastname);
			return convertView;
		}
	}
	
	public void showFrindsPopup(int eventId)
	{
		LayoutInflater inflater = getLayoutInflater();
		RelativeLayout popup=(RelativeLayout) inflater.inflate(R.layout.friends_popup, null);
		
		ImageView ivClose = (ImageView)popup.findViewById(R.id.ivClose);
		LinearLayout llFriendsList = (LinearLayout)popup.findViewById(R.id.llFriendsList);
		
		ivClose.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
			}
		});
		vecFriends = new ArrayList<UserFriend>();
		vecFriends=friendBL.getUsrFrendAttendingEvents(Integer.toString(eventId));
		if(vecFriends.size()!=0)
		{
			for(int i=0; i<vecFriends.size(); i++)
			{
				UserFriend friend = vecFriends.get(i);
				final int friendId=friend.ID1;
				LinearLayout llFriendItem = (LinearLayout) inflater.inflate(R.layout.friends_list_cell, null);
				llFriendsList.addView(llFriendItem);
				llFriendItem.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						Intent intent = new Intent(ShareEvent.this,FriendsProfile.class);
						intent.putExtra(USERFRIENDID,friendId);
						
						TabGroupActivity pActivity = (TabGroupActivity)ShareEvent.this.getParent();
						pActivity.startChildActivity(FRIENDSPROFILE, intent);
						
						customDialog.dismiss();
					}
				});
				
				ImageView ivFriendsListIcon = (ImageView) llFriendItem.findViewById(R.id.ivFriendsListIcon);
				TextView tvFriendsListName = (TextView) llFriendItem.findViewById(R.id.tvFriendsListName);
				ProgressBar progressBar=(ProgressBar)llFriendItem.findViewById(R.id.friend_list_progress_bar);
				String friendImageUrl=AppConstants.IMAGE_HOST_URL+friend.Image1;
				if(!friend.Image1.equals(AppConstants.NOIMAGE))
				{
					if(!objDrawableManager.fetchDrawableOnThread(friendImageUrl, ivFriendsListIcon,50,50,friend.image_id,friend.isImageUpdated)){
						ivFriendsListIcon.setImageResource(R.drawable.no_image_smal_event);
					}
					progressBar.setVisibility(View.GONE);
				}
				else
				{
					progressBar.setVisibility(View.GONE);
					ivFriendsListIcon.setImageResource(R.drawable.no_image_smal_event);
				}
				tvFriendsListName.setText(friend.FirstName1+" "+friend.LastName1);
			}
		}
		customDialog = new PopUpDailog(getParent(), popup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}

	public void showFavouritePopup(final int eventId,final int businessId)
	{

		LayoutInflater inflater = getLayoutInflater();
		LinearLayout popup=(LinearLayout) inflater.inflate(R.layout.category_popup, null);
		Button btnCompany = (Button)popup.findViewById(R.id.btnCompany);
		Button btnEvent = (Button)popup.findViewById(R.id.btnEvent);
		EventsBusinessLayer ebl = new EventsBusinessLayer();
		final UserAccountBusinessLayer uabl = new UserAccountBusinessLayer();
		TextView dialogText = (TextView) popup.findViewById(R.id.add_favorite_text);
		if(ebl.isEventAttendedByUser(eventId, AppConstants.USER_ID) && uabl.isBusinessIsInUserFavorites(businessId, AppConstants.USER_ID)){
			dialogText.setText(R.string.all_added_to_favorite);
		}else{
			dialogText.setText(R.string.add_to_favorite);
		}
		if(ebl.isEventAttendedByUser(eventId, AppConstants.USER_ID)){
			btnEvent.setVisibility(View.GONE);
		}
		
		if(uabl.isBusinessIsInUserFavorites(businessId, AppConstants.USER_ID)){
			btnCompany.setVisibility(View.GONE);
		}
		
		btnCompany.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				int duration = Toast.LENGTH_SHORT;
				CharSequence text="";
				int status = Ga2ooJsonParsers.getInstance().addBusinessToUser(AppConstants.USER_ID, businessId);
				
				
				if(status>0)
				{
					Business tmpBusiness = new Business();
					tmpBusiness.businessid = businessId;
					tmpBusiness.businessname= " ";
					tmpBusiness.useraddedbusinessid = 0;
					tmpBusiness.date_updated = "0";
					uabl.InsertUserBusiness(tmpBusiness);
					text = getResources().getString(R.string.company_added_successfully);  
					Toast toast = Toast.makeText(getApplicationContext(), text, duration);
					toast.show();
				}
				else if(status==0)
				{
					 text =getResources().getString(R.string.compamy_already_in_your_favorite);  
					 Toast toast = Toast.makeText(getApplicationContext(), text, duration);
						toast.show();
				}else if(status==-1){
					DialogUtility.showConnectionErrorDialog(ShareEvent.this.getParent());
				}
				customDialog.dismiss();
			}
		});
		
		btnEvent.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v) 
			{
				customDialog.dismiss();
				int duration = Toast.LENGTH_SHORT;
				CharSequence text="";
				int status =Ga2ooJsonParsers.getInstance().addEventToUser(AppConstants.USER_ID, eventId);
				if(status>0)
				{
					text = getResources().getString(R.string.event_successfully_added);  ;    
					eventsBL=new EventsBusinessLayer();
					eventsBL.addToAttending(eventId, AppConstants.USER_ID);
					userActBL.insertUserFavorites(AppConstants.USER_ID, eventId,status);
					Toast toast = Toast.makeText(getApplicationContext(), text, duration);
					toast.show();
				}
				else if(status==0)
				{
					 text = getResources().getString(R.string.event_already_in_palendar);    
					 Toast toast = Toast.makeText(getApplicationContext(), text, duration);
						toast.show();
				}else if(status==-1){
					DialogUtility.showConnectionErrorDialog(ShareEvent.this.getParent());
				}
				
			 }
		});
		customDialog = new PopUpDailog(getParent(), popup, 220, LayoutParams.WRAP_CONTENT);
		customDialog.show();
	}

}
