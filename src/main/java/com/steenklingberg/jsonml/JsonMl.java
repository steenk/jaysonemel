package com.steenklingberg.jsonml;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.steenklingberg.json.JsonArray;
import com.steenklingberg.json.JsonObject;
import com.steenklingberg.json.JsonParser;
import com.steenklingberg.json.JsonString;
import com.steenklingberg.json.JsonStructure;
import com.steenklingberg.json.JsonValue;

public class JsonMl {
	JsonArray root = null;

	public JsonMl (String jsonml) {
		this.root = parse(jsonml);
	}

	private JsonArray parse (String json) {
		JsonParser parser = new JsonParser();
		JsonValue jv = parser.parse(json);
		if (validate(jv)) {
			return (JsonArray)jv; 
		} else {
			return null;
		}
	}

	private boolean validate (JsonValue jsonml) {
		if (jsonml.getType() != "array") {
			return false;
		}
		ListIterator<JsonValue> iter = ((JsonArray)jsonml).listIterator();
		JsonValue item = iter.next();
		if (item.getType() != "string") {
			return false;
		}
		while (iter.hasNext()) {
			item = iter.next();
			if (item.getType() == "array" && !validate(item)) {
				return false;
			}
		}
		return true;
	}

	private String element (JsonValue json) {
		if (json.getType() != "array") {
			return "";
		}
		boolean closed = true;
		ListIterator<JsonValue> iter = ((JsonArray)json).listIterator();
		String name = ((JsonString)iter.next()).getValue();
		boolean comment = false;
		if ("!--".equals(name)) comment = true;
		StringBuilder sb = new StringBuilder();
		sb.append("<")
		.append(name);
		if (comment) sb.append(" ");
		JsonValue item;
		while (iter.hasNext()) {
			item = iter.next();
			if (item.getType() == "array") {
				if (closed && !comment) sb.append(">");
				sb.append(element(item));
				closed = false;
			} else {
				if (item.getType() != "object") {
					if (closed && !comment) {
						sb.append(">");
						closed = false;
					}
					sb.append(item.getStringValue());
				} else {
					Set<String> keys = ((JsonObject)item).keySet();
					Iterator<String> iter2 = keys.iterator();
					String key;
					while (iter2.hasNext()) {
						key = iter2.next();
						sb.append(" ")
						.append(key)
						.append("=\"")
						.append(((JsonValue)((JsonObject)item).get(key)).getStringValue())
						.append("\"");
						if (iter2.hasNext()) {
							sb.append(",");
						}
					}
				}
			}	
		}
		if (closed) {
			if (comment) {
				sb.append(" --");
			} else {
				sb.append("/");
			}
			sb.append(">");
		} else {
			sb.append("</")
			.append(name)
			.append(">");
		}
		return sb.toString();
	}

	public String toXml () {
		if (success()) {
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + element(root);
		}
		return null;
	}

	private String stringifyAttr (JsonObject attr) {
		StringBuilder sb = new StringBuilder();
		Set<String> keys = attr.keySet();
		Iterator<String> iter = keys.iterator();
		String key;
		while (iter.hasNext()) {
			key = iter.next();
			sb.append("\"@")
					.append(key)
					.append("\":")
					.append(attr.get(key).toString());
			if (iter.hasNext()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	private String stringifyArray (JsonArray json) {
		StringBuilder sb = new StringBuilder();
		ListIterator<JsonValue> iter = json.listIterator();
		iter.next();
		Hashtable<String, List<String>> table = new Hashtable<>();
		if (json.size() == 1) {
			sb.append("null");
		}
		if (json.size() > 2) {
			sb.append("{");
		}
		JsonValue item;
		while (iter.hasNext()) {
			item = iter.next();
			if (!(item instanceof JsonStructure)) {
				table.computeIfAbsent("\"#text\"", k -> new ArrayList<>());
				table.get("\"#text\"").add(item.getStringValue());
			} else if (item.getType().equals("array")) {
				String key = ((JsonArray)item).get(0).toString();
				table.computeIfAbsent(key, k -> new ArrayList<>());
				if (((JsonArray)item).size() > 2) {
					table.get(key).add(stringifyArray((JsonArray)item));
				} else if (((JsonArray)item).size() == 2) {
					table.get(key).add(((JsonArray)item).get(1).toString());
				} else {
					table.get(key).add("null");
				}
			} else if (item.getType().equals("object")) {
				sb.append(stringifyAttr((JsonObject)item));
				if (iter.hasNext()) {
					sb.append(",");
				}
			}
		}
		Enumeration<String> keys = table.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			sb.append(key)
					.append(":");
			if (table.get(key).size() > 0) {
				if (key.equals("\"#text\"")) {
					sb.append("\"")
							.append(String.join("", table.get(key)))
							.append("\"");
				} else {
					if (table.get(key).size() > 1) sb.append("[");
					ListIterator<String> liter = table.get(key).listIterator();
					while (liter.hasNext()) {
						String lcont = liter.next();
						sb.append(lcont);
						if (liter.hasNext()) {
							sb.append(",");
						}
					}
					if (table.get(key).size() > 1) sb.append("]");
				}
			}
			if (keys.hasMoreElements()) {
				sb.append(",");
			}
		}
		if (json.size() > 2) {
			sb.append("}");
		}
		return sb.toString();
	}

	private String stringify (JsonMl jsonml) {
		StringBuilder sb = new StringBuilder();
		JsonArray json = (JsonArray) jsonml.getJsonValue();
		sb.append("{");
		if (json.size() > 0 && json.get(0).getType().equals("string")) {
			String name = json.get(0).toString();
			sb.append(name)
					.append(":");
		}
		sb.append(stringifyArray(json))
				.append("}");
		return sb.toString();
	}

	public JsonValue getJsonValue() {
		return root;
	}

	public boolean success () {
		return (root != null);
	}

	public String getJsonMlString () {
		return root.toString();
	}

	public String getJsonString () {
		return stringify(this);
	}

	public String toString () {
		if (success()) {
			return root.toString();
		}
		return null;
	}
}



























