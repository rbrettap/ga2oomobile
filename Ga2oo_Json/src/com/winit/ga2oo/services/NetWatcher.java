package com.winit.ga2oo.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetWatcher extends BroadcastReceiver {
	
	private static final String LOGTAG = "NetWatcher";
    @Override
    public void onReceive(Context context, Intent intent) {
        //here, check that the network connection is available. If yes, start your service. If not, stop your service.
       ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo info = cm.getActiveNetworkInfo();
       if (info != null) {
           if (info.isConnected()) {
               Log.v(LOGTAG, "service start from receiver");
               Intent intent1 = new Intent(context, FriendsService.class);
               context.startService(intent1);
           }
           else {
        	   Log.v(LOGTAG, "service start from receiver");
               Intent intent2 = new Intent(context, FriendsService.class);
               context.stopService(intent2);
           }
       }
    }
}
