package com.winit.ga2oo.xmlparsers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

import com.winit.ga2oo.businesslayer.UserAccountBusinessLayer;
import com.winit.ga2oo.common.AppConstants;
import com.winit.ga2oo.objects.Association;
import com.winit.ga2oo.objects.BusinessRegistration;
import com.winit.ga2oo.objects.UserAccount;
import com.winit.ga2oo.objects.UserCurrentLocationBasedOnZip;
import com.winit.ga2oo.objects.UserLocationObject;

public class Ga2ooParsers 
{
//	private int status;
//	private InputStream responceInputStream;
//	private DocumentBuilder loginXmlBuilder,regXmlBuilder;
//	private UrlPost urlPost;
//	private UserAccountBusinessLayer userAccBL;
//	private Vector<UserLocationObject> vctUserLocation;
//	private Vector<Association> vctUserAssociations;
//	private Vector<UserAccount> vctUserAccount;
//	private DocumentBuilder addEventXmlBuilder;
//	private String recommendationStatus;
//	public static String regMessage="",addFriendMessage="",updateUserProfileMgs;
//	
	/*
	 * method used to authenticate a user 
	 */
//	public int LoginStatus(String username, String password)
//	{
//		try
//		{
//			String strLogin = "<useraccount>" 
//									+"<username>"+username+"</username>"
//									+"<password>"+password+"</password>"
//							  +"</useraccount>";
//			
//			
//			urlPost = new UrlPost();
//			responceInputStream = urlPost.loginPost(strLogin,new URL(AppConstants.HOST_URL+AppConstants.Authenticate_User_URL));			
//			
//			loginXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			Document xmlDoc = loginXmlBuilder.parse(responceInputStream);
//
//			NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//			if(xmlRoot.getLength() != 0)
//			{
//				//Node rootChild = xmlRoot.item(0);
//			}
//			
//			NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//			if(xmlRoot1.getLength() != 0)
//			{
//				Node rootChild = xmlRoot1.item(0);
//				status += Integer.parseInt(rootChild.getFirstChild().getNodeValue().toString());
//			}
//		Log.i("STATUS",""+status);
//		}			
//		
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	return status;
//	}
	
	/*
	 * end of method  authenticate a user 
	 */
	
	
	
//	public void userAccountParser(int userId)
//	{
//		try
//		{
//			String strLogin = "";
//			urlPost = new UrlPost();
//			responceInputStream = urlPost.loginPost(strLogin,new URL(AppConstants.HOST_URL+AppConstants.USER_ACCOUNT_URL));			
//			loginXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//		}			
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//	
	
	/*
	 * method addEventToUser is used to add new events.
	 */
//	public int addEventToUser(int user_id, int eventid)
//	{
//		try
//		{
//			String strAddEvent = "<useraccount>"
//									 +"<id>"+user_id+"</id>"
//									 +"<events>"
//									 		+"<event>" +
//									 				"<eventid>"+eventid+"</eventid>"
//									 		+"</event>"
//								   	 +"</events>"
//								 +"</useraccount>";
//			
//			Log.i("this", strAddEvent);
//			urlPost = new UrlPost();
//			responceInputStream= urlPost.registrationPost(strAddEvent, new URL(AppConstants.HOST_URL+AppConstants.ADD_EVENT));
//			addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//			
//			NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//			if(xmlRoot.getLength() != 0)
//			{
//				//Node rootChild = xmlRoot.item(0);
//			}
//
//			NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("results");
//			if(xmlRoot.getLength() != 0)
//			{
//				//Node rootChild = xmlRoot.item(0);
//			}
//			
//			NodeList xmlRoot2 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//			if(xmlRoot1.getLength() != 0)
//			{
//				Node rootChild1 = xmlRoot2.item(0);
//				status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//			}
//		}
//		catch(Exception ee)
//		{
//			Log.i("error is here",""+ee.toString());
//		}
//	Log.i("statusss",""+status);
//	return status;
//	}
	/*
	 *end of  method addEventToUser.
	 */
	
	/*
	 * method deleteEventToUser is used to add new events.
	 */
//	public int deleteUserEvent(int user_id, int eventAddedId)
//	{
//		try
//		{
//			String strDeleteEvent = "";//"<useraccount>"
////									 +"<id>"+user_id+"</id>"
////									 +"<events>"
////									 	+"<addedeventid>"+eventAddedId+"</addedeventid>"
////								   	 +"</events>"
////								 +"</useraccount>";
//			
//			Log.i("this", AppConstants.HOST_URL+AppConstants.ADD_EVENT+"id/"+AppConstants.USER_ID+"/deleteids/"+eventAddedId);
//			urlPost = new UrlPost();
//			responceInputStream= urlPost.userDeleteRequest(strDeleteEvent, new URL(AppConstants.HOST_URL+AppConstants.ADD_EVENT+"id/"+AppConstants.USER_ID+"/deleteids/"+eventAddedId));
//			addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//			
//			NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//			if(xmlRoot.getLength() != 0)
//			{
//				//Node rootChild = xmlRoot.item(0);
//			}
//
//			NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("results");
//			if(xmlRoot.getLength() != 0)
//			{
//				//Node rootChild = xmlRoot.item(0);
//			}
//			
//			NodeList xmlRoot2 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//			if(xmlRoot1.getLength() != 0)
//			{
//				Node rootChild1 = xmlRoot2.item(0);
//				status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//			}
//		}
//		catch(Exception ee)
//		{
//			Log.i("error is here",""+ee.toString());
//		}
//	Log.i("statusss",""+status);
//	return status;
//	}
	/*
	 *end of  method deleteEventToUser.
	 */
	
	
//	public int addBusinessToUser(int user_id, int businessId)
//	{
//		try
//		{
//			String strAddEvent = "<useraccount>"
//									 +"<id>"+user_id+"</id>"
//									 +"<fav_businesses>"
//									 		+"<business>" +
//									 				"<businessid>"+businessId+"</businessid>"
//									 		+"</business>"
//								   	 +"</fav_businesses>"
//								 +"</useraccount>";
//			
//			Log.i("this", strAddEvent);
//			urlPost = new UrlPost();
//			responceInputStream= urlPost.registrationPost(strAddEvent, new URL(AppConstants.HOST_URL+AppConstants.ADD_BUSINESS));
//			addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//			
//			NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//			if(xmlRoot.getLength() != 0)
//			{
//				//Node rootChild = xmlRoot.item(0);
//			}
//
//			NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("results");
//			if(xmlRoot.getLength() != 0)
//			{
//				//Node rootChild = xmlRoot.item(0);
//			}
//			
//			NodeList xmlRoot2 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//			if(xmlRoot1.getLength() != 0)
//			{
//				Node rootChild1 = xmlRoot2.item(0);
//				status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//			}
//		}
//		catch(Exception ee)
//		{
//			Log.i("error is here",""+ee.toString());
//		}
//	Log.i("statusss",""+status);
//	return status;
//	}
	/*
	 *end of  method addBusinessToUser.
	 */

//	public int deleteUserBusiness(int user_id, int eventAddedId)
//	{
//		try
//		{
//			String strDeleteBusiness = "";//"<useraccount>"
////									 +"<id>"+user_id+"</id>"
////									 +"<fav_businesses>"
////									 	+"<addedbusinessid>"+eventAddedId+"</addedbusinessid>"
////								   	 +"</fav_businesses>"
////								 +"</useraccount>";
//			
//			Log.i("this", strDeleteBusiness);
//			urlPost = new UrlPost();
//			responceInputStream= urlPost.userDeleteRequest(strDeleteBusiness, new URL(AppConstants.HOST_URL+AppConstants.ADD_BUSINESS+"id/"+AppConstants.USER_ID+"/deleteids/"+eventAddedId));
//			addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//			NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//			if(xmlRoot.getLength() != 0)
//			{
//				//Node rootChild = xmlRoot.item(0);
//			}
//
//			NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("results");
//			if(xmlRoot.getLength() != 0)
//			{
//				//Node rootChild = xmlRoot.item(0);
//			}
//			
//			NodeList xmlRoot2 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//			if(xmlRoot1.getLength() != 0)
//			{
//				Node rootChild1 = xmlRoot2.item(0);
//				status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//			}
//		}
//		catch(Exception ee)
//		{
//			Log.i("error is here",""+ee.toString());
//		}
//	Log.i("statusss",""+status);
//	return status;
//	}
	/*
	 *end of  method delete Business.
	 */

	/*
	 * method deleteUserFriend is used to delete friend of an user if user want
	 */
//	public int deleteUserFriend(int userFriendId)
//	{
//		try
//		{
//			String strRequest="";
//			urlPost = new UrlPost();
//			responceInputStream = urlPost.userDeleteRequest(strRequest,new URL(AppConstants.HOST_URL+AppConstants.FRIEND_URL+AppConstants.USER_ID+"/deleteids/"+userFriendId));
//		    addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//		    
////		    InputStreamReader reader = new InputStreamReader(responceInputStream );
////		    StringBuilder buf = new StringBuilder();
////		    char[] cbuf = new char[ 2048 ];
////		    int num;
////		    while ( -1 != (num=reader.read( cbuf )))
////		    {
////		          buf.append( cbuf, 0, num );
////		    }
////		    String result = buf.toString();
////			System.err.println( "\nResponse from server after delete friend request = " + result );
//		    
//		    Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//
//		    NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//		    if(xmlRoot.getLength() != 0)
//		    {
//		    	//Node rootChild = xmlRoot.item(0);
//		    }
//		    
//		    NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("results");
//		    if(xmlRoot1.getLength() != 0)
//		    {
//		    	//Node rootChild = xmlRoot.item(0);
//		    }
//		    
//		    NodeList xmlRoot2 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//		    if(xmlRoot2.getLength() != 0)
//		    {
//		    	Node rootChild1 = xmlRoot2.item(0);
//		    	status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//		    }
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	Log.i("delete friend",""+status);
//	return status;
//	}
	/*
	 *end of  method deleteUserFriend .
	 */
	
	
	
	/*
	 * method addFriend is used to add new friends
	 */
//	public int addFriend(int userId,int friendId)
//	{
//		try
//		{
//			String strFriendRequest="<useraccount>" +
//											"<id>"+userId+"</id>" +
//											"<friendships>" +
//												"<friend>" +
//													"<friendid>"+friendId+"</friendid>" +
//												"</friend>" +
//											"</friendships>" +
//									"</useraccount>";
//			urlPost = new UrlPost();
//			Log.i("strFriendRequest",strFriendRequest);
//			responceInputStream = urlPost.friendRequest(strFriendRequest,new URL(AppConstants.HOST_URL+AppConstants.ADD_FRIEND));
//			
//            addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//     		Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//      			
//      		NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//      		if(xmlRoot.getLength() != 0)
//      		{
//      			//Node rootChild = xmlRoot.item(0);
//      		}
//      		
//      		NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("results");
//      		if(xmlRoot1.getLength() != 0)
//      		{
//      			//Node rootChild = xmlRoot.item(0);
//      		}
//      		
//      		NodeList xmlRoot2 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//      		if(xmlRoot2.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot2.item(0);
//      		}
//      		NodeList xmlRoot3 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("code");
//      		if(xmlRoot3.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot3.item(0);
//      			status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//      		}
//      		NodeList xmlRoot4 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("message");
//      		if(xmlRoot4.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot4.item(0);
//      			addFriendMessage =rootChild1.getFirstChild().getNodeValue().toString();
//      		}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		Log.i("message",""+addFriendMessage+""+status);
//	return status;
//	}
	/*
	 *end of  method addFriend .
	 */
	
//	public int friendRequestResponce(int userId,int useraddedfriendid,int responce)
//	{
//		try
//		{
//			String strFriendRequest="<useraccount>" +
//											"<id>"+userId+"</id>" +
//											"<friendships>" +
//												"<friend>" +
//													"<useraddedfriendid>"+useraddedfriendid+"</useraddedfriendid>" +
//													"<status>"+responce+"</status>"+ 
//												"</friend>" +
//											"</friendships>" +
//									"</useraccount>";
//			urlPost = new UrlPost();
//			Log.i("strFriendRequest",strFriendRequest);
//			responceInputStream = urlPost.friendRequest(strFriendRequest,new URL(AppConstants.HOST_URL+AppConstants.FRIEND_REQUEST_RESPONCE));
//			Log.i("url",AppConstants.HOST_URL+AppConstants.FRIEND_REQUEST_RESPONCE);
//            addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//     		Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//      		NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//      		if(xmlRoot.getLength() != 0)
//      		{
//      			//Node rootChild = xmlRoot.item(0);
//      		}
//      		
//      		NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("results");
//      		if(xmlRoot1.getLength() != 0)
//      		{
//      			//Node rootChild = xmlRoot.item(0);
//      		}
//      		
//      		NodeList xmlRoot2 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//      		if(xmlRoot2.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot2.item(0);
//      		}
//      		NodeList xmlRoot3 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("code");
//      		if(xmlRoot3.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot3.item(0);
//      			status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//      		}
//      		NodeList xmlRoot4 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("message");
//      		if(xmlRoot4.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot4.item(0);
//      			addFriendMessage =rootChild1.getFirstChild().getNodeValue().toString();
//      		}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		Log.i("message",""+addFriendMessage+""+status);
//	return status;
//	}	
	
//	public int notificationMarkasRead(int notifiactionId)
//	{
//		try
//		{
//			String strMarkNotificationasRead="";
//			urlPost = new UrlPost();
//			Log.i("strMarkNotificationasRead",strMarkNotificationasRead);
//			responceInputStream = urlPost.notificationResponce(strMarkNotificationasRead,new URL(AppConstants.HOST_URL+AppConstants.USER_NOTIFICATIONS+AppConstants.USER_ID+"/markread/"+notifiactionId));
//			Log.i("url",AppConstants.HOST_URL+AppConstants.USER_NOTIFICATIONS+AppConstants.USER_ID+"/markread/"+notifiactionId);
//            addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//     		Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//      		NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//      		if(xmlRoot.getLength() != 0)
//      		{
//      			//Node rootChild = xmlRoot.item(0);
//      		}
//      		
//      		NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("results");
//      		if(xmlRoot1.getLength() != 0)
//      		{
//      			//Node rootChild = xmlRoot.item(0);
//      		}
//      		
//      		NodeList xmlRoot2 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//      		if(xmlRoot2.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot2.item(0);
//      		}
//      		NodeList xmlRoot3 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("code");
//      		if(xmlRoot3.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot3.item(0);
//      			status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//      		}
//      		NodeList xmlRoot4 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("message");
//      		if(xmlRoot4.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot4.item(0);
//      			addFriendMessage =rootChild1.getFirstChild().getNodeValue().toString();
//      		}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		Log.i("message",""+addFriendMessage+""+status);
//	return status;
//	}
	/*
	 * method registerNewUser is used to send request of new user
	 */
//	public int registerNewUser(Vector<com.winit.ga2oo.objects.Registration> vctRegData)
//	{
//		try
//		{
//			if(AppConstants.vctUserCurrentLocation==null)
//				AppConstants.vctUserCurrentLocation=new Vector<UserCurrentLocationBasedOnZip>();
//			String strRequest="<useraccount>"
//								  +"<email>"+vctRegData.get(0).strEmail+"</email>"
//								  +"<username>"+vctRegData.get(0).strUserName+"</username>"
//	                              +"<firstname>"+vctRegData.get(0).strFName+"</firstname>"
//	                              +"<lastname>"+vctRegData.get(0).strLName+"</lastname>"
//	                              +"<currentzip>None</currentzip>"
//	                              +"<gender>"+vctRegData.get(0).strGender+"</gender>"
//	                              +"<birthday>"+vctRegData.get(0).strDOB+"</birthday>"
//	                              +"<password>"+vctRegData.get(0).strPassword+"</password>"
//	                              +"<is_active>True</is_active>"
//	                              +"<is_public>True</is_public>"
//	                              +"<is_calendar_shared>True</is_calendar_shared>"
//	                              +"<deviceid>None</deviceid>"
//	                       		  +"<savedlocations>"
//	 									+"<location>"
//	 										+"<is_primary>True</is_primary>"
//											+"<geocode>None</geocode>"
//											+"<address>"+AppConstants.vctUserCurrentLocation.get(0).strAddress+"</address>"
//											+"<city>"+AppConstants.vctUserCurrentLocation.get(0).strCity+"</city>"
//											+"<state>"+AppConstants.vctUserCurrentLocation.get(0).strState+"</state>"
//											+"<zipcode>"+AppConstants.vctUserCurrentLocation.get(0).zipCode+"</zipcode>"
//											+"<country>"+AppConstants.vctUserCurrentLocation.get(0).strCountryName+"</country>"
//	 									+"</location>"
//	 							 +"</savedlocations>"
//	 						+"</useraccount>";//+AppConstants.vctUserCurrentLocation.get(0).strGeoCode+
//					
//			Log.i("",strRequest);
//			urlPost = new UrlPost();
//			responceInputStream = urlPost.registrationPost(strRequest,new URL(AppConstants.HOST_URL+AppConstants.USER_REGISTRATION));
//
//            regXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//    	    Document xmlDoc = regXmlBuilder.parse(responceInputStream);
// 		    
//    	    NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//			if(xmlRoot.getLength() != 0)
//			{
//				//Node rootChild = xmlRoot.item(0);
//			}
//			
//			NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("code");
//			if(xmlRoot1.getLength() != 0)
//			{
//				Node rootChild1 = xmlRoot1.item(0);
//				status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//			}
//			
//			NodeList xmlRoot2 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("message");
//			if(xmlRoot1.getLength() != 0)
//			{
//				Node rootChild1 = xmlRoot2.item(0);
//				regMessage = rootChild1.getFirstChild().getNodeValue().toString();
//			}
//		Log.i("STATUS",""+status);
//		}
//		catch(Exception e)
//		{
//		e.printStackTrace();
//		}
//		
//		return status;
//	}
	/*
	 * end of method registerNewUser.
	 */
	
	
//	public int registerNewBusiness(Vector<BusinessRegistration> vctBusinessRegistration)
//	{
//		try
//		{
//			String strBusinessRegReq="<useraccount>"+
//									"<bizname>"+vctBusinessRegistration.get(0).strCompanyName+"</bizname>" +
//									"<contactname>"+vctBusinessRegistration.get(0).strName+"</contactname>" +
//									"<businesstype>"+vctBusinessRegistration.get(0).intBusinessType+"</businesstype>" +//"+vctBusinessRegistration.get(0).strBusinessType+"
//									"<contactemail>"+vctBusinessRegistration.get(0).strEmail+"</contactemail>" +
//									"<supportemail>None</supportemail>" +
//									"<phonenumber>"+vctBusinessRegistration.get(0).strPhNo+"</phonenumber>" +
//									"<password>"+vctBusinessRegistration.get(0).strPassword+"</password>"+
//									"<is_active>True</is_active>" +
//									"<date_updated>True</date_updated>" +
//											"<location>"+
//												"<geocode>None</geocode>"+
//												"<address>"+vctBusinessRegistration.get(0).strAddress+"</address>"+
//												"<city>"+vctBusinessRegistration.get(0).strCity+"</city>"+
//												"<state>"+vctBusinessRegistration.get(0).strState+"</state>"+
//												"<zipcode>"+vctBusinessRegistration.get(0).strZip+"</zipcode>"+
//												"<country>"+vctBusinessRegistration.get(0).strCountry+"</country>"+
//											"</location>"+
//									"</useraccount>";
//		Log.i("",strBusinessRegReq);
//		urlPost = new UrlPost();
//		responceInputStream = urlPost.registrationPost(strBusinessRegReq,new URL(AppConstants.HOST_URL+AppConstants.BUSINESS_REGISTRATION));
//          
//        regXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//		Document xmlDoc = regXmlBuilder.parse(responceInputStream);
//		
//		NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("business");
//		if(xmlRoot.getLength() != 0)
//		{
//			//Node rootChild = xmlRoot.item(0);
//		}
//		
//		NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//		if(xmlRoot1.getLength() != 0)
//		{
//			Node rootChild1 = xmlRoot1.item(0);
//			status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//		}
//		Log.i("STATUS",""+status);
//		//currently data posting to server is remaining
//	  }
//	  catch (Exception e)
//	  {
//		Log.e("error",e.toString());
//	  }
//	 return status;	
//   }
	
	
	/*
	 * method uploadUserImage used to upload new image of user.
	 */
//	 public int uploadUserImage(int userId,String imageAsciiCode)
//	 {
//		 try
//		 {
//				String strImageUploadReq = "<useraccount>"
//												 +"<id>"+userId+"</id>" 
//												 +"<image>"
//												 +imageAsciiCode
//												 +"</image>"
//											+"</useraccount>";
//				Log.i("this", strImageUploadReq);
//				urlPost = new UrlPost();
//				responceInputStream= urlPost.registrationPost(strImageUploadReq, new URL(AppConstants.HOST_URL+AppConstants.USER_IMAGE));
//				
//				addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//				Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//				
//				NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//				if(xmlRoot.getLength() != 0)
//				{
//					//Node rootChild = xmlRoot.item(0);
//				}
//			
//				NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//				if(xmlRoot1.getLength() != 0)
//				{
//					Node rootChild1 = xmlRoot1.item(0);
//					status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//				}
//			}
//			catch(Exception ee)
//			{
//				Log.i("error is here",""+ee.toString());
//			}
//			Log.i("status",""+status);
//			return status;
//	 }	
	/*
		 * end of method uploadUserImage
		 */
	 
	 
	 	/*
		 * method uploadUserImage used to upload new image of user.
		 */
//	public int addUserSavedLocation(Vector<UserLocationObject> userSavedLoaction)
//	{
//		 try
//		 {
//				String SavedLocationReq = "<useraccount>" +
//														"<id>"+AppConstants.USER_ID+"</id>" +
//															"<savedlocations>" +
//																"<location>" +
//																	"<is_primary>"+userSavedLoaction.get(0).Is_Primary+"</is_primary>"+
//																	"<geocode>"+userSavedLoaction.get(0).GeoCode+"</geocode>"+
//																	"<address>"+userSavedLoaction.get(0).Address+"</address>"+
//																	"<city>"+userSavedLoaction.get(0).City+"</city>"+
//																	"<state>"+userSavedLoaction.get(0).State+"</state>"+
//																	"<zipcode>"+userSavedLoaction.get(0).Zip+"</zipcode>"+
//																	"<country>"+userSavedLoaction.get(0).Country+"</country>"+
//																"</location>" +
//															"</savedlocations>" +
//										"</useraccount>";
//						
//				Log.i("SavedLocationReq", SavedLocationReq);
//					
//				urlPost = new UrlPost();
//				responceInputStream= urlPost.registrationPost(SavedLocationReq, new URL(AppConstants.HOST_URL+AppConstants.POST_USER_LOCATION_URL));
//					
//				addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//				Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//				
//				NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//				if(xmlRoot.getLength() != 0)
//				{
//					//Node rootChild = xmlRoot.item(0);
//				}
//				
//				NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//				if(xmlRoot1.getLength() != 0)
//				{
//					Node rootChild1 = xmlRoot1.item(0);
//					status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//				}
//			}
//		   catch(Exception ee)
//		   {
//				Log.i("error is here",""+ee.toString());
//		   }
//		Log.i("status",""+status);
//		return status;
//	 }
	/*
	  * end of method uploadUserImage
	  */
		 
//	public int changeUserPrimaryLocation(int locationId)
//	{
//		try
//		{
//			String updatePrimaryLocationReq="<useraccount>" +
//													"<id>"+AppConstants.USER_ID+"</id>"+
//													 "<locationid>"+locationId+"</locationid>"+
//												"</useraccount>";	
//			Log.i("updatePrimaryLocationReq", updatePrimaryLocationReq);
//				
//			urlPost = new UrlPost();
//			responceInputStream= urlPost.registrationPost(updatePrimaryLocationReq, new URL(AppConstants.HOST_URL+AppConstants.UPDATE_PRIAMRY_LOCATION));
//				 
////			InputStreamReader reader = new InputStreamReader(responceInputStream );
////		    StringBuilder buf = new StringBuilder();
////		    char[] cbuf = new char[ 2048 ];
////		    int num;
////		    while ( -1 != (num=reader.read( cbuf )))
////		    {
////		          buf.append( cbuf, 0, num );
////		    }
////		    String result = buf.toString();
////		    //  reader.close();
////			Log.i("",""+ result);
//			
//			addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//			Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//			
//			NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//			if(xmlRoot.getLength() != 0)
//			{
//				//Node rootChild = xmlRoot.item(0);
//			}
//			
//			NodeList xmlRoot1 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//			if(xmlRoot1.getLength() != 0)
//			{
//				Node rootChild1 = xmlRoot1.item(0);
//				status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//			}
//			Log.i("changedLocationStatus",""+status);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	return status;
//	}
		 
//	public int removeSavedLocation(int locationId)
//	{
//		try
//		{
//			String removeSavedLocationReq="";	
//			
//			urlPost = new UrlPost();
//			responceInputStream= urlPost.userDeleteRequest(removeSavedLocationReq, new URL(AppConstants.HOST_URL+AppConstants.POST_USER_LOCATION_URL+"id/"+AppConstants.USER_ID+"/deleteids/"+locationId));
//			InputStreamReader reader = new InputStreamReader(responceInputStream );
//	        
//			StringBuilder buf = new StringBuilder();
//	        char[] cbuf = new char[ 2048 ];
//	        int num;
//	        while ( -1 != (num=reader.read( cbuf )))
//	        {
//	            buf.append( cbuf, 0, num );
//	        }
//	        String result = buf.toString();
//	        reader.close();
//			Log.i("\nResponse from server after reomve of saved location = " ,""+ result);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	return status;
//	}
		 
		 
//	public int updateUserProfile(Vector<com.winit.ga2oo.objects.UpdateProfile> vctUpdateprofile)
//	{
//		try
//		{
//			String strUserLocation="";
//			String strUserOldAssociations="";
//			userAccBL			=new UserAccountBusinessLayer();
//			vctUserLocation		=new Vector<UserLocationObject>();
//			vctUserLocation		=userAccBL.getUserSavedLocation(AppConstants.USER_ID);
//			vctUserAssociations	=new Vector<Association>();
//			vctUserAssociations =userAccBL.getUserAssociations(AppConstants.USER_ID);
//			vctUserAccount		=new Vector<UserAccount>();
//			vctUserAccount		=userAccBL.getUserInformation();
//			for(int i=0;i<vctUserLocation.size();i++)
//			{
//				strUserLocation=strUserLocation+"<location>"
//													+"<id>"+vctUserLocation.get(i).locationId+"</id>"
//													+"<is_primary>"+vctUserLocation.get(i).Is_Primary+"</is_primary>"
//													+"<geocode>"+vctUserLocation.get(i).GeoCode+"</geocode>"
//													+"<address>"+vctUserLocation.get(i).Address+"</address>"
//													+"<city>"+vctUserLocation.get(i).City+"</city>"
//													+"<state>"+vctUserLocation.get(i).State+"</state>"
//													+"<zip>"+vctUserLocation.get(i).Zip+"</zip>"
//													+"<country>"+vctUserLocation.get(i).Country+"</country>"
//												+"</location>";
//			}
//								
//			for(int i=0;i<vctUserAssociations.size();i++)
//			{
//				strUserOldAssociations=strUserOldAssociations+"<association>" +
//               													"<id>"+vctUserAssociations.get(i).ID+"</id>"+
//               													"<associationid>"+vctUserAssociations.get(i).Associationid+"</associationid>" +
//               													"<associationtype>"+vctUserAssociations.get(i).Associationtype+"</associationtype>" +
//               					  							  "</association>";
//			}
//			String strRequest	="<useraccount>"
//										 +"<id>"+AppConstants.USER_ID+"</id>"
//										 +"<email>"+vctUpdateprofile.get(0).strEmail+"</email>"
//										 +"<username>"+vctUpdateprofile.get(0).strUserName+"</username>"
//										 +"<firstname>"+vctUpdateprofile.get(0).strFName+"</firstname>"
//										 +"<lastname>"+vctUpdateprofile.get(0).strLName+"</lastname>"
//										 +"<currentzip>"+vctUserAccount.get(0).CurrentZip+"</currentzip>"
//										 +"<gender>"+vctUserAccount.get(0).Gender+"</gender>"
//										 +"<birthday>"+vctUserAccount.get(0).BirthDay+"</birthday>"
//										 +"<password>"+vctUserAccount.get(0).Password+"</password>"
//										 +"<is_active>"+vctUserAccount.get(0).Is_Active+"</is_active>"
//										 +"<is_public>"+vctUserAccount.get(0).Is_Public+"</is_public>"
//										 +"<is_calendar_shared>"+vctUserAccount.get(0).Is_Calendar_Shared+"</is_calendar_shared>"
//										 +"<deviceid>"+vctUserAccount.get(0).DeviceId+"</deviceid>"
//										 +"<savedlocations>"+
//										 			strUserLocation
//										 +"</savedlocations>"
//										 +"<associations>" +
//										 			strUserOldAssociations +
//										 "</associations>"
//								+"</useraccount>";
//						 
//			Log.i("strRequest",strRequest);
//			urlPost = new UrlPost();
//			responceInputStream = urlPost.updateProfilePost(strRequest,new URL(AppConstants.HOST_URL+AppConstants.UPDTAE_PROFILE));
////			InputStreamReader reader = new InputStreamReader(responceInputStream );
////			StringBuilder buf = new StringBuilder();
////			char[] cbuf = new char[ 2048 ];
////			int num;
////			while ( -1 != (num=reader.read( cbuf )))
////			{
////				buf.append( cbuf, 0, num );
////			}
////			String result = buf.toString();
////			System.err.println( "\nResponse from server after friend request = " + result );
////			reader.close();
//			 addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//	     		Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//	      			
//	      		NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//	      		if(xmlRoot.getLength() != 0)
//	      		{
//	      			//Node rootChild = xmlRoot.item(0);
//	      		}
//	      		NodeList xmlRoot2 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//	      		if(xmlRoot2.getLength() != 0)
//	      		{
//	      			Node rootChild1 = xmlRoot2.item(0);
//	      		}
//	      		NodeList xmlRoot3 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("code");
//	      		if(xmlRoot3.getLength() != 0)
//	      		{
//	      			Node rootChild1 = xmlRoot3.item(0);
//	      			status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//	      		}
//	      		NodeList xmlRoot4 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("message");
//	      		if(xmlRoot4.getLength() != 0)
//	      		{
//	      			Node rootChild1 = xmlRoot4.item(0);
//	      			updateUserProfileMgs =rootChild1.getFirstChild().getNodeValue().toString();
//	      		}
//			}
//			catch(Exception e)
//			{
//				e.printStackTrace();
//			}
//			Log.i("message",""+updateUserProfileMgs+","+status);
//		return status;
//	}
		 
//	public int updateUserAccount(Vector<com.winit.ga2oo.objects.UpdateProfile> vctUpdateprofile)
//	{
//		try
//		{
//			String strUserLocation="";
//			String strUserOldAssociations="";
//			userAccBL			=new UserAccountBusinessLayer();
//			vctUserLocation		=new Vector<UserLocationObject>();
//			vctUserLocation		=userAccBL.getUserSavedLocation(AppConstants.USER_ID);
//			vctUserAssociations	=new Vector<Association>();
//			vctUserAssociations =userAccBL.getUserAssociations(AppConstants.USER_ID);
//			vctUserAccount		=new Vector<UserAccount>();
//			vctUserAccount		=userAccBL.getUserInformation();
//			for(int i=0;i<vctUserLocation.size();i++)
//			{
//				strUserLocation=strUserLocation+"<location>"
//													+"<id>"+vctUserLocation.get(i).locationId+"</id>"
//													+"<is_primary>"+vctUserLocation.get(i).Is_Primary+"</is_primary>"
//													+"<geocode>"+vctUserLocation.get(i).GeoCode+"</geocode>"
//													+"<address>"+vctUserLocation.get(i).Address+"</address>"
//													+"<city>"+vctUserLocation.get(i).City+"</city>"
//													+"<state>"+vctUserLocation.get(i).State+"</state>"
//													+"<zip>"+vctUserLocation.get(i).Zip+"</zip>"
//													+"<country>"+vctUserLocation.get(i).Country+"</country>"
//												+"</location>";
//			}
//								
//			for(int i=0;i<vctUserAssociations.size();i++)
//			{
//				strUserOldAssociations=strUserOldAssociations+"<association>" +
//																	"<id>"+vctUserAssociations.get(i).ID+"</id>"+
//																	"<associationid>"+vctUserAssociations.get(i).Associationid+"</associationid>" +
//																	"<associationtype>"+vctUserAssociations.get(i).Associationtype+"</associationtype>" +
//															   "</association>";
//			}
//			String strRequest	="<useraccount>"
//										+"<id>"+AppConstants.USER_ID+"</id>"
//										+"<email>"+vctUserAccount.get(0).Email+"</email>"
//										+"<username>"+vctUserAccount.get(0).UserName+"</username>"
//										+"<firstname>"+vctUserAccount.get(0).FirstName+"</firstname>"
//										+"<lastname>"+vctUserAccount.get(0).LastName+"</lastname>"
//										+"<currentzip>"+vctUserAccount.get(0).CurrentZip+"</currentzip>"
//										+"<gender>"+vctUserAccount.get(0).Gender+"</gender>"
//										+"<birthday>"+vctUserAccount.get(0).BirthDay+"</birthday>"
//										+"<password>"+vctUpdateprofile.get(0).strPassword+"</password>"
//										+"<is_active>"+vctUpdateprofile.get(0).isActive+"</is_active>"
//										+"<is_public>"+vctUserAccount.get(0).Is_Public+"</is_public>"
//										+"<is_calendar_shared>"+vctUpdateprofile.get(0).strVisibleMode+"</is_calendar_shared>"
//										+"<deviceid>"+vctUserAccount.get(0).DeviceId+"</deviceid>"
//										+"<savedlocations>"+
//												strUserLocation
//										+"</savedlocations>"
//										+"<associations>" +
//												strUserOldAssociations +
//										"</associations>"
//								+"</useraccount>";
//			Log.i("strRequest",strRequest);
//			urlPost = new UrlPost();
//			responceInputStream = urlPost.updateProfilePost(strRequest,new URL(AppConstants.HOST_URL+AppConstants.UPDTAE_PROFILE));
////			InputStreamReader reader = new InputStreamReader(responceInputStream );
////			StringBuilder buf = new StringBuilder();
////			char[] cbuf = new char[ 2048 ];
////			int num;
////			while ( -1 != (num=reader.read( cbuf )))
////			{
////				buf.append( cbuf, 0, num );
////			}
////			String result = buf.toString();
////			System.err.println( "\nResponse from server after friend request = " + result );
////			reader.close();
//			addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//     		Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//      			
//      		NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//      		if(xmlRoot.getLength() != 0)
//      		{
//      			//Node rootChild = xmlRoot.item(0);
//      		}
//      		NodeList xmlRoot2 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//      		if(xmlRoot2.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot2.item(0);
//      		}
//      		NodeList xmlRoot3 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("code");
//      		if(xmlRoot3.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot3.item(0);
//      			status += Integer.parseInt(rootChild1.getFirstChild().getNodeValue().toString());
//      		}
//      		NodeList xmlRoot4 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("message");
//      		if(xmlRoot4.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot4.item(0);
//      			updateUserProfileMgs =rootChild1.getFirstChild().getNodeValue().toString();
//      		}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		Log.i("message",""+updateUserProfileMgs+","+status);
//	return status;
//	}
	
//	public String sendRecommendation(int[] vctFriendIds,int eventId, String strSubject,String strDetail)
//	{
//		try
//		{
//			String strFreind="";
//			if(vctFriendIds.length!=0)
//			for(int i=0;i<vctFriendIds.length;i++)
//			{
//				strFreind=strFreind+"<friend>"+"<friendid>"+vctFriendIds[i]+"</friendid>"+"</friend>";
//			}
//			String updatePrimaryLocationReq="<useraccount>" +
//													"<id>"+AppConstants.USER_ID+"</id>"+
//													 "<eventid>"+eventId+"</eventid>"+
//													 "<subject>"+strSubject+"</subject>"+
//													 "<message>"+strDetail+"</message>"+
//													 "<friendships>"+ strFreind+"</friendships>"+
//												"</useraccount>";	
//			Log.i("updatePrimaryLocationReq", updatePrimaryLocationReq);
//				
//			urlPost = new UrlPost();
//			responceInputStream= urlPost.registrationPost(updatePrimaryLocationReq, new URL(AppConstants.HOST_URL+AppConstants.USER_RECOMMENDATION));
//				 
////			InputStreamReader reader = new InputStreamReader(responceInputStream );
////		    StringBuilder buf = new StringBuilder();
////		    char[] cbuf = new char[ 2048 ];
////		    int num;
////		    while ( -1 != (num=reader.read( cbuf )))
////		    {
////		          buf.append( cbuf, 0, num );
////		    }
////		    String result = buf.toString();
////		    //  reader.close();
////			Log.i("",""+ result);
//			
//			addEventXmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//     		Document xmlDoc = addEventXmlBuilder.parse(responceInputStream);
//      			
//      		NodeList xmlRoot = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("useraccount");
//      		if(xmlRoot.getLength() != 0)
//      		{
//      			//Node rootChild = xmlRoot.item(0);
//      		}
//      		NodeList xmlRoot2 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("result");
//      		if(xmlRoot2.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot2.item(0);
//      		}
//      		NodeList xmlRoot3 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("code");
//      		if(xmlRoot3.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot3.item(0);
//      			recommendationStatus = rootChild1.getFirstChild().getNodeValue().toString();
//      		}
//      		NodeList xmlRoot4 = ((org.w3c.dom.Document) xmlDoc).getElementsByTagName("message");
//      		if(xmlRoot4.getLength() != 0)
//      		{
//      			Node rootChild1 = xmlRoot4.item(0);
//      			updateUserProfileMgs =rootChild1.getFirstChild().getNodeValue().toString();
//      		}
//			Log.i("recommendationStatus",""+recommendationStatus);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	return recommendationStatus;
//	}
		 

}
