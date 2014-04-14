/*
 * Copyright 2014 RJB. All Rights Reserved.
 * 
 * Created by Richard Brett on April 6, 2014.
 * 
 * Author: Richard Brett
 */

package com.rjb.android.impl.core.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;

import com.rjb.android.impl.core.RJBCore;

import java.lang.reflect.Method;

public class DeviceScreenUtil {

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static Point getRealDisplayDimensions() {
        WindowManager wm = (WindowManager) RJBCore.getInstance().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        // Refer link http://stackoverflow.com/questions/10991194/android-displaymetrics-returns-incorrect-screen-size-in-pixels-on-ics
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(outPoint);
        } else if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                outPoint.x = (Integer) mGetRawW.invoke(display);
                outPoint.y = (Integer) mGetRawH.invoke(display);
            } catch (Throwable e) {
                display.getSize(outPoint);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(outPoint);
        } else {
            outPoint.x = display.getWidth();
            outPoint.y = display.getHeight();
        }
        return outPoint;
    }

    @SuppressWarnings("deprecation")
    public static DisplayMetrics getDisplayMetrics() {
        WindowManager wm = (WindowManager) RJBCore.getInstance().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        return displayMetrics;
    }

    @SuppressLint("NewApi")
    public static DisplayMetrics getRealDisplayMetrics() {
        WindowManager wm = (WindowManager) RJBCore.getInstance().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics displayMetrics;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            displayMetrics = new DisplayMetrics();
            display.getRealMetrics(displayMetrics);
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            try {
                displayMetrics = new DisplayMetrics();
                Method getRealMetrics = Display.class.getMethod("getRealMetrics");
                getRealMetrics.invoke(display, displayMetrics);
            } catch (Exception e) {
                displayMetrics = getDisplayMetrics();
            }
        } else {
            displayMetrics = getDisplayMetrics();
        }

        return displayMetrics;
    }

    /**
     * Provides logical density of the display
     */
    public static float getDensity() {
        DisplayMetrics dm = getRealDisplayMetrics();
        return dm.density;
    }

    /**
     * Converts concrete pixels to device independent pixels based on display density
     */
    public static int pxToDp(int px) {
        DisplayMetrics dm = getRealDisplayMetrics();
        return (int) Math.round(px / dm.density);
    }

    /**
     * Converts device independent pixels to concrete pixels based on display density
     */
    public static int dpToPx(int dip) {
        DisplayMetrics dm = getRealDisplayMetrics();
        return (int) Math.round(dip * dm.density);
    }

    public static int getWidthPx() {
        return getRealDisplayDimensions().x;
    }

    public static int getHeightPx() {
        return getRealDisplayDimensions().y;
    }

    /**
     * Return width/height of the screen depending on current orientation.
     */
    public static Pair<Integer, Integer> getScreenDipByOrientation(int aOrientation) {
        if (aOrientation == Configuration.ORIENTATION_PORTRAIT) {
            return Pair.create(getWidthDp(), getHeightDp());
        } else {
            return Pair.create(getHeightDp(), getWidthDp());
        }
    }

    public static int getWidthDp() {
        return pxToDp(getWidthPx());
    }

    public static int getHeightDp() {
        return pxToDp(getHeightPx());
    }

    @SuppressWarnings("deprecation")
    public static int getOrientation() {
        Point point = getRealDisplayDimensions();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if (point.x == point.y) {
            orientation = Configuration.ORIENTATION_SQUARE;
        } else {
            if (point.x < point.y) {
                orientation = Configuration.ORIENTATION_PORTRAIT;
            } else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }

    /**
     * Returns the width/height of the screen in dip
     */
    public static Pair<Integer, Integer> getScreenDimsInDip() {
        return Pair.create(getWidthDp(), getHeightDp());
    }

    /**
     * Returns the width/height of the screen in dip as reported by the iPhone (i.e. always
     * reporting portrait dimensions)
     */
    public static Pair<Integer, Integer> getScreenDimsInDipAsiPhone(int aOrientation) {
        int actualWidth = getWidthDp();
        int actualHeight = getHeightDp();
        switch (aOrientation) {
            case Configuration.ORIENTATION_LANDSCAPE: // reversed for landscape, same as iPhone
                return Pair.create(actualHeight, actualWidth);
            case Configuration.ORIENTATION_PORTRAIT:
            default:
                return Pair.create(actualWidth, actualHeight);
        }
    }

}
