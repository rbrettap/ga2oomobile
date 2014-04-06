package com.rjb.android.impl.core.serializer;

import com.rjb.android.impl.core.http.HttpParams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * HttpParams serializer.
 * 
 * @author rbrett
 */
public class HttpParamsSerializer implements Serializer<HttpParams> {
    public HttpParamsSerializer() {
    }
    
    @Override
    public void serialize(OutputStream os, HttpParams object) throws IOException {
        if (os == null || object == null) {
            return;
        }
        
        object.writeFormEncodedByteStream(os);
    }

    @Override
    public HttpParams deserialize(InputStream is) throws IOException {
        if (is == null) {
            return null;
        }
        
        HttpParams params = new HttpParams();
        params.readFormEncodedByteStream(is);
        
        return params;
    }
}
