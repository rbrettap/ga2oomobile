package com.rjb.android.impl.core.log;

import com.winit.ga2oo.BuildConfig;
import com.rjb.android.impl.core.FConstants;

import android.text.TextUtils;
import android.util.Log;

// An Android Log class that supports enable/disable of Log messages.

public final class Flog {
    private static int MESSAGE_LIMIT = 4000;
    private static boolean fIsDisabled = false;

    public static final int VERBOSE = Log.VERBOSE;
    public static final int DEBUG = Log.DEBUG;
    public static final int INFO = Log.INFO;
    public static final int WARN = Log.WARN;
    public static final int ERROR = Log.ERROR;
    public static final int ASSERT = Log.ASSERT;

    private static int fCurrentLogLevel = WARN;

    // Please modify the value of fInternalLogging with caution.
    // for Eclipse build: the value of fInternalLogging will be used without modification,
    // for ant build: the value will be substituted by something like:
    // <regexp pattern="fInternalLogging\s*=\s*\w+\s*;" />
    // <substitution expression="fInternalLogging = ${env.logging};" />
    private static boolean fInternalLogging = true;

    // Calling disableLog will prevent the Flurry agent from sending Log
    // messages.
    public static void disableLog() {
        fIsDisabled = true;
    }

    // Calling enableLog will cause the Flurry agent to send normal Log
    // messages.
    public static void enableLog() {
        fIsDisabled = false;
    }

    public static int getLogLevel() {
        return fCurrentLogLevel;
    }

    /**
     * Sets the current logging level. If the log level is set to N, logging statements greater than
     * N won't be printed to android.util.Log. Defaults to printing warnings and errors. The
     * hierarchy of logging levels is: VERBOSE DEBUG INFO WARN ERROR ASSERT
     * 
     * @param level
     */
    public static void setLogLevel(int level) {
        fCurrentLogLevel = level;
    }

    public static boolean isLoggable(String tag, int level) {
        return Log.isLoggable(tag, level);
    }

    public static void d(String tag, String msg, Throwable tr) {
        logPublic(DEBUG, tag, msg, tr);
    }

    public static void d(String tag, String msg) {
        logPublic(DEBUG, tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        logPublic(ERROR, tag, msg, tr);
    }

    public static void e(String tag, String msg) {
        logPublic(ERROR, tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        logPublic(INFO, tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        logPublic(INFO, tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        logPublic(VERBOSE, tag, msg, tr);
    }

    public static void v(String tag, String msg) {
        logPublic(VERBOSE, tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        logPublic(WARN, tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        logPublic(WARN, tag, msg);
    }

    public static void p(int i, String tag, String msg, Throwable tr) {
        logPrivate(i, tag, msg, tr);
    }

    public static void p(int i, String tag, String msg) {
        logPrivate(i, tag, msg);
    }

    private static void logPublic(int priority, String tag, String msg, Throwable tr) {
        logPublic(priority, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    private static void logPublic(int priority, String tag, String msg) {
        if (!fIsDisabled && fCurrentLogLevel <= priority) {
            logInternal(priority, tag, msg);
        }
    }

    private static void logPrivate(int priority, String tag, String msg, Throwable tr) {
        logPrivate(priority, tag, msg + '\n' + Log.getStackTraceString(tr));
    }

    private static void logPrivate(int priority, String tag, String msg) {
        if (fInternalLogging) {
            logInternal(priority, tag, msg);
        }
    }

    private static void logInternal(int priority, String tag, String msg) {
        String finalTag = BuildConfig.DEBUG ? tag : FConstants.kLogTag;

        // Divide message on chunks and log them separately. Proper approach would be to check
        // return value of Log.println for calculating the size of next chunk, but Log.println
        // returns number of bytes written for some internal message buffer, not number of
        // characters written for incoming message string. So fixed length for chunks is used here.
        int chunkStart = 0;
        int end = TextUtils.isEmpty(msg) ? 0 : msg.length();
        while (chunkStart < end) {
            int chunkEnd = (MESSAGE_LIMIT > end - chunkStart) ? end : chunkStart + MESSAGE_LIMIT;
            String messageChunk = msg.substring(chunkStart, chunkEnd);
            int bytesWritten = Log.println(priority, finalTag, messageChunk);
            if (0 >= bytesWritten) {
                // failed to write
                break;
            }

            chunkStart = chunkEnd;
        }
    }
}
