package com.ga2oo.palendar;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Contact extends Activity 
{
	private TextView tvContactNumber;
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.contact_view);	
		tvContactNumber = (TextView)findViewById(R.id.tvContactNumber); 
		tvContactNumber.setAutoLinkMask(Linkify.ALL);
		tvContactNumber.setMovementMethod(LinkMovementMethod.getInstance());
    }
	@Override
    public void onResume()
    {
    	super.onResume();
    	
        TabsActivity.btnBack.setVisibility(View.VISIBLE);
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar_empty);
        TabsActivity.tvTitle.setText(getResources().getString(R.string.contact));
        TabsActivity.tvTitle.setVisibility(View.VISIBLE);
        TabsActivity.btnLogout.setVisibility(View.GONE);
        TabsActivity.rlCommonTitleBar.invalidate();

        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				TabGroupActivity pActivity = (TabGroupActivity)Contact.this.getParent();
				pActivity.finishFromChild(null);
			}
		});
    }
}
