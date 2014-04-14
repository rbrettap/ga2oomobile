package com.rjb.android.impl.core.util;

import java.io.PrintStream;
import java.io.PrintWriter;

public abstract class TrackedSafeRunnable extends SafeRunnable {
    private int fPriority;

    public TrackedSafeRunnable()
    {
        super();
    }
    
    public TrackedSafeRunnable(PrintStream stream)
    {
        super(stream);
    }
    
    public TrackedSafeRunnable(PrintWriter writer)
    {
        super(writer);
    }

    public int getPriority() {
        return fPriority;
    }

    public void setPriority(int priority) {
        fPriority = priority;
    }

    public void executionStarted() {
    }
    
    public void executionFinished() {
    }
    
    public void executionCancelled() {
    }
    
    public void executionRejected() {
    }
}
