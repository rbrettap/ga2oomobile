package com.ga2oo.palendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Registration extends Activity
{
	private RelativeLayout rlBusiness,rlPersonal;
	private Button btnBack;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.registration);
		rlPersonal = (RelativeLayout)findViewById(R.id.rlPersonal);
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				finish();
			}
		});
		
		rlPersonal.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(Registration.this,RegistrationforPersonal.class);
				startActivity(intent);
			}
		});
	}
}
