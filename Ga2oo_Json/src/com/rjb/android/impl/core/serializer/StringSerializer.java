
package com.rjb.android.impl.core.serializer;

import com.rjb.android.impl.core.util.GeneralUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Byte array serializer.
 * 
 * @author rbrett
 */
public class StringSerializer implements Serializer<String> {
    public StringSerializer() {
    }

    @Override
    public void serialize(OutputStream os, String object) throws IOException {
        if (os == null || object == null) {
            return;
        }

        byte[] bytes = object.getBytes("utf-8");
        os.write(bytes, 0, bytes.length);
    }

    @Override
    public String deserialize(InputStream is) throws IOException {
        if (is == null) {
            return null;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        GeneralUtil.copyStream(is, os);
        return os.toString();
    }
}
