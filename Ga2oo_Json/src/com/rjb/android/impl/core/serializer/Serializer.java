
package com.rjb.android.impl.core.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serializer Interface to describe a generic serializer/deserializer. Unlike java.io.Serializable,
 * we control the output format. Serializers MUST be thread-safe.
 * 
 * @author rbrett
 */
public interface Serializer<ObjectType> {
    public void serialize(OutputStream os, ObjectType object) throws IOException;

    public ObjectType deserialize(InputStream is) throws IOException;
}
