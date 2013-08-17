package com.rjb.android.impl.core.util;

import com.rjb.android.impl.core.log.Flog;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * For use with various Android multithreading APIs: Handler.post*, View.post*, Activity.runOnUiThread, etc.
 * Swallows top-level exceptions to prevent SDK from crashing developers' apps.
 * 
 * @author rbrett
 * @param <T>
 *
 */
public abstract class SafeFutureTask<V> implements Callable<V>
{
    private static final String kLogTag = SafeFutureTask.class.getSimpleName();
    
    PrintStream fStream; // allow subclasses to stop stack traces from going to System.err
    PrintWriter fWriter;
    
    private final FutureTask<V> futureTask;

    public SafeFutureTask(PrintStream stream)
    {
        fStream = stream;
        futureTask = new FutureTask<V>(this);
    }
    
    public SafeFutureTask() {
        
        futureTask = new FutureTask<V>(this);
    }

    /**
     * Subclasses should override safeCall instead of Callable.call
     */
    protected abstract V safeCall();
    
    @Override
    public V call() throws Exception {
        
        try
        {
            return safeCall();
            
        }
        catch(Throwable t)
        {
            if(fStream != null)
            {
                t.printStackTrace(fStream);
            }
            else if(fWriter != null)
            {
                t.printStackTrace(fWriter);
            }
            else
            {
                t.printStackTrace();
            }
            Flog.p(Flog.ERROR, kLogTag, "", t);
        }
        return null;
        
        
    }

    public FutureTask<V> getFutureTask() {
        return futureTask;
    }
}
