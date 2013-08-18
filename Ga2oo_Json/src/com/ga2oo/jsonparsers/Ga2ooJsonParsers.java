package com.ga2oo.jsonparsers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.objects.Association;
import com.ga2oo.palendar.objects.BusinessRegistration;
import com.ga2oo.palendar.objects.UserAccount;
import com.ga2oo.palendar.objects.UserLocationObject;
import com.ga2oo.palendar.xmlparsers.LocationDeleteWrapper;
import com.ga2oo.parsing.net.JsonHttpHelper;

public class Ga2ooJsonParsers {

	private static final String DELETE = "DELETE";

	private static final String POST = "POST";

	private static final String APPLICATION_JSON = "application/json";

	private static final String CONTENT_TYPE = "Content-Type";

	private static String LOGTAG = "Ga2ooJsonParsers";
	
	private static JsonHttpHelper jsonHelper = JsonHttpHelper.getInstance();
	 static int status;
	 public static String regMessage="",addFriendMessage="",updateUserProfileMgs;
	
	public static int loginStatus(String username, String password)
	{
		try {
			Log.i(LOGTAG, "Login started...");
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.Authenticate_User_URL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
			connection.setRequestMethod(POST);
			String content = "{\"useraccount\": {\"username\": \""+username+"\", \"password\": \""+password+"\"} }";
			DataOutputStream printout = new DataOutputStream ( connection.getOutputStream () );
			// send the data
			printout.writeBytes(content);
			printout.flush();
			printout.close();
			 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
			Object userAccObject = jsonHelper.parse(element, UserAccountWrapper.class);
			if(userAccObject!=null){
				status = ((UserAccountWrapper)userAccObject).getUserAccount().result.code;
			}else status=0;
			}  
			catch(Exception e){  
				status=0;
				Log.e(LOGTAG, "Error in login!");
			} 
			Log.i(LOGTAG, "Login successfully completed.");
	return status;
	}
	
	public static int deleteUserFriend(int userFriendId)
	{
		try
		{
			Log.i(LOGTAG, "Delete friend started...");
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.FRIEND_URL+AppConstants.USER_ID+AppConstants.DELETE_ID+userFriendId);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
			connection.setRequestMethod(DELETE);
			 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
				Object userAccObject = jsonHelper.parse(element, EventAddDeleteWrapper.class);
				status = ((EventAddDeleteWrapper)userAccObject).getUseraccount().result.get(1).code; 
				Log.i(LOGTAG, "status delete user friend = "+status); 
		}
		catch(Exception e)
		{
			status=0;
			Log.e(LOGTAG, "Error in user friend delete!");
			e.printStackTrace();
		}
		Log.i(LOGTAG, "Friend delete successfully completed.");
	return status;
	}

	public static int addFriend(int userId,int friendId)
	{
		try
		{
			Log.i(LOGTAG, "User friend adding started...");
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.Authenticate_User_URL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
			connection.setRequestMethod(POST);
			String content = "{\"useraccount\": {\"id\": "+userId+", \"friendships\": [{ \"friendid\""+friendId+"}] }";
			DataOutputStream printout = new DataOutputStream ( connection.getOutputStream () );
			printout.writeBytes(content);
			printout.flush();
			printout.close();
			 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
			Object userAccObject = jsonHelper.parse(element, UserAccountResultWrapper.class);
			status = ((UserAccountResultWrapper)userAccObject).getUserAccount().result.code; 
		}
		catch(Exception e)
		{
			status=0;
			Log.e(LOGTAG, "Error in adding friend!");
			e.printStackTrace();
		}
		Log.i(LOGTAG, "Friend adding successfully completed.");
	return status;
	}
		
	public static int registerNewUser(List<com.ga2oo.palendar.objects.Registration> vctRegData)
	{
		StringBuilder strRequest = new StringBuilder();
		try
		{
			Log.i(LOGTAG, "Register new user started...");
			strRequest.append("{\"useraccount\":{");
			strRequest.append("\"email\":\"");
			strRequest.append(vctRegData.get(0).strEmail);
			strRequest.append("\",\"username\":\"");
			strRequest.append(vctRegData.get(0).strUserName);
			strRequest.append("\",\"firstname\":\"");
			strRequest.append(vctRegData.get(0).strFName);
			strRequest.append("\",\"lastname\":\"");
			strRequest.append(vctRegData.get(0).strLName);
			strRequest.append("\",\"currentzip\":\"");
			strRequest.append("None");
			strRequest.append("\",\"gender\":\"");
			strRequest.append(vctRegData.get(0).strGender);
			strRequest.append("\",\"birthday\":\"");
			strRequest.append(vctRegData.get(0).strDOB);
			strRequest.append("\",\"password\":\"");
			strRequest.append(vctRegData.get(0).strPassword);
			strRequest.append("\",\"is_active\":\"");
			strRequest.append("True");
			strRequest.append("\",\"is_public\":\"");
//			strRequest.append("\n");
			strRequest.append("True");
			strRequest.append("\",\"is_calendar_shared\":\"");
			strRequest.append("True");
			strRequest.append("\",\"deviceid\":\"");
			strRequest.append("None");
			strRequest.append("\",\"savedlocations\":[{\"geocode\":\"");
			strRequest.append("None");
			strRequest.append("\",\"is_primary\":\"");
			strRequest.append("True");
			strRequest.append("\",\"address\":\"");
			strRequest.append(vctRegData.get(0).strAddress);
			strRequest.append("\",\"country\":\"");
			strRequest.append(vctRegData.get(0).strCountry);
			strRequest.append("\",\"city\":\"");
//			strRequest.append("\n");
			strRequest.append(vctRegData.get(0).strCity);
			strRequest.append("\",\"zipcode\":\"");
			strRequest.append(vctRegData.get(0).strZipcode);
			strRequest.append("\",\"state\":\"");
			strRequest.append(vctRegData.get(0).strState);
			strRequest.append("\"}],\"associations\":[{\"associationid\":\"");
			strRequest.append(vctRegData.get(0).strTwitterAssociationId);
			strRequest.append("\",\"associationtype\":\"");
			strRequest.append("twitter");
			strRequest.append("\"},{\"associationid\":\"");
			strRequest.append(vctRegData.get(0).strFbAssociationId);
			strRequest.append("\",\"associationtype\":\"");
			strRequest.append("facebook");
			strRequest.append("\"},{\"associationid\":\"");
			strRequest.append(vctRegData.get(0).strYoutubeAssociationId);
			strRequest.append("\",\"associationtype\":\"");
			strRequest.append("youtube");
			strRequest.append("\"}]}}");
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.USER_REGISTRATION);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
			connection.setRequestMethod(POST);
			DataOutputStream printout = new DataOutputStream ( connection.getOutputStream () );
			
			// send the data
			printout.writeBytes(strRequest.toString());
			printout.flush();
			printout.close();
			 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
			Object userAccObject = jsonHelper.parse(element, UserAccountResultWrapper.class);
			status = ((UserAccountResultWrapper)userAccObject).getUserAccount().result.code;
			regMessage = ((UserAccountResultWrapper)userAccObject).getUserAccount().result.message;
		}
		catch(Exception e)
		{
			status=0;
			Log.e(LOGTAG, "Error in register new user!");
			e.printStackTrace();
		}
		Log.i(LOGTAG, "Register new user successfully completed.");
		return status;
	}
	
	public static int registerNewBusiness(List<BusinessRegistration> vctBusinessRegistration)
	{
		StringBuilder strRequest = new StringBuilder();
		try
		{
			Log.i(LOGTAG, "Register new business started...");
			strRequest.append("{\"businessaccount\":{\"bizname\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strCompanyName);
			strRequest.append("\",\"contactname\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strName);
			strRequest.append("\",\"businesstype\":\"");
			strRequest.append(vctBusinessRegistration.get(0).intBusinessType);
			strRequest.append("\",\"contactemail\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strEmail);
			strRequest.append("\",\"supportemail\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strSuppEmail);
			strRequest.append("\",\"phonenumber\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strPhNo);
			strRequest.append("\",\"faxnumber\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strFaxNo);
			strRequest.append("\",\"url\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strUrl);
			strRequest.append("\",\"image\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strImage);
			strRequest.append("\",\"ein\":\"");
			strRequest.append("1");
			strRequest.append("\",\"password\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strPassword);
			strRequest.append("\",\"description\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strDescription);
			strRequest.append("\",\"savedlocations\":[{\"geocode\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strGeocode);
			strRequest.append("\",\"is_primary\":\"");
			strRequest.append(vctBusinessRegistration.get(0).isPrimary);
			strRequest.append("\",\"address\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strAddress);
			strRequest.append("\",\"country\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strCountry);
			strRequest.append("\",\"city\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strCity);
			strRequest.append("\",\"zipcode\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strZip);
			strRequest.append("\",\"state\":\"");
			strRequest.append(vctBusinessRegistration.get(0).strState);
			strRequest.append("\"}]}}");
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.BUSINESS_REGISTRATION);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
			connection.setRequestMethod(POST);
			DataOutputStream printout = new DataOutputStream ( connection.getOutputStream () );
			printout.writeBytes(strRequest.toString());
			printout.flush();
			printout.close();
			 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
			Object userAccObject = jsonHelper.parse(element, BuisnessAccountResultWrapper.class);
			status = ((BuisnessAccountResultWrapper)userAccObject).getBusinessaccount().result.code;
		}
	  catch (Exception e)
	  {
		  status=0;
		  Log.e(LOGTAG, "Error in register new business");
		  e.printStackTrace();
		  status = 0;
	  }
		Log.i(LOGTAG, "Register new business successfully completed.");
	 return status;	

   }

	public static int uploadUserImage(int userId,String imageAsciiCode)
	 {
		StringBuffer strRequest = new StringBuffer();
		 try
		 {
			 Log.i(LOGTAG, "Start uploading user image...");
			 strRequest.append("{ \"useraccount\" : {\"id\":");
			 strRequest.append(AppConstants.USER_ID);
			 strRequest.append(", \"image\":");
			 strRequest.append(imageAsciiCode);
			 strRequest.append("}}");
			 URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.USER_IMAGE);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
				connection.setRequestMethod(POST);
				DataOutputStream printout = new DataOutputStream ( connection.getOutputStream () );
				JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
				// send the data
				printout.writeBytes(strRequest.toString());
				printout.flush();
				printout.close();
			
		 }catch(Exception e){
			 status = 0;
			 Log.e(LOGTAG, "Error in loading user image!");
			 e.printStackTrace();
		 }
		 Log.i(LOGTAG, "Uploading user image successfully completed.");
		 return status;
	 }	

	public static int addUserSavedLocation(List<UserLocationObject> userSavedLoaction)
	{
		StringBuilder strRequest = new StringBuilder();
		 try
		 {
			 Log.i(LOGTAG, "Start adding user saved location...");
			 strRequest.append("{ \"useraccount\" : {\"id\":");
			 strRequest.append(AppConstants.USER_ID);
			 strRequest.append(", \"savedlocations\":[{\"address\":\"");
			 strRequest.append(userSavedLoaction.get(0).address);
			 strRequest.append("\", \"city\":\"");
			 strRequest.append(userSavedLoaction.get(0).city);
			 strRequest.append("\", \"state\":\"");
			 strRequest.append(userSavedLoaction.get(0).state);
			 strRequest.append("\", \"zipcode\":\"");
			 strRequest.append(userSavedLoaction.get(0).zipcode);
			 strRequest.append("\", \"country\":\"");
			 strRequest.append(userSavedLoaction.get(0).country);
			 strRequest.append("\", \"geocode\":\"");
			 strRequest.append(userSavedLoaction.get(0).geocode);
			 strRequest.append("\", \"is_primary\":\"");
			 strRequest.append(userSavedLoaction.get(0).is_primary);
			 strRequest.append("\"}]}}");
			 URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.POST_USER_LOCATION_URL);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
				connection.setRequestMethod(POST);
				DataOutputStream printout = new DataOutputStream ( connection.getOutputStream () );
				// send the data
				printout.writeBytes(strRequest.toString());
				printout.flush();
				printout.close();
				 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
				Object userAccObject = jsonHelper.parse(element, UserAccountResultWrapper.class);
				status = ((UserAccountResultWrapper)userAccObject).getUserAccount().result.code;
		 }catch(Exception e){
			 status=0;
			 Log.e(LOGTAG, "Error in adding user saved location!");
			 e.printStackTrace();
		 }
		 Log.i(LOGTAG, "Adding user saved location successfully completed.");
		 return status;
	}

	public static int changeUserPrimaryLocation(int locationId)
	{
		StringBuilder strRequest = new StringBuilder();
		try
		{
			Log.i(LOGTAG, "Start changing user primary location...");
			strRequest.append("{\"useraccount\":{\"id\":\"");
			strRequest.append(AppConstants.USER_ID);
			strRequest.append("\",\"savedlocations\":[{\"id\":\"");
			strRequest.append(locationId);
			strRequest.append("\",\"is_primary\":\"true\"}]}}");
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.UPDATE_PROFILE);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
				connection.setRequestMethod(POST);
				DataOutputStream printout = new DataOutputStream ( connection.getOutputStream () );
				// send the data
				printout.writeBytes(strRequest.toString());
				printout.flush();
				printout.close();
				 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
				Object userAccObject = jsonHelper.parse(element, UserAccountResultWrapper.class);
				status = ((UserAccountResultWrapper)userAccObject).getUserAccount().result.code;
			
		}catch(Exception e){
			status=0;
			 Log.e(LOGTAG, "Error in changing user primary location!");
			e.printStackTrace();
		}
		 Log.i(LOGTAG, "Changing user primary location successfully completed.");
		return status;
	}

	public static int removeSavedLocation(int locationId)
	{
		try
		{
			 Log.i(LOGTAG, "Removed saved location...");
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.POST_USER_LOCATION_URL+"/id/"+AppConstants.USER_ID+AppConstants.DELETE_ID+locationId);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
			connection.setRequestMethod(DELETE);
			
			 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
				Object userAccObject = jsonHelper.parse(element, LocationDeleteWrapper.class);
				status = ((LocationDeleteWrapper)userAccObject).getUserAccount().result.get(0).code;  
		}catch(Exception e){
			status=0;
			 Log.e(LOGTAG, "Error in removed saved location!");
			e.printStackTrace();
		}
		 Log.i(LOGTAG, "Removed saved location successfully completed.");
		return status;
	}

	public static int updateUserProfile(List<com.ga2oo.palendar.objects.UpdateProfile> vctUpdateprofile){
		UserAccountBusinessLayer userAccBL;
		List<UserLocationObject> vctUserLocation;
		List<Association> vctUserAssociations;
		List<UserAccount> vctUserAccount;
		StringBuilder strRequest = new StringBuilder();
		StringBuilder strUserLocation=new StringBuilder();
		try{
			
			 Log.i(LOGTAG, "Update user profile...");
			String strUserOldAssociations="";
			userAccBL			=new UserAccountBusinessLayer();
			vctUserLocation		=new ArrayList<UserLocationObject>();
			vctUserLocation		=userAccBL.getUserSavedLocation(AppConstants.USER_ID);
			vctUserAssociations	=new ArrayList<Association>();
			vctUserAssociations =userAccBL.getUserAssociations(AppConstants.USER_ID);
			vctUserAccount		=new ArrayList<UserAccount>();
			vctUserAccount		=userAccBL.getUserInformation();
			for(int i=0;i<vctUserLocation.size();i++)
			{
				strUserLocation.append("{\"id\": \"");
				strUserLocation.append(vctUserLocation.get(i).locationId);
				strUserLocation.append("\", \"address\": \"");
				strUserLocation.append(vctUserLocation.get(i).Address);
				strUserLocation.append("\", \"city\": \"");
				strUserLocation.append(vctUserLocation.get(i).City);
				strUserLocation.append("\", \"state\": \"");
				strUserLocation.append(vctUserLocation.get(i).State);
				strUserLocation.append("\", \"zipcode\": \"");
				strUserLocation.append(vctUserLocation.get(i).Zip);
				strUserLocation.append("\", \"country\": \"");
				strUserLocation.append(vctUserLocation.get(i).Country);
				strUserLocation.append("\", \"geocode\": \"");
				strUserLocation.append(vctUserLocation.get(i).GeoCode);
				strUserLocation.append("\", \"is_primary\":\"");
				strUserLocation.append(vctUserLocation.get(i).Is_Primary);
				strUserLocation.append("\" }");
				if(i!=(vctUserLocation.size()-1)){
					strUserLocation.append(", ");
				}
			}
			strRequest.append("{ \"useraccount\" : {\"id\": ");
			strRequest.append(AppConstants.USER_ID);
			strRequest.append(", \"email\": \"");
			strRequest.append(vctUpdateprofile.get(0).strEmail);
			strRequest.append("\", \"username\": \"");
			strRequest.append(vctUpdateprofile.get(0).strUserName);
			strRequest.append("\", \"firstname\": \"");
			strRequest.append(vctUpdateprofile.get(0).strFName);
			strRequest.append("\", \"lastname\": \"");
			strRequest.append(vctUpdateprofile.get(0).strLName);
			strRequest.append("\", \"currentzip\": \"");
			strRequest.append(vctUserAccount.get(0).zipcode);
			strRequest.append("\", \"gender\": \"");
			strRequest.append(vctUserAccount.get(0).gender);
			strRequest.append("\", \"birthday\": \"");
			strRequest.append(vctUserAccount.get(0).birthday);
			strRequest.append("\", \"password\": \"");
			strRequest.append(vctUserAccount.get(0).password);
			strRequest.append("\", \"is_active\": \"");
			strRequest.append(vctUserAccount.get(0).is_active);
			strRequest.append("\", \"is_public\": \"");
			strRequest.append(vctUserAccount.get(0).is_public);
			strRequest.append("\", \"is_calendar_shared\": \"");
			strRequest.append(vctUserAccount.get(0).is_calendar_shared);
			strRequest.append("\", \"savedlocations\": [");
			strRequest.append(strUserLocation);
			strRequest.append("]}}");
			
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.UPDATE_PROFILE);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
			connection.setRequestMethod(POST);
			DataOutputStream printout = new DataOutputStream ( connection.getOutputStream () );
			
			// send the data
			printout.writeBytes(strRequest.toString());
			printout.flush();
			printout.close();
			 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
			Object userAccObject = jsonHelper.parse(element, UserAccountResultWrapper.class);
			status = ((UserAccountResultWrapper)userAccObject).getUserAccount().result.code;
			updateUserProfileMgs =  ((UserAccountResultWrapper)userAccObject).getUserAccount().result.message;
			
		}catch(Exception e){
			status=0;
			 Log.e(LOGTAG, "Error in update user profile!");
			e.printStackTrace();
		}
		 Log.i(LOGTAG, "UUpdate user profile successfully completed.");
		return status;
	}
	
	public static int addEventToUser(int user_id, int eventid)
	{
		try
		{
			Log.i(LOGTAG, "Adding event to user...");
			StringBuilder strAddEvent = new StringBuilder();
			strAddEvent.append("{ \"useraccount\" : {\"id\":");
			strAddEvent.append(user_id);
			strAddEvent.append(", \"events\" :[{\"eventid\":");
			strAddEvent.append(eventid);
			strAddEvent.append("}]}}");
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.ADD_EVENT);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
			connection.setRequestMethod(POST);
			DataOutputStream printout = new DataOutputStream ( connection.getOutputStream () );
			printout.writeBytes(strAddEvent.toString());
			printout.flush();
			printout.close();
			 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
			Object userAccObject = jsonHelper.parse(element, EventAddDeleteWrapper.class);
			status = ((EventAddDeleteWrapper)userAccObject).getUseraccount().result.get(0).code;
		}catch(Exception e){
			status = -1;
			 Log.e(LOGTAG, "Error in adding event ot user!");
			e.printStackTrace();
		}
		 Log.i(LOGTAG, "adding event to user successfully completed.");
	return status;
	}
		
	public static int deleteUserEvent(int user_id, int eventAddedId)
	{
		try
		{
			Log.i(LOGTAG, "Deleting user event...");
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.ADD_EVENT+"id/"+user_id+AppConstants.DELETE_ID+eventAddedId);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
			connection.setRequestMethod(DELETE);
			
			 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
			Object userAccObject = jsonHelper.parse(element, EventAddDeleteWrapper.class);
			status = ((EventAddDeleteWrapper)userAccObject).getUseraccount().result.get(0).code;
			Log.v(LOGTAG, "message = "+ ((EventAddDeleteWrapper)userAccObject).getUseraccount().result.get(0).message);
		}
		catch(Exception e)
		{
			status=0;
			Log.e(LOGTAG, "Error in deleting user event!");
			e.printStackTrace();
		}
		 Log.i(LOGTAG, "Deleting user event successfully completed.");
		 return status;
	}
	
	public static int addBusinessToUser(int user_id, int businessId)
	{
		try
		{
			Log.i(LOGTAG, "Adding business to user...");
			StringBuilder strAddBusiness = new StringBuilder();
			strAddBusiness.append("{ \"useraccount\" : {\"id\":");
			strAddBusiness.append(user_id);
			strAddBusiness.append(", \"fav_businesses\" :[{\"businessid\":");
			strAddBusiness.append(businessId);
			strAddBusiness.append("}]}}");
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.ADD_BUSINESS);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
			connection.setRequestMethod(POST);
			DataOutputStream printout = new DataOutputStream ( connection.getOutputStream () );
			printout.writeBytes(strAddBusiness.toString());
			printout.flush();
			printout.close();
			 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
			Object userAccObject = jsonHelper.parse(element, BusinessToUserWrapper.class);
			status = ((BusinessToUserWrapper)userAccObject).getUseraccount().result.get(0);
		}
		catch(Exception e)
		{
			status=-1;
			 Log.e(LOGTAG, "Error in adding business to user!");
			e.printStackTrace();
		}
		Log.i(LOGTAG, "Adding business to user successfully completed");
		return status;
	}
	
	public static int deleteUserBusiness(int user_id, int eventAddedId)
	{
		try
		{
			Log.i(LOGTAG, "Deleting user business...");
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.ADD_BUSINESS+"id/"+user_id+AppConstants.DELETE_ID+eventAddedId);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
			connection.setRequestMethod(DELETE);
			 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
			 Object userAccObject = jsonHelper.parse(element, EventAddDeleteWrapper.class);
				status = ((EventAddDeleteWrapper)userAccObject).getUseraccount().result.get(0).code;	
		}
		catch(Exception e)
		{
			status=0;
			 Log.e(LOGTAG, "Error in deleting user business!");
			e.printStackTrace();
		}
		Log.i(LOGTAG, "Deleting user business successfully completed.");
		return status;
	}

	public static String sendRecommendation(int[] friendIds,int eventId, String strSubject,String strDetail)
	{
		try
		{
			Log.i(LOGTAG, "Sending recommendation...");
			StringBuilder strSendRecommenation = new StringBuilder();
			strSendRecommenation.append("{ \"useraccount\" : {\"id\":");
			strSendRecommenation.append(AppConstants.USER_ID);
			strSendRecommenation.append(",\"subject\": \"");
			strSendRecommenation.append(strSubject);
			strSendRecommenation.append("\",\"eventid\":");
			strSendRecommenation.append(eventId);
			strSendRecommenation.append("\"message\":\"");
			strSendRecommenation.append(strDetail);
			strSendRecommenation.append("\",friendships\":[");
			for(int i=0;i<friendIds.length;i++){
				strSendRecommenation.append("{\"friendid\":");
				strSendRecommenation.append(friendIds[i]);
				strSendRecommenation.append("}");
				if(i!=(friendIds.length-1)){
					strSendRecommenation.append(",");
				}
			}
			strSendRecommenation.append("]}}");
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.USER_RECOMMENDATION);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
			connection.setRequestMethod(POST);
			DataOutputStream printout = new DataOutputStream ( connection.getOutputStream () );
			printout.writeBytes(strSendRecommenation.toString());
			printout.flush();
			printout.close();
			 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
			 status = 0;
			 Object friendResult = jsonHelper.parse(element, EventAddDeleteWrapper.class);
			 status = ((EventAddDeleteWrapper)friendResult).getUseraccount().result.get(0).code;
		}
		catch(Exception e)
		{
			status=0;
			 Log.e(LOGTAG, "Error in sending recommendation!");
			e.printStackTrace();
		}
		 Log.i(LOGTAG, "Sending recommendation successfully completed.");
		return ""+status;
	}
	
	public static int friendRequestResponce(int userId,int useraddedfriendid,int responce)
	{
		try
		{
			Log.i(LOGTAG, "Sending friend request responce...");
			StringBuilder strFriendRequestResponce  = new StringBuilder();
			strFriendRequestResponce.append("{ \"useraccount\" : {\"id\":");
			strFriendRequestResponce.append(userId);
			strFriendRequestResponce.append(",\"friendships\":[{\"useraddedfriendid\": ");
			strFriendRequestResponce.append(useraddedfriendid);
			strFriendRequestResponce.append(", \"status\":");
			strFriendRequestResponce.append(responce);
			strFriendRequestResponce.append("}]}}");
			URL url = new URL(AppConstants.JSON_HOST_URL+AppConstants.FRIEND_REQUEST_RESPONCE);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty(CONTENT_TYPE,APPLICATION_JSON);
			connection.setRequestMethod(POST);
			DataOutputStream printout = new DataOutputStream ( connection.getOutputStream () );
			printout.writeBytes(strFriendRequestResponce.toString());
			printout.flush();
			printout.close();
			 JsonElement element = new JsonParser().parse(new BufferedReader(new InputStreamReader(connection.getInputStream())));
			 status = 0;
			 Object friendResult = jsonHelper.parse(element, EventAddDeleteWrapper.class);
			 status = ((EventAddDeleteWrapper)friendResult).getUseraccount().result.get(0).code;
		}
		catch(Exception e)
		{
			status=0;
			 Log.e(LOGTAG, "Error in sending friend request responce!");
			e.printStackTrace();
		}
		Log.i(LOGTAG, "Sending friend request responce successfully completed.");
		return status;
	}	
}