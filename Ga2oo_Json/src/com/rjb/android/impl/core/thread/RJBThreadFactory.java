package com.rjb.android.impl.core.thread;

import java.util.concurrent.ThreadFactory;

public class RJBThreadFactory implements ThreadFactory {
    private final ThreadGroup fGroup;
    private final int fPriority;

    public RJBThreadFactory(String name) {
        this(name, Thread.NORM_PRIORITY);
    }
    
    public RJBThreadFactory(String name, int priority) {
        fGroup = new ThreadGroup(name);
        fPriority = priority;
    }

    public Thread newThread(Runnable r) {
        Thread thread = new Thread(fGroup, r);
        thread.setName(fGroup.getName() + ":" + thread.getId());
        thread.setPriority(fPriority);
                
        return thread;
    }
}
