package com.steenklingberg.json;

public class JsonTrue implements JsonValue {

	public JsonTrue () {

	}

	public boolean getValue () {
		return true;
	}

	@Override
	public String getType () {
		return "true";
	}

	@Override
	public String getStringValue () {
		return toString();
	}

	public String toString () {
		return "true";
	}

}