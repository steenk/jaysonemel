package com.steenklingberg.json;

public class JsonNull implements JsonValue {

	public JsonNull () {

	}

	@Override
	public String getType () {
		return "null";
	}

	@Override
	public String getStringValue () {
		return toString();
	}

	public Object getValue() {
		return null;
	}

	public String toString () {
		return "null";
	}

}