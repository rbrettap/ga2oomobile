package com.rjb.android.impl.core.http;

import com.rjb.android.impl.core.executor.PriorityComparator;
import com.rjb.android.impl.core.executor.TrackedThreadPoolExecutor;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Http request manager.
 *
 * @author rbrett
 */
public final class HttpRequestManager extends TrackedThreadPoolExecutor<HttpStreamRequest> {
    protected static final int fMaxThreadCount = 5;
    protected static final int fKeepAliveTimeMS = 5000; // 5 seconds
    protected static final int kInitialQueueCapacity = 11;

    private static HttpRequestManager instance_ = null;

    public static synchronized HttpRequestManager getInstance() {
        if (instance_ == null) {
            instance_ = new HttpRequestManager();
        }

        return instance_;
    }

    protected HttpRequestManager() {
        super(HttpRequestManager.class.getName(), 0, fMaxThreadCount, fKeepAliveTimeMS, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<Runnable>(kInitialQueueCapacity, new PriorityComparator()));
    }
}
