
package com.rjb.android.impl.core.http;

import com.rjb.android.impl.core.serializer.Serializer;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Http request encapsulation.
 * 
 * @author rbrett
 */
public class HttpRequest<RequestObjectType, ResponseObjectType> extends HttpStreamRequest {
    public interface Listener<RequestObjectType, ResponseObjectType> {
        public void result(HttpRequest<RequestObjectType, ResponseObjectType> request, ResponseObjectType response);
    }

    public static class SimpleListener<RequestObjectType, ResponseObjectType> implements Listener<RequestObjectType, ResponseObjectType> {
        @Override
        public void result(HttpRequest<RequestObjectType, ResponseObjectType> request, ResponseObjectType response) {
        }
    }

    private Listener<RequestObjectType, ResponseObjectType> fListener;
    private RequestObjectType fRequest;
    private ResponseObjectType fResponse;
    private Serializer<RequestObjectType> fRequestSerializer;
    private Serializer<ResponseObjectType> fResponseSerializer;
    
    public HttpRequest() {
        super();
    }

    public void setRequest(RequestObjectType request) {
        fRequest = request;
    }
    
    public void setRequestSerializer(Serializer<RequestObjectType> serializer) {
        fRequestSerializer = serializer;
    }
    
    public void setResponseSerializer(Serializer<ResponseObjectType> serializer) {
        fResponseSerializer = serializer;
    }
    
    public void setListener(Listener<RequestObjectType, ResponseObjectType> listener) {
        fListener = listener;
    }
    
    public ResponseObjectType getResponse() {
        return fResponse;
    }

    @Override
    public void safeRun() {
        // set up the stream listener
        setupStreamListener();
        
        // execute the request
        super.safeRun();
    }

    private void setupStreamListener() {
        // set up the stream listener
        setStreamListener(new StreamListener() {
            @Override
            public void requestData(HttpStreamRequest request, OutputStream os) throws Exception {
                if (fRequest != null && fRequestSerializer != null) {
                    fRequestSerializer.serialize(os,  fRequest);
                }
            }

            @Override
            public void responseData(HttpStreamRequest request, InputStream is) throws Exception {
                // check for a successful request
                if (!request.getResponseCodeSuccess()) {
                    return;
                }

                if (fResponseSerializer != null) {
                    fResponse = fResponseSerializer.deserialize(is);
                }
            }

            @Override
            public void completed(HttpStreamRequest request) {
                notifyListener();
            }
        });
    }
    
    private void notifyListener() {
        if (fListener == null) {
            return;
        }

        if (isCancelled()) {
            return;
        }
        
        fListener.result(this, fResponse);
    }
}
