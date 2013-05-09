package com.winit.ga2oo.xmlparsers;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

public class UrlPost 
{
	private HttpURLConnection connection;
	private InputStream isResponse;
	public InputStream loginPost(String xmlString,URL url)
	{
		try
		{
			connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "text/xml");
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(xmlString);
			outputStream.flush();
			isResponse = (InputStream) connection.getInputStream();
		}
		catch(UnknownHostException e)
		{
			Log.e("Failure","Network Failure"+e.toString());
		}		
		catch(SocketException e)
		{
			Log.e("Failure--","Network Failure"+e.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return isResponse;
	}

	public InputStream registrationPost(String xmlString,URL url)
	{
		try
		{
			connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			String strDate=""+dateFormatLocal.parse(dateFormatGmt.format(new Date()));
			//connection.setRequestProperty("Content-Type", "application/xml");
			connection.setRequestProperty("Content-Type", "text/xml");
			connection.setRequestProperty("Date", strDate);
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(xmlString);
			outputStream.flush();
			isResponse = (InputStream) connection.getInputStream();
		}
		catch(UnknownHostException e)
		{
			Log.e("Failure","Network Failure"+e.toString());
		}		
		catch(SocketException e)
		{
			Log.e("Failure--","Network Failure"+e.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return isResponse;
	}
	
	public InputStream userDeleteRequest(String xmlString,URL url)
	{
		try
		{
			connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("DELETE");
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			String strDate=""+dateFormatLocal.parse(dateFormatGmt.format(new Date()));
			connection.setRequestProperty("Content-Type", "text/xml");
			connection.setRequestProperty("Date", strDate);
			isResponse = (InputStream) connection.getInputStream();
		}
		catch(UnknownHostException e)
		{
			Log.e("Failure","Network Failure"+e.toString());
		}		
		catch(SocketException e)
		{
			Log.e("Failure--","Network Failure"+e.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return isResponse;
	} 
	
	public InputStream friendRequest(String xmlString,URL url)
	{
		try
		{
			connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			//connection.setRequestProperty("Content-Type", "application/xml");
			connection.setRequestProperty("Content-Type", "text/xml");
			//connection.setRequestProperty("Date", "Fri, 08 Jun  2011 6:35:00 GMT");
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(xmlString);
			outputStream.flush();
			isResponse = (InputStream) connection.getInputStream();
			
		}
		catch(UnknownHostException e)
		{
			Log.e("Failure","Network Failure"+e.toString());
		}		
		catch(SocketException e)
		{
			Log.e("Failure--","Network Failure"+e.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return isResponse;
	}

	public InputStream updateProfilePost(String xmlString,URL url)
	{
		try
		{
			connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
			SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
			String strDate=""+dateFormatLocal.parse(dateFormatGmt.format(new Date()));
			//connection.setRequestProperty("Content-Type", "application/xml");
			connection.setRequestProperty("Content-Type", "text/xml");
			connection.setRequestProperty("Content-Length", Integer.toString(xmlString.length()));
			connection.setRequestProperty("Host","5.ga2ootesting.appspot.com");
			connection.setRequestProperty("Date", strDate);
			connection.setRequestProperty("User-Agent","Fiddler");
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(xmlString);
			outputStream.flush();
			isResponse = (InputStream) connection.getInputStream();
		}
		catch(UnknownHostException e)
		{
			Log.e("Failure","Network Failure"+e.toString());
		}		
		catch(SocketException e)
		{
			Log.e("Failure--","Network Failure"+e.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return isResponse;
	}

	public InputStream notificationResponce(String xmlString,URL url)
	{
		try
		{
			connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			//connection.setRequestProperty("Content-Type", "application/xml");
			connection.setRequestProperty("Content-Type", "text/xml");
			//connection.setRequestProperty("Date", "Fri, 08 Jun  2011 6:35:00 GMT");
			DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(xmlString);
			outputStream.flush();
			isResponse = (InputStream) connection.getInputStream();
			
		}
		catch(UnknownHostException e)
		{
			Log.e("Failure","Network Failure"+e.toString());
		}		
		catch(SocketException e)
		{
			Log.e("Failure--","Network Failure"+e.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return isResponse;
	}
}
