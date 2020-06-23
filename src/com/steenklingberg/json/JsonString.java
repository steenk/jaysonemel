package com.steenklingberg.json;

public class JsonString implements JsonValue {

	String string;

	public JsonString (String string) {
		this.string = string;
	}

	public void setValue (String string) {
		this.string = string;
	}

	public String getValue () {
		return string;
	}

	@Override
	public String getType () {
		return "string";
	}

	@Override
	public String getStringValue () {
		return string.trim();
	}

	public String toString () {
		return "\"" + string.trim() + "\"";
	}
}