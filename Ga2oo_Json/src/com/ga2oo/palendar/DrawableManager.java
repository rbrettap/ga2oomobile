
package com.ga2oo.palendar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.ga2oo.palendar.common.ImageHelper;
import com.ga2oo.palendar.databaseaccess.DatabaseHelper;

public class DrawableManager 
{
		private static final String LOGTAG = "DrawableManager";
		private static final String PNG = ".png";
		private final Map<String, Drawable> drawableMap;
		private static final List<String> imagesOnSD = new ArrayList<String>();
	    private int width, height;
	    private boolean fetchResult = true, fetchButtonResult = true;
	    
	    public DrawableManager() 
	    {
	    	drawableMap = new HashMap<String, Drawable>();
	    }
	    
	    public Drawable fetchDrawable(String urlString) throws Exception 
	    {
	    	if (drawableMap.containsKey(urlString)) 
	    	{
	    		return (Drawable) drawableMap.get(urlString);
	    	}else{

	        		Drawable drawable = downloadImage(urlString, width, height);
	        		drawableMap.put(urlString, drawable);
	        		return drawable;
	    	}
	    	}

	    public Drawable fetchDrawable(String urlString, final String imageId, String isImageUdated) throws Exception 
	    {
	    	Log.v(LOGTAG, "IMEGE ID ="+imageId);
	    	Drawable drawable;
	    	if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
		    	
		    	File imageFile = new File(Environment.getExternalStorageDirectory()+DatabaseHelper.FOLDER_NAME, imageId + PNG);
		    	if(imageFile.exists() & "false".equals(isImageUdated)){
	    	         Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile));
	    	         drawable = new BitmapDrawable(bitmap);
		    	}else{
		    			OutputStream myOutput;
			    		InputStream myInput;
		    			URL urlDownload = new URL(urlString);
						HttpURLConnection conDownload= (HttpURLConnection) urlDownload.openConnection();
						myInput = conDownload.getInputStream();			
		        		File path = new File(Environment.getExternalStorageDirectory()+DatabaseHelper.FOLDER_NAME);
		        		path.mkdir();
		        		File file = new File(path,imageId+PNG);
		            	myOutput = new FileOutputStream(file);
		            	byte[] buffer = new byte[2048];
		    	    	int length;
		    	    	while ((length = myInput.read(buffer))>0)
		    	    	{
		    	    		myOutput.write(buffer, 0, length);
		    	    	}
		    	    	myOutput.flush();
		    	    	myOutput.close();
		    	    	myInput.close();
		    	    	
		    	    	 Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(imageFile));
		    	         drawable = new BitmapDrawable(bitmap);
		    	         
		    	}
	    	}else{ 
       		 drawable = downloadImage(urlString, width, height);
       		drawableMap.put(urlString, drawable);
       	}
	    	return drawable;
	    }

	    public boolean fetchDrawableOnThread(final String urlString, final ImageView imageView, int width, int height) 
	    {
	    	fetchResult=true;
	    	this.width = width;
	    	this.height = height;
	    	
	    	if (drawableMap.containsKey(urlString)) 
	    	{
	    		imageView.setImageDrawable((Drawable) drawableMap.get(urlString));
	    	}

	    	final Handler handler = new Handler()
	    	{
	    		public void handleMessage(Message message)
	    		{
	    			imageView.setImageDrawable((Drawable) message.obj);
	    		}
	    	};

	    	Thread thread = new Thread() 
	    	{
	    		@Override
	    		public void run() 
	    		{
	    			Drawable drawable;
					try {
						drawable = fetchDrawable(urlString);
						Message message = handler.obtainMessage(1, drawable);
		    			handler.sendMessage(message);
					} catch (Exception e) {
						e.printStackTrace();
						fetchResult = false;
					}
	    			
	    		}
	    	};
	    	thread.start();
	    	return fetchResult;
	    }
	    
	    public boolean fetchDrawableOnThread(final String urlString, final ImageView imageView, int width, int height, final String imageID, final String isImageUpdated) 
	    {
		    	fetchResult=true;
		    	this.width = width;
		    	this.height = height;
		    	final Handler handler = new Handler()
		    	{
		    		public void handleMessage(Message message)
		    		{
		    			imageView.setImageDrawable((Drawable) message.obj);
		    		}
		    	};
	
		    	Thread thread = new Thread() 
		    	{
		    		@Override
		    		public void run() 
		    		{
		    			Drawable drawable;
						try {
							drawable = fetchDrawable(urlString,imageID,isImageUpdated);
							Message message = handler.obtainMessage(1, drawable);
			    			handler.sendMessage(message);
						} catch (Exception e) {
							e.printStackTrace();
							fetchResult = false;
						}
		    			
		    		}
		    	};
		    	thread.start();
		    	return fetchResult;
	    }

	    public boolean fetchDrawableOnThreadButton(final String urlString, final Button btn, int width, int height) 
	    {
	    	fetchButtonResult = true;
	    	this.width = width;
	    	this.height = height;
	    	
	    	if (drawableMap.containsKey(urlString)) 
	    	{
	    		btn.setBackgroundDrawable((Drawable) drawableMap.get(urlString));
	    	}

	    	final Handler handler = new Handler()
	    	{
	    		public void handleMessage(Message message)
	    		{
	    			btn.setBackgroundDrawable((Drawable) drawableMap.get(urlString));
	    		}
	    	};

	    	Thread thread = new Thread() 
	    	{
	    		@Override
	    		public void run() 
	    		{
	    			Drawable drawable;
					try {
						drawable = fetchDrawable(urlString);
						Message message = handler.obtainMessage(1, drawable);
		    			handler.sendMessage(message);
					} catch (Exception e) {
						e.printStackTrace();
						fetchButtonResult = false;
					}
	    			
	    		}
	    	};
	    	thread.start();
	    	return fetchButtonResult;
	    }
//	    private InputStream fetch(String urlString) throws MalformedURLException, IOException 
//	    {
//	    	DefaultHttpClient httpClient = new DefaultHttpClient();
//	    	HttpGet request = new HttpGet(urlString);
//	    	HttpResponse response = httpClient.execute(request);
//	    	return response.getEntity().getContent();
//	    }
	    
	    public BitmapDrawable downloadImage(String imgUrl, float WIDTH, float HEIGHT) throws Exception
		{
			BitmapDrawable Img = null;
				System.gc();
		    	
				URL urlDownload = new URL(imgUrl);
	            Log.e("ImageUrl", imgUrl);
				HttpURLConnection conDownload= (HttpURLConnection) urlDownload.openConnection();
	            InputStream sstrmDownload = conDownload.getInputStream();			
				
				Bitmap bmp = BitmapFactory.decodeStream(sstrmDownload);
				
				int height = (int) HEIGHT ,width = (int) WIDTH;
				float scale = 0f;
		    	
			    if(HEIGHT / height > WIDTH / width)
			    	scale = WIDTH / width;
			    else
		       		scale = HEIGHT / height;
				width = (int) (width * scale);
				height = (int) (height * scale);	       
		       
		       Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, width, height, true);
		   	   bmp.recycle();
		   	   Img = new BitmapDrawable(ImageHelper.getRoundedCornerBitmap(bmp2, 6));
			return Img;
		}
	    public void clear()
	    {
	    	if(drawableMap != null)
	    	{
	    		Set<String> keys = drawableMap.keySet();
	    		for(String key : keys)
	    		{
	    			BitmapDrawable drawable = (BitmapDrawable) drawableMap.get(key);
	    		    if(drawable != null)
	    			   drawable.getBitmap().recycle();
	    		    System.gc();
	    		}
	    		drawableMap.clear();
	    	}
	    }

}
