package com.winit.ga2oo;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.AbsoluteLayout;

public class CustomView extends AbsoluteLayout
{
	private int left,top,right,bottom;
	public String titleName;
	public Bitmap bitmap;
	public int y;
	//public Button btnImap;
	public CustomView(Context context)
	{
		super(context);
		left = 0;
		top = 0;
		right = 60;
		bottom = 20;
		titleName = getResources().getString(R.string.new_point);
		y=22;
		//bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.imap);
	}
	
}
