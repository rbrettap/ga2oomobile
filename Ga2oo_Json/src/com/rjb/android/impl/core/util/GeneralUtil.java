package com.rjb.android.impl.core.util;

import com.rjb.android.impl.core.RJBCore;
import com.rjb.android.impl.core.log.Flog;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Pair;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Static utility methods that do NOT reference advertising or AppCloud code
 *
 */
public final class GeneralUtil
{
    private static final String kLogTag = GeneralUtil.class.getSimpleName();
    
    public static final int kCopyBufferSize = 1024; // 1K
    private static final int kMaxReportedStringLength = 255;

    public static String sanitize(String string)
    {
        return sanitize(string, kMaxReportedStringLength);
    }
    
    public static String sanitize(String string, int maxLength)
    {
        if (string == null)
        {
            return "";
        }
        else if (string.length() <= maxLength)
        {
            return string;
        }
        else
        {
            return string.substring(0, maxLength);
        }
    }
    
    public static String urlEncode(String s)
    {
        try
        {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            Flog.p(Flog.WARN, kLogTag, "Cannot encode '" + s + "'");
            return "";
        }
    }
    
    public static String urlDecode(String s)
    {
        try
        {
            return URLDecoder.decode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            Flog.p(Flog.WARN, kLogTag, "Cannot decode '" + s + "'");
            return "";
        }
    }
    
    public static void safeClose(Closeable c)
    {
        try
        {
            if (c != null)
            {
                c.close();
            }
        }
        catch(Throwable ignored)
        {
            // ignore
        }
    }
    
    /** 
     * May return a null.
     */
    public static byte[] toSHA1(String text) 
    {
        MessageDigest md;
        try 
        {
            md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes(), 0, text.length());
            return md.digest();
        } 
        catch (NoSuchAlgorithmException e) 
        {
            Flog.p(Flog.ERROR, kLogTag, "Unsupported SHA1: " + e.getMessage());
            return null;
        }
    }

    public static String hexEncode(byte[] bytes)
    {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        char[] chars = new char[] {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        for(byte b : bytes)
        {
            byte lowBits = (byte)(b & 0x0000000f);
            byte highBits = (byte)((b & 0x000000f0) >> 4);
            buf.append(chars[highBits]);
            buf.append(chars[lowBits]);
        }
        return buf.toString();
    }

//  AdUnity
    public static boolean checkExpiration(long expirationTime)
    {
        boolean active = false;
        if (System.currentTimeMillis() <= expirationTime)
        {
            active = true;
        }
        return active;
    }
    
//  AdUnity
    public static Map<String, String> convertJsonToMap(String json)
    {
        Map<String, String> contentMap = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            @SuppressWarnings("unchecked")
            Iterator<String> key = jsonObject.keys();
            while (key.hasNext()) {
                String name = key.next();
                try {
                    String value = jsonObject.getString(name);
                    contentMap = new HashMap<String, String>();
                    contentMap.put(name, value);
                } catch (JSONException e) {
                    Flog.p(Flog.DEBUG, kLogTag, "Cannot iterate over key: " + name);
                    return contentMap;
                }
            }
        } catch (JSONException e) {
            Flog.p(Flog.DEBUG, kLogTag, "Cannot convert json to map for: " + json);
            return contentMap;
        }
        return contentMap;
    }
    
//  AdUnity
    public static boolean callIntent(Context context, String urlString)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(urlString));
        if(canIUseThisIntent(intent))
        {
            Flog.p(Flog.DEBUG, kLogTag, "Launching intent for " + urlString);
            context.startActivity(intent);
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean canIUseThisIntent(Intent intent) {
        PackageManager packageManager = RJBCore.getInstance().getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static boolean canHandleIntentInternally(Intent intent) {
        boolean result = false;
        if (null != intent) {
            ComponentName componentName = intent.resolveActivity(RJBCore.getInstance().getPackageManager());
            result = RJBCore.getInstance().getApplicationContext().getPackageName().equals(componentName.getPackageName());
        }

        return result;
    }

    /**
     * Provides logical density of the display
     */
    public static float getDensity()
    {
        DisplayMetrics dm = RJBCore.getInstance().getDisplayMetrics();
        return dm.density;
    }

    /**
     * Converts concrete pixels to device independent pixels based on display density
     */
    public static int pxToDp(int px)
    {
        DisplayMetrics dm = RJBCore.getInstance().getDisplayMetrics();
        return (int) Math.round(px / dm.density);
    }

    /**
     * Converts device independent pixels to concrete pixels based on display density
     */
    public static int dpToPx(int dip)
    {
        DisplayMetrics dm = RJBCore.getInstance().getDisplayMetrics();
        return (int) Math.round(dip * dm.density);
    }

    public static int getWidthPx()
    {
        DisplayMetrics displayMetrics = RJBCore.getInstance().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getHeightPx()
    {
        DisplayMetrics displayMetrics = RJBCore.getInstance().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * Return width/height of the screen depending on current orientation.
     */
    public static Pair<Integer, Integer> getScreenDipByOrientation()
    {
        if(getOrientation() == Configuration.ORIENTATION_PORTRAIT)
        {
            return Pair.create(getWidthDp(), getHeightDp());
        }
        else
        {
            return Pair.create(getHeightDp(), getWidthDp());
        }
    }

    public static int getWidthDp()
    {
        return pxToDp(getWidthPx());
    }

    public static int getHeightDp()
    {
        return pxToDp(getHeightPx());
    }
    
    public static int getOrientation()
    {
        return RJBCore.getInstance().getConfiguration().orientation;
    }
    /**
     * Returns the width/height of the screen in dip
     */
    public static Pair<Integer, Integer> getScreenDimsInDip()
    {
        return Pair.create(getWidthDp(), getHeightDp());
    }
    /**
     * Returns the width/height of the screen in dip as reported by the iPhone
     * (i.e. always reporting portrait dimensions)
     */
    public static Pair<Integer, Integer> getScreenDimsInDipAsiPhone()
    {
        int actualWidth = getWidthDp();
        int actualHeight = getHeightDp();
        switch(getOrientation())
        {
            case Configuration.ORIENTATION_LANDSCAPE: // reversed for landscape, same as iPhone
                return Pair.create(actualHeight, actualWidth);
            case Configuration.ORIENTATION_PORTRAIT:
            default:
                return Pair.create(actualWidth, actualHeight);
        }
    }
    public static String escapeJSStringLiteral(String s)
    {
        return s.replace("'", "\\'").
            replace("\\n", "").
            replace("\\r", "").
            replace("\\t", "");
    }

    public static Map<String, String> getParams(String query)
    {
        Map<String, String> params = new HashMap<String, String>();
        if (!TextUtils.isEmpty(query))
        {
            for (String p : query.split("&"))
            {
                String[] q = p.split("=");
                if (!q[0].equals("event")) // event name is not a param
                {
                    params.put(urlDecode(q[0]), urlDecode(q[1]));
                }
            }
        }
        return params;
    }
    
    public static long hash64(String string) {
        if (string == null) {
            return 0;
        }

        long h = 1125899906842597L; // prime
        int len = string.length();

        for (int i = 0; i < len; i++) {
            h = 31 * h + string.charAt(i);
        }

        return h;
    }

    public static long copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[kCopyBufferSize];

        long total = 0;
        while (true) {
            int read = in.read(buf);
            if (read < 0) {
                break;
            }

            out.write(buf, 0, read);
            total += read;
        }

        return total;
    }

    public static byte[] copyBytes(InputStream is) throws IOException {
        if (is == null) {
            return null;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        GeneralUtil.copyStream(is, os);
        return os.toByteArray();
    }
}
