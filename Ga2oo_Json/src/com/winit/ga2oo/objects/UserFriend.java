package com.winit.ga2oo.objects;

import java.util.List;


public class UserFriend{

	public int noOfFriends;
	public int userFriendAddedId;
	public int ID1;
	public String UserName1;
	public String Email1;
	public String FirstName1;
	public String LastName1;
	public String Image1;
	public String Address;
	public String City;
	public String State;
	public String Country;
	
	public int id;
	public String username="";
	public String email="";
	public String firstname="";
	public String lastname="";
	public String zipcode="";
	public String gender="";
	public String birthday="";
	public String password="";
	public String is_active="";
	public String is_public="";
	public String is_calendar_shared="";
	public String deviceid="";
	public String date_updated="";
	public String imagesrc="";
	
	public List <Friend> friendships;
	
	public List<Association> associations;
	public List<UserLocation> savedlocations;
	
	public String image_id;
	public String image_updated;
	public String isImageUpdated;
}