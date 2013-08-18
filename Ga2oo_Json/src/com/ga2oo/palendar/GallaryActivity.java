package com.ga2oo.palendar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;
import android.view.WindowManager;

import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.common.Base64;
import com.ga2oo.palendar.common.Tools;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;

public class GallaryActivity extends Activity 
{
	private static final int SELECT_IMAGE = 1;
	private static final String SETTINGS = "Settings";
	private static final String IMAGE = "image/*";
	private static final String SELECTIMAGES = "Select Images";
	private static final String PATH = "path";
	
	private String selectedImagePath,strAscii="",strImageAsciiCode="";
	private int settingcont=0;
	/**
	 * This class is used to open the Default Gallery which contains all videos 
	 */
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);// to remove window title
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
        	int l=bundle.getInt(SETTINGS);
        	settingcont=l;
        }
        Intent intent =getIntent(); 
	    intent.setType(IMAGE);
	    intent.setAction(Intent.ACTION_GET_CONTENT);
	   
	    startActivityForResult(Intent.createChooser(intent,SELECTIMAGES), SELECT_IMAGE);
	}/**
	 * after we select the Image from gallery , we pass an intent to Message Preview class
	 */
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (resultCode == RESULT_OK) 
	    {
			if (requestCode == SELECT_IMAGE) 
	        {
				Uri selectedImageUri = data.getData();
	            selectedImagePath = getPath(selectedImageUri);
	            try
	            {
	            	if(Tools.selectedImg.equalsIgnoreCase(""))
	         	   	{
	            		Tools.selectedImg =selectedImagePath;
	            		ProfileSettings.profileSettings.ivImage.setImageBitmap(getScaledBitmap(selectedImagePath));
	            		strImageAsciiCode = imageToAscii(selectedImagePath);
//	            		ga2ooParsersUserImageUpload.uploadUserImage(AppConstants.USER_ID, strImageAsciiCode);
	            		Ga2ooJsonParsers.uploadUserImage(AppConstants.USER_ID, strImageAsciiCode);
	         	   	}
	            }
	 	   	   catch(NullPointerException npe)
	 	   	   {
	 	   	   } 
	            
	 	   	   Tools.selectedImg= selectedImagePath;
	 	   	   if(settingcont==0)
	 	   	   {
	 	   		   finishFromChild(GallaryActivity.this);
	 	   	   }
	 	   	   else
	 	   	   {
	 	   		   Intent intent=new Intent();
	 	   		   intent.putExtra(PATH,selectedImagePath )	;
	 	   		   setResult(2, intent);
	 	   		   finish();
	 	   	   }
	        }  
	    }
		else
	    {
			GallaryActivity.this.finish();
	    }	
	}

	public String getPath(Uri uri) 
	{
		String[] projection = { MediaStore.Images.Media.DATA };
	    Cursor cursor = managedQuery(uri, projection, null, null, null);
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
	
	public Bitmap getScaledBitmap(String selectedImagePath)
	{
		Bitmap desBmp = null;		
		BitmapFactory.Options options2 = new BitmapFactory.Options();
	       
        options2.inPreferredConfig = Bitmap.Config.RGB_565; 
        
        Bitmap srcBmp = BitmapFactory.decodeFile(selectedImagePath, options2);
        
		int height =  srcBmp.getHeight(), width = srcBmp.getWidth();
    	
		float HEIGHT = 80, WIDTH = 80;
		
		float scale = 0f;
    	
	    if(HEIGHT / height > WIDTH / width)
	    	scale = WIDTH / width;
	    else
       		scale = HEIGHT / height;
       
       height = (int)(scale * height);
       width = (int)(scale * width);
        
//   	   try
//   	   {
   		   desBmp = Bitmap.createScaledBitmap(srcBmp, width, height, true);
   		   srcBmp.recycle();
//   	   }
//   	   catch(NullPointerException npe)
//   	   {
//   	   } 
   	   return desBmp;
	}
	
	public String imageToAscii(String imagePath)
	{
		File myImageFile = new File(imagePath);
		FileInputStream fis;
		try
		{
			fis = new FileInputStream(myImageFile);
			byte[] data = new byte[1024];
			byte[] tmp = new byte[0];
			byte[] myArrayImage = new byte[0];
			int len = 0 ;
			int total = 0;
			while( (len = fis.read(data)) != -1 )
			{
				total += len;
				tmp = myArrayImage;
				myArrayImage = new byte[total];
				System.arraycopy(tmp,0,myArrayImage,0,tmp.length);
				System.arraycopy(data,0,myArrayImage,tmp.length,len);
				strAscii= Base64.encodeBytes(myArrayImage);
			}
			fis.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		  //new String(myArrayImage, "US-ASCII");
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
		   e.printStackTrace();
		}
		return strAscii;
	}
}
