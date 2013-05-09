package com.winit.ga2oo;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;

public class TabGroupActivity extends ActivityGroup 
{
    public ArrayList<String> mIdList;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState); 
        if (mIdList == null) {
        	mIdList = new ArrayList<String>();
        }
    }

    /**
     * This is called when a child activity of this one calls its finish method. 
     * This implementation calls {@link LocalActivityManager#destroyActivity} on the child activity
     * and starts the previous activity.
     * If the last child activity just called finish(),this activity (the parent),
     * calls finish to finish the entire group.
     */
  @Override
  public void finishFromChild(Activity child) 
  {
	  for(int i=0; i< mIdList.size(); i++)
		  Log.i("finishFromChild", mIdList.get(i));
	  
      LocalActivityManager manager = getLocalActivityManager();
      
      int index = mIdList.size()-1;
      if (index < 1) 
      {
          finish();
          return;
      }
    
      manager.destroyActivity(mIdList.get(index), true);
     
      mIdList.remove(index); index--;
      String lastId = mIdList.get(index);
      
      Log.e("LastId", ""+lastId);
      
      Intent lastIntent = manager.getActivity(lastId).getIntent();
      Window newWindow = manager.startActivity(lastId, lastIntent);
      View view  = newWindow.getDecorView();
      //view.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
      //setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
      setContentView(view);
  }
     
  @Override 
  public void finishActivityFromChild(Activity child, int requestCode) 
  {
	  this.overridePendingTransition(com.winit.ga2oo.R.anim.slide_right1,com.winit.ga2oo.R.anim.slide_left1);
	  LocalActivityManager manager = getLocalActivityManager();
      
	  int index = mIdList.size()-1;
      
      if (index < 1)
      {
          finish();
          return;
      }
      manager.destroyActivity(mIdList.get(index), true);
      
      mIdList.remove(index); index--;
      String lastId = mIdList.get(index);
     
      Intent lastIntent = child.getIntent();
      lastIntent.setAction(Intent.ACTION_PICK);
      Window newWindow = manager.startActivity(lastId, lastIntent);
      View view = newWindow.getDecorView();
      view.setAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
      setContentView(view);
  }

  
  /**
   * Starts an Activity as a child Activity to this.
   * @param Id Unique identifier of the activity to be started.
   * @param intent The Intent describing the activity to be started.
   * @throws android.content.ActivityNotFoundException.
   */
  public void startChildActivity(String Id, Intent intent) 
  {    
	 Id = Id+""+(mIdList.size());
	 mIdList.add(Id);
	 Window window = getLocalActivityManager().startActivity(Id,intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    
      if (window != null)
      {
          for(int i=0; i< mIdList.size(); i++)
    		  Log.i("startChildActivity", mIdList.get(i));
          
          View view = window.getDecorView();
         // setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT)); 
          setContentView(view);
      }   
  }
  
  public void startChildActivityForResult(String Id, Intent intent) 
  {     
      Window window = getLocalActivityManager().startActivity(Id,intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT));
      if (window != null) 
      {
          mIdList.add(Id);
          setContentView(window.getDecorView()); 
      }    
  }
 
  
  /**
   * The primary purpose is to prevent systems before android.os.Build.VERSION_CODES.ECLAIR
   * from calling their default KeyEvent.KEYCODE_BACK during onKeyDown.
   */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) 
  {
      if (keyCode == KeyEvent.KEYCODE_BACK) 
      {
          //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
          return true;
      }
      return super.onKeyDown(keyCode, event);
  }

  /**
   * Overrides the default implementation for KeyEvent.KEYCODE_BACK 
   * so that all systems call onBackPressed().
   */
  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event)
  {
      if (keyCode == KeyEvent.KEYCODE_BACK) 
      {
          onBackPressed();
          return true; 
      }
      return super.onKeyUp(keyCode, event);
  }
  
  /**
   * If a Child Activity handles KeyEvent.KEYCODE_BACK.
   * Simply override and add this method.
   */
  @Override
  public void  onBackPressed() 
  {
	  TabsActivity.tabWidget.setVisibility(View.VISIBLE);
      int length = mIdList.size();
	  Activity current = getLocalActivityManager().getActivity(mIdList.get(length-1));
      current.finish();
  }
}

