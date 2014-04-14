
package com.rjb.android.impl.core.http;

import com.rjb.android.impl.core.log.Flog;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpParams;

/**
 * Utility functions for HttpClient.
 * 
 * @author rbrett
 */
public final class HttpClientUtil {
    private static SchemeRegistry sSchemeRegistry;
    
    private static synchronized SchemeRegistry getSchemeRegistry() {
        if (sSchemeRegistry != null) {
            return sSchemeRegistry;
        }
        
        sSchemeRegistry = new SchemeRegistry();
        sSchemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        // relax SSL certificate restrictions for internal builds
        if (Flog.getInternalLogging()) {
            sSchemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 443));
        } else {
            sSchemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        }
        
        return sSchemeRegistry;
    }
    
    public static HttpClient getNewHttpClient(HttpParams httpParameters) {
        return new DefaultHttpClient(httpParameters);
    }

    public static HttpClient getNewSSLEnabledHttpClient(HttpParams httpParameters) {
        ClientConnectionManager ccm = new SingleClientConnManager(httpParameters, getSchemeRegistry());
        return new DefaultHttpClient(ccm, httpParameters);
    }
}
