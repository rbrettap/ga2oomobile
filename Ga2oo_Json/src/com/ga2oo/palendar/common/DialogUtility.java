package com.ga2oo.palendar.common;

import com.ga2oo.palendar.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtility {
	
	private static ProgressDialog dialog;

	public static void showConnectionErrorDialog(Context context){
		 AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
		 dlgAlert.setMessage(context.getResources().getString(R.string.error_in_server_data));
	     dlgAlert.setTitle(context.getResources().getString(R.string.ga2oo_info));
	     dlgAlert.setPositiveButton(context.getResources().getString(R.string.ok), null);
	     dlgAlert.setCancelable(true);
	     dlgAlert.create().show();
	}
	
	public static void showLoadingDialog(Context context){
		dialog = new ProgressDialog(context);
		dialog.setMessage(context.getResources().getString(R.string.loading));
		dialog.show();
	}
	
	public static void showLogoutDialog(Context context){
		dialog = new ProgressDialog(context);
		dialog.setMessage(context.getResources().getString(R.string.logout));
	}
	
	public static void diamissDialog(){
		dialog.dismiss();
	}
}
