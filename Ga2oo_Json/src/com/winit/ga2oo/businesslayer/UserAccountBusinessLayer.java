package com.winit.ga2oo.businesslayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.databaseaccess.DatabaseHelper;
import com.winit.ga2oo.databaseaccess.DictionaryEntry;
import com.winit.ga2oo.objects.Association;
import com.winit.ga2oo.objects.Business;
import com.winit.ga2oo.objects.FavoriteEvent;
import com.winit.ga2oo.objects.Friend;
import com.winit.ga2oo.objects.Notifications;
import com.winit.ga2oo.objects.UserAccount;
import com.winit.ga2oo.objects.UserLocation;
import com.winit.ga2oo.objects.UserLocationObject;
import com.winit.ga2oo.objects.UserRecommendationObject;

public class UserAccountBusinessLayer 
{
	private static final String USERIMAGEID = "USERIMAGEID";
	private static final String USERIMAGEISUPDATED = "USERIMAGEISUPDATED";
	private static final String USERIMAGEDATEUPDATED = "USERIMAGEDATEUPDATED";
	private static final String IMG_WIDTH = "&img_width";
	private static final String IMG_ID = "img_id=";
	private static final String FRIENDIMAGEID = "FRIENDIMAGEID";
	private static final String FRIENDIMAGEISUPDATED = "FRIENDIMAGEISUPDATED";
	private static final String FRIENDIMAGEDATEUPDATED = "FRIENDIMAGEDATEUPDATED";
	private static final String DISTINCT = " DISTINCT ";
	private static final String GAEVENTNAME = "GAEVENTNAME";
	private static final String GAEVENTID = "GAEVENTID";
	private static final String MESSAGE = "MESSAGE";
	private static final String SUBJECT = "SUBJECT";
	private static final String THREAD_ID = "THREAD_ID";
	private static final String USERNOTIFICATIONS2 = "USERNOTIFICATIONS";
	private static final String DATECREATED = "DATECREATED";
	private static final String FRIENDREQUESTID = "FRIENDREQUESTID";
	private static final String NOTIFICATIONTYPE = "NOTIFICATIONTYPE";
	private static final String SENDERID = "SENDERID";
	private static final String NOTIFICATIONID = "NOTIFICATIONID";
	private static final String TEXT = "TEXT";
	private static final String EVENTNAME = "EVENTNAME";
	private static final String OR = " OR ";
	private static final String SENDERNAME = "SENDERNAME";
	private static final String ISREAD = "ISREAD";
	private static final String ORDER_BY = " ORDER BY ";
	private static final String IN = " IN ";
	private static final String NOT = " NOT ";
	private static final String ACCOUNT = "Account";
	private static final String PROFILE = "Profile";
	private static final String BUSINESS = "BUSINESS";
	private static final String IS_PUBLIC = "IS_PUBLIC";
	private static final String LAST_NAME = "LAST_NAME";
	private static final String FIRST_NAME = "FIRST_NAME";
	private static final String USER_NAME = "USER_NAME";
	private static final String USER_ID = "USER_ID";
	private static final String ATTENDING = "ATTENDING";
	private static final String RIGHT_LIKE = "%'";
	private static final String LEFT_LIKE = "'%";
	private static final String LIKE = " LIKE ";
	private static final String ALLFIELDS = " * ";
	private static final String USERADDEDBUSINESSID = "USERADDEDBUSINESSID";
	private static final String BUSINESSNAME = "BUSINESSNAME";
	private static final String BUSINESSID = "BUSINESSID";
	private static final String CONTACTNAME = "CONTACTNAME";
	private static final String URL = "URL";
	private static final String CONTACTEMAIL = "CONTACTEMAIL";
	private static final String SUPPORTEMAIL = "SUPPORTEMAIL";
	private static final String PHONE = "PHONE";
	private static final String BIZNAME = "BIZNAME";
	private static final String FAX = "FAX";
	private static final String BUSINESSTYPE = "BUSINESSTYPE";
	private static final String IS_ACTIVE = "IS_ACTIVE";
	private static final String ID = "ID";
	private static final String NONE = "none";
	private static final String LOCATIONDATEUPDATED = "LOCATIONDATEUPDATED";
	private static final String GEOCODE = "GEOCODE";
	private static final String FRIENDIMAGE = "FRIENDIMAGE";
	private static final String STATUS = "STATUS";
	private static final String DATEUPDATED = "DATEUPDATED";
	private static final String FRIENDID = "FRIENDID";
	private static final String USERADDEDFRIENDID = "USERADDEDFRIENDID";
	private static final String ASSOCIATIONID = "ASSOCIATIONID";
	private static final String ASSOCIATIONTYPE = "ASSOCIATIONTYPE";
	private static final String ASSOID = "ASSOID";
	private static final String GEOPOINT = "GEOPOINT";
	private static final String ADDRESS = "ADDRESS";
	private static final String STATE = "STATE";
	private static final String COUNTRY = "COUNTRY";
	private static final String CITY = "CITY";
	private static final String TRUE = "True";
	private static final String FALSE = "False";
	private static final String QUOTE = "'";
	private static final String ISPRIMARY = "ISPRIMARY";
	private static final String SET = " SET ";
	private static final String UPDATE = "UPDATE ";
	private static final String EVENTID = "EVENTID";
	private static final String USERADDEDEVENTID = "USERADDEDEVENTID";
	private static final String AND = " AND ";
	private static final String LOCATIONID = "LOCATIONID";
	private static final String INVISIBLEMODE = "INVISIBLEMODE";
	private static final String DATE_UPDATED = "DATE_UPDATED";
	private static final String EQUALLY = "=";
	private static final String WHERE = " WHERE ";
	private static final String FROM = " FROM ";
	private static final String ISPUBLIC = "ISPUBLIC";
	private static final String DEVICEID = "DEVICEID";
	private static final String ISACTIVE = "ISACTIVE";
	private static final String GENDER = "GENDER";
	private static final String ZIPCODE = "ZIPCODE";
	private static final String BIRTHDAY = "BIRTHDAY";
	private static final String ISCALENDERSHARED = "ISCALENDERSHARED";
	private static final String PASSWORD = "PASSWORD";
	private static final String EMAIL = "EMAIL";
	private static final String USERIMAGE = "USERIMAGE";
	private static final String LASTNAME = "LASTNAME";
	private static final String FIRSTNAME = "FIRSTNAME";
	private static final String USERNAME = "USERNAME";
	private static final String COMMA = " , ";
	private static final String USERID = "USERID";
	private static final String SELECT = "SELECT ";
	private static final String LOGTAG = "UserAccountBusinessLayer";
	private static final String USER="USER";
	private static final String FRIENDS="FRIENDS";
	private static final String USERLOCATION="USERLOCATION";
	private static final String BUSINESSLIST = "BUSINESSLIST";
	private static final String USERNOTIFICATIONS = USERNOTIFICATIONS2;
	private static final String USERINBOX = "USERINBOX";
	private static final String USERASSOCIATION="USERASSOCIATION";
	private static final String USERFAVORITEBUSINESS="USERFAVORITEBUSINESS";
	private static final String USERFAVORITE="USERFAVORITE";
	private static final String GA2OOUSERLIST="GA2OOUSERLIST";
	private static final String EVENTDETAILS="EVENTDETAILS";
	private static final String DATEFORMAT = "yyyy-MM-dd";
	private static final String DATEFORMATLONG = "yyyy-MM-dd";
	
	public int businessId;
	
	public static boolean isUserDataNotEmpty(){
		DictionaryEntry[][] data = DatabaseHelper.get(SELECT+USERID+FROM+USER);
		return (data.length>0?true:false);
	}
	
	public List<UserAccount> getUserInformation()
	{
		// TODO for day - startdate would be 04-Apr-2011 00:00:00 and  end date would be 04-Apr-2011 23:59:59
		//SELECT * FROM tblAppointments where AppointmentTime between  '04-Apr-2011 00:00:00' and '15-Apr-2011 23:59:59' 
		DictionaryEntry[][] data=null;
			data = DatabaseHelper.get(SELECT+USERID+COMMA+USERNAME+COMMA+FIRSTNAME+COMMA+LASTNAME+COMMA+USERIMAGE+COMMA+EMAIL+COMMA+PASSWORD+COMMA
									+ISCALENDERSHARED+COMMA+BIRTHDAY+COMMA+ZIPCODE+COMMA+GENDER+COMMA+ISACTIVE+COMMA+DEVICEID+COMMA+ISPUBLIC+COMMA+USERIMAGEID+COMMA+USERIMAGEDATEUPDATED+COMMA+USERIMAGEISUPDATED+
									FROM+USER+WHERE+USERID+EQUALLY+AppConstants.USER_ID);
		
	  List<UserAccount> result  = new ArrayList<UserAccount>();
		
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					UserAccount objUserAct = new UserAccount();
					objUserAct.id = (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objUserAct.username = (data[i][1].value!=null?data[i][1].value.toString():"");
					objUserAct.firstname=(data[i][2].value!=null?data[i][2].value.toString():"");
					objUserAct.lastname=(data[i][3].value!=null?data[i][3].value.toString():"");
					objUserAct.imagesrc = (data[i][4].value!=null?data[i][4].value.toString():"");
					objUserAct.email= (data[i][5].value!=null?data[i][5].value.toString():"");
					objUserAct.password = (data[i][6].value!=null?data[i][6].value.toString():"");
					objUserAct.is_calendar_shared= (data[i][7].value!=null?data[i][7].value.toString():"");
					objUserAct.birthday= (data[i][8].value!=null?data[i][8].value.toString():"");
					objUserAct.zipcode= (data[i][9].value!=null?data[i][9].value.toString():"");
					objUserAct.gender= (data[i][10].value!=null?data[i][10].value.toString():"");
					objUserAct.is_active= (data[i][11].value!=null?data[i][11].value.toString():"");
				//	objUserAct.DeviceId= (data[i][12].value!=null?data[i][12].value.toString():"");
					objUserAct.is_public= (data[i][13].value!=null?data[i][13].value.toString():"");
					objUserAct.imageId= (data[i][14].value!=null?data[i][14].value.toString():"0");
					objUserAct.image_updated= (data[i][15].value!=null?data[i][15].value.toString():"");
					objUserAct.isImageUpdated= (data[i][16].value!=null?data[i][16].value.toString():"false");
					result.add(objUserAct);	
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}
	
	public long Insert(UserAccount objUserAccount)
	{
		String whereClause = USERID+EQUALLY+objUserAccount.id;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+USERID+COMMA+DATE_UPDATED+FROM+USER+WHERE+whereClause); 
		DictionaryEntry [][] data2 = DatabaseHelper.get(SELECT+USERIMAGEDATEUPDATED+FROM+USER+WHERE+whereClause); 
		ContentValues values = new ContentValues();
		values.put(USERID, objUserAccount.id);
 		values.put(BIRTHDAY,objUserAccount.birthday);
 		values.put(USERNAME, objUserAccount.username);
 		values.put(ISACTIVE,objUserAccount.is_active);
 		values.put(USERIMAGE,(objUserAccount.imagesrc==null?AppConstants.NOIMAGE:objUserAccount.imagesrc));
 		values.put(ZIPCODE,objUserAccount.zipcode);
 		values.put(INVISIBLEMODE," ");
 		values.put(DEVICEID,objUserAccount.deviceid);
 		values.put(GENDER, objUserAccount.gender);
 		values.put(FIRSTNAME, objUserAccount.firstname);
 		values.put(EMAIL, objUserAccount.email);
 		values.put(LASTNAME, objUserAccount.lastname);
 		values.put(PASSWORD, objUserAccount.password);
 		values.put(ISCALENDERSHARED, objUserAccount.is_calendar_shared);
 		values.put(ISPUBLIC, objUserAccount.is_public);
 		values.put(DATE_UPDATED, objUserAccount.date_updated);
 		if(data2!=null && data2[0][0]!=null){
				if(objUserAccount.image_updated!=null && !"None".equals(objUserAccount.image_updated)){
					Date oldDate = convertStringToDateLong(data2[0][0].value.toString());  // need to cast from value to object string RBRB
					Date newDate = convertStringToDateLong(objUserAccount.image_updated);
					if(newDate.after(oldDate)){
						values.put(USERIMAGEISUPDATED, "true");
					}else{
						values.put(USERIMAGEISUPDATED, "false");
					}
				}
			}else{
				values.put(USERIMAGEISUPDATED, "false");
			}
			values.put(USERIMAGEDATEUPDATED,  (objUserAccount.image_updated!=null?objUserAccount.image_updated:""));
			values.put(USERIMAGEID,getImageIDfromSrc(objUserAccount.imagesrc));
		if(data != null && data[0][0].value != null)
		{
			if(!objUserAccount.date_updated.equals(data[0][1].value.toString())){
				return DatabaseHelper.doUpdate(USER,values,whereClause, null);
			}else{
				return 0;
			}

		}
		else
		{
	 		return DatabaseHelper.doInsert(USER, values);
		}
	}
	
	public boolean deleteUserInfo()
	{
 		return DatabaseHelper.deleteAllDataFromTable(USER);
	}

	public boolean removeSavedLocation(int locationId)
	{
 		return DatabaseHelper.deleteAllDataFromTable(USERLOCATION+WHERE+LOCATIONID+EQUALLY+locationId+AND+USERID+EQUALLY+AppConstants.USER_ID);
	}

//	public boolean deleteAllSavedLocation()
//	{
//		return DatabaseHelper.deleteAllDataFromTable(USERLOCATION);
//	}
	
	public String getUserAddedEventId(int eventID){
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+USERADDEDEVENTID+FROM+USERFAVORITE+WHERE+EVENTID+EQUALLY+eventID);
		if(data != null && data[0][0].value != null)
		{
			return data[0][0].value.toString();
		}else{
			return null;
		}
	}
	
	public boolean updatePrimaryLocation(int locationId)
	{
 			if(locationId!=0)
 			{
 				DatabaseHelper.doUpdate(UPDATE+USERLOCATION+SET+ISPRIMARY+EQUALLY+QUOTE+FALSE+QUOTE+WHERE+USERID+EQUALLY+AppConstants.USER_ID);
 				DatabaseHelper.doUpdate(UPDATE+USERLOCATION+SET+ISPRIMARY+EQUALLY+QUOTE+TRUE+QUOTE+WHERE+LOCATIONID+EQUALLY+locationId+AND+USERID+EQUALLY+AppConstants.USER_ID);
 			}
 			else if(locationId==0){
 				DatabaseHelper.doUpdate(UPDATE+USERLOCATION+SET+ISPRIMARY+EQUALLY+QUOTE+FALSE+QUOTE+WHERE+USERID+EQUALLY+AppConstants.USER_ID);
 			}

		return true;
	}
	
	public long InsertSavedLocation(UserLocationObject objUserLocation)
	{
		String whereClause = LOCATIONID+EQUALLY+objUserLocation.id;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+LOCATIONID+COMMA+DATE_UPDATED+FROM+USERLOCATION+WHERE+whereClause); 
		ContentValues values = new ContentValues();
		values.put(LOCATIONID, objUserLocation.id);
		values.put(USERID, AppConstants.USER_ID);
 		values.put(ZIPCODE,objUserLocation.zipcode);
 		values.put(ISPRIMARY, objUserLocation.is_primary);
 		values.put(CITY,objUserLocation.city);
 		values.put(COUNTRY,objUserLocation.country);
 		values.put(STATE,objUserLocation.state);
 		values.put(ADDRESS,objUserLocation.address);
 		values.put(GEOPOINT, objUserLocation.geocode);
 		values.put(DATE_UPDATED, objUserLocation.date_updated);
		if(data != null && data[0][0].value != null)
		{
			if(!objUserLocation.date_updated.equals(data[0][1].value.toString())){
				return DatabaseHelper.doUpdate(USERLOCATION, values, whereClause, null);
			}else{
				return 0;
			}
		}
		else
		{
	 		return DatabaseHelper.doInsert(USERLOCATION, values);
		}
	}
	
	public List<UserLocationObject> getUserSavedLocation(int userID)
	{
		DictionaryEntry[][] data=null;
			data = DatabaseHelper.get(SELECT+LOCATIONID+COMMA+CITY+COMMA+ZIPCODE+COMMA+ADDRESS+COMMA+ISPRIMARY+COMMA+COUNTRY+COMMA+STATE+COMMA+GEOPOINT+FROM+USERLOCATION+WHERE +
					USERID+EQUALLY+AppConstants.USER_ID);
		
			List<UserLocationObject> result  = new ArrayList<UserLocationObject>();
		
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					UserLocationObject objUserLoaction = new UserLocationObject();
					objUserLoaction.locationId=(data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objUserLoaction.City 	= (data[i][1].value!=null?data[i][1].value.toString():"");
					objUserLoaction.Zip 	= (data[i][2].value!=null?data[i][2].value.toString():"");
					objUserLoaction.Address= (data[i][3].value!=null?data[i][3].value.toString():"");
					objUserLoaction.Is_Primary= (data[i][4].value!=null?data[i][4].value.toString():"");
					objUserLoaction.Country= (data[i][5].value!=null?data[i][5].value.toString():"");
					objUserLoaction.State= (data[i][6].value!=null?data[i][6].value.toString():"");
					objUserLoaction.GeoCode= (data[i][7].value!=null?data[i][7].value.toString():"");
					result.add(objUserLoaction);	
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
	return result;	
	}
	
	public long InsertUserAssociation(Association objAssociation)
	{
		String whereClause = ASSOID+EQUALLY+objAssociation.ID;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+ASSOID+FROM+USERASSOCIATION+WHERE+whereClause); 
		ContentValues values = new ContentValues();
		values.put(ASSOID, objAssociation.ID);
		values.put(USERID, AppConstants.USER_ID);
 		values.put(ASSOCIATIONTYPE,objAssociation.Associationtype);
 		values.put(ASSOCIATIONID, objAssociation.Associationid);
		if(data != null && data[0][0].value != null)
		{
			return DatabaseHelper.doUpdate(USERASSOCIATION, values, whereClause, null);
		}
		else
		{
	 		return DatabaseHelper.doInsert(USERASSOCIATION, values);
		}
	}

	public List<Association> getUserAssociations(int userID)
	{
		DictionaryEntry[][] data=null;
			data = DatabaseHelper.get(SELECT+ASSOID+COMMA+ASSOCIATIONTYPE+COMMA+ASSOCIATIONID+FROM+USERASSOCIATION+WHERE+USERID+EQUALLY+userID);
		
			List<Association> result  = new ArrayList<Association>();
		
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					Association objAssociation = new Association();
					objAssociation.ID 	= (data[i][0]!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objAssociation.Associationtype 	= (data[i][1].value!=null?data[i][1].value.toString():"");
					objAssociation.Associationid= (data[i][2].value!=null?data[i][2].value.toString():"");
					result.add(objAssociation);	
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}
	
	private Date convertStringToDate(String dateString){
	    SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
	    Date convertedDate;
	    try {
	        convertedDate = dateFormat.parse(dateString);
	        Log.i(LOGTAG, "convertedDate =  " + convertedDate);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        return null;
	    }
	    return convertedDate;

	}
	
	private Date convertStringToDateLong(String dateString){
	    SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMATLONG);
	    Date convertedDate;
	    try {
	        convertedDate = dateFormat.parse(dateString);
	        Log.i(LOGTAG, "convertedDate =  " + convertedDate);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        return null;
	    }
	    return convertedDate;

	}	

	private String getImageIDfromSrc(String src){
		String result = "";
		
		if (src != null)
		{
		  int startIndex = src.indexOf(IMG_ID)+IMG_ID.length();
		  int endIndex = src.indexOf(IMG_WIDTH);
		  result = src.substring(startIndex, endIndex);
		}
		return result;
	}
	
	
	public long InsertFriend(Friend objFriend)
	{
		String whereClause =USERADDEDFRIENDID+EQUALLY+objFriend.useraddedfriendid;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+FRIENDID+COMMA+DATEUPDATED+FROM+FRIENDS+WHERE+whereClause); 
		DictionaryEntry [][] data2 = DatabaseHelper.get(SELECT+FRIENDIMAGEDATEUPDATED+FROM+FRIENDS+WHERE+whereClause); 
		ContentValues values = new ContentValues();
		
		values.put(USERADDEDFRIENDID, objFriend.useraddedfriendid);
 		values.put(STATUS,objFriend.status);
 		values.put(FRIENDID, objFriend.friendid);
 		values.put(FIRSTNAME,objFriend.firstname);
 		values.put(LASTNAME, objFriend.lastname);
 		values.put(ISCALENDERSHARED,objFriend.is_calendar_shared);
 		values.put(EMAIL,objFriend.email);
 		values.put(FRIENDIMAGE,objFriend.imagesrc);
 		values.put(DATEUPDATED, objFriend.date_updated);
 		values.put(USERID,AppConstants.USER_ID);
 		if(objFriend.savedlocations!=null && objFriend.savedlocations.size()>0){
	 		for(int i=0;i<objFriend.savedlocations.size();i++){
	 			if(objFriend.savedlocations.get(i).is_primary){
	 				values.put(LOCATIONID,objFriend.savedlocations.get(0).locationid);
	 		 		values.put(ISPRIMARY, objFriend.savedlocations.get(0).is_primary);
	 		 		values.put(GEOCODE,objFriend.savedlocations.get(0).geocode);
	 		 		
	 		 		values.put(ADDRESS, objFriend.savedlocations.get(0).address);
	 		 		values.put(CITY,objFriend.savedlocations.get(0).city);
	 		 		values.put(STATE, objFriend.savedlocations.get(0).state);
	 		 		values.put(ZIPCODE,objFriend.savedlocations.get(0).zipcode);
	 		 		
	 		 		values.put(COUNTRY, objFriend.savedlocations.get(0).country);
	 		 		values.put(LOCATIONDATEUPDATED,NONE);
	 		 		
	 			}
	 		}
 		}
		if(data2 != null && data2[0][0].value != null){
					if(objFriend.image_updated!=null && !"None".equals(objFriend.image_updated)){
						Date oldDate = convertStringToDate(data2[0][0].value.toString());
						Date newDate = convertStringToDate(objFriend.image_updated);
						if(newDate.after(oldDate)){
							values.put(FRIENDIMAGEISUPDATED, "true");
						}else{
							values.put(FRIENDIMAGEISUPDATED, "false");
						}
					}
				}else{
					values.put(FRIENDIMAGEISUPDATED, "false");
				}
				values.put(FRIENDIMAGEDATEUPDATED,  (objFriend.image_updated!=null?objFriend.image_updated:""));
				values.put(FRIENDIMAGEID,getImageIDfromSrc(objFriend.imagesrc));

		if(data != null && data[0][0].value != null)
		{
			if(!objFriend.date_updated.equals(data[0][1].value.toString())){
				Log.v(LOGTAG, "friend updated");
				return DatabaseHelper.doUpdate(FRIENDS, values, whereClause, null);
			}else{
				Log.v(LOGTAG, "friend nothing");
				return 0;
			}
		}
		else
		{
			Log.v(LOGTAG, "friend inserted");
	 		return DatabaseHelper.doInsert(FRIENDS, values);
		}
	}
	
	public long InsertBusinessList(Business objBusiness)
	{
		String whereClause = ID+EQUALLY+objBusiness.id;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+ID+COMMA+DATE_UPDATED+FROM+BUSINESSLIST+WHERE+whereClause); 
		ContentValues values = new ContentValues();
		values.put(ID, objBusiness.id);
 		values.put(IS_ACTIVE,objBusiness.is_active);
 		values.put(BUSINESSTYPE, objBusiness.businesstype);
 		values.put(FAX,objBusiness.faxnumber);
 		values.put(BIZNAME,objBusiness.bizname);
 		values.put(PHONE,objBusiness.phonenumber);
 		values.put(SUPPORTEMAIL, objBusiness.supportemail);
 		values.put(CONTACTEMAIL, objBusiness.contactemail);
 		values.put(URL, objBusiness.url);
 		values.put(CONTACTNAME, objBusiness.contactname);
 		values.put(DATE_UPDATED, objBusiness.date_updated);
		if(data != null)
		{
//			if(!(objBusiness.date_updated!=null?objBusiness.date_updated:"").equals(data[0][1].value.toString())){
				return DatabaseHelper.doUpdate(BUSINESSLIST, values, whereClause, null);
//			}else{
//				return 0;
//			}
		}
		else
		{
	 		return DatabaseHelper.doInsert(BUSINESSLIST, values);
		}
	}
	
	public long InsertUserBusiness(Business objBusiness)
	{
		String whereClause = BUSINESSID+EQUALLY+objBusiness.businessid;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+USERID+COMMA+DATEUPDATED+FROM+USERFAVORITEBUSINESS+WHERE+whereClause); 
		ContentValues values = new ContentValues();
		values.put(USERID, AppConstants.USER_ID);
 		values.put(BUSINESSID,objBusiness.businessid);
 		values.put(BUSINESSNAME, objBusiness.businessname);
 		values.put(USERADDEDBUSINESSID,objBusiness.useraddedbusinessid);
 		values.put(DATEUPDATED,objBusiness.date_updated);
		if(data != null && data[0][0].value != null)
		{
			if(!objBusiness.date_updated.equals(data[0][1].value.toString())){
				return DatabaseHelper.doUpdate(USERFAVORITEBUSINESS, values, whereClause, null);
			}else{
				return 0;
			}
		}
		else
		{
	 		return DatabaseHelper.doInsert(USERFAVORITEBUSINESS, values);
		}
	}
	
	
	public boolean isBusinessIsInUserFavorites(int businessId, int userId){
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+ALLFIELDS+FROM+USERFAVORITEBUSINESS+WHERE+BUSINESSID+EQUALLY+businessId+AND+USERID+EQUALLY+userId);
		if(data!=null){
			return (data.length!=0);
		}else{
			return false;
		}
	}

	public List<Business> getUserFavouriteBusiness(int UserId,String strSearchKeyWord)
	{

		DictionaryEntry[][] data=null;
		if(strSearchKeyWord.equals(""))
			data = DatabaseHelper.get(SELECT+BUSINESSID+COMMA+BUSINESSNAME+COMMA+USERADDEDBUSINESSID+COMMA+DATEUPDATED+FROM+USERFAVORITEBUSINESS+WHERE+USERID+EQUALLY+UserId);
		else
			data = DatabaseHelper.get(SELECT+BUSINESSID+COMMA+BUSINESSNAME+COMMA+USERADDEDBUSINESSID+COMMA+DATEUPDATED+FROM+USERFAVORITEBUSINESS+WHERE+BUSINESSNAME+LIKE+LEFT_LIKE+strSearchKeyWord+RIGHT_LIKE+AND+USERID+EQUALLY+UserId);
		
		List<Business> result  = new ArrayList<Business>();
		
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					Business objBusiness = new Business();
					objBusiness.businessid 	= (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objBusiness.businessname 	= (data[i][1].value!=null?data[i][1].value.toString():"");
					objBusiness.useraddedbusinessid= (data[i][2].value!=null?Integer.parseInt(data[i][2].value.toString()):0);
					objBusiness.date_updated 	= (data[i][3].value!=null?data[i][3].value.toString():"");
					result.add(objBusiness);	
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}
	
	public List<UserLocation> getUserLocation()
	{
		// TODO for day - startdate would be 04-Apr-2011 00:00:00 and  end date would be 04-Apr-2011 23:59:59
		//SELECT * FROM tblAppointments where AppointmentTime between  '04-Apr-2011 00:00:00' and '15-Apr-2011 23:59:59' 
		String flag="true";
		DictionaryEntry[][] data=null;
			data = DatabaseHelper.get(SELECT+CITY+COMMA+ZIPCODE+COMMA+COUNTRY+COMMA+GEOPOINT+FROM+USERLOCATION+WHERE+ISPRIMARY+EQUALLY+QUOTE+flag+QUOTE+AND+USERID+EQUALLY+AppConstants.USER_ID);
		
			List<UserLocation> result  = new ArrayList<UserLocation>();
		
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					UserLocation objUserLoc = new UserLocation();
					objUserLoc.city = (data[i][0].value!=null?data[i][0].value.toString():"");
					objUserLoc.zipcode = (data[i][1].value!=null?data[i][1].value.toString():"");
					objUserLoc.country = (data[i][2].value!=null?data[i][2].value.toString():"");
					objUserLoc.geocode=(data[i][3].value!=null?data[i][3].value.toString():"");
					result.add(objUserLoc);	
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}
	
//	public boolean delete(UserAccount objUserAccount)
//	{
//
//		return DatabaseHelper.deleteAllDataFromTable("tblCategory where CategoryId="+objUserAccount.id);
//		
//	}

	public long insertUserFavorites(int userId, int eventId,int userAddedEventId)
	{
		String whereClause = EVENTID+EQUALLY+QUOTE+eventId+QUOTE+AND+USERID+EQUALLY+AppConstants.USER_ID;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+ALLFIELDS+FROM+USERFAVORITE+WHERE+whereClause); 
		ContentValues values = new ContentValues();
		values.put(USERID, AppConstants.USER_ID);
		values.put(EVENTID, eventId);
		values.put(USERADDEDEVENTID, eventId);
		if(data != null && data[0][0].value != null)
		{
			return DatabaseHelper.doUpdate(USERFAVORITE, values, whereClause, null);
		}
		else
		{
			return DatabaseHelper.doInsert(USERFAVORITE, values);
		}
	}

	public boolean deleteUserFavorites(int userId, int eventId)
	{
 		return (DatabaseHelper.deleteAllDataFromTable(USERFAVORITE+WHERE+EVENTID+EQUALLY+eventId+AND+USERID+EQUALLY+userId) & DatabaseHelper.deleteAllDataFromTable(ATTENDING+WHERE+EVENTID+EQUALLY+eventId+AND+USER_ID+EQUALLY+userId));
	}

	public boolean deleteUserFavoritesBusiness(int userId, int businessId)
	{
	 	return DatabaseHelper.deleteAllDataFromTable(USERFAVORITEBUSINESS+WHERE+BUSINESSID+EQUALLY+businessId+AND+USERID+EQUALLY+userId);
	}

	public List<FavoriteEvent> getUserFavorite(int userID)
	{
		List<FavoriteEvent> result  = new ArrayList<FavoriteEvent>();
		DictionaryEntry data [][]= DatabaseHelper.get(SELECT+EVENTID+COMMA+USERADDEDEVENTID+FROM+USERFAVORITE+WHERE+USERID+EQUALLY+userID);
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					FavoriteEvent objEventDetails = new FavoriteEvent();
					objEventDetails.eventId =(data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objEventDetails.userAddedEventId =(data[i][1].value!=null?Integer.parseInt(data[i][1].value.toString()):0);
					result.add(objEventDetails);	
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
	return result;
	}

	public long InsertAllGa2ooUsers(UserAccount objUserAccount)
	{
		String whereClause = USER_ID+EQUALLY+objUserAccount.id;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+USER_ID+COMMA+DATE_UPDATED+FROM+GA2OOUSERLIST+WHERE+whereClause);
		ContentValues values = new ContentValues();
		values.put(USER_ID, objUserAccount.id);
		values.put(USER_NAME, objUserAccount.username);
		values.put(EMAIL, objUserAccount.email);
		values.put(FIRST_NAME, objUserAccount.firstname);
		values.put(LAST_NAME, objUserAccount.lastname);
		values.put(IS_ACTIVE, objUserAccount.is_active);
		values.put(IS_PUBLIC, objUserAccount.is_public);
		values.put(DATE_UPDATED, objUserAccount.date_updated);
		if(data != null && data[0][0].value != null)
		{
			if(!objUserAccount.date_updated.equals(data[0][1].value.toString())){
				return DatabaseHelper.doUpdate(GA2OOUSERLIST, values, whereClause, null);
			}else{
				return 0;
			}
		}
		else
		{
			return DatabaseHelper.doInsert(GA2OOUSERLIST, values);
		}
	}

	public List<FavoriteEvent> getAllEventsByCompany(int eventId)
	{
		List<FavoriteEvent> result  = new ArrayList<FavoriteEvent>();
		
		DictionaryEntry [][] data1 = DatabaseHelper.get(SELECT+BUSINESS+FROM+EVENTDETAILS+WHERE+EVENTID+EQUALLY+eventId);
		if(data1 != null)
		businessId=Integer.parseInt(data1[0][0].value.toString());
		
		DictionaryEntry [][] data2 = DatabaseHelper.get(SELECT+EVENTID+FROM+EVENTDETAILS+WHERE+BUSINESS+EQUALLY+businessId); 
		if(data2 != null)
		{						
			for( int i = 0; i<data2.length; i++)
			{		
				try
				{
					FavoriteEvent objEventDetails = new FavoriteEvent();
					objEventDetails.eventId =(data2[i][0].value!=null?Integer.parseInt(data2[i][0].value.toString()):0);
					result.add(objEventDetails);	
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
	return result;
	}

	public boolean updateUser(List<com.winit.ga2oo.objects.UpdateProfile> vctUpadateProfile,String flag)
	{
		ContentValues updatedValues=new ContentValues();
		if(flag.equals(PROFILE))
		{
			updatedValues.put(USERNAME, vctUpadateProfile.get(0).strUserName);
			updatedValues.put(EMAIL, vctUpadateProfile.get(0).strEmail);
			updatedValues.put(FIRSTNAME, vctUpadateProfile.get(0).strFName);
			updatedValues.put(LASTNAME, vctUpadateProfile.get(0).strLName);
		}
		else if(flag.equals(ACCOUNT))
		{
			updatedValues.put(PASSWORD, vctUpadateProfile.get(0).strPassword);
			updatedValues.put(ISCALENDERSHARED, vctUpadateProfile.get(0).strVisibleMode);
		}

			DatabaseHelper.doUpdate(USER, updatedValues,USERID+EQUALLY+"?", new String[] {Integer.toString(AppConstants.USER_ID)});
		
		return true;
	}
	
	public List<UserAccount> serchGa2ooUser(String serchKeyWord)
	{
		List<UserAccount> result  = new ArrayList<UserAccount>();
		  
		  DictionaryEntry data [][]= DatabaseHelper.get(SELECT+USER_ID+COMMA+FIRST_NAME+FROM+GA2OOUSERLIST+WHERE+FIRST_NAME+LIKE+LEFT_LIKE+serchKeyWord+RIGHT_LIKE+AND+USER_ID+
				  										NOT+IN+"("+SELECT+FRIENDID+FROM+FRIENDS+")"+ORDER_BY+FIRST_NAME);
			if(data != null)
			{						
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						UserAccount objuserAccount = new UserAccount();
						objuserAccount.id 		=(data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
						objuserAccount.firstname =(data[i][1].value!=null?data[i][1].value.toString():"");
						result.add(objuserAccount);	
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
		  
		  return result;
	}
	
	public List<Notifications> getUserNotifications()
	{
		List<Notifications> result  = new ArrayList<Notifications>();
		  
		  DictionaryEntry data [][]= DatabaseHelper.get(SELECT+ALLFIELDS+FROM+USERNOTIFICATIONS+WHERE+ISREAD+EQUALLY+QUOTE+"false"+QUOTE);
			if(data != null)
			{						
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						Notifications objNotifications = new Notifications();
						objNotifications.notificationId 		=	(data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
						objNotifications.senderId 				=	(data[i][1].value!=null?Integer.parseInt(data[i][1].value.toString()):0);
						objNotifications.strNotificationType 	=	(data[i][2].value!=null?data[i][2].value.toString():"");
						objNotifications.strSenderName 			=	(data[i][3].value!=null?data[i][3].value.toString():"");
						objNotifications.friendRequestId 		=	(data[i][4].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
						objNotifications.eventId 				=	(data[i][5].value!=null?Integer.parseInt(data[i][5].value.toString()):0);
						objNotifications.strEventName 			=	(data[i][6].value!=null?data[i][6].value.toString():"");
						objNotifications.strText 				=	(data[i][7].value!=null?data[i][7].value.toString():"");
						objNotifications.strIsRead 				=	(data[i][8].value!=null?data[i][8].value.toString():"");
						objNotifications.strDateCreated 		=	(data[i][9].value!=null?data[i][9].value.toString():"");
						
						result.add(objNotifications);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
		  return result;
	}
	
	public List<Notifications> getUserFilteredNotifications(String filterText){
		List<Notifications> result  = new ArrayList<Notifications>();
		  
		  DictionaryEntry data [][]= DatabaseHelper.get(SELECT+ALLFIELDS+FROM+USERNOTIFICATIONS+WHERE+ISREAD+EQUALLY+QUOTE+"false"+QUOTE+
				  						AND+"("+SENDERNAME+LIKE+LEFT_LIKE+filterText+RIGHT_LIKE+OR+EVENTNAME+LIKE+LEFT_LIKE+filterText+RIGHT_LIKE+
				  						OR+TEXT+LIKE+LEFT_LIKE+filterText+RIGHT_LIKE+")");
			if(data != null)
			{						
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						Notifications objNotifications = new Notifications();
						objNotifications.notificationId 		=	(data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
						objNotifications.senderId 				=	(data[i][1].value!=null?Integer.parseInt(data[i][1].value.toString()):0);
						objNotifications.strNotificationType 	=	(data[i][2].value!=null?data[i][2].value.toString():"");
						objNotifications.strSenderName 			=	(data[i][3].value!=null?data[i][3].value.toString():"");
						objNotifications.friendRequestId 		=	(data[i][4].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
						objNotifications.eventId 				=	(data[i][5].value!=null?Integer.parseInt(data[i][5].value.toString()):0);
						objNotifications.strEventName 			=	(data[i][6].value!=null?data[i][6].value.toString():"");
						objNotifications.strText 				=	(data[i][7].value!=null?data[i][7].value.toString():"");
						objNotifications.strIsRead 				=	(data[i][8].value!=null?data[i][8].value.toString():"");
						objNotifications.strDateCreated 		=	(data[i][9].value!=null?data[i][9].value.toString():"");
						result.add(objNotifications);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
		  return result;
	}
	
	public boolean insertUserNotifications(Notifications objUserNotifications)
	{
//		String whereClause = "NOTIFICATIONID="+objUserNotifications.notificationId;
//		DictionaryEntry [][] data = DatabaseHelper.get("select ISREAD from "+USERNOTIFICATIONS+"  where "+whereClause); 
		ContentValues values = new ContentValues();
		values.put(NOTIFICATIONID, objUserNotifications.notificationid);
 		values.put(SENDERID,objUserNotifications.sender);
 		values.put(NOTIFICATIONTYPE, objUserNotifications.notificationtype);
 		values.put(SENDERNAME,objUserNotifications.sendername);
 		values.put(FRIENDREQUESTID,objUserNotifications.friendrequestid);
 		values.put(EVENTID,objUserNotifications.eventid);
 		values.put(EVENTNAME,(objUserNotifications.eventname!=null?objUserNotifications.eventname:""));
 		values.put(TEXT, objUserNotifications.text);
 		values.put(ISREAD, objUserNotifications.is_read);
 		values.put(DATECREATED, objUserNotifications.date_created);
		values.put(NOTIFICATIONID, objUserNotifications.notificationid);
//		if(data != null && data[0][0].value != null)
//		{
//			if("false".equals(objUserNotifications.is_read)){
//				return DatabaseHelper.doUpdate(USERNOTIFICATIONS, values, whereClause, null);
//			}else{
//				return 0;
//			}
//		}
//		else
//		{
	 		DatabaseHelper.doInsert(USERNOTIFICATIONS, values);
	 		return true;
//		}
	}
	
	public boolean deleteUserNotifications(){
		return DatabaseHelper.deleteAllDataFromTable(USERNOTIFICATIONS2);
	}
	
	public boolean updateUserNotificationTable(int notificationId)
	{
 			if(notificationId!=0)
 			{
 				return DatabaseHelper.doUpdate(UPDATE+USERNOTIFICATIONS+SET+ISREAD+EQUALLY+QUOTE+"true"+QUOTE+WHERE+USERID+EQUALLY+AppConstants.USER_ID+AND+NOTIFICATIONID+EQUALLY+notificationId);
 			}
		return true;
	}
	
	public boolean deleteInboxValues()
	{
		return DatabaseHelper.deleteAllDataFromTable(USERINBOX);
	}
	
	public boolean InsertInUserInbox(UserRecommendationObject objUserRecommendationObject)
	{
//		DictionaryEntry [][] data = DatabaseHelper.get("select ISREAD from "+USERINBOX+" where SENDERID="+objUserRecommendationObject.fromid+" AND USERID ="+AppConstants.USER_ID+" AND "); 
//		if(data != null && data[0][0].value != null)
//		{
//			return true;
//		}
//		else
//		{
			ContentValues values = new ContentValues();
			values.put(USERID, AppConstants.USER_ID);
			values.put(SENDERID, objUserRecommendationObject.fromid);
	 		values.put(SENDERNAME,objUserRecommendationObject.fromname);
	 		values.put(THREAD_ID, objUserRecommendationObject.thread_id);
	 		values.put(SUBJECT,objUserRecommendationObject.subject);
	 		values.put(MESSAGE,objUserRecommendationObject.message);
	 		values.put(GAEVENTID,objUserRecommendationObject.gaeventid);
	 		values.put(GAEVENTNAME,objUserRecommendationObject.gaeventname);
	 		values.put(DATECREATED, objUserRecommendationObject.date_created);
	 		values.put(ISREAD, objUserRecommendationObject.is_read);
	 		DatabaseHelper.doInsert(USERINBOX, values);
	 		return true;
//		}
	}

	public List<UserRecommendationObject> getUserInboxMessage()
	{
		List<UserRecommendationObject> result  = new ArrayList<UserRecommendationObject>();
		  
		  DictionaryEntry data [][]= DatabaseHelper.get(SELECT+DISTINCT+ALLFIELDS+FROM+USERINBOX+WHERE+USERID+EQUALLY+AppConstants.USER_ID);
			if(data != null)
			{						
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						UserRecommendationObject objUserRecommendation 	= new UserRecommendationObject();
						objUserRecommendation.fromid 					=	(data[i][1].value!=null?Integer.parseInt(data[i][1].value.toString()):0);
						objUserRecommendation.fromname				=	(data[i][2].value!=null?data[i][2].value.toString():"");
						objUserRecommendation.thread_id			 	=	(data[i][3].value!=null?data[i][3].value.toString():"");
						objUserRecommendation.subject 				=	(data[i][4].value!=null?data[i][4].value.toString():"");
						objUserRecommendation.message 				=	(data[i][5].value!=null?data[i][5].value.toString():"");
						objUserRecommendation.gaeventid 			=	(data[i][6].value!=null?data[i][6].value.toString():"");
						objUserRecommendation.gaeventname 			=	(data[i][7].value!=null?data[i][7].value.toString():"");
						objUserRecommendation.date_created 			=	(data[i][8].value!=null?data[i][8].value.toString():"");
						objUserRecommendation.is_read 				=	(data[i][9].value!=null?data[i][9].value.toString():"");
						
						result.add(objUserRecommendation);	
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
		  
		  return result;
	}

	public List<UserRecommendationObject> getFilteredUserInboxMessage(String filterText)
	{
		List<UserRecommendationObject> result  = new ArrayList<UserRecommendationObject>();
		  
		  DictionaryEntry data [][]= DatabaseHelper.get(SELECT+DISTINCT+ALLFIELDS+FROM+USERINBOX+WHERE+USERID+EQUALLY+AppConstants.USER_ID+
				  								AND+"("+SENDERNAME+LIKE+LEFT_LIKE+filterText+RIGHT_LIKE+OR+SUBJECT+LIKE+LEFT_LIKE+filterText+RIGHT_LIKE+
				  								OR+GAEVENTNAME+LIKE+LEFT_LIKE+filterText+RIGHT_LIKE+")");
			if(data != null)
			{						
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						UserRecommendationObject objUserRecommendation 	= new UserRecommendationObject();
						objUserRecommendation.fromid 					=	(data[i][1].value!=null?Integer.parseInt(data[i][1].value.toString()):0);
						objUserRecommendation.fromname				=	(data[i][2].value!=null?data[i][2].value.toString():"");
						objUserRecommendation.thread_id			 	=	(data[i][3].value!=null?data[i][3].value.toString():"");
						objUserRecommendation.subject 				=	(data[i][4].value!=null?data[i][4].value.toString():"");
						objUserRecommendation.message 				=	(data[i][5].value!=null?data[i][5].value.toString():"");
						objUserRecommendation.gaeventid 			=	(data[i][6].value!=null?data[i][6].value.toString():"");
						objUserRecommendation.gaeventname 			=	(data[i][7].value!=null?data[i][7].value.toString():"");
						objUserRecommendation.date_created 			=	(data[i][8].value!=null?data[i][8].value.toString():"");
						objUserRecommendation.is_read 				=	(data[i][9].value!=null?data[i][9].value.toString():"");
						
						result.add(objUserRecommendation);	
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
		  
		  return result;
	}
	
	
//	public void clearData()
//	{
//		
// 		DatabaseHelper.deleteAllDataFromTable("USER");
// 		DatabaseHelper.deleteAllDataFromTable("EVENTDETAILS");
// 		DatabaseHelper.deleteAllDataFromTable("ATTENDING");
 //		DatabaseHelper.deleteAllDataFromTable("USERFAVORITE");
// 		DatabaseHelper.deleteAllDataFromTable("USERLOCATION");
// 		DatabaseHelper.deleteAllDataFromTable("USERNOTIFICATIONS");
//		DatabaseHelper.deleteAllDataFromTable("EVENTLIST");
// 		DatabaseHelper.deleteAllDataFromTable("EVENT_LOCATION");
// 		DatabaseHelper.deleteAllDataFromTable("FRIENDS");
// 		DatabaseHelper.deleteAllDataFromTable("USERFAVORITEBUSINESS");
///	}
	
//	public boolean clearGa2ooUserList()
//	{
//		return DatabaseHelper.deleteAllDataFromTable(GA2OOUSERLIST);
//	}

}
