
package com.rjb.android.impl.core.http;

import com.rjb.android.impl.core.util.GeneralUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Http parameter encapsulation.
 * 
 * @author rbrett
 */
public class HttpParams {
    private static final String kFormEncodedContentType = "application/x-www-form-urlencoded";
    private static final String kUtf8 = "utf-8";
    
    private final HashMap<String, String> fParameters = new HashMap<String, String>();

    public HttpParams() {
    }

    public void clear() {
        fParameters.clear();
    }

    public void add(String key, String value) {
        if (key == null) {
            return;
        }
        
        fParameters.put(key, value);
    }

    public void remove(String key) {
        if (key == null) {
            return;
        }
        
        fParameters.remove(key);
    }
    
    public String get(String key) {
        if (key == null) {
            return null;
        }
        
        return fParameters.get(key);
    }

    public int size() {
        return fParameters.size();
    }
    
    public String getFormEncodedContentType() {
        return kFormEncodedContentType;
    }
       
    public String getFormEncodedString() {
        StringBuilder builder = new StringBuilder();

        Set<Map.Entry<String, String>> entrySet = fParameters.entrySet();
        if (entrySet.size() > 0) {
            for (Map.Entry<String, String> entry : entrySet) {
                builder.append(GeneralUtil.urlEncode(entry.getKey()));
                builder.append("=");
                builder.append(GeneralUtil.urlEncode(entry.getValue()));
                builder.append("&");
            }
            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }

    public byte[] getFormEncodedBytes() {
        try {
            return getFormEncodedString().getBytes(kUtf8);
        } catch (UnsupportedEncodingException e) {
            // this should never happen
            return new byte[] {};
        }
    }
    
    public void writeFormEncodedByteStream(OutputStream os) throws IOException {
        os.write(getFormEncodedBytes());
    }

    public void parseFormEncodedString(String string) {
        clear();
        
        if (string == null) {
            return;
        }
        
        String[] keyValuePairs = string.split("&");
        for(String pair : keyValuePairs) {
            String[] keyvalue = pair.split("=");
            if (keyvalue.length > 1) {
                add(GeneralUtil.urlDecode(keyvalue[0]), GeneralUtil.urlDecode(keyvalue[1]));
            } else {
                add(GeneralUtil.urlDecode(keyvalue[0]), "");
            }
        }
    }
    
    public void parseFormEncodedBytes(byte[] bytes) {
        String string = null;
        
        if (bytes != null) {
            try {
                string = new String(bytes, kUtf8);
            } catch (UnsupportedEncodingException e) {
                // this should never happen
            }
        }
        
        parseFormEncodedString(string);
    }
    
    public void readFormEncodedByteStream(InputStream is) throws IOException {
        parseFormEncodedBytes(GeneralUtil.copyBytes(is));
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof HttpParams)) {
            return false;
        }
        
        HttpParams other = (HttpParams) obj;
        if (other.size() != size()) {
            return false;
        }
        
        Set<Map.Entry<String, String>> entrySet = fParameters.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            String value = entry.getValue();
            String otherValue = other.get(entry.getKey());
            
            if (value != otherValue && !(value != null && value.equals(otherValue))) {
                return false;
            }
        }
     
        return true;
    }
}
