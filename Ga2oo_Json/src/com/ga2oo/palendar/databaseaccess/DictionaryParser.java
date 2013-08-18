package com.ga2oo.palendar.databaseaccess;



public abstract class DictionaryParser extends IDictionaryParserHandler
{
	private static final String NULL = "null";

	public DictionaryParser(DictionaryEntry[][] datasource) 
	{
		try
		{
			if(datasource != null && datasource.length > 0)
			{
				for(int i=0; i<datasource.length; i++)
				{
					if(datasource[i] != null && datasource[i].length > 0)
					{
						for(int j=0; j<datasource[i].length; j++)
						{
							if(datasource[i][j].key == null) 
								datasource[i][j].key = NULL;
							if(datasource[i][j].value == null) 
								datasource[i][j].value = NULL;
							characters(datasource[i][j].key, datasource[i][j].value);
							if(j==datasource[i].length-1)
								endElement();
						}
					}else{
						exception(new NullPointerException());
					}
				}
			}else{
				exception(new NullPointerException());
			}
		}
		catch(Exception e)
		{
			exception(e);
		}
	}
}
