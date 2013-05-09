package com.winit.ga2oo.businesslayer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.databaseaccess.DatabaseHelper;
import com.winit.ga2oo.databaseaccess.DictionaryEntry;
import com.winit.ga2oo.objects.Attending;
import com.winit.ga2oo.objects.Business;
import com.winit.ga2oo.objects.Category;
import com.winit.ga2oo.objects.EventDates;
import com.winit.ga2oo.objects.EventImages;
import com.winit.ga2oo.objects.EventLocation;
import com.winit.ga2oo.objects.Events;
import com.winit.ga2oo.objects.EventsDetails;
import com.winit.ga2oo.objects.EventsDetailsData;
import com.winit.ga2oo.objects.FavoriteEvent;
import com.winit.ga2oo.objects.Subcategory;

public class EventsBusinessLayer
{
	
	private static final String EVENTIMAGEISUPDATED = "EVENTIMAGEISUPDATED";
	private static final String IMG_WIDTH = "&img_width";
	private static final String IMG_ID = "img_id=";
	private static final String CONTACTNAME = "CONTACTNAME";
	private static final String URL = "URL";
	private static final String CONTACTEMAIL = "CONTACTEMAIL";
	private static final String SUPPORTEMAIL = "SUPPORTEMAIL";
	private static final String PHONE = "PHONE";
	private static final String EIN = "EIN";
	private static final String FAX = "FAX";
	private static final String BUSINESSTYPE = "BUSINESSTYPE";
	private static final String CATEGORY2 = "CATEGORY2";
	private static final String OR = " OR ";
	private static final String ORDER_BY = " ORDER BY ";
	private static final String USERID = "USERID";
	private static final String USERADDEDEVENTID = "USERADDEDEVENTID";
	private static final String DOT = ".";
	private static final String IMAGE = "IMAGE";
	private static final String DATE_CREATED = "DATE_CREATED";
	private static final String SUPP_PHN = "SUPP_PHN";
	private static final String SUPP_EMAIL = "SUPP_EMAIL";
	private static final String EVENT_END_TIME = "EVENT_END_TIME";
	private static final String EVENT_END_DATE = "EVENT_END_DATE";
	private static final String EVENT_EXPIRATION = "EVENT_EXPIRATION";
	private static final String IS_FEATURED = "IS_FEATURED";
	private static final String LIKE_COUNT = "LIKE_COUNT";
	private static final String IMAGE4 = "IMAGE4";
	private static final String IMAGE3 = "IMAGE3";
	private static final String IMAGE2 = "IMAGE2";
	private static final String BIZNAME = "BIZNAME";
	private static final String PRICE = "PRICE";
	private static final String TICKETS = "TICKETS";
	private static final String IMAGE1 = "IMAGE1";
	private static final String BUSINESS = "BUSINESS";
	private static final String EVENT_START_TIME = "EVENT_START_TIME";
	private static final String EVENT_START_DATE = "EVENT_START_DATE";
	private static final String DESCRIPTION = "DESCRIPTION";
	private static final String RIGHT_BRACKET = ")";
	private static final String LEFT_BRACKET = "(";
	private static final String IN = " IN ";
	private static final String CATEGORY1 = "CATEGORY1";
	private static final String LIKE_RIGHT = "%'";
	private static final String LIKE_LEFT = "'%";
	private static final String LIKE = " LIKE ";
	private static final String QUOTE = "'";
	private static final String NAME = "NAME";
	private static final String ID = "ID";
	private static final String SUBCATEGORYES = "SUBCATEGORYES";
	private static final String MAINCATEGORYNAME = "MAINCATEGORYNAME";
	private static final String DATE_UPDATED = "DATE_UPDATED";
	private static final String MAINCATEGORYID = "MAINCATEGORYID";
	private static final String ALLFIELDS = " * ";
	private static final String AND = " AND ";
	private static final String EQUALLY = "=";
	private static final String USER_ID = "USER_ID";
	private static final String ZIP = "ZIP";
	private static final String COUNTRY = "COUNTRY";
	private static final String CITY = "CITY";
	private static final String ADDRESS = "ADDRESS";
	private static final String STATE = "STATE";
	private static final String GEOCODE = "GEOCODE";
	private static final String LOCATION_ID = "LOCATION_ID";
	private static final String EVENT_ID = "EVENT_ID";
	private static final String CATEGORY = "CATEGORY";
	private static final String STATUS = "STATUS";
	private static final String EVENTMAINIMAGE = "EVENTMAINIMAGE";
	private static final String EVENTIMAGE = "EVENTIMAGE";
	private static final String TRUE = "true";
	private static final String BUSINESSID = "BUSINESSID";
	private static final String EVENTPRICE = "EVENTPRICE";
	private static final String ISFEATURED = "ISFEATURED";
	private static final String EVENTSTARTTIME = "EVENTSTARTTIME";
	private static final String EVENTSTARTDATE = "EVENTSTARTDATE";
	private static final String EVENTNAME = "EVENTNAME";
	private static final String WHERE = " WHERE ";
	private static final String FROM = " FROM ";
	private static final String DATEUPDATED = "DATEUPDATED";
	private static final String COMMA = " , ";
	private static final String SELECT = "SELECT ";
	private static final String DATEFORMAT = "yyyy-MM-dd";
	private static final String EVENTID = "EVENTID";
	private final static String LOGTAG = "EventsBusinessLayer";
	private static final String NOIMAGE = "no image";
	private static final String CATEGORYLIST="CATEGORYLIST";
	private static final String EVENTLIST="EVENTLIST";
	private static final String EVENTDETAILS="EVENTDETAILS";
	private static final String EVENT_LOCATION="EVENT_LOCATION";
	private static final String ATTENDING="ATTENDING";
	private static final String USERFAVORITE="USERFAVORITE";
	private static final String BUSINESSLIST="BUSINESSLIST";
	private static final String EVENTIMAGEID = "EVENTIMAGEID";
	private static final String EVENTIMAGEDATEUPDATED="EVENTIMAGEDATEUPDATED";
	
	private Date convertStringToDate(String dateString){
		    SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
		    Date convertedDate;
		    try {
		        convertedDate = dateFormat.parse(dateString);
		    } catch (ParseException e) {
		        e.printStackTrace();
		        return null;
		    }
		    return convertedDate;

		}

	
	//Insert data from web service to EventList
	public long InsertEventList(EventsDetails objEvents)
	{
		String WHEREClause = EVENTID+EQUALLY+objEvents.eventid;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+EVENTID+COMMA+DATEUPDATED+FROM+EVENTLIST+WHERE+WHEREClause); 
		DictionaryEntry [][] data2 = DatabaseHelper.get(SELECT+EVENTIMAGEDATEUPDATED+FROM+EVENTLIST+WHERE+WHEREClause);
		ContentValues values = new ContentValues();
		values.put(EVENTID, objEvents.eventid);	 		
 		values.put(EVENTNAME, objEvents.eventname);
 		values.put(EVENTSTARTDATE, objEvents.event_start_date);	 		
 		values.put(EVENTSTARTTIME, objEvents.event_start_time);
 		values.put(ISFEATURED, objEvents.is_featured);
 		values.put(EVENTPRICE, objEvents.price);
 		values.put(BUSINESSID, ""+objEvents.business);	 		
 		values.put(DATEUPDATED, objEvents.date_updated);
 		if(objEvents.images!=null){
	 		for(int i=0;i<objEvents.images.size();i++){
	 			if(TRUE.equals(objEvents.images.get(i).mainimage)){
	 				values.put(EVENTIMAGE, (objEvents.images!=null?objEvents.images.get(i).imagesrc:NOIMAGE));
	 				if(data2!=null && data2[0][0]!=null){
	 					if(objEvents.images.get(i).image_updated!=null && !"None".equals(objEvents.images.get(i).image_updated)){
	 						Date oldDate = convertStringToDate(data2[0][0].toString());
	 						Date newDate = convertStringToDate(objEvents.images.get(i).image_updated);
	 						if(newDate.after(oldDate)){
	 							values.put(EVENTIMAGEISUPDATED, "true");
	 						}else{
	 							values.put(EVENTIMAGEISUPDATED, "false");
	 						}
	 					}
	 				}else{
	 					values.put(EVENTIMAGEISUPDATED, "false");
	 				}
	 				values.put(EVENTIMAGEDATEUPDATED,  (objEvents.images!=null?objEvents.images.get(i).image_updated:""));
	 				values.put(EVENTIMAGEID,getImageIDfromSrc(objEvents.images.get(i).imagesrc));
	 			}
	 		}
 		}else{
 			values.put(EVENTIMAGE, NOIMAGE);
 		}
 		values.put(EVENTMAINIMAGE, TRUE);
 		values.put(STATUS, objEvents.status);
 		for(int i=0; i<objEvents.categories.size();i++){
 			values.put(CATEGORY+(i+1), objEvents.categories.get(i).maincategory);
 		}
		if(data != null && data[0][0].value != null)
		{
			
			if(!objEvents.date_updated.equals(data[0][1].value.toString())){
				for(int j=0;j<objEvents.attending.size();j++){
					objEvents.attending.get(j).eventID =  objEvents.eventid;
					InsertAttending(objEvents.attending.get(j));
				}
				for(int k=0;k<objEvents.locations.size();k++){
					objEvents.locations.get(k).EventId = objEvents.eventid;
					insertIntoEventLocation(objEvents.locations.get(k));
				}
				return DatabaseHelper.doUpdate(EVENTLIST, values, WHEREClause, null);
				
			}else{
				return 0;
			}
		}
		else
		{
			if(objEvents.attending!=null){
				for(int j=0;j<objEvents.attending.size();j++){
					objEvents.attending.get(j).eventID =  objEvents.eventid;
					InsertAttending(objEvents.attending.get(j));
				}
			}
			if(objEvents.locations!=null){
				for(int k=0;k<objEvents.locations.size();k++){
					objEvents.locations.get(k).EventId = objEvents.eventid;
					insertIntoEventLocation(objEvents.locations.get(k));
				}
			}
	 		return DatabaseHelper.doInsert(EVENTLIST, values);
		
		}
	}
	
	
	private String getImageIDfromSrc(String src){
		String result = null;
		int startIndex = src.indexOf(IMG_ID)+IMG_ID.length();
		int endIndex = src.indexOf(IMG_WIDTH);
		result = src.substring(startIndex, endIndex);
		return result;
	}
	
	public long insertIntoEventLocation(EventLocation objEventLocation)
	{
		String WHEREClause = EVENT_ID+EQUALLY+objEventLocation.EventId;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+EVENT_ID+FROM+EVENT_LOCATION+WHERE+WHEREClause); 
		ContentValues values = new ContentValues();
		values.put(LOCATION_ID, objEventLocation.id);
 		values.put(EVENT_ID,objEventLocation.EventId);
 		values.put(GEOCODE,objEventLocation.geocode);
 		values.put(STATE,objEventLocation.state);
 		values.put(ADDRESS,objEventLocation.address);
 		values.put(CITY,objEventLocation.city);
 		values.put(COUNTRY,objEventLocation.country);
 		values.put(ZIP,objEventLocation.zipcode);
		if(data != null && data[0][0].value != null)
		{
			return DatabaseHelper.doUpdate(EVENT_LOCATION, values, WHEREClause, null);
		}
		else
		{
	 		return DatabaseHelper.doInsert(EVENT_LOCATION, values);
		}
	}

	public long InsertAttending(Attending objAttending){
		
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+ EVENTID+COMMA+USER_ID+FROM+ATTENDING+WHERE+EVENTID+EQUALLY+objAttending.eventID+AND+USER_ID+EQUALLY+objAttending.id);
		ContentValues values = new ContentValues();
		values.put(EVENTID, objAttending.eventID);
		values.put(USER_ID,objAttending.id);
		
		if(data!=null && data[0][0].value!=null){
			return 0;
		}else{	
			return	DatabaseHelper.doInsert(ATTENDING, values);
		}
   }
	
	public long addToAttending(int eventID,int userID){
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+EVENTID+COMMA +USER_ID+FROM+ATTENDING+WHERE+EVENTID+EQUALLY+eventID+AND+USER_ID+EQUALLY+userID);
		ContentValues values = new ContentValues();
		values.put(EVENTID, eventID);
 		values.put(USER_ID,userID);
 		if(data!=null && data[0][0].value!=null){
			return 0;
		}else{
			return DatabaseHelper.doInsert(ATTENDING, values); 		
		}
	}
	
	public boolean isEventAttendedByUser(int eventID, int userID){
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+ALLFIELDS+FROM+ATTENDING+WHERE+EVENTID+EQUALLY+eventID+AND+USER_ID+EQUALLY+userID);
		if(data!=null){
			return (data.length!=0);
		}else{
			return false;
		}
	}
	
	public long insertCategories(Category objCategory)
	{
		String WHEREClause = MAINCATEGORYID+EQUALLY+objCategory.maincategory;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+MAINCATEGORYID+COMMA+DATE_UPDATED+FROM+CATEGORYLIST+WHERE+ WHEREClause); 
		ContentValues values = new ContentValues();
		values.put(MAINCATEGORYID, objCategory.maincategory);
 		values.put(MAINCATEGORYNAME,objCategory.maincategoryname);
 		values.put(DATE_UPDATED, objCategory.date_updated);
 		String subcategoryes = "";
 		for(int i=0;i<objCategory.subcategories.size();i++){
 			subcategoryes+=(objCategory.subcategories.get(i).subcategoryid+",");
 			insertSubcategory(objCategory.subcategories.get(i));
 		}
 		values.put(SUBCATEGORYES,subcategoryes);
		if(data != null && data[0][0].value != null)
		{
			
			if(!objCategory.date_updated.equals(data[0][1].value.toString())){
				return DatabaseHelper.doUpdate(CATEGORYLIST,values,WHEREClause,null);
			}else{
				return 0;
			}
		}
		else
		{
			return DatabaseHelper.doInsert(CATEGORYLIST, values);
		}
	}
	
	public long insertSubcategory(Subcategory subcategoryObj){
		String WHEREClause = ID+EQUALLY+subcategoryObj.subcategoryid;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+ID+COMMA+DATE_UPDATED+FROM+SUBCATEGORYES +WHERE+ WHEREClause); 
		ContentValues values = new ContentValues();
		values.put(ID, subcategoryObj.subcategoryid);
		values.put(DATE_UPDATED, subcategoryObj.date_updated);
		values.put(NAME, subcategoryObj.subcategoryname);
		if(data != null && data[0][0].value != null){
			if(!subcategoryObj.date_updated.equals(data[0][1].value.toString())){
				return DatabaseHelper.doUpdate(SUBCATEGORYES, values, WHEREClause, null);
			}else{
				return 0;
			}
		}else{
			return DatabaseHelper.doInsert(SUBCATEGORYES, values);
		}
		
	}
	
	public List<Events> getAllEvents()
	{
		// TODO for day - startdate would be 04-Apr-2011 00:00:00 and  end date would be 04-Apr-2011 23:59:59
		//SELECT * FROM tblAppointments WHERE AppointmentTime between  '04-Apr-2011 00:00:00' and '15-Apr-2011 23:59:59' 
		DictionaryEntry[][] data=DatabaseHelper.get(SELECT+EVENTID+FROM+EVENTLIST);
			
		
			List<Events> result = new ArrayList<Events>();
		if(data != null)
		{	
			for( int i = 0; i<data.length; i++)
			{	
				try
				{
					Events objEvent = new Events();
					objEvent.eventid = Integer.parseInt(data[i][0].value.toString());	
					result.add(objEvent);	
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}

	public ArrayList<EventsDetails> getFeaturedEvent()
	{
		DictionaryEntry[][] data=null;
		data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTIMAGE+COMMA+EVENTSTARTDATE+COMMA
							+BUSINESSID+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+ISFEATURED+EQUALLY+QUOTE+TRUE+QUOTE);
		ArrayList<EventsDetails> result = new ArrayList<EventsDetails>();
		DateFormat df = new SimpleDateFormat(DATEFORMAT);
		 Calendar cal = Calendar.getInstance();
		Date date, currentDate;
		currentDate = cal.getTime();
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{	
				
				try
				{
					EventsDetails objEvent 	= new EventsDetails();
					objEvent.eventid 		= (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objEvent.eventname 		= (data[i][1].value!=null?data[i][1].value.toString():"");
					objEvent.image = (data[i][2].value!=null?data[i][2].value.toString():AppConstants.NOIMAGE);					
					objEvent.event_start_date = (data[i][3].value!=null?data[i][3].value.toString():"");
					objEvent.EventBusiness 	= (data[i][4].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
					objEvent.event_start_time	= (data[i][5].value!=null?data[i][5].value.toString():"");
					objEvent.price		= (data[i][6].value!=null?data[i][6].value.toString():"");
					objEvent.imageDateUpdated = (data[i][7].value!=null?data[i][7].value.toString():"");
					objEvent.imageId = (data[i][8].value!=null?data[i][8].value.toString():"0");
					objEvent.isImageUpdated =(data[i][9].value!=null?data[i][9].value.toString():"false");
					date = df.parse(objEvent.event_start_date);
					if(currentDate.before(date)){
						result.add(objEvent);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}
	
	public static ArrayList<EventsDetails> getEventsByDate(String strDate){
		ArrayList<EventsDetails> result = new ArrayList<EventsDetails>();
		DictionaryEntry[][] data=null;
		data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTIMAGE+COMMA+EVENTSTARTDATE+COMMA
				+BUSINESSID+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+EVENTSTARTDATE+EQUALLY+QUOTE+strDate+QUOTE);
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{	
				
				try
				{
					EventsDetails objEvent 	= new EventsDetails();
					objEvent.eventid 		= (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objEvent.eventname 		= (data[i][1].value!=null?data[i][1].value.toString():"");
					objEvent.image = (data[i][2].value!=null?data[i][2].value.toString():AppConstants.NOIMAGE);
					objEvent.event_start_date = (data[i][3].value!=null?data[i][3].value.toString():"");
					objEvent.EventBusiness 	= (data[i][4].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
					objEvent.event_start_time	= (data[i][5].value!=null?data[i][5].value.toString():"");
					objEvent.price		= (data[i][6].value!=null?data[i][6].value.toString():"");
					objEvent.imageDateUpdated = (data[i][7].value!=null?data[i][7].value.toString():"");
					objEvent.imageId = (data[i][8].value!=null?data[i][8].value.toString():"0");
					objEvent.isImageUpdated =(data[i][9].value!=null?data[i][9].value.toString():"false");
						result.add(objEvent);
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;		
	}
	
	public List<EventsDetails> getUpcomingEventByBusiness(int businessId)
	{
		DictionaryEntry[][] data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA
						+EVENTIMAGE+COMMA+EVENTSTARTDATE+COMMA+BUSINESSID+COMMA+EVENTSTARTTIME
						+COMMA+EVENTPRICE+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+businessId);
		
		List<EventsDetails> result = new ArrayList<EventsDetails>();
		
		if(data != null)
		{					
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					EventsDetails objEvent = new EventsDetails();
					objEvent.eventid =  (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objEvent.eventname = (data[i][1].value!=null?data[i][1].value.toString():"");
					objEvent.image= (data[i][2].value!=null?data[i][2].value.toString():"");
					objEvent.event_start_date =(data[i][3].value!=null?data[i][3].value.toString():"");
					objEvent.EventBusiness=(data[i][4].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
					objEvent.event_start_time = (data[i][5].value!=null?data[i][5].value.toString():"");
					objEvent.price = (data[i][6].value!=null?data[i][6].value.toString():"");
					objEvent.imageDateUpdated = (data[i][7].value!=null?data[i][7].value.toString():"");
					objEvent.imageId = (data[i][8].value!=null?data[i][8].value.toString():"0");
					objEvent.isImageUpdated =(data[i][9].value!=null?data[i][9].value.toString():"false");
					result.add(objEvent);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}

	public List<EventsDetails> getAllEventsList()
	{
		DictionaryEntry[][] data = DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTIMAGE+COMMA+EVENTPRICE+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST);
	  List<EventsDetails> result = new ArrayList<EventsDetails>();
		
		if(data != null)
		{	
			
			for( int i = 0; i<data.length; i++)
			{	
				try
				{
					EventsDetails objEventsDetails=new EventsDetails();
					objEventsDetails.eventid=(data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objEventsDetails.eventname=(data[i][1].value!=null?data[i][1].value.toString():"");
					objEventsDetails.event_start_date=(data[i][2].value!=null?data[i][2].value.toString():"");
					objEventsDetails.image=(data[i][3].value!=null?data[i][3].value.toString():NOIMAGE);
					objEventsDetails.price=(data[i][4].value!=null?data[i][4].value.toString():"");
					objEventsDetails.imageDateUpdated = (data[i][5].value!=null?data[i][5].value.toString():"");
					objEventsDetails.imageId = (data[i][6].value!=null?data[i][6].value.toString():"0");
					objEventsDetails.isImageUpdated =(data[i][7].value!=null?data[i][7].value.toString():"false");
					result.add(objEventsDetails);
				}
				catch(Exception ee)
				{
					ee.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static  List<EventsDetails> searchEvent(String strSearchKeyword,String strSelectedDate,int SelectedCategoryId)
	{
		DictionaryEntry[][] data=null;
		if(SelectedCategoryId==0)
		{
			if(!strSearchKeyword.equals("") && !strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA
									+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE
									+EVENTSTARTDATE+EQUALLY+QUOTE+strSelectedDate+QUOTE+AND+EVENTNAME+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT);
			else if(!strSearchKeyword.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED
									+FROM+EVENTLIST+WHERE+EVENTNAME+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT);
			else if(!strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED
									+FROM+EVENTLIST+WHERE+EVENTSTARTDATE+EQUALLY+QUOTE+strSelectedDate+QUOTE);	
			else
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED
									+FROM+EVENTLIST);
		}
		else
		{
			if(!strSearchKeyword.equals("") && !strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED
										+FROM+EVENTLIST+WHERE+EVENTSTARTDATE+EQUALLY+QUOTE+strSelectedDate+QUOTE+AND+EVENTNAME+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+AND+CATEGORY1+EQUALLY+SelectedCategoryId);
			else if(!strSearchKeyword.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTNAME+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+AND+CATEGORY1+EQUALLY+SelectedCategoryId);
			else if(!strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTSTARTDATE+EQUALLY+QUOTE+strSelectedDate+QUOTE+AND+CATEGORY1+EQUALLY+SelectedCategoryId);	
			else
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+
									FROM+EVENTLIST+WHERE+CATEGORY1+EQUALLY+SelectedCategoryId);
		}
		
		List<EventsDetails> result = new ArrayList<EventsDetails>();
			
			if(data != null)
			{					
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						EventsDetails objEvent 		=	new EventsDetails();
						objEvent.eventid 			= 	(data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
						objEvent.eventname 			=	(data[i][1].value!=null?data[i][1].value.toString():"");
						objEvent.event_start_date 	=	(data[i][2].value!=null?data[i][2].value.toString():"");
						objEvent.event_start_time 	= 	(data[i][3].value!=null?data[i][3].value.toString():"");
						objEvent.price 				= 	(data[i][4].value!=null?data[i][4].value.toString():"");
						objEvent.image				= 	(data[i][5].value!=null?data[i][5].value.toString():NOIMAGE);
						objEvent.EventBusiness 		= 	Integer.parseInt(data[i][6].value.toString());
						objEvent.imageDateUpdated	=	(data[i][6].value!=null?data[i][6].value.toString():"");
						objEvent.imageId			=	(data[i][7].value!=null?data[i][7].value.toString():"0");
						objEvent.isImageUpdated =(data[i][8].value!=null?data[i][8].value.toString():"false");
						result.add(objEvent);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
		return result;
	}
	
	public List<EventsDetails> searchEventforCalView(String strSearchKeyword,int selectedCategoryId)
	{
		DictionaryEntry[][] data=null;
		if(!strSearchKeyword.equals("") && selectedCategoryId!=0)
			data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+CATEGORY1+EQUALLY+selectedCategoryId+AND+EVENTNAME+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT);
		else if(!strSearchKeyword.equals("")&& selectedCategoryId==0)
			data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
								FROM+EVENTLIST+WHERE+EVENTNAME+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT);
		else if(strSearchKeyword.equals("")&& selectedCategoryId!=0)
			data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
								FROM+EVENTLIST+WHERE+CATEGORY1+EQUALLY+selectedCategoryId);	
		else
			data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
								FROM+EVENTLIST);	
		
		List<EventsDetails> result = new ArrayList<EventsDetails>();
			
			if(data != null)
			{					
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						EventsDetails objEvent 		=	new EventsDetails();
						objEvent.eventid 			= 	(data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
						objEvent.eventname 			=	(data[i][1].value!=null?data[i][1].value.toString():"");
						objEvent.event_start_date 	=	(data[i][2].value!=null?data[i][2].value.toString():"");
						objEvent.event_start_time 	= 	(data[i][3].value!=null?data[i][3].value.toString():"");
						objEvent.price 				= 	(data[i][4].value!=null?data[i][4].value.toString():"");
						objEvent.image 				= 	(data[i][5].value!=null?data[i][5].value.toString():NOIMAGE);
						objEvent.EventBusiness 		= 	(data[i][6].value!=null?Integer.parseInt(data[i][6].value.toString()):0);
						objEvent.imageDateUpdated	=	(data[i][7].value!=null?data[i][7].value.toString():"");
						objEvent.imageId			=	(data[i][8].value!=null?data[i][8].value.toString():"0");
						objEvent.isImageUpdated =(data[i][9].value!=null?data[i][9].value.toString():"false");
						result.add(objEvent);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
		return result;
	}

	public List<EventsDetails> searchEventByLocation(String strSearchKeyword,String strSelectedDate,int SelectedCategoryId)
	{
		DictionaryEntry[][] data=null;
		if(SelectedCategoryId==0)
		{
			if(!strSearchKeyword.equals("")&&!strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
										FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+SELECT+EVENT_ID+FROM+EVENT_LOCATION+WHERE+CITY+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+RIGHT_BRACKET+AND+EVENTSTARTDATE+EQUALLY+QUOTE+strSelectedDate+QUOTE);
			else if(strSearchKeyword.equals("")&&strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST);
			else if(!strSearchKeyword.equals("")&&strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
										FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+SELECT+EVENT_ID+FROM+EVENT_LOCATION+WHERE+CITY+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+RIGHT_BRACKET);
			else if(strSearchKeyword.equals("")&&!strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
										FROM+EVENTLIST+WHERE+EVENTSTARTDATE+EQUALLY+QUOTE+strSelectedDate+QUOTE);
		}
		else
		{
			if(!strSearchKeyword.equals("")&&!strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
										FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+SELECT+EVENT_ID+FROM+EVENT_LOCATION+WHERE+CITY+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+RIGHT_BRACKET+
										AND+EVENTSTARTDATE+EQUALLY+QUOTE+strSelectedDate+QUOTE+AND+CATEGORY1+EQUALLY+SelectedCategoryId);
			else if(strSearchKeyword.equals("")&&strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
										FROM+EVENTLIST+CATEGORY1+EQUALLY+SelectedCategoryId);
			else if(!strSearchKeyword.equals("")&&strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
										FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+SELECT+EVENT_ID+FROM+EVENT_LOCATION+WHERE+CITY+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+RIGHT_BRACKET+
										AND+CATEGORY1+EQUALLY+SelectedCategoryId);
			else if(strSearchKeyword.equals("")&&!strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
										FROM+EVENTLIST+WHERE+EVENTSTARTDATE+EQUALLY+QUOTE+strSelectedDate+QUOTE+AND+CATEGORY1+EQUALLY+SelectedCategoryId);
		}
		   List<EventsDetails> result = new ArrayList<EventsDetails>();
		   
			
			if(data != null)
			{					
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						EventsDetails objEvent 		=	new EventsDetails();
						objEvent.eventid 			= 	(data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
						objEvent.eventname 			=	(data[i][1].value!=null?data[i][1].value.toString():"");
						objEvent.event_start_date 	=	(data[i][2].value!=null?data[i][2].value.toString():"");
						objEvent.event_start_time 	= 	(data[i][3].value!=null?data[i][3].value.toString():"");
						objEvent.price 				= 	(data[i][4].value!=null?data[i][4].value.toString():"");
						objEvent.image 				= 	(data[i][5].value!=null?data[i][5].value.toString():"");
						objEvent.EventBusiness		= 	(data[i][6].value!=null?Integer.parseInt(data[i][6].value.toString()):0);
						objEvent.imageDateUpdated	=	(data[i][7].value!=null?data[i][7].value.toString():"");
						objEvent.imageId			=	(data[i][8].value!=null?data[i][8].value.toString():"0");
						objEvent.isImageUpdated =(data[i][9].value!=null?data[i][9].value.toString():"false");
						result.add(objEvent);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
		return result;
	}
	
	public ArrayList<EventDates> searchEventForPalender(String strAttendingEvents,String strSearchKeyword,String strSelectedDate,int categoryId)
	{
		DictionaryEntry[][] data=null;
		if(categoryId==0)
		{
			if(!strSearchKeyword.equals("") && !strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTNAME+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+AND+EVENTSTARTDATE+EQUALLY+QUOTE+strSelectedDate+QUOTE);
			else if(strSearchKeyword.equals("") && strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET);	
			else if(!strSearchKeyword.equals("") && strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTNAME+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT);
			else if(strSearchKeyword.equals("") && !strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTSTARTDATE+EQUALLY+QUOTE+strSelectedDate+QUOTE);
		}
		else
		{
			if(!strSearchKeyword.equals("") && !strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTNAME+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+
									AND+EVENTSTARTDATE+EQUALLY+QUOTE+strSelectedDate+QUOTE+AND+CATEGORY1+EQUALLY+categoryId);
			else if(strSearchKeyword.equals("") && strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+CATEGORY1+EQUALLY+categoryId);	
			else if(!strSearchKeyword.equals("") && strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTNAME+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+AND+CATEGORY1+EQUALLY+categoryId);
			else if(strSearchKeyword.equals("") && !strSelectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTSTARTDATE+EQUALLY+QUOTE+strSelectedDate+QUOTE+AND+CATEGORY1+EQUALLY+categoryId);
		}

		   ArrayList<EventDates> result = new ArrayList<EventDates>();
			
			if(data != null)
			{					
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						EventDates objEvent 		=	new EventDates();
						objEvent.eventId 			= 	(data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
						objEvent.eventName 			=	(data[i][1].value!=null?data[i][1].value.toString():"");
						objEvent.strEventStartdate 	=	(data[i][2].value!=null?data[i][2].value.toString():"");
						objEvent.strEventStartTime 	=	(data[i][3].value!=null?data[i][3].value.toString():"");
						objEvent.strEventPrice	 	=	(data[i][4].value!=null?data[i][4].value.toString():"");
						objEvent.eventImage 		= 	(data[i][5].value!=null?data[i][5].value.toString():"");
						objEvent.businessId 		= 	(data[i][6].value!=null?Integer.parseInt(data[i][6].value.toString()):0);
						objEvent.imageDateUpdated	=	(data[i][7].value!=null?data[i][7].value.toString():"");
						objEvent.imageId			=	(data[i][8].value!=null?data[i][8].value.toString():"0");
						objEvent.isImageUpdated =(data[i][9].value!=null?data[i][9].value.toString():"false");
						result.add(objEvent);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
		return result;
	}
	
	public ArrayList<EventDates> searchEventForPalenderByLocation(String strAttendingEvents,String strSearchKeyword,String selectedDate,int categoryId)
	{
		DictionaryEntry[][] data=null;
		Log.i(strSearchKeyword,strSearchKeyword);
		if(categoryId==0)
		{
			if(!strSearchKeyword.equals("")&&!selectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTID+IN+LEFT_BRACKET+SELECT+EVENT_ID+
									FROM+EVENT_LOCATION+WHERE+CITY+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+RIGHT_BRACKET+AND+EVENTSTARTDATE+EQUALLY+QUOTE+selectedDate+QUOTE);
			else if(strSearchKeyword.equals("")&& selectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET);	
			else if(strSearchKeyword.equals("")&&!selectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTSTARTDATE+EQUALLY+QUOTE+selectedDate+QUOTE);
			else if(!strSearchKeyword.equals("")&& selectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTID+IN+LEFT_BRACKET+SELECT+EVENT_ID+FROM+EVENT_LOCATION+
									WHERE+CITY+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+RIGHT_BRACKET);
		}
		else
		{
			if(!strSearchKeyword.equals("")&&!selectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTID+IN+LEFT_BRACKET+SELECT+EVENT_ID+
									FROM+EVENT_LOCATION+WHERE+CITY+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+RIGHT_BRACKET+AND+EVENTSTARTDATE+EQUALLY+QUOTE+selectedDate+QUOTE+AND+CATEGORY1+EQUALLY+categoryId);
			else if(strSearchKeyword.equals("")&& selectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+CATEGORY1+EQUALLY+categoryId);	
			else if(strSearchKeyword.equals("")&&!selectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTSTARTDATE+EQUALLY+QUOTE+selectedDate+QUOTE+AND+CATEGORY1+EQUALLY+categoryId);
			else if(!strSearchKeyword.equals("")&& selectedDate.equals(""))
				data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
									FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTID+IN+LEFT_BRACKET+SELECT+EVENT_ID+FROM+EVENT_LOCATION+
									WHERE+CITY+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+RIGHT_BRACKET+AND+CATEGORY1+EQUALLY+categoryId);
		}

		  ArrayList<EventDates> result = new ArrayList<EventDates>();
			
			if(data != null)
			{					
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						EventDates objEvent 		=	new EventDates();
						objEvent.eventId 			= 	(data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
						objEvent.eventName 			=	(data[i][1].value!=null?data[i][1].value.toString():"");
						objEvent.strEventStartdate 	=	(data[i][2].value!=null?data[i][2].value.toString():"");
						objEvent.strEventStartTime 	=	(data[i][3].value!=null?data[i][3].value.toString():"");
						objEvent.strEventPrice	 	=	(data[i][4].value!=null?data[i][4].value.toString():"");
						objEvent.eventImage 		= 	(data[i][5].value!=null?data[i][5].value.toString():"");
						objEvent.businessId 		= 	(data[i][6].value!=null?Integer.parseInt(data[i][6].value.toString()):0);
						objEvent.imageDateUpdated	=	(data[i][7].value!=null?data[i][7].value.toString():"");
						objEvent.imageId			=	(data[i][8].value!=null?data[i][8].value.toString():"0");
						objEvent.isImageUpdated =(data[i][9].value!=null?data[i][9].value.toString():"false");
						result.add(objEvent);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
		return result;
	}
	
	public List<EventsDetails> searchInFavoriteEvents(String strEventIds,String strSearchKeyword)
	{
		DictionaryEntry[][] data=null;
		if(!strSearchKeyword.equals("")){
		data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
								FROM+EVENTLIST+WHERE+EVENTNAME+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+AND+EVENTID+IN+LEFT_BRACKET+strEventIds+RIGHT_BRACKET);
		}else{
		data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
								FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strEventIds+RIGHT_BRACKET);	
		}			
		  List<EventsDetails> result = new ArrayList<EventsDetails>();
			
			if(data != null)
			{					
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						EventsDetails objEvent = new EventsDetails();
						objEvent.eventid =  (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
						objEvent.eventname = (data[i][1].value!=null?data[i][1].value.toString():"");
						objEvent.event_start_date = (data[i][2].value!=null?data[i][2].value.toString():"");
						objEvent.event_start_time = (data[i][3].value!=null?data[i][3].value.toString():"");
						objEvent.price = (data[i][4].value!=null?data[i][4].value.toString():"");
						objEvent.image = (data[i][5].value!=null?data[i][5].value.toString():"");
						objEvent.imageDateUpdated	=	(data[i][6].value!=null?data[i][6].value.toString():"");
						objEvent.imageId			=	(data[i][7].value!=null?data[i][7].value.toString():"");
						objEvent.isImageUpdated =(data[i][8].value!=null?data[i][8].value.toString():"false");
						result.add(objEvent);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
		return result;
	}
	
	public ArrayList<EventsDetails> getAllEventsInCategoryOnDate(String strEventIds,String strEventDate)
	{
		DictionaryEntry[][] data=null;
		Cursor cursor;
		if(!strEventIds.equals("")){
			cursor=DatabaseHelper.doSelect(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+COMMA+EVENTIMAGEISUPDATED+
								FROM+EVENTLIST+WHERE+EVENTSTARTDATE+EQUALLY+QUOTE+strEventDate+QUOTE+AND+EVENTID+IN+LEFT_BRACKET+strEventIds+RIGHT_BRACKET);
			
		}
		else{
			cursor=DatabaseHelper.doSelect(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+COMMA+EVENTIMAGEISUPDATED+
								FROM+EVENTLIST+WHERE+EVENTSTARTDATE+EQUALLY+QUOTE+strEventDate+QUOTE);
		}
			
		ArrayList<EventsDetails> result = new ArrayList<EventsDetails>();
	
		cursor.moveToFirst();
		for(int i=0; i<cursor.getCount();i++){
			EventsDetails objEvent = new EventsDetails();
			objEvent.eventid =  cursor.getInt(0);
			objEvent.eventname = (cursor.getString(1)!=null?cursor.getString(1):"");
			objEvent.event_start_date =(cursor.getString(2)!=null?cursor.getString(2):"");
			objEvent.event_start_time = (cursor.getString(3)!=null?cursor.getString(3):"");
			objEvent.price = (cursor.getString(4)!=null?cursor.getString(4):"");
			objEvent.image = (cursor.getString(5)!=null?cursor.getString(5):NOIMAGE);
			objEvent.EventBusiness= cursor.getInt(6);
			objEvent.imageDateUpdated	=	(cursor.getString(7)!=null?cursor.getString(7):"");
			objEvent.imageId			=	(cursor.getString(8)!=null?cursor.getString(8):"0");
			objEvent.isImageUpdated		=   (cursor.getString(9)!=null?cursor.getString(9):"false");
			result.add(objEvent);
			cursor.moveToNext();
		}		
		
		return result;
	}
	
	public List<EventsDetails> geteventDetails(int Condition)
	{
	
	DictionaryEntry[][] data=null;
	DictionaryEntry[][] data2=null;
	DictionaryEntry[][] databusiness=null;
	data=DatabaseHelper.get(SELECT+EVENTID+COMMA+NAME+COMMA+DESCRIPTION+COMMA+EVENT_START_DATE+COMMA+EVENT_START_TIME+COMMA+BUSINESS+COMMA+IMAGE1+COMMA+TICKETS+COMMA+PRICE+
							FROM+EVENTDETAILS+WHERE+EVENTID+EQUALLY+Condition);
	data2=DatabaseHelper.get(SELECT+ADDRESS+COMMA+CITY+COMMA+STATE+COMMA+COUNTRY+COMMA+GEOCODE+COMMA+ZIP+
							FROM+EVENT_LOCATION+WHERE+EVENT_ID+EQUALLY+Condition);
	List<EventsDetails> result = new ArrayList<EventsDetails>();
		
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					EventsDetails objEvent = new EventsDetails();
					objEvent.eventid = (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objEvent.eventname =(data[i][1].value!=null?data[i][1].value.toString():"");
					objEvent.description = (data[i][2].value!=null?data[i][2].value.toString():"");
					databusiness= DatabaseHelper.get(SELECT+BIZNAME+FROM+BUSINESSLIST+WHERE+"id="+data[i][5].value);
					objEvent.BusinessName= (databusiness[i][0].value!=null?databusiness[i][0].value.toString():"");
					objEvent.event_start_date = (data[i][3].value!=null?data[i][3].value.toString():"");
					objEvent.event_start_time = (data[i][4].value!=null?data[i][4].value.toString():"");
					objEvent.business=(data[i][5].value!=null?Integer.parseInt(data[i][5].value.toString()):0);
					objEvent.image = (data[i][6].value!=null?data[i][6].value.toString():"");
					objEvent.tickets = (data[i][7].value!=null?data[i][7].value.toString():"");
					objEvent.price = (data[i][8].value!=null?data[i][8].value.toString():"");
					if(data2!=null)
					{
						objEvent.EventAddress=(data2[i][0].value!=null?data2[0][0].value.toString():"");
						objEvent.EventCity=(data2[i][1].value!=null?data2[0][1].value.toString():"");
						objEvent.EventState=(data2[i][2].value!=null?data2[0][2].value.toString():"");
						objEvent.EventCountry=(data2[i][3].value!=null?data2[0][3].value.toString():"");
						objEvent.EventGeoCode=(data2[i][4].value!=null?data2[0][4].value.toString():"");
						objEvent.EventLocationZip=(data2[i][5].value!=null?data2[0][5].value.toString():"");
					}
					result.add(objEvent);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}

	public List<EventsDetails> getEventsByBusiness(int businessId)
	{
	DictionaryEntry[][] data=null;
	data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTSTARTDATE+COMMA+EVENTSTARTTIME+COMMA+BUSINESSID+COMMA+EVENTIMAGE+
							COMMA+EVENTPRICE+COMMA+EVENTMAINIMAGE+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
							FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+businessId);
	List<EventsDetails> result = new ArrayList<EventsDetails>();
		
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					EventsDetails objEvent 		= new EventsDetails();
					objEvent.eventid 			= (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objEvent.eventname 			= (data[i][1].value!=null?data[i][1].value.toString():"");
					objEvent.event_start_date 	= (data[i][2].value!=null?data[i][2].value.toString():"");
					objEvent.event_start_time 	= (data[i][3].value!=null?data[i][3].value.toString():"");
					objEvent.EventBusiness		= (data[i][4].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
					objEvent.image 				= (data[i][5].value!=null?data[i][5].value.toString():"");
					objEvent.price 				= (data[i][6].value!=null?data[i][6].value.toString():"");
					objEvent.imageDateUpdated	=	(data[i][7].value!=null?data[i][7].value.toString():"");
					objEvent.imageId			=	(data[i][8].value!=null?data[i][8].value.toString():"0");
					objEvent.isImageUpdated =(data[i][9].value!=null?data[i][9].value.toString():"false");
					result.add(objEvent);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}
	
	public List<EventImages> getAllEventImages(int eventId)
	{
		DictionaryEntry data[][]=DatabaseHelper.get(SELECT+IMAGE1+COMMA+IMAGE2+COMMA+IMAGE3+COMMA+IMAGE4+FROM+EVENTDETAILS+WHERE+EVENTID+EQUALLY+eventId);
		
		List<EventImages> result = new ArrayList<EventImages>();
		
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					EventImages objEventImages = new EventImages();
					objEventImages.strEventImage1 = (data[i][0].value!=null?data[i][0].value.toString():AppConstants.NOIMAGE);
					objEventImages.strEventImage2 = (data[i][1].value!=null?data[i][1].value.toString():AppConstants.NOIMAGE);
					objEventImages.strEventImage3 = (data[i][2].value!=null?data[i][2].value.toString():AppConstants.NOIMAGE);
					objEventImages.strEventImage4 = (data[i][3].value!=null?data[i][3].value.toString():AppConstants.NOIMAGE);
					result.add(objEventImages);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		
		return result;
	}
	
	public List<EventsDetails> getPostEventDetails(int Condition)
	{
	DictionaryEntry[][] data=DatabaseHelper.get(SELECT+EVENTNAME+FROM+EVENTLIST+WHERE+EVENTID+EQUALLY+Condition);
	
	List<EventsDetails> result = new ArrayList<EventsDetails>();
		
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					EventsDetails objEvent = new EventsDetails();
					objEvent.eventname =data[i][0].value.toString();
					result.add(objEvent);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}
	
	public long InsertIntoEventDetails(EventsDetailsData objEventsDetails)
	{
		String WHEREClause = EVENTID+EQUALLY+objEventsDetails.id;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+EVENTID+COMMA+DATE_UPDATED+FROM+EVENTDETAILS+WHERE+WHEREClause); 
		ContentValues values = new ContentValues();
		values.put(EVENTID, objEventsDetails.id);	 		
 		values.put(LIKE_COUNT,objEventsDetails.likecount);
 		values.put(IS_FEATURED,objEventsDetails.is_featured);	 		
 		values.put(EVENT_START_DATE, objEventsDetails.event_start_date);	 		
 		values.put(PRICE, objEventsDetails.price);
 		values.put(EVENT_EXPIRATION, objEventsDetails.event_expiration);
 		values.put(NAME, objEventsDetails.eventname);
 		values.put(EVENT_START_TIME, objEventsDetails.event_start_time);
 		values.put(DESCRIPTION, objEventsDetails.description);
 		values.put(EVENT_END_DATE, objEventsDetails.event_end_date);
 		values.put(EVENT_END_TIME, objEventsDetails.event_end_time);
 		values.put(TICKETS, objEventsDetails.tickets);
 		values.put(SUPP_EMAIL,objEventsDetails.supportemail);
 		values.put(SUPP_PHN,objEventsDetails.supportphone);
 		values.put(STATUS,objEventsDetails.status);
 		values.put(BUSINESS,objEventsDetails.business.id);
 		values.put(DATE_UPDATED,objEventsDetails.date_updated);
 		values.put(DATE_CREATED,objEventsDetails.date_created);
 		if(objEventsDetails.images!=null){
	 		for(int i=0;i<objEventsDetails.images.size();i++){
		 		values.put(IMAGE+(i+1),(objEventsDetails.images.get(i).imagesrc!=null?objEventsDetails.images.get(i).imagesrc:AppConstants.NOIMAGE));
	 		}
 		}else{
 			for(int i=1;i<5;i++){
		 		values.put(IMAGE+i,NOIMAGE);
	 		}
 		}
 	
		if(data != null && data[0][0].value != null)
		{
			if(!objEventsDetails.date_updated.equals(data[0][1].value.toString())){
				return DatabaseHelper.doUpdate(EVENTDETAILS, values, WHEREClause, null);
			}else{
				return 0;
			}
			
		}
		else
		{
	 		return DatabaseHelper.doInsert(EVENTDETAILS, values);
		}
	}
	
	//method to get list events attending by loged in user
	public String getLogedInUserAttendingEvents(int USER_ID)
	{
		// TODO for day - startdate would be 04-Apr-2011 00:00:00 and  end date would be 04-Apr-2011 23:59:59
		//SELECT * FROM tblAppointments WHERE AppointmentTime between  '04-Apr-2011 00:00:00' and '15-Apr-2011 23:59:59' 
		DictionaryEntry[][] data = DatabaseHelper.get(SELECT+EVENTID+FROM+ATTENDING+WHERE+USER_ID+EQUALLY+USER_ID);
		String strAttendingEvents="";
	
		
		if(data != null)
		{					
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
			
					if(i<data.length-1)
					{
						strAttendingEvents=data[i][0].value.toString()+","+strAttendingEvents;
					}
					else
					{
						strAttendingEvents=strAttendingEvents+data[i][0].value.toString();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return strAttendingEvents;
	}
	
	public String getAllEventsInCategory(int categoryId, String strAttendingEvent)
	{
		// TODO for day - startdate would be 04-Apr-2011 00:00:00 and  end date would be 04-Apr-2011 23:59:59
		//SELECT * FROM tblAppointments WHERE AppointmentTime between  '04-Apr-2011 00:00:00' and '15-Apr-2011 23:59:59' 
		DictionaryEntry[][] data=null;
		if(!strAttendingEvent.equals(""))
			data = DatabaseHelper.get(SELECT+EVENTID+FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvent+RIGHT_BRACKET+AND+CATEGORY1+EQUALLY+categoryId);
		else 
			data = DatabaseHelper.get(SELECT+EVENTID+FROM+EVENTLIST+WHERE+CATEGORY1+EQUALLY+categoryId);
		String strEventsInCategory="";
	
		
		if(data != null)
		{					
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
			
					if(i<data.length-1)
					{
						strEventsInCategory=data[i][0].value.toString()+","+strEventsInCategory;
					}
					else
					{
						strEventsInCategory=strEventsInCategory+data[i][0].value.toString();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return strEventsInCategory;
	}
	
	//method to get the start and end dates of user attending events
	public ArrayList<EventDates> getdatesOfUserAttendingEvents(String strAttendingEvents,String eventDate)
	{	
		
		ArrayList<EventDates> result = new ArrayList<EventDates>();
		if(!strAttendingEvents.equals(""))
		{
				DictionaryEntry[][] data=null;
					if(eventDate.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+
												COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET);
					else
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+
												COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE);
			if(data != null)
				{						
					for( int i = 0; i<data.length; i++)
					{		
						try
						{
							EventDates objEvent = new EventDates();
							objEvent.strEventStartdate = (data[i][0].value!=null?data[i][0].value.toString():"");
							objEvent.eventName = (data[i][1].value!=null?data[i][1].value.toString():"");
							objEvent.eventId =(data[i][2].value!=null?Integer.parseInt(data[i][2].value.toString()):0);
							objEvent.eventImage = (data[i][3].value!=null?data[i][3].value.toString():NOIMAGE);
							objEvent.businessId = (data[i][3].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
							objEvent.strEventPrice = (data[i][5].value!=null?data[i][5].value.toString():"");
							objEvent.strEventStartTime = (data[i][6].value!=null?data[i][6].value.toString():"");
							objEvent.imageDateUpdated	=	(data[i][7].value!=null?data[i][7].value.toString():"");
							objEvent.imageId			=	(data[i][8].value!=null?data[i][8].value.toString():"0");
							objEvent.isImageUpdated =(data[i][9].value!=null?data[i][9].value.toString():"false");
							result.add(objEvent);	
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

	public List<EventDates> getUserAttendingEventsByDate(){
		List<EventDates> result = new ArrayList<EventDates>();
		Cursor cursor = DatabaseHelper.doSelect(SELECT+EVENTDETAILS+DOT+EVENTID+COMMA+EVENTDETAILS+DOT+NAME+COMMA+EVENTDETAILS+DOT+EVENT_START_DATE+COMMA+EVENTDETAILS+DOT+IMAGE1+
												FROM+EVENTDETAILS+COMMA+ATTENDING+WHERE+EVENTDETAILS+DOT+EVENTID+EQUALLY+ATTENDING+DOT+EVENTID+AND+ATTENDING+DOT+USER_ID+EQUALLY+AppConstants.USER_ID);
		cursor.moveToFirst();
		for(int i=0; i<cursor.getCount();i++){
			EventDates objEvent = new EventDates();
			objEvent.eventId = cursor.getInt(1);
			objEvent.eventName =cursor.getString(2);
			objEvent.strEventStartdate = cursor.getString(3);
			objEvent.eventImage =cursor.getString(4);
			result.add(objEvent);
			cursor.moveToNext();
		}		
		return result;
	}
	
	public ArrayList<EventDates> getEventsByBusiness(int BusinessId,String serchKeyword,String eventDate,int categoryId)
	{		
		ArrayList<EventDates> result = new ArrayList<EventDates>();
		if(BusinessId!=0)
		{
				DictionaryEntry[][] data=null;
				if(categoryId==0)
				{
					if(eventDate.equals("")&&serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+BusinessId);
					else if(!eventDate.equals("")&&serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+QUOTE+BusinessId+QUOTE+AND+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE);
					else if(eventDate.equals("")&&!serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+QUOTE+BusinessId+QUOTE+AND+EVENTNAME+LIKE+LIKE_LEFT+serchKeyword+LIKE_RIGHT);
					else if(!eventDate.equals("")&&!serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+QUOTE+BusinessId+QUOTE+AND+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE+AND+EVENTNAME+LIKE+LIKE_LEFT+serchKeyword+LIKE_RIGHT);
				}
				else
				{
					if(eventDate.equals("")&&serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+BusinessId+AND+CATEGORY1+EQUALLY+categoryId);
					else if(!eventDate.equals("")&&serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+QUOTE+BusinessId+QUOTE+AND+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE+AND+CATEGORY1+EQUALLY+categoryId);
					else if(eventDate.equals("")&&!serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+QUOTE+BusinessId+QUOTE+AND+EVENTNAME+LIKE+LIKE_LEFT+serchKeyword+LIKE_RIGHT+AND+CATEGORY1+EQUALLY+categoryId);
					else if(!eventDate.equals("")&&!serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+QUOTE+BusinessId+QUOTE+AND+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE+AND+EVENTNAME+LIKE+LIKE_LEFT+serchKeyword+LIKE_RIGHT+
												AND+CATEGORY1+EQUALLY+categoryId);
				}
				
			if(data != null)
				{						
					for( int i = 0; i<data.length; i++)
					{		
						try
						{
							EventDates objEvent = new EventDates();
							objEvent.strEventStartdate = (data[i][0].value!=null?data[i][0].value.toString():"");
							objEvent.eventName = (data[i][1].value!=null?data[i][1].value.toString():"");
							objEvent.eventId =(data[i][2].value!=null?Integer.parseInt(data[i][2].value.toString()):0);
							objEvent.eventImage = (data[i][3].value!=null?data[i][3].value.toString():NOIMAGE);
							objEvent.businessId = (data[i][3].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
							objEvent.strEventPrice = (data[i][5].value!=null?data[i][5].value.toString():NOIMAGE);
							objEvent.strEventStartTime = (data[i][6].value!=null?data[i][6].value.toString():"");
							objEvent.imageDateUpdated	=	(data[i][7].value!=null?data[i][7].value.toString():"");
							objEvent.imageId			=	(data[i][8].value!=null?data[i][8].value.toString():"0");
							objEvent.isImageUpdated =(data[i][9].value!=null?data[i][9].value.toString():"false");
							result.add(objEvent);	
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

	public ArrayList<EventDates> getEventsByBusinessLocation(int BusinessId,String serchKeyword,String eventDate,int categoryId)
	{		
		ArrayList<EventDates> result = new ArrayList<EventDates>();
		
		if(BusinessId!=0)
		{
				DictionaryEntry[][] data=null;
				if(categoryId==0)
				{
					if(eventDate.equals("")&&serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
													FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+BusinessId);
					else if(!eventDate.equals("")&&serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
													FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+QUOTE+BusinessId+QUOTE+AND+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE);
					else if(eventDate.equals("")&&!serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
													FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+QUOTE+BusinessId+QUOTE+AND+EVENTID+IN+LEFT_BRACKET+SELECT+EVENT_ID+
													FROM+EVENT_LOCATION+WHERE+CITY+LIKE+LIKE_LEFT+serchKeyword+LIKE_RIGHT+RIGHT_BRACKET);
					else if(!eventDate.equals("")&&!serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
													FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+QUOTE+BusinessId+QUOTE+AND+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE+AND+EVENTID+IN+LEFT_BRACKET+
													SELECT+EVENT_ID+FROM+EVENT_LOCATION+WHERE+CITY+LIKE+LIKE_LEFT+serchKeyword+LIKE_RIGHT+RIGHT_BRACKET);
				}
				else
				{
					if(eventDate.equals("")&&serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+BusinessId+AND+CATEGORY1+EQUALLY+categoryId);
					else if(!eventDate.equals("")&&serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+QUOTE+BusinessId+QUOTE+AND+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE+AND+CATEGORY1+EQUALLY+categoryId);
					else if(eventDate.equals("")&&!serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+QUOTE+BusinessId+QUOTE+AND+EVENTID+IN+LEFT_BRACKET+SELECT+EVENT_ID+
												FROM+EVENT_LOCATION+WHERE+CITY+LIKE+LIKE_LEFT+serchKeyword+LIKE_RIGHT+RIGHT_BRACKET+AND+CATEGORY1+EQUALLY+categoryId);
					else if(!eventDate.equals("")&&!serchKeyword.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTPRICE+COMMA+EVENTSTARTTIME+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
												FROM+EVENTLIST+WHERE+BUSINESSID+EQUALLY+QUOTE+BusinessId+QUOTE+AND+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE+AND+EVENTID+IN+LEFT_BRACKET+
												SELECT+EVENT_ID+FROM+EVENT_LOCATION+WHERE+CITY+LIKE+LIKE_LEFT+serchKeyword+LIKE_RIGHT+RIGHT_BRACKET+AND+CATEGORY1+EQUALLY+categoryId);
				}
				
			if(data != null)
				{						
					for( int i = 0; i<data.length; i++)
					{		
						try
						{
							EventDates objEvent = new EventDates();
							objEvent.strEventStartdate = (data[i][0].value!=null?data[i][0].value.toString():"");
							objEvent.eventName = (data[i][1].value!=null?data[i][1].value.toString():"");
							objEvent.eventId =(data[i][2].value!=null?Integer.parseInt(data[i][2].value.toString()):0);
							objEvent.eventImage = (data[i][3].value!=null?data[i][3].value.toString():"");
							objEvent.businessId = (data[i][4].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
							objEvent.strEventPrice = (data[i][5].value!=null?data[i][5].value.toString():"");
							objEvent.strEventStartTime = (data[i][6].value!=null?data[i][6].value.toString():"");
							objEvent.imageDateUpdated	=	(data[i][7].value!=null?data[i][7].value.toString():"");
							objEvent.imageId			=	(data[i][8].value!=null?data[i][8].value.toString():"0");
							objEvent.isImageUpdated =(data[i][9].value!=null?data[i][9].value.toString():"false");
							result.add(objEvent);	
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

	//TODO: STOPED HERE
	
	public ArrayList<EventDates> getdatesOfUserAttendingEventsCalView(String strAttendingEvents,String eventDate,int categoryId,String searchKeyword)
	{		
		ArrayList<EventDates> result = new ArrayList<EventDates>();
		if(!strAttendingEvents.equals(""))
		{
				DictionaryEntry[][] data=null;
				if(categoryId==0&&searchKeyword.equals(""))
					data = DatabaseHelper.get(SELECT+EVENTLIST+DOT+EVENTSTARTDATE+COMMA+EVENTLIST+DOT+EVENTNAME+COMMA+EVENTLIST+DOT+EVENTID+COMMA
											+EVENTLIST+DOT+EVENTIMAGE+COMMA+EVENTLIST+DOT+BUSINESSID+COMMA+USERFAVORITE+DOT+USERADDEDEVENTID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
											FROM+EVENTLIST+COMMA+USERFAVORITE+WHERE+EVENTLIST+DOT+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+
											AND+EVENTLIST+DOT+EVENTID+EQUALLY+USERFAVORITE+DOT+EVENTID+AND+EVENTLIST+DOT+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE);
				else if(categoryId!=0&&searchKeyword.equals(""))
					data = DatabaseHelper.get(SELECT+EVENTLIST+DOT+EVENTSTARTDATE+COMMA+EVENTLIST+DOT+EVENTNAME+COMMA+EVENTLIST+DOT+EVENTID+COMMA
											+EVENTLIST+DOT+EVENTIMAGE+COMMA+EVENTLIST+DOT+BUSINESSID+COMMA+USERFAVORITE+DOT+USERADDEDEVENTID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
											FROM+EVENTLIST+COMMA+USERFAVORITE+WHERE+EVENTLIST+DOT+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+
											AND+EVENTLIST+DOT+EVENTID+EQUALLY+USERFAVORITE+DOT+EVENTID+AND+EVENTLIST+DOT+CATEGORY1+EQUALLY+categoryId+
											AND+EVENTLIST+DOT+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE);
				else if(categoryId==0&&!searchKeyword.equals(""))
					data = DatabaseHelper.get(SELECT+EVENTLIST+DOT+EVENTSTARTDATE+COMMA+EVENTLIST+DOT+EVENTNAME+COMMA+EVENTLIST+DOT+EVENTID+COMMA
											+EVENTLIST+DOT+EVENTIMAGE+COMMA+EVENTLIST+DOT+BUSINESSID+COMMA+USERFAVORITE+DOT+USERADDEDEVENTID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
											FROM+EVENTLIST+COMMA+USERFAVORITE+WHERE+EVENTLIST+DOT+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+
											AND+EVENTLIST+DOT+EVENTID+EQUALLY+USERFAVORITE+DOT+EVENTID+AND+EVENTNAME+LIKE+LIKE_LEFT+searchKeyword+LIKE_RIGHT+
											AND+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE);
				else if(categoryId!=0&&!searchKeyword.equals(""))
					data = DatabaseHelper.get(SELECT+EVENTLIST+DOT+EVENTSTARTDATE+COMMA+EVENTLIST+DOT+EVENTNAME+COMMA+EVENTLIST+DOT+EVENTID+COMMA
											+EVENTLIST+DOT+EVENTIMAGE+COMMA+EVENTLIST+DOT+BUSINESSID+COMMA+USERFAVORITE+DOT+USERADDEDEVENTID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
											FROM+EVENTLIST+COMMA+USERFAVORITE+WHERE+EVENTLIST+DOT+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+
											AND+EVENTLIST+DOT+EVENTID+EQUALLY+USERFAVORITE+DOT+EVENTID+AND+EVENTLIST+DOT+CATEGORY1+EQUALLY+categoryId+
											AND+EVENTLIST+DOT+EVENTNAME+LIKE+LIKE_LEFT+searchKeyword+LIKE_RIGHT+AND+EVENTLIST+DOT+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE);
			if(data != null)
				{				
					for( int i = 0; i<data.length; i++)
					{		
						try
						{
							EventDates objEvent = new EventDates();
							objEvent.strEventStartdate = (data[i][0].value!=null?data[i][0].value.toString():"");
							objEvent.eventName = (data[i][1].value!=null?data[i][1].value.toString():"");
							objEvent.eventId =(data[i][2].value!=null?Integer.parseInt(data[i][2].value.toString()):0);
							objEvent.eventImage = (data[i][3].value!=null?data[i][3].value.toString():NOIMAGE);
							objEvent.businessId = (data[i][4].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
							objEvent.userAddedEventId = (data[i][5].value!=null?Integer.parseInt(data[i][5].value.toString()):0);
							objEvent.imageDateUpdated	=	(data[i][6].value!=null?data[i][6].value.toString():"");
							objEvent.imageId			=	(data[i][7].value!=null?data[i][7].value.toString():"0");
							objEvent.isImageUpdated =(data[i][8].value!=null?data[i][8].value.toString():"false");
							result.add(objEvent);	
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
	
	public List<EventDates> getdatesFriendsPalenderCalView(String strAttendingEvents,String eventDate,int categoryId,String searchKeyword)
	{		
		 List<EventDates> result = new ArrayList<EventDates>();
		if(!strAttendingEvents.equals(""))
		{
				DictionaryEntry[][] data=null;
				if(categoryId==0&&searchKeyword.equals(""))
					data = DatabaseHelper.get(SELECT+EVENTLIST+DOT+EVENTSTARTDATE+COMMA+EVENTLIST+DOT+EVENTNAME+COMMA+EVENTLIST+DOT+EVENTID+COMMA+EVENTLIST+DOT+EVENTIMAGE+COMMA
											+EVENTLIST+DOT+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+EVENTLIST+DOT+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTLIST+DOT+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE);
				else if(categoryId!=0&&searchKeyword.equals(""))
					data = DatabaseHelper.get(SELECT+EVENTLIST+DOT+EVENTSTARTDATE+COMMA+EVENTLIST+DOT+EVENTNAME+COMMA+EVENTLIST+DOT+EVENTID+COMMA+EVENTLIST+DOT+EVENTIMAGE+COMMA
											+EVENTLIST+DOT+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+EVENTLIST+DOT+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTLIST+DOT+CATEGORY1+EQUALLY+categoryId+
											AND+EVENTLIST+DOT+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE);
				else if(categoryId==0&&!searchKeyword.equals(""))
					data = DatabaseHelper.get(SELECT+EVENTLIST+DOT+EVENTSTARTDATE+COMMA+EVENTLIST+DOT+EVENTNAME+COMMA+EVENTLIST+DOT+EVENTID+COMMA+EVENTLIST+DOT+EVENTIMAGE+COMMA
											+EVENTLIST+DOT+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+EVENTLIST+DOT+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTNAME+LIKE+LIKE_LEFT+searchKeyword+LIKE_RIGHT+
											AND+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE);
				else if(categoryId!=0&&!searchKeyword.equals(""))
					data = DatabaseHelper.get(SELECT+EVENTLIST+DOT+EVENTSTARTDATE+COMMA+EVENTLIST+DOT+EVENTNAME+COMMA+EVENTLIST+DOT+EVENTID+COMMA+EVENTLIST+DOT+EVENTIMAGE+COMMA
											+EVENTLIST+DOT+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+EVENTLIST+DOT+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTLIST+DOT+CATEGORY1+EQUALLY+categoryId+
											AND+EVENTLIST+DOT+EVENTNAME+LIKE+LIKE_LEFT+searchKeyword+LIKE_RIGHT+AND+EVENTLIST+DOT+EVENTSTARTDATE+EQUALLY+QUOTE+eventDate+QUOTE);
			if(data != null)
				{						
					for( int i = 0; i<data.length; i++)
					{		
						try
						{
							EventDates objEvent = new EventDates();
							objEvent.strEventStartdate = (data[i][0].value!=null?data[i][0].value.toString():"");
							objEvent.eventName = (data[i][1].value!=null?data[i][1].value.toString():"");
							objEvent.eventId =(data[i][2].value!=null?Integer.parseInt(data[i][2].value.toString()):0);
							objEvent.eventImage = (data[i][3].value!=null?data[i][3].value.toString():"");
							objEvent.businessId = (data[i][4].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
							objEvent.imageDateUpdated	=	(data[i][5].value!=null?data[i][5].value.toString():"");
							objEvent.imageId			=	(data[i][6].value!=null?data[i][6].value.toString():"0");
							objEvent.isImageUpdated =(data[i][7].value!=null?data[i][7].value.toString():"false");
							result.add(objEvent);	
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

	public List<EventDates> getdatesOfUserAttendingEventsCalView(String strAttendingEvents,int selectedCategoryId,String serchKeyWord)
	{		
		List<EventDates> result = new ArrayList<EventDates>();
		if(!strAttendingEvents.equals(""))
		{
				DictionaryEntry[][] data=null;
					if(selectedCategoryId==0&&serchKeyWord.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET);
					else if(selectedCategoryId!=0&&serchKeyWord.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+EVENTID+
												IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+CATEGORY1+EQUALLY+selectedCategoryId);
					else if(selectedCategoryId==0&&!serchKeyWord.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+EVENTID+
												IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+EVENTNAME+LIKE+LIKE_LEFT+serchKeyWord+LIKE_RIGHT);
					else if(selectedCategoryId!=0&&!serchKeyWord.equals(""))
						data = DatabaseHelper.get(SELECT+EVENTSTARTDATE+COMMA+EVENTNAME+COMMA+EVENTID+COMMA+EVENTIMAGE+COMMA+BUSINESSID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+EVENTID+
												IN+LEFT_BRACKET+strAttendingEvents+RIGHT_BRACKET+AND+CATEGORY1+EQUALLY+selectedCategoryId+AND+EVENTNAME+LIKE+LIKE_LEFT+serchKeyWord+LIKE_RIGHT);
				
			if(data != null)
				{						
					for( int i = 0; i<data.length; i++)
					{		
						try
						{
							EventDates objEvent = new EventDates();
							objEvent.strEventStartdate = (data[i][0].value!=null?data[i][0].value.toString():"");
							objEvent.eventName = (data[i][1].value!=null?data[i][1].value.toString():"");
							objEvent.eventId =(data[i][2].value!=null?Integer.parseInt(data[i][2].value.toString()):0);
							objEvent.eventImage = (data[i][3].value!=null?data[i][3].value.toString():NOIMAGE);
							objEvent.businessId = (data[i][4].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
							objEvent.imageDateUpdated	=	(data[i][5].value!=null?data[i][5].value.toString():"");
							objEvent.imageId			=	(data[i][6].value!=null?data[i][6].value.toString():"0");
							objEvent.isImageUpdated =(data[i][7].value!=null?data[i][7].value.toString():"false");
							result.add(objEvent);	
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
	
	public List<EventsDetails> getFavoriteEventDetails(String strFavoriteEventId)
	{
	DictionaryEntry[][] data=DatabaseHelper.get(SELECT+EVENTLIST+DOT+EVENTID+COMMA+EVENTLIST+DOT+EVENTNAME+COMMA+EVENTLIST+DOT+EVENTSTARTDATE+COMMA+EVENTLIST+DOT+EVENTIMAGE+COMMA
												+USERFAVORITE+DOT+USERADDEDEVENTID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+COMMA+USERFAVORITE+WHERE+EVENTLIST+DOT+EVENTID+EQUALLY+USERFAVORITE+DOT+EVENTID+
												AND+USERFAVORITE+DOT+USERID+EQUALLY+AppConstants.USER_ID);
	List <EventsDetails> result = new ArrayList<EventsDetails>();
		
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					EventsDetails objEvent = new EventsDetails();
					objEvent.eventid = (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objEvent.eventname =(data[i][1].value!=null?data[i][1].value.toString():"");
					objEvent.event_start_date = (data[i][2].value!=null?data[i][2].value.toString():"");
					objEvent.image =(data[i][3].value!=null?data[i][3].value.toString():"");
					objEvent.useraddedeventid = (data[i][4].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
					objEvent.imageDateUpdated	=	(data[i][5].value!=null?data[i][5].value.toString():"");
					objEvent.imageId			=	(data[i][6].value!=null?data[i][6].value.toString():"0");
					objEvent.isImageUpdated =(data[i][7].value!=null?data[i][7].value.toString():"false");
					result.add(objEvent);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}
	
	public List<EventsDetails> getFavoriteEventDetails()
	{
	DictionaryEntry[][] data=DatabaseHelper.get(SELECT+EVENTLIST+DOT+EVENTID+COMMA+EVENTLIST+DOT+EVENTNAME+COMMA+EVENTLIST+DOT+EVENTSTARTDATE+COMMA+EVENTLIST+DOT+EVENTIMAGE+COMMA
												+USERFAVORITE+DOT+USERADDEDEVENTID+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+COMMA+USERFAVORITE+WHERE+EVENTLIST+DOT+EVENTID+EQUALLY+USERFAVORITE+DOT+EVENTID+
												AND+USERFAVORITE+DOT+USERID+EQUALLY+AppConstants.USER_ID);
	List <EventsDetails> result = new ArrayList<EventsDetails>();
		
		if(data != null)
		{						
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					EventsDetails objEvent = new EventsDetails();
					objEvent.eventid = (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objEvent.eventname =(data[i][1].value!=null?data[i][1].value.toString():"");
					objEvent.event_start_date = (data[i][2].value!=null?data[i][2].value.toString():"");
					objEvent.image =(data[i][3].value!=null?data[i][3].value.toString():NOIMAGE);
					objEvent.useraddedeventid = (data[i][4].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
					objEvent.imageDateUpdated	=	(data[i][5].value!=null?data[i][5].value.toString():"");
					objEvent.imageId			=	(data[i][6].value!=null?data[i][6].value.toString():"0");
					objEvent.isImageUpdated =(data[i][7].value!=null?data[i][7].value.toString():"false");
					result.add(objEvent);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}			
		}
		return result;
	}

	public List<Category> getAllCategories()
	{
		DictionaryEntry[][] data=DatabaseHelper.get(SELECT+MAINCATEGORYID+COMMA+MAINCATEGORYNAME+FROM+CATEGORYLIST+ORDER_BY+MAINCATEGORYNAME);
	   
		List<Category> result = new ArrayList<Category>();
			
			if(data != null)
			{						
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						Category objCategory = new Category();
						objCategory.maincategory = (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
						objCategory.maincategoryname =(data[i][1].value!=null?data[i][1].value.toString():"");
						result.add(objCategory);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
		return result;
	}
	
	public static List<EventsDetails> getEventOnDate(String strDate ,int seletedCategoryId,String strSearchKeyword)
	{
		DictionaryEntry[][] data=null;
		if(seletedCategoryId==0 && strSearchKeyword.equals(""))
			data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTIMAGE+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+EVENTSTARTDATE+EQUALLY+QUOTE+strDate+QUOTE);
		else if(seletedCategoryId!=0 && strSearchKeyword.equals(""))
			data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTIMAGE+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+EVENTSTARTDATE+EQUALLY+QUOTE+strDate+QUOTE+
									AND+LEFT_BRACKET+CATEGORY1+EQUALLY+seletedCategoryId+OR+CATEGORY2+EQUALLY+seletedCategoryId+RIGHT_BRACKET);//OR CATEGORY2="+seletedCategoryId+ EVENTNAME LIKE '%"+strSearchKeyword+"%'"
		else if(seletedCategoryId==0 && !strSearchKeyword.equals(""))
			data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTIMAGE+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+EVENTSTARTDATE+EQUALLY+QUOTE+strDate+QUOTE+AND+LEFT_BRACKET+EVENTNAME+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+RIGHT_BRACKET);//OR CATEGORY2="+seletedCategoryId+ EVENTNAME LIKE '%"+strSearchKeyword+"%'"
		else if(seletedCategoryId!=0 && !strSearchKeyword.equals(""))
			data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTIMAGE+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+FROM+EVENTLIST+WHERE+EVENTSTARTDATE+EQUALLY+QUOTE+strDate+QUOTE+AND+LEFT_BRACKET+CATEGORY1+EQUALLY+seletedCategoryId+OR+CATEGORY2+EQUALLY+seletedCategoryId+RIGHT_BRACKET+
									AND+LEFT_BRACKET+EVENTNAME+LIKE+LIKE_LEFT+strSearchKeyword+LIKE_RIGHT+RIGHT_BRACKET);//OR CATEGORY2="+seletedCategoryId+ EVENTNAME LIKE '%"+strSearchKeyword+"%'"

		List<EventsDetails> result = new ArrayList<EventsDetails>();
		   
			if(data != null)
			{						
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						EventsDetails objEvent = new EventsDetails();
						objEvent.eventid =(data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
						objEvent.eventname =(data[i][1].value!=null?data[i][1].value.toString():"");
						objEvent.image=(data[i][2].value!=null?data[i][2].value.toString():"");
						objEvent.imageDateUpdated	=	(data[i][3].value!=null?data[i][3].value.toString():"");
						objEvent.imageId			=	(data[i][4].value!=null?data[i][4].value.toString():"0");
						objEvent.isImageUpdated =(data[i][5].value!=null?data[i][5].value.toString():"false");
						result.add(objEvent);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
		return result;
	}
	
	
	
	public long insertUserFavorites(FavoriteEvent objFavoriteEvent)
	{
		String WHEREClause = EVENTID+EQUALLY+QUOTE+objFavoriteEvent.eventId+QUOTE+AND+USERID+EQUALLY+AppConstants.USER_ID;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+USERID+FROM+USERFAVORITE+WHERE+WHEREClause);
		ContentValues values = new ContentValues();
		values.put(USERID, AppConstants.USER_ID);
		values.put(EVENTID, objFavoriteEvent.eventid);
		values.put(USERADDEDEVENTID, objFavoriteEvent.useraddedeventid);
		if(data != null && data[0][0].value != null)
		{
			return DatabaseHelper.doUpdate(USERFAVORITE, values, WHEREClause, null);
		}
		else
		{
			return DatabaseHelper.doInsert(USERFAVORITE, values);
		}
	}
	
//	public boolean deleteUserFavorites(){
//	     return  DatabaseHelper.deleteAllDataFromTable(USERFAVORITE);	
//	}
	
	
	public List<EventsDetails> getUpcomingEvent(int userId)
	{
		DictionaryEntry[][] data=DatabaseHelper.get(SELECT+EVENTID+COMMA+EVENTNAME+COMMA+EVENTIMAGE+COMMA+EVENTSTARTDATE+COMMA+BUSINESSID+COMMA+EVENTSTARTTIME+COMMA+EVENTPRICE+
													COMMA+EVENTMAINIMAGE+COMMA+EVENTIMAGEDATEUPDATED+COMMA+EVENTIMAGEID+COMMA+EVENTIMAGEISUPDATED+
													FROM+EVENTLIST+WHERE+EVENTID+IN+LEFT_BRACKET+SELECT+EVENTID+FROM+ATTENDING+WHERE+USER_ID+EQUALLY+userId+RIGHT_BRACKET);
	       
			
		List<EventsDetails> result = new ArrayList<EventsDetails>();
			
			if(data != null)
			{					
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						EventsDetails objEvent = new EventsDetails();
						objEvent.eventid =  (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
						objEvent.eventname = (data[i][1].value!=null?data[i][1].value.toString():"");
						objEvent.image= (data[i][2].value!=null?data[i][2].value.toString():NOIMAGE);
						objEvent.event_start_date =(data[i][3].value!=null?data[i][3].value.toString():"");
						objEvent.business=(data[i][4].value!=null?Integer.parseInt(data[i][4].value.toString()):0);
						objEvent.event_start_time = (data[i][5].value!=null?data[i][5].value.toString():"");
						objEvent.price = (data[i][6].value!=null?data[i][6].value.toString():"");
						objEvent.imageDateUpdated	=	(data[i][7].value!=null?data[i][7].value.toString():"");
						objEvent.imageId			=	(data[i][8].value!=null?data[i][8].value.toString():"0");
						objEvent.isImageUpdated     =   (data[i][9].value!=null?data[i][9].value.toString():"false");
						result.add(objEvent);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}			
			}
			return result;
	}
	
	public static List<Business> getBusinessInformation(String businessId)
	{
		List<Business> result = new ArrayList<Business>();
		DictionaryEntry[][] data=DatabaseHelper.get(SELECT+ID+COMMA+BUSINESSTYPE+COMMA+FAX+COMMA+BIZNAME+COMMA+EIN+COMMA+PHONE+COMMA+SUPPORTEMAIL+COMMA+CONTACTEMAIL+COMMA+URL+COMMA+CONTACTNAME+
													FROM+BUSINESSLIST+WHERE+ID+EQUALLY+QUOTE+businessId+QUOTE);
		   if(data != null)
			{						
				for( int i = 0; i<data.length; i++)
				{		
					try
					{
						Business objBusiness = new Business();
						objBusiness.businessid =(data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
						objBusiness.businesstype =(data[i][1].value!=null?data[i][1].value.toString():"");
						objBusiness.faxnumber=(data[i][2].value!=null?data[i][2].value.toString():"");
						objBusiness.businessname =(data[i][3].value!=null?data[i][3].value.toString():"");
						objBusiness.Ein=(data[i][4].value!=null?data[i][4].value.toString():"");
						objBusiness.phonenumber =(data[i][5].value!=null?data[i][5].value.toString():"");
						objBusiness.supportemail=(data[i][6].value!=null?data[i][6].value.toString():"");
						objBusiness.contactemail =(data[i][7].value!=null?data[i][7].value.toString():"");
						objBusiness.url=(data[i][8].value!=null?data[i][8].value.toString():"");
						objBusiness.contactname =(data[i][9].value!=null?data[i][9].value.toString():"");
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
	
	public void setImageStatusLoaded(int eventId){
		ContentValues values = new ContentValues();
		values.put(EVENTIMAGEISUPDATED, "false");
		DatabaseHelper.doUpdate(EVENTLIST, values, EVENTID+EQUALLY+eventId, null);	
	}

}
