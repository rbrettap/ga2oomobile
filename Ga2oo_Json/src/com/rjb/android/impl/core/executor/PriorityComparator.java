package com.rjb.android.impl.core.executor;

import com.rjb.android.impl.core.log.Flog;
import com.rjb.android.impl.core.util.TrackedSafeRunnable;

import java.util.Comparator;

/**
 * PriorityComparator
 * <p/>
 * Created by rbrett on 11/13/13.
 */
public class PriorityComparator implements Comparator<Runnable> {
    private static final String kLogTag = PriorityComparator.class.getSimpleName();

    public PriorityComparator() {
    }

    @Override
    public int compare(Runnable runnable1, Runnable runnable2) {
        int priority1 = getPriority(runnable1);
        int priority2 = getPriority(runnable2);

        if (priority1 < priority2) {
            return -1;
        } else if (priority1 > priority2) {
            return 1;
        } else {
            return 0;
        }
    }

    private int getPriority(Runnable runnable) {
        int priority = Integer.MAX_VALUE;

        if (runnable != null) {
            if (runnable instanceof TrackedFutureTask) {
                TrackedFutureTask<?> trackedFutureTask = (TrackedFutureTask<?>) runnable;
                TrackedSafeRunnable trackedSafeRunnable = (TrackedSafeRunnable) trackedFutureTask.getRunnable();
                if (trackedSafeRunnable != null) {
                    priority = trackedSafeRunnable.getPriority();
                }
            } else if (runnable instanceof TrackedSafeRunnable) {
                priority = ((TrackedSafeRunnable) runnable).getPriority();
            } else {
                Flog.p(Flog.ERROR, kLogTag, "Unknown runnable class: " + runnable.getClass().getName());
            }
        }

        return priority;
    }
}
