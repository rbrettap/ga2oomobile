
package com.rjb.android.impl.core.serializer;

import com.rjb.android.impl.core.util.GeneralUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZip serializer.
 * 
 * @author rbrett
 */
public class GZipSerializer<ObjectType> extends FilterSerializer<ObjectType> {
    public GZipSerializer(Serializer<ObjectType> serializer) {
        super(serializer);
    }

    @Override
    public void serialize(OutputStream os, ObjectType object) throws IOException {
        if (os != null) {
            GZIPOutputStream gzipOutputStream = null;

            try {
                gzipOutputStream = new GZIPOutputStream(os);
                super.serialize(gzipOutputStream, object);
            } finally {
                GeneralUtil.safeClose(gzipOutputStream);
            }
        }
    }

    @Override
    public ObjectType deserialize(InputStream is) throws IOException {
        if (is != null) {
            GZIPInputStream gzipInputStream = null;

            try {
                gzipInputStream = new GZIPInputStream(is);
                return super.deserialize(gzipInputStream);
            } finally {
                GeneralUtil.safeClose(gzipInputStream);
            }
        }

        return null;
    }
}
