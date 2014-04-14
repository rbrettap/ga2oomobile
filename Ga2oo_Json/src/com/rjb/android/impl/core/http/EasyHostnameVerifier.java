
package com.rjb.android.impl.core.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class EasyHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}
