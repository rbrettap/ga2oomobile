package com.rjb.android.impl.core.net;

/**
 * Interface for network state listener
 */
public interface NetworkStateListener {
    /**
     * Method for notification about change in the network state
     *
     * @param isNetworkEnable
     */
    void onNetworkStateChanged(boolean isNetworkEnable);
}
