package com.winit.ga2oo.common;

import java.util.ArrayList;
import java.util.List;

import com.winit.ga2oo.objects.Attending;
import com.winit.ga2oo.objects.Business;
import com.winit.ga2oo.objects.BusinessType;
import com.winit.ga2oo.objects.Category;
import com.winit.ga2oo.objects.EventCategory;
import com.winit.ga2oo.objects.EventImages;
import com.winit.ga2oo.objects.EventLocation;
import com.winit.ga2oo.objects.EventsDetails;
import com.winit.ga2oo.objects.EventsDetailsData;
import com.winit.ga2oo.objects.FavoriteEvent;
import com.winit.ga2oo.objects.Friend;
import com.winit.ga2oo.objects.Globalization;
import com.winit.ga2oo.objects.Notifications;
import com.winit.ga2oo.objects.UserAccount;
import com.winit.ga2oo.objects.UserCurrentLocationBasedOnZip;
import com.winit.ga2oo.objects.UserFriend;
import com.winit.ga2oo.objects.UserLocation;
import com.winit.ga2oo.objects.UserLocationObject;
import com.winit.ga2oo.objects.UserRecommendationObject;


public class AppConstants 
{
	
//	public static final String HOST_URL = "http://5.ga2ootesting.appspot.com/rest/v1/";
//	public static final String IMAGE_HOST_URL="http://5.ga2ootesting.appspot.com/";
	public static final String JSON_HOST_URL = "http://ga2ooprod.appspot.com/rest/v2/";
	public static final String IMAGE_HOST_URL="http://ga2ooprod.appspot.com";
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
	public static final String UPDTAE_PROFILE="user/update/";
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
	
	public static final String NOIMAGE="no image";

}
