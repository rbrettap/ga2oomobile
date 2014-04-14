package com.ga2oo.palendar;

import java.util.ArrayList;
import java.util.List;

import com.ga2oo.palendar.businesslayer.UserAccountBusinessLayer;
import com.ga2oo.palendar.objects.UserAccount;
//import com.ga2oo.palendar.xmlparsers.Ga2ooParsers;
import com.ga2oo.jsonparsers.Ga2ooJsonParsers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;


public class Account extends Activity 
{
	
	private static final String TRUE = "True";
	private static final String FALSE = "False";
	private static final String ACCOUNT = "Account";
	
	private EditText etAccPassword;
	private ImageView btnYes,btnNo,btnYesDelete,btnNoDelete;
	public  List<UserAccount> vctUserAccount;
	private List<com.ga2oo.palendar.objects.UpdateProfile> vctUpdateData=null;
	
	private UserAccountBusinessLayer userAccBL;
//	private Ga2ooParsers register;
	private AlertDialog alertDialog;
	private String isInVisible,isDeleteAcount=FALSE;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.account_view);
		
		alertDialog 	= new AlertDialog.Builder(getParent()).create();
		alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				alertDialog.dismiss();
			}
		});
		btnYes 			= (ImageView)findViewById(R.id.btnYes);
		btnNo 			= (ImageView)findViewById(R.id.btnNo);
		btnYesDelete	= (ImageView)findViewById(R.id.btnYesDelete);
		btnNoDelete 	= (ImageView)findViewById(R.id.btnNoDelete);
		etAccPassword 	= (EditText)findViewById(R.id.etAccPassword);
		
		userAccBL		=new UserAccountBusinessLayer();
		vctUserAccount	=new ArrayList<UserAccount>();
		vctUserAccount	= userAccBL.getUserInformation();
		
		if(vctUserAccount.size()!=0)
		{
			etAccPassword.setText(vctUserAccount.get(0).password);
			if(vctUserAccount.get(0).is_calendar_shared.equals(TRUE))
			{
				isInVisible=FALSE;
				btnYes.setImageResource(R.drawable.yesclickbtn);
				btnNo.setImageResource(R.drawable.noclickbtn);
			}
			else
			{
				isInVisible=TRUE;
				btnYes.setImageResource(R.drawable.yes);
				btnNo.setImageResource(R.drawable.no);
			}
			if(vctUserAccount.get(0).is_active.equals(TRUE))
			{
				isDeleteAcount=TRUE;
				btnNoDelete.setImageResource(R.drawable.noclickbtn);
				btnYesDelete.setImageResource(R.drawable.yesclickbtn);
			}
			else
			{
				isDeleteAcount=FALSE;
				btnNoDelete.setImageResource(R.drawable.no);
				btnYesDelete.setImageResource(R.drawable.yes);
			}
		}
		btnYes.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				isInVisible=FALSE;
				btnYes.setImageResource(R.drawable.yes);
				btnNo.setImageResource(R.drawable.no);
			}
		});


		btnNo.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				isInVisible=TRUE;
				btnYes.setImageResource(R.drawable.yesclickbtn);
				btnNo.setImageResource(R.drawable.noclickbtn);
			}
		});
		btnNoDelete.setImageResource(R.drawable.noclickbtn);
		btnYesDelete.setImageResource(R.drawable.yesclickbtn);
		btnYesDelete.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				isDeleteAcount=FALSE;
				btnYesDelete.setImageResource(R.drawable.yes);
				btnNoDelete.setImageResource(R.drawable.no);
			}
		});

		btnNoDelete.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				isDeleteAcount=TRUE;
				btnYesDelete.setImageResource(R.drawable.yesclickbtn);
				btnNoDelete.setImageResource(R.drawable.noclickbtn);
			}
		});
		
    }
	@Override
    public void onResume()
    {
    	super.onResume();
        TabsActivity.btnBack.setVisibility(View.VISIBLE);
        TabsActivity.rlCommonTitleBar.setBackgroundResource(R.drawable.top_bar_empty);
        TabsActivity.tvTitle.setText(getResources().getString(R.string.account));
        TabsActivity.tvTitle.setVisibility(View.VISIBLE);
        TabsActivity.tvTitle.setGravity(Gravity.CENTER);
        TabsActivity.btnLogout.setBackgroundResource(R.drawable.done);
        TabsActivity.rlCommonTitleBar.invalidate();

        TabsActivity.btnBack.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etAccPassword.getWindowToken(), 0);
				TabGroupActivity pActivity = (TabGroupActivity)Account.this.getParent();
				pActivity.finishFromChild(null);
			}
		});
        
        TabsActivity.btnLogout.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View v) 
			{
				vctUpdateData=new ArrayList<com.ga2oo.palendar.objects.UpdateProfile>();
				com.ga2oo.palendar.objects.UpdateProfile objupdateProfile=new com.ga2oo.palendar.objects.UpdateProfile();
				objupdateProfile.strPassword=etAccPassword.getText().toString();
				objupdateProfile.strVisibleMode=isInVisible;
				objupdateProfile.isActive=isDeleteAcount;
				vctUpdateData.add(objupdateProfile);
				int updateStatusStatus = Ga2ooJsonParsers.getInstance().updateUserProfile(vctUpdateData);
				if(updateStatusStatus>0)
				{
					 alertDialog.setTitle(getResources().getString(R.string.success));
					 alertDialog.setMessage(Ga2ooJsonParsers.updateUserProfileMgs);
					 alertDialog.show();
					  userAccBL.updateUser(vctUpdateData,ACCOUNT);
				}
				else
				{
					 alertDialog.setTitle(getResources().getString(R.string.failure));
					 alertDialog.setMessage(getResources().getString(R.string.try_again));
					 alertDialog.show();
				}
				alertDialog.setButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which) 
					{
						TabGroupActivity pActivity = (TabGroupActivity)Account.this.getParent();
						pActivity.finishFromChild(null);
						alertDialog.dismiss();
					}
				});
			}
		});
    }
}
