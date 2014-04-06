
package com.rjb.android.impl.core.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import android.annotation.TargetApi;
import android.os.Build;

import com.rjb.android.impl.core.collections.ArrayListMultimap;
import com.rjb.android.impl.core.log.Flog;
import com.rjb.android.impl.core.net.NetworkStateProvider;
import com.rjb.android.impl.core.util.GeneralUtil;
import com.rjb.android.impl.core.util.TrackedSafeRunnable;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * Http stream request encapsulation.
 * 
 * @author rbrett
 */
public class HttpStreamRequest extends TrackedSafeRunnable {
    private static final String kLogTag = HttpStreamRequest.class.getSimpleName();

    // common properties
    public static final String kPropertyAccept = "Accept";
    public static final String kPropertyAcceptEncoding = "Accept-Encoding";
    public static final String kPropertyAuthorization = "Authorization";
    public static final String kPropertyContentLength = "Content-Length";
    public static final String kPropertyContentType = "Content-Type";
    public static final String kPropertyLocation = "Location";
    public static final String kPropertyRange = "Range";

    public interface StreamListener {
        public void requestData(HttpStreamRequest request, OutputStream os) throws Exception;

        public void responseData(HttpStreamRequest request, InputStream is) throws Exception;

        public void completed(HttpStreamRequest request);
    }

    public static class SimpleStreamListener implements StreamListener {
        @Override
        public void requestData(HttpStreamRequest request, OutputStream os) throws Exception {
        }

        @Override
        public void responseData(HttpStreamRequest request, InputStream is) throws Exception {
        }

        @Override
        public void completed(HttpStreamRequest request) {
        }
    }

    private static synchronized SSLContext getSslContext() {
        if (sSslContext != null) {
            return sSslContext;
        }

        try {
            // create a trust manager that accepts self-signed certificates
            TrustManager[] trustAllCerts = new TrustManager[] {
                new EasyX509TrustManager(null)
            };

            // create an SSL context using our trust manager
            sSslContext = SSLContext.getInstance("TLS");
            sSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Exception e) {
            Flog.p(Flog.DEBUG, kLogTag, "Exception creating SSL context", e);
        }

        return sSslContext;
    }

    private static synchronized HostnameVerifier getHostnameVerifier() {
        if (sHostnameVerifier != null) {
            return sHostnameVerifier;
        }

        sHostnameVerifier = new EasyHostnameVerifier();
        return sHostnameVerifier;
    }

    public enum RequestMethod {
        kUnknown,
        kGet,
        kPost,
        kPut,
        kDelete,
        kHead;

        public String toString() {
            switch (this) {
                case kPost:
                    return "POST";

                case kPut:
                    return "PUT";

                case kDelete:
                    return "DELETE";

                case kHead:
                    return "HEAD";

                case kGet:
                default:
                    return "GET";
            }
        }

        public HttpRequestBase toHttpClientRequest(String url) {
            // set up the request
            switch (this) {
                case kPost:
                    return new HttpPost(url);

                case kPut:
                    return new HttpPut(url);

                case kDelete:
                    return new HttpDelete(url);

                case kHead:
                    return new HttpHead(url);

                case kGet:
                default:
                    return new HttpGet(url);
            }
        }
    }

    private static SSLContext sSslContext;
    private static HostnameVerifier sHostnameVerifier;

    private static final int kConnectTimeoutMSDefault = 10000;
    private static final int kReadTimeoutMSDefault = 15000;

    private String fUrl;
    private RequestMethod fRequestMethod = RequestMethod.kUnknown;
    private int fConnectTimeoutMS = kConnectTimeoutMSDefault;
    private int fReadTimeoutMS = kReadTimeoutMSDefault;
    private boolean fAllowRedirect = true;
    private final ArrayListMultimap<String, String> fRequestProperties = new ArrayListMultimap<String, String>();
    private StreamListener fStreamListener;

    private HttpURLConnection fUrlConnection;
    private HttpClient fHttpClient;
    private boolean fDisconnected;
    private boolean fCancelled;

    private Exception fException;
    private int fResponseCode = -1;
    private final ArrayListMultimap<String, String> fResponseProperties = new ArrayListMultimap<String, String>();

    private final Object fCancelLock = new Object();

    public HttpStreamRequest() {
        super();
    }

    public void setUrl(String url) {
        fUrl = url;
    }

    public String getUrl() {
        return fUrl;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        fRequestMethod = requestMethod;
    }
    
    public RequestMethod getRequestMethod() {
        return fRequestMethod;
    }

    public void setConnectTimeoutMS(int timeoutMS) {
        fConnectTimeoutMS = timeoutMS;
    }

    public void setReadTimeoutMS(int timeoutMS) {
        fReadTimeoutMS = timeoutMS;
    }

    public void setAllowRedirect(boolean allowRedirect) { fAllowRedirect = allowRedirect; }

    public void addRequestParameter(String key, String value) {
        fRequestProperties.put(key, value);
    }
    
    public void setStreamListener(StreamListener streamListener) {
        fStreamListener = streamListener;
    }

    public boolean isCancelled() {
        synchronized(fCancelLock) {
            return fCancelled;
        }
    }

    public boolean getSuccess() {
        return !getStreamError() && getResponseCodeSuccess();
    }

    public boolean getResponseCodeSuccess() {
        return fResponseCode >= 200 && fResponseCode < 400;
    }

    public int getResponseCode() {
        return fResponseCode;
    }

    public boolean getStreamError() {
        return fException != null;
    }

    public Exception getStreamException() {
        return fException;
    }

    public ArrayListMultimap<String, String> getResponseProperties() {
        return fResponseProperties;
    }

    public List<String> getResponseProperty(String property) {
        if (property == null) {
            return null;
        }

       return fResponseProperties.get(property);
    }

    public void cancel() {
        synchronized(fCancelLock) {
            fCancelled = true;
        }

        disconnectFromCancel();
    }

    @Override
    public void safeRun() {
        try {
            if (fUrl == null) {
                return;
            }
            
            if (!NetworkStateProvider.getInstance().isNetworkEnabled()) {
                Flog.p(Flog.DEBUG, kLogTag, "Network not available, aborting http request: " + fUrl);
                return;
            }
            
            // Google suggests to use HttpUrlRequest for >= Gingerbread
            // see http://android-developers.blogspot.com/2011/09/androids-http-clients.html
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                executeHttpUrlRequest();
            } else {
                executeHttpClientRequest();
            }

            Flog.p(Flog.INFO, kLogTag, "HTTP status: " + fResponseCode + " for url: " + fUrl);
        } catch (Exception e) {
            Flog.p(Flog.INFO, kLogTag, "HTTP status: " + fResponseCode + " for url: " + fUrl);
            Flog.p(Flog.DEBUG, kLogTag, "Exception during http request: " + fUrl, e);
            fException = e;
        } finally {
            // notify
            notifyComplete();
        }
    }

    @Override
    public void executionCancelled() {
        cancel();
    }

    private void executeHttpUrlRequest() throws Exception {
        // don't care about synchronization here
        if (fCancelled) {
            return;
        }
        
        URL url = new URL(fUrl);

        try {
            // set up the connection
            fUrlConnection = (HttpURLConnection) url.openConnection();
            fUrlConnection.setConnectTimeout(fConnectTimeoutMS);
            fUrlConnection.setReadTimeout(fReadTimeoutMS);
            fUrlConnection.setRequestMethod(fRequestMethod.toString());
            fUrlConnection.setInstanceFollowRedirects(fAllowRedirect);
            fUrlConnection.setDoOutput(RequestMethod.kPost.equals(fRequestMethod));
            fUrlConnection.setDoInput(true);

            // relax SSL certificate restrictions for internal builds
            if (Flog.getInternalLogging() && fUrlConnection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConnection = (HttpsURLConnection) fUrlConnection;

                // always verify the host - don't check for certificate
                // trust every server - don't check for any certificate
                httpsConnection.setHostnameVerifier(getHostnameVerifier());
                httpsConnection.setSSLSocketFactory(getSslContext().getSocketFactory());
            }

            // set up the request properties
            Collection<Map.Entry<String, String>> requestProperties = fRequestProperties.entries();
            for (Map.Entry<String, String> entry : requestProperties) {
                fUrlConnection.addRequestProperty(entry.getKey(), entry.getValue());
            }

            // don't try to use a gzip stream for requests with no response body (default is to use gzip stream)
            if (!(RequestMethod.kGet.equals(fRequestMethod) || RequestMethod.kPost.equals(fRequestMethod))) {
                fUrlConnection.setRequestProperty(kPropertyAcceptEncoding, "");
            }

            // don't care about synchronization here
            if (fCancelled) {
                return;
            }

            // set up the request data stream
            if (RequestMethod.kPost.equals(fRequestMethod)) {
                OutputStream os = null;
                BufferedOutputStream bos = null;
                try {

                    // streams are unbuffered
                    os = fUrlConnection.getOutputStream();
                    bos = new BufferedOutputStream(os);

                    // notify the listener
                    notifyRequestData(bos);

                } finally {
                    GeneralUtil.safeClose(bos);
                    GeneralUtil.safeClose(os);
                }
            }

            // get the response code
            fResponseCode = fUrlConnection.getResponseCode();

            // get the response headers
            Map<String, List<String>> headerFields = fUrlConnection.getHeaderFields();
            Collection<Map.Entry<String, List<String>>> headerFieldsEntries = headerFields.entrySet();
            for (Map.Entry<String, List<String>> headerFieldsEntry : headerFieldsEntries) {
                for (String value : headerFieldsEntry.getValue()) {
                    fResponseProperties.put(headerFieldsEntry.getKey(), value);
                }
            }

            // if this is not a get or post request, we're done
            if (!(RequestMethod.kGet.equals(fRequestMethod) || RequestMethod.kPost.equals(fRequestMethod))) {
                return;
            }

            // don't care about synchronization here
            if (fCancelled) {
                return;
            }

            // set up the response stream
            InputStream is = null;
            BufferedInputStream bis = null;
            try {
                // streams are unbuffered
                is = fUrlConnection.getInputStream();
                bis = new BufferedInputStream(is);

                // notify the listener
                notifyResponseData(bis);
            } finally {
                GeneralUtil.safeClose(bis);
                GeneralUtil.safeClose(is);
            }
        } finally {
            disconnect();
        }
    }

    private void executeHttpClientRequest() throws Exception {
        // don't care about synchronization here
        if (fCancelled) {
            return;
        }
        
        // set up the request
        HttpRequestBase request = fRequestMethod.toHttpClientRequest(fUrl);

        // set up the request properties
        Collection<Map.Entry<String, String>> entrySet = fRequestProperties.entries();
        for (Map.Entry<String, String> entry : entrySet) {
            request.setHeader(entry.getKey(), entry.getValue());
        }

        // don't use any encodings if no data is to be returned
        if (!(RequestMethod.kGet.equals(fRequestMethod) || RequestMethod.kPost.equals(fRequestMethod))) {
            request.removeHeaders(kPropertyAcceptEncoding);
        }

        // set up the request data stream
        if (RequestMethod.kPost.equals(fRequestMethod)) {
            ((HttpPost) request).setEntity(new AbstractHttpEntity() {
                public boolean isRepeatable() {
                    return false;
                }

                public long getContentLength() {
                    return -1;
                }

                public boolean isStreaming() {
                    return false;
                }

                public InputStream getContent() throws IOException {
                    throw new UnsupportedOperationException();
                }

                @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                public void writeTo(final OutputStream outstream) throws IOException {
                    // set up the request data stream
                    BufferedOutputStream bos = null;
                    try {
                        bos = new BufferedOutputStream(outstream);

                        // notify the listener
                        notifyRequestData(bos);
                    } catch (IOException e) {
                        throw e;
                    } catch (Exception e) { 
                        // this is ridiculous but necessary to support < Gingerbread, apparently
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                            throw new IOException(e);
                        } else {
                            throw new IOException(e.toString());
                        }
                    } finally {
                        GeneralUtil.safeClose(bos);
                    }
                }
            });
        }

        try {
            // set up the request parameters
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, fConnectTimeoutMS);
            HttpConnectionParams.setSoTimeout(httpParameters, fReadTimeoutMS);
            httpParameters.setParameter("http.protocol.handle-redirects", fAllowRedirect);

            // send the request
            fHttpClient = HttpClientUtil.getNewSSLEnabledHttpClient(httpParameters);
            HttpResponse response = fHttpClient.execute(request);

            // don't care about synchronization here
            if (fCancelled) {
                throw new Exception("Request cancelled");
            }

            // handle the response
            if (response != null) {
                // get the response code
                fResponseCode = response.getStatusLine().getStatusCode();

                // get the response headers
                Header[] responseHeaders = response.getAllHeaders();
                if (responseHeaders != null) {
                    for (Header header : responseHeaders) {
                        for (HeaderElement element : header.getElements()) {
                            fResponseProperties.put(element.getName(), element.getValue());
                        }
                    }
                }

                // if this is not a get or post request, we're done
                if (!(RequestMethod.kGet.equals(fRequestMethod) || RequestMethod.kPost.equals(fRequestMethod))) {
                    return;
                }

                // don't care about synchronization here
                if (fCancelled) {
                    throw new Exception("Request cancelled");
                }

                // set up the response stream
                HttpEntity entity = response.getEntity();
                if (entity != null) {

                    InputStream is = null;
                    BufferedInputStream bis = null;
                    try {
                        // streams are unbuffered
                        is = entity.getContent();
                        bis = new BufferedInputStream(is);

                        // notify the listener
                        notifyResponseData(bis);
                    } finally {
                        GeneralUtil.safeClose(bis);
                        GeneralUtil.safeClose(is);
                    }
                }
            }
        } finally {
            disconnect();
        }
    }

    private void notifyRequestData(OutputStream os) throws Exception {
        if (fStreamListener == null) {
            return;
        }

        if (isCancelled()) {
            return;
        }

        if (os == null) {
            return;
        }

        fStreamListener.requestData(this, os);
    }

    private void notifyResponseData(InputStream is) throws Exception {
        if (fStreamListener == null) {
            return;
        }

        if (isCancelled()) {
            return;
        }

        if (is == null) {
            return;
        }

        fStreamListener.responseData(this, is);
    }

    private void notifyComplete() {
        if (fStreamListener == null) {
            return;
        }

        if (isCancelled()) {
            return;
        }

        fStreamListener.completed(this);
    }

    private void disconnect() {
        if (fDisconnected) {
            return;
        }

        fDisconnected = true;

        if (fUrlConnection != null) {
            fUrlConnection.disconnect();
        }

        if (fHttpClient != null) {
            fHttpClient.getConnectionManager().shutdown();
        }
    }

    private void disconnectFromCancel() {
        if (fDisconnected) {
            return;
        }

        fDisconnected = true;

        // cancel may be called from any thread; avoid NetworkOnMainThreadException or any other strange behavior
        if (fUrlConnection != null || fHttpClient != null) {
            Thread thread = new Thread() {
                public void run() {
                    if (fUrlConnection != null) {
                        fUrlConnection.disconnect();
                    }

                    if (fHttpClient != null) {
                        fHttpClient.getConnectionManager().shutdown();
                    }
                }
            };
            thread.start();
        }
    }
}
