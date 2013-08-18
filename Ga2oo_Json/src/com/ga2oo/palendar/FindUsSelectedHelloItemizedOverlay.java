package com.ga2oo.palendar;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

@SuppressWarnings("unchecked")
public class FindUsSelectedHelloItemizedOverlay extends ItemizedOverlay 
{
	public static ImageButton ibArrow;
	Context mContext;
	MapController mc;
	MapView absMap;
	CustomView customView;
	//Geocoder geocoder;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private GeoPoint point;
	public FindUsSelectedHelloItemizedOverlay(Drawable defaultMarker) 
	{
		super(defaultMarker);
	}

	public FindUsSelectedHelloItemizedOverlay(Drawable defaultMarker, Context context,MapView absMap,MapController mc,CustomView customView) 
	{
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		this.mc=mc;
		this.absMap=absMap;
		if(this.customView!=null)
			this.customView.setVisibility(View.GONE);
		this.customView = customView; 
	}
	
	@Override
	protected OverlayItem createItem(int i) 
	{
	  return mOverlays.get(i);
	}
	
	public void addOverlay(OverlayItem overlay) 
	{
	    mOverlays.add(overlay);
	    populate();
	}
	
	

	@Override
	public int size() 
	{
		return mOverlays.size();
	}
	
	
//	protected boolean onTap(final int index) 
//	{
//
//		 final OverlayItem item = mOverlays.get(index);
//		  GeoPoint point=item.getPoint();
//		  mc.animateTo(point);
//		  customView.bringToFront();
//		  customView.setVisibility(View.VISIBLE);
//		 // TextView view = new TextView(mContext);
//		 
//		//  customView.imgView.setImageDrawable(bitmap);
//		 
//		  /*try {
//			List<Address> address = geocoder.getFromLocation(point.getLatitudeE6()/1E6,point.getLongitudeE6()/1E6,5);
//			Address addObj = address.get(0);
//			int addLine = addObj.getMaxAddressLineIndex();
//			String addressResult ="";
//			for(int i=0;i<addLine;i++)
//			{
//				addressResult += addObj.getAddressLine(i)+",";
//			}*/  
//	
//		//customView.setLayoutParams(new MapView.LayoutParams(200,60,point,-30,-30,MapView.LayoutParams.MODE_MAP));
//			customView.setOnClickListener(new OnClickListener()
//			{
//				
//				@Override
//				public void onClick(View v) 
//				{
//					v.setVisibility(View.INVISIBLE);
//				}
//			});
//		  return true;
//	}

}
