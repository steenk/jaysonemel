package com.steenklingberg.json;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class JsonObject extends HashMap<String, JsonValue> implements JsonValue, JsonStructure {

	public JsonObject () {
		super();
	}
	
	@Override
	public JsonValue getValue (String key) {
		if ("".equals(key)) {
			return this;
		} else {
			return (JsonValue) this.get(key);
		}
	}

	public void setValue (String key, JsonValue value) {
		this.put(key, value);
	}

	public void setValue (JsonString key, JsonValue value) {
		this.put(key.toString(), value);
	}

	public void setValue (String key, String value) {
		if (value == null) {
			this.put(key, new JsonNull());
		} else {
			this.put(key, new JsonString(value));
		}
	}

	public void setValue (String key, int value) {
		this.put(key, new JsonNumber(value));
	}

	public void setValue (String key, float value) {
		this.put(key, new JsonNumber(value));
	}

	public void setValue (String key, boolean value) {
		if (value) {
			this.put(key, new JsonTrue());
		} else {
			this.put(key, new JsonFalse());
		}
	}

	@Override
	public String getType () {
		return "object";
	}

	@Override
	public String getStringValue () {
		return toString();
	}

	public String toString () {
		StringBuilder sb = new StringBuilder();
		Set<String> keys = this.keySet();
		Iterator<String> iter = keys.iterator();
		sb.append("{");
		String key;
		while (iter.hasNext()) {
			key = iter.next();
			sb.append("\"")
			.append(key)
			.append("\":")
			.append(this.get(key).toString());
			if (iter.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("}");
		return sb.toString();
	}

}