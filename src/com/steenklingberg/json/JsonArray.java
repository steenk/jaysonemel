package com.steenklingberg.json;

import java.util.ArrayList;
import java.util.ListIterator;

public class JsonArray extends ArrayList<JsonValue> implements JsonValue, JsonStructure {

	public JsonArray () {
		super();
	}

	public JsonValue getValue (int index) {
		return this.get(index);
	}

	@Override
	public JsonValue getValue (String key) {
		if ("".equals(key)) {
			return this;
		} else {
			int i = Integer.parseInt(key);
			return this.get(i);
		}
	}

	public void setValue (JsonValue value) {
		this.add(value);
	}

	@Override
	public String getType () {
		return "array";
	}

	@Override
	public String getStringValue () {
		return toString();
	}

	public String toString () {
		StringBuilder sb = new StringBuilder();
		ListIterator<JsonValue> iter = this.listIterator();
		sb.append("[");
		JsonValue val;
		while (iter.hasNext()) {
			val = iter.next();
			sb.append(val.toString());
			if (iter.hasNext()) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

}