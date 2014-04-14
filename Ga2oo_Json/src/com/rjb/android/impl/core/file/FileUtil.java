package com.rjb.android.impl.core.file;

import android.content.Context;
import android.os.Environment;

import com.rjb.android.impl.core.RJBCore;
import com.rjb.android.impl.core.log.Flog;
import com.rjb.android.impl.core.util.GeneralUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public final class FileUtil {
    private static String kLogTag = FileUtil.class.getSimpleName();

    public static File getApplicationFilesDir(boolean allowExternal) {
        final Context context = RJBCore.getInstance().getApplicationContext();

        if (allowExternal && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return context.getExternalFilesDir(null);
        } else {
            return context.getFilesDir();
        }
    }

    public static File getApplicationCacheDir(boolean allowExternal) {
        final Context context = RJBCore.getInstance().getApplicationContext();

        if (allowExternal && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return context.getExternalCacheDir();
        } else {
            return context.getCacheDir();
        }
    }

    /**
     * Creates the directory for storing Flurry files.
     * 
     * @return true if the directory is created or already exists, false if it cannot be created.
     */
    public static boolean createParentDir(File file) {
        if (file == null) {
            return false;
        }

        File absoluteFile = file.getAbsoluteFile();
        if (absoluteFile == null) {
            return false;
        }
        
        File parentDir = file.getParentFile();
        if (parentDir == null) {
            // nothing to do
            return true;
        }
        
        boolean success = parentDir.mkdirs();
        if (!success && !parentDir.isDirectory()) {
            Flog.p(Flog.ERROR, kLogTag, "Unable to create persistent dir: " + parentDir);
            return false;
        }
        
        return true;
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            // delete all files in the directory first
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    @Deprecated 
    public static String readFileToString(File file) {
        if (file == null || !file.exists()) {
            Flog.p(Flog.INFO, kLogTag, "Persistent file doesn't exist.");
            return null;
        }
        
        Flog.p(Flog.INFO, kLogTag, "Loading persistent data: " + file.getAbsolutePath());

        FileInputStream in = null;
        StringBuilder str = null;

        try {
            in = new FileInputStream(file);
            int read = 0;
            str = new StringBuilder();
            byte[] buf = new byte[1024];

            while ((read = in.read(buf)) > 0) {
                str.append(new String(buf, 0, read));
            }
        } catch (Throwable t) {
            Flog.p(Flog.ERROR, kLogTag, "Error when loading persistent file", t);
            str = null;
        } finally {
            GeneralUtil.safeClose(in);
        }

        if (str != null) {
            return str.toString();
        } else {
            return null;
        }
    }
    
    @Deprecated 
    public static void writeStringToFile(File file, String text) {
        if (file == null) {
            Flog.p(Flog.INFO, kLogTag, "No persistent file specified.");
            return;
        }
        
        if (text == null) {
            Flog.p(Flog.INFO, kLogTag, "No data specified; deleting persistent file: " + file.getAbsolutePath());
            file.delete();
            return;
        }
        
        Flog.p(Flog.INFO, kLogTag, "Writing persistent data: " + file.getAbsolutePath());
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(text.getBytes());
        } catch (Throwable t) {
            Flog.p(Flog.ERROR, kLogTag, "Error writing persistent file", t);
        } finally {
            GeneralUtil.safeClose(fos);
        }
    }

    public static String readPersistentTextFile(File file) {
        if (file == null || !file.exists()) {
            Flog.p(Flog.INFO, kLogTag, "Persistent file doesn't exist.");
            return null;
        }

        Flog.p(Flog.INFO, kLogTag, "Loading persistent data: " + file.getAbsolutePath());

        String result = null;
        FileInputStream fis = null;
        DataInputStream in = null;
        try {
            fis = new FileInputStream(file);
            in = new DataInputStream(fis);
            result = in.readUTF();
        } catch (Throwable t) {
            Flog.p(Flog.ERROR, kLogTag, "Error when loading persistent file");
        } finally {
            GeneralUtil.safeClose(in);
        }

        return result;
    }

    public static void writePersistentTextFile(File file, String text) {
        if (file == null) {
            Flog.p(Flog.INFO, kLogTag, "No persistent file specified.");
            return;
        }
        
        if (text == null) {
            Flog.p(Flog.INFO, kLogTag, "No data specified; deleting persistent file: " + file.getAbsolutePath());
            file.delete();
            return;
        }
        
        Flog.p(Flog.INFO, kLogTag, "Writing persistent data: " + file.getAbsolutePath());
        
        FileOutputStream persistentFileOutputStream = null;
        DataOutputStream out = null;
        try {
            boolean success = FileUtil.createParentDir(file);
            if (!success) {
                return;
            }

            persistentFileOutputStream = new FileOutputStream(file);
            out = new DataOutputStream(persistentFileOutputStream);
            out.writeUTF(text);
        } catch (Throwable t) {
            Flog.p(Flog.ERROR, kLogTag, "Error writing persistent file", t);
        } finally {
            GeneralUtil.safeClose(out);
        }
    }
}
