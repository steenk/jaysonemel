package com.steenklingberg.json;

import java.util.regex.Pattern;

public class JsonPointer {

	private JsonValue root;
	private static final Pattern number = Pattern.compile("^\\d+$");

	public JsonPointer () {
		this.root = null;
	}

	public JsonPointer (JsonValue json) {
		this.root = json;
	}

	public JsonStructure getRoot() {
		return (JsonStructure)this.root;
	}

	public JsonValue point (String pointer) {
		String[] parts = pointer.split("/");
		JsonValue node = this.root;
		try {
			for (String part : parts) {
				String key = part.replace("~1", "/").replace("~0", "~");
				node = ((JsonStructure) node).getValue(key);
			}
		} catch (Exception e) {
			node = null;
		}

		return node;
	}

	private void resize (JsonStructure struct, int size) {
		if (struct instanceof JsonArray) {
			for (int i = ((JsonArray) struct).size(); i<size; i++) {
				((JsonArray) struct).add(new JsonNull());
			}
		}
	}

	public boolean createOrUpdate (String pointer, JsonValue value) {
		boolean success = false;
		if (!pointer.startsWith("/")) pointer = "/" + pointer;
		String[] parts = pointer.split("/");
		StringBuilder sb = new StringBuilder();
		String part;
		String key = "";
		String type;
		JsonStructure current = null;
		for (int i=1; i<parts.length; i++) {
			part = parts[i];
			type = number.matcher(part).matches() ? "array" : "object";
			sb.append("/");
			String path = sb.toString();
			JsonStructure obj = null;
			JsonValue struct = point(path);
			if (struct instanceof JsonStructure) {
				current = (JsonStructure)struct;
			}
			if (struct == null) {
				if (path.equals("/")) {
					if ("object".equals(type)) {
						this.root = new JsonObject();
					} else {
						int size = Integer.parseInt(part) + 1;
						this.root = new JsonArray(size);
						resize(obj, size);
					}
					current = (JsonStructure)this.root;
				} else {
					if (type.equals("object")) {
						obj = new JsonObject();
					} else {
						int size = Integer.parseInt(part) + 1;
						obj = new JsonArray(size);
						resize(obj, size);
					}
					if (current instanceof JsonObject) {
						((JsonObject)current).setValue(key, (JsonValue)obj);
					} else {
						((JsonArray)current).set(Integer.parseInt(key), (JsonValue) obj);
					}
					current = obj;
				}
			}
			key = part;
			sb.append(part);
		}
		if (current.getType().equals("object")) {
			((JsonObject) current).setValue(key, value);
			success = true;
		} else {
			int i = Integer.parseInt(key);
			resize(current, i + 1);
			((JsonArray)current).set(i, value);
			success = true;
		}
		return success;
	}

	public static void main (String[] args) {
		JsonPointer p = new JsonPointer();
		if (p.createOrUpdate("/apa/banan/1", new JsonString("frukt"))) {
			System.out.println(p.getRoot());
		}
		if (p.createOrUpdate("/apa/dagar/2/3", new JsonString("tisdag"))) {
			System.out.println(p.getRoot());
		}
	}

}