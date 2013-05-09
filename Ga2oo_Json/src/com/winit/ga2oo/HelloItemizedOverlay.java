package com.winit.ga2oo;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay
{
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	public static ImageButton ibArrow;
	MapController mc;
	MapView absMap;
	HelloItemizedOverlay itemizedoverlay;
	CustomView customView;

	public HelloItemizedOverlay(Drawable defaultMarker) {
		  super(boundCenterBottom(defaultMarker));
		}

	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	@Override
	protected OverlayItem createItem(final int i) {
	  return mOverlays.get(i);
	}

	@Override
	public int size() {
	  return mOverlays.size();
	}
	
	public HelloItemizedOverlay(Drawable defaultMarker, Context context,MapView absMap,MapController mc) 
	{
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		this.mc=mc;
		this.absMap=absMap;
	
		
	}

	
	@Override
	protected boolean onTap(int index) {
		
		customView=new CustomView(mContext);
		absMap.removeAllViews();
		absMap.addView(customView);
		customView.setBackgroundResource(R.drawable.maplocation1);
		customView.setVisibility(View.INVISIBLE);

		customView.removeAllViews();
		

		
		absMap.invalidate();

	  OverlayItem item = mOverlays.get(index);
	  final GeoPoint point1=item.getPoint();
	  customView.bringToFront();
	  customView.setVisibility(View.VISIBLE);
	  
	  customView.bringToFront();
	  customView.setVisibility(View.VISIBLE);
	  
	  
	  TextView tv2=new TextView(mContext);
	  tv2.setText(item.getTitle());
//	  tv2.setWidth(100);
	  tv2.setSingleLine(true);
	  tv2.setSingleLine();
		
	  tv2.setTextColor(Color.WHITE);
	  tv2.setTextSize(12);
	  tv2.setTypeface(Typeface.DEFAULT_BOLD);
	  
	  TextView tv1=new TextView(mContext);
	  tv1.setSingleLine();
//	  tv1.setWidth(100);
	  tv1.setSingleLine(true);
	  tv1.setText(item.getSnippet());
		
	  tv1.setTextColor(Color.WHITE);
	  tv1.setTextSize(10);
	  tv1.setTypeface(Typeface.DEFAULT_BOLD);
	  
	  customView.setTag(item.getTitle());
	  
	  customView.addView(tv2, new AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,10,10));
	  customView.addView(tv1, new AbsoluteLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT,10,25));
	 
	  customView.setLayoutParams(new MapView.LayoutParams( 175, 78, point1,-76,-100, MapView.LayoutParams.MODE_MAP|MapView.LayoutParams.TOP_LEFT));
	
//	  customView.setOnClickListener(new OnClickListener() 
//	  {
//		  public void onClick(View v) 
//		  {
//			customView.setVisibility(View.INVISIBLE);
//			String storeName=(String)v.getTag();
//			 for(int count=0; count<AppsConstants.vectSearchedStores.size(); count++)
//		        { 
//			       if(AppsConstants.vectSearchedStores.get(count).storeName.equalsIgnoreCase(storeName))
//			       {
//			    	   
//			    	   Intent intent=new Intent(mContext, PurchaseActivity.class);
//						intent.putExtra("StoreId", count);
//						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//						int value=1999;
//						intent.putExtra("ClassName", value);
//						
//						((Activity)mContext).startActivity(intent);
//			    	   
////			    	   ((Activity)mContext).setResult(count);
//					   ((Activity)mContext).finish();
//			       }
//		        }
//		  }
//	  });
	  return true;
	}

	
}
