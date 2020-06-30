package com.steenklingberg.json;

public interface JsonStructure {

	public abstract String getType ();

	public abstract JsonValue getValue (String key);
	
}
