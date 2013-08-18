package com.ga2oo.palendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

public class BuyTicket extends Activity 
{
	private static final String EVNETTICKET = "event_ticket";
	
	private WebView buyticket;
	private String strEventTicketUrl;
	public void onCreate(Bundle savedInstanceState)
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.buyticket);
	    strEventTicketUrl=getIntent().getExtras().getString(EVNETTICKET);
	    buyticket = (WebView) findViewById(R.id.buyticket);
	    buyticket.getSettings().setJavaScriptEnabled(true);
	    buyticket.loadUrl(strEventTicketUrl);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && buyticket.canGoBack())
	    {
	    	buyticket.goBack();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
