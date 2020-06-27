package steenklingberg.jsonml;

import java.util.ListIterator;
import java.util.Set;
import java.util.Iterator;
import com.steenklingberg.json.JsonParser;
import com.steenklingberg.json.JsonValue;
import com.steenklingberg.json.JsonObject;
import com.steenklingberg.json.JsonArray;
import com.steenklingberg.json.JsonString;

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

	public boolean success () {
		return (root != null);
	}

	public String toString () {
		if (success()) {
			return root.toString();
		}
		return null;
	}
}



























