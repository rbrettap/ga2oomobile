package com.winit.ga2oo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class About extends Activity 
{
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_view);
    }
	@Override
    public void onResume()
    {
    	super.onResume();    	
        TabsActivity.btnBack.setVisibility(View.VISIBLE);
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar_empty);
        TabsActivity.tvTitle.setText(getResources().getString(R.string.about));
        TabsActivity.tvTitle.setVisibility(View.VISIBLE);
        TabsActivity.btnLogout.setVisibility(View.GONE);
        TabsActivity.rlCommonTitleBar.invalidate();

        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				TabGroupActivity pActivity = (TabGroupActivity)About.this.getParent();
				pActivity.finishFromChild(null);
			}
		});
    }
}
