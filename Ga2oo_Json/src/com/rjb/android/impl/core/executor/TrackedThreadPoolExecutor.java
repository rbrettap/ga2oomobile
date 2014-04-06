
package com.rjb.android.impl.core.executor;

import com.rjb.android.impl.core.collections.ArrayListMultimap;
import com.rjb.android.impl.core.log.Flog;
import com.rjb.android.impl.core.thread.RJBThreadFactory;
import com.rjb.android.impl.core.util.SafeRunnable;
import com.rjb.android.impl.core.util.TrackedSafeRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TrackedThreadPoolExecutor<T extends TrackedSafeRunnable> {
    private static final String kLogTag = TrackedThreadPoolExecutor.class.getSimpleName();

    private final ArrayListMultimap<Object, T> fForwardMap = new ArrayListMultimap<Object, T>();
    private final HashMap<T, Object> fReverseMap = new HashMap<T, Object>();
    private final HashMap<T, Future<?>> fFutureMap = new HashMap<T, Future<?>>();

    private static final int kMaxThreadCountDefault = 1;
    private static final int kKeepAliveTimeMS = 1000; // 1 second

    private final ThreadPoolExecutor fExecutor;

    public TrackedThreadPoolExecutor(String name) {
        this(name, kMaxThreadCountDefault, kKeepAliveTimeMS);
    }

    public TrackedThreadPoolExecutor(String name, int maxThreadCount, int keepAliveMS) {
        this(name, maxThreadCount, maxThreadCount, keepAliveMS, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    @SuppressWarnings("unchecked")
    public TrackedThreadPoolExecutor(String name, int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        fExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue) {

            @Override
            protected void beforeExecute(Thread t, final Runnable r) {
                super.beforeExecute(t, r);

                final T trackedSafeRunnable = getTrackedSafeRunnable(r);
                if (trackedSafeRunnable == null) {
                    return;
                }

                new SafeRunnable() {
                    @Override
                    public void safeRun() {
                        trackedSafeRunnable.executionStarted();
                    }
                }.run();
            }

            @Override
            protected void afterExecute(final Runnable r, Throwable t) {
                super.afterExecute(r, t);

                final T trackedSafeRunnable = getTrackedSafeRunnable(r);
                if (trackedSafeRunnable == null) {
                    return;
                }

                synchronized(fFutureMap) {
                    fFutureMap.remove(trackedSafeRunnable);
                }
                TrackedThreadPoolExecutor.this.removeTrackingEntries(trackedSafeRunnable);

                new SafeRunnable() {
                    @Override
                    public void safeRun() {
                        trackedSafeRunnable.executionFinished();
                    }
                }.run();
            }

            @Override
            protected <V> RunnableFuture<V> newTaskFor(Callable<V> c) {
                throw new UnsupportedOperationException("Callable not supported");
            }

            @Override
            protected <V> RunnableFuture<V> newTaskFor(Runnable r, V v) {
                TrackedFutureTask<V> trackedFutureTask = new TrackedFutureTask<V>(r, v);

                synchronized(fFutureMap) {
                    fFutureMap.put((T) r, trackedFutureTask);
                }

                return trackedFutureTask;
            }
        };

        fExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy() {
            @Override
            public void rejectedExecution(final Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                super.rejectedExecution(runnable, threadPoolExecutor);

                final T trackedSafeRunnable = getTrackedSafeRunnable(runnable);
                if (trackedSafeRunnable == null) {
                    return;
                }

                synchronized(fFutureMap) {
                    fFutureMap.remove(trackedSafeRunnable);
                }
                removeTrackingEntries(trackedSafeRunnable);

                new SafeRunnable() {
                    @Override
                    public void safeRun() {
                        trackedSafeRunnable.executionRejected();
                    }
                }.run();
            }
        });

        RJBThreadFactory factory = new RJBThreadFactory(name, Thread.MIN_PRIORITY);
        fExecutor.setThreadFactory(factory);
    }

    public synchronized void execute(Object tag, T runnable) {
        if (tag == null || runnable == null) {
            return;
        }

        addTrackingEntries(tag, runnable);
        fExecutor.submit(runnable);
    }

    public synchronized void cancel(Object tag) {
        if (tag == null) {
            return;
        }

        Collection<T> runnables = new HashSet<T>();
        runnables.addAll(fForwardMap.get(tag));
        for (T runnable : runnables) {
            cancel(runnable);
        }
    }

    public synchronized void cancel(final T runnable) {
        if (runnable == null) {
            return;
        }

        Future<?> future;
        synchronized(fFutureMap) {
            future = fFutureMap.remove(runnable);
        }
        removeTrackingEntries(runnable);

        if (future != null) {
            future.cancel(true);
        }

        new SafeRunnable() {
            @Override
            public void safeRun() {
                runnable.executionCancelled();
            }
        }.run();
    }

    public synchronized void cancelAll() {
        Set<Object> tags = new HashSet<Object>();
        tags.addAll(fForwardMap.keySet());

        for (Object tag : tags) {
            cancel(tag);
        }
    }

    public synchronized List<T> getTasks(Object tag) {
        if (tag == null) {
            return Collections.emptyList();
        }

        List<T> tasks = new ArrayList<T>();
        tasks.addAll(fForwardMap.get(tag));

        return tasks;
    }

    public synchronized long getTaskCount() {
        return fReverseMap.size();
    }

    public synchronized long getTaskCount(Object tag) {
        if (tag == null) {
            return 0;
        }

        return fForwardMap.get(tag).size();
    }

    public synchronized long getTaskCount(T runnable) {
        if (runnable == null) {
            return 0;
        }

        return (fReverseMap.get(runnable) == null ? 0 : 1);
    }

    private synchronized void addTrackingEntries(Object tag, T runnable) {
        fForwardMap.put(tag, runnable);
        fReverseMap.put(runnable, tag);
    }

    private synchronized void removeTrackingEntries(T runnable) {
        removeTrackingEntries(fReverseMap.get(runnable), runnable);
    }

    private synchronized void removeTrackingEntries(Object tag, T runnable) {
        fForwardMap.remove(tag, runnable);
        fReverseMap.remove(runnable);
    }

    @SuppressWarnings("unchecked")
    private T getTrackedSafeRunnable(Runnable r) {
        T trackedSafeRunnable = null;

        if (r instanceof TrackedFutureTask) {
            TrackedFutureTask<?> trackedFutureTask = (TrackedFutureTask<?>) r;
            trackedSafeRunnable = (T) trackedFutureTask.getRunnable();
        } else if (r instanceof TrackedSafeRunnable) {
            trackedSafeRunnable = (T) r;
        } else {
            Flog.p(Flog.ERROR, kLogTag, "Unknown runnable class: " + r.getClass().getName());
        }

        return trackedSafeRunnable;
    }
}
