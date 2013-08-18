package com.ga2oo.palendar.services;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.google.gson.JsonElement;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.jsonparsers.UserFriendWrapper;
import com.ga2oo.parsing.net.JsonHttpHelper;

public class FriendsService extends Service {
	
	private static final String LOGTAG = "FriendsService";
	 private Looper mServiceLooper;
	public boolean isRequestFinished=true;
	private FriendsRequestHandler mServiceHandler;
//	Timer myTimer;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.v(LOGTAG, "Service created");
//		myTimer = new Timer();
//		myTimer.schedule(new TimerTask() {
//			@Override
//			public void run() {
//				Log.v(LOGTAG, "service running");
//			}
//			}, 0, 100);
		
		 HandlerThread thread = new HandlerThread("ServiceStartArguments",
		           Process.THREAD_PRIORITY_BACKGROUND);
		    thread.start();
		    mServiceLooper = thread.getLooper();
		    mServiceHandler = new FriendsRequestHandler(mServiceLooper);
		    mServiceHandler.handleMessage(null);
		super.onCreate();
	}


	@Override
	public void onDestroy() {
	//	myTimer.cancel();
		Log.v(LOGTAG, "Service destroyed");
	}


	private final class FriendsRequestHandler extends Handler {
		private JsonElement element;
		private JsonHttpHelper jsonHelper;
		private UserAccountBusinessLayer uabl;

		public FriendsRequestHandler(Looper mServiceLooper) {
			super(mServiceLooper);
		}

		@Override
		public void handleMessage(Message msg) {
				uabl = new UserAccountBusinessLayer();
				jsonHelper =new JsonHttpHelper();
				Log.i(LOGTAG, "Loading friends list start....");
				try {
					element = jsonHelper.sendGetRequest(AppConstants.JSON_HOST_URL+AppConstants.FRIEND_URL+AppConstants.USER_ID);
				} catch (Exception e) {
					Log.e(LOGTAG, "Error in loading friends list.");
					e.printStackTrace();
				}
				Object userFriendObjects =  jsonHelper.parse(element, UserFriendWrapper.class);
				if(userFriendObjects!=null){
					for(int i=0;i<((UserFriendWrapper)userFriendObjects).getUseraccount().friendships.size();i++){
						uabl.InsertFriend(((UserFriendWrapper)userFriendObjects).getUseraccount().friendships.get(i));
					}
					Log.i(LOGTAG, "Loading friends list successfully completed.");
				}


			super.handleMessage(msg);
		}

	}
}
