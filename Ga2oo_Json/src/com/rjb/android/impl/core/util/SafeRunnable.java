package com.rjb.android.impl.core.util;

import com.rjb.android.impl.core.log.Flog;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * For use with various Android multithreading APIs: Handler.post*, View.post*, Activity.runOnUiThread, etc.
 * Swallows top-level exceptions to prevent SDK from crashing developers' apps.
 * 
 * @author rbrett
 *
 */
public abstract class SafeRunnable implements Runnable
{
    private static final String kLogTag = SafeRunnable.class.getSimpleName();
    
    PrintStream fStream; // allow subclasses to stop stack traces from going to System.err
    PrintWriter fWriter;

    public SafeRunnable()
    {
        super();
    }
    
    public SafeRunnable(PrintStream stream)
    {
        super();
        fStream = stream;
    }
    
    public SafeRunnable(PrintWriter writer)
    {
        super();
        fWriter = writer;
    }

    /**
     * Subclasses should override safeRun instead of Runnable.run
     */
    public abstract void safeRun();
    
    @Override
    public final void run()
    {
        try
        {
            safeRun();
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
    }
}
