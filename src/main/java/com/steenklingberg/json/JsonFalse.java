package com.steenklingberg.json;

public class JsonFalse implements JsonValue {

	public JsonFalse () {

	}

	public boolean getValue () {
		return false;
	}

	public boolean getBooleanValue () {
		return false;
	}

	@Override
	public String getType () {
		return "false";
	}

	@Override
	public String getStringValue () {
		return toString();
	}

	public String toString () {
		return "false";
	}

}