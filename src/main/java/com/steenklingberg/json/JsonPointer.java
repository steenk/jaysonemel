package com.steenklingberg.json;

public class JsonPointer {

	JsonValue json;

	public JsonPointer (JsonValue json) {
		this.json = json;
	}

	public JsonValue point (String path) {
		String[] parts = path.split("/");
		JsonValue node = this.json;
		for (int i=0; i < parts.length; i++) {
			String key = parts[i].replace("~1", "/").replace("~0", "~");
			node = ((JsonStructure)node).getValue(key);
		}

		return node;
	}
	
}