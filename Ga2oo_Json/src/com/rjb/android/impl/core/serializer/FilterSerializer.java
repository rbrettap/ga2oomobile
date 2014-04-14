
package com.rjb.android.impl.core.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Filter serializer.
 * 
 * @author rbrett
 */
public class FilterSerializer<ObjectType> implements Serializer<ObjectType> {
    protected final Serializer<ObjectType> fSerializer;

    public FilterSerializer(Serializer<ObjectType> serializer) {
        fSerializer = serializer;
    }

    @Override
    public void serialize(OutputStream os, ObjectType object) throws IOException {
        if (fSerializer != null && os != null && object != null) {
            fSerializer.serialize(os, object);
        }
    }

    @Override
    public ObjectType deserialize(InputStream is) throws IOException {
        if (fSerializer != null && is != null) {
            return fSerializer.deserialize(is);
        }

        return null;
    }
}
