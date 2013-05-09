package com.winit.ga2oo.objects;

import java.util.List;

public class UserAccount 
{
	public int id;
	public String username="";
	public String email="";
	public String firstname="";
	public String lastname="";
	public String zipcode="none";
	public String gender="";
	public String birthday="";
	public String password="";
	public String is_active="";
	public String is_public="";
	public String is_calendar_shared="";
	public String deviceid="";
	public String date_updated="";
	public String imagesrc="";

	public List<Business> fav_businesses;
	public List<Notifications> notifications;
	
	public List<FavoriteEvent> events;
	
	public int eventcount;
	
	public Ga2ooResult result;
	
	public String imageId;
	public String image_updated;
	public String isImageUpdated;
}
