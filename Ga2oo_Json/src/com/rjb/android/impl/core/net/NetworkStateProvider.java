
package com.rjb.android.impl.core.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

import com.rjb.android.impl.core.RJBCore;
import com.rjb.android.impl.core.collections.WeakList;

/**
 * Helper class register for network state changes
 */
public class NetworkStateProvider extends BroadcastReceiver {

    /**
     * Intent filter name for listening network status
     */
    private static final String ANDROID_NET_CONN_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    /**
     * Network state flag
     */
    boolean mIsNetworkEnable;
    Boolean mOverrideNetworkState;

    private final WeakList<NetworkStateListener> mListeners;

    private boolean mHasPermission = false;
    private static final String kPermissionForNetworkCheck = "android.permission.ACCESS_NETWORK_STATE";

    private static NetworkStateProvider sNetworkStateProvider;

    public enum NetworkType {

        NONE_OR_UNKNOWN(0),
        WIFI(1),
        CELL(2);

        private int i;

        NetworkType(int i) {
            this.i = i;
        }

        public int getValue() {
            return i;
        }
    };

    public NetworkStateProvider() {
        mListeners = new WeakList<NetworkStateListener>();
    }

    public static synchronized NetworkStateProvider getInstance() {
        if (sNetworkStateProvider == null) {
            sNetworkStateProvider = new NetworkStateProvider();
        }

        return sNetworkStateProvider;
    }

    public synchronized void initAndRegisterIfNeeded() {        
        Context context = RJBCore.getInstance().getApplicationContext();
        mHasPermission = context.checkCallingOrSelfPermission(kPermissionForNetworkCheck) == PackageManager.PERMISSION_GRANTED;
        
        mIsNetworkEnable = getNetworkStatus(context);

        if (mHasPermission) {
            register();
        }
    }

    public synchronized void addListener(NetworkStateListener listener) {
        mListeners.add(listener);
    }

    public synchronized boolean removeListener(NetworkStateListener listener) {
        return mListeners.remove(listener);
    }

    public synchronized void removeAllListeners() {
        mListeners.clear();
    }

    private synchronized List<NetworkStateListener> getListeners() {
        return mListeners.get();
    }

    synchronized boolean hasListener(NetworkStateListener listener) {
        return mListeners.contains(listener);
    }

    synchronized int getListenerCount() {
        return mListeners.size();
    }

    /**
     * @brief Used for tests
     * @param aState null not to override netework state, true - to get the network is available and false if not available
     */
    public void setOverrideNetworkState(Boolean aState) {
        mOverrideNetworkState = aState;
    }
    
    public boolean isNetworkEnabled() {
        if(mOverrideNetworkState != null) {
            return mOverrideNetworkState;
        }
        
        return mIsNetworkEnable;
    }

    void register() {
        Context context = RJBCore.getInstance().getApplicationContext();
        mIsNetworkEnable = getNetworkStatus(context);
        context.registerReceiver(this, new IntentFilter(ANDROID_NET_CONN_CONNECTIVITY_CHANGE));
    }

    void unregister() {
        Context context = RJBCore.getInstance().getApplicationContext();
        context.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean networkStatus = getNetworkStatus(context);

        if (mIsNetworkEnable != networkStatus) {
            mIsNetworkEnable = networkStatus;
            
            List<NetworkStateListener> listeners = getListeners();
            for (NetworkStateListener listener : listeners) {
                listener.onNetworkStateChanged(mIsNetworkEnable);
            }
        }
    }

    private boolean getNetworkStatus(Context aContext) {

        if (!mHasPermission || aContext == null) {
            // if permission isn't granted, pretend network is enabled
            // a network request that fails will fail more gracefully than below without permission
            // granted
            return true;
        }

        ConnectivityManager cm = (ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentNetworkInfo = cm.getActiveNetworkInfo();
        return currentNetworkInfo != null && currentNetworkInfo.isConnected() ? true : false;
    }

    public NetworkType getNetworkType() {
        if (!mHasPermission) {
            // if permission isn't granted, pretend network is enabled
            // a network request that fails will fail more gracefully than below without permission
            // granted
            return NetworkType.NONE_OR_UNKNOWN;
        }

        ConnectivityManager cm = (ConnectivityManager) RJBCore.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo currentNetworkInfo = cm.getActiveNetworkInfo();
        if (currentNetworkInfo == null || !currentNetworkInfo.isConnected()) {
            return NetworkType.NONE_OR_UNKNOWN;
        }

        switch(currentNetworkInfo.getType()) {
            case ConnectivityManager.TYPE_ETHERNET:
            case ConnectivityManager.TYPE_WIFI:
                return NetworkType.WIFI;

            case ConnectivityManager.TYPE_BLUETOOTH:
            case ConnectivityManager.TYPE_MOBILE:
            case ConnectivityManager.TYPE_MOBILE_DUN:
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
            case ConnectivityManager.TYPE_MOBILE_MMS:
            case ConnectivityManager.TYPE_MOBILE_SUPL:
            case ConnectivityManager.TYPE_WIMAX:
                return NetworkType.CELL;

            case ConnectivityManager.TYPE_DUMMY:
                return NetworkType.NONE_OR_UNKNOWN;

            default:
                return NetworkType.CELL;
        }
    }

}
