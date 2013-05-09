package com.winit.ga2oo.businesslayer;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;

import com.winit.ga2oo.databaseaccess.DatabaseHelper;
import com.winit.ga2oo.databaseaccess.DictionaryEntry;
import com.winit.ga2oo.objects.BusinessType;

public class BusinessBusinessLayer {
	
	private static final String WHERE = " WHERE ";
	private static final String FROM = " FROM ";
	private static final String ALLFIELDS = " * ";
	private static final String SELECT = "SELECT ";
	private static final String DATE_UPDATED = "DATE_UPDATED";
	private static final String BUSINESSTYPEID = "BUSINESSTYPEID";
	private static final String BUSINESSTYPE="BUSINESSTYPE";

	public long insertBusinesstype(BusinessType type){
		String whereClause = BUSINESSTYPEID+"="+type.businesstypeid;
		DictionaryEntry [][] data = DatabaseHelper.get(SELECT+BUSINESSTYPEID+", "+DATE_UPDATED+FROM+BUSINESSTYPE+WHERE+whereClause); 
		ContentValues values = new ContentValues();
		values.put(BUSINESSTYPEID, type.businesstypeid);	 		
 		values.put(BUSINESSTYPE, type.businesstypename);
 		values.put(DATE_UPDATED, type.date_updated);
		if(data != null && data[0][0].value != null)
		{
			if(!type.date_updated.equals(data[0][1].value.toString())){
				return DatabaseHelper.doUpdate(BUSINESSTYPE, values, whereClause, null);
			}else{
				return 0;
			}
		}
		else
		{
	 		return DatabaseHelper.doInsert(BUSINESSTYPE, values);		
		}
		
	}
	
	public static  List<BusinessType> getBusinessTypes(){
		List<BusinessType> result = new ArrayList<BusinessType>();
		DictionaryEntry[][] data=null;
		data = DatabaseHelper.get(SELECT+ALLFIELDS+FROM+BUSINESSTYPE);
		if(data != null){
			for( int i = 0; i<data.length; i++)
			{		
				try
				{
					BusinessType objBType = new BusinessType();
					objBType.businesstypeid = (data[i][0].value!=null?Integer.parseInt(data[i][0].value.toString()):0);
					objBType.businesstypename =(data[i][1].value!=null?data[i][1].value.toString():"");
					result.add(objBType);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		return result;
	}

}
