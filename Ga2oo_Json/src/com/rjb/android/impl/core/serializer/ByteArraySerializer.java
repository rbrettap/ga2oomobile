
package com.rjb.android.impl.core.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.rjb.android.impl.core.util.GeneralUtil;

/**
 * Byte array serializer.
 * 
 * @author rbrett
 */
public class ByteArraySerializer implements Serializer<byte[]> {
    public ByteArraySerializer() {
    }

    @Override
    public void serialize(OutputStream os, byte[] object) throws IOException {
        if (os == null || object == null) {
            return;
        }

        os.write(object, 0, object.length);
    }

    @Override
    public byte[] deserialize(InputStream is) throws IOException {
        if (is == null) {
            return null;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        GeneralUtil.copyStream(is, os);
        return os.toByteArray();
    }
}
