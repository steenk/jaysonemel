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