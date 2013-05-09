package com.winit.ga2oo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FAQ extends Activity 
{
	private TextView tvFaq;
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_view);
		
		tvFaq = (TextView)findViewById(R.id.tvAbout);
		tvFaq.setText(getResources().getString(R.string.FAQNotAvailableNote));
    }
	@Override
    public void onResume()
    {
    	super.onResume();
        TabsActivity.btnBack.setVisibility(View.VISIBLE);
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar_empty);
        TabsActivity.tvTitle.setText(getResources().getString(R.string.faq));
        TabsActivity.tvTitle.setVisibility(View.VISIBLE);
        TabsActivity.btnLogout.setVisibility(View.GONE);
        TabsActivity.rlCommonTitleBar.invalidate();

        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				TabGroupActivity pActivity = (TabGroupActivity)FAQ.this.getParent();
				pActivity.finishFromChild(null);
			}
		});
    }
}
