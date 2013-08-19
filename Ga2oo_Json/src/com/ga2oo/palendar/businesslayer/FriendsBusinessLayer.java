package com.ga2oo.palendar.businesslayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ga2oo.palendar.common.AppConstants;
import com.ga2oo.palendar.databaseaccess.DatabaseHelper;
import com.ga2oo.palendar.databaseaccess.DictionaryEntry;
import com.ga2oo.palendar.objects.Friend;
import com.ga2oo.palendar.objects.UserFriend;

public class FriendsBusinessLayer
{
	private static final String FRIENDIMAGEISUPDATED = "FRIENDIMAGEISUPDATED";
	private static final String FRIENDIMAGEDATEUPDATED = "FRIENDIMAGEDATEUPDATED";
	private static final String FRIENDIMAGEID = "FRIENDIMAGEID";
	private static final String SET = " SET ";
	private static final String UPDATE = "UPDATE ";
	private static final String EVENTID2 = "EVENTID";
	private static final String COUNT = " COUNT ";
	private static final String OR = " OR ";
	private static final String RIGHT_LIKE = "%'";
	private static final String LEFT_LIKE = "'%";
	private static final String LIKE = " LIKE ";
	private static final String FALSE = "false";
	private static final String COUNTRY = "COUNTRY";
	private static final String STATE = "STATE";
	private static final String CITY = "CITY";
	private static final String ADDRESS = "ADDRESS";
	private static final String FRIENDIMAGE = "FRIENDIMAGE";
	private static final String RIGHT_BRACKET = ")";
	private static final String LEFT_BARCKET = "(";
	private static final String IN = " IN ";
	private static final String _1 = "1";
	private static final String STATUS = "STATUS";
	private static final String QUOTE = "'";
	private static final String USER_ID = "USER_ID";
	private static final String DOT = ".";
	private static final String ISPUBLIC = "ISPUBLIC";
	private static final String ISCALENDERSHARED = "ISCALENDERSHARED";
	private static final String PASSWORD = "PASSWORD";
	private static final String LASTNAME = "LASTNAME";
	private static final String EMAIL = "EMAIL";
	private static final String FIRSTNAME = "FIRSTNAME";
	private static final String GENDER = "GENDER";
	private static final String DEVICEID = "DEVICEID";
	private static final String ZIPCODE = "ZIPCODE";
	private static final String USERIMAGE = "USERIMAGE";
	private static final String ISACTIVE = "ISACTIVE";
	private static final String USERNAME = "USERNAME";
	private static final String BIRTHDAY = "BIRTHDAY";
	private static final String ALLELEMENTS = " * ";
	private static final String AND = " AND ";
	private static final String USERADDEDFRIENDID = "USERADDEDFRIENDID";
	private static final String EQUALLY = "=";
	private static final String USERID = "USERID";
	private static final String WHERE = " WHERE ";
	private static final String FROM = " FROM ";
	private static final String DATEUPDATED = "DATEUPDATED";
	private static final String COMMA = " , ";
	private static final String FRIENDID = "FRIENDID";
	private static final String DISTINCT = " DISTINCT ";
	private static final String SELECT = "SELECT ";
	private static final String FRIENDS="FRIENDS";
	private static final String FRIENDINFO="FRIENDINFO";
	private static final String  ATTENDING="ATTENDING";
	private static final String LOGTAG = "FriendsBusinessLayer";
	private static final String DATEFORMAT = "yyyy MM dd";
	

	public List<Friend> getAllFriends(int USER_ID)
	{
		// TODO for day - startdate would be 04-Apr-2011 00:00:00 and  end date would be 04-Apr-2011 23:59:59
		//SELECT * FROM tblAppointments where AppointmentTime between  '04-Apr-2011 00:00:00' and '15-Apr-2011 23:59:59' 
		
		DictionaryEntry[][] data=null;
			data = DatabaseHelper.get(SELECT+DISTINCT+FRIENDID+COMMA+DATEUPDATED+FROM+FRIENDS+WHERE+USERID+EQUALLY+USER_ID);//WHERE USERID="+USER_ID
		
	   List<Friend> result = new ArrayList<Friend>();
		
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					Friend objFriend = new Friend();
					objFriend.friendid = (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
//					objEvent.EventImage = (data[i][1].value!=null?Integer.parseInt(data[i][1].value.toString());
					objFriend.date_updated = (data[i][1].value!=null?data[i][1].value.toString():"");
					result.add(objFriend);	
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}
	
	public List<Friend> getAllNewFriends(int USER_ID)
	{
		// TODO for day - startdate would be 04-Apr-2011 00:00:00 and  end date would be 04-Apr-2011 23:59:59
		//SELECT * FROM tblAppointments where AppointmentTime between  '04-Apr-2011 00:00:00' and '15-Apr-2011 23:59:59' 
		
		DictionaryEntry[][] data=null;
			data = DatabaseHelper.get(SELECT+DISTINCT+FRIENDID+COMMA+DATEUPDATED+FROM+FRIENDS );//WHERE USERID="+USER_ID+" AND FRIENDID NOT IN (SELECT USERID FROM FRIENDINFO)"
		
			   List<Friend> result = new ArrayList<Friend>();
		
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					Friend objFriend = new Friend();
					objFriend.friendid = (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
//					objEvent.EventImage = Integer.parseInt(data[i][1].value.toString());
					objFriend.date_updated = (data[i][1].value!=null?data[i][1].value.toString():"");
					result.add(objFriend);	
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}

	public boolean deleteFriend(int userFriendAddedId)
	{
		return DatabaseHelper.deleteAllDataFromTable(FRIENDS+WHERE+USERADDEDFRIENDID+EQUALLY+userFriendAddedId+AND+USERID+EQUALLY+AppConstants.USER_ID);
	}
	
	public boolean deleteAlFriend(){
		return DatabaseHelper.deleteAllDataFromTable(FRIENDS);
	}
	
	public boolean Insert(UserFriend objUserFriend)
	{ 
		String whereClause = USERID+EQUALLY+objUserFriend.id;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+ALLELEMENTS+FROM+FRIENDINFO+WHERE+whereClause);
		ContentValues values = new ContentValues();
 		values.put(USERID, objUserFriend.id);
 		values.put(BIRTHDAY,objUserFriend.birthday);
 		values.put(USERNAME, objUserFriend.username);
 		values.put(ISACTIVE,objUserFriend.is_active);
 		values.put(USERIMAGE,(objUserFriend.imagesrc!=null?objUserFriend.imagesrc:AppConstants.NOIMAGE));
 		values.put(ZIPCODE,objUserFriend.zipcode);
// 		values.put("INVISIBLEMODE",objUserAccount.i);
 		values.put(DEVICEID,objUserFriend.deviceid);
 		values.put(GENDER, objUserFriend.gender);
 		values.put(FIRSTNAME, objUserFriend.firstname);
 		values.put(EMAIL, objUserFriend.email);
 		values.put(LASTNAME, objUserFriend.lastname);
 		values.put(PASSWORD, objUserFriend.password);
 		values.put(ISCALENDERSHARED, objUserFriend.is_calendar_shared);
 		values.put(ISPUBLIC, objUserFriend.is_public);
		if(data != null && data[0][0].value != null)
		{
			DatabaseHelper.doUpdate(FRIENDINFO, values, whereClause, null);
			return false;
		}
		else
		{
	 		DatabaseHelper.doInsert(FRIENDINFO, values);
	 		return true;
		}
	}
	
	public List<UserFriend> getUsrFrendAttendingEvents(String strEventId)
	{
		
		List<UserFriend> result = new ArrayList<UserFriend>();

		String strlocalFriendId="";
		DictionaryEntry[][] data=null;
		data = DatabaseHelper.get(SELECT+ATTENDING+DOT+USER_ID+COMMA+FRIENDS+DOT+FRIENDID+FROM+ATTENDING+COMMA+FRIENDS+
								WHERE+ATTENDING+DOT+EVENTID2+EQUALLY+QUOTE+strEventId+QUOTE+AND+ATTENDING+DOT+USER_ID+EQUALLY+FRIENDS+DOT+FRIENDID+
								AND+FRIENDS+DOT+USERID+EQUALLY+AppConstants.USER_ID+AND+FRIENDS+DOT+STATUS+EQUALLY+_1);
			
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
			
					if(i<data.length-1)
					{
						strlocalFriendId=data[i][0].value.toString()+","+strlocalFriendId;
					}
					else
					{
						strlocalFriendId=strlocalFriendId+data[i][0].value.toString();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		if(!strlocalFriendId.equals(""))
		{
			DictionaryEntry[][] data2=null;
			data2 = DatabaseHelper.get(SELECT+FRIENDID+COMMA+FIRSTNAME+COMMA+LASTNAME+COMMA+FRIENDIMAGE+COMMA+FRIENDIMAGEID+COMMA+FRIENDIMAGEDATEUPDATED+COMMA+FRIENDIMAGEISUPDATED+FROM+FRIENDS+WHERE+FRIENDID+IN+LEFT_BARCKET+strlocalFriendId+RIGHT_BRACKET);
			if(data2 != null)
				{						
					for( int i = 0; i<data2.length; i++)
						{		
								try
								{
									
									UserFriend objUserAct = new UserFriend();
									objUserAct.ID1 = (data2[i][0].value!=null?Integer.parseInt(data2[i][0].value.toString()):0);
									objUserAct.FirstName1=(data2[i][1].value!=null?data2[i][1].value.toString():"");
									objUserAct.LastName1=(data2[i][2].value!=null?data2[i][2].value.toString():"");
									objUserAct.Image1 = (data2[i][3].value!=null?data2[i][3].value.toString():"");
									objUserAct.image_id =  (data2[i][4].value!=null?data2[i][4].value.toString():"");
									objUserAct.image_updated =  (data2[i][5].value!=null?data2[i][5].value.toString():"0");
									objUserAct.isImageUpdated =  (data2[i][6].value!=null?data2[i][6].value.toString():"false");
									objUserAct.noOfFriends = data2.length;
									result.add(objUserAct);	
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
						}
				}
		}

		return result;
	}
	
	public List<UserFriend> getUserFriendInformation(String strFriendsId)
	{
		// TODO for day - startdate would be 04-Apr-2011 00:00:00 and  end date would be 04-Apr-2011 23:59:59
		//SELECT * FROM tblAppointments where AppointmentTime between  '04-Apr-2011 00:00:00' and '15-Apr-2011 23:59:59' 

		List<UserFriend> result = new ArrayList<UserFriend>();
		   
		DictionaryEntry[][] data=null;
		if(!strFriendsId.equals(""))
			data = DatabaseHelper.get(SELECT+FRIENDID+COMMA+FIRSTNAME+COMMA+LASTNAME+COMMA+FRIENDIMAGE+COMMA+ISCALENDERSHARED+COMMA+EMAIL+COMMA+USERADDEDFRIENDID+COMMA+
									ADDRESS+COMMA+CITY+COMMA+STATE+COMMA+ZIPCODE+COMMA+COUNTRY+COMMA+FRIENDIMAGEID+COMMA+FRIENDIMAGEDATEUPDATED+COMMA+FRIENDIMAGEISUPDATED+FROM+FRIENDS+WHERE+FRIENDID+EQUALLY+QUOTE+strFriendsId+QUOTE+
									AND+USERID+EQUALLY+AppConstants.USER_ID);
		else
			data = DatabaseHelper.get(SELECT+FRIENDID+COMMA+FIRSTNAME+COMMA+LASTNAME+COMMA+FRIENDIMAGE+COMMA+ISCALENDERSHARED+COMMA+EMAIL+COMMA+USERADDEDFRIENDID+COMMA+
									ADDRESS+COMMA+CITY+COMMA+STATE+COMMA+ZIPCODE+COMMA+COUNTRY+COMMA+FRIENDIMAGEID+COMMA+FRIENDIMAGEDATEUPDATED+COMMA+FRIENDIMAGEISUPDATED+FROM+FRIENDS+WHERE+USERID+EQUALLY+AppConstants.USER_ID+
									AND+STATUS+EQUALLY+_1);
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					UserFriend objUserAct = new UserFriend();
					objUserAct.id = (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objUserAct.firstname=(data[i][1].value!=null?data[i][1].value.toString():"");
					objUserAct.lastname=(data[i][2].value!=null?data[i][2].value.toString():"");
					objUserAct.imagesrc = (data[i][3].value!=null?data[i][3].value.toString():AppConstants.NOIMAGE);
					objUserAct.is_calendar_shared = (data[i][4].value!=null?data[i][4].value.toString():FALSE);
					objUserAct.email = (data[i][5].value!=null?data[i][5].value.toString():"");
					objUserAct.userFriendAddedId=(data[i][6].value!=null?Integer.parseInt(data[i][6].value.toString()):0);
					objUserAct.Address=(data[i][7].value!=null?data[i][7].value.toString():" ");
					objUserAct.City = (data[i][8].value!=null?data[i][8].value.toString():" ");
					objUserAct.State = (data[i][9].value!=null?data[i][9].value.toString():" ");
					objUserAct.zipcode = (data[i][10].value!=null?data[i][10].value.toString():" ");
					objUserAct.Country = (data[i][11].value!=null?data[i][11].value.toString():" ");
					objUserAct.image_id =  (data[i][12].value!=null?data[i][12].value.toString():"");
					objUserAct.image_updated =  (data[i][13].value!=null?data[i][13].value.toString():"0");
					objUserAct.isImageUpdated =  (data[i][14].value!=null?data[i][14].value.toString():"false");
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
	
	public List<UserFriend> getFilteredUserFriendInformation(String filterStr)
	{
		List<UserFriend> result = new ArrayList<UserFriend>();
		   
		DictionaryEntry[][] data=null;
			data = DatabaseHelper.get(SELECT+FRIENDID+COMMA+FIRSTNAME+COMMA+LASTNAME+COMMA+FRIENDIMAGE+COMMA+ISCALENDERSHARED+COMMA+EMAIL+COMMA+USERADDEDFRIENDID+COMMA+
									ADDRESS+COMMA+CITY+COMMA+STATE+COMMA+ZIPCODE+COMMA+COUNTRY+COMMA+FRIENDIMAGEID+COMMA+FRIENDIMAGEDATEUPDATED+COMMA+FRIENDIMAGEISUPDATED+FROM+FRIENDS+WHERE+USERID+EQUALLY+AppConstants.USER_ID+
									AND+STATUS+EQUALLY+_1+AND+LEFT_BARCKET+FIRSTNAME+LIKE+LEFT_LIKE+filterStr+RIGHT_LIKE+OR+LASTNAME+LIKE+LEFT_LIKE+filterStr+RIGHT_LIKE+RIGHT_BRACKET);
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					UserFriend objUserAct = new UserFriend();
					objUserAct.id = (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objUserAct.firstname=(data[i][1].value!=null?data[i][1].value.toString():"");
					objUserAct.lastname=(data[i][2].value!=null?data[i][2].value.toString():"");
					objUserAct.imagesrc = (data[i][3].value!=null?data[i][3].value.toString():AppConstants.NOIMAGE);
					objUserAct.is_calendar_shared = (data[i][4].value!=null?data[i][4].value.toString():FALSE);
					objUserAct.email = (data[i][5].value!=null?data[i][5].value.toString():"");
					objUserAct.userFriendAddedId=(data[i][6].value!=null?Integer.parseInt(data[i][6].value.toString()):0);
					objUserAct.Address=(data[i][7].value!=null?data[i][7].value.toString():" ");
					objUserAct.City = (data[i][8].value!=null?data[i][8].value.toString():" ");
					objUserAct.State = (data[i][9].value!=null?data[i][9].value.toString():" ");
					objUserAct.zipcode = (data[i][10].value!=null?data[i][10].value.toString():" ");
					objUserAct.Country = (data[i][11].value!=null?data[i][11].value.toString():" ");
					objUserAct.image_id =  (data[i][12].value!=null?data[i][12].value.toString():"");
					objUserAct.image_updated =  (data[i][13].value!=null?data[i][13].value.toString():"0");
					objUserAct.isImageUpdated =  (data[i][14].value!=null?data[i][14].value.toString():"false");
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
	
	public String getAttendingCountByEventId(String eventId){
		DictionaryEntry[][] data=DatabaseHelper.get(SELECT+COUNT+LEFT_BARCKET+USER_ID+RIGHT_BRACKET+FROM+ATTENDING+WHERE+EVENTID2+EQUALLY+eventId);
		String result = data[0][0].value.toString();
		return result;
	}
	
	public List<UserFriend> getAllFriends()
	{
		// TODO for day - startdate would be 04-Apr-2011 00:00:00 and  end date would be 04-Apr-2011 23:59:59
		//SELECT * FROM tblAppointments where AppointmentTime between  '04-Apr-2011 00:00:00' and '15-Apr-2011 23:59:59' 

		List<UserFriend> result = new ArrayList<UserFriend>();
		DictionaryEntry[][] data = DatabaseHelper.get(SELECT+FRIENDID+COMMA+FIRSTNAME+COMMA+LASTNAME+COMMA+FRIENDIMAGE+COMMA+EMAIL+COMMA+FRIENDIMAGEID+COMMA+FRIENDIMAGEDATEUPDATED+COMMA+FRIENDIMAGEISUPDATED+
														FROM+FRIENDS+WHERE+USERID+EQUALLY+AppConstants.USER_ID+AND+STATUS+EQUALLY+_1);
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					UserFriend objUserAct = new UserFriend();
					objUserAct.id = (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objUserAct.firstname=(data[i][1].value!=null?data[i][1].value.toString():"");
					objUserAct.lastname=(data[i][2].value!=null?data[i][2].value.toString():"");
					objUserAct.imagesrc = (data[i][3].value!=null?data[i][3].value.toString():AppConstants.NOIMAGE);
					objUserAct.email = (data[i][4].value!=null?data[i][4].value.toString():"");
					objUserAct.image_id =  (data[i][5].value!=null?data[i][5].value.toString():"");
					objUserAct.image_updated =  (data[i][6].value!=null?data[i][6].value.toString():"0");
					objUserAct.isImageUpdated =  (data[i][7].value!=null?data[i][7].value.toString():"false");
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

	public boolean updateFriendsTable(int status)
	{
		DatabaseHelper.doUpdate(UPDATE+FRIENDS+SET+STATUS+EQUALLY+status+WHERE+USERID+EQUALLY+AppConstants.USER_ID);
		return true;
	}
}
