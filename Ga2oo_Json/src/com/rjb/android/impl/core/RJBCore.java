package com.rjb.android.impl.core;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;

public class RJBCore {
    private static RJBCore sRJBCore;
    
    private final Context fApplicationContext;
    private final Handler fMainHandler;
    
    public static synchronized void init(Context context) {
        if (sRJBCore != null) {
            return;
        }

        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }

        sRJBCore = new RJBCore(context);
    }

    public static RJBCore getInstance() {
        return sRJBCore;
    }

    private RJBCore(Context context) {
        fApplicationContext = context.getApplicationContext();
        fMainHandler = new Handler(Looper.getMainLooper());
    }
    
    public Context getApplicationContext() {
        return fApplicationContext;
    }
    
    public PackageManager getPackageManager() {
        return fApplicationContext.getPackageManager();
    }
    
    public Resources getResources() {
        return fApplicationContext.getResources();
    }
    
    public Configuration getConfiguration() {
        return fApplicationContext.getResources().getConfiguration();
    }
    
    public DisplayMetrics getDisplayMetrics() {
        return fApplicationContext.getResources().getDisplayMetrics();
    }
            
    public Handler getMainHandler() {
        return fMainHandler;
    }
    
    public void postOnMainHandler(Runnable r) {
        if(r == null)
        {
            return;
        }
        
        fMainHandler.post(r);
    }
}
