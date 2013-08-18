package com.ga2oo.palendar.controls;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;

public class PopUpDailog extends Dialog 
{
	public PopUpDailog(Context context,View view, int lpW, int lpH) 
	{
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view, new LayoutParams(lpW, lpH));
	}
}
