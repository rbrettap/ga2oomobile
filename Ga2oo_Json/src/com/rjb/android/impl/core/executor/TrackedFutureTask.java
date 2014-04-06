package com.rjb.android.impl.core.executor;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * TrackedFutureTask
 *
 * Created by rbrett on 11/13/13.
 */
public class TrackedFutureTask<V> extends FutureTask<V> {
    private final WeakReference<Callable<V>> fCallable;
    private final WeakReference<Runnable> fRunnable;

    public TrackedFutureTask(Callable<V> callable) {
        super(callable);

        fCallable = new WeakReference<Callable<V>>(callable);
        fRunnable = null;
    }

    public TrackedFutureTask(Runnable runnable, V result) {
        super(runnable, result);

        fCallable = null;
        fRunnable = new WeakReference<Runnable>(runnable);
    }

    public Callable<V> getCallable() {
        return fCallable.get();
    }

    public Runnable getRunnable() {
        return fRunnable.get();
    }
}
