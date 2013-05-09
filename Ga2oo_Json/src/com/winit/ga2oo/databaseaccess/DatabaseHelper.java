package com.winit.ga2oo.databaseaccess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String FROM = " FROM ";

	private static final String DELETE = "DELETE";

	public static final String LOGTAG = "DatabaseHelper";
	
	public static String DB_PATH = "/data/data/com.winit.ga2oo/";
	public static String FOLDER_NAME = "/ga2oo";
	 
    public static String DB_NAME = "Ga2oo.sqlite";
 
    public static SQLiteDatabase _database; 
 
    private final Context myContext;
    public static String apstorphe = "'";
	public static String sep = ",";	
	
	public DatabaseHelper(Context context) 
    {	 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH=myContext.getFilesDir().toString()+"/";
    }

	
	/**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException
    {
       	boolean dbExist = checkDataBase();
 
    	if(!dbExist)
    	{
    		//By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
    		try
    		{
    			
    			copyDataBase();
    		} 
    		catch (IOException e) 
    		{	 
        		throw new Error("Error copying database");
         	}
    	} 
    }
    
    public static String getCurrentDBPath(){
    	if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
    		return Environment.getExternalStorageDirectory().toString()+FOLDER_NAME+"/"+DB_NAME;
    	}else{
    		return DB_PATH + DB_NAME;
    	}
    }
    
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase()
    {	 
    	SQLiteDatabase checkDB = null;
 
    	try
    	{
    		String myPath = getCurrentDBPath();
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE); 
    	}
    	catch(SQLiteException e)
    	{
    		//database does't exist yet.	 
    	}
 
    	if(checkDB != null)
    	{	 
    		checkDB.close();	 
    	}
 
    	return checkDB != null ? true : false;
    }
    
    public void copyDataBase() throws IOException
    {
     
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
    	OutputStream myOutput;
    	String outFileName = DB_PATH + DB_NAME;
    	// Path to the just created empty db
    	
    	if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
    		File path = new File(Environment.getExternalStorageDirectory()+FOLDER_NAME);
    		path.mkdir();
    		File file = new File(path,DB_NAME);
        	myOutput = new FileOutputStream(file);

    	}else{    	
	    	//Open the empty db as the output stream
	    	myOutput = new FileOutputStream(outFileName);
    	}
	    	//transfer bytes from the inputfile to the outputfile
	    	byte[] buffer = new byte[2048];
	    	int length;
	    	while ((length = myInput.read(buffer))>0)
	    	{
	    		myOutput.write(buffer, 0, length);
	    	}
	 
	    	//Close the streams
	    	myOutput.flush();
	    	myOutput.close();
	    	myInput.close();
    	
 
    }
    
    private static SQLiteDatabase openDataBase() throws SQLException
    {	 
    	//Open the database
    	if(_database == null)    
    	{
        _database = SQLiteDatabase.openDatabase(getCurrentDBPath(), null, SQLiteDatabase.OPEN_READWRITE
                | SQLiteDatabase.CREATE_IF_NECESSARY);
    	}
    	else if(!_database.isOpen())
    	{
    		_database = SQLiteDatabase.openDatabase(getCurrentDBPath(), null, SQLiteDatabase.OPEN_READWRITE
                    | SQLiteDatabase.CREATE_IF_NECESSARY);
    	}
    	return _database;
    }
    
    
   private void closedatabase() 
    { 
	    if(_database != null)
		    _database.close(); 
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{		
		
	}

	public static DictionaryEntry[][] get(String query_str)
	{
		
		DictionaryEntry dir = null;
		String[] columns;
		int index;
		int rowIndex = 0;
		DictionaryEntry[] row_obj = null; //An array of columns and their values
		DictionaryEntry[][] data_arr = null;
		Cursor c;	
		
		if(_database != null)
		{
			try 
			{
				openDataBase();
				c = _database.rawQuery(query_str, null);
				if(c.moveToFirst())
				{
					rowIndex = 0;
					data_arr = new DictionaryEntry[c.getCount()][];
					do
					{
						columns = c.getColumnNames();
						row_obj = new DictionaryEntry[columns.length]; //(columns.length);
						for(int i=0; i<columns.length; i++)
						{
							dir = new DictionaryEntry(); 							
							dir.key = columns[i];
							index = c.getColumnIndex(dir.key);
							dir.value = c.getString(index);
							row_obj[i] = dir;
						}
						data_arr[rowIndex] = row_obj;
						rowIndex++;
					}
					while(c.moveToNext());
				}
				c.close();
			}
			catch(Exception e) 
			{
				e.printStackTrace();
			}
		}
		return data_arr;
	}
	
	public static boolean deleteDir(File dir) 
	{ 
		if (dir.isDirectory()) 
		{ 
			String[] children = dir.list(); 
			for (int i=0; i<children.length; i++) 
			{ 
				boolean success = deleteDir(new File(dir, children[i])); 
				if (!success) 
				{ 
					return false; 
				} 
			} 
		} 
		// The directory is now empty so delete it return dir.delete(); } 
		return dir.delete();
	}
	
	public static boolean deleteAllDataFromTable(String tablename){
		try{
			openDataBase();
	       _database.execSQL(DELETE+FROM+tablename);
		}catch(Exception e){
			return false;
		}
//		finally{
//			closedatabase();
//		}
	 	return true;		
	}

	public static Cursor doSelect(String request){
		Log.v(LOGTAG, "select request = "+request);
		openDataBase();
		Cursor result = _database.rawQuery(request, null);
//		closedatabase();
		return result;
	}
	
	public static long doInsert(String tableName, ContentValues values){
		openDataBase();
		long result =_database.insert(tableName, null, values);
//		closedatabase();
		return result;	
	}
	
	public static long doUpdate(String table, ContentValues values, String whereClause, String[] whereArgs){
		openDataBase();
		return _database.update(table, values, whereClause, whereArgs);
	}
	
	public static boolean doUpdate(String query){
		try{
		openDataBase();
		 _database.execSQL(query);
		}catch(Exception e){
			return false;
		}
		return true;
	}
}
