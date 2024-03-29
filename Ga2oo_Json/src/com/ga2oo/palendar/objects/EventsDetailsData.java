package com.ga2oo.palendar.objects;

import java.util.List;

import com.ga2oo.palendar.Image;

public class EventsDetailsData {
	public int useraddedeventid;
	public String BusinessName;
	public int EventBusiness;
	public String EventAddress="";
	public String EventCity="";
	public String EventState="";
	public String EventCountry="";
	public String EventGeoCode="";
	public String EventLocationZip="";
	public String image;
	
	public int id;
	public int eventid;
	public String eventname;
	public String description;
	public String tickets;
	public String supportemail;
	public String supportphone;
	public String event_start_date;
	public String event_end_date;
	public String event_start_time;
	public String event_end_time;
	public String event_expiration;
	public String is_featured;
	public String price;
	public String likecount;
	public String date_updated;
	public String date_created;
	public String status;
	public int ga2oometer;

	public Business business;
	public List<Image> images;
	public List<Categories> categories;
	public List<Attending> attending;
	public List<EventLocation> locations;

}
