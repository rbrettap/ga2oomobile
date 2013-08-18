
package com.ga2oo.palendar.databaseaccess;

public abstract class IDictionaryParserHandler 
{
	public abstract void characters(String key, Object value) throws NullPointerException;
	public abstract void endElement();
	public abstract void exception(Exception e);
}
