package com.ga2oo.palendar.common;

import java.util.ArrayList;
import java.util.List;

import com.ga2oo.palendar.objects.Attending;
import com.ga2oo.palendar.objects.Business;
import com.ga2oo.palendar.objects.BusinessType;
import com.ga2oo.palendar.objects.Category;
import com.ga2oo.palendar.objects.EventCategory;
import com.ga2oo.palendar.objects.EventImages;
import com.ga2oo.palendar.objects.EventLocation;
import com.ga2oo.palendar.objects.EventsDetails;
import com.ga2oo.palendar.objects.EventsDetailsData;
import com.ga2oo.palendar.objects.FavoriteEvent;
import com.ga2oo.palendar.objects.Friend;
import com.ga2oo.palendar.objects.Globalization;
import com.ga2oo.palendar.objects.Notifications;
import com.ga2oo.palendar.objects.UserAccount;
import com.ga2oo.palendar.objects.UserCurrentLocationBasedOnZip;
import com.ga2oo.palendar.objects.UserFriend;
import com.ga2oo.palendar.objects.UserLocation;
import com.ga2oo.palendar.objects.UserLocationObject;
import com.ga2oo.palendar.objects.UserRecommendationObject;


public class AppConstants 
{
	
//	public static final String HOST_URL = "http://5.ga2ootesting.appspot.com/rest/v1/";
//	public static final String IMAGE_HOST_URL="http://5.ga2ootesting.appspot.com/";
//    public static final String JSON_HOST_URL = "http://ga2ooprod.appspot.com/rest/v2/";
	public static final String JSON_HOST_URL = "http://api.thepalendar.com/rest/v2/";
	public static final String IMAGE_HOST_URL="";  // this should be empty since the image url has the http 
	public static final String Authenticate_User_URL =  "user/signin/";
	public static final String USER_ACCOUNT_URL = "user/id/";
	public static final String GET_USER_LOCATION_URL = "user/savedlocations/id/";
	public static final String POST_USER_LOCATION_URL = "user/savedlocations/";
	public static final String UPDATE_PRIAMRY_LOCATION="user/savedlocations/updateprimary";
	public static final String EVENT_LIST= "event/filter";
	public static final String FRIEND_URL= "user/friends/id/";
	public static final String ALL_GA2OO_USER="user/list/";
	public static final String BUSINESS_LIST_URL= "business/list/";
	public static final String EVENT_DETAILS = "event/id/";
	public static final String FRIEND_DELETE="user/friends/delete/id/";
	public static final String USER_REGISTRATION="user/register";
	public static final String BUSINESS_REGISTRATION="business/register";
	public static final String ADD_EVENT="user/events/";
	public static final String ADD_BUSINESS="user/businesses/";
	public static final String USER_IMAGE="user/img";
	public static final String ADD_FRIEND="user/friends/";
	public static final String CATEGORY_LIST="categories/list";
	public static final String UPDATE_PROFILE="user/update/";
	public static final String USER_NOTIFICATIONS="user/notifications/received/id/";
	public static final String EVENT_FILTER="event/filter?";
	public static final String BUSINESS_DETAIL="business/id/";
	public static final String FRIEND_REQUEST_RESPONCE="user/friendresponse";
	public static final String USER_INBOX="user/inbox/received/id/";
	public static final String USER_RECOMMENDATION="user/recommendevents";
	public static final String DELETE_ID = "/deleteids/";
	public static final String BUSINES_TYPES = "business/businesstypes/list";
	public static final String GET_USER_EVENTS = "user/events/id/";
	public static final String MARKREAD = "/markread/";
	public static final String FINDGATOOUSERS = "user/find?param=";
	public static final String RECOVER_PASSWORD = "user/recoverpassword/";
	public static final String DATERANGE = "daterange=";
	public static final String EVENTFILTER = "event/filter?startdate=";
	public static int USER_ID;
	public static boolean isProfileUpdated=false;
	public static String googleApiKey="AIzaSyAaIDsz4n8sACcuJrGpnwkSfIBrUZwjX9Y";
	public static String strAPIURL="http://maps.google.com/maps/geo?key=AIzaSyAaIDsz4n8sACcuJrGpnwkSfIBrUZwjX9Y&output=xml&q=";
	
	public static List<UserAccount> vctUserAccount;
	public static List<UserFriend> vctUserFriend;
	public static List<UserLocationObject> vctUserLocation;
	public static List<EventsDetails> vctEvents;
	public static List<UserLocation> vctUserLocationInfo;
	public static List<Friend> vctFriend;
	public static List<BusinessType> vctBusinessType;
	public static List<Business> vctBusiness;
	public static List<Globalization> vctGlobalization;
	public static List<EventCategory> vctEventCategory;
	public static List<Category> vctCategory;
	public static List<EventImages> vctEventImages;
	public static List<EventsDetailsData> vctEventsDetails;
	public static List<Attending> vctEventAttending;
	public static List<FavoriteEvent> vctUserFavorites;
	public static List<Business> vctUserFavoritesBusiness;
	public static List<Notifications> vctUserNotifications;
	public static ArrayList<EventsDetails> vctFilteredEvents;
	public static List<Business> vctBusinessDetail;
	public static List<UserCurrentLocationBasedOnZip> vctUserCurrentLocation;
	public static List<UserRecommendationObject> vctUserRecommendations;
	public static int DEVICE_DISPLAY_WIDTH;
	public static ArrayList<EventLocation> vctEventLocation;
	
	public static final String APP_ID = "148110835259569";
	public static final String Flurry_Api_Key = "FDQ6ZW6TB8C3SM9PY3GJ";
	
	
	public static final String NOIMAGE="no image";

}
