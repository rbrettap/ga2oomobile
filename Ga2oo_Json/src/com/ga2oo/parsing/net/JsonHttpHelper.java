package com.ga2oo.parsing.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class JsonHttpHelper {

	public static final String LOGNAME = "JsonHttpHelper";
	
	private static JsonHttpHelper instance = null;
	private HttpClient httpclient;
	
	public JsonHttpHelper(){
		httpclient = new DefaultHttpClient();
	}
	
	public static synchronized JsonHttpHelper getInstance() {
	       
		if (instance == null) {
            instance = new JsonHttpHelper();
        }
        return instance;
    }
	
	//TODO
	public JsonElement sendPostRequest(String url,List<NameValuePair> params) throws ClientProtocolException, IOException{
		JsonElement element = null;
	    HttpPost httppost = new HttpPost(url);
	    HttpResponse response = httpclient.execute(httppost);
		InputStream in =response.getEntity().getContent();
		element = new JsonParser().parse(new BufferedReader(new InputStreamReader(in)));
		 return element;
	}
	
	public JsonElement sendGetRequest(String url) throws ClientProtocolException, IOException{
			JsonElement element = null;
		    HttpGet httpget = new HttpGet(url);
		    HttpResponse response = httpclient.execute(httpget);
			InputStream in =response.getEntity().getContent();
			element = new JsonParser().parse(new BufferedReader(new InputStreamReader(in)));
	    return element;
	}
	
	public Object parse(JsonElement element, Class classObj){
		return new Gson().fromJson(element, classObj);
	}
	
	
}
